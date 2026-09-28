package modelo;

/**
 * Turno de uso del banco de pruebas compartido.
 * Autor: Esteban
 */
public class Turno {
    private final String grupo;
    private final String practica;

    public Turno(String grupo, String practica) {
        this.grupo = grupo;
        this.practica = practica;
    }

    public String getGrupo() {
        return grupo;
    }

    public String getPractica() {
        return practica;
    }

    @Override
    public String toString() {
        return grupo + " - " + practica;
    }
}
