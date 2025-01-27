package gestorAplicacion;
import java.io.Serializable;
import java.util.ArrayList;

import gestorAplicacion.Administracion.Empleado;
import gestorAplicacion.Bodega.Bolsa;
import gestorAplicacion.Bodega.Prenda;

public class Venta implements Serializable {
	private static final long serialVersionUID = 1L; // Para serializacion
	private ArrayList<Prenda> articulos= new ArrayList<Prenda>();
	private ArrayList<Bolsa> bolsas=new ArrayList<Bolsa>();
	private static ArrayList<String> codigosRegalo = new ArrayList<String>();
    private static ArrayList<Integer> montosRegalo = new ArrayList<Integer>();
	private Empleado encargado;
	private Empleado asesor;
	private Sede sede;
	private Fecha fechaVenta;
	private int montoPagado; 
	private Persona cliente;
	private int numero; 
	private int costoEnvio;
	private int subtotal;
	private static float pesimismo = 0.02F;

	public Venta(Sede sede,Fecha fecha, Persona c, Empleado a, Empleado e,ArrayList<Prenda> articulos){
		this(sede, fecha, c);
		asesor=a;
		encargado=e;
		encargado.getVentasEncargadas().add(this);
		this.articulos=articulos;
		for(Prenda prenda : articulos){
		sede.getPrendasInventadas().remove(prenda);
	}
	}

	
	public Venta(Sede sede,Fecha fecha, Persona c, Empleado a, Empleado v,ArrayList<Prenda> articulos, int sub, int mp){
		this(sede,fecha,c,a,v,articulos);
		subtotal=sub;
		montoPagado=mp;
		sede.getCuentaSede().setAhorroBanco(sede.getCuentaSede().getAhorroBanco()+montoPagado);
	}

	public Venta(Sede sede,Fecha fecha, Persona c){
		this.sede=sede;
		this.fechaVenta=fecha;
		Venta venta=this;
		cliente=c;
		sede.actualizarHistorialVentas(venta);
	}

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


	static public int cantidadVentasEncargadasEnMes(Empleado empleado, Fecha fecha){
		int cantidad=0;
		for (Venta venta : empleado.getVentasEncargadas()){
			if (venta.getFechaVenta().getMes()==fecha.getMes()&&venta.getFechaVenta().getAño()==fecha.getAño()){
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
					costos+=venta.articulos.get(i).calcularCostoInsumos();
				}}
			}
		}
		long balanceCostosProduccion=valorCalculado-costos;
		return balanceCostosProduccion;
	}

	// Retorna de 0 a 0.5 el porcentaje que sube el monto de ventas el black friday
	static public float blackFriday(Fecha fecha) {
		int año;
		if (fecha.getMes() > 11) {
			año = fecha.getAño();
		} else if (fecha.getMes() == 11 && fecha.getDia() >= 24) {
			año = fecha.getAño();
		} else {
			año = fecha.getAño() - 1;
		} // Si no ha pasado black friday, usar el año anterior
		ArrayList<Fecha> diasBlackFriday = new ArrayList<Fecha>();
		diasBlackFriday.add(new Fecha(28, 11, año));
		diasBlackFriday.add(new Fecha(29, 11, año));
		diasBlackFriday.add(new Fecha(30, 11, año));
		ArrayList<Fecha> FechasNormales = new ArrayList<Fecha>();
		// Tres Fechas contiguas en el mismo mes pero sin black Fiday
		FechasNormales.add(new Fecha(23, 11, año));
		FechasNormales.add(new Fecha(24, 11, año));
		FechasNormales.add(new Fecha(25, 11, año));
		long montoventasBF = 0;
		long montoventasComunes = 0;
		for (Sede sede : Sede.getlistaSedes()) {
			for (Venta venta : sede.getHistorialVentas()) {
				for (int i = 0; i < 3; i++) {
					if (diasBlackFriday.get(i).getAño() == venta.getFechaVenta().getAño()
							&& diasBlackFriday.get(i).getMes() == venta.getFechaVenta().getMes()
							&& diasBlackFriday.get(i).getDia() == venta.getFechaVenta().getDia()) {
						montoventasBF += venta.getMontoPagado();
					} else if (FechasNormales.get(i).getAño() == venta.getFechaVenta().getAño()
							&& FechasNormales.get(i).getMes() == venta.getFechaVenta().getMes()
							&& FechasNormales.get(i).getDia() == venta.getFechaVenta().getDia()) {
						montoventasComunes += venta.getMontoPagado();
					}
				}
			}
		}
		float diferencia = (montoventasBF - montoventasComunes)/ (float) montoventasComunes;
		System.out.println(montoventasBF);
		System.out.println(montoventasComunes);
		System.out.println(diferencia);
		return Math.round(Math.min(diferencia/3,0.3f));
	}

   static public ArrayList<Venta> filtrar(ArrayList<Venta> ventas, Fecha fecha){
	   ArrayList<Venta> ventasMes=new ArrayList<Venta>();
	   for (Venta venta : ventas){
		   if (Fecha.compararAño(venta.getFechaVenta().getAño(),fecha.getAño())&&Fecha.compararMes(venta.getFechaVenta().getMes(),fecha.getMes())){
			   ventasMes.add(venta);
		   }
	   }
	   return ventasMes;
   }

   static public ArrayList<Venta> filtrar(ArrayList<Venta> ventas, Empleado empleado){
	   ArrayList<Venta> asesoradas = new ArrayList<Venta>();
	   for (Venta venta : ventas){
		   if (venta.asesor.equals(empleado)){
			   asesoradas.add(venta);
		   }
	   }
	   return asesoradas;
   }
   
   // Da la cantidad de veces que se vendió una prenda con ese nombre en la lista
   // Auxiliar de Venta.predecirVentas
   static public int cantidadProducto(ArrayList<Venta> ventas, String prenda){
	   int cantidad=0;
	   for (Venta venta : ventas){
		   for (Prenda articulo : venta.getArticulos()){
			   if (articulo.getNombre().equals(prenda)){
				   cantidad++;
			   }
		   }
	   }
	   return cantidad;
   }


   // Regresión lineal    
	// Calcula la pendiente promedio usando 3 meses anteriores al de la fecha dada
	// preda debe ser el nombre de la prenda
	static public int predecirVentas(Fecha fechaActual,Sede sede, String prenda){
		int  ventasMes1= Venta.cantidadProducto(Venta.filtrar(sede.getHistorialVentas(),fechaActual.restarMeses(3)),prenda);
		int  ventasMes2= Venta.cantidadProducto(Venta.filtrar(sede.getHistorialVentas(),fechaActual.restarMeses(2)),prenda);
		float pendienteMes1a2=(ventasMes2-ventasMes1);
		int  ventasMes3= Venta.cantidadProducto(Venta.filtrar(sede.getHistorialVentas(),fechaActual.restarMeses(1)),prenda);
		float pendienteMes2a3=(ventasMes3-ventasMes2);
		float pendientePromedio=(pendienteMes1a2+pendienteMes2a3)/2;
		return (int) Math.ceil(ventasMes3+pendientePromedio);
	}


	public ArrayList<Prenda> getArticulos(){
		return articulos;
	}
	public void setArticulos(ArrayList<Prenda> articulos){
		this.articulos=articulos;
	}
	public ArrayList<Bolsa> getBolsas(){
		return bolsas;
	}
	public void setBolsas(ArrayList<Bolsa> bolsas){
		this.bolsas=bolsas;
	}
	public Empleado getEncargado(){
		return encargado;
	}
	public void setEncargado(Empleado emp){
		encargado=emp;
	}
	public Empleado getAsesor(){
		return asesor;
	}
	public void setAsesor(Empleado emp){
		asesor=emp;
	}
	public Sede getSede(){
		return sede;
	}
	public void setSede(Sede sede){
		this.sede=sede;
	}
	public Fecha getFechaVenta(){
		return fechaVenta;
	}
	public void setFechaVenta(Fecha fecha){
		fechaVenta=fecha;
	}
	public int getMontoPagado(){
		return montoPagado;
	}
	public void setMontoPagado(int monto){
		if (montoPagado==0){
			sede.getCuentaSede().setAhorroBanco(sede.getCuentaSede().getAhorroBanco()+monto);
			montoPagado=monto;
		}
		else{
			sede.getCuentaSede().setAhorroBanco(sede.getCuentaSede().getAhorroBanco()-montoPagado);
			montoPagado=monto;
			sede.getCuentaSede().setAhorroBanco(sede.getCuentaSede().getAhorroBanco()-monto);}
		}
	public Persona getCliente(){
		return cliente;
	}
	public void setCliente(Persona persona){
		cliente=persona;
	}
	public int getNumero(){
		return numero;
	}
	public void setNumero(int numero){
		this.numero=numero;
	}
	public int getCostoEnvio(){
		return costoEnvio;
	}
	public void setCostoEnvio(int monto){
		costoEnvio=monto;
	}
	public int getsubtotal(){
		return subtotal;
	}
	public void setSubtotal(int monto){
		subtotal=monto;
	}
	static public void setPesimismo(float newPesimism){
		Venta.pesimismo = newPesimism;
	}
	static public float getPesimismo(){
		return Venta.pesimismo;
	}
	public static ArrayList<String> getCodigosRegalo(){
		return Venta.codigosRegalo;
	}
	public static void setCodigosRegalos(ArrayList<String> codigo){
		Venta.codigosRegalo=codigo;
	}
	public static ArrayList<Integer> getMontosRegalo(){
		return Venta.montosRegalo;
	}
	public static void setMontosRegalo(ArrayList<Integer> montos){
		Venta.montosRegalo=montos;
	}
	
}