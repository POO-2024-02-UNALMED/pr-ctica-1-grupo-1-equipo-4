package gestorAplicacion.Bodega;
import gestorAplicacion.Administracion.Empleado;
import gestorAplicacion.Fecha;
import gestorAplicacion.Sede;
import java.util.ArrayList;

public class Pantalon extends Prenda {
    public Pantalon(Fecha fecha, Empleado modista, boolean descartada, boolean terminada, Sede sede){
        super(fecha, sede, "Pantalon", modista, descartada, terminada);
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
    private static ArrayList<Float> cantidadInsumo;
    private static ArrayList<String> tipoinsumo;
    
    public static ArrayList<String> getTipoInsumo(){return tipoinsumo;}
    public static ArrayList<Float> getCantidadInsumo(){return cantidadInsumo;}
}