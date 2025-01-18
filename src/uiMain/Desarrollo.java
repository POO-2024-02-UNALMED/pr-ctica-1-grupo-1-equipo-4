package uiMain;
// Debemos quitar esta clase antes de entregar.

import java.util.Scanner;
import gestorAplicacion.Sede;


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
                break;
            case 2:
                verSedes(in);
            case 0:
                break bucleDesarrollo;
            default:
                break;
        }
        }
    }

    static void verSedes(Scanner in){
        System.out.println("Hay estas sedes:");
        for (int idxSede=0; idxSede<Sede.getlistaSedes().size(); idxSede++){
            System.out.println(idxSede+". "+Sede.getlistaSedes().get(idxSede).getNombre());
        }
        bucleSedes:
        while (true){
        String comando = in.nextLine();
        switch (comando) {
            case "atras":
                
                break bucleSedes;
            case "quitar":
                System.out.println("Cual quieres quitar?");
                int idxSede = in.nextInt();
                Sede.getlistaSedes().remove(idxSede);
                break;
            default:
                break;
        }
        }


    }
}
