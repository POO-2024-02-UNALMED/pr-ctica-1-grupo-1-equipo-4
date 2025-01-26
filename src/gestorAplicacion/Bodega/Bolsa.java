package gestorAplicacion.Bodega;

import gestorAplicacion.Sede;

public class Bolsa extends Insumo {
	private int capacidadMaxima;

    public Bolsa(String nombre, int cantidad, Proveedor proveedor, Sede sede, int capacidadMaxima) {
        super(nombre, cantidad, proveedor, sede);
		this.capacidadMaxima=capacidadMaxima;
    }

	public Bolsa(String nombre,Proveedor proveedor) {
        super(nombre, proveedor);
    }

	// Calcula el precio de la bolsa, que puede diferir del definido como Insumo.
	@Override
    public int getPrecioIndividual() {
       return Math.round(precioXUnidad-(precioXUnidad*proveedor.getDescuento()*capacidadMaxima));
    }
	
	public int getCapacidadMaxima() {
		return capacidadMaxima;
	}
	
	public void setCapacidadMaxima(int capacidadMaxima) {
		this.capacidadMaxima = capacidadMaxima;
	}
}

