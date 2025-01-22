package gestorAplicacion.Bodega;

import java.util.ArrayList;

public class Repuesto extends Insumo { // Implementa serializable por repuesto
    private static final long serialVersionUID = 1L;
    //hereda el atributo nombre
    //hereda el atributo horasDeVidaUtil
    private static ArrayList<Repuesto> listadoRepuestos = new ArrayList<>();
    private int horasDeUso;
    

    public Repuesto(String nombre, int horasDeVidaUtil, Proveedor proveedor){
        super(nombre, horasDeVidaUtil, proveedor);
        listadoRepuestos.add(this);
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
    public static ArrayList<Repuesto> getListadoRepuestoss(){
        return listadoRepuestos;
    }

        //metodo para hacer una copia de un objeto de tipo Repuesto, con la misma inicializacion de atributos del que queremos copiar
    public Repuesto copiar(){
        return new Repuesto(this.nombre, this.horasDeVidaUtil, this.proveedor);
    }
	public int calcularGastoMensual() {
		return this.proveedor.getPrecio();
    }
}