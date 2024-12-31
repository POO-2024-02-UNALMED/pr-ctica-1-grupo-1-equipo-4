package gestorAplicacion;
import java.util.ArrayList;

public class Empleado extends Persona{
	
	public static ArrayList <Empleado> listaEmpleados;
    private Area areaActual;
    String fechaContratacion;
    //LocalDate?
    int rendimiento;
    Sede sede;
    Maquinaria maquinaria;
    ArrayList<Area> areas;
    int traslados;
    
    Empleado(){
    	listaEmpleados.add(this);
    }
    
    public ArrayList <Empleado> despedirEmpleado(){
        ArrayList <Empleado> listaADespedir = null;
        for (Sede sede : Sede.listaSedes){
            for (Empleado emp : sede.listaEmpleado){
                
            }
        }
        return listaADespedir;
    }

    public Area getAreaActual(){
        return areaActual;
    }
}
