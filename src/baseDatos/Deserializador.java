package baseDatos;

import gestorAplicacion.Administracion.Banco;
import gestorAplicacion.Bodega.Proveedor;
import gestorAplicacion.Persona;
import gestorAplicacion.Sede;
import gestorAplicacion.Bodega.Camisa;
import gestorAplicacion.Bodega.Pantalon;
import gestorAplicacion.Bodega.Prenda;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class Deserializador {
    private static File rutaTemp = new File("src\\baseDatos\\temp");

    public static void deserializar() {
        
        // Estos File son cada uno un archivo
        File[] docs = rutaTemp.listFiles();

        for(File file: docs){
            if (file.length()!=0){
                cargarArchivo(file);
            } else {
                System.out.println("El archivo "+file.getName()+" esta vacio");
            }
        }
          
    }

    private static void cargarArchivo(File file){
        FileInputStream fis;
        ObjectInputStream ois;
        try {
            if (file.getAbsolutePath().contains("Sedes")) {
                    fis = new FileInputStream(file);
                    ois = new ObjectInputStream(fis);
                    Sede.setlistaSedes((ArrayList<Sede>) ois.readObject());
                    Sede.setPrendasInventadasTotal((ArrayList<Prenda>) ois.readObject());
                    System.out.println("Se deserializaron las sedes con "+ Sede.getPrendasInventadasTotal().size()+ " prendas inventadas");

            }
            if (file.getAbsolutePath().contains("Bancos")) {
                    fis = new FileInputStream(file);
                    ois = new ObjectInputStream(fis);
                    Banco.setListaBancos((ArrayList<Banco>) ois.readObject());;
                    Banco.setCuentaPrincipal((Banco) ois.readObject());

            }
            if (file.getAbsolutePath().contains("Personas")) {
   
                    fis = new FileInputStream(file);
                    ois = new ObjectInputStream(fis);
                    Persona.setListaPersonas((ArrayList<Persona>) ois.readObject());

            } else if (file.getAbsolutePath().contains("Proveedores")){
                        fis = new FileInputStream(file);
                        ois = new ObjectInputStream(fis);
                        Proveedor.setListaProveedores((ArrayList<Proveedor>) ois.readObject());
            } else if (file.getAbsolutePath().contains("Prendas")) {
                        fis = new FileInputStream(file);
                        ois = new ObjectInputStream(fis);
                        Camisa.setCantidadInsumo((ArrayList<Integer>) ois.readObject());
                        Camisa.setTipoInsumo((ArrayList<String>) ois.readObject());
                        Pantalon.setCantidadInsumo((ArrayList<Integer>) ois.readObject());
                        Pantalon.setTipoInsumo( (ArrayList<String>) ois.readObject());;
            }
        } catch (FileNotFoundException e) {
            System.out.println("Archivo "+file.getName()+ "vacío.");
        } catch (IOException e) {
            System.out.println("Hay algo mal con el archivo "+file.getName());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Hay algo mal con la clase asociada a "+file.getName());
            e.printStackTrace();
        }
    }
}