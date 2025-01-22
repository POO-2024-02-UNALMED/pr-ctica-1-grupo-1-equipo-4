package gestorAplicacion.Bodega;
import gestorAplicacion.Fecha;
import java.util.ArrayList;

public class Repuesto extends Insumo { // Implementa serializable por repuesto
    private static final long serialVersionUID = 1L;
    //hereda el atributo nombre
    //hereda el atributo horasDeVidaUtil
    private static ArrayList<Repuesto> listadoRepuestos = new ArrayList<>();
    private ArrayList<Fecha> FechasCompra= new ArrayList<Fecha>();
    private ArrayList<Integer>  preciosCompra= new ArrayList<Integer>();
    private int horasDeUso;
    
    public ArrayList<Fecha> getFechasCompra(){return FechasCompra;}
    public void setFechasCompra(Fecha FechasCompra){this.FechasCompra.add(FechasCompra);}
    public ArrayList<Fecha> getPreciosCompra(){return FechasCompra;}
    public void setPreciosCompra(int precio){this.preciosCompra.add(precio); }
    
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
	public int calcularGastoMensual(Fecha fecha) {
        int gastoMensual=0;
        for (int i=0;i<FechasCompra.size();i++){
            if (FechasCompra.get(i).getAño()==fecha.getAño()&&FechasCompra.get(i).getMes()==fecha.getMes()){
            gastoMensual+=preciosCompra.get(i);}
        }
		return gastoMensual;
    }
}