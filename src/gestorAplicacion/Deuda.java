package gestorAplicacion;
import java.util.ArrayList;

public class Deuda {
	private static ArrayList<Deuda> listaDeudas;
	private int fechaEndeudamiento;
	//private String fechaEndeudamiento;
	//LocalDate fechaEndeudamiento;
	private int valorinicialDeuda;
	private int interes;
	private boolean estadodePago;
	private String entidad;
	//nombre de la clase
	private String tipoEntidad;
	private int capitalPagado;
	
	public Double deudaActual(int año){
		Double deudaAcumulada=0.0;
		if (!this.getEstadodePago()){
			int años=año-fechaEndeudamiento;
			//Acá va lo de las fechas
			//int años = fecha - fechaEndeudamiento;
			deudaAcumulada+=(valorinicialDeuda-capitalPagado)+(valorinicialDeuda-capitalPagado)*interes*años;
		} 
		return deudaAcumulada;
	}

	public static ArrayList<Deuda> getListaDeudas(){
		return listaDeudas;
	}
	
	//public String getFechaEndeudamiento(){
	public int getFechaEndeudamiento(){
		return fechaEndeudamiento;
	}
	
	public int getValorinicialDeuda(){
		return valorinicialDeuda;
	}
	
	public int getInteres(){
		return interes;
	}
	
	public boolean getEstadodePago(){
		return estadodePago;
	}
	
	public String getEntidad(){
		return entidad;
	}

	public String getTipoEntidad(){
		return tipoEntidad;
	}
	public int getCapitalPagado(){
		return capitalPagado;
	}
	
	public void setListaDeudas(ArrayList<Deuda> listaDeudas) {
	 if (listaDeudas == null) {
            throw new IllegalArgumentException("La lista no puede ser nula");
        }
		this.listaDeudas = listaDeudas;
	}
	
	//public void setFechaEndeudamiento(String fechaEndeudamiento) {
	public void setFechaEndeudamiento(int fechaEndeudamiento) {
		this.fechaEndeudamiento = fechaEndeudamiento;
	}
	
	public void setValorinicialDeuda(int valorinicialDeuda) {
		this.valorinicialDeuda = valorinicialDeuda;
	}
	
	public void setInteres(int interes) {
		this.interes = interes;
	}
	
	public void setEstadodePago(boolean estadodePago) {
		this.estadodePago = estadodePago;
	}
	
	public void setEntidad(String entidad) {
		this.entidad = entidad;
	}
	
	public void setCapitalPagado(int capitalPagado) {
		this.capitalPagado = capitalPagado;
	}
	
}