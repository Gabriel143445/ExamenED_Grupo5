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
        this.responsable = responsable;
        this.codigoEquipo = codigoEquipo;
        this.fecha = fecha;
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
