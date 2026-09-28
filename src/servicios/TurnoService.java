package servicios;

import estructuras.ListaCircular;
import modelo.TipoMovimiento;
import modelo.Turno;
import util.Resultado;

/**
 * Turnos rotativos del banco de pruebas compartido (lista circular).
 * Al llegar al ultimo grupo, el turno vuelve al primero sin reiniciar.
 * Autor: Esteban
 */
public class TurnoService {
    private static final String BANCO = "BANCO";
    private final ListaCircular<Turno> turnos = new ListaCircular<>();
    private final HistorialService historial;

    public TurnoService(HistorialService historial) {
        this.historial = historial;
    }

    public Resultado agregar(String grupo, String practica) {
        if (grupo == null || grupo.trim().length() < 3) {
            return Resultado.error("El nombre del grupo debe tener al menos 3 caracteres.");
        }
        if (practica == null || practica.trim().isEmpty()) {
            return Resultado.error("Debe indicar la practica a realizar.");
        }
        if (turnos.existe(t -> t.getGrupo().equalsIgnoreCase(grupo.trim()))) {
            return Resultado.error(grupo.trim() + " ya tiene un turno en la ronda.");
        }
        turnos.insertar(new Turno(grupo.trim(), practica.trim()));
        historial.registrar(TipoMovimiento.TURNO, BANCO, "Turno agregado: " + grupo.trim());
        return Resultado.ok("Turno agregado al final de la ronda. Turnos en ronda: " + turnos.tamanio());
    }

    public Resultado avanzar() {
        if (turnos.estaVacia()) {
            return Resultado.error("No hay turnos registrados.");
        }
        Turno anterior = turnos.consultarActual();
        Turno nuevo = turnos.avanzar();
        historial.registrar(TipoMovimiento.TURNO, BANCO, "Turno pasa de " + anterior.getGrupo()
                + " a " + nuevo.getGrupo());
        return Resultado.ok("Turno actual: " + nuevo);
    }

    public Resultado eliminarActual() {
        Turno eliminado = turnos.eliminarActual();
        if (eliminado == null) {
            return Resultado.error("No hay turnos registrados.");
        }
        historial.registrar(TipoMovimiento.TURNO, BANCO, "Turno eliminado: " + eliminado.getGrupo());
        Turno actual = turnos.consultarActual();
        return Resultado.ok("Turno eliminado: " + eliminado.getGrupo()
                + (actual == null ? ". La ronda quedo vacia." : ". Ahora le toca a " + actual.getGrupo()));
    }

    /** Muestra N avances consecutivos para evidenciar la circularidad. */
    public void simular(int pasos) {
        if (turnos.estaVacia()) {
            System.out.println("  (no hay turnos registrados)");
            return;
        }
        System.out.println("  Inicio: " + turnos.consultarActual());
        for (int i = 1; i <= pasos; i++) {
            System.out.println("  Paso " + i + ": " + turnos.avanzar());
        }
        historial.registrar(TipoMovimiento.TURNO, BANCO, "Simulacion de " + pasos + " avances de turno");
    }

    public Turno consultarActual() {
        return turnos.consultarActual();
    }

    public void mostrarRonda() {
        turnos.mostrarRonda();
    }

    public int cantidad() {
        return turnos.tamanio();
    }
}
