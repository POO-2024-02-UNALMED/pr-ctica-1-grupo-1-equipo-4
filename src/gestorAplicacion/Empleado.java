package gestorAplicacion;
import java.util.ArrayList;
import java.util.Arrays;

public class Empleado extends Persona{
	
	public static ArrayList <Empleado> listaEmpleados;
    ArrayList<Integer> balances = new ArrayList<Integer>();
    // Usado para empleados de Dirección.

    private Area areaActual;
    private String fechaContratacion;
    //LocalDate?
    private int rendimiento;
    private Sede sede;
    private Maquinaria maquinaria;
    private ArrayList<Area> areas;
    int traslados;
    int prendasArruinadas=0;
    int prendasProducidas=0;
    float pericia; // De 0 a 1, la posibilidad de que una prenda no se arruine al ser manejada por el modista.
    private int bonificacion=0;

    Empleado(){
    	listaEmpleados.add(this);
    }
    
    static public ArrayList <Empleado> listaInicialDespedirEmpleado(){
        // Preparamos las listas de empleados
        ArrayList <Empleado> listaADespedir = new ArrayList<Empleado>(); // A en el doc. 
        ArrayList <ArrayList<Empleado>> listaATransferir = new ArrayList<>(Arrays.asList()); // B en el doc.
        for (int i = 0; i < Sede.getlistaSedes().size(); i++){
            listaATransferir.add(new ArrayList<Empleado>());
        }

        // Juzgamos el rendimiento de todos los empleados
        for (Sede sede : Sede.getlistaSedes()){
            for (Empleado emp : sede.getlistaEmpleados()){
                int rendimiento=0;
                switch (emp.areaActual){
                    case CORTE:
                        rendimiento = (emp.prendasProducidas/emp.prendasArruinadas)*100;
                        break;
                    case VENTAS:
                        float acumuladoVentasSede = 0;
                        for (Empleado empAcumulado : sede.getlistaEmpleados()){
                            if (empAcumulado.areaActual == Area.VENTAS){
                                acumuladoVentasSede+=Venta.acumuladoVentasAsesoradas(empAcumulado);
                            }
                        }
                        float promedioVentasSede = acumuladoVentasSede/sede.getlistaEmpleados().size();
                        rendimiento = (int) ((Venta.acumuladoVentasAsesoradas(emp)/promedioVentasSede)*100);
                        break;
                    case OFICINA:
                        acumuladoVentasSede = 0;
                        for (Empleado empAcumulado : sede.getlistaEmpleados()){
                            if (empAcumulado.areaActual == Area.VENTAS){
                                Venta.acumuladoVentasEmpleadoEncargado(emp);
                            }
                        }
                        promedioVentasSede = acumuladoVentasSede/sede.getlistaEmpleados().size();
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
                for (int idxSede = 0; idxSede < Sede.getlistaSedes().size(); idxSede++){
                    if (Sede.getlistaSedes().get(idxSede).getRendimientoDeseado(emp.areaActual) <= rendimiento + 20){
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

        for (int idxSede = 0; idxSede < Sede.getlistaSedes().size(); idxSede++){
            for (Empleado emp : listaATransferir.get(idxSede)){
                emp.trasladarEmpleado(Sede.getlistaSedes().get(idxSede));
            }
        }
        
        return listaADespedir;
    }

    private void trasladarEmpleado(Sede sedeNueva){
        sede.getlistaEmpleados().remove(this);
        sedeNueva.getlistaEmpleados().add(this);
        traslados++;
        int aPagar = Maquinaria.remuneracionDanos(this);
        Banco.getCuentaPrincipal().transaccion(aPagar);
        modificarBonificacion(aPagar*-1);
        Maquinaria.liberarMaquinariaDe(this);
    }

    public Area getAreaActual(){return areaActual;}
    public void setAreaActual(Area a){areaActual=a;}
    public String getFechaContratacion(){return fechaContratacion;}
    public void setFechaContratacion(String fecha){fechaContratacion=fecha;}
    public int getRendimiento(){return rendimiento;}
    public void getRendimiento(int rend){rendimiento=rend;}
    public Sede getSede(){return sede;}
    public void setSede(Sede sede){this.sede=sede;}
    public Maquinaria getMaquinaria(){return maquinaria;}
    public void getMaquinaria(Maquinaria maquina){maquinaria=maquina;}
    public ArrayList<Area> getAreas(){return areas;}
    public void setAreas(ArrayList<Area> areas){this.areas=areas;}
    public int getBonificacion(){return bonificacion;}
    public void getRendimientoBonificacion(int boni){bonificacion=boni;}
    public float getPericia(){return pericia;}

    private void modificarBonificacion(int bonificacion){
        this.bonificacion += bonificacion;
    }

    static public void despedirEmpleados(ArrayList<Empleado> empleados){
        for (Empleado emp : empleados){
            emp.sede.getlistaEmpleados().remove(emp);
            listaEmpleados.remove(emp);

            int aPagar = Maquinaria.remuneracionDanos(emp);
            Banco.getCuentaPrincipal().transaccion(aPagar);
            Maquinaria.liberarMaquinariaDe(emp);
        }
    }
}
