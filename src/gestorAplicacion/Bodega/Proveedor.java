package gestorAplicacion.Bodega;
import java.util.ArrayList;

import gestorAplicacion.Administracion.Deuda;

public class Proveedor {
	private static ArrayList<Proveedor> listaProveedores;
	Insumo tipoInsumo;
	//tipoBien:Bien
	// insumo provisto por este proveedor
	int precio;
	String nombre;
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
}