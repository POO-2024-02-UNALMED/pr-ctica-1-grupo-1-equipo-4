package gestorAplicacion.Bodega;
import gestorAplicacion.Administracion.Empleado;
import uiMain.Main;
import gestorAplicacion.Sede;
import gestorAplicacion.Fecha;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class Maquinaria implements Serializable{
	private static final long serialVersionUID = 1L;
	String nombre;
	Empleado user;
	int horasUso;
	boolean estado = true;
	// True if buena, False if dañada
	boolean asignable;
	boolean mantenimiento = false;
	public Sede sede;
	long valor;
	int horasVisitaTecnico;
	int horaRevision;
	ArrayList<Repuesto> repuestos=new ArrayList<Repuesto>();

	ArrayList<Proveedor> listProveedoresBaratos = new ArrayList<Proveedor>();
	Fecha ultFechaRevision;

	public Maquinaria(){
		this.nombre = "maquinaEjemplo";
	}

	public Maquinaria(String nombre, long valor, int horaRevision, ArrayList<Repuesto> repuestos, Sede sede){
		this.nombre = nombre;
		this.valor = valor;
		this.horaRevision = horaRevision;
		this.repuestos = repuestos;
		this.sede = sede;
		Sede.getlistaSedes().get(0).getlistaMaquinas().add(this);
	}
	public Maquinaria(String nombre, long valor, int horaRevision, ArrayList<Repuesto> repuestos, Sede sede, int prueba){
		this.nombre = nombre;
		this.valor = valor;
		this.horaRevision = horaRevision;
		this.repuestos = repuestos;
		this.sede = sede;
		Sede.getlistaSedes().get(1).getlistaMaquinas().add(this);
	}
		//sobrecarga de constructores para ejemplidicar la interaccion1 de produccion
	public Maquinaria(String nombre, long valor, int horaRevision, ArrayList<Repuesto> repuestos, Sede sede, String s){
		this.nombre = nombre;
		this.valor = valor;
		this.horaRevision = horaRevision;
		this.repuestos = repuestos;
		this.sede = sede;
		Sede.getlistaSedes().get(0).getlistaMaquinas().add(this);
		this.horasUso = 1000000;
	}
	public Maquinaria(String nombre, long valor, int horaRevision, ArrayList<Repuesto> repuestos, Sede sede, boolean bool){
		this.nombre = nombre;
		this.valor = valor;
		this.horaRevision = horaRevision;
		this.repuestos = repuestos;
		this.sede = sede;
		Sede.getlistaSedes().get(1).getlistaMaquinas().add(this);
		this.horasUso = 1000000;
	}

		//metodo para hacer una copia de un objeto de tipo Maquinaria, con la misma inicializacion de atributos del que queremos copiar
	public Maquinaria copiar(){
		ArrayList<Repuesto> Nuevosrepuestos=new ArrayList<>();
		for (Repuesto rep:repuestos){
			Nuevosrepuestos.add(rep.copiar());
		}
		return new Maquinaria(this.nombre, this.valor, this.horaRevision, Nuevosrepuestos, this.sede);
	}

	public Maquinaria copiar(int pruebaa){
		ArrayList<Repuesto> Nuevosrepuestos=new ArrayList<>();
		for (Repuesto rep:repuestos){
			Nuevosrepuestos.add(rep.copiar());
		}
		return new Maquinaria(this.nombre, this.valor, this.horaRevision, Nuevosrepuestos, this.sede, 1);
	}

	public static long gastoMensualClase(Fecha fecha){
		long gastoMaquinaria=0;
		for (Sede sede:Sede.getlistaSedes()){
			for (Maquinaria maquinaria : sede.getlistaMaquinas()){
				for (Repuesto repuesto:maquinaria.repuestos){
					gastoMaquinaria+=repuesto.calcularGastoMensual(fecha);
				}
			}
		}
        return gastoMaquinaria;
    }

	/*Retorna el valor de la maquinaria dañada asignada a un empleado, ayuda a Empleado.listaInicialDespedirEmpelado*/
	static public int remuneracionDanos(Empleado empleado){
		int remuneracion = 0;
		for (Maquinaria maq : empleado.getSede().getlistaMaquinas()){
			if (maq.user==empleado  && maq.estado){
				remuneracion += maq.valor;
			}
		}
		return remuneracion;
	}

	static public void liberarMaquinariaDe(Empleado empleado){
		for (Maquinaria maq : empleado.getSede().getlistaMaquinas()){
			if (maq.user== empleado){
				maq.user = null;
			}
		}
	}
	
	public String getNombre(){
		return nombre;
	}

	public ArrayList<Repuesto> getRepuestos(){
		return repuestos;
	}
	public void setRepuestos(Repuesto repaCambiar){
		this.repuestos.remove(repaCambiar);
	}

	public int getHoraRevision(){
		return horaRevision;
	}

	public int getHorasUso(){
		return horasUso;
	}

	public Sede getSede(){
		return this.sede;
	}

	public ArrayList<Maquinaria> agruparMaquinasDisponibles(Fecha fecha){
		ArrayList<Maquinaria> maqDisponibles = new ArrayList<>();	//listado temporal de maquinarias disponibles, el cual será pasado como argumento para la segunda interracion
		ArrayList<Proveedor> todosProvBaratos = new ArrayList<>();
		Main main = new Main();
		boolean encontrado = false;
		Proveedor proveedorBarato=null;
		for(Sede cadaSede : Sede.getlistaSedes()){
			for(Maquinaria cadaMaquina : cadaSede.getlistaMaquinas()){
				//ver si las horas predeterminadas de revision no han sobrepasado las horas de uso de cada maquina
				if ((cadaMaquina.getHoraRevision() - cadaMaquina.getHorasUso()) > 0){
					cadaMaquina.mantenimiento = false; //eso quiere decir que no requiere manteminiento o revision
					for(Repuesto cadaRepuesto : cadaMaquina.getRepuestos()){
						if ((cadaRepuesto.getHorasDeVidaUtil() - cadaRepuesto.getHorasDeUso()) <= 0){
							Main.printsInt1(1, cadaRepuesto);
							//se debe reemplazar
							//llamando al metodo que encuentra los proveedores mas baratos de los repuestos existentes
							todosProvBaratos = encontrarProveedoresBaratos();
							ArrayList<Proveedor> listaProveedores = Proveedor.getListaProveedores();
							for(Proveedor elMasEconomico : todosProvBaratos){
								if(elMasEconomico.getInsumo().getNombre().equalsIgnoreCase(cadaRepuesto.getNombre()) ){
									proveedorBarato = elMasEconomico;
									Main.recibeProveedorB(proveedorBarato);
									Main.printsInt1(2, cadaRepuesto);
									break;
								}
							}
							//procederemos a comprarlo preguntandole al usuario de cual sede quiere restar la money
							//pero antes hay que ver si la plata de alguna de las dos sedes creadas alcanza para comprarlo
							for(Sede sedeCreada : Sede.getlistaSedes()){
								//lo siguiente dira que si la plata de cualquiera de las sedes es mayor a lo que vale el repuesto
								//mas barato que requerimos, se procede a hacer la eleccion de cual sede se quiere descontar la plata:
								if(sedeCreada.getCuentaSede().getAhorroBanco() >= proveedorBarato.getInsumo().getPrecioIndividual()){
									//se llamara al metodo dondeRetirar() en donde se le pregunta al usuario de cual sede
									//quiere retirar la plata
									main.dondeRetirar();
									//AHORA FALTA QUITAR EL REPUESTO QUE NO SIRVE DEL ARRAYLIST DE REPUESTOS DE LA MAQUINA Y
									
									// Aquí quitamos el repuesto que no sirve del ArrayList de repuestos de la máquina y del de los repuestos creados
									cadaMaquina.setRepuestos(cadaRepuesto);
									Repuesto.setListadoRepuestos(cadaRepuesto);

									//AGREGAR UNA COPIA DEL REPUESTO A DICHO ARRAYLIST DE REPUESTOS DE LA MAQUINA AFECTADA
									cadaMaquina.getRepuestos().add(cadaRepuesto.copiar(proveedorBarato));
									cadaRepuesto.setPrecioCompra(proveedorBarato.getPrecio());
									cadaRepuesto.setFechasCompra(fecha);
									
									Main.printsInt11(cadaRepuesto, cadaMaquina, cadaSede, 1);

									encontrado = true;
									break;
								}	
							}
							if (!encontrado) {
								Main.printsInt11(cadaRepuesto, cadaMaquina, cadaSede, 2);
								cadaRepuesto.setEstado();
								Main.printsInt11(cadaRepuesto, cadaMaquina, cadaSede, 3);
							}
						}
					}
				} else{
					cadaMaquina.mantenimiento = true;
					cadaMaquina.ultFechaRevision = fecha;
					Main.printsInt111(cadaMaquina, 4);
				}

				int pista = 0;
				for(Repuesto rep : cadaMaquina.getRepuestos()){
					if(rep.isEstado() == true){
						++pista;
					}
				}
				if(cadaMaquina.getRepuestos().size() == pista){
					cadaMaquina.estado = true;
				} else{
					cadaMaquina.estado = false;
				}
				
				//añadir esta maquina a la lista temporal de las maquinas que estan disponibles
				if(cadaMaquina.mantenimiento == false && cadaMaquina.estado == true){
					maqDisponibles.add(cadaMaquina);
				}

				cadaMaquina.mantenimiento = false; //pq cuando vuelva a ser usada ya se habra arreglado
			}
		}

		return maqDisponibles;
	}

	public ArrayList<Proveedor> encontrarProveedoresBaratos(){
		for(Repuesto cadaRepuesto : Repuesto.getListadoRepuestoss()){
			Proveedor proveedorBarato;
			proveedorBarato = null;
			for(Proveedor proveedores : Proveedor.getListaProveedores()){
				//ver si el nombre del repuesto o insumo es igual al nombre del insumo que tiene el proveedor en su atributo insumo
				//o sea, que vende el proveedor:
				if(proveedores.getInsumo().getNombre().equalsIgnoreCase(cadaRepuesto.getNombre())){
					//con estos if se determina cual proveedor vende mas barato el insumo a reemplazar
					//y se guarda en la variable proveedorBarato:
					if(proveedorBarato == null){
						proveedorBarato = proveedores;
					} else if(proveedores.getInsumo().getPrecioIndividual() <= proveedorBarato.getInsumo().getPrecioIndividual()){
						proveedorBarato = proveedores;
					}

				}
			}

			listProveedoresBaratos.add(proveedorBarato);
		}

		return listProveedoresBaratos;
	}

	static public void asignarMaquinaria(Empleado emp){
		
		ArrayList<String> maquinariaPorAsignar = new ArrayList<>(emp.getAreaActual().getMaquinariaNecesaria());
		for (Maquinaria maq : emp.getSede().getlistaMaquinas()){
			if(maquinariaPorAsignar.contains(maq.nombre) || maq.user==null){
				maq.user = emp;
				maquinariaPorAsignar.remove(maq.nombre);
				break;
			}
		}
	}

	public String toString(){
		return "La "+nombre+" operada por "+user.getNombre()+" ubicada en la sede "+sede.getNombre()+" tiene "+horasUso+" horas de uso";
	}

	// Metodo auxiliar de Prenda.producirListaPrendas
	static Maquinaria seleccionarDeTipo(Sede sede,String tipo){
		Collections.shuffle(sede.getlistaMaquinas());
		for (Maquinaria maq : sede.getlistaMaquinas()){
			if (maq.nombre.equals(tipo)){
				return maq;
			}
		}
		return null;
	}

	void usar(int horas){
		horasUso+=horas;
		for (Repuesto repuesto:repuestos){
			repuesto.usar(horas);
		}
	}

	// Auxiliar de Sede.planProduccion
	public boolean esDeProduccion(){
		if(Camisa.getMaquinariaNecesaria().contains(getNombre()) || Pantalon.getMaquinariaNecesaria().contains(getNombre())){ 
			return true;
		}
		return false;
	}

}