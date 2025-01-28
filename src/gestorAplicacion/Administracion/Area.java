package gestorAplicacion.Administracion;

import gestorAplicacion.Fecha;
import gestorAplicacion.Sede;
import gestorAplicacion.Venta;
import java.util.ArrayList;
import java.util.Arrays;

public enum Area {

	DIRECCION ("Direccion",new ArrayList<String>(Arrays.asList("computador","impresora"))),
    OFICINA ("Oficina", new ArrayList<String>(Arrays.asList("computador","registradora"))),
    VENTAS ("Ventas", new ArrayList<String>(Arrays.asList("escaner"))),
    CORTE ("Corte", new ArrayList<String>(Arrays.asList("maquina de coser","maquina de corte","plancha industrial")));
	
	private float rendimientoDeseado;
	private final String nombre;
    private final ArrayList<String> MaquinariaNecesaria;

    private Area (String nom, ArrayList<String> MaquinariaNecesaria){
    	this.nombre=nom;
        this.MaquinariaNecesaria=MaquinariaNecesaria;
    }
    
    public static ArrayList<Float> rendimientoDeseadoActual(Sede sede, Fecha fecha) {
        ArrayList<Float> rendimientoSede = new ArrayList<>();
        for (Area a : Area.values()) {
            switch (a) {
                case DIRECCION:
                    a.rendimientoDeseado = (3 / 5f)*100;
                    break;
                case OFICINA:
                    int cantidadEmpleadosOfi =sede.cantidadPorArea(Area.OFICINA);
                    a.rendimientoDeseado = (float) Venta.filtrar(sede.getHistorialVentas(), fecha).size() / cantidadEmpleadosOfi; // Cantidad de ventas por empleado de oficina
                    break;
                case VENTAS:
                    long montoTotal = 0;
                    for (Venta venta : Venta.filtrar(sede.getHistorialVentas(),fecha)) {
                        int montoPagado = venta.getMontoPagado();
                        montoTotal += montoPagado;
                    }
                    int cantidadVentas = Venta.filtrar(sede.getHistorialVentas(), fecha).size();
                    a.rendimientoDeseado = (montoTotal / cantidadVentas)*0.8f;
                    break;
                case CORTE:
                    int prendasDescartadas = 0;
                    int prendasProducidas = 0;

                    for(Empleado emp: sede.getlistaEmpleados()){
                        prendasDescartadas += emp.getPrendasDescartadas();
                        prendasProducidas += emp.getPrendasProducidas();
                    }

                    a.rendimientoDeseado = ((float) prendasProducidas / (prendasDescartadas + prendasProducidas))*90f;

                    break;
            }
            rendimientoSede.add(a.rendimientoDeseado);

        }
        return rendimientoSede;
    }

    public ArrayList<String> getMaquinariaNecesaria() {
        return MaquinariaNecesaria;
    }

    public String getNombre(){
        return this.nombre;
    }
}
