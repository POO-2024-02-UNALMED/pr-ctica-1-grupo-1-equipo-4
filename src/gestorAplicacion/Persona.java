package gestorAplicacion;
import java.util.ArrayList;

public class Persona {
	
	protected static ArrayList <Persona> listaPersonas;
	protected String nombre;
	protected int documento;
	protected long salario;
	protected Rol rol;
	protected int experiencia;
	protected boolean trabaja;
	protected Membresia membresia;
	
	Persona(){
		listaPersonas.add(this);
	}
	Persona (String nom, int doc, Rol rol, int exp, boolean t, Membresia mem){
		this.nombre=nom;
		this.documento=doc;
		this.rol=rol;
		this.experiencia=experiencia;
		this.trabaja=t;
		this.membresia=mem;
		listaPersonas.add(this);
	}
}