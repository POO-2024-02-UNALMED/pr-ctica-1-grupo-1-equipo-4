package gestorAplicacion;
// Esta en el paquete común pues administra Empleados e Insumos.

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

import gestorAplicacion.Administracion.Area;
import gestorAplicacion.Administracion.Empleado;
import gestorAplicacion.Administracion.Evaluacionfinanciera;
import gestorAplicacion.Administracion.Resultado;
import gestorAplicacion.Administracion.Rol;
import gestorAplicacion.Bodega.Insumo;
import gestorAplicacion.Bodega.Maquinaria;
import gestorAplicacion.Bodega.Prenda;
import gestorAplicacion.Administracion.Banco;
import uiMain.Main;
public class Sede implements Serializable{

	private static final long serialVersionUID = 1L; // Para serializacion

	private static ArrayList<Prenda> prendasInventadasTotal = new ArrayList<Prenda>(); // Por razones de serializacion.
	private static ArrayList<Empleado> listaEmpleadosTotal=new ArrayList<Empleado>(); // Por razones de serializacion.
	private static ArrayList<Sede> listaSedes = new ArrayList<Sede>();

	// Ahora está aquí y en no en Evaluacionfinanciera por razones de serialización.
	private static ArrayList<Evaluacionfinanciera> evaluacionesFinancieras = new ArrayList<Evaluacionfinanciera>();
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
	

	public Sede(int prueba){}
	
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

	// Retorna lo que no se pudo sacar de la sede s
	static public int transferirInsumo(Insumo i, Sede donadora, Sede beneficiaria, int cantidadSolicitada){
		int restante = 0;
		int idxInsumo = donadora.getListaInsumosBodega().indexOf(i);

		if (idxInsumo == -1){
			return cantidadSolicitada; // Se salta el resto del metodo, porque no hay nada que transferir.
		}

		int cantidadDisponible = Math.min(donadora.getCantidadInsumosBodega().get(idxInsumo),cantidadSolicitada);
		long ajusteStock = (Insumo.getPrecioStockTotal())-(i.getPrecioIndividual()*cantidadSolicitada);
		Insumo.setPrecioStockTotal(ajusteStock);
		if((cantidadDisponible-cantidadSolicitada)==0){
			donadora.cantidadInsumosBodega.set(idxInsumo,0);
			
		}
		else if((cantidadDisponible-cantidadSolicitada)<0){
			restante = (cantidadDisponible - cantidadSolicitada)*-1;
			donadora.cantidadInsumosBodega.set(idxInsumo,0);
		}
		else{
			donadora.cantidadInsumosBodega.set(idxInsumo,(cantidadDisponible-cantidadSolicitada));
		}

		añadirInsumo(i, beneficiaria, cantidadSolicitada-cantidadDisponible);
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
				if(i.equals(sede.getListaInsumosBodega().get(x)) ){
					for(int c : sede.cantidadInsumosBodega){
						if(sede.getCantidadInsumosBodega().get(x)!= 0){
							index = x;
							retorno = true;
							sedeATransferir = sede;
							precio = i.getPrecioCompra();
							break;
						}
					}
					
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
	static public void setEvaluacionesFinancieras(ArrayList<Evaluacionfinanciera> evaluaciones){evaluacionesFinancieras=evaluaciones;}
	static public ArrayList<Evaluacionfinanciera> getEvaluacionesFinancieras(){return evaluacionesFinancieras;}
	static public ArrayList<Empleado> getListaEmpleadosTotal(){return listaEmpleadosTotal;}
	static public ArrayList<Prenda> getPrendasInventadasTotal(){return prendasInventadasTotal;}
	static public void setPrendasInventadasTotal(ArrayList<Prenda> prendas){prendasInventadasTotal=prendas;} // Para serializacion
	
	public ArrayList<Integer> getProdAproximada(){
		return prodAproximada;
	}

	// ---Interacciones---
	// Interacción 2 de Gestion Humana
	// Retorna una lista con los roles a reemplazar y que sede debe reemplazar los empleados de cada rol, de ser posible. Luego también la cantidad de empleados que quedan por reemplazar.
	static public ArrayList<Object> obtenerNececidadTransferenciaEmpleados(ArrayList<Empleado> despedidos){ //Despedidos es A en el doc.
		ArrayList<Rol> rolesARevisar = new ArrayList<Rol>();
		ArrayList<Sede> sedeOrigen = new ArrayList<Sede>();
		for (Empleado emp : despedidos){
			if (!rolesARevisar.contains(emp.getRol())){
				rolesARevisar.add(emp.getRol());
				sedeOrigen.add(emp.getSede());
			}
		}
		
		ArrayList<Sede> transferirDe = new ArrayList<Sede>();
		ArrayList<Rol> rolesATransferir = new ArrayList<Rol>();


		for (int idxRol=0; idxRol<rolesARevisar.size();idxRol++){
			Rol rol = rolesARevisar.get(idxRol);

			revisarSedesDonadoras:
			for (Sede sede : listaSedes){
				if (sede.equals(sedeOrigen.get(idxRol))){
					continue; // Evitar donacion de la misma sede de origen.
				}
				switch(rol){
					case MODISTA:

					if (sede.cantidadPorRol(rol)!=0){
						int produccionTotal = 0;
						for (int nProduccion: sede.getProduccionAproximada()){
							produccionTotal += nProduccion;
						}
						int produccionPorModista = produccionTotal / sede.cantidadPorRol(rol);

						if (produccionPorModista<30){
							transferirDe.add(sede);
							rolesATransferir.add(rol);
							break revisarSedesDonadoras;
						}
					}
					break;
					
					case SECRETARIA:
					int ejecutivos = sede.cantidadPorRol(Rol.EJECUTIVO);
					int secretarias = sede.cantidadPorRol(Rol.SECRETARIA);
					int empleados = sede.listaEmpleado.size();
					if (!(empleados/secretarias > 18 || ejecutivos/secretarias > 2)){
						transferirDe.set(idxRol, sede);
						rolesATransferir.add(rol);
						break revisarSedesDonadoras;
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

	public int cantidadPorArea(Area area){
		int cantidad = 0;
		for (Empleado emp : listaEmpleado) {
			if (emp.getAreaActual() == area){
				cantidad++;
			}
		}
		return cantidad;
	}

	public String toString(){
		return nombre;
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
	public ArrayList<ArrayList<ArrayList<Integer>>> planProduccion(ArrayList<Maquinaria> maqDisponible, Fecha fecha, Scanner scanner){
		ArrayList<ArrayList<ArrayList<Integer>>> aProducirFinal = new ArrayList<>();
		ArrayList<ArrayList<Integer>> aProducir = new ArrayList<>();
		ArrayList<ArrayList<Integer>> listaEspera = new ArrayList<>();

		ArrayList<ArrayList<Integer>> listaEsperaVacia = new ArrayList<>();
		ArrayList<Integer> listaDeCeros = new ArrayList<>();
		ArrayList<Maquinaria> maqSedeP = new ArrayList<>();
		ArrayList<Maquinaria> maqSede2 = new ArrayList<>();
		int senal = 0;
		
		listaDeCeros.add(0, 0);
		listaDeCeros.add(1, 0);

		listaEsperaVacia.add(0, listaDeCeros);
		listaEsperaVacia.add(1, listaDeCeros);

			//dividir las maquinas disponibles por sedes:
		for(Maquinaria todMaquinas : maqDisponible){
			if(todMaquinas.getSede().getNombre().equalsIgnoreCase("Sede Principal")){
				maqSedeP.add(todMaquinas);
			} else {
				maqSede2.add(todMaquinas);
			}
		}

			//dividir las maquinas de cada sede por funcion:
		for(Maquinaria todMaqSedeP : maqSedeP){
			if(todMaqSedeP.esDeProduccion()){
				getlistaSedes().get(0).maqProduccion.add(todMaqSedeP);
			} else{
				getlistaSedes().get(0).maqOficina.add(todMaqSedeP);
			}
		}
		for(Maquinaria todMaqSede2 : maqSede2){
			if(todMaqSede2.esDeProduccion()){
				getlistaSedes().get(1).maqProduccion.add(todMaqSede2);
			} else{
				getlistaSedes().get(1).maqOficina.add(todMaqSede2);
			}
		}


		if(getlistaSedes().get(0).maqProduccion.size() >= 3){
			senal = 5;
		}
		if(getlistaSedes().get(1).maqProduccion.size() >= 3){
			senal = senal + 10;
		}

		if(senal == 5){
			Main.printsInt2(1);

			int opcion = 0;
			while(opcion != 1 && opcion != 2){
				opcion = scanner.nextInt();
				if(opcion == 1){
					//llamar a un metodo1: prodSedeP() para empezar a producir todo en la sede principal
					aProducir.add(0, prodSedeP(fecha));
					aProducir.add(1, listaDeCeros);
					
					aProducirFinal.add(0, aProducir);
					aProducirFinal.add(1, listaEsperaVacia);

				} else if(opcion == 2){
					//llamar a otro metodo2 para pasar la produccion a una lista de espera
					aProducir.add(0, calcProduccionSedes(fecha).get(0));
					aProducir.add(1, listaDeCeros);

					listaEspera.add(0, prodTransferida1(fecha));
					listaEspera.add(1, listaDeCeros);

					aProducirFinal.add(0, aProducir);
					aProducirFinal.add(1, listaEspera);
				} else{
					Main.printsInt2(2);
				}
			}
		} else if (senal == 10) {
			Main.printsInt2(3);

			int opcion = 0;
			while(opcion != 1 && opcion != 2){
				opcion = scanner.nextInt();
				if(opcion == 1){
					//llamar a un metodo3: prodSede2() para empezar a producir todo en la sede 2
					aProducir.add(0, listaDeCeros);
					aProducir.add(1, prodSede2(fecha));

					aProducirFinal.add(0, aProducir);
					aProducirFinal.add(1, listaEsperaVacia);
					
				} else if(opcion == 2){
					//llamar a otro metodo2 para pasar la produccion a una lista de espera
					aProducir.add(0, listaDeCeros);
					aProducir.add(1, calcProduccionSedes(fecha).get(1));

					listaEspera.add(0, listaDeCeros);
					listaEspera.add(1, prodTransferida2(fecha));

					aProducirFinal.add(0, aProducir);
					aProducirFinal.add(1, listaEspera);

				} else{
					Main.printsInt2(4);
				}
			}
		} else if(senal == 15){
			//aquí se produce todo entre las dos sedes
			int senalRec = sobreCargada(fecha);

			if(senalRec == 5){
				//sedeP sobrecargada, preguntar si quiere distribuir la produccion con la sede2(saldria al dia sig lo distribuido) o producirlo el mismo dia con un mayor costo
				Main.printsInt2(5);

				int opciom = 0;
				while(opciom != 1 && opciom !=2){
					opciom = scanner.nextInt();
					if(opciom == 1){
						int nuevosPantP = calcProduccionSedes(fecha).get(0).get(0) + (int) Math.ceil( (calcProduccionSedes(fecha).get(0).get(0) + calcProduccionSedes(fecha).get(1).get(0)) / 2 );
						int nuevosPant2 = calcProduccionSedes(fecha).get(1).get(0) + (int) Math.floor( (calcProduccionSedes(fecha).get(0).get(0) + calcProduccionSedes(fecha).get(1).get(0)) / 2 );

						int nuevasCamP = calcProduccionSedes(fecha).get(0).get(1) + (int) Math.ceil( (calcProduccionSedes(fecha).get(0).get(1) + calcProduccionSedes(fecha).get(1).get(1)) / 2 );
						int nuevasCam2 = calcProduccionSedes(fecha).get(1).get(1) + (int) Math.floor( (calcProduccionSedes(fecha).get(1).get(1) + calcProduccionSedes(fecha).get(0).get(1)) / 2 );

						ArrayList<Integer> loDeLaP = new ArrayList<>();
						ArrayList<Integer> loDeLa2 = new ArrayList<>();

						loDeLaP.add(0, nuevosPantP);
						loDeLaP.add(1, nuevasCamP);
						loDeLa2.add(0, nuevosPant2);
						loDeLa2.add(1, nuevasCam2);

						aProducir.add(0, loDeLaP);
						aProducir.add(1, loDeLa2);

						aProducirFinal.add(0, aProducir);
						aProducirFinal.add(1, listaEsperaVacia);
					} else if(opciom == 2){
						aProducir = calcProduccionSedes(fecha);

						aProducirFinal.add(0, aProducir);
						aProducirFinal.add(1, listaEsperaVacia);

					} else{
						Main.printsInt2(6);
					}
				}

			} else if(senalRec == 10){
				//sede2 sobrecargada, preguntar si quiere distribuir la produccion con la sedeP(saldria al dia sig lo distribuido) o producirlo el mismo dia con un mayor costo
				Main.printsInt2(7);

				int opciom = 0;
				while(opciom != 1 && opciom !=2){
					opciom = scanner.nextInt();
					if(opciom == 1){
						int nuevosPantP = calcProduccionSedes(fecha).get(0).get(0) + (int) Math.floor( (calcProduccionSedes(fecha).get(0).get(0) + calcProduccionSedes(fecha).get(1).get(0)) / 2 );
						int nuevosPant2 = calcProduccionSedes(fecha).get(1).get(0) + (int) Math.ceil( (calcProduccionSedes(fecha).get(0).get(0) + calcProduccionSedes(fecha).get(1).get(0)) / 2 );

						int nuevasCamP = calcProduccionSedes(fecha).get(0).get(1) + (int) Math.floor( (calcProduccionSedes(fecha).get(0).get(1) + calcProduccionSedes(fecha).get(1).get(1)) / 2 );
						int nuevasCam2 = calcProduccionSedes(fecha).get(1).get(1) + (int) Math.ceil( (calcProduccionSedes(fecha).get(1).get(1) + calcProduccionSedes(fecha).get(0).get(1)) / 2 );

						ArrayList<Integer> loDeLaP = new ArrayList<>();
						ArrayList<Integer> loDeLa2 = new ArrayList<>();

						loDeLaP.add(0, nuevosPantP);
						loDeLaP.add(1, nuevasCamP);
						loDeLa2.add(0, nuevosPant2);
						loDeLa2.add(1, nuevasCam2);

						aProducir.add(0, loDeLaP);
						aProducir.add(1, loDeLa2);

						aProducirFinal.add(0, aProducir);
						aProducirFinal.add(1, listaEsperaVacia);
					} else if(opciom == 2){
						aProducir = calcProduccionSedes(fecha);

						aProducirFinal.add(0, aProducir);
						aProducirFinal.add(1, listaEsperaVacia);
						
					} else{
						Main.printsInt2(8);
					}
				}
			} else if(senalRec == 15){
				//las dos sedes estan sobrecargadas, preguntar si quiere producirlas el otro dia, o todo el mismo dia con un mayor costo
				Main.printsInt2(9);

				int opciom = 0;
				while(opciom != 1 && opciom != 2){
					opciom = scanner.nextInt();
					if(opciom == 1){
						ArrayList<Integer> elGuardaPdeHoy = new ArrayList<>();
						ArrayList<Integer> elGuarda2deHoy = new ArrayList<>();
						ArrayList<Integer> elGuardaPdeManana = new ArrayList<>();
						ArrayList<Integer> elGuarda2deManana = new ArrayList<>();

						int pSedePespera = Math.max( 0, calcProduccionSedes(fecha).get(0).get(0) - 10*modistasQueHay().get(0) ); 
						int pSedeP = calcProduccionSedes(fecha).get(0).get(0) - pSedePespera;

						int cSedePespera = Math.max( 0, calcProduccionSedes(fecha).get(0).get(1) - 10*modistasQueHay().get(0) ); 
						int cSedeP = calcProduccionSedes(fecha).get(0).get(1) - cSedePespera;

						int pSede2espera = Math.max( 0, calcProduccionSedes(fecha).get(1).get(0) - 10*modistasQueHay().get(1) ); 
						int pSede2 = calcProduccionSedes(fecha).get(1).get(0) - pSede2espera;
						
						int cSede2espera = Math.max( 0, calcProduccionSedes(fecha).get(1).get(1) - 10*modistasQueHay().get(1) );
						int cSede2 = calcProduccionSedes(fecha).get(1).get(1) - cSede2espera;

						elGuardaPdeHoy.add(0, pSedeP);
						elGuardaPdeHoy.add(1, cSedeP);

						elGuarda2deHoy.add(0, pSede2);
						elGuarda2deHoy.add(1, cSede2);

						elGuardaPdeManana.add(0, pSedePespera);
						elGuardaPdeManana.add(1, cSedePespera);

						elGuarda2deManana.add(0, pSede2espera);
						elGuarda2deManana.add(1, cSede2espera);

						aProducir.add(0, elGuardaPdeHoy);
						aProducir.add(1, elGuarda2deHoy);
						listaEspera.add(0, elGuardaPdeManana);
						listaEspera.add(1, elGuarda2deManana);

						aProducirFinal.add(0, aProducir);
						aProducirFinal.add(1, listaEspera);
						
					} else if(opciom == 2){
						aProducir = calcProduccionSedes(fecha);

						aProducirFinal.add(0, aProducir);
						aProducirFinal.add(1, listaEsperaVacia);
					} else{
						Main.printsInt2(10);
					}
				}

			} else if(senalRec == 0){
				//NINGUNA SEDE SOBRECARGADA, RETORNAR PRODUCCION NORMAL, ES DECIR, CON LA LISTA DE ESPERA CON VALORES EN 0
				aProducir = calcProduccionSedes(fecha);

				aProducirFinal.add(0, aProducir);
				aProducirFinal.add(1, listaEsperaVacia);
			}

		} else{
			Main.printsInt2(11);
		}

		return aProducirFinal;
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

	public ArrayList<Insumo> insumnosPorNombre(ArrayList<String> nombres){
		ArrayList<Insumo> insumos = new ArrayList<>();
		for (String nombre : nombres){
			for (Insumo insumo : listaInsumosBodega){
				if (insumo.getNombre().equalsIgnoreCase(nombre)){
					insumos.add(insumo);
				}
			}
		}
		return insumos;
	}

	public ArrayList<Integer> modistasQueHay(){
		ArrayList<Integer> modistasEnCadaSede = new ArrayList<>();
		int enLaP = 0;
		int enLa2 = 0;

		for(Empleado empCreados : listaEmpleadosTotal){
			if(empCreados.getAreaActual().getNombre().equalsIgnoreCase("Corte") && empCreados.getSede().getNombre().equalsIgnoreCase("Sede Principal")){
				++enLaP;
			}
			if(empCreados.getAreaActual().getNombre().equalsIgnoreCase("Corte") && empCreados.getSede().getNombre().equalsIgnoreCase("Sede 2")){
				++enLa2;
			}
		}

		modistasEnCadaSede.add(0, enLaP);
		modistasEnCadaSede.add(1, enLa2);

		return modistasEnCadaSede;
	}

	public int sobreCargada(Fecha fecha){
		int senal = 0;

		if( ((calcProduccionSedes(fecha).get(0).get(0) + calcProduccionSedes(fecha).get(0).get(1)) / modistasQueHay().get(0)) > 10 ){
			senal = 5;
		} 
		if( ((calcProduccionSedes(fecha).get(1).get(0) + calcProduccionSedes(fecha).get(1).get(1)) / modistasQueHay().get(1)) > 10 ){
			senal = senal + 10;
		}

		return senal;
	}

	public ArrayList<Integer> prodTransferida1(Fecha fecha){
		ArrayList<Integer> prodaTransferir = new ArrayList<>();

		prodaTransferir.add(0, calcProduccionSedes(fecha).get(1).get(0) );
		prodaTransferir.add(1, calcProduccionSedes(fecha).get(1).get(1) );


		return prodaTransferir;
	}

	public ArrayList<Integer> prodTransferida2(Fecha fecha){
		ArrayList<Integer> prodaTransferir2 = new ArrayList<>();

		prodaTransferir2.add(0, calcProduccionSedes(fecha).get(0).get(0));
		prodaTransferir2.add(1, calcProduccionSedes(fecha).get(0).get(1));

		return prodaTransferir2;
	}
	
}