import datos.DatosPrueba;
import datos.PruebasSistema;
import menus.MenuColaEspera;
import menus.MenuConfiguracion;
import menus.MenuHistorial;
import menus.MenuInventario;
import menus.MenuPrestamos;
import menus.MenuTurnos;
import modelo.EstadoEquipo;
import modelo.Turno;
import servicios.ColaEsperaService;
import servicios.ConfiguracionService;
import servicios.HistorialService;
import servicios.InventarioService;
import servicios.PrestamoService;
import servicios.TurnoService;
import util.Consola;

/**
 * Sistema de equipos de redes y routers para practicas de networking.
 * Grupo 5 - Estructura de Datos.
 * Punto de entrada: crea los servicios, carga datos y muestra el menu.
 * Autor: Rommel (integracion)
 */
public class Main {

    public static void main(String[] args) {
        // Orden de creacion segun dependencias
        HistorialService historial = new HistorialService();
        ColaEsperaService cola = new ColaEsperaService(historial);
        InventarioService inventario = new InventarioService(historial, cola);
        PrestamoService prestamos = new PrestamoService(inventario, cola, historial);
        ConfiguracionService configuracion = new ConfiguracionService(inventario, historial);
        TurnoService turnos = new TurnoService(historial);

        DatosPrueba.cargar(inventario, prestamos, cola, configuracion, turnos, historial);

        MenuInventario menuInventario = new MenuInventario(inventario);
        MenuPrestamos menuPrestamos = new MenuPrestamos(prestamos, inventario, cola);
        MenuColaEspera menuCola = new MenuColaEspera(cola, inventario, prestamos);
        MenuHistorial menuHistorial = new MenuHistorial(historial);
        MenuTurnos menuTurnos = new MenuTurnos(turnos);
        MenuConfiguracion menuConfiguracion = new MenuConfiguracion(configuracion, inventario);

        int opcion;
        do {
            System.out.println();
            System.out.println("==========================================================");
            System.out.println("   SISTEMA DE EQUIPOS DE REDES Y ROUTERS - GRUPO 5");
            System.out.println("   Estructura de Datos - Java");
            System.out.println("==========================================================");
            System.out.println(" 1. Inventario de equipos            (lista secuencial)");
            System.out.println(" 2. Prestamos y devoluciones         (lista simple)");
            System.out.println(" 3. Cola de espera alta demanda      (cola FIFO)");
            System.out.println(" 4. Historial de movimientos         (lista doble)");
            System.out.println(" 5. Turnos del banco de pruebas      (lista circular)");
            System.out.println(" 6. Mantenimiento");
            System.out.println(" 7. Configuracion logica             (pila)");
            System.out.println(" 8. Deshacer ultimo cambio de configuracion");
            System.out.println(" 9. Resumen del sistema");
            System.out.println("10. Ejecutar casos de prueba automaticos");
            System.out.println(" 0. Salir");
            System.out.println("==========================================================");
            opcion = Consola.leerEntero("Seleccione una opcion: ", 0, 10);

            switch (opcion) {
                case 1 -> menuInventario.mostrar();
                case 2 -> menuPrestamos.mostrar();
                case 3 -> menuCola.mostrar();
                case 4 -> menuHistorial.mostrar();
                case 5 -> menuTurnos.mostrar();
                case 6 -> menuInventario.mostrarMantenimiento();
                case 7 -> menuConfiguracion.mostrar();
                case 8 -> menuConfiguracion.deshacer();
                case 9 -> mostrarResumen(inventario, prestamos, cola, configuracion, turnos, historial);
                case 10 -> PruebasSistema.ejecutar();
                case 0 -> System.out.println("Sistema finalizado. Hasta pronto.");
                default -> { }
            }
        } while (opcion != 0);
    }

    private static void mostrarResumen(InventarioService inventario, PrestamoService prestamos,
                                       ColaEsperaService cola, ConfiguracionService configuracion,
                                       TurnoService turnos, HistorialService historial) {
        Consola.titulo("RESUMEN DEL SISTEMA");
        System.out.println("  Equipos en inventario:      " + inventario.tamanio());
        System.out.println("    Disponibles:              " + inventario.contarPorEstado(EstadoEquipo.DISPONIBLE));
        System.out.println("    Prestados:                " + inventario.contarPorEstado(EstadoEquipo.PRESTADO));
        System.out.println("    En mantenimiento:         " + inventario.contarPorEstado(EstadoEquipo.MANTENIMIENTO));
        System.out.println("  Prestamos activos:          " + prestamos.cantidad());
        System.out.println("  Solicitudes en cola:        " + cola.tamanio());
        System.out.println("  Cambios en pila (deshacer): " + configuracion.cambiosPendientes());
        Turno actual = turnos.consultarActual();
        System.out.println("  Turno actual del banco:     " + (actual == null ? "ninguno" : actual));
        System.out.println("  Movimientos en historial:   " + historial.total());
    }
}
