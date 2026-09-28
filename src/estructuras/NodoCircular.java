package estructuras;

/**
 * Nodo de la lista circular.
 * Autor: Esteban
 */
public class NodoCircular<T> {
    T dato;
    NodoCircular<T> siguiente;

    public NodoCircular(T dato) {
        this.dato = dato;
    }
}
