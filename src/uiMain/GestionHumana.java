package uiMain;

import java.util.ArrayList;
import java.util.Scanner;

import gestorAplicacion.Sede;
import gestorAplicacion.Empleado;
import gestorAplicacion.Rol;
import gestorAplicacion.Venta;

public class GestionHumana {
    static public ArrayList<Empleado> despedirEmpleados(Scanner scanner) {
        System.out.println("Obteniendo lista sugerida de empleados");
        ArrayList<Empleado> aDespedir = Empleado.listaInicialDespedirEmpleado();
        for (Empleado emp : aDespedir) {
            System.out.println(emp.getNombre()+" "+emp.getAreaActual());
        }

        System.out.println("Esta es una lista de empleados que no estan rindiendo correctamente, ¿que deseas hacer?");
        int opcion=2;
        while (opcion == 2){
            System.out.println("1. Elegir ya a los despedidos");
            System.out.println("2. Añadir a alguien mas");
            opcion = scanner.nextInt();
            if (opcion == 2){
                System.out.println("¿De que sede quieres añadir al empleado?"); // Para no imprimir una lista demasiado larga
                for(int i = 0; i < Sede.getlistaSedes().size(); i++){
                    System.out.println(i+". "+Sede.getlistaSedes().get(i).getNombre());
                }
                int sede = scanner.nextInt();
                System.out.println("¿Que empleado quieres despedir? Pon su documento.");
                for (Empleado emp : Sede.getlistaSedes().get(sede).getlistaEmpleados()) {
                    System.out.println(emp.getNombre()+" "+emp.getAreaActual()+" "+emp.getDocumento());
                }
                int doc = scanner.nextInt();
                for (Empleado emp : Sede.getlistaSedes().get(sede).getlistaEmpleados()) {
                    if (emp.getDocumento() == doc){
                        aDespedir.add(emp);
                    }
                }
            }
        }

        // Ya tenemos la lista definitiva de despedibles, incluidos los que el usuario quiera.
        ArrayList<Empleado> seleccion = new ArrayList<Empleado>();
        System.out.println("¿Que empleados quieres despedir? Pon sus documentos o FIN para terminar.");
        String doc = scanner.nextLine();
        while (!doc.equals("FIN")){
            for (Empleado emp : aDespedir) {
                if (emp.getDocumento() == Integer.parseInt(doc)){
                    seleccion.add(emp);
                }
            }
            doc = scanner.nextLine();
        }

        // Ya tenemos la lista de empleados a despedir.

        Empleado.despedirEmpleados(seleccion);
        return seleccion;
    }

    public static void reorganizarEmpleados(Scanner in, ArrayList<Empleado> despedidos){
        System.out.println("Todavía nos quedan "+despedidos.size()+" empleados por reemplazar, hay que contratar.");
        ArrayList<Object> necesidades = Sede.obtenerNececidadTransferenciaEmpleados(despedidos);
        // Desempacamos los datos dados por GestorAplicacion
        ArrayList<Rol> rolesATransferir = (ArrayList<Rol>) necesidades.get(0);
        ArrayList<Sede> transferirDe = (ArrayList<Sede>) necesidades.get(1);
        ArrayList<Empleado> aContratar = (ArrayList<Empleado>) necesidades.get(2);

        // Lista de empleados a tranferir de sede, seleccionados por el usuario.
        ArrayList<Empleado> aTransferir = new ArrayList<Empleado>();

        for (int rolidx=0; rolidx<rolesATransferir.size();rolidx++){
            Rol rol = rolesATransferir.get(rolidx);
            Sede sede = transferirDe.get(rolidx);
            System.out.println("Se necesita transferir "+rol+" de "+sede.getNombre()+", estos son los candidatos: Ingresa su numero de documento para hacerlo.");
            for (Empleado emp : sede.getlistaEmpleados()){
                if (emp.getRol() == rol){
                    String descripcion= emp.getNombre()+" Documento:"+emp.getDocumento();
                    switch (emp.getRol()){
                        case VENDEDOR:
                            descripcion += " Ventas asesoradas: "+Venta.acumuladoVentasAsesoradas(emp);
                            break;
                        case MODISTA:
                            descripcion +=" Pericia: "+emp.getPericia();
                            break;
                        default:
                            descripcion+=" contratado en "+emp.getFechaContratacion();
                    }
                    System.out.println(descripcion);
                }
            }

            // Obtenemos la cantidad de empleados a seleccionar
            int cantidad=0;
            for (Empleado emp : despedidos)){
                if (emp.getRol() == rol){
                    cantidad++;
                }
            }
            for (int i=0; i<cantidad; i++){
                int doc = in.nextInt();
                for (Empleado emp : sede.getlistaEmpleados()){
                    if (emp.getDocumento() == doc){
                        aTransferir.add(emp);
                    }
                }
            }
            
        }
        
    }
}
