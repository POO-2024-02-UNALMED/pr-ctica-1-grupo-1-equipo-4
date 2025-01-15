package gestorAplicacion.Bodega;

import gestorAplicacion.Fecha;
import gestorAplicacion.Sede;
import gestorAplicacion.Administracion.GastoMensual;

public class Insumo implements GastoMensual{
	protected Fecha fechaCompra;
	protected static long precioStockTotal;
	protected String nombre;
	protected float cantidad;
	protected Proveedor proveedor;
	protected Sede sede;
	protected int precioCompra;
	protected int precioXUnidad;
	protected int ultimoPrecio;
	// Actualizado al surtir
	//Precio por toda la compra

	Insumo(Fecha fecha,String nombre,float cantidad,Proveedor proveedor,Sede sede){
		fechaCompra=fecha;
		this.nombre=nombre;
		this.cantidad=cantidad;
		this.proveedor=proveedor;
		this.sede=sede;
		this.precioXUnidad = Math.round(precioCompra/cantidad);
		this.ultimoPrecio = this.precioXUnidad;
		precioCompra=proveedor.getPrecio()*Math.round(cantidad);
		precioStockTotal+=precioCompra;
	}

	@Override
    public int calcularGastoMensual() {
        int gasto=this.getPrecioIndividual();
        return gasto;
    }

	public static long gastoMensualClase(Fecha fecha){
		long gastoActual=0;
        long gastoPasado=0;
		long gastoInsumo;
		for (Sede sede:Sede.getlistaSedes()){
		for (Insumo insumo:sede.getListaInsumosBodega()){
			if (insumo instanceof Bolsa){
				long [] lista=insumo.gastoMensualTipo(fecha, insumo.fechaCompra, insumo);
				gastoActual+=lista[0];
				gastoPasado+=lista[1];
				}}}
		if (gastoActual!=0){gastoInsumo=gastoActual;}else{gastoInsumo=gastoPasado;}
        return gastoInsumo;
    }

	public Fecha getFechaCompra(){return fechaCompra;}
	public void getFechaCompra(Fecha fecha){fechaCompra=fecha;}
	public static long getPrecioStockTotal(){return precioStockTotal;}
	public static void setPrecioStockTotal(long pSt){Insumo.precioStockTotal = pSt;}
	public String getNombre(){return nombre;}
	public String setNombre(){return nombre;}
	public Proveedor getProveedor(){return proveedor;}
	public void setProveedor(Proveedor p){proveedor=p;}
	public Sede getSede (){return sede;}
	public void setSede(Sede s){sede=s;}
	public float getCantidad(){return cantidad;} 
	public void setCantidad(float cant){this.cantidad = cant;} 
	public int getPrecioCompra(){return precioCompra;} 
	public void setPrecioCompra(int precio){this.precioCompra=precio;} 
	public int getPrecioIndividual(){return precioXUnidad;} 
	public void setUltimoPrecio(int precio){this.ultimoPrecio=precio;} 
	public int getUltimoPrecio(){return ultimoPrecio;} 
}