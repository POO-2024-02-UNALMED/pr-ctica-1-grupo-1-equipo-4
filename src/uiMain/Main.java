package uiMain;

import java.util.Scanner;
import java.util.ArrayList;
import gestorAplicacion.Empleado;

public class Main {
    public static void main (String[] args){
        Scanner in = new Scanner(System.in);
        buclePrincipal:
        while (true){
        System.out.println("¿Que operación desea realizar?");
        System.out.println("1.Reemplazar empleados");
        System.out.println("2.Adquirir insumos para produccion");
        System.out.println("3.Ver el desglose economico de la empresa");
        System.out.println("4.Vender un producto");
        System.out.println("5.Producir prendas");
        System.out.println("0.Salir");
        int opcion = in.nextInt();
        switch(opcion) {
        case 1:
            ArrayList<Empleado> despedidos = GestionHumana.despedirEmpleados(in);
            GestionHumana.reorganizarEmpleados(in, despedidos);
            break;
        case 2:
            break;
        case 3:
            break;
        case 4:
            break;
        case 5:
            break;
        case 0:
            break buclePrincipal;
        default:
            System.out.println("Esa opción no es valida.");
        }

        }
        in.close();
    }
}
