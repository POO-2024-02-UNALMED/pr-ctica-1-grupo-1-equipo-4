package gestorAplicacion.Administracion;
import java.io.Serializable;
import java.util.ArrayList;

import baseDatos.Deserializador;

public class Banco implements Serializable {
	private static final long serializarVersionUID = 3L; // Para serializacion
	private String nombreEntidad;
	private String nombreCuenta;
	private ArrayList<Deuda> deuda;
	private long ahorroBanco;
	private float interes;
	private static ArrayList<Banco> listaBancos = new ArrayList<Banco>();
	private static Banco cuentaPrincipal = new Banco("principal","Bancolombia",125_000_000);


	public Banco(String cuenta, String nombre, int ahorro){
		this.nombreEntidad=nombre;
		this.nombreCuenta = cuenta;
		this.ahorroBanco = ahorro;
		listaBancos.add(this);
	}

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

	// Monto positivo para ingresos, negativo para gastos.
	public void transaccion(long monto) {
		this.ahorroBanco += monto;
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

	public String toString(){
		return "Cuenta: "+nombreCuenta+" en "+nombreEntidad+ " Ahorro: "+String.format("%,d", ahorroBanco);
	}
}