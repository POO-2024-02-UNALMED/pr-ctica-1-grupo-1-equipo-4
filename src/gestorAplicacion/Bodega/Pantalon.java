package gestorAplicacion.Bodega;
import gestorAplicacion.Administracion.Empleado;
import gestorAplicacion.Fecha;
import gestorAplicacion.Sede;
import java.util.ArrayList;

public class Pantalon extends Prenda {
    private static final long serialVersionUID = 1L;
    public Pantalon(Fecha fecha, Empleado modista, boolean descartada, boolean terminada, Sede sede, ArrayList<Insumo> insumos){
        super(fecha, sede, "Camisa", modista, descartada, terminada,insumos);
    }
    @Override
    public int calcularGastoMensual() {
        int gasto=0;
        for (int i=0; i<insumo.size();i++){
            Insumo tipo=insumo.get(i);
            float cantidad=cantidadInsumo.get(i);
            gasto+=Math.round(tipo.getPrecioIndividual()*cantidad);
        }
        return gasto;
    }

    public static long PrecioVenta(){
        long precios=0;
        int cantidades=0;
        for (Prenda pantalon:Prenda.getPrendasInventadas()){
            if (pantalon instanceof Pantalon){
                precios+=pantalon.calcularPrecio();
                cantidades++;
            }
        }
        long precioVenta=Math.round(precios/cantidades);
        //Se promedian todos los "precios por los que se deberían vender las prendas para que todas las camisas se vendan al mismo precio"
        return precioVenta;
    }
    private static ArrayList<Float> cantidadInsumo=new ArrayList<Float>();
    private static ArrayList<String> tipoinsumo=new ArrayList<String>();
    
    public static ArrayList<String> getTipoInsumo(){return tipoinsumo;}
    public static ArrayList<Float> getCantidadInsumo(){return cantidadInsumo;}
    public static void setTipoInsumo(ArrayList<String> tipos){tipoinsumo=tipos;}
    public static void setCantidadInsumo(ArrayList<Float> cantidades){cantidadInsumo=cantidades;}
}