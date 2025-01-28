package gestorAplicacion.Bodega;
import gestorAplicacion.Administracion.Area;
import gestorAplicacion.Administracion.Empleado;
import gestorAplicacion.Administracion.GastoMensual;
import gestorAplicacion.Administracion.Rol;
import gestorAplicacion.Fecha;
import gestorAplicacion.Sede;
import gestorAplicacion.Venta;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import uiMain.Main;

public abstract class Prenda implements GastoMensual, Serializable{
    private static final long serialVersionUID = 1L;
    protected Fecha fechaFabricacion;
	protected String nombre;
    protected Empleado modista;
    protected boolean descartada;
    protected boolean terminada;
    protected Sede sede;
    protected static ArrayList<Maquinaria> maquinaria = new ArrayList<Maquinaria>();
    protected static ArrayList<Integer> cantidadInsumo = new ArrayList<Integer>();
    protected ArrayList<Insumo> insumo = new ArrayList<Insumo>();
    protected ArrayList<Integer> enStock; //Representa el inventario por sede
    // Esta lista es, en cada sede, cuanto hay en stock
    //de esta prenda. El indice es el mismo de la sede en la lista de sedes.
    protected float costoInsumos=0;
    private static int cantidadUltimaProduccion = 0;
    protected int costoProduccion=0;
    protected long precio;
    //costos necesarios para calcular el precio de cada prenda
    protected static float porcentajeGanancia = 0.40f;  // Porcentaje que afecta la cantidad a producir
    //costos necesarios para calcular el precio de cada prenda
    // Porcentaje que afecta la cantidad a producir
    ArrayList<Object> ultimoPaso = new ArrayList<Object>(); // Reescrito al usar siguientePaso()
    
    public Prenda(Fecha fecha, Sede sede, String nombre, Empleado modista, boolean descartada, boolean terminada, ArrayList<Insumo> insumos){
        fechaFabricacion=fecha;
        this.sede=sede;
        this.nombre=nombre;
        this.modista=modista;
        this.descartada=descartada;
        this.terminada=terminada;
        for (int i=0;i<insumos.size();i++){
            this.insumo.add(insumos.get(i));
            this.costoInsumos+=insumos.get(i).getPrecioIndividual();
        }
        int manoObra=0;
        int modistas=0;
        for (Empleado emp:Empleado.getEmpCreadoss()){
            if(emp.getAreaActual().equals(Area.CORTE)){
                manoObra+=emp.calcularSalario();
                modistas++;
            }
        }
        this.costoProduccion=Math.round((manoObra/modistas)*0.08f);
        Sede.getPrendasInventadasTotal().add(this);
        sede.getPrendasInventadas().add(this);
        if(descartada){modista.setPrendasDescartadas(modista.getPrendasDescartadas()+1);}
        else if (terminada) {modista.setPrendasProducidas(modista.getPrendasProducidas()+1);}
    }
    
    public static boolean producirPrendas(ArrayList<ArrayList<ArrayList<Integer>>>planProduccion, Fecha hoy){
        cantidadUltimaProduccion = 0;
        Fecha diaDeProduccion = hoy;
        boolean alcanzaInsumos = true;
        for (ArrayList<ArrayList<Integer>> dia : planProduccion){
            for (int i=0;i<dia.size();i++){
                Sede sede=Sede.getlistaSedes().get(i);
                if (!producirListaPrendas(dia.get(i), sede, diaDeProduccion)){
                    alcanzaInsumos = false;
                }
            }
            diaDeProduccion=diaDeProduccion.diaSiguiente();
        }
        return alcanzaInsumos;
    }

    // true si los insumos alcanzaron, false si no.
    private static boolean producirListaPrendas(ArrayList<Integer> planProduccion, Sede sede, Fecha fechaProduccion){
        boolean alcanzaInsumos = true;
        int cantidadPantalones = planProduccion.get(0);
        int cantidadCamisas = planProduccion.get(1);
        ArrayList<Prenda> prendas = new ArrayList<Prenda>();

        ArrayList<Insumo> insumosPantalon = sede.insumosPorNombre(Pantalon.getTipoInsumo());
        for (int i=0;i<cantidadPantalones;i++){
            if (sede.quitarInsumos(insumosPantalon, Pantalon.getCantidadInsumo())){
                Pantalon pantalon = new Pantalon(fechaProduccion,null,false, false,sede,insumosPantalon);
                prendas.add(pantalon);
            } else {
                alcanzaInsumos = false;
                break;
            }
        }
        ArrayList<Insumo> insumosCamisa = sede.insumosPorNombre(Camisa.getTipoInsumo());
        for (int i=0;i<cantidadCamisas;i++){
            if (sede.quitarInsumos(insumosCamisa, Camisa.getCantidadInsumo())){
                Camisa camisa = new Camisa(fechaProduccion,null,false, false,sede, insumosCamisa);
                prendas.add(camisa);
            } else {
                alcanzaInsumos = false;
                break;
            }
        }

        int idxTanda = 0;
        while (true){
            if (prendas.size()==0){
                break;
            }

            ArrayList<ArrayList<Prenda>> tandas = new ArrayList<ArrayList<Prenda>>();
            ArrayList<Prenda> paraCorte = new ArrayList<Prenda>();
            ArrayList<Prenda> paraTijereado = new ArrayList<Prenda>();
            ArrayList<Prenda> paraCoser = new ArrayList<Prenda>();
            ArrayList<Prenda> paraBordadora = new ArrayList<Prenda>();
            ArrayList<Prenda> paraTermofijado = new ArrayList<Prenda>();
            ArrayList<Prenda> paraPlancha = new ArrayList<Prenda>();
            ArrayList<Prenda> prendasBordadora = new ArrayList<Prenda>();

            for (Prenda paraTanda: prendas){
                ArrayList<Object> siguientePaso = paraTanda.siguientePaso();
                switch (((String) siguientePaso.get(0)).toLowerCase()){
                    case "maquina de corte":
                        paraCorte.add(paraTanda);
                        break;
                    case "maquina de tijereado":
                        paraTijereado.add(paraTanda);
                        break;
                    case "maquina de coser industrial":
                        paraCoser.add(paraTanda);
                        break;
                    case "maquina de bordadora":
                        paraBordadora.add(paraTanda);
                        break;
                    case "maquina de termofijado":
                        paraTermofijado.add(paraTanda);
                        break;
                    case "plancha industrial":
                        paraPlancha.add(paraTanda);
                        break;
                    case "bordadora industrial":
                        prendasBordadora.add(paraTanda);
                        break;
                }
            }

            tandas.add(paraTermofijado);
            tandas.add(paraBordadora);
            tandas.add(paraPlancha);
            tandas.add(paraCorte);
            tandas.add(paraTijereado);
            tandas.add(paraCoser);
            tandas.add(prendasBordadora);


            Empleado modista = Main.pedirModista(prendas.size(), sede,idxTanda);
            for (ArrayList<Prenda> tanda: tandas){
                if (tanda.size()==0){
                    continue; // No hay prendas con esta maquinaria
                }
                Maquinaria maquina = Maquinaria.seleccionarDeTipo(sede,(String) tanda.get(0).ultimoPaso.get(0));
                for (Prenda prenda: tanda){
                    maquina.usar((int) prenda.ultimoPaso.get(1));
                    String resultado = prenda.realizarPaso(modista);
                    if (resultado.equals("DESCARTAR")){
                        prenda.descartada=true;
                        modista.setPrendasDescartadas(modista.getPrendasDescartadas()+1);
                        prendas.remove(prenda);
                    } else if (resultado.equals("LISTO")){
                        prenda.terminada=true;
                        modista.setPrendasProducidas(modista.getPrendasProducidas()+1);
                        prendas.remove(prenda);
                        cantidadUltimaProduccion++;
                    }
                }
            }
            idxTanda++;
        }
        

        return alcanzaInsumos;
    }

    abstract ArrayList<Object> siguientePaso();
    
    abstract String realizarPaso(Empleado modista);

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
    public boolean getPrendasDescartadas(){
        return descartada;
    }
    public String getNombre(){
        return nombre;
    }
    public ArrayList<Insumo> getInsumo(){
        return insumo;
    }
    //public static ArrayList<String> getTipoInsumo(){return tipoInsumo;}
    public static ArrayList<Integer> getCantidadInsumo(){
        return cantidadInsumo;
    }
    public float getCostoInsumos(){
        return costoInsumos;
    }
    public long getPrecio() {
        return this.precio;
    }

    public String toString(){
        return "La prenda de tipo "+nombre;
    }

    public static int getCantidadUltimaProduccion(){
        return cantidadUltimaProduccion;
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
