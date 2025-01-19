package gestorAplicacion.Bodega;
import gestorAplicacion.Administracion.Empleado;
import gestorAplicacion.Administracion.GastoMensual;
import gestorAplicacion.Fecha;
import gestorAplicacion.Sede;
import gestorAplicacion.Venta;
import java.util.ArrayList;

public abstract class Prenda implements GastoMensual{
    protected Fecha fechaFabricacion;
	protected String nombre;
    protected Empleado modista;
    protected boolean descartada;
    protected boolean terminada;
    protected Sede sede;
    protected static ArrayList<Maquinaria> maquinaria;
    protected static ArrayList<Float> cantidadInsumo;
    protected static ArrayList<Insumo> insumo;
    protected ArrayList<Integer> enStock; //Representa el inventario por sede
    // Esta lista es, en cada sede, cuanto hay en stock
    //de esta prenda. El indice es el mismo de la sede en la lista de sedes.
    protected float costoInsumos;
    protected int costoProduccion;
    //costos necesarios para calcular el precio de cada prenda
    protected int porcentajeGanancia;
    private static ArrayList<Prenda> prendasInventadas;
    // Porcentaje que afecta la cantidad a producir
    
    public Prenda(Fecha fecha, Sede sede, String nombre, Empleado modista, boolean descartada, boolean terminada){
        fechaFabricacion=fecha;
        this.sede=sede;
        this.nombre=nombre;
        this.modista=modista;
        this.descartada=descartada;
        this.terminada=terminada;
        prendasInventadas.add(this);
        Sede.getPrendasInventadas().add(this);
        if(descartada){modista.setPrendasDescartadas(modista.getPrendasDescartadas()+1);}
        else{modista.setPrendasDescartadas(modista.getPrendasProducidas()+1);}
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

    public static float prevenciones(float descuento, float nuevoDescuento,Fecha fecha){
        for(Sede sede : Sede.getlistaSedes()){
            for (Prenda prenda : sede.getPrendasInventadas()){
                if (descuento>0.0F || nuevoDescuento>0.0F){
                if (nuevoDescuento>0.0F){
                    prenda.porcentajeGanancia-=prenda.porcentajeGanancia*(1-nuevoDescuento);
                } 
                Venta.setPesimismo(Venta.getPesimismo()-0.05F);
            }
            else {Venta.setPesimismo(Venta.getPesimismo()+0.1F);}
        }}
        return Venta.getPesimismo();
        }

    public boolean getPrendasDescartadas(){return descartada;}
    public String getNombre(){return nombre;}
    public ArrayList<Insumo> getInsumo(){return insumo;}
    //public static ArrayList<String> getTipoInsumo(){return tipoInsumo;}
    public static ArrayList<Float> getCantidadInsumo(){return cantidadInsumo;}
    public static ArrayList<Prenda> getPrendasInventadas(){return prendasInventadas;}
    public float getCostoInsumos(){return costoInsumos;}
    
    public float calcularCostoInsumos() {
     float costoInsumos=0;	
     for (int i = 0; i < insumo.size(); i++) {
    	    Insumo insumoActual = insumo.get(i);
    	    float cantidadActual = cantidadInsumo.get(i);
    	    costoInsumos += insumoActual.precioCompra * cantidadActual;
    	} 
     return costoInsumos;
     }
 //Recorre el tamaño de la lista insumos, ya que sus índices están alineados a los de cantidadInsumo se accede a cada índice
 //para calcular el precio del ínsumo en cada índice y la cantidad que se necesita de este dependiendo de la prenda
    
}
