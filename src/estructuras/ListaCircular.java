package estructuras;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Lista circular simplemente enlazada.
 * Uso en el sistema: turnos rotativos del banco de pruebas compartido.
 *
 * Se mantienen dos referencias:
 *  - actual:   nodo que tiene el turno.
 *  - anterior: nodo previo a 'actual' (el ultimo de la ronda).
 * Con ellas insertar, avanzar y eliminar el actual son O(1).
 * Invariante: anterior.siguiente == actual (la lista nunca tiene null).
 * Autor: Esteban
 */
public class ListaCircular<T> {
    private NodoCircular<T> actual;
    private NodoCircular<T> anterior;
    private int tamanio;

    public boolean estaVacia() {
        return actual == null;
    }

    public int tamanio() {
        return tamanio;
    }

    /** Inserta al final de la ronda (justo antes del turno actual). O(1). */
    public void insertar(T dato) {
        NodoCircular<T> nuevo = new NodoCircular<>(dato);
        if (actual == null) {
            nuevo.siguiente = nuevo;
            actual = nuevo;
            anterior = nuevo;
        } else {
            nuevo.siguiente = actual;
            anterior.siguiente = nuevo;
            anterior = nuevo;
        }
        tamanio++;
    }

    /** Pasa el turno al siguiente nodo. O(1). */
    public T avanzar() {
        if (actual == null) {
            return null;
        }
        anterior = actual;
        actual = actual.siguiente;
        return actual.dato;
    }

    public T consultarActual() {
        return actual == null ? null : actual.dato;
    }

    /** Elimina el nodo que tiene el turno; el turno pasa al siguiente. O(1). */
    public T eliminarActual() {
        if (actual == null) {
            return null;
        }
        T eliminado = actual.dato;
        if (actual == anterior) {
            actual = null;
            anterior = null;
        } else {
            anterior.siguiente = actual.siguiente;
            actual = actual.siguiente;
        }
        tamanio--;
        return eliminado;
    }

    public boolean existe(Predicate<T> criterio) {
        if (actual == null) {
            return false;
        }
        NodoCircular<T> nodo = actual;
        do {
            if (criterio.test(nodo.dato)) {
                return true;
            }
            nodo = nodo.siguiente;
        } while (nodo != actual);
        return false;
    }

    /** Recorre una vuelta completa empezando por el turno actual. */
    public void recorrer(Consumer<T> accion) {
        if (actual == null) {
            return;
        }
        NodoCircular<T> nodo = actual;
        do {
            accion.accept(nodo.dato);
            nodo = nodo.siguiente;
        } while (nodo != actual);
    }

    public void mostrarRonda() {
        if (estaVacia()) {
            System.out.println("  (no hay turnos registrados)");
            return;
        }
        NodoCircular<T> nodo = actual;
        int posicion = 1;
        do {
            String marca = (nodo == actual) ? "  <- TURNO ACTUAL" : "";
            System.out.println("  " + (posicion++) + ". " + nodo.dato + marca);
            nodo = nodo.siguiente;
        } while (nodo != actual);
        System.out.println("  (vuelve a: " + actual.dato + ")");
    }
}
