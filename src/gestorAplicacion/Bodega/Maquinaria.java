package gestorAplicacion.Bodega;
import gestorAplicacion.Administracion.Empleado;
import gestorAplicacion.Sede;

import java.net.ResponseCache;
import java.util.ArrayList;

public class Maquinaria {
	String nombre;
	Empleado user;
	int horasUso;
	boolean estado = false;
	// True if buena, False if dañada
	boolean asignable;
	boolean mantenimiento;
	Sede sede;
	long valor;
	int horasVisitaTecnico;
	int horaRevision;
	ArrayList<Repuesto> repuestos;
	ArrayList<Integer> horasUltimoCambio;

	public Maquinaria(String nombre, long valor, int horaRevision, ArrayList<Repuesto> repuestos){
		this.nombre = nombre;
		this.valor = valor;
		this.horaRevision = horaRevision;
		this.repuestos = repuestos;

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

	public void hacerMantenimiento(){
		ArrayList<Maquinaria> maqDisponibles = new ArrayList<>();	//listado temporal de maquinarias disponibles, el cual será pasado como argumento para la segunda interracion

		for(Sede cadaSede : Sede.getlistaSedes()){
			for(Maquinaria cadaMaquina : cadaSede.getlistaMaquinas()){
				//ver si las horas predeterminadas de revision no han sobrepasado las horas de uso de cada maquina
				if ((cadaMaquina.getHoraRevision() - cadaMaquina.getHorasUso()) > 0){
					cadaMaquina.mantenimiento = false; //eso quiere decir que no requiere manteminiento o revision
					for(Repuesto cadaRepuesto : cadaMaquina.getRepuestos()){
						if ((cadaRepuesto.getHorasDeVidaUtil() - cadaRepuesto.getHorasDeUso()) <= 0){
							//se debe reemplazar
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
	}

	public void cambioSioNo(){
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