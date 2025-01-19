package gestorAplicacion.Bodega;

import gestorAplicacion.Sede;

public class Insumo{
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
	protected int horasDeVidaUtil;

	public Insumo(String nombre,float cantidad,Proveedor proveedor,Sede sede){
		this.nombre=nombre;
		this.cantidad=cantidad;
		this.proveedor=proveedor;
		this.sede=sede;
		this.precioXUnidad = Math.round(precioCompra/cantidad);
		this.ultimoPrecio = this.precioXUnidad;
		precioCompra=proveedor.getPrecio()*Math.round(cantidad);
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



	
	public static long getPrecioStockTotal(){return precioStockTotal;}
	public static void setPrecioStockTotal(long pSt){Insumo.precioStockTotal = pSt;}
	public String getNombre(){return nombre;}
	public String setNombre(){return nombre;}
	public Proveedor getProveedor(){return proveedor;}
	public void setProveedor(Proveedor p){proveedor=p;}
	public Sede getSede (){return sede;}
	public void setSede(Sede s){sede=s;}
	public float getCantidad(){return cantidad;} 
	public void setCantidad(float cant){
		this.cantidad = cant;
		for (int i=0;i<sede.getListaInsumosBodega().size();i++){
			Insumo insumo = sede.getListaInsumosBodega().get(i);
			if(insumo.nombre.equals(this.nombre)){
				sede.getCantidadInsumosBodega().set(i,Math.round(cant));
			}
		}
	} 
	public int getPrecioCompra(){return precioCompra;} 
	public void setPrecioCompra(int precio){this.precioCompra=precio;} 
	public int getPrecioIndividual(){return precioXUnidad;} 
	public void setUltimoPrecio(int precio){this.ultimoPrecio=precio;} 
	public int getUltimoPrecio(){return ultimoPrecio;} 
}