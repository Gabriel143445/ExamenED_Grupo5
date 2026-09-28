package util;

import java.util.Scanner;

/**
 * Lectura validada de datos por consola.
 * Ninguna entrada invalida detiene el programa.
 * Autor: Rommel
 */
public final class Consola {
    private static final Scanner SC = new Scanner(System.in);

    private Consola() {
    }

    private static String leerLinea() {
        if (!SC.hasNextLine()) {
            System.out.println("\nEntrada finalizada. Cerrando el sistema.");
            System.exit(0);
        }
        return SC.nextLine().trim();
    }

    /** Lee un texto no vacio. */
    public static String leerTexto(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = leerLinea();
            if (!texto.isEmpty()) {
                return texto;
            }
            System.out.println("  El valor no puede estar vacio.");
        }
    }

    /** Lee un entero dentro del rango [min, max]. */
    public static int leerEntero(String mensaje, int min, int max) {
        while (true) {
            System.out.print(mensaje);
            String texto = leerLinea();
            try {
                int numero = Integer.parseInt(texto);
                if (numero >= min && numero <= max) {
                    return numero;
                }
                System.out.println("  Ingrese un valor entre " + min + " y " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("  Ingrese un numero entero valido.");
            }
        }
    }

    /** Lee una respuesta s/n. */
    public static boolean leerSiNo(String mensaje) {
        while (true) {
            System.out.print(mensaje + " (s/n): ");
            String texto = leerLinea().toLowerCase();
            if (texto.equals("s") || texto.equals("si")) {
                return true;
            }
            if (texto.equals("n") || texto.equals("no")) {
                return false;
            }
            System.out.println("  Responda 's' o 'n'.");
        }
    }

    public static void titulo(String texto) {
        System.out.println();
        System.out.println("---------------- " + texto + " ----------------");
    }

    public static void mostrar(Resultado resultado) {
        System.out.println(resultado);
    }
}
