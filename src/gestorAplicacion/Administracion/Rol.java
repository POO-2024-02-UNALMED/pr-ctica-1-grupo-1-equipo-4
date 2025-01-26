package gestorAplicacion.Administracion;

public enum Rol {
	
	PRESIDENTE (3000000),
	EJECUTIVO(2000000),
	ASISTENTE(1000000),
	DISEÑADOR(2000000),
	MODISTA(1000000),
	SECRETARIA(1000000),
	PLANTA(1500000),
	VENDEDOR(1000000);
	
	
    private final int salarioInicial;

    Rol (int sl){
    	this.salarioInicial=sl;
    }
    
    public final int getSalarioInicial() {
    	return salarioInicial;
    }
}
