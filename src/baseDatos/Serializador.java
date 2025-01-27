package baseDatos;

import gestorAplicacion.Administracion.Banco;
import gestorAplicacion.Bodega.Proveedor;
import gestorAplicacion.Persona;
import gestorAplicacion.Sede;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;


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


        // Iteramos por cada archivo de la carpeta donde guardamos las cosas de nuestro programa
        for (File file: docs){
            try {
                // PrintWriter tiene la particularidad de que al crearlo, trunca el archivo.
                PrintWriter pw = new PrintWriter(file);
                pw.close();
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
            try {
                if (file.getAbsolutePath().contains("Sedes")) {
                    // FileOutputStream recibe un archivo y lo abre para escritura.
                    fos = new FileOutputStream(file);
                    // Creamos un ObjectOutputStream, y le decimos a donde pasar los datos.
                    oos = new ObjectOutputStream(fos);
                    // Escribimos el objeto en el flujo de bytes.
                    oos.writeObject(Sede.getlistaSedes());
                    oos.writeObject(Sede.getPrendasInventadasTotal());
                    // Cerramos el flujo de bytes.
                    oos.close();
                    System.out.println("Sedes serializadas, con "+ Sede.getPrendasInventadasTotal().size()+ " prendas inventadas");
                }
                else if (file.getAbsolutePath().contains("Bancos")) {
                        // FileOutputStream recibe un archivo y lo abre para escritura.
                        fos = new FileOutputStream(file);
                        // Creamos un ObjectOutputStream, y le decimos a donde pasar los datos.
                        oos = new ObjectOutputStream(fos);
                        // Escribimos el objeto en el flujo de bytes.
                        oos.writeObject(Banco.getListaBancos());
                        oos.writeObject(Banco.getCuentaPrincipal());
                        // Cerramos el flujo de bytes.
                        oos.close();
                        System.out.println("Bancos/cuentas serializadas");
                }
                else if (file.getAbsolutePath().contains("Personas")) {
                        // FileOutputStream recibe un archivo y lo abre para escritura.
                        fos = new FileOutputStream(file);
                        // Creamos un ObjectOutputStream, y le decimos a donde pasar los datos.
                        oos = new ObjectOutputStream(fos);
                        // Escribimos el objeto en el flujo de bytes.
                        oos.writeObject(Persona.getListaPersonas());
                        // Cerramos el flujo de bytes.
                        oos.close();
                        System.out.println("Peronas serializadas");
                } else if (file.getAbsolutePath().contains("Proveedores")) {
                        fos = new FileOutputStream(file);
                        oos = new ObjectOutputStream(fos);
                        oos.writeObject(Proveedor.getListaProveedores());
                        oos.close();
                        System.out.println("Proveedores serializados");
                }
            } catch (FileNotFoundException e) {
                System.out.println("Archivo "+file.getName()+ "no existe, y de alguna manera esta en la carpeta, llamen a Dios.");
            } catch (IOException e) {
                System.out.println("Hay algo mal con el archivo "+file.getName());
                e.printStackTrace();
            }

        }
    }

}
