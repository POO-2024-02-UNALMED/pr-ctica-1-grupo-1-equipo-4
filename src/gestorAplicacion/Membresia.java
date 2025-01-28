/* Equipo 4 grupo 1
 * Clase que define los tipos de membresia que puede tener un cliente
 */
package src.gestorAplicacion;

public enum Membresia {
	ORO (0.30F),PLATA(0.15F), BRONCE(0.5F), NULA(0.0F);
    private final float porcentajeDescuento;

    Membresia(float dsc){
    	this.porcentajeDescuento=dsc;
    }

	public float getPorcentajeDescuento(){return porcentajeDescuento;}
	}