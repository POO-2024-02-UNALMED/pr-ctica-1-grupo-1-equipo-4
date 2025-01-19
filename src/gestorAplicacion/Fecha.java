package gestorAplicacion;

import java.io.Serializable;

public class Fecha implements Serializable{

	private static final long serializarVersionUID = 1L; // Para serializacion
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
		if (mes < 1) {
			mes += 12;
			año--;
		}
		return new Fecha(this.dia, mes, año);
	}

	public static boolean compararFecha(Fecha fecha1, Fecha fecha2) {
		return (compararAño(fecha1.año, fecha2.año) && compararMes(fecha1.mes, fecha2.mes) && compararDia(fecha1.dia, fecha2.dia));
	}

	public int diasHasta(Fecha hasta) {
		int dias = 0;
		Fecha fecha = new Fecha(this.dia, this.mes, this.año);
		while (!compararFecha(fecha, hasta)) {
			dias++;
			fecha = fecha.siguienteDia();
		}
		return dias;
	}

	// Actua in-place, modifica la fecha.
	public void siguienteDia(){
		dia++;
		if (dia==32 && (mes==1 || mes==3 || mes==5 || mes==7 || mes==8 || mes==10 || mes==12)){
			mes++;
			dia=0;
		}
		if (dia==31 && (mes==4 || mes==6 || mes==9 || mes==11)){
			mes++;
			dia=0;
		}
		if (dia==30 && mes==2){
			mes++;
			dia=0;
		}
		if(mes==12){
			año++;
			mes=0;
		}
	}
}
