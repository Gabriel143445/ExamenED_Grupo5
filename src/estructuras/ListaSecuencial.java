package estructuras;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Lista secuencial basada en un arreglo contiguo.
 * Uso en el sistema: inventario general de equipos de red.
 * Acceso por indice O(1); insercion al final O(1) amortizado;
 * busqueda y eliminacion O(n) por el desplazamiento de elementos.
 * Autor: Kerly
 */
public class ListaSecuencial<T> {
    private static final int CAPACIDAD_INICIAL = 10;

    private Object[] elementos;
    private int tamanio;

    public ListaSecuencial() {
        this(CAPACIDAD_INICIAL);
    }

    public ListaSecuencial(int capacidad) {
        if (capacidad <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser mayor que cero");
        }
        elementos = new Object[capacidad];
        tamanio = 0;
    }

    public boolean estaVacia() {
        return tamanio == 0;
    }

    public int tamanio() {
        return tamanio;
    }

    public int capacidad() {
        return elementos.length;
    }

    /** Inserta al final. Si el arreglo esta lleno, duplica su capacidad. */
    public void insertar(T dato) {
        if (tamanio == elementos.length) {
            crecer();
        }
        elementos[tamanio] = dato;
        tamanio++;
    }

    private void crecer() {
        Object[] nuevo = new Object[elementos.length * 2];
        for (int i = 0; i < tamanio; i++) {
            nuevo[i] = elementos[i];
        }
        elementos = nuevo;
    }

    @SuppressWarnings("unchecked")
    public T obtener(int indice) {
        validarIndice(indice);
        return (T) elementos[indice];
    }

    /** Reemplaza el elemento de una posicion. O(1). */
    public void modificar(int indice, T dato) {
        validarIndice(indice);
        elementos[indice] = dato;
    }

    /** Busqueda secuencial. Devuelve el indice o -1. O(n). */
    public int buscar(Predicate<T> criterio) {
        for (int i = 0; i < tamanio; i++) {
            if (criterio.test(obtener(i))) {
                return i;
            }
        }
        return -1;
    }

    /** Elimina por indice desplazando los elementos a la izquierda. O(n). */
    public T eliminar(int indice) {
        validarIndice(indice);
        T eliminado = obtener(indice);
        for (int i = indice; i < tamanio - 1; i++) {
            elementos[i] = elementos[i + 1];
        }
        elementos[tamanio - 1] = null;
        tamanio--;
        return eliminado;
    }

    public T eliminar(Predicate<T> criterio) {
        int indice = buscar(criterio);
        return indice == -1 ? null : eliminar(indice);
    }

    public int contar(Predicate<T> criterio) {
        int contador = 0;
        for (int i = 0; i < tamanio; i++) {
            if (criterio.test(obtener(i))) {
                contador++;
            }
        }
        return contador;
    }

    public void recorrer(Consumer<T> accion) {
        for (int i = 0; i < tamanio; i++) {
            accion.accept(obtener(i));
        }
    }

    public void mostrar() {
        if (estaVacia()) {
            System.out.println("  (lista vacia)");
            return;
        }
        for (int i = 0; i < tamanio; i++) {
            System.out.println("  " + (i + 1) + ". " + obtener(i));
        }
    }

    private void validarIndice(int indice) {
        if (indice < 0 || indice >= tamanio) {
            throw new IndexOutOfBoundsException("Indice fuera de rango: " + indice);
        }
    }
}
