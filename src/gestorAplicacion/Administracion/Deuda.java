

	public String toString(){
		return "La deuda con el "+tipoEntidad+" "+entidad+" inició con un valor de: "+valorinicialDeuda+ "\n"+
				"Con un interés de: "+interes+" y se debía pagar en: "+cuotas+" cuotas" +"\n"+ 
				"Por ahora se ha pagado "+capitalPagado;
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
	
	public static void setListaDeudas(ArrayList<Deuda> listaDeudas) {
	 if (listaDeudas == null) {
            throw new IllegalArgumentException("La lista no puede ser nula");
        }
		Deuda.listaDeudas = listaDeudas;
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

	// Utilizado por unificarDeudasXProveedor para aumentar la deuda.
    public void actualizarDeuda(Fecha fecha, int montoDeuda, int cuotas) {
		long deudaActual=this.deudaActual(fecha.getAño());
		valorinicialDeuda=montoDeuda+deudaActual;
		capitalPagado=0;
		this.cuotas=cuotas;
    }
	
	public static void compararDeudas(Fecha fecha){
		Banco mayorBanco= null;
        Proveedor mayorProveedor = null;
        long mayorPrecioB = 0;
		long mayorPrecioP = 0;
		Deuda deudaP=null;
		Deuda deudaB=null;
		for (Deuda deuda:listaDeudas){
                for (Proveedor proveedor: Proveedor.getListaProveedores()){
					if(proveedor.getDeuda()!=null){
                    long deudap=proveedor.getDeuda().deudaActual(fecha.getAño());
                    if ((deudap != 0) && (!proveedor.getDeuda().estadodePago) && (deudap > mayorPrecioP)) {
                        mayorPrecioP=deudap;
						mayorProveedor=proveedor;
						deudaP=proveedor.getDeuda();
					}}}
					for (Banco banco: Banco.getListaBancos()){
						for (Deuda deudaa: banco.getDeuda()){
						if(deudaa!=null){
						long deudab=deudaa.deudaActual(fecha.getAño());
						if ((deudab != 0) && (!deudaa.estadodePago) && (deudab > mayorPrecioB)) {
							mayorPrecioB=deudab;
							mayorBanco=banco;
							deudaB=deudaa;}}
					}}}
		long pagoP=deudaP.pagarDeuda(fecha);
		deudaP.capitalPagado+=deudaP.deudaActual(fecha.getAño())-pagoP;
		long pagoB=deudaB.pagarDeuda(fecha);
		deudaB.capitalPagado+=deudaB.deudaActual(fecha.getAño())-pagoB;
	}
	public long pagarDeuda(Fecha fecha){
		long pagar=deudaActual(fecha.getAño());
		for (Banco banco : Banco.getListaBancos()){
			while (banco.getAhorroBanco()>=3_000_000){
			if(pagar>0 && pagar-500_000>=0){
				banco.setAhorroBanco(banco.getAhorroBanco()-500_000);
				pagar-=500_000;
			}
			else if (pagar>0){banco.setAhorroBanco(banco.getAhorroBanco()-pagar);}
			else if(pagar==0){estadodePago=true;break;}
		}}
		return pagar;
	}
}