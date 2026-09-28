package estructuras;

import java.util.function.Consumer;

/**
 * Pila LIFO con nodos propios (reutiliza Nodo).
 * Uso en el sistema: cambios de configuracion logica para poder
 * revertir el ultimo (deshacer). apilar y desapilar son O(1).
 * Autor: Gabriel
 */
public class Pila<T> {
    private Nodo<T> tope;
    private int tamanio;

    public boolean estaVacia() {
        return tope == null;
    }

    public int tamanio() {
        return tamanio;
    }

    /** Coloca un elemento en el tope. O(1). */
    public void apilar(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        nuevo.siguiente = tope;
        tope = nuevo;
        tamanio++;
    }

    /** Retira el elemento del tope. O(1). Devuelve null si esta vacia. */
    public T desapilar() {
        if (estaVacia()) {
            return null;
        }
        T dato = tope.dato;
        tope = tope.siguiente;
        tamanio--;
        return dato;
    }

    /** Consulta el tope sin retirarlo. */
    public T consultarTope() {
        return tope == null ? null : tope.dato;
    }

    /** Recorre desde el tope hacia la base. */
    public void recorrer(Consumer<T> accion) {
        Nodo<T> actual = tope;
        while (actual != null) {
            accion.accept(actual.dato);
            actual = actual.siguiente;
        }
    }

    public void mostrar() {
        if (estaVacia()) {
            System.out.println("  (pila vacia)");
            return;
        }
        Nodo<T> actual = tope;
        boolean esTope = true;
        while (actual != null) {
            System.out.println("  " + (esTope ? "TOPE -> " : "        ") + actual.dato);
            esTope = false;
            actual = actual.siguiente;
        }
    }
}
