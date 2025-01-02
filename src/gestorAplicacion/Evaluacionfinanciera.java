package gestorAplicacion;
import java.util.ArrayList;

public class Evaluacionfinanciera {
	static ArrayList <Evaluacionfinanciera> historialEvaluaciones;
	static ArrayList <Deuda> deudas;
	//deudaBanco+deudaProveedores
	long pagoPersonas;
	double balance;
	boolean proyección;
	Empleado presidente;

	Evaluacionfinanciera(double balance,Empleado presidente){
		if (presidente.getAreaActual()==Area.DIRECCION && presidente.getRol()==Rol.PRESIDENTE){
			this.presidente=presidente;
			this.balance=balance;
			historialEvaluaciones.add(this);
		}
	}
}