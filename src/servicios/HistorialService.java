package servicios;

import estructuras.ListaDoble;
import modelo.Movimiento;
import modelo.TipoMovimiento;
import util.Fechas;

import java.util.function.Consumer;

/**
 * Historial de movimientos sobre una lista doblemente enlazada.
 * Toda operacion importante del sistema se registra aqui.
 * Autor: Esteban
 */
public class HistorialService {
    private final ListaDoble<Movimiento> historial = new ListaDoble<>();

    public void registrar(TipoMovimiento tipo, String codigoEquipo, String detalle) {
        String codigo = (codigoEquipo == null || codigoEquipo.isEmpty()) ? "-" : codigoEquipo;
        historial.insertarAlFinal(new Movimiento(Fechas.ahora(), tipo, codigo, detalle));
    }

    /** Recorrido hacia adelante: del mas antiguo al mas reciente. */
    public void mostrarCronologico() {
        historial.mostrarAdelante();
    }

    /** Recorrido hacia atras: del mas reciente al mas antiguo. */
    public void mostrarInverso() {
        historial.mostrarAtras();
    }

    /** Ultimos N movimientos usando los enlaces 'anterior'. */
    public void mostrarUltimos(int cantidad) {
        if (historial.estaVacia()) {
            System.out.println("  (historial vacio)");
            return;
        }
        historial.recorrerAtras(cantidad, m -> System.out.println("  " + m));
    }

    /** Filtra los movimientos de un equipo. Devuelve cuantos encontro. */
    public int mostrarPorEquipo(String codigo) {
        int[] contador = {0};
        historial.recorrerAdelante(m -> {
            if (m.getCodigoEquipo().equalsIgnoreCase(codigo)) {
                System.out.println("  " + m);
                contador[0]++;
            }
        });
        return contador[0];
    }

    public void recorrer(boolean adelante, Consumer<Movimiento> accion) {
        if (adelante) {
            historial.recorrerAdelante(accion);
        } else {
            historial.recorrerAtras(accion);
        }
    }

    public Movimiento ultimo() {
        return historial.ultimo();
    }

    public int total() {
        return historial.tamanio();
    }
}
