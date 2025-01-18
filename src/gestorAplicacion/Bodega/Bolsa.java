package gestorAplicacion.Bodega;

import gestorAplicacion.Sede;

public class Bolsa extends Insumo {
	private int capacidadMaxima;

    public Bolsa(String nombre, float cantidad, Proveedor proveedor, Sede sede, int capacidadMaxima) {
        super(nombre, cantidad, proveedor, sede);
		this.capacidadMaxima=capacidadMaxima;
    }

	public Bolsa(String nombre,Proveedor proveedor) {
        super(nombre, proveedor);
    }

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

