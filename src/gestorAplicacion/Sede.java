package gestorAplicacion;
// Esta en el paquete común pues administra Empleados e Insumos.

import java.io.Serializable;
import java.util.ArrayList;

import gestorAplicacion.Administracion.Area;
import gestorAplicacion.Administracion.Empleado;
import gestorAplicacion.Administracion.Resultado;
import gestorAplicacion.Administracion.Rol;
import gestorAplicacion.Bodega.Insumo;
import gestorAplicacion.Bodega.Maquinaria;
import gestorAplicacion.Bodega.Prenda;
public class Sede implements Serializable{

	private static final long serializarVersionUID = 1L; // Para serializacion

	private static ArrayList<Sede> listaSedes = new ArrayList<Sede>();
	private ArrayList<Empleado> listaEmpleado = new ArrayList<Empleado>();
	private ArrayList <Maquinaria> listaMaquina= new ArrayList<Maquinaria>();
	private ArrayList<Venta> historialVentas= new ArrayList<Venta>();
	private static ArrayList<Prenda> prendasInventadas= new ArrayList<Prenda>();
	private ArrayList<Insumo> listaInsumosBodega= new ArrayList<Insumo>();
	private ArrayList<Integer> cantidadInsumosBodega= new ArrayList<Integer>();
	private ArrayList<Integer> produccionAproximada=new ArrayList<Integer>();
	// Una lista  de cantidad de prendas a producior por sede
	private ArrayList<Prenda> prendasProduccion= new ArrayList<Prenda>();
	private String nombre;
	private int distancia;
	//Con respecto a la principal

	public void Vender(ArrayList<Prenda> productos, ArrayList<Integer> cantidades,Empleado vendedor, Empleado Oficina, Sede sede){
		for(Prenda prenda : productos){
			ArrayList<Insumo>insumosNecesarios=prenda.getInsumo();
			for (Insumo insumo : insumosNecesarios){
				
			}
	}
	}
	
	public Sede(){
		listaSedes.add(this);
	}

	public Sede(String nombre){
		this();
		this.nombre = nombre;
	}
	// necesario para añadir sedes a la lista de sedes, pero dicha lista no está cargada aún y además
	// se estan creando ahí mismo.
	public Sede(boolean porDefecto){
		listaSedes.add(this);
	}

	static public Resultado verificarProductoBodega(Insumo i, Sede sede){
		boolean retorno = false;
		int index = -1;
		for(int x = 0 ; x < sede.getListaInsumosBodega().size() ; x++){
			if(i.equals(sede.getListaInsumosBodega().get(x))){
				index = x;
				retorno = true;
				break;
			}
		
		}
		Resultado resultado = new Resultado(retorno, index);
		return resultado;
	}

	static public int restarInsumo(Insumo i, Sede s, int c){
		int restante = 0;
		for(int x = 0 ; x < s.getListaInsumosBodega().size() ; x++){
			if(i.equals(s.getListaInsumosBodega().get(x))){
				int cantidad = s.getCantidadInsumosBodega().get(x);
				long ajusteStock = (Insumo.getPrecioStockTotal())-(i.getPrecioIndividual()*c);
				Insumo.setPrecioStockTotal(ajusteStock);
				if((cantidad-c)==0){
					s.cantidadInsumosBodega.set(x,0);
					
				}
				else if((cantidad-c)<0){
					restante = (cantidad - c)*-1;
					s.cantidadInsumosBodega.set(x,0);
				}
				else{
				s.cantidadInsumosBodega.set(x,(cantidad-c));
				}
			}
		
		}
		return restante;
	}

	static public void añadirInsumo(Insumo i, Sede s, int c){
		for(int x = 0 ; x < s.getListaInsumosBodega().size() ; x++){
			if(i.equals(s.getListaInsumosBodega().get(x))){
				int cantidad = s.getCantidadInsumosBodega().get(x);
				s.cantidadInsumosBodega.set(x,(cantidad+c));
				long ajusteStock = (Insumo.getPrecioStockTotal())+(i.getPrecioIndividual()*c);
				Insumo.setPrecioStockTotal(ajusteStock);
			}
		
		}
	}


	static public Resultado verificarProductoOtraSede(Insumo i){
		boolean retorno = false;
		int index = -1;
		Sede sedeATransferir = null;
		int precio = 0;
		for(Sede sede : listaSedes){
			for(int x = 0 ; x < sede.getListaInsumosBodega().size() ; x++){
				if(i.equals(sede.getListaInsumosBodega().get(x))){
					index = x;
					retorno = true;
					sedeATransferir = sede;
					precio = i.getPrecioCompra();
					break;
				}
		
			}
		}
		Resultado resultado = new Resultado(retorno, index, sedeATransferir, precio);
		return resultado;
	}


	public void actualizarHistorialVentas(Venta venta){historialVentas.add(venta);}
	public float getRendimientoDeseado(Area area){return Area.rendimientoDeseadoActual(this).get(area.ordinal());}
	public static ArrayList<Sede> getlistaSedes(){return listaSedes;}
	public static void setlistaSedes(ArrayList<Sede> Sede){listaSedes=Sede;}
	public ArrayList<Empleado> getlistaEmpleados(){return listaEmpleado;}
	public void setlistaEmpleados(ArrayList<Empleado> Emp){listaEmpleado=Emp;}
	public ArrayList<Maquinaria> getlistaMaquinas(){return listaMaquina;}
	public void setlistaMaquinas(ArrayList<Maquinaria> Maquinaria){listaMaquina=Maquinaria;}
	public ArrayList<Venta> getHistorialVentas(){return historialVentas;}
	public void setHistorialVentas(ArrayList<Venta> venta){historialVentas=venta;}	
	public static ArrayList<Prenda> getPrendasInventadas(){return prendasInventadas;}
	public static void setPrendasInventadas(ArrayList<Prenda> prenda){prendasInventadas=prenda;}
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
	public void anadirEmpleado(Empleado emp){listaEmpleado.add(emp);}
	public void quitarEmpleado(Empleado emp){listaEmpleado.remove(emp);}

	// ---Interacciones---
	// Interacción 2 de Gestion Humana
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

	// Interaccion 2 de gestión humana
	static public void reemplazarPorCambioSede(ArrayList<Empleado> despedidos,ArrayList<Empleado> aTransferir){
		for (Empleado despedido : despedidos){
			for (Empleado reemplazo: aTransferir){
				if (despedido.getRol() == reemplazo.getRol()){
					int aPagar = Maquinaria.remuneracionDanos(reemplazo);
					reemplazo.modificarBonificacion(aPagar*-1);
					reemplazo.setSede(despedido.getSede());
					Maquinaria.asignarMaquinaria(reemplazo);
					aTransferir.remove(reemplazo);
					break;
				}
			}
		}
	}

	// ---Metodos ayudantes---

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

	public String toString(){
		return nombre+" con "	+listaEmpleado.size()+" empleados";
	}
}