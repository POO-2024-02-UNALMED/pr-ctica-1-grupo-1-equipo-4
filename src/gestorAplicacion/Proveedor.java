package gestorAplicacion;
import java.util.ArrayList;

public class Proveedor {
	private static ArrayList<Proveedor> listaProveedores;
	Insumo tipoInsumo;
	//tipoBien:Bien
	// insumo proveido por este proveedor
	int precio;
	String nombre;
	ArrayList<Deuda> deuda;

	public static ArrayList<Proveedor> getListaProveedores(){
		return listaProveedores;
	}
	public ArrayList<Deuda> getDeuda (){return deuda;}
}