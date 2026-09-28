package menus;

import modelo.Solicitud;
import servicios.ColaEsperaService;
import servicios.InventarioService;
import servicios.PrestamoService;
import util.Consola;

/**
 * Submenu de la cola de espera.
 * Autor: Gabriel
 */
public class MenuColaEspera {
    private final ColaEsperaService colaEspera;
    private final InventarioService inventario;
    private final PrestamoService prestamos;

    public MenuColaEspera(ColaEsperaService colaEspera, InventarioService inventario,
                          PrestamoService prestamos) {
        this.colaEspera = colaEspera;
        this.inventario = inventario;
        this.prestamos = prestamos;
    }

    public void mostrar() {
        int opcion;
        do {
            Consola.titulo("COLA DE ESPERA - ALTA DEMANDA (FIFO)");
            System.out.println("1. Encolar solicitud");
            System.out.println("2. Consultar frente");
            System.out.println("3. Atender frente (desencolar si el equipo esta disponible)");
            System.out.println("4. Listar cola");
            System.out.println("0. Regresar");
            opcion = Consola.leerEntero("Opcion: ", 0, 4);
            switch (opcion) {
                case 1 -> {
                    String codigo = Consola.leerTexto("Codigo del equipo solicitado: ");
                    String responsable = Consola.leerTexto("Estudiante o grupo: ");
                    Consola.mostrar(colaEspera.encolar(responsable, inventario.buscar(codigo)));
                }
                case 2 -> {
                    Solicitud frente = colaEspera.consultarFrente();
                    System.out.println(frente == null ? "  La cola esta vacia." : "  Frente: " + frente);
                }
                case 3 -> Consola.mostrar(prestamos.atenderFrenteCola());
                case 4 -> {
                    colaEspera.listar();
                    System.out.println("  Solicitudes en espera: " + colaEspera.tamanio());
                }
                default -> { }
            }
        } while (opcion != 0);
    }
}
