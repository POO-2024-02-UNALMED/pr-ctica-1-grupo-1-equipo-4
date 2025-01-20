package gestorAplicacion;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import gestorAplicacion.Administracion.Empleado;
import gestorAplicacion.Administracion.Rol;
import gestorAplicacion.Bodega.Maquinaria;

public class Persona implements Serializable{

	
	private static final long serialVersionUID = 2L; // Para serializacion
	
	protected static ArrayList <Persona> listaPersonas = new ArrayList<Persona>();
	protected String nombre;
	protected int documento;
	protected int salario;
	protected Rol rol;
	protected int experiencia;
	protected boolean trabaja;
	protected Membresia membresia;
	
	Persona(){
		listaPersonas.add(this);
	}
	public Persona (String nom, int doc, Rol rol, int exp, boolean t, Membresia mem){
		this.nombre=nom;
		this.documento=doc;
		this.rol=rol;
		this.experiencia=exp;
		this.trabaja=t;
		this.membresia=mem;
		listaPersonas.add(this);
	}

	public String toString(){
		return "Nombre: "+nombre+"\n"+"Documento: "+documento+"\n"+"Rol: "+rol;}
	public Rol getRol(){return rol;}
	public Membresia getMembresia(){return membresia;}
	public int getDocumento(){return documento;}
	public String getNombre(){return nombre;}
	public int getExperiencia(){return experiencia;}
	public boolean isTrabaja(){return trabaja;}
	public static ArrayList<Persona> getListaPersonas(){return listaPersonas;}
	public static void setListaPersonas(ArrayList<Persona> lista){listaPersonas=lista;} // Usado al deserializear

	// Interacción 3 de Gestion Humana
	// Retorna la lista de aptos, la lista de roles y cuantos de cada rol.
	public static ArrayList<Object> entrevistar(ArrayList<Empleado> aReemplazar){
		ArrayList<Rol> rolesAReemplazar = new ArrayList<Rol>();
		ArrayList<Integer> cantidad = new ArrayList<Integer>();
		for(Empleado empleado: aReemplazar){
			if(!rolesAReemplazar.contains(empleado.getRol())){
				rolesAReemplazar.add(empleado.getRol());
				cantidad.add(0);
			}
			int rolIdx = rolesAReemplazar.indexOf(empleado.getRol());
			cantidad.set(rolIdx, cantidad.get(rolIdx)+1);
		}

		ArrayList<Persona> aptos = new ArrayList<Persona>();

		for (Persona persona:listaPersonas){
			if(persona.trabaja==false && rolesAReemplazar.contains(persona.getRol())){
				aptos.add(persona);
				break;
			}
		}

		ArrayList<Object> retorno = new ArrayList<Object>();
		retorno.add(aptos);
		retorno.add(rolesAReemplazar);
		retorno.add(cantidad);
		return retorno;
	}

	// Interacción 3 de Gestion Humana
	static public void contratar(ArrayList<Persona> aContratar, ArrayList<Empleado> aReemplazar){
		for(Persona persona: aContratar){
			Persona.listaPersonas.remove(persona);
			Empleado emp = new Empleado(persona);
			for(Empleado antiguo: aReemplazar){
				if(antiguo.getRol().equals(persona.getRol())){
					emp.setAreaActual(antiguo.getAreaActual());
					emp.setSede(antiguo.getSede());
					Maquinaria.asignarMaquinaria(emp);
					aReemplazar.remove(antiguo);
					break;
				}
			}
			emp.setSalario(persona.getRol().getSalarioInicial()+persona.getRol().getSalarioInicial()*0.5*persona.getExperiencia());
		}
	}
	public static void imprimirNoEmpleados() {
		ArrayList<Persona> noEmpleados = new ArrayList<>();
		System.out.println("Lista de clientes:");
		for (Persona persona : listaPersonas) {
			if (!(persona instanceof Empleado)) {
				noEmpleados.add(persona);
			}
		}
		
		int index = 0; 
		for (Persona persona : noEmpleados) {
			System.out.println(index + ". " + persona);
			index++;
     }
	}
	public String toString() {
		String rolString;
		if (rol != null) {
			rolString = rol.toString();
		} else {
			rolString = "Sin rol";
		}
	
		String trabajaString;
		if (trabaja) {
			trabajaString = "Trabaja";
		} else {
			trabajaString = "No trabaja";
		}
	
		String membresiaString;
		if (membresia != null) {
			membresiaString = membresia.toString();
		} else {
			membresiaString = "Sin membresía";
		}
	
		return "Nombre: " + nombre +
			   ", Documento: " + documento +
			   ", Rol: " + rolString +
				", Experiencia: " + experiencia +
				", Trabaja: " + trabajaString +
				", Membresía: " + membresiaString;
}

}