package gestorAplicacion.Administracion;
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

	public static ArrayList<Empleado> listaEmpleados=new ArrayList<Empleado>();
    // Usado para empleados de Dirección.

    private Area areaActual;
    private Fecha fechaContratacion;
    private Sede sede;
    private Maquinaria maquinaria;
    private ArrayList<Evaluacionfinanciera> evaluaciones = new ArrayList<Evaluacionfinanciera>();
    private ArrayList<Venta> ventasEncargadas = new ArrayList<Venta>();
    private ArrayList<Area> areas = new ArrayList<Area>();
    private int traslados;
    private int prendasDescartadas=0;
    private int prendasProducidas=0;
    private float pericia; // De 0 a 1, la posibilidad de que una prenda no se arruine al ser manejada por el modista.
    private int bonificacion=0;  
	protected int salario;
    public Empleado(Persona p){
        super(p.getNombre(),p.getDocumento(),p.getRol(),p.getExperiencia(),p.isTrabaja(),p.getMembresia());
        Empleado.listaEmpleados.add(this);
    }

    public Empleado(Area area,Fecha fecha, Sede sede,Persona p){
        this(p);
        this.areaActual=area;
        this.areas.add(area);
        fechaContratacion=fecha;
        this.sede=sede;
        sede.getlistaEmpleados().add(this);
    }
    
    public Empleado(Area area,Fecha fecha, Sede sede,String nom, int doc, Rol rol, int exp, Membresia mem, Maquinaria maquina){
        super(nom,doc,rol,exp,true,mem);
        this.areaActual=area;
        this.areas.add(area);
        fechaContratacion=fecha;
        this.sede=sede;
        Empleado.listaEmpleados.add(this);
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
    // Retorna la gente a despedir, que no se pudo transferir para evitar despido, y una lista de mensajes si se nececita imprimir algo.
    static public ArrayList <Object> listaInicialDespedirEmpleado(Fecha fecha){
        // Preparamos las listas de empleados
        ArrayList <Empleado> listaADespedir = new ArrayList<Empleado>(); // A en el doc. 
        ArrayList<String> mensajes = new ArrayList<String>();
        ArrayList <Object> retorno = new ArrayList<Object>(Arrays.asList(listaADespedir,mensajes));
        ArrayList <ArrayList<Empleado>> listaATransferir = new ArrayList<>(Arrays.asList()); // B en el doc.
        for (int i = 0; i < Sede.getlistaSedes().size(); i++){
            listaATransferir.add(new ArrayList<Empleado>());
        }

        // Juzgamos el rendimiento de todos los empleados
        for (Sede sede : Sede.getlistaSedes()){
            for (Empleado emp : sede.getlistaEmpleados()){
                float rendimiento=emp.calcularRendimiento(fecha);
                boolean seVaADespedir = false;

                
                // Añadimos a la lista los empleados con rendimiento insuficiente.
                float rendimientoDeseado = emp.getSede().getRendimientoDeseado(emp.areaActual,fecha);
                if (rendimiento < rendimientoDeseado){
                    seVaADespedir = true;
                    listaADespedir.add(emp);
                    mensajes.add("El empleado "+emp.getNombre()+" ha sido tiene rendimiento insuficiente, con un rendimiento de "+String.format("%,d",(int) rendimiento)+" y un rendimiento deseado de "+String.format("%,d",(int) rendimientoDeseado));
                }
                // Verificamos posibilidades de transferencia.
                if(seVaADespedir){
                    for (int idxSede = 0; idxSede < Sede.getlistaSedes().size(); idxSede++){
                        if (Sede.getlistaSedes().get(idxSede).getRendimientoDeseado(emp.areaActual,fecha) <= rendimiento + 20 && seVaADespedir){
                            mensajes.add("El empleado "+emp.getNombre()+" ha sido transferido a la sede "+Sede.getlistaSedes().get(idxSede).getNombre());
                            listaADespedir.remove(emp);
                            listaATransferir.get(idxSede).add(emp);
                            seVaADespedir = false;
                        }
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
                            mensajes.add("El empleado "+emp.getNombre()+" ha sido transferido al area "+Area.values()[emp.areaActual.ordinal()+1]+" de la sede "+emp.getSede().getNombre());
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
                mensajes.addAll(emp.trasladarEmpleado(Sede.getlistaSedes().get(idxSede)));
            }
        }
        
        return retorno;
    }

    // Interaccion 1 gestion humana. Puedes poner conTransacciones en falso en caso de que quieras quitar al empleado "Limpiamente", por ejemplo
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
    // El retorno es solo por si la funcion nececita avisar algo al usuario y debe imprimirse.
    private ArrayList<String> trasladarEmpleado(Sede sedeNueva){
        ArrayList<String> mensajes = new ArrayList<String>();
        int aPagar = Maquinaria.remuneracionDanos(this);
        if (Banco.getCuentaPrincipal()!=null){
            Banco.getCuentaPrincipal().transaccion(aPagar);
        } else {
            mensajes.add("Perdonenos pero disculpenos: No se ha podido recibir la remuneración de daños, no hay cuenta principal, sugerimos añadir una.");
        }
        modificarBonificacion(aPagar*-1);
        Maquinaria.liberarMaquinariaDe(this);

        traslados++;
        setSede(sedeNueva);

        Maquinaria.asignarMaquinaria(this);
        return mensajes;
    }

    // Metodo ayudante de listarInicialDespedirEmpleado
    public float calcularRendimiento(Fecha fecha){
        float rendimiento=0;
        switch (areaActual){
            case CORTE:

            if (prendasDescartadas==0){
                rendimiento = 100; // Nos evitamos divisiones por 0
            } else {
                rendimiento = (prendasProducidas/prendasDescartadas)*100;
            }
            break;

            case VENTAS:
            int ventasAsesoradas = Venta.filtrar(getSede().getHistorialVentas(),this).size();
            if (ventasAsesoradas!=0){
                rendimiento= Venta.acumuladoVentasAsesoradas(this)/ventasAsesoradas;
            } else {
                rendimiento=0;
            }
            break;

            case OFICINA:
            float acumuladoVentasSede = Venta.filtrar(sede.getHistorialVentas(), fecha).size();
            float promedioVentasSede = acumuladoVentasSede/sede.cantidadPorArea(Area.OFICINA);
            float ventasEncargadas = Venta.cantidadVentasEncargadasEnMes(this,fecha);
            rendimiento = (int) ((ventasEncargadas/promedioVentasSede)*100);

            break;

            case DIRECCION:

            float balancesPositivos = 0;
            float balancesNegativos = 0;
            for (Evaluacionfinanciera evaluacion : evaluaciones){
                if (evaluacion.getBalance()>0){
                    balancesPositivos++;
                } else {
                    balancesNegativos++;
                    if (evaluacion.getBalance()> Evaluacionfinanciera.promedioBalance()*-0.2){
                        balancesNegativos-=0.5; // Damos mejor rendimiento si la perdida no es mucha.
                    }
                }
            }
        
            if (balancesNegativos+balancesPositivos==0){ // Evita dividir por 0 y despedir nuevos
                rendimiento = 100;
            } else{
                rendimiento = ((float) balancesPositivos/(float)(balancesNegativos+balancesPositivos))*100f;
            }
            break;
        }
        return rendimiento;
    }

    // ------------------ Getters y Setters ------------------
    public String toString(){
        return super.toString()+"\n"+"Area: "+areaActual+" - "+"Sede: "+sede+" - "+"Traslados: "+traslados;
    }

    public void modificarBonificacion(int bonificacion){
        this.bonificacion += bonificacion;
    }

    public int getTraslados(){return traslados;}
    public void setTraslados(int traslados){this.traslados=traslados;}
    public int getPrendasDescartadas(){return prendasDescartadas;}
    public void setPrendasDescartadas(int prendas){this.prendasDescartadas=prendas;}  
    public int getPrendasProducidas(){return prendasProducidas;}
    public void setPrendasProducidas(int prendasProducidas){this.prendasProducidas=prendasProducidas;}
    public float getPericia(){return pericia;}
    public void setPericia(float pericia){this.pericia=pericia;}  
    public Area getAreaActual(){return areaActual;}
    public void setAreaActual(Area a){areaActual=a;}
    public Fecha getFechaContratacion(){return fechaContratacion;}
    public void setFechaContratacion(Fecha fecha){fechaContratacion=fecha;}
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
    public void setSalario(int salario){this.salario=salario;}
    public void setEvaluacionesFinancieras(ArrayList<Evaluacionfinanciera> evaluaciones){this.evaluaciones=evaluaciones;}
    public ArrayList<Evaluacionfinanciera> getEvaluacionesFinancieras(){return evaluaciones;}
    public ArrayList<Venta> getVentasEncargadas(){return ventasEncargadas;}

    public static ArrayList<Empleado> getEmpCreadoss(){
        return Empleado.listaEmpleados;
    }

}
