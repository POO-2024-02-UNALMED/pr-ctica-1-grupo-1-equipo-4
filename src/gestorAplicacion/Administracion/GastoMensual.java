package gestorAplicacion.Administracion;

import gestorAplicacion.Fecha;
import gestorAplicacion.Bodega.Insumo;
import gestorAplicacion.Bodega.Maquinaria;
import gestorAplicacion.Bodega.Prenda;

public interface GastoMensual {
    public abstract int calcularGastoMensual();
    public default long[] gastoMensualTipo (Fecha fecha,Fecha fechaObjeto,Object objeto){
        long gastoActual=0;
        long gastoPasado=0;
        long[] gastoTotal= new long [2];
        if (fechaObjeto.getAño()==fecha.getAño()){
                if (fechaObjeto.getMes()==fecha.getMes()){
                    if (objeto instanceof Insumo){
                        Insumo insumo=(Insumo)objeto;
                        gastoActual+=insumo.calcularGastoMensual();
                        gastoTotal [0]=(gastoActual);
                    }
                    else if(objeto instanceof Prenda){
                        Prenda prenda=(Prenda)objeto;
                        gastoActual+=prenda.calcularGastoMensual();
                        gastoTotal [0]=gastoActual;
                    }}
                if (fechaObjeto.getMes()==fecha.getMes()-1){
                    if (objeto instanceof Insumo){
                        Insumo insumo=(Insumo)objeto;
                        gastoPasado+=insumo.calcularGastoMensual();
                        gastoTotal [1]=(gastoPasado);
                    }
                    else if(objeto instanceof Prenda){
                        Prenda prenda=(Prenda)objeto;
                        gastoPasado+=prenda.calcularGastoMensual();
                        gastoTotal [1]=(gastoPasado);
                    }
                }
            }
        return gastoTotal;
    }
    public static long gastosMensuales(Fecha fecha){
        long GastosMaquinaria=Maquinaria.gastoMensualClase();
        long GastosPrenda=Prenda.gastoMensualClase(fecha);
        long GastosBolsa=Insumo.gastoMensualClase(fecha);
        long GastosNomina=Empleado.gastoMensualClase();
        long suma=GastosMaquinaria+GastosPrenda+GastosBolsa+GastosNomina;
        return suma;}
}