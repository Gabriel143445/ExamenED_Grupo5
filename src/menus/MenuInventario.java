package menus;

import modelo.EquipoRed;
import modelo.EstadoEquipo;
import modelo.TipoEquipo;
import servicios.InventarioService;
import util.Consola;

/**
 * Submenus de inventario y mantenimiento.
 * Autor: Kerly
 */
public class MenuInventario {
    private final InventarioService inventario;

    public MenuInventario(InventarioService inventario) {
        this.inventario = inventario;
    }

    public void mostrar() {
        int opcion;
        do {
            Consola.titulo("INVENTARIO (lista secuencial)");
            System.out.println("1. Mostrar inventario completo");
            System.out.println("2. Registrar equipo");
            System.out.println("3. Buscar equipo por codigo");
            System.out.println("4. Modificar estado");
            System.out.println("5. Eliminar equipo");
            System.out.println("6. Mostrar solo equipos disponibles");
            System.out.println("0. Regresar");
            opcion = Consola.leerEntero("Opcion: ", 0, 6);
            switch (opcion) {
                case 1 -> {
                    System.out.println("  CODIGO | TIPO         | CAPACIDAD  | ESTADO        | CONFIGURACION");
                    inventario.mostrar();
                    System.out.println("  Total de equipos: " + inventario.tamanio());
                }
                case 2 -> registrar();
                case 3 -> buscar();
                case 4 -> modificarEstado();
                case 5 -> eliminar();
                case 6 -> inventario.mostrarPorEstado(EstadoEquipo.DISPONIBLE);
                default -> { }
            }
        } while (opcion != 0);
    }

    public void mostrarMantenimiento() {
        int opcion;
        do {
            Consola.titulo("MANTENIMIENTO");
            System.out.println("1. Listar equipos en mantenimiento");
            System.out.println("2. Enviar equipo a mantenimiento");
            System.out.println("3. Finalizar mantenimiento (volver a Disponible)");
            System.out.println("0. Regresar");
            opcion = Consola.leerEntero("Opcion: ", 0, 3);
            switch (opcion) {
                case 1 -> inventario.mostrarPorEstado(EstadoEquipo.MANTENIMIENTO);
                case 2 -> {
                    String codigo = Consola.leerTexto("Codigo del equipo: ");
                    String motivo = Consola.leerTexto("Motivo: ");
                    Consola.mostrar(inventario.enviarAMantenimiento(codigo, motivo));
                }
                case 3 -> Consola.mostrar(inventario.finalizarMantenimiento(
                        Consola.leerTexto("Codigo del equipo: ")));
                default -> { }
            }
        } while (opcion != 0);
    }

    private void registrar() {
        String codigo = Consola.leerTexto("Codigo (ej. RED010): ");
        System.out.println("Tipo: 1. Router  2. Switch  3. Access Point");
        TipoEquipo tipo = TipoEquipo.values()[Consola.leerEntero("Seleccione: ", 1, 3) - 1];
        int puertos = Consola.leerEntero("Numero de " + tipo.getUnidad() + ": ", 0, 1000);
        Consola.mostrar(inventario.registrar(codigo, tipo, puertos));
    }

    private void buscar() {
        EquipoRed equipo = inventario.buscar(Consola.leerTexto("Codigo: "));
        System.out.println(equipo == null ? "[ERROR] Equipo no encontrado." : "  " + equipo);
    }

    private void modificarEstado() {
        String codigo = Consola.leerTexto("Codigo del equipo: ");
        EquipoRed equipo = inventario.buscar(codigo);
        if (equipo == null) {
            System.out.println("[ERROR] Equipo no encontrado.");
            return;
        }
        System.out.println("Estado actual: " + equipo.getEstado());
        System.out.println("Nuevo estado: 1. Disponible  2. Mantenimiento");
        int opcion = Consola.leerEntero("Seleccione: ", 1, 2);
        EstadoEquipo nuevo = opcion == 1 ? EstadoEquipo.DISPONIBLE : EstadoEquipo.MANTENIMIENTO;
        Consola.mostrar(inventario.modificarEstado(codigo, nuevo));
    }

    private void eliminar() {
        String codigo = Consola.leerTexto("Codigo del equipo a eliminar: ");
        if (inventario.buscar(codigo) == null) {
            System.out.println("[ERROR] Equipo no encontrado.");
            return;
        }
        if (Consola.leerSiNo("Confirma eliminar " + codigo.toUpperCase() + "?")) {
            Consola.mostrar(inventario.eliminar(codigo));
        } else {
            System.out.println("Operacion cancelada.");
        }
    }
}
