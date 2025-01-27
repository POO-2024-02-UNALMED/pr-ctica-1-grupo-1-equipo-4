package gestorAplicacion;

import java.io.Serializable;

public class Fecha implements Serializable{

	private static final long serialVersionUID = 1L; // Para serializacion
	private int dia;
	private int mes;
	private int año;

	public Fecha(){}
	public Fecha(int dia, int mes, int año){this.dia=dia;this.mes=mes;this.año=año;}

	public int getDia() {return dia;}
	public void setDia(int dia) {this.dia=dia;}
	public int getMes() {return mes;}
	public void setMes(int mes) {this.mes=mes;}
	public int getAño() {return año;}
	public void setaAño(int año) {this.año=año;}
	
	public static boolean compararAño(int año1, int año2) {
		if (año1==año2){
			return true;
		}
		return false;
	}

	public static boolean compararMes(int mes1,int mes2) {
		if (mes1==mes2){
			return true;
		}
		return false;
	}
	public static boolean compararDia(int dia1, int dia2) {
		if (dia1==dia2){
			return true;
		}
		return false;
	}

	public Fecha restarMeses(int meses) {
		int mes = this.mes - meses;
		int año = this.año;
		if (mes == 0) {
			mes=this.mes;
			año=this.año;
		}
		else if (mes < 1) {
			mes += 13;
			año--;
		}
		return new Fecha(this.dia, mes, año);
	}

	public static boolean compararFecha(Fecha fecha1, Fecha fecha2) {
		return (compararAño(fecha1.año, fecha2.año) && compararMes(fecha1.mes, fecha2.mes) && compararDia(fecha1.dia, fecha2.dia));
	}

	public int diasHasta(Fecha hasta) {
		int dias = 0;
		int años = hasta.año - this.año;
		dias += años * 365;
		int meses = hasta.mes - this.mes;
		dias += meses * 30;
		dias += hasta.dia - this.dia;
		return dias;
	}

	public Fecha diaSiguiente(){
		Fecha nuevaFecha = new Fecha(dia,mes,año);
		nuevaFecha.dia++;
		if (nuevaFecha.dia>31){
			nuevaFecha.dia=1;
			nuevaFecha.mes++;
			if (nuevaFecha.mes>12){
				nuevaFecha.mes=1;
				nuevaFecha.año++;
			}
		}
		return nuevaFecha;
	}


	public String toString(){
		return "Día: "+dia+" Mes: "+mes+" Año:"+año;
	}
}