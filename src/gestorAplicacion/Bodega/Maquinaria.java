package gestorAplicacion.Bodega;
import gestorAplicacion.Administracion.Empleado;
import uiMain.Main;
import gestorAplicacion.Sede;

import java.io.Serializable;
import java.net.ResponseCache;
import java.util.ArrayList;

public class Maquinaria implements Serializable{
	private static final long serialVersionUID = 1L;
	String nombre;
	Empleado user;
	int horasUso;
	boolean estado = false;
	// True if buena, False if dañada
	boolean asignable;
	boolean mantenimiento;
	public Sede sede;
	long valor;
	int horasVisitaTecnico;
	int horaRevision;
	ArrayList<Repuesto> repuestos;
	ArrayList<Integer> horasUltimoCambio;

	Proveedor proveedorBarato;
	ArrayList<Proveedor> listProveedoresBaratos = new ArrayList<>();

	public Maquinaria(){
		this.nombre = "maquinaEjemplo";
	}

	public Maquinaria(String nombre, long valor, int horaRevision, ArrayList<Repuesto> repuestos){
		this.nombre = nombre;
		this.valor = valor;
		this.horaRevision = horaRevision;
		this.repuestos = repuestos;

	}

		//metodo para hacer una copia de un objeto de tipo Maquinaria, con la misma inicializacion de atributos del que queremos copiar
	public Maquinaria copiar(){
		return new Maquinaria(this.nombre, this.valor, this.horaRevision, this.repuestos);
	}

	public static long gastoMensualClase(){
		long gastoMaquinaria=0;
		for (Sede sede:Sede.getlistaSedes()){
			for (Maquinaria maquinaria : sede.getlistaMaquinas()){
				for (int i=0;i<maquinaria.repuestos.size();i++){
					int veces=Math.round(744/maquinaria.horasUltimoCambio.get(i));
					gastoMaquinaria+=maquinaria.repuestos.get(i).calcularGastoMensual()*veces;
					
				}
			}
		}
        return gastoMaquinaria;
    }

	/*Retorna el valor de la maquinaria dañada asignada a un empleado, ayuda a Empleado.listaInicialDespedirEmpelado*/
	static public int remuneracionDanos(Empleado empleado){
		int remuneracion = 0;
		for (Maquinaria maq : empleado.getSede().getlistaMaquinas()){
			if (maq.user.equals(empleado)  && maq.estado){
				remuneracion += maq.valor;
			}
		}
		return remuneracion;
	}

	static public void liberarMaquinariaDe(Empleado empleado){
		for (Maquinaria maq : empleado.getSede().getlistaMaquinas()){
			if (maq.user.equals(empleado)){
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

	public int getHoraRevision(){
		return horaRevision;
	}

	public int getHorasUso(){
		return horasUso;
	}


	public ArrayList<Maquinaria> agruparMaquinasDisponibles(){
		ArrayList<Maquinaria> maqDisponibles = new ArrayList<>();	//listado temporal de maquinarias disponibles, el cual será pasado como argumento para la segunda interracion
		ArrayList<Proveedor> todosProvBaratos = new ArrayList<>();
		Main main = new Main();
		boolean encontrado = false;

		for(Sede cadaSede : Sede.getlistaSedes()){
			for(Maquinaria cadaMaquina : cadaSede.getlistaMaquinas()){
				//ver si las horas predeterminadas de revision no han sobrepasado las horas de uso de cada maquina
				if ((cadaMaquina.getHoraRevision() - cadaMaquina.getHorasUso()) > 0){
					cadaMaquina.mantenimiento = false; //eso quiere decir que no requiere manteminiento o revision
					for(Repuesto cadaRepuesto : cadaMaquina.getRepuestos()){
						if ((cadaRepuesto.getHorasDeVidaUtil() - cadaRepuesto.getHorasDeUso()) <= 0){
							//se debe reemplazar
							//llamando al metodo que encuentra los proveedores mas baratos de los repuestos existentes
							todosProvBaratos = encontrarProveedoresBaratos();
							for(Proveedor elMasEconomico : todosProvBaratos){
								proveedorBarato = null;
								if(elMasEconomico.getInsumo().getNombre().equalsIgnoreCase(cadaRepuesto.getNombre())){
									proveedorBarato = elMasEconomico;
									Main.recibeProveedorB(proveedorBarato);
									break;
								}
							}
							//procederemos a comprarlo preguntandole al usuario de cual sede quiere restar la money
							//pero antes hay que ver si la plata de alguna de las dos sedes creadas alcanza para comprarlo
							for(Sede sedeCreada: Sede.getlistaSedes()){
								//lo siguiente dira que si la plata de cualquiera de las sedes es mayor a lo que vale el repuesto
								//mas barato que requerimos, se procede a hacer la eleccion de cual sede se quiere descontar la plata:
								if(sedeCreada.getCuentaSede().getAhorroBanco() >= proveedorBarato.getInsumo().getPrecioIndividual()){
									//se llamara al metodo dondeRetirar() en donde se le pregunta al usuario de cual sede
									//quiere retirar la plata
									main.dondeRetirar();
									//AHORA FALTA QUITAR EL REPUESTO QUE NO SIRVE DEL ARRAYLIST DE REPUESTOS DE LA MAQUINA Y
									//AGREGAR UNA COPIA DEL REPUESTO A DICHO ARRAYLIST DE REPUESTOS DE LA MAQUINA AFECTADA
									
									encontrado = true;
									break;
								}	
							}
							if (!encontrado) {
								System.out.println("Ninguna de las sedes cuenta con dinero suficiente, considere pedir un prestamo.");
							}
						}
					}
				} else{
					cadaMaquina.mantenimiento = true;
					// ver como hacer para realizar la revision y que la maquina pueda volver a ser utilizada
				}
				
				//añadir esta maquina a la lista temporal de las maquinas que estan disponibles
				if(cadaMaquina.mantenimiento == false){
					maqDisponibles.add(cadaMaquina);
				}
			}
		}

		return maqDisponibles;
	}

	public ArrayList<Proveedor> encontrarProveedoresBaratos(){

		for(Repuesto cadaRepuesto : Repuesto.getListadoRepuestoss()){
			proveedorBarato = null;
			for(Proveedor proveedores : Proveedor.getListaProveedores()){
				//ver si el nombre del repuesto o insumo es igual al nombre del insumo que tiene el proveedor en su atributo insumo
				//o sea, que vende el proveedor:
				if(proveedores.getInsumo().getNombre().equalsIgnoreCase(cadaRepuesto.getNombre())){
					//con estos if se determina cual proveedor vende mas barato el insumo a reemplazar
					//y se guarda en la variable proveedorBarato:
					if(proveedorBarato == null){
						proveedorBarato = proveedores;
					}
					else if(proveedores.getInsumo().getPrecioIndividual() <= proveedorBarato.getInsumo().getPrecioIndividual()){
						proveedorBarato = proveedores;
					}

				}
			}

			listProveedoresBaratos.add(proveedorBarato);
		}	
		proveedorBarato = null;

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
}