package estructuras;

import java.util.function.Consumer;

/**
 * Cola FIFO con nodos propios (reutiliza Nodo).
 * Uso en el sistema: solicitudes en espera de equipos de alta demanda.
 * encolar y desencolar son O(1) porque se guardan frente y final.
 * Autor: Gabriel
 */
public class Cola<T> {
    private Nodo<T> frente;
    private Nodo<T> fin;
    private int tamanio;

    public boolean estaVacia() {
        return frente == null;
    }

    public int tamanio() {
        return tamanio;
    }

    /** Agrega al final de la cola. O(1). */
    public void encolar(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        if (fin == null) {
            frente = nuevo;
            fin = nuevo;
        } else {
            fin.siguiente = nuevo;
            fin = nuevo;
        }
        tamanio++;
    }

    /** Retira el elemento del frente. O(1). Devuelve null si esta vacia. */
    public T desencolar() {
        if (estaVacia()) {
            return null;
        }
        T dato = frente.dato;
        frente = frente.siguiente;
        if (frente == null) {
            fin = null;
        }
        tamanio--;
        return dato;
    }

    /** Consulta el frente sin retirarlo. */
    public T consultarFrente() {
        return frente == null ? null : frente.dato;
    }

    /** Recorre del frente al final sin modificar la cola. */
    public void recorrer(Consumer<T> accion) {
        Nodo<T> actual = frente;
        while (actual != null) {
            accion.accept(actual.dato);
            actual = actual.siguiente;
        }
    }

    public void mostrar() {
        if (estaVacia()) {
            System.out.println("  (cola vacia)");
            return;
        }
        Nodo<T> actual = frente;
        int posicion = 1;
        while (actual != null) {
            String marca = (actual == frente) ? "  <- FRENTE" : "";
            System.out.println("  " + (posicion++) + ". " + actual.dato + marca);
            actual = actual.siguiente;
        }
    }
}
