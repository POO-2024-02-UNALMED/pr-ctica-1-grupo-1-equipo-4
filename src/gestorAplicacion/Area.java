import java.util.ArrayList;

public enum Area {

	DIRECCION=new Area("Direccion");
	OFICINA= new Area("Oficina");
	VENTAS= new Area("Ventas");
	CORTE= new Area("Corte");
	
	private int rendimientoDeseado;
	private final String nombre;
    private static ArrayList <Area> listaAreas;
    
    Area() {
    	listaAreas.add(this);
    }
    Area (String nom){
    	this.nombre=nom;
    	listaAreas.add(this);
    }
    
    public static int rendimientoDeseadoActual(Sede sede){
    	for(Area a : listaAreas){
    		if (a.nombre == "Direccion") {
    			rendimientoDeseado=3/5;
    		else if(a.nombre == "Oficina") {
    			//rendimientoDeseado=promedio de ventas de la sede
    		}
    		else if (a.nombre == "Ventas") {
    			//rendimientoDeseado=promedio de ventas de la sede
    		}
    		else if (a.nombre == "Corte") {
    			//promedio de las prendas por empleado con atributo descartada==True
    		}
    		else {rendimientoDeseado=0;}
    	}
    	
    	
    }
}
