package gestorAplicacion.Bodega;
import gestorAplicacion.Administracion.Empleado;
import gestorAplicacion.Administracion.GastoMensual;
import gestorAplicacion.Administracion.Rol;
import gestorAplicacion.Fecha;
import gestorAplicacion.Sede;
import gestorAplicacion.Venta;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Prenda implements GastoMensual, Serializable{
    private static final long serialVersionUID = 1L;
    protected Fecha fechaFabricacion;
	protected String nombre;
    protected Empleado modista;
    protected boolean descartada;
    protected boolean terminada;
    protected Sede sede;
    protected static ArrayList<Maquinaria> maquinaria = new ArrayList<Maquinaria>();
    protected static ArrayList<Float> cantidadInsumo = new ArrayList<Float>();
    protected ArrayList<Insumo> insumo = new ArrayList<Insumo>();
    protected ArrayList<Integer> enStock; //Representa el inventario por sede
    // Esta lista es, en cada sede, cuanto hay en stock
    //de esta prenda. El indice es el mismo de la sede en la lista de sedes.
    protected float costoInsumos=0;
    protected int costoProduccion=0;
    protected long precio;
    //costos necesarios para calcular el precio de cada prenda
    protected static float porcentajeGanancia = 0.30f;  // Porcentaje que afecta la cantidad a producir
    //costos necesarios para calcular el precio de cada prenda
    private static ArrayList<Prenda> prendasInventadas = new ArrayList<Prenda>();
    // Porcentaje que afecta la cantidad a producir
    
    public Prenda(Fecha fecha, Sede sede, String nombre, Empleado modista, boolean descartada, boolean terminada, ArrayList<Insumo> insumos){
        fechaFabricacion=fecha;
        this.sede=sede;
        this.nombre=nombre;
        this.modista=modista;
        this.descartada=descartada;
        this.terminada=terminada;
        this.insumo=insumos;
        for (int i=0;i<insumos.size();i++){
            this.costoInsumos+=insumos.get(i).getPrecioIndividual();
        }
        Prenda.prendasInventadas.add(this);
        sede.getPrendasInventadas().add(this);
        if(descartada){modista.setPrendasDescartadas(modista.getPrendasDescartadas()+1);}
        else{modista.setPrendasProducidas(modista.getPrendasProducidas()+1);}
    }
    

	public static long gastoMensualClase(Fecha fecha){
		long gastoPrenda=0;
        long gastoActual=0;
        long gastoPasado=0;
		for (Prenda prenda:prendasInventadas){
				long [] lista=prenda.gastoMensualTipo(fecha, prenda.fechaFabricacion, prenda);
				gastoActual+=lista[0];
				gastoPasado+=lista[1];
				}
		if (gastoActual!=0){gastoPrenda=gastoActual;}else{gastoPrenda=gastoPasado;}
        return gastoPrenda;
    }

    // Retorna el pesimismo
    public static float prevenciones(float descuento, float nuevoDescuento,Fecha fecha){
        for(Sede sede : Sede.getlistaSedes()){
            for (Prenda prenda : sede.getPrendasInventadas()){
                if (descuento>0.0F || nuevoDescuento>0.0F){
                if (nuevoDescuento>0.0F){
                    Prenda.porcentajeGanancia-=Prenda.porcentajeGanancia*(1-nuevoDescuento);
                } 
                Venta.setPesimismo(Venta.getPesimismo()-0.05F);
            }
            else {Venta.setPesimismo(Venta.getPesimismo()+0.1F);}
        }}
        return Venta.getPesimismo();
        }
    
    //-------------------Getters y Setters-------------------
    public boolean getPrendasDescartadas(){return descartada;}
    public String getNombre(){return nombre;}
    public ArrayList<Insumo> getInsumo(){return insumo;}
    //public static ArrayList<String> getTipoInsumo(){return tipoInsumo;}
    public static ArrayList<Float> getCantidadInsumo(){return cantidadInsumo;}
    public static ArrayList<Prenda> getPrendasInventadas(){return prendasInventadas;}
    public float getCostoInsumos(){return costoInsumos;}
    public long getPrecio() {return this.precio;}

    public String toString(){
        return "La prenda de tipo "+nombre+" a cargo del modista "+modista.getNombre()+" se encuentra actualmente en la sede"+sede.getNombre();
    }
    
    public float calcularCostoInsumos() {
        this.costoInsumos = 0;
        for (int i = 0; i < insumo.size(); i++) {
               Insumo insumoI = insumo.get(i);
               float cantidad = cantidadInsumo.get(i);
               this.costoInsumos += insumoI.precioXUnidad * cantidad;
           } 
        return this.costoInsumos;
        }//En este método recorro cada índice de la lista de insumos, ya que la lista insumo y cantidadInsumo están relacionadas por índice
       //Obtengo el valor de cada una según el índice en el que se encuentre el recorrido del tamano de la lista insumo
       //Según el precio unitario del insumo, lo multiplico por la cantidad del insumo que requiere la prenda, estos cálculos los voy 
       //Sumando en el atributo de instancia costoInsumos por cada insumo en la lista y su cantidad respectiva
       
        public int calcularCostoProduccion(){
           int sumSalarios = 0;
           for(Empleado empleado: sede.getlistaEmpleados()) {
               if(empleado.getRol().equals(Rol.MODISTA)) {
                   sumSalarios += empleado.getRol().getSalarioInicial();
               }
           this.costoProduccion = Math.round(sumSalarios * 0.01f);
           
       }
           return this.costoProduccion;
       }//En este método sumo cada uno de los salarios de los modistas de la sede, a esta suma le saco el 1% y lo declaro como el costo de Producción
        
       public long calcularPrecio() {
           float costoTotal = this.costoInsumos + this.costoProduccion;
           double gananciaDeseada = costoTotal+ (costoTotal*Prenda.porcentajeGanancia);
           this.precio = Math.round(gananciaDeseada);
           return precio;
          }//Este método calcula el precio de la prenda haciendo uso de una suma entre los costos que se requirieron para la creación de la prenda
          //Luego esta suma la multiplica con el porcentaje de ganancia al cuál se le suma un 1 para sacar el porcentaje total que se desea calcular
         //Este 1 se suma para no solo obtener el valor de ganancia adicional a los costos, sino que sumar en definitiva los costos con el valor de
         //Ganancia y obtener el precio definitivo de la prenda.
       
      
}
