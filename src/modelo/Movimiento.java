package modelo;

/**
 * Movimiento del historial (lista doble).
 * Autor: Esteban
 */
public class Movimiento {
    private final String fecha;
    private final TipoMovimiento tipo;
    private final String codigoEquipo;
    private final String detalle;

    public Movimiento(String fecha, TipoMovimiento tipo, String codigoEquipo, String detalle) {
        this.fecha = fecha;
        this.tipo = tipo;
        this.codigoEquipo = codigoEquipo;
        this.detalle = detalle;
    }

    public TipoMovimiento getTipo() {
        return tipo;
    }

    public String getCodigoEquipo() {
        return codigoEquipo;
    }

    public String getDetalle() {
        return detalle;
    }

    @Override
    public String toString() {
        return String.format("[%s] %-13s %-6s %s", fecha, tipo, codigoEquipo, detalle);
    }
}
