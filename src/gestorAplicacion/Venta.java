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
		for (Venta venta : empleado.getSede().getHistorialVentas()){
			if (venta.asesor == empleado){
				acumulado += venta.subtotal;
			}
		}
		return acumulado;
	}	
	static public int acumuladoVentasEmpleadoEncargado(Empleado empleado){
		int acumulado=0;
		for (Venta venta : empleado.getSede().getHistorialVentas()){
			if (venta.encargado == empleado){
				acumulado += venta.subtotal;
			}
		}
		return acumulado;
	}

	public Evaluacionfinanciera calcularBalancePrevio(Empleado empleado){
		//Empleado debe ser de rol presidente y área direccion
		double balanceTotal=0;
		double valorCalculado=0;
		long costos=0;
		for (Sede sede : Sede.getlistaSedes()){
			for (Venta venta : sede.getHistorialVentas()){
				int monto=venta.montoPagado;
				double descuento=venta.cliente.getMembresia().getPorcentajeDescuento();
				valorCalculado+= monto + (monto*descuento)+costoEnvio;
				for (int i=0;i<venta.articulos.size();i++){
					costos+=venta.articulos.get(i).costoInsumos;
				}
			}
		}
		double balanceCostosProduccion=valorCalculado-costos;
		//preguntar al usuario qué tipo de deudas incluir
		//Ensayo con ambas
		Deuda deuda=new Deuda();
		double deudaCalculada=0;
		switch (deuda.getTipoEntidad()){
                case "Proveedor":
					for (Proveedor proveedor : Proveedor.getListaProveedores()) {
						for(Deuda deudaP: proveedor.getDeuda()){
							deudaCalculada+=deudaP.deudaActual(numero);
						}
					}
					break;
				case "Banco":
					for (Banco banco : Banco.getListaBancos()) {
						for(Deuda deudaB: banco.getDeuda()){
							deudaCalculada+=deudaB.deudaActual(numero);
						}
					}
					break;
				case "Ambos":
					for (Proveedor proveedor : Proveedor.getListaProveedores()) {
						for(Deuda deudaP: proveedor.getDeuda()){
							deudaCalculada+=deudaP.deudaActual(numero);
						}
					}
					for (Banco banco : Banco.getListaBancos()) {
						for(Deuda deudaB: banco.getDeuda()){
							deudaCalculada+=deudaB.deudaActual(numero);
						}
					}
					break;
	}
	balanceTotal=balanceCostosProduccion-deudaCalculada;
	Evaluacionfinanciera nuevoBalance=new Evaluacionfinanciera (balanceTotal,empleado);
	return nuevoBalance;
}}