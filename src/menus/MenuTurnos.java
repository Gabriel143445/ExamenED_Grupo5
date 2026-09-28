package menus;

import servicios.TurnoService;
import util.Consola;

/**
 * Submenu de turnos del banco de pruebas (lista circular).
 * Autor: Esteban
 */
public class MenuTurnos {
    private final TurnoService turnos;

    public MenuTurnos(TurnoService turnos) {
        this.turnos = turnos;
    }

    public void mostrar() {
        int opcion;
        do {
            Consola.titulo("TURNOS DEL BANCO DE PRUEBAS (lista circular)");
            System.out.println("1. Mostrar ronda");
            System.out.println("2. Agregar turno");
            System.out.println("3. Avanzar turno");
            System.out.println("4. Eliminar turno actual");
            System.out.println("5. Simular N avances");
            System.out.println("0. Regresar");
            opcion = Consola.leerEntero("Opcion: ", 0, 5);
            switch (opcion) {
                case 1 -> turnos.mostrarRonda();
                case 2 -> {
                    String grupo = Consola.leerTexto("Grupo: ");
                    String practica = Consola.leerTexto("Practica: ");
                    Consola.mostrar(turnos.agregar(grupo, practica));
                }
                case 3 -> Consola.mostrar(turnos.avanzar());
                case 4 -> Consola.mostrar(turnos.eliminarActual());
                case 5 -> turnos.simular(Consola.leerEntero("Numero de avances: ", 1, 50));
                default -> { }
            }
        } while (opcion != 0);
    }
}
