package gestorAplicacion.Administracion;

import gestorAplicacion.Bodega.Insumo;
import gestorAplicacion.Bodega.Maquinaria;
import gestorAplicacion.Bodega.Prenda;
import gestorAplicacion.Fecha;

public interface GastoMensual {
    public abstract int calcularGastoMensual();
    public default long[] gastoMensualTipo (Fecha fecha,Fecha fechaObjeto,GastoMensual objeto){
        long gastoActual=0;
        long gastoPasado=0;
        long[] gastoTotal= new long [2];
        if (fechaObjeto.getAño()==fecha.getAño()){
                if (fechaObjeto.getMes()==fecha.getMes()){
                        gastoActual+=objeto.calcularGastoMensual();
                        gastoTotal [0]=gastoActual;}
                if (fechaObjeto.getMes()==fecha.getMes()-1){
                        gastoPasado+=objeto.calcularGastoMensual();
                        gastoTotal [1]=(gastoPasado);
                    }
                }
        return gastoTotal;
    }
    public static long gastosMensuales(Fecha fecha){
        long GastosMaquinaria=Maquinaria.gastoMensualClase(fecha);
        long GastosNomina=Empleado.gastoMensualClase();
        long GastoBolsa=Insumo.gastoMensualClase(fecha);
        long suma=GastosMaquinaria+GastosNomina+GastoBolsa;
        return suma;
    }
}