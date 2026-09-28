package util;

/**
 * Respuesta de una operacion de negocio: exito/error + mensaje.
 * Permite que los servicios no impriman directamente y que el menu
 * decida como mostrar la informacion.
 * Autor: Rommel
 */
public final class Resultado {
    private final boolean exito;
    private final String mensaje;

    private Resultado(boolean exito, String mensaje) {
        this.exito = exito;
        this.mensaje = mensaje;
    }

    public static Resultado ok(String mensaje) {
        return new Resultado(true, mensaje);
    }

    public static Resultado error(String mensaje) {
        return new Resultado(false, mensaje);
    }

    public boolean isExito() {
        return exito;
    }

    public String getMensaje() {
        return mensaje;
    }

    @Override
    public String toString() {
        return (exito ? "[OK] " : "[ERROR] ") + mensaje;
    }
}
