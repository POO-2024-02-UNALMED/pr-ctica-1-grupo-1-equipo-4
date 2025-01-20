package gestorAplicacion.Administracion;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import gestorAplicacion.Fecha;
import gestorAplicacion.Persona;
import gestorAplicacion.Sede;
import gestorAplicacion.Membresia;
import gestorAplicacion.Venta;
import gestorAplicacion.Bodega.Maquinaria;

public class Empleado extends Persona implements GastoMensual{
	
    
    private static final long serialVersionUID = 1L;

	public static ArrayList <Empleado> listaEmpleados=new ArrayList<Empleado>();
    ArrayList<Integer> balances = new ArrayList<Integer>();
    // Usado para empleados de Dirección.

    private Area areaActual;
    private Fecha fechaContratacion;
    private int rendimiento;
    private Sede sede;
    private Maquinaria maquinaria;
    private ArrayList<Area> areas = new ArrayList<Area>();
    private int traslados;
    private int prendasDescartadas=0;
    private int prendasProducidas=0;
    private float pericia; // De 0 a 1, la posibilidad de que una prenda no se arruine al ser manejada por el modista.
    private int bonificacion=0;  
    public Empleado(Persona p){
        super(p.getNombre(),p.getDocumento(),p.getRol(),p.getExperiencia(),p.isTrabaja(),p.getMembresia());
        listaEmpleados.add(this);
        sede.getlistaEmpleados().add(this);
    }

    public Empleado(Area area,Fecha fecha, Sede sede,Persona p){
        this(p);
        this.areaActual=area;
        this.areas.add(area);
        fechaContratacion=fecha;
        this.sede=sede;
    }
    
    public Empleado(Area area,Fecha fecha, Sede sede,String nom, int doc, Rol rol, int exp, Membresia mem, Maquinaria maquina){
        super(nom,doc,rol,exp,true,mem);
        this.areaActual=area;
        this.areas.add(area);
        fechaContratacion=fecha;
        this.sede=sede;
        listaEmpleados.add(this);
        maquinaria=maquina;
        sede.getlistaMaquinas().add(maquina);
        sede.getlistaEmpleados().add(this);
    }

    @Override
    public int calcularGastoMensual() {
        int gasto=salario;
        return gasto;
    }

    public static int gastoMensualClase() {
        int gasto=0;
        for (Empleado empleado : listaEmpleados){
            gasto+=empleado.calcularGastoMensual();
        }
        return gasto;
    }

    // Interacción 1 de Gestion Humana
    static public ArrayList <Empleado> listaInicialDespedirEmpleado(Fecha fecha){
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
                boolean seVaADespedir = false;
                switch (emp.areaActual){
                    case CORTE:
                        rendimiento = (emp.prendasProducidas/emp.prendasDescartadas)*100;
                        break;
                    case VENTAS:
                        rendimiento= Venta.acumuladoVentasAsesoradas(emp)/Venta.listaVentasAsesoradas(emp).size();
                        break;

                    case OFICINA:
                        float acumuladoVentasSede = 0;
                        for (Empleado empAcumulado : sede.getlistaEmpleados()){
                            if (empAcumulado.areaActual == Area.VENTAS){
                                acumuladoVentasSede+=Venta.cantidadVentasEncargadasEnMes(empAcumulado,fecha);
                            }
                        }
                        float promedioVentasSede = acumuladoVentasSede/sede.getlistaEmpleados().size();
                        rendimiento = (int) ((Venta.cantidadVentasEncargadasEnMes(emp,fecha)/promedioVentasSede)*100);

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
                
                // Añadimos a la lista los empleados con rendimiento insuficiente.
                if (rendimiento < emp.getSede().getRendimientoDeseado(emp.areaActual)){
                    seVaADespedir = true;
                    listaADespedir.add(emp);
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
                    if (emp.areaActual.equals(Area.CORTE)==false && emp.traslados<2){
                        boolean puedeCambiarArea = true;
                        for (Area areaPasada : emp.areas){
                            if (areaPasada.ordinal()>emp.areaActual.ordinal()){ // Al parecer areas mayores tienen ordinales mayores
                                puedeCambiarArea = false; // Por haber trabajado en un área más baja.
                                break;
                            }
                        }
                        if (puedeCambiarArea && emp.areaActual.ordinal()<Area.values().length-1){ // verificamos tambien si hay area mas baja
                            emp.setAreaActual(Area.values()[emp.areaActual.ordinal()+1]);
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

    // Interaccion 2 gestion humana. Puedes poner conTransacciones en falso en caso de que quieras quitar al empleado "Limpiamente", por ejemplo
    // en el modulo de Desarrollo.
    static public void despedirEmpleados(ArrayList<Empleado> empleados, boolean conTransacciones, Fecha fecha){
        for (Empleado emp : empleados){
            emp.sede.getlistaEmpleados().remove(emp);
            listaEmpleados.remove(emp);

            if(conTransacciones){
                int aPagar = Maquinaria.remuneracionDanos(emp);
                int cesantias = emp.salario*(emp.fechaContratacion.diasHasta(fecha))/360;
                Banco.getCuentaPrincipal().transaccion((cesantias-aPagar)*-1);
            }
            Maquinaria.liberarMaquinariaDe(emp);
        }
    }


    // Interaccion 1 gestion humana

    private void trasladarEmpleado(Sede sedeNueva){
        int aPagar = Maquinaria.remuneracionDanos(this);
        Banco.getCuentaPrincipal().transaccion(aPagar);
        modificarBonificacion(aPagar*-1);
        Maquinaria.liberarMaquinariaDe(this);

        traslados++;
        setSede(sedeNueva);

        Maquinaria.asignarMaquinaria(this);
    }

    // ------------------ Getters y Setters ------------------
    public String toString(){
        return super.toString()+"\n"+"Area: "+areaActual+"\n"+"Sede: "+sede+"\n"+"Traslados: "+traslados;
    }

    public void modificarBonificacion(int bonificacion){
        this.bonificacion += bonificacion;
    }

    public int getTraslados(){return traslados;}
    public void setTraslados(int traslados){this.traslados=traslados;}
    public int getPrendasDescartadas(){return prendasDescartadas;}
    public void setPrendasDescartadas(int prendas){this.prendasDescartadas=prendas;}  
    public int getPrendasProducidas(){return prendasProducidas;}
    public void setPrendasProcidas(int prendasProducidas){this.prendasProducidas=prendasProducidas;}
    public float getPericia(){return pericia;}
    public void setPericia(float pericia){this.pericia=pericia;}  
    public Area getAreaActual(){return areaActual;}
    public void setAreaActual(Area a){areaActual=a;}
    public Fecha getFechaContratacion(){return fechaContratacion;}
    public void setFechaContratacion(Fecha fecha){fechaContratacion=fecha;}
    public int getRendimiento(){return rendimiento;}
    public void getRendimiento(int rend){rendimiento=rend;}
    public Sede getSede(){return sede;}
    public void setSede(Sede sede){
        if (this.sede!=null){
            this.getSede().quitarEmpleado(this);
        }
        this.sede=sede;
        this.sede.anadirEmpleado(this);
    }
    public Maquinaria getMaquinaria(){return maquinaria;}
    public void getMaquinaria(Maquinaria maquina){maquinaria=maquina;}
    public ArrayList<Area> getAreas(){return areas;}
    public void setAreas(ArrayList<Area> areas){this.areas=areas;}
    public int getBonificacion(){return bonificacion;}
    public void setRendimientoBonificacion(int boni){bonificacion=boni;}
    public void setSalario(double salario){this.salario=salario;}
}
