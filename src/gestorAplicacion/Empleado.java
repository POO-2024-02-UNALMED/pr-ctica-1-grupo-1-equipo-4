package gestorAplicacion;
import java.util.ArrayList;

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
    
    Empleado(){
    	listaEmpleados.add(this);
    }
    
    public ArrayList <Empleado> listaInicialDespedirEmpleado(){
        ArrayList <Empleado> listaADespedir = null;
        for (Sede sede : Sede.listaSedes){
            for (Empleado emp : sede.listaEmpleado){
                int rendimiento;
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
            }
        }
        return listaADespedir;
    }

    public Area getAreaActual(){
        return areaActual;
    }
}
