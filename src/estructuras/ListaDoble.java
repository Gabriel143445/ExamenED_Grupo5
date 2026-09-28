package estructuras;

import java.util.function.Consumer;

/**
 * Lista doblemente enlazada.
 * Uso en el sistema: historial de movimientos.
 * Permite recorrer en orden cronologico (adelante) y del mas
 * reciente al mas antiguo (atras) sin invertir la lista.
 * Autor: Esteban
 */
public class ListaDoble<T> {
    private NodoDoble<T> cabeza;
    private NodoDoble<T> cola;
    private int tamanio;

    public boolean estaVacia() {
        return cabeza == null;
    }

    public int tamanio() {
        return tamanio;
    }

    /** Inserta al final enlazando anterior y siguiente. O(1). */
    public void insertarAlFinal(T dato) {
        NodoDoble<T> nuevo = new NodoDoble<>(dato);
        if (cabeza == null) {
            cabeza = nuevo;
            cola = nuevo;
        } else {
            nuevo.anterior = cola;
            cola.siguiente = nuevo;
            cola = nuevo;
        }
        tamanio++;
    }

    public void recorrerAdelante(Consumer<T> accion) {
        NodoDoble<T> actual = cabeza;
        while (actual != null) {
            accion.accept(actual.dato);
            actual = actual.siguiente;
        }
    }

    public void recorrerAtras(Consumer<T> accion) {
        recorrerAtras(tamanio, accion);
    }

    /** Recorre desde la cola hacia atras como maximo 'limite' elementos. */
    public void recorrerAtras(int limite, Consumer<T> accion) {
        NodoDoble<T> actual = cola;
        int visitados = 0;
        while (actual != null && visitados < limite) {
            accion.accept(actual.dato);
            actual = actual.anterior;
            visitados++;
        }
    }

    public T primero() {
        return cabeza == null ? null : cabeza.dato;
    }

    public T ultimo() {
        return cola == null ? null : cola.dato;
    }

    public void mostrarAdelante() {
        if (estaVacia()) {
            System.out.println("  (historial vacio)");
            return;
        }
        recorrerAdelante(dato -> System.out.println("  " + dato));
    }

    public void mostrarAtras() {
        if (estaVacia()) {
            System.out.println("  (historial vacio)");
            return;
        }
        recorrerAtras(dato -> System.out.println("  " + dato));
    }
}
