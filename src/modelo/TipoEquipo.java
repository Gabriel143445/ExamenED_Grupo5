package modelo;

/**
 * Tipos de equipo del laboratorio de networking.
 * Cada tipo define su unidad (puertos o antenas) y su rango valido.
 * Autor: Kerly
 */
public enum TipoEquipo {
    ROUTER("Router", "puertos", 1, 16),
    SWITCH("Switch", "puertos", 4, 52),
    ACCESS_POINT("Access Point", "antenas", 1, 8);

    private final String nombre;
    private final String unidad;
    private final int minimo;
    private final int maximo;

    TipoEquipo(String nombre, String unidad, int minimo, int maximo) {
        this.nombre = nombre;
        this.unidad = unidad;
        this.minimo = minimo;
        this.maximo = maximo;
    }

    public String getUnidad() {
        return unidad;
    }

    public int getMinimo() {
        return minimo;
    }

    public int getMaximo() {
        return maximo;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
