package gestorAplicacion;
import java.util.ArrayList;
public class Sede{
	static ArrayList<Sede> listaSedes;
	ArrayList<Empleado> listaEmpleado;
	ArrayList <Maquinaria> listaMaquina;
	ArrayList<Venta> historialVentas;
	ArrayList<Prenda> prendasInventadas;
	ArrayList<Insumo> listaInsumosBodega;
	ArrayList<Integer> cantidadInsumosBodega;
	ArrayList<Integer> produccionAproximada;
	// Una lista  de cantidad de prendas a producior por sede
	ArrayList<Prenda> prendasProduccion;
	String nombre;
	int Distancia;
	//Con respecto a la principal

	private ArrayList<Float> rendimientoDeseado= new ArrayList<Float>();

	public float getRendimientoDeseado(Area area){
		return rendimientoDeseado.get(area.ordinal());
	}
}