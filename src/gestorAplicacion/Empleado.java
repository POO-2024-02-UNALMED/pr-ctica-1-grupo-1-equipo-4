import java.util.ArrayList;

public class Empleado extends Persona{
	
	public static ArrayList <Empleado> listaEmpleados;
    private Area areaActual;
    String fechaContratacion;
    //LocalDate?
    int rendimiento;
    Sede sede;
    Maquinaria maquinaria;
    ArrayList(Area) areas;
    int traslados;
    
    Empleado(){
    	listaEmpleados.add(this);
    }
    
    public ArrayList despedirEmpleado(){

    }
}
