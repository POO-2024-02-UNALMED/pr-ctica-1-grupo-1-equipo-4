package gestorAplicacion;
import java.util.ArrayList;

public class Proveedor {
	static ArrayList<Proveedor> listaProveedores;
	Insumo tipoInsumo;
	//tipoBien:Bien
	// insumo proveido por este proveedor
	int precio;
	String nombre;
	ArrayList<Deuda> deuda;
}