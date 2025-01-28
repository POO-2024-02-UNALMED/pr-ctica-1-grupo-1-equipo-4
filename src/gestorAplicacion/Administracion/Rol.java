package gestorAplicacion.Administracion;

public enum Rol {
	
	PRESIDENTE (3_000_000),
	EJECUTIVO(2_000_000),
	ASISTENTE(1_000_000),
	DISEÑADOR(2_000_000),
	MODISTA(1_000_000),
	SECRETARIA(1_000_000),
	PLANTA(1_500_000),
	VENDEDOR(1_000_000);
	
	
    private final int salarioInicial;

    Rol (int sl){
    	this.salarioInicial=sl;
    }
    
    public final int getSalarioInicial() {
    	return salarioInicial;
    }
}
