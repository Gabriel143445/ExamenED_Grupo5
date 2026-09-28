package util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utilidad para obtener la fecha y hora actual formateada.
 * Autor: Rommel
 */
public final class Fechas {
    private static final DateTimeFormatter FORMATO =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private Fechas() {
    }

    public static String ahora() {
        return LocalDateTime.now().format(FORMATO);
    }
}
