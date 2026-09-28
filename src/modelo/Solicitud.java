package modelo;

/**
 * Solicitud en espera de un equipo que esta prestado (alta demanda).
 * Autor: Gabriel
 */
public class Solicitud {
    private final String responsable;
    private final String codigoEquipo;
    private final String fecha;

    public Solicitud(String responsable, String codigoEquipo, String fecha) {
        if (responsable == null || responsable.isBlank() || codigoEquipo == null
                || codigoEquipo.isBlank() || fecha == null || fecha.isBlank()) {
            throw new IllegalArgumentException("La solicitud requiere responsable, equipo y fecha.");
        }
        this.responsable = responsable.trim();
        this.codigoEquipo = codigoEquipo.trim().toUpperCase();
        this.fecha = fecha.trim();
    }

    public String getResponsable() {
        return responsable;
    }

    public String getCodigoEquipo() {
        return codigoEquipo;
    }

    public String getFecha() {
        return fecha;
    }

    @Override
    public String toString() {
        return responsable + " espera " + codigoEquipo + " (desde " + fecha + ")";
    }
}
