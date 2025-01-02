package gestorAplicacion;
import java.util.ArrayList;

public class Banco {
	private String nombreCuenta;
	private ArrayList<Deuda> deuda;
	private long ahorroBanco;
	private int interes;
	private static ArrayList<Banco> listaBancos;
	private static Banco cuentaPrincipal = new Banco("principal",125_000_000);

	public Banco(String nombre, int ahorro){
		this.nombreCuenta = nombre;
		this.ahorroBanco = ahorro;
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
	
	public int getInteres() {
		return interes;
	}
	
	public static ArrayList<Banco> getListaBancos() {
		return listaBancos;
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
	
	public void setInteres(int interes) {
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
}