package menus;

import servicios.HistorialService;
import util.Consola;

/**
 * Submenu del historial (lista doblemente enlazada).
 * Autor: Esteban
 */
public class MenuHistorial {
    private final HistorialService historial;

    public MenuHistorial(HistorialService historial) {
        this.historial = historial;
    }

    public void mostrar() {
        int opcion;
        do {
            Consola.titulo("HISTORIAL (lista doblemente enlazada)");
            System.out.println("1. Recorrer hacia adelante (cronologico)");
            System.out.println("2. Recorrer hacia atras (mas reciente primero)");
            System.out.println("3. Ultimos N movimientos");
            System.out.println("4. Movimientos de un equipo");
            System.out.println("0. Regresar");
            opcion = Consola.leerEntero("Opcion: ", 0, 4);
            switch (opcion) {
                case 1 -> historial.mostrarCronologico();
                case 2 -> historial.mostrarInverso();
                case 3 -> historial.mostrarUltimos(Consola.leerEntero("Cantidad: ", 1, 1000));
                case 4 -> {
                    int n = historial.mostrarPorEquipo(Consola.leerTexto("Codigo del equipo: "));
                    System.out.println("  Movimientos encontrados: " + n);
                }
                default -> { }
            }
            if (opcion != 0) {
                System.out.println("  Total de movimientos: " + historial.total());
            }
        } while (opcion != 0);
    }
}
