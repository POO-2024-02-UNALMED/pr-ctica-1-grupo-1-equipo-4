

	public static long gastoMensualClase(Fecha fecha){
		long gastoPrenda=0;
        long gastoActual=0;
        long gastoPasado=0;
		for (Prenda prenda:Sede.getPrendasInventadasTotal()){
				long [] lista=prenda.gastoMensualTipo(fecha, prenda.fechaFabricacion, prenda);
				gastoActual+=lista[0];
				gastoPasado+=lista[1];
				}
		if (gastoActual!=0){gastoPrenda=gastoActual;}else{gastoPrenda=gastoPasado;}
        return gastoPrenda;
    }

    // Retorna el pesimismo
    public static float prevenciones(float descuento, float nuevoDescuento,Fecha fecha){
        for(Sede sede : Sede.getlistaSedes()){
            for (Prenda prenda : sede.getPrendasInventadas()){
                if (descuento>0.0F || nuevoDescuento>0.0F){
                if (nuevoDescuento>0.0F){
                    Prenda.porcentajeGanancia-=Prenda.porcentajeGanancia*(1-nuevoDescuento);
                } 
                Venta.setPesimismo(Venta.getPesimismo()-0.05F);
            }
            else {Venta.setPesimismo(Venta.getPesimismo()+0.1F);}
        }}
        return Venta.getPesimismo();
        }
    
    //-------------------Getters y Setters-------------------
    public boolean getPrendasDescartadas(){return descartada;}
    public String getNombre(){return nombre;}
    public ArrayList<Insumo> getInsumo(){return insumo;}
    //public static ArrayList<String> getTipoInsumo(){return tipoInsumo;}
    public static ArrayList<Integer> getCantidadInsumo(){return cantidadInsumo;}
    public float getCostoInsumos(){return costoInsumos;}
    public long getPrecio() {return this.precio;}

    public String toString(){
        return "La prenda de tipo "+nombre;
    }
    
    public float calcularCostoInsumos() {
        this.costoInsumos = 0;
        for (int i = 0; i < insumo.size(); i++) {
               Insumo insumoI = insumo.get(i);
               float cantidad =0;
               if (this instanceof Pantalon)
                cantidad=Pantalon.getCantidadInsumo().get(i);
                else if (this instanceof Camisa)
                cantidad=Camisa.getCantidadInsumo().get(i);
               this.costoInsumos += insumoI.precioXUnidad * cantidad;
           } 
        return this.costoInsumos;
        }//En este método recorro cada índice de la lista de insumos, ya que la lista insumo y cantidadInsumo están relacionadas por índice
       //Obtengo el valor de cada una según el índice en el que se encuentre el recorrido del tamano de la lista insumo
       //Según el precio unitario del insumo, lo multiplico por la cantidad del insumo que requiere la prenda, estos cálculos los voy 
       //Sumando en el atributo de instancia costoInsumos por cada insumo en la lista y su cantidad respectiva
       
        public int calcularCostoProduccion(){
           int sumSalarios = 0;
           for(Empleado empleado: sede.getlistaEmpleados()) {
               if(empleado.getRol().equals(Rol.MODISTA)) {
                   sumSalarios += empleado.getRol().getSalarioInicial();
               }
           this.costoProduccion = Math.round(sumSalarios * 0.01f);
           
       }
           return this.costoProduccion;
       }//En este método sumo cada uno de los salarios de los modistas de la sede, a esta suma le saco el 1% y lo declaro como el costo de Producción
        
       public long calcularPrecio() {
           float costoTotal = this.costoInsumos + this.costoProduccion;
           double gananciaDeseada = costoTotal+ (costoTotal*Prenda.porcentajeGanancia);
           this.precio = Math.round(gananciaDeseada);
           return precio;
          }//Este método calcula el precio de la prenda haciendo uso de una suma entre los costos que se requirieron para la creación de la prenda
          //Luego esta suma la multiplica con el porcentaje de ganancia al cuál se le suma un 1 para sacar el porcentaje total que se desea calcular
         //Este 1 se suma para no solo obtener el valor de ganancia adicional a los costos, sino que sumar en definitiva los costos con el valor de
         //Ganancia y obtener el precio definitivo de la prenda.
       
      
}
