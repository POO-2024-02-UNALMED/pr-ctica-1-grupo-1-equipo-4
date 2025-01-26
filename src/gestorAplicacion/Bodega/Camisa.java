package gestorAplicacion.Bodega;

import gestorAplicacion.Administracion.Empleado;
import gestorAplicacion.Fecha;
import gestorAplicacion.Sede;
import java.util.ArrayList;
import java.util.Arrays;

public class Camisa extends Prenda {
    private static final long serialVersionUID = 1L;
    public static ArrayList<ArrayList<Insumo>> posiblesInsumosNecesarios = new ArrayList<ArrayList<Insumo>>();
    private static ArrayList<Integer> cantidadInsumo=new ArrayList<Integer>();
    private static ArrayList<String> tipoinsumo=new ArrayList<String>();
    int pasoActual=0;

    static public ArrayList<String> maquinariaNecesaria = new ArrayList<String>(Arrays.asList("Maquina de Corte", "Bordadora Industrial", "Maquina de Coser Industrial","Maquina de Termofijado", "Plancha Industrial"));
    public Camisa(Fecha fecha, Empleado modista, boolean descartada, boolean terminada, Sede sede, ArrayList<Insumo> insumos){
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
        long precioVenta=0;
        for (Prenda camisa:Prenda.getPrendasInventadas()){
            if (camisa instanceof Camisa){
                precios+=camisa.calcularPrecio();
                cantidades++;
            }
        }
        precioVenta=Math.round(precios/cantidades);
        //Se promedian todos los "precios por los que se deberían vender las prendas para que todas las camisas se vendan al mismo"
        return precioVenta;
    }

    @Override
    ArrayList<Object> siguientePaso() {
        ArrayList<Object> retorno = new ArrayList<Object>();
        switch (pasoActual) {
            case 1:
                retorno.add("Maquina de Corte");
                retorno.add(5);
                break;
            case 2:
                retorno.add("Maquina de Coser Industrial");
                retorno.add(10);
                break;
            case 3:
                retorno.add("Plancha Industrial");
                retorno.add(5);
                break;
            case 4:
                retorno.add("Bordadora Industrial");
                retorno.add(5);
                break;
            case 5:
                retorno.add("Maquina de Termofijado");
                retorno.add(10);
                break;
            default:
                retorno.add("LISTO");
                break;
        }
        ultimoPaso = retorno;
        return retorno;
    }

    @Override
    String realizarPaso(Empleado modista){
       this.modista=modista;
        float probabilidadDeExito = modista.getPericia();
        switch(pasoActual){
            case 1:
                break;
            case 2:
                probabilidadDeExito = modista.getPericia()*0.9f;
                break;
            case 3:
                probabilidadDeExito = modista.getPericia()*0.8f;
                break;
            case 4:
                break;
            case 5:
                probabilidadDeExito = modista.getPericia()*0.9f;
                break;

        }
        String retorno="CONTINUAR";
        pasoActual++;
        float suerte = (float) Math.random();
        if (pasoActual>4){;
            return "LISTO";
        }
        if(suerte>probabilidadDeExito){
            retorno = "DESCARTAR";
            descartada=true;
        }
        return retorno;
    }

    

    public static ArrayList<String> getTipoInsumo(){return tipoinsumo;}
    public static ArrayList<Integer> getCantidadInsumo(){return cantidadInsumo;}
    public static void setTipoInsumo(ArrayList<String> tipos){tipoinsumo=tipos;}
    public static void setCantidadInsumo(ArrayList<Integer> cantidades){cantidadInsumo=cantidades;}
    public static ArrayList<ArrayList<Insumo>> getPosiblesInsumosNecesarios() {
        return posiblesInsumosNecesarios;
    }
    public static ArrayList<String> getMaquinariaNecesaria() {
        return maquinariaNecesaria;
    }

    public static ArrayList<Insumo> getInsumosNecesariosAleatorios(){
        return getPosiblesInsumosNecesarios().get((int) Math.random());
    }
}

