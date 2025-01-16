package baseDatos;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

import gestorAplicacion.Sede;
import gestorAplicacion.Persona;
import gestorAplicacion.Administracion.Banco;


public class Serializador {
    // Este File representa una carpeta
    private static File rutaTemp = new File("src\\baseDatos\\temp");

    public static void serializar() {

        ObjectOutputStream oos; // Lee el objeto y lo convierte en un flujo de bytes, que se almacena en algún lugar.
        FileOutputStream fos; // El lugar. Recibe el flujo de bytes y lo almacena en un archivo.

        // En este momento vamos a borrar los contenidos del archivo, a esto le llamamos "Truncar",
        // lo hacemos a la hora de guardar para evitar que se acumulen datos viejos y nos generen inconvenientes.

        // Estos File son cada uno un archivo
        File[] docs = rutaTemp.listFiles();
        System.out.println("Encontramos estos archivos para guardar: " +docs.length);
        System.out.println("En la ruta"+rutaTemp.getAbsolutePath());

        PrintWriter pw;

        // Iteramos por cada archivo de la carpeta donde guardamos las cosas de nuestro programa
        for (File file: docs){
            try {
                // PrintWriter tiene la particularidad de que al crearlo, trunca el archivo.
                pw = new PrintWriter(file);
            } catch (FileNotFoundException e) {
                // Esto esta en la presentación, pero parece puesto automaticamente.
                // Si no se puede abrir un archivo que sabemos que existe, es porque algo muy raro paso y el codigo de arriba falló.
                e.printStackTrace();
            }
        }

        // Ahora los archivos entan truncados.
        // Ahora vamos a escribir a ellos, java los crea si no existen.

        for(File file: docs){
            System.out.println("Miramos archivo: "+ file.getName());
            if (file.getAbsolutePath().contains("Sedes")) {
            try {
                // FileOutputStream recibe un archivo y lo abre para escritura.
                fos = new FileOutputStream(file);
                // Creamos un ObjectOutputStream, y le decimos a donde pasar los datos.
                oos = new ObjectOutputStream(fos);
                // Escribimos el objeto en el flujo de bytes.
                oos.writeObject(Sede.getlistaSedes());
                // Cerramos el flujo de bytes.
                oos.close();
                System.out.println("Sedes serializadas");
            } catch (FileNotFoundException e) {
                // Si algo falla, lo imprimimos.
                e.printStackTrace();
            } catch (Exception e) {
                // Si algo falla, lo imprimimos.
                e.printStackTrace();
            }
        }
            if (file.getAbsolutePath().contains("Bancos")) {
                try {
                    // FileOutputStream recibe un archivo y lo abre para escritura.
                    fos = new FileOutputStream(file);
                    // Creamos un ObjectOutputStream, y le decimos a donde pasar los datos.
                    oos = new ObjectOutputStream(fos);
                    // Escribimos el objeto en el flujo de bytes.
                    oos.writeObject(Banco.getListaBancos());
                    // Cerramos el flujo de bytes.
                    oos.close();
                    System.out.println("Bancos/cuentas actualizadas");
                } catch (FileNotFoundException e) {
                    // Si algo falla, lo imprimimos.
                    e.printStackTrace();
                } catch (Exception e) {
                    // Si algo falla, lo imprimimos.
                    e.printStackTrace();
                }
            }
            if (file.getAbsolutePath().contains("Personas")) {
                try {
                    // FileOutputStream recibe un archivo y lo abre para escritura.
                    fos = new FileOutputStream(file);
                    // Creamos un ObjectOutputStream, y le decimos a donde pasar los datos.
                    oos = new ObjectOutputStream(fos);
                    // Escribimos el objeto en el flujo de bytes.
                    oos.writeObject(Persona.getListaPersonas());
                    // Cerramos el flujo de bytes.
                    oos.close();
                    System.out.println("Peronas serializadas");
                } catch (FileNotFoundException e) {
                    // Si algo falla, lo imprimimos.
                    e.printStackTrace();
                } catch (Exception e) {
                    // Si algo falla, lo imprimimos.
                    e.printStackTrace();
                }
            }
        }
    }

}
