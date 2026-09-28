package servicios;

import estructuras.Pila;
import modelo.CambioConfiguracion;
import modelo.ConfiguracionLogica;
import modelo.EquipoRed;
import modelo.TipoMovimiento;
import util.Fechas;
import util.Resultado;

/**
 * Cambios de configuracion logica (hostname, IP, prefijo, VLAN).
 * Cada cambio se apila; deshacer desapila el ultimo y restaura la
 * configuracion anterior (LIFO).
 * Autor: Gabriel
 */
public class ConfiguracionService {
    private final Pila<CambioConfiguracion> pilaCambios = new Pila<>();
    private final InventarioService inventario;
    private final HistorialService historial;

    public ConfiguracionService(InventarioService inventario, HistorialService historial) {
        this.inventario = inventario;
        this.historial = historial;
    }

    public Resultado aplicar(String codigo, String hostname, String ip, int prefijo, int vlan) {
        EquipoRed equipo = inventario.buscar(codigo);
        if (equipo == null) {
            return Resultado.error("Equipo no encontrado.");
        }
        if (!ConfiguracionLogica.esHostnameValido(hostname)) {
            return Resultado.error("Hostname invalido (inicia con letra; letras, numeros o guion; max 20).");
        }
        if (!ConfiguracionLogica.esIpValida(ip)) {
            return Resultado.error("IP invalida. Use formato IPv4 (ej. 192.168.10.1).");
        }
        if (!ConfiguracionLogica.esPrefijoValido(prefijo)) {
            return Resultado.error("Prefijo invalido. Debe estar entre /8 y /30.");
        }
        if (!ConfiguracionLogica.esIpDeHost(ip, prefijo)) {
            return Resultado.error("La IP " + ip + "/" + prefijo
                    + " es direccion de red o de broadcast; no se puede asignar a un equipo.");
        }
        if (!ConfiguracionLogica.esVlanValida(vlan)) {
            return Resultado.error("VLAN invalida. Debe estar entre 1 y 4094.");
        }
        if (inventario.ipEnUso(ip, equipo.getCodigo())) {
            return Resultado.error("La IP " + ip + " ya esta asignada a otro equipo.");
        }

        ConfiguracionLogica anterior = equipo.getConfiguracion();
        ConfiguracionLogica nueva = new ConfiguracionLogica(hostname, ip, prefijo, vlan);
        if (nueva.mismosValores(anterior)) {
            return Resultado.error("La configuracion ingresada es igual a la actual.");
        }

        equipo.setConfiguracion(nueva);
        pilaCambios.apilar(new CambioConfiguracion(equipo.getCodigo(), anterior, nueva, Fechas.ahora()));
        historial.registrar(TipoMovimiento.CONFIGURACION, equipo.getCodigo(), "Nueva configuracion: " + nueva);
        return Resultado.ok("Configuracion aplicada a " + equipo.getCodigo() + ": " + nueva);
    }

    /**
     * Configuracion de la carga inicial: se valida igual que un cambio
     * normal, pero no queda en la pila (no se puede deshacer).
     */
    public Resultado aplicarInicial(String codigo, String hostname, String ip, int prefijo, int vlan) {
        Resultado r = aplicar(codigo, hostname, ip, prefijo, vlan);
        if (r.isExito()) {
            pilaCambios.desapilar();
        }
        return r;
    }

    /** Deshacer: desapila el ultimo cambio y restaura la configuracion anterior. */
    public Resultado revertirUltimo() {
        CambioConfiguracion cambio = pilaCambios.desapilar();
        if (cambio == null) {
            return Resultado.error("No existen cambios de configuracion para revertir.");
        }
        EquipoRed equipo = inventario.buscar(cambio.getCodigoEquipo());
        if (equipo == null) {
            return Resultado.error("El equipo " + cambio.getCodigoEquipo()
                    + " ya no existe; el cambio fue descartado de la pila.");
        }
        ConfiguracionLogica anterior = cambio.getAnterior();
        if (anterior != null && inventario.ipEnUso(anterior.getIp(), equipo.getCodigo())) {
            pilaCambios.apilar(cambio);
            return Resultado.error("No se puede restaurar: la IP " + anterior.getIp()
                    + " ahora la usa otro equipo. Revierta primero ese cambio.");
        }
        equipo.setConfiguracion(anterior);
        String restaurada = anterior == null ? "sin configurar" : anterior.toString();
        historial.registrar(TipoMovimiento.DESHACER, equipo.getCodigo(), "Configuracion restaurada: " + restaurada);
        return Resultado.ok("Cambio revertido en " + equipo.getCodigo() + ". Configuracion actual: " + restaurada);
    }

    public CambioConfiguracion consultarUltimo() {
        return pilaCambios.consultarTope();
    }

    public void mostrarPila() {
        pilaCambios.mostrar();
    }

    public int cambiosPendientes() {
        return pilaCambios.tamanio();
    }
}
