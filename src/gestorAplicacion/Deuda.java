package gestorAplicacion;
import java.util.ArrayList;

public class Deuda {
	private static ArrayList<Deuda> listaDeudas;
	private String fechaEndeudamiento;
	//LocalDate fechaEndeudamiento;
	private int valorinicialDeuda;
	private int interes;
	private boolean estadodePago;
	private String entidad;
	private int capitalPagado;
	
	public ArrayList<Deuda> getListaDeudas(){
		return listaDeudas;
	}
	
	public String getFechaEndeudamiento(){
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

	public int getCapitalPagado(){
		return capitalPagado;
	}
	
	public void setListaDeudas(ArrayList<Deuda> listaDeudas) {
	 if (listaDeudas == null) {
            throw new IllegalArgumentException("La lista no puede ser nula");
        }
		this.listaDeudas = listaDeudas;
	}
	
	public void setFechaEndeudamiento(String fechaEndeudamiento) {
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