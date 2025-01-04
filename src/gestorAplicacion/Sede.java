package gestorAplicacion;
import java.util.ArrayList;
public class Sede{
	private static ArrayList<Sede> listaSedes;
	private ArrayList<Empleado> listaEmpleado;
	private ArrayList <Maquinaria> listaMaquina;
	private ArrayList<Venta> historialVentas;
	private ArrayList<Prenda> prendasInventadas;
	private ArrayList<Insumo> listaInsumosBodega;
	private ArrayList<Integer> cantidadInsumosBodega;
	private ArrayList<Integer> produccionAproximada;
	// Una lista  de cantidad de prendas a producior por sede
	private ArrayList<Prenda> prendasProduccion;
	private String nombre;
	private int distancia;
	//Con respecto a la principal
	private ArrayList<Float> rendimientoDeseado= new ArrayList<Float>();

	public void Vender(ArrayList<Prenda> productos, ArrayList<Integer> cantidades,Empleado vendedor, Empleado Oficina, Sede sede){
		for(Prenda prenda : productos){
			ArrayList<Insumo>insumosNecesarios=prenda.getInsumo();
			for (Insumo insumo : insumosNecesarios){
				
			}
	}
	}


	public float getRendimientoDeseado(Area area){return rendimientoDeseado.get(area.ordinal());}
	public void setRendimientoDeseado(ArrayList<Float> rendimiento){ rendimientoDeseado=rendimiento;}
	public static ArrayList<Sede> getlistaSedes(){return listaSedes;}
	public static void setlistaSedes(ArrayList<Sede> Sede){listaSedes=Sede;}
	public ArrayList<Empleado> getlistaEmpleados(){return listaEmpleado;}
	public void setlistaEmpleados(ArrayList<Empleado> Emp){listaEmpleado=Emp;}
	public ArrayList<Maquinaria> getlistaMaquinas(){return listaMaquina;}
	public void setlistaMaquinas(ArrayList<Maquinaria> Maquinaria){listaMaquina=Maquinaria;}
	public ArrayList<Venta> getHistorialVentas(){return historialVentas;}
	public void setHistorialVentas(ArrayList<Venta> venta){historialVentas=venta;}	
	public ArrayList<Prenda> getPrendasInventadas(){return prendasInventadas;}
	public void setPrendasInventadas(ArrayList<Prenda> prenda){prendasInventadas=prenda;}
	public ArrayList<Insumo> getListaInsumosBodega(){return listaInsumosBodega;}
	public void setlistaInsumosBodega(ArrayList<Insumo> Insumos){listaInsumosBodega=Insumos;}
	public ArrayList<Integer> getCantidadInsumosBodega(){return cantidadInsumosBodega;}
	public void setCantidadInsumosBodega(ArrayList<Integer> CantidadIns){cantidadInsumosBodega=CantidadIns;}
	public ArrayList<Integer> getProduccionAproximada(){return produccionAproximada;}
	public void setProduccionAproximada(ArrayList<Integer> produccion){produccionAproximada=produccion;}
	public ArrayList<Prenda> getPrendasProduccion(){return prendasProduccion;}
	public void setPrendasProduccion(ArrayList<Prenda> Prendasp){prendasProduccion=Prendasp;}
	public String getNombre(){return nombre;}
	public void setNombre(String nombre){this.nombre=nombre;}	
	public int getDistancia(){return distancia;}
	public void setDistancia(int distancia){this.distancia=distancia;}		
}