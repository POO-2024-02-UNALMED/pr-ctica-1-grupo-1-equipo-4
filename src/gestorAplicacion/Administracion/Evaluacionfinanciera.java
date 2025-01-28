/* Equipo 4 grupo 1
 * Clase Evaluacionfinanciera
 * Representa una evaluacion financiera generada durante la funcionalidad
 * de desglose economico
 */
package src.gestorAplicacion.Administracion;
import src.gestorAplicacion.Fecha;
import src.gestorAplicacion.Sede;
import src.gestorAplicacion.Venta;
import src.gestorAplicacion.Administracion.Banco;
import java.io.Serializable;

public class Evaluacionfinanciera implements Serializable {
	private static final long serialVersionUID = 1L;
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
			presidente.getEvaluacionesFinancieras().add(this);
		}
	}

	public Evaluacionfinanciera(double balance){
		this.balance=balance;
	}

	public static long estimadoVentasGastos(Fecha fechaActual,float porcentajeUsuario, Evaluacionfinanciera balanceAnterior){
		long montoVentasPasado=0;
		for (Sede sede : Sede.getlistaSedes()) {
			for (Venta venta : sede.getHistorialVentas()) {
				if (fechaActual.compararAño(fechaActual.getAño(), venta.getFechaVenta().getAño())
						&& fechaActual.compararMes(fechaActual.getAño() - 1, venta.getFechaVenta().getAño())) {
					montoVentasPasado += venta.getsubtotal() + venta.getCostoEnvio();
				}
			}
		}
		//Predecimos las ventas con un porcentaje de fidelidad 
		//(porcentaje de las ventas que se pueden dar por sentado y se mantienen de un mes al otro)
		float porcentajeFidelidadOro;
		if (balanceAnterior.balance>=0){porcentajeFidelidadOro=0.8F;}
		else {porcentajeFidelidadOro=0.5F;}
		if (porcentajeUsuario==0.0F){porcentajeFidelidadOro=0.9F;}
		//Preguntamos al usuario si desea cambiar el de Oro
		float porcentajeFidelidadPlata=porcentajeFidelidadOro-0.2F;
		float porcentajeFidelidadBronce=porcentajeFidelidadOro-0.4F;
		float porcentajeFidelidadNull=porcentajeUsuario;
		float prediccionVentas=montoVentasPasado*(porcentajeFidelidadOro+porcentajeFidelidadPlata+porcentajeFidelidadBronce+porcentajeFidelidadNull);
		long gastosMensuales=GastoMensual.gastosMensuales(fechaActual);
		long diferenciaEstimada=Math.round((prediccionVentas-gastosMensuales*0.8)+ (Banco.totalAhorros()*0.05));
		return diferenciaEstimada;
	}



	public long getPagoPersonas(){
		return pagoPersonas;
	}
	public void setPagoPersonas(long pago){
		pago=pagoPersonas;
	}
	public double getBalance(){
		return balance;
	}
	public void setBalance(double balance){
		this.balance=balance;
	}
	public boolean  getProyeccion(){
		return proyeccion;
	}
	public void setProyeccion(boolean proyeccion){
		this.proyeccion=proyeccion;
	}
	public Empleado getPresidente(){
		return presidente;
	}
	public void setPresidente(Empleado presidente){
		if (presidente.getAreaActual()==Area.DIRECCION && presidente.getRol()==Rol.PRESIDENTE){
			this.presidente=presidente;}}	
	static float promedioBalance(){
		float promedio=0;
		for (Evaluacionfinanciera evaluacion : Sede.getEvaluacionesFinancieras()){
			promedio+=evaluacion.balance;
		}
		promedio=promedio/Sede.getEvaluacionesFinancieras().size();
		return promedio;
	}
}
