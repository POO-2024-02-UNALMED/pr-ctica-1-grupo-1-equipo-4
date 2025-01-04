package gestorAplicacion;
import java.util.ArrayList;

public abstract class Prenda {
	String nombre;
    Empleado modista;
    boolean descartada;
    boolean terminada;
    Sede sede;
    ArrayList<Maquinaria> maquinaria;
    ArrayList<Float> cantidadInsumo;
    ArrayList<Insumo> insumo;
    ArrayList<Integer> enStock;
    // Esta lista es, en cada sede, cuanto hay en stock
    //de esta prenda. El indice es el mismo de la sede en la lista de sedes.
    int costoInsumos;
    int costoProduccion;
    int porcentajeGanancia;
    ArrayList <Prenda> historialPrendasVendidas;
    float pesimismo;
    // Porcentaje que afecta la cantidad a producir

    public String getNombre(){return nombre;}
    public ArrayList<Insumo> getInsumo(){return insumo;}
}