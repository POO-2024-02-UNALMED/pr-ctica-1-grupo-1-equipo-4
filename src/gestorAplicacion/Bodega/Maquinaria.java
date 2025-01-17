package gestorAplicacion.Bodega;
import gestorAplicacion.Administracion.Empleado;
import gestorAplicacion.Sede;
import java.util.ArrayList;

public class Maquinaria {
	String nombre;
	Empleado user;
	int horasUso;
	boolean estado;
	// True if buena, False if dañada
	boolean asignable;
	boolean mantenimiento;
	Sede sede;
	long valor;
	int horasVisitaTecnico;
	int horaRevision;
	ArrayList<Repuesto> repuestos;
	ArrayList<Integer> horasUltimoCambio;


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
					
	public static void hacerMantenimiento(){
		for(Sede cadaSede : Sede.getlistaSedes()){
			for(Maquinaria cadaMaquinaria : cadaSede.getlistaMaquinas()){

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