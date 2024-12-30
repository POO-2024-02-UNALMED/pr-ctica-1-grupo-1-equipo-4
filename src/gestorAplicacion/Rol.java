import java.util.ArrayList;

public enum Rol {
	
	PRESIDENTE=new Rol("Presidente",100000);
	EJECUTIVO= new Rol("Ejecutivo",100000);
	ASISTENTE= new Rol("Asistente",100000);
	DISEÑADOR= new Rol("Diseñador",100000);
	MODISTA= new Rol("Modista",100000);
	SECRETARIA= new Rol("Secretaria",100000);
	PLANTA= new Rol("Planta",100000);
	
	private final String nombre;
    private final int salarioInicial;
    private static ArrayList <Rol> listaRoles;
    
    Rol() {
    	listaRoles.add(this);
    }
    Rol (String nom, int sl){
    	this.nombre=nom;
    	this.salarioInicial=sl;
    	listaRoles.add(this);
    }
}
