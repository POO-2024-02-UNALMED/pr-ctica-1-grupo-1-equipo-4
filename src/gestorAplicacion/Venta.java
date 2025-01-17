package gestorAplicacion;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import gestorAplicacion.Administracion.Empleado;
import gestorAplicacion.Bodega.Prenda;

public class Venta implements Serializable {
	private static final long serializarVersionUID = 1L; // Para serializacion
	private ArrayList<Prenda> articulos;
	private ArrayList<Integer> cantidades;
	private Empleado encargado;
	private Empleado asesor;
	private Sede sede;
	private Fecha fechaVenta;
	private int montoPagado; 
	private Persona cliente;
	private int numero; 
	private int costoEnvio;
	private int subtotal;
	private static float pesimismo;


	// Metodo ayudante para Empleado.listaInicialDespedirEmpleado,
	// que calcula el acumulado de ventas asesoradas o registradas por empleado en pesos.
	static public int acumuladoVentasAsesoradas(Empleado empleado){
		int acumulado=0;
		for (Venta venta : empleado.getSede().getHistorialVentas()){
			if (venta.asesor.equals(empleado)){
				acumulado += venta.subtotal;
			}
		}
		return acumulado;
	}

	static public ArrayList<Venta> listaVentasAsesoradas(Empleado empleado){
		ArrayList<Venta> asesoradas = new ArrayList<Venta>();
		for (Venta venta : empleado.getSede().getHistorialVentas()){
			if (venta.asesor.equals(empleado)){
				asesoradas.add(venta);
			}
		}
		return asesoradas;
	}

	static public int cantidadVentasEncargadas(Empleado empleado){
		int cantidad=0;
		for (Venta venta : empleado.getSede().getHistorialVentas()){
			if (venta.encargado.equals(empleado)){
				cantidad++;
			}
		}
		return cantidad;
	}


	static public int acumuladoVentasEmpleadoEncargado(Empleado empleado){
		int acumulado=0;
		for (Venta venta : empleado.getSede().getHistorialVentas()){
			if (venta.encargado.equals(empleado)){
				acumulado += venta.subtotal;
			}
		}
		return acumulado;
	}

	public static long calcularBalanceVentaProduccion(Fecha fecha){
		long valorCalculado=0;
		long costos=0;
		for (Sede sede : Sede.getlistaSedes()){
			for (Venta venta : sede.getHistorialVentas()){
				if (Fecha.compararAño(venta.getFechaVenta().getAño(),fecha.getAño())&&Fecha.compararMes(venta.getFechaVenta().getMes(),fecha.getMes())){
				int monto=venta.montoPagado;
				float descuento=venta.cliente.getMembresia().getPorcentajeDescuento();
				valorCalculado+= Math.round(monto + (monto*descuento)+venta.costoEnvio);
				for (int i=0;i<venta.articulos.size();i++){
					costos+=venta.articulos.get(i).getCostoInsumos();
				}}
			}
		}
		long balanceCostosProduccion=valorCalculado-costos;
		return balanceCostosProduccion;
	}

	static public float blackFriday(Fecha fecha){
	int año;
	if (fecha.getMes()>11){año=fecha.getAño();}
	else if (fecha.getMes()==11 && fecha.getDia()<=24){año=fecha.getAño();}
	else{año=fecha.getAño()-1;}
	ArrayList<Fecha> diasBlackFriday=new ArrayList();
	diasBlackFriday.add(new Fecha(28,11,año));diasBlackFriday.add(new Fecha(29,11,año));diasBlackFriday.add(new Fecha(30,11,año));
	ArrayList<Fecha> FechasNormales=new ArrayList();
	//Tres Fechas contiguas en el mismo mes pero sin black Fiday
	FechasNormales.add(new Fecha(23,11,año));FechasNormales.add(new Fecha(24,11,año));FechasNormales.add(new Fecha(25,11,año));
	long montoventasBF=0;
	long montoventasDC=0;
	for(Sede sede: Sede.getlistaSedes()){
	for(Venta venta : sede.getHistorialVentas()){
		if (diasBlackFriday.contains(venta.getFechaVenta())){montoventasBF+=venta.getMontoPagado();}
		else if (FechasNormales.contains(venta.getFechaVenta())){montoventasDC+=venta.getMontoPagado();}}}
	long diferencia=montoventasBF-montoventasDC;
	if(diferencia<=0){return 0.0F;}
	else if(0>diferencia && diferencia<=(montoventasBF*0.1)){return 0.1F;}
	else if(0>diferencia && diferencia<=(montoventasBF*0.2)){return 0.2F;}
	else if(0>diferencia && diferencia<=(montoventasBF*0.3)){return 0.3F;}
	else if(0>diferencia && diferencia<=(montoventasBF*0.4)){return 0.4F;}
	else {return 0.5F;}
   }

   static public ArrayList<Venta> filtrarPorMes(ArrayList<Venta> ventas, Fecha fecha){
	   ArrayList<Venta> ventasMes=new ArrayList();
	   for (Venta venta : ventas){
		   if (Fecha.compararAño(venta.getFechaVenta().getAño(),fecha.getAño())&&Fecha.compararMes(venta.getFechaVenta().getMes(),fecha.getMes())){
			   ventasMes.add(venta);
		   }
	   }
	   return ventasMes;
   }

	public ArrayList<Prenda> getArticulos(){return articulos;}
	public void setArticulos(ArrayList<Prenda> articulos){this.articulos=articulos;}
	public ArrayList<Integer> getCantidades(){return cantidades;}
	public void setCantidades(ArrayList<Integer> cantidades){
		if (cantidades.size()==articulos.size()){this.cantidades=cantidades;}
		}
	public Empleado getEncargado(){return encargado;}
	public void setEncargado(Empleado emp){encargado=emp;}
	public Empleado getAsesor(){return asesor;}
	public void setAsesor(Empleado emp){asesor=emp;}
	public Sede getSede(){return sede;}
	public void setSede(Sede sede){this.sede=sede;}
	public Fecha getFechaVenta(){return fechaVenta;}
	public void setFechaVenta(Fecha fecha){fechaVenta=fecha;}
	public int getMontoPagado(){return montoPagado;}
	public void setMontoPagado(int monto){montoPagado=monto;}
	public Persona getCliente(){return cliente;}
	public void setCliente(Persona persona){cliente=persona;}
	public int getNumero(){return numero;}
	public void setNumero(int numero){this.numero=numero;}
	public int getCostoEnvio(){return costoEnvio;}
	public void setCostoEnvio(int monto){costoEnvio=monto;}
	public int getsubtotal(){return subtotal;}
	public void setSubtotal(int monto){subtotal=monto;}
	static public void setPesimismo(float newPesimism){Venta.pesimismo = newPesimism;}
	static public float getPesimismo(){return Venta.pesimismo;}
}