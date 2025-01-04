package gestorAplicacion;

public enum Rol {
	
	PRESIDENTE ("Presidente",100000),
	EJECUTIVO("Ejecutivo",100000),
	ASISTENTE("Asistente",100000),
	DISEÑADOR("Diseñador",100000),
	MODISTA("Modista",100000),
	SECRETARIA("Secretaria",100000),
	PLANTA("Planta",100000),
	VENDEDOR("Vendedor",100000);
	
	private final String nombre;
    private final long salarioInicial;

    Rol (String nom, long sl){
    	this.nombre=nom;
    	this.salarioInicial=sl;
    }
    
    public final String getNombre() {
    	return nombre;
    }
    
    public final long getSalarioInicial() {
    	return salarioInicial;
    }
}
