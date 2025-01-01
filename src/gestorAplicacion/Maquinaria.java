package gestorAplicacion;
import java.util.ArrayList;

public class Maquinaria{
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
	static private ArrayList<Maquinaria> listaMaquinaria= new ArrayList<Maquinaria>();

	/*Retorna el valor de la maquinaria dañada asignada a un empleado, ayuda a Empleado.listaInicialDespedirEmpelado*/
	static public int remuneracionDanos(Empleado empleado){
		int remuneracion = 0;
		for (Maquinaria maq : Maquinaria.listaMaquinaria){
			if (maq.user == empleado && !maq.estado){
				remuneracion += maq.valor;
			}
		}
		return remuneracion;
	}
}