package gestorAplicacion;

public enum Membresia {
	ORO ("Oro",0.30F),PLATA("Plata",0.15F), BRONCE("bronce",0.5F), NULA("Ninguna",0.0F);
	private final String nombre;
    private final float porcentajeDescuento;

    Membresia(String nom, float dsc){
    	this.nombre=nom;
    	this.porcentajeDescuento=dsc;
    }

	public float getPorcentajeDescuento(){return porcentajeDescuento;}
	}