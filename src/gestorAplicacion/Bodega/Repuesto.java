package gestorAplicacion.Bodega;

public class Repuesto {
	private final String nombre;
    private final int horasDeVidaUtil;
    private int horasDeUso;
    

    public Repuesto(String nombre, int horasDeVidaUtil){
        this.nombre = nombre;
        this.horasDeVidaUtil = horasDeVidaUtil;
    }

    public String getNombre(){
        return nombre;
    }
    public int getHorasDeVidaUtil(){
        return horasDeVidaUtil;
    }

    public void setHorasDeUso(){
        //aquí se modificaran las horas de uso de cada repuesto (segun lo que definamos en la clase MAQUINARIA sobre cuantas horas toma la fabricacion de cada tipo de prenda)
    }
    public int getHorasDeUso(){
        return horasDeUso;
    }

    public float calcularGastoMensual(){
        return 0.0F;
    }
}