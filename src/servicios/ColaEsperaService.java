package servicios;

import estructuras.Cola;
import modelo.EquipoRed;
import modelo.EstadoEquipo;
import modelo.Solicitud;
import modelo.TipoMovimiento;
import util.Fechas;
import util.Resultado;

/**
 * Cola de espera (FIFO) para equipos de alta demanda.
 *
 * REGLA GRUPO 5: un equipo en mantenimiento no puede ingresar a la
 * cola de asignacion. Se valida al encolar y, si un equipo pasa a
 * mantenimiento, sus solicitudes se retiran de la cola (depurar).
 *
 * La cola retira nodos coincidentes sin alterar el orden del resto.
 * Autor: Gabriel
 */
public class ColaEsperaService {
    private final Cola<Solicitud> cola = new Cola<>();
    private final HistorialService historial;

    public ColaEsperaService(HistorialService historial) {
        this.historial = historial;
    }

    public Resultado encolar(String responsable, EquipoRed equipo) {
        if (equipo == null) {
            return Resultado.error("El equipo no existe.");
        }
        if (responsable == null || responsable.trim().length() < 3) {
            return Resultado.error("El responsable debe tener al menos 3 caracteres.");
        }
        if (equipo.getEstado() == EstadoEquipo.MANTENIMIENTO) {
            return Resultado.error("REGLA GRUPO 5: " + equipo.getCodigo()
                    + " esta en mantenimiento y no puede ingresar a la cola de asignacion.");
        }
        if (equipo.getEstado() == EstadoEquipo.DISPONIBLE) {
            return Resultado.error(equipo.getCodigo()
                    + " esta disponible: registre el prestamo directamente.");
        }
        if (yaEspera(responsable, equipo.getCodigo())) {
            return Resultado.error(responsable + " ya esta en espera de " + equipo.getCodigo() + ".");
        }

        cola.encolar(new Solicitud(responsable.trim(), equipo.getCodigo(), Fechas.ahora()));
        historial.registrar(TipoMovimiento.COLA, equipo.getCodigo(),
                "Solicitud encolada de " + responsable.trim());
        return Resultado.ok("Solicitud agregada a la cola. Posicion: " + cola.tamanio()
                + " | En espera de " + equipo.getCodigo() + ": " + contarPara(equipo.getCodigo()));
    }

    public Solicitud consultarFrente() {
        return cola.consultarFrente();
    }

    /** Atiende estrictamente el frente (FIFO). */
    public Solicitud desencolar() {
        Solicitud atendida = cola.desencolar();
        if (atendida != null) {
            historial.registrar(TipoMovimiento.COLA, atendida.getCodigoEquipo(),
                    "Solicitud desencolada de " + atendida.getResponsable());
        }
        return atendida;
    }

    /**
     * Extrae la solicitud mas antigua para un equipo concreto.
     * Recorre una vez: O(n) y conserva el orden del resto.
     */
    public Solicitud extraerPrimeraPara(String codigoEquipo) {
        if (codigoEquipo == null || codigoEquipo.isBlank()) {
            return null;
        }
        Solicitud encontrada = cola.extraerPrimero(
                s -> s.getCodigoEquipo().equalsIgnoreCase(codigoEquipo.trim()));
        if (encontrada != null) {
            historial.registrar(TipoMovimiento.COLA, encontrada.getCodigoEquipo(),
                    "Solicitud atendida de " + encontrada.getResponsable());
        }
        return encontrada;
    }

    /**
     * Retira todas las solicitudes de un equipo (por ejemplo, cuando
     * pasa a mantenimiento). Devuelve cuantas se retiraron.
     */
    public int depurarEquipo(String codigoEquipo, String motivo) {
        if (codigoEquipo == null || codigoEquipo.isBlank()) {
            return 0;
        }
        String clave = codigoEquipo.trim();
        return cola.eliminarSi(s -> s.getCodigoEquipo().equalsIgnoreCase(clave),
                s -> historial.registrar(TipoMovimiento.COLA, s.getCodigoEquipo(),
                        "Solicitud de " + s.getResponsable() + " retirada: " + motivo));
    }

    public boolean yaEspera(String responsable, String codigoEquipo) {
        if (responsable == null || responsable.isBlank()
                || codigoEquipo == null || codigoEquipo.isBlank()) {
            return false;
        }
        boolean[] existe = {false};
        cola.recorrer(s -> {
            if (s.getResponsable().equalsIgnoreCase(responsable.trim())
                    && s.getCodigoEquipo().equalsIgnoreCase(codigoEquipo.trim())) {
                existe[0] = true;
            }
        });
        return existe[0];
    }

    public int contarPara(String codigoEquipo) {
        if (codigoEquipo == null || codigoEquipo.isBlank()) {
            return 0;
        }
        int[] contador = {0};
        cola.recorrer(s -> {
            if (s.getCodigoEquipo().equalsIgnoreCase(codigoEquipo.trim())) {
                contador[0]++;
            }
        });
        return contador[0];
    }

    public void listar() {
        cola.mostrar();
    }

    public int tamanio() {
        return cola.tamanio();
    }

    public boolean estaVacia() {
        return cola.estaVacia();
    }
}
