package estructuras;

/**
 * Nodo con enlace al anterior y al siguiente.
 * Autor: Esteban
 */
public class NodoDoble<T> {
    T dato;
    NodoDoble<T> anterior;
    NodoDoble<T> siguiente;

    public NodoDoble(T dato) {
        this.dato = dato;
    }
}
