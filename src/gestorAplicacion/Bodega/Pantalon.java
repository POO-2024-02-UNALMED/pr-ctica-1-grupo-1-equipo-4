package gestorAplicacion.Bodega;
import gestorAplicacion.Administracion.Empleado;
import gestorAplicacion.Fecha;
import gestorAplicacion.Sede;
import java.util.ArrayList;
import java.util.Arrays;

public class Pantalon extends Prenda {
    private static final long serialVersionUID = 1L;
    public Pantalon(Fecha fecha, Empleado modista, boolean descartada, boolean terminada, Sede sede, ArrayList<Insumo> insumos){
        super(fecha, sede, "Pantalon", modista, descartada, terminada,insumos);
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
        for (Prenda pantalon:Sede.getPrendasInventadasTotal()){
            if (pantalon instanceof Pantalon){
                precios+=pantalon.calcularPrecio();
                cantidades++;
            }
        }
        long precioVenta=Math.round(precios/cantidades);
        //Se promedian todos los "precios por los que se deberían vender las prendas para que todas las camisas se vendan al mismo precio"
        return precioVenta;
    }

    // Usa el modista en el atributo de la clase Prenda
    @Override
    ArrayList<Object> siguientePaso() {
        ArrayList<Object> retorno = new ArrayList<Object>();
        switch (pasoActual) {
            case 1:
                retorno.add("Maquina de Corte");
                retorno.add(5);
                break;
            case 2:
                retorno.add("Maquina de Tijereado");
                retorno.add(2);
                break;
            case 3:
                retorno.add("Maquina de Coser Industrial");
                retorno.add(10);
                break;
            default:
                retorno.add("LISTO");
                terminada=true;
                break;
        }
        ultimoPaso=retorno;
        return retorno;
    }

    String realizarPaso(Empleado modista){
        this.modista=modista;
        float probabilidadDeExito = modista.getPericia();
        switch (pasoActual) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                probabilidadDeExito*=0.8;
        }
        pasoActual++;
        String retorno= "CONTINUAR";
        if (pasoActual>3){
            retorno="LISTO";
        }
        if (Math.random()>probabilidadDeExito){
            retorno="DESCARTAR";
        }
        return retorno;

    }


    private static ArrayList<Integer> cantidadInsumo=new ArrayList<Integer>();
    private static ArrayList<String> tipoinsumo=new ArrayList<String>();
    int pasoActual=1;
    public static ArrayList<String> maquinariaNecesaria = new ArrayList<String>(Arrays.asList("Maquina de Corte", "Maquina de Coser Industrial", "Maquina de Tijereado"));

    public static ArrayList<String> getTipoInsumo(){return tipoinsumo;}
    public static ArrayList<Integer> getCantidadInsumo(){return cantidadInsumo;}
    public static void setTipoInsumo(ArrayList<String> tipos){tipoinsumo=tipos;}
    public static void setCantidadInsumo(ArrayList<Integer> cantidades){cantidadInsumo=cantidades;}

    public static ArrayList <String> getMaquinariaNecesaria(){
        return maquinariaNecesaria;
    }

}