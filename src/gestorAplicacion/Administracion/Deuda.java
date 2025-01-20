package gestorAplicacion.Administracion;
import java.io.Serializable;
import java.util.ArrayList;

import gestorAplicacion.Fecha;
import gestorAplicacion.Bodega.Camisa;
import gestorAplicacion.Bodega.Pantalon;
import gestorAplicacion.Bodega.Proveedor;

public class Deuda implements Serializable {
	private static final long serializarVersionUID = 1L; // Para serializacion
	private static ArrayList<Deuda> listaDeudas=new ArrayList<Deuda>();
	private final Fecha FECHACREACION;
	private long valorinicialDeuda;
	private float interes;
	private boolean estadodePago;
	private String entidad;
	//nombre de la clase
	private String tipoEntidad;
	private long capitalPagado;
	private int cuotas;

	public Deuda(Fecha fecha,long valor,String entidad, String tipo,int cuotas){
		FECHACREACION=fecha;
		this.valorinicialDeuda=valor;
		this.entidad=entidad;
		this.tipoEntidad=tipo;
		this.cuotas=cuotas;
		listaDeudas.add(this);
		if(tipo=="Banco"){
			for (Banco banco : Banco.getListaBancos()){
				if (banco.getNombreEntidad().equals(entidad)){
					interes=banco.getInteres();
				}
			}
		}
	}

	public long  deudaActual(int año){
		long deudaAcumulada=0;
		if (!this.getEstadodePago()){
			int años=cuotas-año-FECHACREACION.getAño();
			//Acá va lo de las fechas
			//int años = fecha - fechaEndeudamiento;
			deudaAcumulada+=Math.round((valorinicialDeuda-capitalPagado)+(valorinicialDeuda-capitalPagado)*interes*años);
		} 
		return deudaAcumulada;
	}

	public long deudaMensual(int año){
		long deudaActual=this.deudaActual(año);
		long deudaMensual=Math.round(deudaActual/this.cuotas);
		return deudaMensual;
	}

	public static long calcularDeudaMensual(Fecha fecha, int eleccion){
		//preguntar al usuario qué tipo de deudas incluir
		//Ensayo con ambas
		long deudaCalculada=0;
		switch (eleccion){
                case 1:
					for (Proveedor proveedor : Proveedor.getListaProveedores()) {
						for(Deuda deudaP: proveedor.getDeuda()){
							ArrayList<String> listaInsumos=Pantalon.getTipoInsumo();
							listaInsumos.addAll(Camisa.getTipoInsumo());
							if (!listaInsumos.contains(proveedor.getInsumo().getNombre())){
							deudaCalculada+=deudaP.deudaMensual(fecha.getAño());}
						}
					}
					break;
				case 2:
					for (Banco banco : Banco.getListaBancos()) {
						for(Deuda deudaB: banco.getDeuda()){
							deudaCalculada+=deudaB.deudaMensual(fecha.getAño());
						}
					}
					break;
				case 3:
					for (Proveedor proveedor : Proveedor.getListaProveedores()) {
						for(Deuda deudaP: proveedor.getDeuda()){
							ArrayList<String> listaInsumos=Pantalon.getTipoInsumo();
							listaInsumos.addAll(Camisa.getTipoInsumo());
							if (listaInsumos.contains(proveedor.getInsumo().getNombre())){
							deudaCalculada+=deudaP.deudaMensual(fecha.getAño());}
						}
					}
					for (Banco banco : Banco.getListaBancos()) {
						for(Deuda deudaB: banco.getDeuda()){
							deudaCalculada+=deudaB.deudaMensual(fecha.getAño());
						}
					}
					break;
	}
	return deudaCalculada;
	}

	static public int calcularCuotas(int monto){
		int cuotas = 0;
		if(monto > 0 && monto < 1000000){
			cuotas = 1;
		}
		else if(monto > 5000000 && monto < 10000000){
			cuotas = 7;
		}
		else if(monto > 10000000 && monto < 20000000){
			cuotas = 12;
		}
		else if(monto > 20000000){
			cuotas = 25;
		}
		return cuotas;
	}

	public static ArrayList<Deuda> getListaDeudas(){
		return listaDeudas;
	}
	
	public long getValorinicialDeuda(){
		return valorinicialDeuda;
	}
	
	public float getInteres(){
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
	public long getCapitalPagado(){
		return capitalPagado;
	}

	public Fecha getFechaCreacion(){
		return FECHACREACION;
	}
	
	public void setListaDeudas(ArrayList<Deuda> listaDeudas) {
	 if (listaDeudas == null) {
            throw new IllegalArgumentException("La lista no puede ser nula");
        }
		this.listaDeudas = listaDeudas;
	}
	
	public void setValorinicialDeuda(long valorinicialDeuda) {
		this.valorinicialDeuda = valorinicialDeuda;
	}
	
	public void setInteres(float interes) {
		this.interes = interes;
	}
	
	public void setEstadodePago(boolean estadodePago) {
		this.estadodePago = estadodePago;
	}
	
	public void setEntidad(String entidad) {
		this.entidad = entidad;
	}
	
	public void setCapitalPagado(long capitalPagado) {
		this.capitalPagado = capitalPagado;
	}
	
}