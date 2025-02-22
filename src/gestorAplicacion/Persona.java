/* Equipo 4 grupo 1 
 * Clase que define los tipos de personas que pueden trabajar o comprar en la empresa
 */
package src.gestorAplicacion;
import java.io.Serializable;
import java.util.ArrayList;

import src.gestorAplicacion.Administracion.Empleado;
import src.gestorAplicacion.Administracion.Rol;
import src.gestorAplicacion.Administracion.Area;
import src.gestorAplicacion.Bodega.Maquinaria;
import src.uiMain.Main;

public class Persona implements Serializable{

	
	private static final long serialVersionUID = 1L; // Para serializacion
	
	protected static ArrayList <Persona> listaPersonas = new ArrayList<Persona>();
	protected String nombre;
	protected final int documento;
	protected int salario;
	protected Rol rol;
	protected int experiencia;
	protected boolean trabaja;
	protected Membresia membresia;

	public Persona (String nom, int doc, Rol rol, int exp, boolean t, Membresia mem){
		this.nombre=nom;
		this.documento=doc;
		this.rol=rol;
		this.experiencia=exp;
		this.trabaja=t;
		this.membresia=mem;
		listaPersonas.add(this);
	}

        @Override
	public String toString(){
		return "Nombre: "+nombre+" - "+"Documento: "+documento+" - "+"Rol: "+rol;}
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
			}
		}
		ArrayList<Object> retorno = new ArrayList<Object>();
		retorno.add(aptos);
		retorno.add(rolesAReemplazar);
		retorno.add(cantidad);
		return retorno;
	}

	// Interacción 3 de Gestion Humana
	static public void contratar(ArrayList<Persona> aContratar, ArrayList<Empleado> aReemplazar, Fecha fecha){
		for(Persona persona: aContratar){
			Area area=null;
			Sede sede=null;
			for(Empleado antiguo: aReemplazar){
				if(antiguo.getRol().equals(persona.getRol())){
					area = antiguo.getAreaActual();
					sede = antiguo.getSede();
					aReemplazar.remove(antiguo);
					break;
				}
			}
			if(area!= null && sede!=null){
				Empleado emp = new Empleado(area,fecha, sede, persona);
				Maquinaria.asignarMaquinaria(emp);
				emp.setSalario((int) (persona.getRol().getSalarioInicial()+persona.getRol().getSalarioInicial()*0.5f*persona.getExperiencia()));
			} else {
				Main.errorDeReemplazo(persona);
			}
		}
	}
	public String rolString() {
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
	public int calcularSalario(){
		int valor=0;
		valor=Math.round((rol.getSalarioInicial()*0.05F)*experiencia)+rol.getSalarioInicial();
	return valor;
	}

	public static int valorEsperadoSalario(){
		int valorEsperado=0;
		for (Persona persona : listaPersonas) {
			if (!(persona instanceof Empleado)) {
				valorEsperado+=persona.calcularSalario();
			}
		}
		return valorEsperado / listaPersonas.size();
	}
	public static int diferenciaSalarios(){
		int diferencia= Persona.valorEsperadoSalario()-Empleado.valorEsperadoSalario();
		return diferencia;
	}
}