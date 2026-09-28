package modelo;

/**
 * Equipo de red del laboratorio (router, switch o access point).
 * Autor: Kerly
 */
public class EquipoRed {
    private final String codigo;
    private final TipoEquipo tipo;
    private final int puertos;
    private EstadoEquipo estado;
    private ConfiguracionLogica configuracion;

    public EquipoRed(String codigo, TipoEquipo tipo, int puertos) {
        this.codigo = codigo;
        this.tipo = tipo;
        this.puertos = puertos;
        this.estado = EstadoEquipo.DISPONIBLE;
        this.configuracion = null;
    }

    public String getCodigo() {
        return codigo;
    }

    public TipoEquipo getTipo() {
        return tipo;
    }

    public int getPuertos() {
        return puertos;
    }

    public EstadoEquipo getEstado() {
        return estado;
    }

    public void setEstado(EstadoEquipo estado) {
        this.estado = estado;
    }

    public ConfiguracionLogica getConfiguracion() {
        return configuracion;
    }

    public void setConfiguracion(ConfiguracionLogica configuracion) {
        this.configuracion = configuracion;
    }

    public boolean tieneCodigo(String otroCodigo) {
        return codigo.equalsIgnoreCase(otroCodigo);
    }

    @Override
    public String toString() {
        String config = configuracion == null ? "sin configurar" : configuracion.toString();
        return String.format("%s | %-12s | %2d %-7s | %-13s | %s",
                codigo, tipo, puertos, tipo.getUnidad(), estado, config);
    }
}
