package modelo;

/**
 * Registro de un cambio de configuracion logica. Guarda la
 * configuracion anterior para poder restaurarla con la pila.
 * Autor: Gabriel
 */
public class CambioConfiguracion {
    private final String codigoEquipo;
    private final ConfiguracionLogica anterior;
    private final ConfiguracionLogica nueva;
    private final String fecha;

    public CambioConfiguracion(String codigoEquipo, ConfiguracionLogica anterior,
                               ConfiguracionLogica nueva, String fecha) {
        this.codigoEquipo = codigoEquipo;
        this.anterior = anterior;
        this.nueva = nueva;
        this.fecha = fecha;
    }

    public String getCodigoEquipo() {
        return codigoEquipo;
    }

    public ConfiguracionLogica getAnterior() {
        return anterior;
    }

    public ConfiguracionLogica getNueva() {
        return nueva;
    }

    @Override
    public String toString() {
        String antes = anterior == null ? "sin configurar" : anterior.getHostname() + " " + anterior.getIp()
                + "/" + anterior.getPrefijo() + " VLAN " + anterior.getVlan();
        String despues = nueva.getHostname() + " " + nueva.getIp() + "/" + nueva.getPrefijo()
                + " VLAN " + nueva.getVlan();
        return "[" + fecha + "] " + codigoEquipo + ": " + antes + "  ->  " + despues;
    }
}
