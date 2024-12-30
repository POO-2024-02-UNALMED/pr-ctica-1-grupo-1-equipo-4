import java.util.ArrayList;

public enum Membresia {
	ORO=new Membresia("Oro",0.30);
	PLATA= new Membresia("Plata",0.15);
	BRONCE= new Membresia("bronce",0.5);
	NULA= new Membresía ("Ninguna",0);
	
	private final String nombre;
    private final float porcentajeDescuento;
    private static ArrayList <Membresia> listaMembresias;
    
    Membresia() {
    	listaMembresias.add(this);
    }
    Rol (String nom, float dsc){
    	this.nombre=nom;
    	this.porcentajeDescuento=dsc;
    	listaMembresias.add(this);
    }
}