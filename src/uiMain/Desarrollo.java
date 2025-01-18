package uiMain;
// Debemos quitar esta clase antes de entregar.

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import gestorAplicacion.Sede;
import gestorAplicacion.Administracion.Area;
import gestorAplicacion.Administracion.Banco;
import gestorAplicacion.Administracion.Empleado;
import gestorAplicacion.Administracion.Rol;
import gestorAplicacion.Fecha;

public class Desarrollo {
    static void menu(Scanner in){
        System.out.println("En la mayoría de listas puedes usar los commandos quitar, agregar, ver y atras para navegar y modificar cosas.");
        System.out.println("por ejemplo, para quitar el elemento 1, pon quitar, enter, luego 1");
        System.out.println("1.Bancos");
        System.out.println("2.Sedes");
        System.out.println("0.atras");
        bucleDesarrollo:
        while (true) {  
        int numero = in.nextInt();
        switch (numero) {
            case 1:
                verBancos(in);
            case 2:
                verSedes(in);
            case 0:
                break bucleDesarrollo;
            default:
                break;
        }
        }
    }

    static void verBancos(Scanner in){
        System.out.println("Tenemos estos bancos:");
        for (int idxBanco=0; idxBanco<Sede.getlistaSedes().size(); idxBanco++){
            System.out.println("Cuenta de sede"+idxBanco+". "+Sede.getlistaSedes().get(idxBanco).getCuentaSede());
        }
        System.out.println("Cuenta principal: -1."+Banco.getCuentaPrincipal());
        bucleBancos:
        while(true){
            String comando = in.next();
            switch (comando) {
                case "atras":
                    break bucleBancos;
                case "quitar":
                    System.out.println("Cual quieres quitar?");
                    int idxBanco = in.nextInt();
                    if (idxBanco==-1){
                        System.out.println("No puedes quitar la cuenta principal.");
                        break;
                    }
                    Banco.getListaBancos().remove(Banco.getListaBancos().get(idxBanco));
                    Sede sedeSinCuenta= Sede.getlistaSedes().get(idxBanco);
                    sedeSinCuenta.setCuentaSede(null);
                    System.out.println("Le quitaste la cuenta a la sede "+sedeSinCuenta.getNombre()+", las sedes nececitan una cuenta para facturar.");
                    break;
                case "agregar":
                    System.out.println("Cual es el nombre del banco?");
                    String nombre = in.next();
                    System.out.println("Cual es el nombre de la cuenta");
                    String cuenta = in.next();
                    System.out.println("Cual es el saldo inicial?");
                    int saldo = Main.nextIntSeguro(in);
                    Banco nuevoBanco = new Banco(nombre, cuenta,saldo);
                    break;
                case "ver":
                    System.out.println("Cual quieres ver?");
                    int idxSede = in.nextInt();
                    menuSede(in, Sede.getlistaSedes().get(idxSede));
                    break;
                default:
                    break;
            }
        }

    }

    static void verSedes(Scanner in){
        bucleSedes:
        while (true){
            System.out.println("Hay estas sedes:");
            for (int idxSede=0; idxSede<Sede.getlistaSedes().size(); idxSede++){
                System.out.println(idxSede+". "+Sede.getlistaSedes().get(idxSede).getNombre());
            }
            String comando = in.next();
            int idxSede;
            switch (comando) {
                case "atras":
                    
                    break bucleSedes;
                case "quitar":
                    System.out.println("Cual quieres quitar?");
                    idxSede = in.nextInt();
                    Sede.getlistaSedes().remove(idxSede);
                    break;
                case "agregar":
                    System.out.println("Cual es el nombre de la sede?");
                    Sede nuevaSede = new Sede();
                    nuevaSede.setNombre(in.next());
                    break;
                case "ver":
                    System.out.println("Cual quieres ver?");
                    idxSede = in.nextInt();
                    menuSede(in, Sede.getlistaSedes().get(idxSede));
                    break;
                default:
                    break;
            }
        }
    }

    static void menuSede(Scanner in, Sede sede){
        bucleSede:
        while (true) {  
            System.out.println("1.Empleados");
            System.out.println("2.Ventas");
            System.out.println("3.Produccion");
            System.out.println("4.Bodega");
            System.out.println("0.atras");
            int numero = Main.nextIntSeguro(in);
            switch (numero) {
                case 1:
                    menuEmpleados(in, sede);
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    menuInsumos(in, sede);
                    break;
                case 0:
                    break bucleSede;
                default:
                    break;
            }
        }
    }

    static void menuEmpleados(Scanner in, Sede sede){
        bucleEmpleados:
        while (true) {  
            System.out.println("Hay estos empleados");
            for (int idxEmpleado=0; idxEmpleado<sede.getlistaEmpleados().size(); idxEmpleado++){
                System.out.println(idxEmpleado+". "+sede.getlistaEmpleados().get(idxEmpleado));
            }

            String comando = in.next();
            switch (comando) {
                case "quitar":
                    System.out.println("Cual quieres quitar?");
                    int idxEmpleado = in.nextInt();
                    Empleado empleado= sede.getlistaEmpleados().get(idxEmpleado);
                    Empleado.despedirEmpleados(new ArrayList<>(Arrays.asList(empleado)), false);
                    break;
                case "agregar":
                    System.out.println("Cual es el nombre del empleado?");
                    String nombre = in.next();
                    
                    System.out.println("En que area trabaja?");
                    for (Area area : Area.values()){
                        System.out.println(area.ordinal()+". "+area);
                    }
                    Area area = Area.values()[in.nextInt()];

                    System.out.println("Ingrese la fecha de contratacion");
                    Fecha fechaContratacion = Main.ingresarFecha(in);

                    System.out.println("Numero de documento");
                    int doc = in.nextInt();

                    System.out.println("Que rol desempeña?");
                    for (Rol rol : Rol.values()){
                        System.out.println(rol.ordinal()+". "+rol);
                    }
                    Rol rol = Rol.values()[in.nextInt()];

                    Empleado nuevoEmpleado = new Empleado(area,fechaContratacion,sede,nombre,doc,rol,0,null);

                    sede.anadirEmpleado(nuevoEmpleado);
                    break;
                case "atras":
                    break bucleEmpleados;
                default:
                    break;
            }
        }
    }

    static void menuInsumos(Scanner in, Sede sede){
        bucleInsumos:
        while(true){
            System.out.println("Hay estos insumos:");
            for (int idxInsumo=0; idxInsumo<sede.getListaInsumosBodega().size(); idxInsumo++){
                System.out.println(idxInsumo+". "+"de "+sede.getListaInsumosBodega().get(idxInsumo)+" hay "+sede.getCantidadInsumosBodega().get(idxInsumo));
            }
            String comando = in.next();
            switch (comando) {
                case "quitar":
                    System.out.println("Cual quieres quitar? Esto eliminará el insumo de todas partes.");
                    int idxInsumo = Main.nextIntSeguro(in);
                    Sede.quitarInsumoDeBodegas(sede.getListaInsumosBodega().get(idxInsumo));
                    break;
                case "atras":
                    break bucleInsumos;
            
                default:
                    break;
            }
        }
    }
}
