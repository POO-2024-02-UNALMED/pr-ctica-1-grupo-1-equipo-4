package gestorAplicacion.Bodega;

import gestorAplicacion.Administracion.GastoMensual;
import gestorAplicacion.Fecha;
import gestorAplicacion.Sede;
import gestorAplicacion.Venta;
import java.io.Serializable;

public class Insumo implements GastoMensual,Serializable{
	private static final long serialVersionUID = 1L;
	protected static long precioStockTotal;
	protected String nombre;
	protected Proveedor proveedor;
	protected Sede sede;
	protected int precioCompra;
	protected int precioXUnidad;
	protected int ultimoPrecio;
	// Actualizado al surtir
	//Precio por toda la compra
	protected int horasDeVidaUtil;

	public Insumo(String nombre,float cantidad,Proveedor proveedor,Sede sede){
		this.nombre=nombre;
		this.proveedor=proveedor;
		this.sede=sede;
		precioCompra=proveedor.getPrecio()*Math.round(cantidad);
		this.precioXUnidad = Math.round(precioCompra/cantidad);
		this.ultimoPrecio = this.precioXUnidad;
		precioStockTotal+=precioCompra;
		sede.getListaInsumosBodega().add(this);
		sede.getCantidadInsumosBodega().add(Math.round(cantidad));
	}

	public Insumo(String nombre,Proveedor proveedor){
		//Para insumos de proveedores
		this.nombre=nombre;
		this.proveedor=proveedor;
		this.precioXUnidad = proveedor.getPrecio();
	}

	public Insumo(String nombre, int horasDeVidaUtil, Proveedor proveedor){
		this(nombre, proveedor);
		this.horasDeVidaUtil = horasDeVidaUtil;
	}

	public static long gastoMensualClase(Fecha fecha){
		long gastoInsumo=0;
        long gastoActual=0;
        long gastoPasado=0;
		for (Sede sede: Sede.getlistaSedes()){
		for (Venta venta: sede.getHistorialVentas()){
		for (Insumo i:venta.getBolsas()){
			long [] lista=i.gastoMensualTipo(fecha, venta.getFechaVenta(), i);
			gastoActual+=lista[0];
			gastoPasado+=lista[1];
		if (gastoActual!=0){gastoInsumo=gastoActual;}else{gastoInsumo=gastoPasado;}
        }
	}}
	return gastoInsumo;
    }

	@Override
	public int calcularGastoMensual() {
		int valor=0;
		for(int i=0;i<sede.getListaInsumosBodega().size();i++){
		if (sede.getListaInsumosBodega().get(i).equals(this)){
		valor=(this.getPrecioIndividual()*sede.getCantidadInsumosBodega().get(i));}}
		return valor;
	}
	
	public static long getPrecioStockTotal(){return precioStockTotal;}
	public static void setPrecioStockTotal(long pSt){Insumo.precioStockTotal = pSt;}
	public String getNombre(){return nombre;}
	public String setNombre(){return nombre;}
	public Proveedor getProveedor(){return proveedor;}
	public void setProveedor(Proveedor p){proveedor=p;}
	public Sede getSede (){return sede;}
	public void setSede(Sede s){sede=s;}
	public int getPrecioCompra(){return precioCompra;} 
	public void setPrecioCompra(int precio){this.precioCompra=precio;} 
	public int getPrecioIndividual(){return precioXUnidad;} 
	public void setUltimoPrecio(int precio){this.ultimoPrecio=precio;} 
	public int getUltimoPrecio(){return ultimoPrecio;}
	public String toString(){
		return "El insumo "+nombre+", comprado al proveedor"+proveedor.getNombre()+" y almacenado en la sede"+sede.getNombre()+" costó aproximadamente $"+precioXUnidad+" por unidad";
	}
}