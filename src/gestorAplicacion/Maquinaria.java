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
}