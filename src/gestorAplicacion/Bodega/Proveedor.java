/* Equipo 4 grupo 1
 * Provee insumos y repuestos a la empresa
 */
package src.gestorAplicacion.Bodega;
import java.io.Serializable;
import java.util.ArrayList;
import src.gestorAplicacion.Fecha;

import src.gestorAplicacion.Administracion.Deuda;

public class Proveedor implements Serializable{
	private static final long serialVersionUID = 1L;
	private static ArrayList<Proveedor> listaProveedores=new ArrayList<Proveedor>();
	Insumo tipoInsumo;
	//tipoBien:Bien
	// insumo provisto por este proveedor
	private int precio;
	private String nombre;
	private float descuento;
	private Deuda deuda;
	
	public Proveedor(){
		Proveedor.listaProveedores.add(this);
	}
	
	public Proveedor(int precio,String nombre){
		this();
		this.precio=precio;
		this.nombre=nombre;
	}

	public Proveedor(int precio,String nombre,Insumo insumo){
		this(precio,nombre);
		tipoInsumo=insumo;
	}

	public Proveedor(int prueba){
		setPrecio(prueba);
	}
	//falta crear constructor que reciba tambien los repuestos y los precios de estos, pero hay que hablarlo primero

	static public Proveedor buscarPorNombreInsumo(String nombre){
		for(Proveedor p: listaProveedores){
			if(p.tipoInsumo.getNombre().equals(nombre)){
				return p;
			}
		}
		return null;
	}
	
	static public int costoDeLaCantidad(Insumo i, int c){
		int precio = 0;
		for(Proveedor p: listaProveedores){
			if(p.tipoInsumo.equals(i)){
				Bolsa b=(Bolsa)i;
				precio = (p.precio-Math.round((p.precio*p.descuento*b.getCapacidadMaxima())))*c;
			}
		}
		return precio;
	}
	
	public void unificarDeudasXProveedor(Fecha fecha, int montoDeuda){
		int cuotas=Deuda.calcularCuotas(montoDeuda+deuda.getValorinicialDeuda()-deuda.getCapitalPagado());
		if (deuda.getEntidad().equals(this.getNombre())&&!deuda.getEstadodePago()){
				deuda.actualizarDeuda(fecha,montoDeuda,cuotas);
			}
		}

	

	public static ArrayList<Proveedor> getListaProveedores(){return listaProveedores;}
	public static void setListaProveedores(ArrayList<Proveedor> lista){listaProveedores=lista;}
	public Deuda getDeuda (){return deuda;}
	public void setDeuda (Deuda deuda){this.deuda=deuda;}
	public Insumo getInsumo(){return tipoInsumo;}
	public void setInsumo(Insumo insumo){this.tipoInsumo=insumo;}
	public int getPrecio (){return precio;}
	public void setPrecio (int monto){precio=monto;}
	public String getNombre (){return nombre;}
	public void setNombre (String nom){nombre=nom;}
	public float getDescuento (){return descuento;}
	public void setDescuento (float monto){descuento=monto;}
	public String toString(){
		return "El proveedor "+nombre+" vende insumos de tipo "+tipoInsumo.getNombre()+" y valen "+precio;
	}
}