package gestorAplicacion.Bodega;
import java.util.ArrayList;

import gestorAplicacion.Administracion.Deuda;

public class Proveedor {
	private static ArrayList<Proveedor> listaProveedores=new ArrayList<Proveedor>();
	Insumo tipoInsumo;
	//tipoBien:Bien
	// insumo provisto por este proveedor
	private int precio;
	private String nombre;
	private float descuento;
	private ArrayList<Deuda> deuda;

	
	public Proveedor(){
		listaProveedores.add(this);
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

	//falta crear constructor que reciba tambien los repuestos y los precios de estos, pero hay que hablarlo primero

	
	static public int costoDeLaCantidad(Insumo i, int c){
		int precio = 0;
		for(Proveedor p: listaProveedores){
			if(p.tipoInsumo.equals(i)){
				precio = (p.precio)*c;
			}
		}
		return precio;
	}

	public static ArrayList<Proveedor> getListaProveedores(){return listaProveedores;}
	public static void setListaProveedores(ArrayList<Proveedor> lista){listaProveedores=lista;}
	public ArrayList<Deuda> getDeuda (){return deuda;}
	public void setDeuda (ArrayList<Deuda> deuda){this.deuda=deuda;}
	public Insumo getInsumo(){return tipoInsumo;}
	public void setInsumo(Insumo insumo){this.tipoInsumo=insumo;}
	public int getPrecio (){return precio;}
	public void setPrecio (int monto){precio=monto;}
	public String getNombre (){return nombre;}
	public void setNombre (String nom){nombre=nom;}
	public float getDescuento (){return descuento;}
	public void setDescuento (float monto){descuento=monto;}
}