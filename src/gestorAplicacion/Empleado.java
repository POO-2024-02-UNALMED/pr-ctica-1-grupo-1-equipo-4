package gestorAplicacion;
import java.util.ArrayList;
import java.util.Arrays;

public class Empleado extends Persona{
	
	public static ArrayList <Empleado> listaEmpleados;
    ArrayList<Integer> balances = new ArrayList<Integer>();
    // Usado para empleados de Dirección.

    private Area areaActual;
    String fechaContratacion;
    //LocalDate?
    int rendimiento;
    Sede sede;
    Maquinaria maquinaria;
    ArrayList<Area> areas;
    int traslados;
    int prendasArruinadas=0;
    int prendasProducidas=0;
    int bonificacion=0;
    
    Empleado(){
    	listaEmpleados.add(this);
    }
    
    public ArrayList <Empleado> listaInicialDespedirEmpleado(){
        // Preparamos las listas de empleados
        ArrayList <Empleado> listaADespedir = new ArrayList<Empleado>(); // A en el doc. 
        ArrayList <ArrayList<Empleado>> listaATransferir = new ArrayList<>(Arrays.asList()); // B en el doc.
        for (int i = 0; i < Sede.listaSedes.size(); i++){
            listaATransferir.add(new ArrayList<Empleado>());
        }

        // Juzgamos el rendimiento de todos los empleados
        for (Sede sede : Sede.listaSedes){
            for (Empleado emp : sede.listaEmpleado){
                int rendimiento=0;
                switch (emp.areaActual){
                    case CORTE:
                        rendimiento = (emp.prendasProducidas/emp.prendasArruinadas)*100;
                        break;
                    case VENTAS:
                        float acumuladoVentasSede = 0;
                        for (Empleado empAcumulado : sede.listaEmpleado){
                            if (empAcumulado.areaActual == Area.VENTAS){
                                acumuladoVentasSede+=Venta.acumuladoVentasAsesoradas(empAcumulado);
                            }
                        }
                        float promedioVentasSede = acumuladoVentasSede/sede.listaEmpleado.size();
                        rendimiento = (int) ((Venta.acumuladoVentasAsesoradas(emp)/promedioVentasSede)*100);
                        break;
                    case OFICINA:
                        acumuladoVentasSede = 0;
                        for (Empleado empAcumulado : sede.listaEmpleado){
                            if (empAcumulado.areaActual == Area.VENTAS){
                                Venta.acumuladoVentasEmpleadoEncargado(emp);
                            }
                        }
                        promedioVentasSede = acumuladoVentasSede/sede.listaEmpleado.size();
                        rendimiento = (int) ((Venta.acumuladoVentasAsesoradas(emp)/promedioVentasSede)*100);
                        break;

                    case DIRECCION:
                        int balancesPositivos = 0;
                        int balancesNegativos = 0;
                        for (int balance : emp.balances){
                            if (balance > 0){
                                balancesPositivos++;
                            } else {
                                balancesNegativos++;
                            }
                        }
                        rendimiento = (balancesPositivos/(balancesNegativos+balancesPositivos))*100;
                        break;
                    }
                boolean seVaADespedir = false;
                if (rendimiento + 20 < sede.getRendimientoDeseado(emp.areaActual)){
                    listaADespedir.add(emp);
                    seVaADespedir = true;
                }

                // Verificamos posibilidades de transferencia.
                for (int idxSede = 0; idxSede < Sede.listaSedes.size(); idxSede++){
                    if (Sede.listaSedes.get(idxSede).getRendimientoDeseado(emp.areaActual) <= rendimiento + 20){
                        listaADespedir.remove(emp);
                        listaATransferir.get(idxSede).add(emp);
                        seVaADespedir = false;
                    }
                }

                if (seVaADespedir){
                    if (emp.areaActual != Area.CORTE && emp.traslados<2){
                        boolean puedeCambiarArea = true;
                        for (Area areaPasada : emp.areas){
                            if (areaPasada.ordinal()<emp.areaActual.ordinal()){
                                puedeCambiarArea = false; // Por haber trabajado en un área más baja.
                                break;
                            }
                        }
                        if (puedeCambiarArea){
                            seVaADespedir=false;
                            listaADespedir.remove(emp);
                        }
                    }
                }
            }
        }

        // Hacemos las transferencias de sede. Las transferencias de Area se hacen en el bucle,
        // que para todo tiene mas sentido, para no hacer una cantidad absurda de bucles.
        // En cualquier caso, esto se ajusta mas al doc.

        for (int idxSede = 0; idxSede < Sede.listaSedes.size(); idxSede++){
            for (Empleado emp : listaATransferir.get(idxSede)){
                int aPagar = Maquinaria.remuneracionDanos(emp);
                emp.modificarBonificacion(aPagar*-1);
                emp.trasladarEmpleado(Sede.listaSedes.get(idxSede));
            }
        }
        
        return listaADespedir;
    }

    private void trasladarEmpleado(Sede sedeNueva){
        sede.listaEmpleado.remove(this);
        sedeNueva.listaEmpleado.add(this);
        traslados++;
    }

    public Area getAreaActual(){
        return areaActual;
    }

    private void modificarBonificacion(int bonificacion){
        this.bonificacion += bonificacion;
    }
}
