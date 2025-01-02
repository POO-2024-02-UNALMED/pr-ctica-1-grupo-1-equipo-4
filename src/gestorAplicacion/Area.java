package gestorAplicacion;

public enum Area {

	DIRECCION ("Direccion"),OFICINA ("Oficina"),VENTAS ("Ventas"),CORTE ("Corte");
	
	private float rendimientoDeseado;
	private final String nombre;

    private Area (String nom){
    	this.nombre=nom;
    }
    
   // public static ArrayList <Float> rendimientoDeseadoActual(Sede sede){
	//	ArrayList <Float> rendimientoSede = null;
    //	for(Area a : Area.values()){
    //		if (a.nombre == "Direccion") {
	//			a.rendimientoDeseado=3/5f;
	//			rendimientoSede.add(a.rendimientoDeseado);
	//		}
    //		else if(a.nombre == "Oficina") {
    //			//rendimientoDeseado=promedio de ventas de la sede
	//			for (Empleado emp:sede.getlistaEmpleados()){
	//			int cantidadEmpleadosOfi=0;
	//				if(emp.getAreaActual().nombre=="Oficina"){
	//					cantidadEmpleadosOfi++;
	//				}
	//			a.rendimientoDeseado=(sede.historialVentas.size())/cantidadEmpleadosOfi;
	//			rendimientoSede.add(a.rendimientoDeseado);
    //		}}
    //		else if (a.nombre == "Ventas") {
    //			//rendimientoDeseado=promedio de ventas de la sede
	//			int montoTotal=0;
	//			for (Venta venta:sede.historialVentas){
	//			montoTotal+=venta.montoPagado;
    //		}
	//			a.rendimientoDeseado=(montoTotal)/sede.historialVentas.size();
	//			rendimientoSede.add(a.rendimientoDeseado);
	//		}
    //		else if (a.nombre == "Corte") {
    //			//promedio de las prendas por empleado con atributo descartada==True
	//			int prendasDercartadas=0;
	//			for (Prenda prenda:sede.prendasInventadas){
	//				if(prenda.descartada==true){
	//					prendasDercartadas++;
	//				}}
	//				int cantidadEmpleadosCort=0;
	//			for (Empleado emp:sede.getlistaEmpleados()){
	//				if(emp.getAreaActual().nombre=="Corte"){
	//					cantidadEmpleadosCort++;
	//				}}
	//			a.rendimientoDeseado=(prendasDercartadas)/cantidadEmpleadosCort;
	//			rendimientoSede.add(a.rendimientoDeseado);
    //		}		
    //}
	//return rendimientoSede;
//}
}
