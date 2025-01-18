package baseDatos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import gestorAplicacion.Sede;
import gestorAplicacion.Persona;
import gestorAplicacion.Administracion.Banco;

public class Deserializador {
    private static File rutaTemp = new File("src\\baseDatos\\temp");

    public static void deserializar() {
        
        // Estos File son cada uno un archivo
        File[] docs = rutaTemp.listFiles();
        FileInputStream fis;
        ObjectInputStream ois;

        for(File file: docs){
            if (file.getAbsolutePath().contains("Sedes")) {
                try {
                    fis = new FileInputStream(file);
                    ois = new ObjectInputStream(fis);
                    Sede.setlistaSedes((ArrayList<Sede>) ois.readObject());
                } catch (FileNotFoundException e) {
                    System.out.println("Hubo un error al cargar la lista de sedes, no se encontro el archivo, stack trace:");
                    e.printStackTrace();
                } catch (IOException e){
                    System.out.println("Hubo un error al cargar la lista de sedes, no se pudo leer el archivo, stack trace:");
                    e.printStackTrace();
                } catch (ClassNotFoundException e){
                    System.out.println("Hubo un error al cargar la lista de sedes,hay algo mal con el codigo o el serialVersionUID, stack trace:");
                    e.printStackTrace();
                }
            }
            if (file.getAbsolutePath().contains("Bancos")) {
                try {
                    fis = new FileInputStream(file);
                    ois = new ObjectInputStream(fis);
                    Banco.setListaBancos((ArrayList<Banco>) ois.readObject());;
                    Banco.setCuentaPrincipal((Banco) ois.readObject());
                } catch (FileNotFoundException e) {
                    System.out.println("Hubo un error al cargar la lista de bancos, no se encontro el archivo, stack trace:");
                    e.printStackTrace();
                } catch (IOException e){
                    System.out.println("Hubo un error al cargar la lista de bancos, no se pudo leer el archivo, stack trace:");
                    e.printStackTrace();
                } catch (ClassNotFoundException e){
                    System.out.println("Hubo un error al cargar la lista de bancos,hay algo mal con el codigo o el serialVersionUID, stack trace:");
                    e.printStackTrace();
                }
            }
            if (file.getAbsolutePath().contains("Personas")) {
                try {
                    fis = new FileInputStream(file);
                    ois = new ObjectInputStream(fis);
                    Persona.setListaPersonas((ArrayList<Persona>) ois.readObject());
                } catch (FileNotFoundException e) {
                    System.out.println("Hubo un error al cargar la lista de personas, no se encontro el archivo, stack trace:");
                    e.printStackTrace();
                } catch (IOException e){
                    System.out.println("Hubo un error al cargar la lista de personas, no se pudo leer el archivo, stack trace:");
                    e.printStackTrace();
                } catch (ClassNotFoundException e){
                    System.out.println("Hubo un error al cargar la lista de personas,hay algo mal con el codigo o el serialVersionUID, stack trace:");
                    e.printStackTrace();
                }
            }
        }
    }

}
