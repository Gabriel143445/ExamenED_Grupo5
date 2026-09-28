package menus;

import modelo.CambioConfiguracion;
import modelo.EquipoRed;
import servicios.ConfiguracionService;
import servicios.InventarioService;
import util.Consola;

/**
 * Submenu de configuracion logica y deshacer (pila).
 * Autor: Gabriel
 */
public class MenuConfiguracion {
    private final ConfiguracionService configuracion;
    private final InventarioService inventario;

    public MenuConfiguracion(ConfiguracionService configuracion, InventarioService inventario) {
        this.configuracion = configuracion;
        this.inventario = inventario;
    }

    public void mostrar() {
        int opcion;
        do {
            Consola.titulo("CONFIGURACION LOGICA (pila de cambios)");
            System.out.println("1. Ver configuracion de un equipo");
            System.out.println("2. Aplicar nueva configuracion");
            System.out.println("3. Consultar ultimo cambio (tope)");
            System.out.println("4. Ver pila de cambios");
            System.out.println("5. Deshacer ultimo cambio");
            System.out.println("0. Regresar");
            opcion = Consola.leerEntero("Opcion: ", 0, 5);
            switch (opcion) {
                case 1 -> verConfiguracion();
                case 2 -> aplicar();
                case 3 -> {
                    CambioConfiguracion tope = configuracion.consultarUltimo();
                    System.out.println(tope == null ? "  No hay cambios registrados." : "  Tope: " + tope);
                }
                case 4 -> {
                    configuracion.mostrarPila();
                    System.out.println("  Cambios en la pila: " + configuracion.cambiosPendientes());
                }
                case 5 -> deshacer();
                default -> { }
            }
        } while (opcion != 0);
    }

    /** Accesible tambien desde el menu principal. */
    public void deshacer() {
        CambioConfiguracion tope = configuracion.consultarUltimo();
        if (tope == null) {
            System.out.println("[ERROR] No existen cambios de configuracion para revertir.");
            return;
        }
        System.out.println("  Se revertira: " + tope);
        if (Consola.leerSiNo("Confirmar deshacer")) {
            Consola.mostrar(configuracion.revertirUltimo());
        }
    }

    private void verConfiguracion() {
        EquipoRed equipo = inventario.buscar(Consola.leerTexto("Codigo del equipo: "));
        if (equipo == null) {
            System.out.println("[ERROR] Equipo no encontrado.");
            return;
        }
        System.out.println("  " + equipo.getCodigo() + ": "
                + (equipo.getConfiguracion() == null ? "sin configurar" : equipo.getConfiguracion()));
    }

    private void aplicar() {
        String codigo = Consola.leerTexto("Codigo del equipo: ");
        EquipoRed equipo = inventario.buscar(codigo);
        if (equipo == null) {
            System.out.println("[ERROR] Equipo no encontrado.");
            return;
        }
        System.out.println("  Actual: " + (equipo.getConfiguracion() == null ? "sin configurar"
                : equipo.getConfiguracion()));
        String hostname = Consola.leerTexto("Hostname: ");
        String ip = Consola.leerTexto("Direccion IP: ");
        int prefijo = Consola.leerEntero("Prefijo (8-30): ", 0, 32);
        int vlan = Consola.leerEntero("VLAN (1-4094): ", 0, 5000);
        Consola.mostrar(configuracion.aplicar(codigo, hostname, ip, prefijo, vlan));
    }
}
