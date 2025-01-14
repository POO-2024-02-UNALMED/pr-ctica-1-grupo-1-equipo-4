package gestorAplicacion.Bodega;

import gestorAplicacion.Fecha;
import gestorAplicacion.Sede;

public class Bolsa extends Insumo {
	private int capacidadMaxima;

    public Bolsa(Fecha fecha, String nombre, float cantidad, Proveedor proveedor, Sede sede, int capacidadMaxima) {
        super(fecha, nombre, cantidad, proveedor, sede);
		this.capacidadMaxima=capacidadMaxima;
    }
	
	public int getCapacidadMaxima() {
		return capacidadMaxima;
	}
	
	public void setCapacidadMaxima(int capacidadMaxima) {
		this.capacidadMaxima = capacidadMaxima;
	}
}

