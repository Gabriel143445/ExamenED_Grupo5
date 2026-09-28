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
 * Para extraer o depurar solo se usan encolar/desencolar: se rota la
 * cola una vuelta completa, por lo que el orden de llegada se conserva.
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
     * Rota la cola una vuelta: O(n) y conserva el orden del resto.
     */
    public Solicitud extraerPrimeraPara(String codigoEquipo) {
        int n = cola.tamanio();
        Solicitud encontrada = null;
        for (int i = 0; i < n; i++) {
            Solicitud s = cola.desencolar();
            if (encontrada == null && s.getCodigoEquipo().equalsIgnoreCase(codigoEquipo)) {
                encontrada = s;
            } else {
                cola.encolar(s);
            }
        }
        if (encontrada != null) {
            historial.registrar(TipoMovimiento.COLA, codigoEquipo,
                    "Solicitud atendida de " + encontrada.getResponsable());
        }
        return encontrada;
    }

    /**
     * Retira todas las solicitudes de un equipo (por ejemplo, cuando
     * pasa a mantenimiento). Devuelve cuantas se retiraron.
     */
    public int depurarEquipo(String codigoEquipo, String motivo) {
        int n = cola.tamanio();
        int retiradas = 0;
        for (int i = 0; i < n; i++) {
            Solicitud s = cola.desencolar();
            if (s.getCodigoEquipo().equalsIgnoreCase(codigoEquipo)) {
                retiradas++;
                historial.registrar(TipoMovimiento.COLA, codigoEquipo,
                        "Solicitud de " + s.getResponsable() + " retirada: " + motivo);
            } else {
                cola.encolar(s);
            }
        }
        return retiradas;
    }

    public boolean yaEspera(String responsable, String codigoEquipo) {
        boolean[] existe = {false};
        cola.recorrer(s -> {
            if (s.getResponsable().equalsIgnoreCase(responsable.trim())
                    && s.getCodigoEquipo().equalsIgnoreCase(codigoEquipo)) {
                existe[0] = true;
            }
        });
        return existe[0];
    }

    public int contarPara(String codigoEquipo) {
        int[] contador = {0};
        cola.recorrer(s -> {
            if (s.getCodigoEquipo().equalsIgnoreCase(codigoEquipo)) {
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
