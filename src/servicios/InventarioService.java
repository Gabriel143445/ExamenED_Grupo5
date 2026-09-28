package servicios;

import estructuras.ListaSecuencial;
import modelo.EquipoRed;
import modelo.EstadoEquipo;
import modelo.TipoEquipo;
import modelo.TipoMovimiento;
import util.Resultado;

/**
 * Inventario general de equipos sobre una lista secuencial.
 * Tambien controla el mantenimiento (regla diferenciadora del Grupo 5):
 * un equipo en mantenimiento nunca aparece como disponible y, al entrar
 * a mantenimiento, sus solicitudes salen de la cola de asignacion.
 * Autor: Kerly
 */
public class InventarioService {
    private final ListaSecuencial<EquipoRed> inventario = new ListaSecuencial<>();
    private final HistorialService historial;
    private final ColaEsperaService colaEspera;

    public InventarioService(HistorialService historial, ColaEsperaService colaEspera) {
        this.historial = historial;
        this.colaEspera = colaEspera;
    }

    // ------------------------ Insertar ------------------------

    public Resultado registrar(String codigo, TipoEquipo tipo, int puertos) {
        if (codigo == null || !codigo.trim().toUpperCase().matches("RED\\d{3}")) {
            return Resultado.error("Codigo invalido. Formato esperado: RED + 3 digitos (ej. RED010).");
        }
        String cod = codigo.trim().toUpperCase();
        if (buscar(cod) != null) {
            return Resultado.error("Ya existe un equipo con el codigo " + cod + ".");
        }
        if (tipo == null) {
            return Resultado.error("Debe indicar el tipo de equipo.");
        }
        if (puertos < tipo.getMinimo() || puertos > tipo.getMaximo()) {
            return Resultado.error("Un " + tipo + " debe tener entre " + tipo.getMinimo()
                    + " y " + tipo.getMaximo() + " " + tipo.getUnidad() + ".");
        }
        inventario.insertar(new EquipoRed(cod, tipo, puertos));
        historial.registrar(TipoMovimiento.REGISTRO, cod, "Equipo registrado: " + tipo + ", "
                + puertos + " " + tipo.getUnidad());
        return Resultado.ok("Equipo " + cod + " registrado como Disponible.");
    }

    // ------------------------ Buscar ------------------------

    public EquipoRed buscar(String codigo) {
        if (codigo == null) {
            return null;
        }
        int indice = inventario.buscar(e -> e.tieneCodigo(codigo.trim()));
        return indice == -1 ? null : inventario.obtener(indice);
    }

    /** Primer equipo DISPONIBLE de un tipo (nunca devuelve uno en mantenimiento). */
    public EquipoRed buscarDisponiblePorTipo(TipoEquipo tipo) {
        int indice = inventario.buscar(e -> e.getTipo() == tipo
                && e.getEstado() == EstadoEquipo.DISPONIBLE);
        return indice == -1 ? null : inventario.obtener(indice);
    }

    public boolean ipEnUso(String ip, String excluirCodigo) {
        return inventario.buscar(e -> e.getConfiguracion() != null
                && e.getConfiguracion().getIp().equals(ip)
                && !e.tieneCodigo(excluirCodigo)) != -1;
    }

    // ------------------------ Mostrar ------------------------

    public void mostrar() {
        inventario.mostrar();
    }

    public int mostrarPorEstado(EstadoEquipo estado) {
        int[] contador = {0};
        inventario.recorrer(e -> {
            if (e.getEstado() == estado) {
                System.out.println("  - " + e);
                contador[0]++;
            }
        });
        if (contador[0] == 0) {
            System.out.println("  (no hay equipos en estado " + estado + ")");
        }
        return contador[0];
    }

    public int contarPorEstado(EstadoEquipo estado) {
        return inventario.contar(e -> e.getEstado() == estado);
    }

    public int tamanio() {
        return inventario.tamanio();
    }

    // ------------------------ Modificar estado ------------------------

    /**
     * Cambio manual de estado. PRESTADO solo se asigna mediante un
     * prestamo; un equipo prestado debe devolverse antes de cambiarlo.
     */
    public Resultado modificarEstado(String codigo, EstadoEquipo nuevo) {
        EquipoRed equipo = buscar(codigo);
        if (equipo == null) {
            return Resultado.error("Equipo no encontrado.");
        }
        if (nuevo == EstadoEquipo.PRESTADO) {
            return Resultado.error("El estado Prestado solo se asigna registrando un prestamo.");
        }
        if (equipo.getEstado() == nuevo) {
            return Resultado.error(equipo.getCodigo() + " ya se encuentra en estado " + nuevo + ".");
        }
        if (nuevo == EstadoEquipo.MANTENIMIENTO) {
            return enviarAMantenimiento(codigo, "cambio manual de estado");
        }
        return finalizarMantenimiento(codigo);
    }

    public Resultado enviarAMantenimiento(String codigo, String motivo) {
        EquipoRed equipo = buscar(codigo);
        if (equipo == null) {
            return Resultado.error("Equipo no encontrado.");
        }
        if (equipo.getEstado() == EstadoEquipo.MANTENIMIENTO) {
            return Resultado.error(equipo.getCodigo() + " ya esta en mantenimiento.");
        }
        if (equipo.getEstado() == EstadoEquipo.PRESTADO) {
            return Resultado.error(equipo.getCodigo()
                    + " esta prestado. Registre la devolucion (con falla) para enviarlo a mantenimiento.");
        }
        return aplicarMantenimiento(equipo, motivo);
    }

    /** Uso interno (por ejemplo, devolucion con falla). */
    Resultado aplicarMantenimiento(EquipoRed equipo, String motivo) {
        equipo.setEstado(EstadoEquipo.MANTENIMIENTO);
        historial.registrar(TipoMovimiento.MANTENIMIENTO, equipo.getCodigo(),
                "Ingresa a mantenimiento: " + motivo);
        int retiradas = colaEspera.depurarEquipo(equipo.getCodigo(), "equipo en mantenimiento");
        String extra = retiradas > 0 ? " Se retiraron " + retiradas + " solicitud(es) de la cola." : "";
        return Resultado.ok(equipo.getCodigo() + " enviado a mantenimiento." + extra);
    }

    public Resultado finalizarMantenimiento(String codigo) {
        EquipoRed equipo = buscar(codigo);
        if (equipo == null) {
            return Resultado.error("Equipo no encontrado.");
        }
        if (equipo.getEstado() != EstadoEquipo.MANTENIMIENTO) {
            return Resultado.error(equipo.getCodigo() + " no esta en mantenimiento.");
        }
        equipo.setEstado(EstadoEquipo.DISPONIBLE);
        historial.registrar(TipoMovimiento.MANTENIMIENTO, equipo.getCodigo(),
                "Sale de mantenimiento: vuelve a Disponible");
        return Resultado.ok(equipo.getCodigo() + " salio de mantenimiento y esta Disponible.");
    }

    /** Uso interno de PrestamoService al prestar o devolver. */
    void actualizarEstado(EquipoRed equipo, EstadoEquipo estado) {
        equipo.setEstado(estado);
    }

    // ------------------------ Eliminar ------------------------

    public Resultado eliminar(String codigo) {
        EquipoRed equipo = buscar(codigo);
        if (equipo == null) {
            return Resultado.error("Equipo no encontrado.");
        }
        if (equipo.getEstado() == EstadoEquipo.PRESTADO) {
            return Resultado.error("No se puede eliminar " + equipo.getCodigo()
                    + " porque tiene un prestamo activo.");
        }
        colaEspera.depurarEquipo(equipo.getCodigo(), "equipo eliminado del inventario");
        inventario.eliminar(e -> e.tieneCodigo(codigo.trim()));
        historial.registrar(TipoMovimiento.ELIMINACION, equipo.getCodigo(), "Equipo eliminado del inventario");
        return Resultado.ok("Equipo " + equipo.getCodigo() + " eliminado del inventario.");
    }
}
