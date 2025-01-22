package uiMain;
// Debemos quitar esta clase antes de entregar.

import gestorAplicacion.Administracion.Area;
import gestorAplicacion.Administracion.Banco;
import gestorAplicacion.Administracion.Empleado;
import gestorAplicacion.Administracion.Rol;
import gestorAplicacion.Bodega.Insumo;
import gestorAplicacion.Bodega.Prenda;
import gestorAplicacion.Fecha;
import gestorAplicacion.Membresia;
import gestorAplicacion.Sede;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Desarrollo {
    static void menu(Scanner in){
        System.out.println("En la mayoría de listas puedes usar los commandos quitar, agregar, ver y atras para navegar y modificar cosas.");
        System.out.println("por ejemplo, para quitar el elemento 1, pon quitar, enter, luego 1");
        
        bucleDesarrollo:
        while (true) {
        System.out.println("- Bancos");
        System.out.println("- Sedes");
        System.out.println("- Prendas");
        System.out.println("- atras");
        String comando = in.next().toLowerCase();
        switch (comando) {
            case "bancos":
                verBancos(in);
                break;
            case "sedes":
                verSedes(in);
                break;
            case "prendas":
                verPrendas(in);
                break;
            case "atras":
                break bucleDesarrollo;
            default:
                System.out.println("Comando no reconocido");
                break;
        }
        }
    }

    static void verPrendas(Scanner in){
        buclePrendas:
        while(true){
            System.out.println("Hay estas prendas:");
            for (int idxPrenda=0; idxPrenda<Prenda.getPrendasInventadas().size()-1; idxPrenda++){
                System.out.println(idxPrenda+". "+Prenda.getPrendasInventadas().get(idxPrenda).getNombre());
            }
            String comando = in.next().toLowerCase();
            switch (comando) {
                case "atras":
                    break buclePrendas;
            }
        }
    }

    static void verBancos(Scanner in){
        bucleBancos:
        while(true){
            System.out.println("Tenemos estos bancos:");
            for (int idxBanco=0; idxBanco<Banco.getListaBancos().size(); idxBanco++){
                System.out.println("Cuenta de sede"+idxBanco+". "+Banco.getListaBancos().get(idxBanco));
            }
            System.out.println("Cuenta principal: -1."+Banco.getCuentaPrincipal());
            System.out.println("Usa el comando 'monto' para restar o sumar a una cuenta");
            String comando = in.next().toLowerCase();
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
                    System.out.println("Cual es el interes de la cuenta?");
                    float interes = in.nextFloat();
                    Banco nuevoBanco = new Banco(cuenta,nombre,saldo, interes);
                    System.out.println("Quieres configurar esta cuenta como principal? (s/n)");
                    if (in.next().equals("s")){
                        Banco.setCuentaPrincipal(nuevoBanco);
                    } else {
                        System.out.println("A que sede le quieres asignar esta cuenta?");
                        int idxSede = Main.nextIntSeguro(in);
                        Sede.getlistaSedes().get(idxSede).setCuentaSede(nuevoBanco);
                    }
                    break;
                case "monto":
                    System.out.println("Cual es el monto? Negativo para restar, positivo para sumar.");
                    int monto = Main.nextIntSeguro(in);
                    System.out.println("A que cuenta?");
                    int idxCuenta = Main.nextIntSeguro(in);
                    if (idxCuenta==-1){
                        Banco.getCuentaPrincipal().transaccion(monto);
                    } else {
                        Sede.getlistaSedes().get(idxCuenta).getCuentaSede().transaccion(monto);
                    }
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
            String comando = in.next().toLowerCase();
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
            System.out.println("- Empleados");
            System.out.println("- Ventas");
            System.out.println("- insumos");
            System.out.println("- atras");
            String comando = in.next().toLowerCase();
            switch (comando) {
                case "empleados":
                    menuEmpleados(in, sede);
                    break;
                case "insumos":
                    menuInsumos(in, sede);
                    break;
                case "atras":
                    break bucleSede;
                default:
                    System.out.println("Esa no nos la sabemos");
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
                    Empleado.despedirEmpleados(new ArrayList<>(Arrays.asList(empleado)), false, Main.ingresarFecha(in));
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

                    Empleado nuevoEmpleado = new Empleado(area,fechaContratacion,sede,nombre,doc,rol,0,Membresia.NULA,null);

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
                System.out.println(idxInsumo+". "+"de "+sede.getListaInsumosBodega().get(idxInsumo).getNombre()+" hay "+sede.getCantidadInsumosBodega().get(idxInsumo));
            }
            String comando = in.next();
            switch (comando) {
                case "quitar":
                    System.out.println("Cual quieres quitar? Esto eliminará el insumo de todas partes.");
                    int idxInsumo = Main.nextIntSeguro(in);
                    Sede.quitarInsumoDeBodegas(sede.getListaInsumosBodega().get(idxInsumo));
                    break;
                case "ver":
                    System.out.println("Cual quieres ver?");
                    idxInsumo = Main.nextIntSeguro(in);
                    menuInsumo(in, sede.getListaInsumosBodega().get(idxInsumo));
                    break;
                case "atras":
                    break bucleInsumos;
            
                default:
                    break;
            }
        }
    }

    static void menuInsumo(Scanner in, Insumo insumo){
        bucleInsumo:
        while(true){
            System.out.println("Insumo: "+insumo.getNombre()+ " es proveido por "+insumo.getProveedor().getNombre()+". costoXUnidad: "+insumo.getPrecioIndividual());
            System.out.println("fue comprado por ultima vez a "+insumo.getUltimoPrecio()+" y tiene precioCompra de "+insumo.getPrecioCompra());
            System.out.println("comandos:");
            String comando = in.next();
            switch (comando) {
                case "atras":
                    break bucleInsumo;
            
                default:
                    break;
            }
        }
    }
}
