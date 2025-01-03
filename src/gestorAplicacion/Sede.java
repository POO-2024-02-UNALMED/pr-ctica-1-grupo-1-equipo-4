package gestorAplicacion;
import java.util.ArrayList;
public class Sede{
	private static ArrayList<Sede> listaSedes = new ArrayList<Sede>();
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

	
	// Retorna una lista con los roles a reemplazar y que sede debe reemplazar los empleados de cada rol, de ser posible. Luego también la cantidad de empleados que quedan por reemplazar.
	static public ArrayList<Object> obtenerNececidadTransferenciaEmpleados(ArrayList<Empleado> despedidos){ //Despedidos es A en el doc.
		ArrayList<Sede> transferirDe = new ArrayList<Sede>();
		ArrayList<Rol> rolesATransferir = new ArrayList<Rol>();

		for (int idxRol=0; idxRol<rolesATransferir.size();idxRol++){
			Rol rol = Rol.values()[idxRol];
			for (Sede sede : listaSedes){
				switch(rol){
					case MODISTA:
					int produccionTotal = 0;
					for (int nProduccion: sede.getProduccionAproximada()){
						produccionTotal += nProduccion;
					}

					int produccionPorModista = produccionTotal / sede.cantidadPorRol(rol);

					if (produccionPorModista<30){
						transferirDe.set(idxRol, sede);
						rolesATransferir.add(rol);
						break;
					}
					break;
					
					case SECRETARIA:
					int ejecutivos = sede.cantidadPorRol(Rol.EJECUTIVO);
					int secretarias = sede.cantidadPorRol(Rol.SECRETARIA);
					int empleados = sede.listaEmpleado.size();
					if (!(empleados/secretarias > 18 || ejecutivos/secretarias > 2)){
						transferirDe.set(idxRol, sede);
						rolesATransferir.add(rol);
						break;
					}
				}
			}
		}
		ArrayList<Object> retorno= new ArrayList<Object>();
		retorno.add(rolesATransferir);
		retorno.add(transferirDe);

		ArrayList<Empleado> aReemplazar = (ArrayList) despedidos.clone();
		for (Empleado emp : despedidos){
			if (rolesATransferir.contains(emp.getRol())){
				aReemplazar.remove(emp);
			}
		}

		retorno.add(aReemplazar);
		return retorno;
	}

	// Devuelve la cantidad de empleados que hay en la sede con el rol dado
	// metodo ayudante para reorganizarEmpleados
	public int cantidadPorRol(Rol rol){
		int cantidad = 0;
		for (Empleado emp : listaEmpleado) {
			if (emp.getRol() == rol){
				cantidad++;
			}
		}
		return cantidad;
	}
}