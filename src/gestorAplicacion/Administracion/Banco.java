/* Equipo 4 grupo 1
 * Clase Banco
 * Representa un banco con una cuenta y un ahorro, posiblemente deudas.
 */
package src.gestorAplicacion.Administracion;
import java.io.Serializable;
import java.util.ArrayList;

public class Banco implements Serializable {
	private static final long serialVersionUID = 1L; // Para serializacion
	private String nombreEntidad;
	private String nombreCuenta;
	private ArrayList<Deuda> deuda= new ArrayList<Deuda>();
	private long ahorroBanco;
	private float interes;
	private static ArrayList<Banco> listaBancos = new ArrayList<Banco>();
	private static Banco cuentaPrincipal;


	public Banco(String cuenta, String nombre, long ahorro, float interes){
		this.nombreEntidad=nombre;
		this.nombreCuenta = cuenta;
		this.ahorroBanco = ahorro;
		this.interes = interes;
		Banco.listaBancos.add(this);
	}

	public void actualizarDeuda(Deuda ndeuda){deuda.add(ndeuda);}

	// Monto positivo para ingresos, negativo para gastos.
	public void transaccion(long monto) {
		this.ahorroBanco += monto;
	}
	

	public String toString(){
		return " La Cuenta: "+nombreCuenta+" en: "+nombreEntidad+ "tiene un Ahorro de: "+String.format("%,d", ahorroBanco)+" y para pedir un préstamo el Banco tiene un interés de:"+interes*100+"%";
	}

	static public long totalAhorros(){
		long total=0;
		for (Banco b: listaBancos){
			total+=b.getAhorroBanco();
		}
		return total;
	}


	// ------------------- Getters y Setters -------------------
	public String getNombreEntidad() {
		return nombreEntidad;
	}

	public String getNombreCuenta() {
		return nombreCuenta;
	}
	
	public ArrayList<Deuda> getDeuda() {
		return deuda;
	}
	
	public long getAhorroBanco() {
		return ahorroBanco;
	}
	
	public float getInteres() {
		return interes;
	}
	
	public static ArrayList<Banco> getListaBancos() {
		return listaBancos;
	}
	public void setNombreEntidad(String nombreBanco) {
		nombreEntidad = nombreBanco;
	}

	public void setNombreCuenta(String nombreCuenta) {
		this.nombreCuenta = nombreCuenta;
	}
	
	//public void setDeuda(ArrayList<Deuda> deuda) {
	 //if (deuda == null) {
       //     throw new IllegalArgumentException("La lista no puede ser nula");
       // }
       // this.deuda = deuda;
	//}
	
	public void setAhorroBanco(long ahorroBanco) {
		this.ahorroBanco = ahorroBanco;
	}
	
	public void setInteres(float interes) {
		this.interes = interes;
	}

	public static void setListaBancos(ArrayList<Banco> listaBancos) {
		  if (listaBancos == null) {
	            throw new IllegalArgumentException("La lista no puede ser nula");
	        }
	        Banco.listaBancos = listaBancos;
	}
	public static Banco getCuentaPrincipal() {
		return cuentaPrincipal;
	}

	public static void setCuentaPrincipal(Banco cuentaPrincipal) {
		Banco.cuentaPrincipal = cuentaPrincipal;
	}

}