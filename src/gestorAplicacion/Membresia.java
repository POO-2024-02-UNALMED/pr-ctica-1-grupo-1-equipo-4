import java.util.ArrayList;

public enum Membresia {
	ORO ("Oro",0.30),PLATA("Plata",0.15), BRONCE("bronce",0.5), NULA("Ninguna",0);
	private final String nombre;
    private final double porcentajeDescuento;

    Membresia(String nom, double dsc){
    	this.nombre=nom;
    	this.porcentajeDescuento=dsc;
    }
}