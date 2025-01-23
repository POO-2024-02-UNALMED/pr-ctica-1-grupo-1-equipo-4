package gestorAplicacion;
// Esta en el paquete común pues administra Empleados e Insumos.

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

import gestorAplicacion.Administracion.Area;
import gestorAplicacion.Administracion.Empleado;
import gestorAplicacion.Administracion.Resultado;
import gestorAplicacion.Administracion.Rol;
import gestorAplicacion.Bodega.Insumo;
import gestorAplicacion.Bodega.Maquinaria;
import gestorAplicacion.Bodega.Prenda;
import gestorAplicacion.Administracion.Banco;
public class Sede implements Serializable{

	private static final long serialVersionUID = 1L; // Para serializacion

	private static ArrayList<Sede> listaSedes = new ArrayList<Sede>();
	private ArrayList<Empleado> listaEmpleado = new ArrayList<Empleado>();
	private ArrayList <Maquinaria> listaMaquina= new ArrayList<Maquinaria>();
	private ArrayList<Venta> historialVentas= new ArrayList<Venta>();
	private ArrayList<Prenda> prendasInventadas = new ArrayList<Prenda>();

	private ArrayList<Insumo> listaInsumosBodega= new ArrayList<Insumo>();
	private ArrayList<Integer> cantidadInsumosBodega= new ArrayList<Integer>();
	private ArrayList<Integer> produccionAproximada=new ArrayList<Integer>();
	// Una lista  de cantidad de prendas a producior por sede
	private ArrayList<Prenda> prendasProduccion = new ArrayList<Prenda>();
	private String nombre;
	private Banco cuentaSede;
	private int distancia;
	//Con respecto a la principal

	private ArrayList<Integer> prodAproximada = new ArrayList<>();  // este atributo sera equivalente al de arriba,
																	// no uso el de arriba porque este lo usaré solo como una lista de dos enteros
																	// la posición 0 para pantalones y la 1 para el numero de camisas a producir
	ArrayList<Maquinaria> maqProduccion = new ArrayList<>();
	ArrayList<Maquinaria> maqOficina = new ArrayList<>();

	
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
		this();
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
	public float getRendimientoDeseado(Area area, Fecha fecha){return Area.rendimientoDeseadoActual(this,fecha).get(area.ordinal());}
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
	public void setCuentaSede(Banco cuenta){cuentaSede=cuenta;}
	public Banco getCuentaSede(){return cuentaSede;}
	public void setDistancia(int distancia){this.distancia=distancia;}	
	public void anadirEmpleado(Empleado emp){listaEmpleado.add(emp);}
	public void quitarEmpleado(Empleado emp){listaEmpleado.remove(emp);}
	
	public ArrayList<Integer> getProdAproximada(){
		return prodAproximada;
	}

	// ---Interacciones---
	// Interacción 2 de Gestion Humana
	// Retorna una lista con los roles a reemplazar y que sede debe reemplazar los empleados de cada rol, de ser posible. Luego también la cantidad de empleados que quedan por reemplazar.
	static public ArrayList<Object> obtenerNececidadTransferenciaEmpleados(ArrayList<Empleado> despedidos){ //Despedidos es A en el doc.
		ArrayList<Sede> transferirDe = new ArrayList<Sede>();
		ArrayList<Rol> rolesATransferir = new ArrayList<Rol>();


		for (int idxRol=0; idxRol<Rol.values().length;idxRol++){
			Rol rol = Rol.values()[idxRol];
			boolean hayEmpleados=false;
			for (Empleado emp : despedidos){
				if (emp.getRol() == rol){
					hayEmpleados = true;
					break;
				}
			}
			if (!hayEmpleados){
				continue; // Nos saltamos esta iteración si no hay empleados de este rol por reemplazar.
			}
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

		ArrayList<Empleado> aReemplazar = (ArrayList<Empleado>) despedidos.clone();
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
			// Buscamos en la lista de empleados a transferir, quien pudo ser seleccionado como reemplazo.
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

	// Usado para eliminar un Insumo limpiamente
	public static void quitarInsumoDeBodegas(Insumo insumo){
		for (Sede sede : listaSedes){
			for (int idxInsumo=0; idxInsumo<sede.getListaInsumosBodega().size(); idxInsumo++){
				if (sede.getListaInsumosBodega().get(idxInsumo).equals(insumo)){
					sede.getListaInsumosBodega().remove(idxInsumo);
					sede.getCantidadInsumosBodega().remove(idxInsumo);
				}
			}
		}
	}

		//interacion 2 de Produccion
	public ArrayList<ArrayList<Integer>> planProduccion(ArrayList<Maquinaria> maqDisponiblee, Fecha fecha, Scanner scanner){
		ArrayList<ArrayList<Integer>> aProducir = new ArrayList<>();
		ArrayList<Integer> listaDeCeros = new ArrayList<>();
		ArrayList<Maquinaria> maqSedeP = new ArrayList<>();
		ArrayList<Maquinaria> maqSede2 = new ArrayList<>();
		int señal = 0;
		
		listaDeCeros.add(0, 0);
		listaDeCeros.add(1, 0);

			//dividir las maquinas disponibles por sedes:
		for(Maquinaria todMaquinas : maqDisponiblee){
			if(todMaquinas.getSede().getNombre().equalsIgnoreCase("Sede Principal")){
				maqSedeP.add(todMaquinas);
			} else {
				maqSede2.add(todMaquinas);
			}
		}

			//dividir las maquinas de cada sede por funcion:
		for(Maquinaria todMaqSedeP : maqSedeP){
			if(todMaqSedeP.getNombre().equalsIgnoreCase("Maquina de coser industrial") || todMaqSedeP.getNombre().equalsIgnoreCase("Maquina de Corte") || todMaqSedeP.getNombre().equalsIgnoreCase("Plancha industrial") || todMaqSedeP.getNombre().equalsIgnoreCase("Bordadora Industrial") || todMaqSedeP.getNombre().equalsIgnoreCase("Maquina de termofijado") || todMaqSedeP.getNombre().equalsIgnoreCase("Maquina de tijereado")){
				maqProduccion.add(todMaqSedeP);
			} else{
				maqOficina.add(todMaqSedeP);
			}
		}
		for(Maquinaria todMaqSede2 : maqSede2){
			if(todMaqSede2.getNombre().equalsIgnoreCase("Maquina de coser industrial") || todMaqSede2.getNombre().equalsIgnoreCase("Maquina de Corte") || todMaqSede2.getNombre().equalsIgnoreCase("Plancha industrial") || todMaqSede2.getNombre().equalsIgnoreCase("Bordadora Industrial") || todMaqSede2.getNombre().equalsIgnoreCase("Maquina de termofijado") || todMaqSede2.getNombre().equalsIgnoreCase("Maquina de tijereado")){
				maqProduccion.add(todMaqSede2);
			} else{
				maqOficina.add(todMaqSede2);
			}
		}


		if(getlistaSedes().get(0).maqProduccion.size() >= 3){
			señal = 5;
		}
		if(getlistaSedes().get(1).maqProduccion.size() >= 3){
			señal = señal + 10;
		}

		if(señal == 5){
			System.out.println("La Sede 2 no está trabajando por falta de maquinaria disponible...\n");
			System.out.println("1. ¿Desea producir todo hoy desde la Sede Principal?");
			System.out.println("2. ¿Desea producir mañana lo de la Sede 2 desde la sede Principal?");

			int opcion = 0;
			while(opcion != 1 && opcion != 2){
				opcion = scanner.nextInt();
				if(opcion == 1){
					//llamar a un metodo1: prodSedeP() para empezar a producir todo en la sede principal
					aProducir.add(0, prodSedeP(fecha));
					aProducir.add(1, listaDeCeros);
					
				} else if(opcion == 2){
					//llamar a otro metodo2 para pasar la produccion a una lista de espera
				} else{
					System.out.println("\n Marque una opcion correcta entre 1 o 2...\n");
				}
			}
		} else if (señal == 10) {
			System.out.println("La Sede Principal no esta trabajando por falta de maquinaria disponible...");
			System.out.println("¿Desea delegar toda la produccion a la Sede 2 o poner en espera la produccion de la Sede Principal? \n");
			System.out.println("1. Delegar");
			System.out.println("2. Poner en espera");

			int opcion = 0;
			while(opcion != 1 && opcion != 2){
				opcion = scanner.nextInt();
				if(opcion == 1){
					//llamar a un metodo3: prodSede2() para empezar a producir todo en la sede 2
					aProducir.add(0, listaDeCeros);
					aProducir.add(1, prodSede2(fecha));
					
				} else if(opcion == 2){
					//llamar a otro metodo2 para pasar la produccion a una lista de espera
				} else{
					System.out.println("\n Marque una opcion correcta entre 1 o 2...\n");
				}
			}
		} else if(señal == 15){
			//aquí se produce todo entre las dos sedes, después de preguntarle previamente al usuario lo q queria
		} else{
			System.out.println("\n Lo sentimos, se debe arreglar la maquinaria en alguna de las dos sedes para comenzar a producir...\n");
		}

		return aProducir;
	}
		
		//aqui se organiza la produccion de cada sede segun el metodo predecirVentas()
	public ArrayList<ArrayList<Integer>> calcProduccionSedes(Fecha fecha){
		ArrayList<ArrayList<Integer>> prodSedesCalculada = new ArrayList<>();
		ArrayList<Integer> prodCalculadaSedeP = new ArrayList<>();
		ArrayList<Integer> prodCalculadaSede2 = new ArrayList<>();
		
		prodCalculadaSedeP.add(0, Venta.predecirVentas(fecha, getlistaSedes().get(0),"Pantalon"));
		prodCalculadaSedeP.add(1, Venta.predecirVentas(fecha, getlistaSedes().get(0), "Camisa"));

		prodCalculadaSede2.add(0, Venta.predecirVentas(fecha, getlistaSedes().get(1), "Pantalon"));
		prodCalculadaSede2.add(1, Venta.predecirVentas(fecha, getlistaSedes().get(1), "Camisa"));

		prodSedesCalculada.add(0, prodCalculadaSedeP);
		prodSedesCalculada.add(1, prodCalculadaSede2);

		return prodSedesCalculada;
	}
		//aqui se hace que todo se produzca en la sede Principal
	public ArrayList<Integer> prodSedeP(Fecha fecha){
		// modificar la produccion aproximada enviando TODO para la sede principal, segun lo que se calculo en calcProduccionSedes
		// luego retornar dicha produccion de la sede P (siendo esta un array de dos elementos, en el 0 el num pantalones y en el 1 camisas)
		// se retorna al metodo planProduccion(), en donde todo se produce en la sedeP
		int pantalonesSedeP = calcProduccionSedes(fecha).get(0).get(0) + calcProduccionSedes(fecha).get(1).get(0);
		int camisasSedeP = calcProduccionSedes(fecha).get(0).get(1) + calcProduccionSedes(fecha).get(1).get(1);

		prodAproximada.add(0, pantalonesSedeP);
		prodAproximada.add(1, camisasSedeP);
		
		return prodAproximada;
	}

	public ArrayList<Integer> prodSede2(Fecha fecha){
		// modificar la produccion aproximada enviando TODO para la sede 2, segun lo que se calculo en calcProduccionSedes
		// luego retornar dicha produccion de la sede 2 al metodo planProduccion(), en donde todo se produce en la sede2
		int pantalonesSede2 = calcProduccionSedes(fecha).get(1).get(0) + calcProduccionSedes(fecha).get(0).get(0);
		int camisasSede2 = calcProduccionSedes(fecha).get(1).get(1) + calcProduccionSedes(fecha).get(0).get(1);

		prodAproximada.add(0, pantalonesSede2);
		prodAproximada.add(1, camisasSede2);

		return prodAproximada;
	}

	
	
}