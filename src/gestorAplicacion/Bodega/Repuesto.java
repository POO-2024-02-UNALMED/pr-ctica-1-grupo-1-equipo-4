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

        //metodo para hacer una copia de un objeto de tipo Repuesto, con la misma inicializacion de atributos del que queremos copiar
    public Repuesto copiar(){
        return new Repuesto(this.nombre, this.horasDeVidaUtil);
    }

    public float calcularGastoMensual(){
        return 0.0F;
    }
}