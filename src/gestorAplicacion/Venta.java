package gestorAplicacion;
import java.util.ArrayList;

public class Venta {
	ArrayList<Prenda> articulos;
	ArrayList<Integer> cantidades;
	Empleado encargado;
	Empleado asesor;
	Sede sede;
	String fechaVenta;
	//LocalDate
	int montoPagado; 
	Persona cliente;
	int numero; 
	int costoEnvio;
	int subtotal;

	// Metodo ayudante para Empleado.listaInicialDespedirEmpleado,
	// que calcula el acumulado de ventas asesoradas o registradas por empleado en pesos.
	static public int acumuladoVentasAsesoradas(Empleado empleado){
		int acumulado=0;
		for (Venta venta : empleado.sede.historialVentas){
			if (venta.asesor == empleado){
				acumulado += venta.subtotal;;
			}
		}
		return acumulado;
	}	
	static public int acumuladoVentasEmpleadoEncargado(Empleado empleado){
		int acumulado=0;
		for (Venta venta : empleado.sede.historialVentas){
			if (venta.encargado == empleado){
				acumulado += venta.subtotal;;
			}
		}
		return acumulado;
	}
}