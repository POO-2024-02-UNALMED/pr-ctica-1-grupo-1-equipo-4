package gestorAplicacion.Administracion;

import java.util.ArrayList;
import java.util.Arrays;

import gestorAplicacion.Sede;
import gestorAplicacion.Venta;

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
    
    public static ArrayList<Float> rendimientoDeseadoActual(Sede sede) {
        ArrayList<Float> rendimientoSede = new ArrayList<>();
        for (Area a : Area.values()) {
            switch (a) {
                case DIRECCION:
                    a.rendimientoDeseado = 3 / 5f;
                    rendimientoSede.add(a.rendimientoDeseado);
                    break;
                case OFICINA:
                    int cantidadEmpleadosOfi = 0;
                    for (Empleado emp : sede.getlistaEmpleados()) {
                        if (emp.getAreaActual().nombre.equals("Oficina")) {
                            cantidadEmpleadosOfi++;
                        }
                    }
                    a.rendimientoDeseado = (float) sede.getHistorialVentas().size() / cantidadEmpleadosOfi;
                    rendimientoSede.add(a.rendimientoDeseado);
                    break;
                case VENTAS:
                    int montoTotal = 0;
                    for (Venta venta : sede.getHistorialVentas()) {
                        montoTotal += venta.getMontoPagado();
                    }
                    a.rendimientoDeseado = ((float) montoTotal / sede.getlistaEmpleados().size())*0.8f;
                    rendimientoSede.add(a.rendimientoDeseado);
                    break;
                case CORTE:
                    int prendasDescartadas = 0;
                    int prendasProducidas = 0;

                    for(Empleado emp: sede.getlistaEmpleados()){
                        prendasDescartadas += emp.getPrendasDescartadas();
                        prendasProducidas += emp.getPrendasProducidas();
                    }

                    a.rendimientoDeseado = ((float) prendasProducidas / (prendasDescartadas + prendasProducidas))*0.9f;

                    break;
            }
        }
        return rendimientoSede;
    }

    public ArrayList<String> getMaquinariaNecesaria() {
        return MaquinariaNecesaria;
    }
}
