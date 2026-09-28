package modelo;

/**
 * Estados posibles de un equipo de red.
 * Autor: Kerly
 */
public enum EstadoEquipo {
    DISPONIBLE("Disponible"),
    PRESTADO("Prestado"),
    MANTENIMIENTO("Mantenimiento");

    private final String etiqueta;

    EstadoEquipo(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    @Override
    public String toString() {
        return etiqueta;
    }
}
