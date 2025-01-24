package gestorAplicacion.Administracion;
import java.io.Serializable;
import java.util.ArrayList;

import gestorAplicacion.Fecha;
import gestorAplicacion.Sede;
import gestorAplicacion.Venta;

public class Evaluacionfinanciera implements Serializable {
	private static final long serialVersionUID = 1L;
	private static ArrayList <Evaluacionfinanciera> historialEvaluaciones = new ArrayList<Evaluacionfinanciera>();
	//deudaBanco+deudaProveedores
	private long pagoPersonas;
	private double balance;
	private boolean proyeccion;
	private Empleado presidente;

	public String Informe(){
		return "El monto del balance a cargo de: "+presidente+" fue de: $"+balance+" pesos";
	}

	public Evaluacionfinanciera(double balance,Empleado presidente){
		this(balance);
		if (presidente.getAreaActual().equals(Area.DIRECCION)){
			this.presidente=presidente;
		}
	}

	public Evaluacionfinanciera(double balance){
		this.balance=balance;
		historialEvaluaciones.add(this);
	}

	public static long estimadoVentasGastos(Fecha fechaActual,float porcentaje, Evaluacionfinanciera balanceAnterior){
		long montoVentasPasado=0;
		for (Sede sede : Sede.getlistaSedes()){
			for (Venta venta : sede.getHistorialVentas()){
				if (fechaActual.compararAño(fechaActual.getAño(),venta.getFechaVenta().getAño()) && fechaActual.compararMes(fechaActual.getAño()-1,venta.getFechaVenta().getAño())){
					montoVentasPasado+=venta.getsubtotal()+venta.getCostoEnvio();
				}
			}}
		//Predecimos las ventas con un porcentaje de fidelidad 
		//(porcentaje de las ventas que se pueden dar por sentado y se mantienen de un mes al otro)
		float porcentajeFidelidadOro=porcentaje;
		if (porcentaje==0.0F){porcentajeFidelidadOro=0.9F;}
		//Preguntamos al usuario si desea cambiar el de Oro
		float porcentajeFidelidadPlata=porcentajeFidelidadOro-0.2F;
		float porcentajeFidelidadBronce=porcentajeFidelidadOro-0.4F;
		float porcentajeFidelidadNull=porcentajeFidelidadOro-0.6F;
		float prediccionVentas=montoVentasPasado*(porcentajeFidelidadOro+porcentajeFidelidadPlata+porcentajeFidelidadBronce+porcentajeFidelidadNull);
		long gastosMensuales=GastoMensual.gastosMensuales(fechaActual);
		long diferenciaEstimada=Math.round(prediccionVentas-((gastosMensuales+balanceAnterior.balance)*0.8));
		return diferenciaEstimada;
	}



	public static ArrayList <Evaluacionfinanciera> getHistorialEvaluaciones(){return historialEvaluaciones;}
	public static void setHistorialEvaluaciones(ArrayList <Evaluacionfinanciera> historial){historialEvaluaciones=historial;}
	public long getPagoPersonas(){return pagoPersonas;}
	public void setPagoPersonas(long pago){pago=pagoPersonas;}
	public double getBalance(){return balance;}
	public void setBalance(double balance){this.balance=balance;}
	public boolean  getProyeccion(){return proyeccion;}
	public void setProyeccion(boolean proyeccion){this.proyeccion=proyeccion;}
	public Empleado getPresidente(){return presidente;}
	public void setPresidente(Empleado presidente){
		if (presidente.getAreaActual()==Area.DIRECCION && presidente.getRol()==Rol.PRESIDENTE){
			this.presidente=presidente;}}	
	static float promedioBalance(){
		float promedio=0;
		for (Evaluacionfinanciera evaluacion : historialEvaluaciones){
			promedio+=evaluacion.balance;
		}
		promedio=promedio/historialEvaluaciones.size();
		return promedio;
	}
}
