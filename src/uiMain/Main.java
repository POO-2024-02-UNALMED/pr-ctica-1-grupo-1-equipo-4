package uiMain;

import gestorAplicacion.Persona;
import gestorAplicacion.Sede;
import gestorAplicacion.Fecha;
import gestorAplicacion.Membresia;
import gestorAplicacion.Venta;
import gestorAplicacion.Administracion.Area;
import gestorAplicacion.Administracion.Banco;
import gestorAplicacion.Administracion.Deuda;
import gestorAplicacion.Administracion.Empleado;
import gestorAplicacion.Administracion.Evaluacionfinanciera;
import gestorAplicacion.Administracion.Rol;
import gestorAplicacion.Administracion.Resultado;
import gestorAplicacion.Bodega.Bolsa;
import gestorAplicacion.Bodega.Prenda;
import gestorAplicacion.Bodega.Insumo;
import gestorAplicacion.Bodega.Maquinaria;
import gestorAplicacion.Bodega.Pantalon;
import gestorAplicacion.Bodega.Proveedor;
import gestorAplicacion.Bodega.Repuesto;
import java.util.ArrayList;
import java.util.Scanner;
import baseDatos.Deserializador;
import baseDatos.Serializador;
import gestorAplicacion.Bodega.Camisa;
import java.lang.reflect.Array;


public class Main {
    public static void main (String[] args){
        Deserializador.deserializar();
        Scanner in = new Scanner(System.in);
        buclePrincipal:
        while (true){
        System.out.println("¿Que operación desea realizar?");
        System.out.println("1.Reemplazar empleados");
        System.out.println("2.Adquirir insumos para produccion");
        System.out.println("3.Ver el desglose economico de la empresa");
        System.out.println("4.Vender un producto");
        System.out.println("5.Producir prendas");
        System.out.println("6.Salir");
        System.out.println("7. Inspeccionar memoria");

        int opcion = in.nextInt();
        Fecha fecha;
        switch(opcion) {
        case 1:
            fecha = ingresarFecha(in);
            ArrayList<Empleado> despedidos = despedirEmpleados(in,fecha);
            ArrayList<Empleado> aContratar = reorganizarEmpleados(in, despedidos);
            contratarEmpleados(aContratar, in);
            break;

        case 2:
            fecha = ingresarFecha(in);
            ArrayList<Object> retorno = planificarProduccion(fecha);
            ArrayList<Object> listaA = coordinarBodegas(retorno);
            ArrayList<Deuda> deuda = comprarInsumos(fecha, listaA);
            break;

        case 3:
            fecha = ingresarFecha(in);
            Evaluacionfinanciera balanceAnterior= calcularBalanceAnterior(fecha, in);
            long diferenciaEstimada= calcularEstimado(fecha,balanceAnterior,in);
            String analisisFuturo=planRecuperacion(diferenciaEstimada, fecha, in);
            String retorna="Según la evaluación del estado Financiero actual: "+balanceAnterior.Informe()+
            "Se realizó un análisis sobre la posibilidad de aplicar descuentos."+analisisFuturo+
            "Este resultado se usó para estimar la diferencia entre ventas y deudas futuras, que fue de: $"+diferenciaEstimada+
            "y por tanto el nuevo porcentaje de pesimismo de la producción es:"+Venta.getPesimismo()+".";
            System.out.println(retorna);
            break;

        case 4:
           Sede venta = Vender(in); 
           

            break;

        case 5:
            
            break;

        case 6:
            Serializador.serializar();
            System.exit(0);
            break buclePrincipal;
        
        case 7:
            Desarrollo.menu(in);
            
        default:
            System.out.println("Esa opción no es valida.");
        }

        }
        in.close();
    }

    // Método ingresarFecha
    static public Fecha ingresarFecha(Scanner scanner){
        int dia=-1;
        int mes=-1;
        while (dia<=0 || dia>31){
            System.out.println("Ingrese día");
            dia = scanner.nextInt();}
        while (mes<=0 || mes>12){
            System.out.println("Ingrese mes");
            mes = scanner.nextInt();}
        System.out.println("Ingrese año");
        int año = scanner.nextInt();
        Fecha fecha=new Fecha(dia,mes,año);
    return fecha;
    }
    
    // Interaccion 1 de Gestion Humana https://docs.google.com/document/d/1IomqwzQR1ZRXw9dFlHx5mA_2oOowyIbxauZeJ6Rqy6Q/edit?tab=t.0#heading=h.z9eys2stm4gz
    static public ArrayList<Empleado> despedirEmpleados(Scanner scanner,Fecha fecha) {
        System.out.println("Obteniendo lista sugerida de empleados");
        ArrayList<Empleado> aDespedir = Empleado.listaInicialDespedirEmpleado(fecha);
        for (Empleado emp : aDespedir) {
            System.out.println(emp.getNombre()+" "+emp.getAreaActual());
        }

        System.out.println("Esta es una lista de empleados que no estan rindiendo correctamente, ¿que deseas hacer?");
        int opcion=2;
        while (opcion == 2){
            System.out.println("1. Elegir a los despedidos");
            System.out.println("2. Añadir a alguien mas");
            opcion = scanner.nextInt();
            if (opcion == 2){
                System.out.println("¿De que sede quieres añadir al empleado?"); // Para no imprimir una lista demasiado larga
                for(int i = 0; i < Sede.getlistaSedes().size(); i++){
                    System.out.println(i+". "+Sede.getlistaSedes().get(i).getNombre());
                }
                int sede = scanner.nextInt();
                System.out.println("¿Que empleado quieres despedir? Pon su documento.");
                for (Empleado emp : Sede.getlistaSedes().get(sede).getlistaEmpleados()) {
                    System.out.println(emp.getNombre()+" "+emp.getAreaActual()+" "+emp.getDocumento());
                }
                int doc = scanner.nextInt();
                for (Empleado emp : Sede.getlistaSedes().get(sede).getlistaEmpleados()) {
                    if (emp.getDocumento() == doc){
                        aDespedir.add(emp);
                    }
                }
            }
        }

        // Ya tenemos la lista definitiva de despedibles, incluidos los que el usuario quiera.
        ArrayList<Empleado> seleccion = new ArrayList<Empleado>();
        System.out.println("¿Que empleados quieres despedir? Pon sus documentos o FIN para terminar.");
        String doc = scanner.nextLine();
        while (!doc.equals("FIN")){
            for (Empleado emp : aDespedir) {
                if (emp.getDocumento() == Integer.parseInt(doc)){
                    seleccion.add(emp);
                }
            }
            doc = scanner.nextLine();
        }

        // Ya tenemos la lista de empleados a despedir.

        Empleado.despedirEmpleados(seleccion, true);
        return seleccion;
    }

    // Interaccion 2 de Gestion Humana https://docs.google.com/document/d/1IomqwzQR1ZRXw9dFlHx5mA_2oOowyIbxauZeJ6Rqy6Q/edit?tab=t.0#heading=h.iadm7mr7n689

    static public ArrayList<Empleado> reorganizarEmpleados(Scanner in, ArrayList<Empleado> despedidos){
        System.out.println("Todavía nos quedan "+despedidos.size()+" empleados por reemplazar, hay que contratar.");
        ArrayList<Object> necesidades = Sede.obtenerNececidadTransferenciaEmpleados(despedidos);
        // Desempacamos los datos dados por GestorAplicacion
        ArrayList<Rol> rolesATransferir = (ArrayList<Rol>) necesidades.get(0);
        ArrayList<Sede> transferirDe = (ArrayList<Sede>) necesidades.get(1);
        ArrayList<Empleado> aContratar = (ArrayList<Empleado>) necesidades.get(2);

        // Lista de empleados a tranferir de sede, seleccionados por el usuario.
        ArrayList<Empleado> aTransferir = new ArrayList<Empleado>();

        for (int rolidx=0; rolidx<rolesATransferir.size();rolidx++){
            Rol rol = rolesATransferir.get(rolidx);
            Sede sede = transferirDe.get(rolidx);
            System.out.println("Se necesita transferir "+rol+" de "+sede.getNombre()+", estos son los candidatos: Ingresa su numero de documento para hacerlo.");
            for (Empleado emp : sede.getlistaEmpleados()){
                if (emp.getRol().equals(rol)){
                    String descripcion= emp.getNombre()+" Documento:"+emp.getDocumento();
                    switch (emp.getRol()){
                        case VENDEDOR:
                            descripcion += " Ventas asesoradas: "+Venta.acumuladoVentasAsesoradas(emp);
                            break;
                        case MODISTA:
                            descripcion +=" Pericia: "+emp.getPericia();
                            break;
                        default:
                            descripcion+=" contratado en "+emp.getFechaContratacion();
                    }
                    System.out.println(descripcion);
                }
            }

            // Obtenemos la cantidad de empleados a seleccionar
            int cantidad=0;
            for (Empleado emp : despedidos){
                if (emp.getRol().equals(rol)){
                    cantidad++;
                }
            }
            for (int i=0; i<cantidad; i++){
                int doc = in.nextInt();
                for (Empleado emp : sede.getlistaEmpleados()){
                    if (emp.getDocumento() == doc){
                        aTransferir.add(emp);
                    }
                }
            }
            
        }

        Sede.reemplazarPorCambioSede(despedidos, aTransferir);
        
        return aContratar;
    }

    // Interaccion 3 gestion Humana https://docs.google.com/document/d/1IomqwzQR1ZRXw9dFlHx5mA_2oOowyIbxauZeJ6Rqy6Q/edit?tab=t.0#heading=h.zgpssvyj8l65
    static public void contratarEmpleados(ArrayList<Empleado> aReemplazar, Scanner in){
        ArrayList<Object> elecciones = Persona.entrevistar(aReemplazar);
        ArrayList<Persona> aptos = (ArrayList<Persona>) elecciones.get(0);
        ArrayList<Rol> rolesAReemplazar = (ArrayList<Rol>) elecciones.get(1);
        ArrayList<Integer> cantidad = (ArrayList<Integer>) elecciones.get(2);

        ArrayList<Persona> aContratar = new ArrayList<Persona>();
        for (int i=0; i<rolesAReemplazar.size(); i++){
            Rol rol = rolesAReemplazar.get(i);
            int cantidadNecesaria = cantidad.get(i);

            System.out.println("Se nececitan "+cantidadNecesaria+" "+rol+"s, estos son los candidatos:");

            for (Persona persona : aptos){
                System.out.println("Nombre: "+persona.getNombre()+" Documento: "+persona.getDocumento()+"con "+ persona.getExperiencia()+" años de experiencia.");  
            }

            System.out.println("Ingresa el documento de los que quieres contratar.");

           

            for(int cantidadContratada = 0; cantidadContratada < cantidadNecesaria; cantidadContratada++){
                int doc = in.nextInt();
                for (Persona persona : aptos){
                    if (persona.getDocumento() == doc){
                        aContratar.add(persona);
                    }
                }
            }

        }

        Persona.contratar(aContratar,aReemplazar);
    }

//Interacción 1 Sistema Financiero
public static Evaluacionfinanciera calcularBalanceAnterior(Fecha fecha, Scanner in){
    System.out.println("Obteniendo balance de ventas/producción");
            double balanceCostosProduccion =Venta.calcularBalanceVentaProduccion(fecha);
            int eleccion=0;
            while (eleccion<=0 || eleccion>=3){
            System.out.println("Ingrese las deudas que quiere calcular");
            System.out.println("Ingrese 1 para proveedor, 2 para Banco o 3 para ambos");
            eleccion = in.nextInt();}
            double deudaCalculada=Deuda.calcularDeudaMensual(fecha,eleccion);
            double balanceTotal=balanceCostosProduccion-deudaCalculada;
            Empleado empleado=null;
            ArrayList<Empleado> elegible = new ArrayList<Empleado>();
            for (Empleado emp:Empleado.listaEmpleados){
                if (emp.getAreaActual().equals(Area.DIRECCION)){
                    elegible.add(emp);
                }
                int i =-1;
                while (i<0||i>=elegible.size()){
                    System.out.println("Ingrese número de 0 a "+(elegible.size()-1+""));
                    i=in.nextInt();
                    empleado = elegible.get(i);}
            }
            Evaluacionfinanciera nuevoBalance=new Evaluacionfinanciera (balanceTotal,empleado);
            return nuevoBalance;
            }

//Interaccion 2 Sistema Financiero
public static long calcularEstimado(Fecha fecha,Evaluacionfinanciera balanceAnterior,Scanner in){

    float porcentaje=-1F;
    while (porcentaje>0.6F || porcentaje<1){
        System.out.println("Ingrese porcentaje a modificar para fidelidd de oro entre 0.6 y 1, o 0.0 si no desea modificarlo");
        porcentaje = in.nextFloat();}
    long diferenciaEstimada=Evaluacionfinanciera.estimadoVentasGastos(fecha, porcentaje, balanceAnterior);
    //Un mes se puede dar por salvado si el 80% de los gastos se pueden ver cubiertos por las ventas predichas
    return diferenciaEstimada;
    }     

//Interaccion 3 Sistema Financiero
public static String planRecuperacion(long diferenciaEstimada,Fecha fecha, Scanner in){
    if (diferenciaEstimada<=0){
        System.out.println("El estimado es negativo, la deuda supera las ventas");
        int i =-1;
        String Nombrebanco=null;
        while (i<0||i>=Banco.getListaBancos().size()){
            System.out.println("Ingrese número de 0 a "+(Banco.getListaBancos().size()-1+""));
            i=in.nextInt();
            Nombrebanco = Banco.getListaBancos().get(i).getNombreEntidad();}
        int cuotas=0;
        while (i<=0||i>=180){
            System.out.println("Ingrese número de 1 a 180");
            cuotas=in.nextInt();}
        Deuda deudaAdquirir=new Deuda(fecha,diferenciaEstimada,Nombrebanco, "Banco",cuotas); 
        }
    float descuento=Venta.blackFriday(fecha);
    String BFString=null;
    if (descuento==0.0F){
        BFString="El análisis de ventas realizado sobre el Black Friday arrojó que la audiencia no reacciona tan bien a los descuentos, propusimos no hacer descuentos";
        System.out.println("Según las Ventas anteriores,  aplicar descuentos no funcionará");
    }else{
        BFString="El análisis de ventas realizado sobre el Black Friday arrojó que la audiencia reacciona bien a los descuentos, propusimos un descuento del "+descuento*100+"%";
        System.out.println("Según las Ventas anteriores, aplicar descuentos si funcionará");}
        System.out.println("¿Desea Cambiar el siguiente descuento: "+(descuento)+"? marque 1 para Si, 2 para no ");
        int num=in.nextInt();
        float nuevoDescuento=0.0F;
        if(num==1){
            while (nuevoDescuento<0.0||descuento>0.5){
                System.out.println("Ingrese descuento entre 0.0 y 0.5");
                nuevoDescuento=in.nextFloat();}}
    Prenda.prevenciones(descuento, nuevoDescuento,fecha);
    String analisisFuturo=BFString+ ", sin embargo su desición fue aplicar un descuento de: " +nuevoDescuento*100+"%.";
    return analisisFuturo;
    }



//Funcionalidad Insumos

 // Interacción 1 de Insumos
 static public ArrayList<Object> planificarProduccion(Fecha fecha){
    //ArrayList<Insumo> listaGuia = new ArrayList<>();
    ArrayList<Object> listaXSede = new ArrayList<>();
    ArrayList<Insumo> insumoXSede = new ArrayList<>();
    ArrayList<Float> cantidadAPedir = new ArrayList<>();
    ArrayList<Object> retorno= new ArrayList<>();

    for(Sede x: Sede.getlistaSedes()){
        
        for(Prenda prenda: Sede.getPrendasInventadas()){
            int proyeccion = predecirVentas(fecha, x, prenda); 

            System.out.println("Sede: "+x+"Prenda: "+prenda+"Proyección: "+proyeccion+ 
                                " Porcentaje de pesimismo: "+Venta.getPesimismo());
            System.out.println("Seleccione una de las siguientes opciones:");
            System.out.println("1. Estoy de acuerdo con el porcentaje de pesimismo");
            System.out.println("2. Deseo cambiar el porcentaje de pesimismo");  
    
            Scanner in = new Scanner(System.in);
            int opcion = in.nextInt();
            switch(opcion) {
            case 1:
                break;
            case 2:
                Scanner porcentaje = new Scanner(System.in);
                int newPesimism = porcentaje.nextInt();
                Venta.setPesimismo(newPesimism);
                break;
            default:
                System.out.println("Esa opción no es valida.");
            }

            float prediccion = proyeccion * (1 - Venta.getPesimismo());

            for(Prenda p:Prenda.getPrendasInventadas()){
                for(Insumo insumo: p.getInsumo()){
                    insumoXSede.add(insumo);
                }
                for(Float i: Prenda.getCantidadInsumo()){
                    cantidadAPedir.add(i * prediccion);
                }
            }
        }

        listaXSede.add(insumoXSede);
        listaXSede.add(cantidadAPedir);
        retorno.add(listaXSede);}
        //retorno.add(listaGuia);}
    return retorno; }

// Regresión lineal    
// Utiliza minimos cuadrados para predecir las ventas de una prenda en una sede
static public int predecirVentas(Fecha fechaActual,Sede sede, Prenda prenda){
    int n=5; // Cantidad de meses previos a usar
    int sumatoriax=0+1+2+3+4+5;
    int sumatoriaxCuadrado=1+2^3+3^2+4^2+5^2;
    int sumatoriaY=0;
    int sumatoriaYCuadrado=0;
    int sumatoriaXY=0;  
    // Iteramos por los 5 meses anteriores
    for(int meses=0;meses<5;meses++){
        //Iteramos por las ventas de la sede de ese mes
        int sumatoriaYMes=0;
        for(Venta venta: Venta.filtrarPorMes(sede.getHistorialVentas(), fechaActual.restarMeses(5-meses))){
            if (venta.getArticulos().contains(prenda)){
                sumatoriaYMes+=venta.getCantidades().get(venta.getArticulos().indexOf(prenda));
            }
        }
        sumatoriaY+=sumatoriaYMes;
        sumatoriaYCuadrado+=sumatoriaYMes^2;
        sumatoriaXY+=sumatoriaYMes*meses;
    }
    //Calculamos los datos de la funcion lineal
    double pendiente=(n*sumatoriaXY-sumatoriax*sumatoriaY)/(n*sumatoriaxCuadrado-sumatoriax^2);
    double intercepcion = (sumatoriaY-pendiente*sumatoriax)/n;
    // y=pendiente*x+intercepcion
    return (int) Math.ceil(pendiente*6+intercepcion); }

    
    
// Interacción 2 de Insumos
static public ArrayList<Object> coordinarBodegas(ArrayList<Object> retorno){
    ArrayList<Object> listaXSede = new ArrayList<>();
    ArrayList<Insumo> listaInsumos = new ArrayList<>();
    ArrayList<Integer> listaCantidades = new ArrayList<>();
    ArrayList<Insumo> insumosAPedir = new ArrayList<>();
    ArrayList<Integer> cantidadAPedir = new ArrayList<>();
    ArrayList<Object> listaA = new ArrayList<Object>();
    ArrayList<Object> listaSede = new ArrayList<>();


    for (Object sede : retorno) {
        // Convertir cada elemento en un ArrayList<Object> correspondiente a una sede
        listaXSede = (ArrayList<Object>) sede;
        
        // Extraer las listas internas: insumos y cantidades
        listaInsumos = (ArrayList<Insumo>) listaXSede.get(0);
        listaCantidades = (ArrayList<Integer>) listaXSede.get(1);
        
        for(Sede s: Sede.getlistaSedes()){
            for(Insumo i: listaInsumos){
                Resultado productoEnBodega = Sede.verificarProductoBodega(i, s);
                if(productoEnBodega.getEncontrado() == true){
                    int restante = Sede.restarInsumo(i, s, listaCantidades.get((int)productoEnBodega.getIndex()));
                    if(restante != 0){
                        Resultado productoEnOtraSede = Sede.verificarProductoOtraSede(i);
                        if(productoEnOtraSede.getEncontrado() == true){
                            System.out.println("Tenemos el insumo "+i.getNombre()+" en nuestra sede "+productoEnOtraSede.getSede()+".");
                            System.out.println("El insumo tiene un costo de "+productoEnOtraSede.getPrecio());
                            System.out.println("Seleccione una de las siguientes opciones:");
                            System.out.println("1. Deseo transferir el insumo desde la sede "+productoEnOtraSede.getSede());
                            System.out.println("2. Deseo comprar el insumo");  
                            
                            Scanner in = new Scanner(System.in);
                            int opcion = in.nextInt();
                            switch(opcion) {
                            case 1:
                                int restante2 = Sede.restarInsumo(i, s, restante);
                                if(restante2 != 0){
                                    insumosAPedir.add(i);
                                    cantidadAPedir.add(restante2);
                                }
                                break;
                            case 2:
                                insumosAPedir.add(i);
                                cantidadAPedir.add(restante);
                                break;
                            default:
                                System.out.println("Esa opción no es valida.");
                            }

                        }
                    }
                    
                }
            }
    
    listaSede.addAll(insumosAPedir);
    listaSede.addAll(cantidadAPedir);
    listaA.add(listaSede);
    
        }
    }

    return listaA;
}


//Interacción 3 de Insumos
static public ArrayList<Deuda> comprarInsumos(Fecha fecha, ArrayList<Object> listaA){
    ArrayList<Object> sede = new ArrayList<>();
    ArrayList<Insumo> insumos = new  ArrayList<>();
    ArrayList<Integer> cantidad = new  ArrayList<>();
    ArrayList<Proveedor> proveedores = new  ArrayList<>();
    ArrayList<Integer> precios = new ArrayList<>();
    ArrayList<Deuda> deudas = new ArrayList<>();

    for (Object s : listaA) {
        sede = (ArrayList<Object>) s;
        insumos = (ArrayList<Insumo>) sede.get(0);
        cantidad = (ArrayList<Integer>) sede.get(1);

        for(Sede sedee: Sede.getlistaSedes()){
    
            for(int i =0; i < insumos.size(); i++){
                Proveedor mejorProveedor = null;
                int mejorPrecio = 0;
                int cantidadAñadir = 0;

                for (Proveedor proveedor: Proveedor.getListaProveedores()){
                    
                    if(proveedor.getInsumo().equals(insumos.get(i))){
                        proveedores.add(proveedor);
                        precios.add(Proveedor.costoDeLaCantidad(insumos.get(i), cantidad.get(i)));
                    }
                }

                for (Proveedor x : proveedores) {
                    int precio = x.costoDeLaCantidad(insumos.get(i), cantidad.get(i));
                    if ((precio != 0) && (precio < mejorPrecio)) {
                        mejorPrecio = precio;
                        mejorProveedor = x;
                            }}
                
                System.out.println("Tenemos el insumo "+insumos.get(i).getNombre()+" con nuestro proveedor "+proveedores.get(i).getNombre()+".");

                if(insumos.get(i).getPrecioIndividual()<insumos.get(i).getUltimoPrecio()){
                    System.out.println("Dado que el costo de la venta por unidad es menor al ultimo precio por el que compramos el insumo");
                    System.out.println("Desea pedir mas de la cantidad necesaria para la producción? ");
                    System.out.println("Cantidad: "+cantidad.get(i));                    
                    System.out.println("1. Si");
                    System.out.println("2. No");  
                                
                    Scanner in = new Scanner(System.in);
                    int opcion = in.nextInt();
                    switch(opcion) {
                    case 1:
                        if(opcion >=0){
                            System.out.println("Cuanta cantidad más desea pedir del insumo "+insumos.get(i).getNombre());
                            Scanner cant = new Scanner(System.in);
                            cantidadAñadir = cant.nextInt();
                        }
                        else{
                            System.out.println("Esa opción no es valida.");
                        }
                        break;
                    case 2:
                        break;
                    default:
                        System.out.println("Esa opción no es valida.");
                    }
                }       
                cantidad.set(i,((cantidad.get(i))+cantidadAñadir));

                Sede.añadirInsumo(insumos.get(i), sedee, cantidad.get(i));

                for (Proveedor proveedor: Proveedor.getListaProveedores()){
                    int montoDeuda = 0;
                    if(insumos.get(i).getProveedor().getNombre().equals(proveedor.getNombre())){
                        montoDeuda += insumos.get(i).getPrecioIndividual()*cantidad.get(i);
                    }
                    if(montoDeuda > 0){
                        Deuda deuda = new Deuda(fecha, montoDeuda, "entidad", "tipo", 5);
                        deudas.add(deuda);
                    }
                    
                }
            }
            
                
                
            
        }
    }
    return deudas;
}



    //METODO PARA CREAR LAS SEDES, LAS MAQUINAS Y LOS REPUESTOS,
    //REQUERIDO PARA LA INTERACCION 1 DE LA FUNCIONALIDAD DE PRODUCCION....
    
public void crearSedesMaquinasRepuestos(){
        //CREACION DE TODOS LOS REPUESTOS QUE MANEJAREMOS PARA LA FUNCIONALIDAD PRODUCCION
    Repuesto AgujasMC = new Repuesto("Agujas de la Maquina de Coser", 12);
    Repuesto Aceite = new Repuesto("Aceite", 60);

    Repuesto Cuchillas = new Repuesto("Cuchillas", 60);
    Repuesto Afiladores = new Repuesto("Afiladores", 750);

    Repuesto ResistenciaElectrica = new Repuesto("Resistencia Electrica", 1500);
    Repuesto MangueraDeVapor = new Repuesto("Manguera de Vapor", 750);

    Repuesto AgujasBI = new Repuesto("Agujas de la Bordadora Industrial", 25);

    Repuesto BandasDeTransmision = new Repuesto("Bandas de Transmision", 2500);
   
    Repuesto TintaN = new Repuesto("Tinta Negra Impresora", 3000);

    
    Repuesto Lector = new Repuesto("Lector de barras", 3000);
    Repuesto PapelQuimico = new Repuesto("Papel químico", 72);

    Repuesto Cargador = new Repuesto("Cargador Computador", 6000);
    Repuesto Mouse = new Repuesto("Mouse Computador", 9000);
    
    //AGRUPACION DE LOS REPUESTOS EN LISTAS PARA ENVIARLOS A LAS MAQUINAS CORRESPONDIENTES
    ArrayList<Repuesto> repuestosMC = new ArrayList<>();
    ArrayList<Repuesto> repuestosMCorte = new ArrayList<>();
    ArrayList<Repuesto> repuestosPI = new ArrayList<>();
    ArrayList<Repuesto> repuestosBI = new ArrayList<>();
    ArrayList<Repuesto> repuestosMTermofijado = new ArrayList<>();
    ArrayList<Repuesto> repuestosMTijereado = new ArrayList<>();
    ArrayList<Repuesto> repuestosImp = new ArrayList<>();
    ArrayList<Repuesto> repuestosRe = new ArrayList<>();
    ArrayList<Repuesto> repuestosComp= new ArrayList<>();

    repuestosImp.add(TintaN);

    repuestosRe.add(PapelQuimico);
    repuestosRe.add(Lector);

    repuestosComp.add(Mouse);
    repuestosComp.add(Cargador);

    repuestosMC.add(AgujasMC);
    repuestosMC.add(Aceite);

    repuestosMCorte.add(Cuchillas);
    repuestosMCorte.add(Afiladores);

    repuestosPI.add(ResistenciaElectrica);
    repuestosPI.add(MangueraDeVapor);

    repuestosBI.add(AgujasBI);
    repuestosBI.add(Aceite.copiar());

    repuestosMTermofijado.add(BandasDeTransmision);
    repuestosMTermofijado.add(ResistenciaElectrica.copiar());

    repuestosMTijereado.add(Cuchillas.copiar());
    repuestosMTijereado.add(Aceite.copiar());

        //CREACION DE LAS MAQUINAS QUE MANEJAREMOS CON SUS RESPECTIVOS RESPUESTOS
    Maquinaria MaquinaDeCoser = new Maquinaria("Maquina de Coser Industrial", 4250000, 600, repuestosMC);
    Maquinaria MaquinaDeCorte = new Maquinaria("Maquina de Corte", 6000000, 700, repuestosMCorte);
    Maquinaria PlanchaIndustrial = new Maquinaria("Plancha Industrial", 2000000, 900, repuestosPI);
    Maquinaria BordadoraIndustrial = new Maquinaria("Bordadora Industrial", 31000000, 500, repuestosBI);
    Maquinaria MaquinaDeTermofijado = new Maquinaria("Maquina de Termofijado", 20000000, 1000, repuestosMTermofijado);
    Maquinaria MaquinaDeTijereado = new Maquinaria("Maquina de Tijereado", 5000000, 600, repuestosMTijereado);
    Maquinaria Impresora = new Maquinaria("Impresora", 800000, 2000, repuestosImp);
    Maquinaria Registradora = new Maquinaria("Caja Registradora", 700000, 17000, repuestosRe);
    Maquinaria Computador = new Maquinaria("Computador", 2_000_000, 10000, repuestosImp);

        //CREACION DE LAS SEDES QUE MANEJAREMOS, CON SUS RESPECTIVAS MAQUINAS EN CADA UNA DE ELLAS
    Sede sedeP = new Sede("Sede Principal");
    Sede sede2 = new Sede("Sede 2");
    
    new Banco("Banco Montreal","principal",400_000_000,0.05F);
    Banco b1=new Banco("Banco Montreal","secundaria",5_000_000,0.05F);
    Banco b3=new Banco ("Bancolombia","principal",125_000_000,0.09F);
    new Banco ("Banco Davivienda","principal",80_000_000,0.1F); 
    Banco b2=new Banco ("Banco de Bogotá","principal",140_000_000,0.07F);
    Banco tm=new Banco("Inversiones Terramoda","principal",160_000_000,0.0F);

    Deuda d1=new Deuda(new Fecha(15,1,20),20_000_000,"Bancolombia","Banco",1);
    b3.actualizarDeuda(d1);
    d1.setEstadodePago(true);
    d1.setCapitalPagado(20_000_000);
    Deuda d2=new Deuda(new Fecha(15,1,20),100_000_000,"Banco Montreal","Banco",18);
    d2.setCapitalPagado(100_000_000/2);
    b1.actualizarDeuda(d2);
    b2.actualizarDeuda(new Deuda(new Fecha(10,1,24),5_000_000,"Banco de Bogotá","Banco",10));
    tm.actualizarDeuda(new Deuda(new Fecha(30,9,22),150_000_000,"Inversiones Terramoda","Banco",18));
    tm.actualizarDeuda(new Deuda(new Fecha(20,2,23),800_000,"Inversiones Terramoda","Banco",18));

    //Episodio 43
    
    Proveedor p1=new Proveedor(15000,"Rag Tela");
    p1.setInsumo(new Insumo("Tela", p1));
    Proveedor p2=new Proveedor(20000,"Macro Telas");
    p2.setInsumo(new Insumo("Hilo", p2));
    Proveedor p4=new Proveedor(7000,"Insumos textileros");
    p4.setInsumo(new Insumo("Cremallera", p4));
    Proveedor p3=new Proveedor(5000,"San Remo");
    p3.setInsumo(new Insumo("Boton", p3));
    Proveedor p5=new Proveedor(18000,"Fatelares");
    p5.setInsumo(new Insumo("Tela", p5));
    Proveedor p6=new Proveedor(20000,"Macro Textil");
    p6.setInsumo(new Insumo("Tela", p6));
    Proveedor p9=new Proveedor(25000,"Hilos Venus");
    p9.setInsumo(new Insumo("Hilo", p9));
    Proveedor p7=new Proveedor(10000,"Insumos para Confección");
    p7.setInsumo(new Insumo("Cremallera", p7));
    Proveedor p8=new Proveedor(8000,"InduBoton");
    p8.setInsumo(new Insumo("Boton", p8));
    Proveedor p10=new Proveedor(5000,"Primavera");
    p10.setDescuento(0.06F);
    p10.setInsumo(new Bolsa("Bolsa", p10));
    Proveedor p11=new Proveedor(8000,"Empaques y Cartones");
    p11.setInsumo(new Bolsa("Bolsa", p11));
    p11.setDescuento(0.1F);
    Proveedor p12=new Proveedor(6000,"Plastienda");
    p10.setDescuento(0.05F);
    p12.setInsumo(new Bolsa("Bolsa", p12));

    Insumo i1=new Insumo("Tela",1*20, p1,sedeP);
    Insumo i2=new Insumo("Tela",1*20, p1,sede2);
    Insumo i3=new Insumo("Boton",4*20, p1,sedeP);
    Insumo i4=new Insumo("Boton",4*20, p1,sede2);
    Insumo i5=new Insumo("Cremallera",1*20, p1,sedeP);
    Insumo i6=new Insumo("Cremallera",1*20, p1,sede2);
    Insumo i7=new Insumo("Hilo",100*20, p1,sedeP);
    Insumo i8=new Insumo("Hilo",100*20, p1,sede2);
    Bolsa i9=new Bolsa("Bolsa",1*20, p1,sedeP,8);
    Bolsa i10=new Bolsa("Bolsa",1*20, p1,sede2,8);
    Bolsa i11=new Bolsa("Bolsa",1*20, p1,sedeP,3);
    Bolsa i12=new Bolsa("Bolsa",1*20, p1,sede2,3);
    Bolsa i13=new Bolsa("Bolsa",1*20, p1,sedeP,1);
    Bolsa i14=new Bolsa("Bolsa",1*20, p1,sede2,1);

    Empleado betty=new Empleado(Area.DIRECCION,new Fecha(1,1,23),sedeP,"Beatriz Pinzón",4269292,Rol.PRESIDENTE,10,Membresia.NULA,Computador);
    Empleado Armando=new Empleado(Area.DIRECCION,new Fecha(30,11,20),sedeP,"Armando Mendoza",19121311,Rol.PRESIDENTE,15,Membresia.PLATA,Computador.copiar());
    Empleado Cata=new Empleado(Area.OFICINA,new Fecha(1,6,16),sedeP,"Catalina Ángel",7296957,Rol.ASISTENTE,20,Membresia.ORO,Impresora);
    Empleado Mario=(new Empleado(Area.OFICINA,new Fecha(30,11,20),sedeP,"Mario Calderón",19256002,Rol.EJECUTIVO,4,Membresia.PLATA,Impresora.copiar()));
    Empleado Hugo=(new Empleado(Area.CORTE,new Fecha(1,5,14),sedeP,"Hugo Lombardi",7980705,Rol.DISEÑADOR,20,Membresia.ORO,MaquinaDeCorte));
    Empleado Inez=(new Empleado(Area.CORTE,new Fecha(1,5,14),sedeP,"Inez Ramirez",23103023,Rol.MODISTA,2,Membresia.NULA,MaquinaDeCoser));
    Empleado Aura=(new Empleado(Area.VENTAS,new Fecha(1,2,23),sedeP,"Aura Maria",4146118,Rol.SECRETARIA,2,Membresia.NULA,Registradora));
    Empleado Sandra=(new Empleado(Area.CORTE,new Fecha(15,9,23),sedeP,"Sandra Patiño",5941859,Rol.SECRETARIA,5,Membresia.NULA,PlanchaIndustrial));
    Empleado Sofia=(new Empleado(Area.CORTE,new Fecha(30,9,22),sedeP,"Sofía Lopez",5079239,Rol.SECRETARIA,6,Membresia.NULA,MaquinaDeTermofijado));
    Empleado Mariana=(new Empleado(Area.CORTE,new Fecha(1,5,23),sedeP,"Mariana Valdéz",4051807,Rol.SECRETARIA,10,Membresia.BRONCE,MaquinaDeTijereado));
    Empleado Bertha=(new Empleado(Area.CORTE,new Fecha(25,2,20),sedeP,"Bertha Muñoz",7137741,Rol.SECRETARIA,15,Membresia.BRONCE,BordadoraIndustrial));
    Empleado Wilson=(new Empleado(Area.VENTAS,new Fecha(4,4,22),sedeP,"Wilson Sastoque",9634927,Rol.PLANTA,5,Membresia.NULA,Registradora.copiar()));

    Empleado Gutierrez=(new Empleado(Area.DIRECCION,new Fecha(5,8,19),sede2,"Saul Gutierrez",9557933,Rol.EJECUTIVO,11,Membresia.NULA,Computador.copiar()));
    Empleado Marcela=(new Empleado(Area.DIRECCION,new Fecha(30,11,20),sede2,"Marcela Valencia",8519803,Rol.EJECUTIVO,10,Membresia.ORO,Computador.copiar()));
    Empleado Gabriela=new Empleado(Area.VENTAS,new Fecha(1,1,24),sede2,"Gabriela Garza",5287925,Rol.VENDEDOR,9,Membresia.PLATA,Registradora.copiar());
    Empleado Patricia=(new Empleado(Area.OFICINA,new Fecha(5,2,23),sede2,"Patricia Fernandez",4595311,Rol.SECRETARIA,6,Membresia.BRONCE,Impresora.copiar()));
    Empleado Kenneth=(new Empleado(Area.CORTE,new Fecha(1,1,24),sede2,"Kenneth Johnson",7494184,Rol.VENDEDOR,8,Membresia.ORO,PlanchaIndustrial.copiar()));
    Empleado Robles=(new Empleado(Area.OFICINA,new Fecha(12,10,24),sede2,"Miguel Robles",7518004,Rol.VENDEDOR,7,Membresia.BRONCE,Impresora.copiar()));
    Empleado Alejandra=(new Empleado(Area.CORTE,new Fecha(1,2,24),sede2,"Alejandra Zingg",6840296,Rol.VENDEDOR,2,Membresia.BRONCE,BordadoraIndustrial.copiar()));
    Empleado Cecilia=(new Empleado(Area.CORTE,new Fecha(1,2,23),sede2,"Cecilia Bolocco",7443886,Rol.ASISTENTE,10,Membresia.PLATA,MaquinaDeCoser.copiar()));
    Empleado Freddy=(new Empleado(Area.VENTAS,new Fecha(31,1,22),sede2,"Freddy Contreras",6740561,Rol.PLANTA,5,Membresia.NULA,Registradora.copiar()));
    Empleado Adriana=(new Empleado(Area.CORTE,new Fecha(18,6,25),sede2,"Adriana arboleda",5927947,Rol.MODISTA,20,Membresia.ORO,MaquinaDeCorte.copiar()));
    Empleado Karina=(new Empleado(Area.CORTE,new Fecha(9,3,25),sede2,"Karina Larson",5229381,Rol.MODISTA,2,Membresia.PLATA,MaquinaDeTermofijado.copiar()));
    Empleado Jenny=(new Empleado(Area.CORTE,new Fecha(1,8,24),sede2,"Jenny Garcia",4264643,Rol.MODISTA,1,Membresia.ORO,MaquinaDeTijereado.copiar()));
    Empleado ol=new Empleado(Area.DIRECCION,new Fecha(1,2,20),sede2,"Gustavo Olarte",7470922,Rol.EJECUTIVO,3,Membresia.NULA,Computador.copiar());
    ol.setTraslados(3);
    ArrayList<Area> a=new ArrayList<Area>();
    a.add(Area.VENTAS);a.add(Area.OFICINA);ol.setAreas(a);
    sede2.getlistaEmpleados().add(ol);
    new Evaluacionfinanciera(-200_000,ol);new Evaluacionfinanciera(-120_000,ol);new Evaluacionfinanciera(-50_000,ol);new Evaluacionfinanciera(1_000,ol);new Evaluacionfinanciera(-70_000,ol);
    new Evaluacionfinanciera(50_000_000,betty);new Evaluacionfinanciera(1_000_000,betty);
    new Evaluacionfinanciera(500_000,Armando);new Evaluacionfinanciera(-10_000,Armando);

    Persona c1=new Persona("Claudia Elena Vásquez",5162307,Rol.MODISTA,2,false,Membresia.BRONCE);
    Persona c2=new Persona("Michel Doniel",9458074,Rol.ASISTENTE,4,false,Membresia.BRONCE);
    Persona c3=new Persona("Claudia Bosch",5975399,Rol.MODISTA,4,false,Membresia.ORO);
    Persona c4=new Persona("Mónica Agudelo",8748155,Rol.MODISTA,8,false,Membresia.ORO);  
    Persona c5=new Persona("Daniel Valencia",9818534,Rol.EJECUTIVO,10,false,Membresia.BRONCE);
    Persona c6=new Persona("Efraín Rodriguez",8256519,Rol.VENDEDOR,10,false,Membresia.NULA);
    Persona c7=new Persona("Mauricio Brightman",8823954,Rol.ASISTENTE,10,false,Membresia.ORO);
    Persona c8=new Persona("Nicolás Mora",4365567,Rol.EJECUTIVO,2,false,Membresia.NULA);
    Persona c9=new Persona("Roberto Mendoza",28096740,Rol.PRESIDENTE,2,false,Membresia.ORO);
    Persona c10=new Persona("Hermes Pinzón",21077781,Rol.ASISTENTE,2,false,Membresia.NULA);
    Persona c11=new Persona("Julia Solano",28943158,Rol.SECRETARIA,10,false,Membresia.BRONCE);
    Persona c12=new Persona("Maria Beatriz Valencia",6472799,Rol.ASISTENTE,2,false,Membresia.BRONCE);
    Persona c13=new Persona("Antonio Sanchéz",8922998,Rol.VENDEDOR,12,false,Membresia.NULA);

    Prenda r1=new Pantalon(new Fecha(1,1,23),Hugo,false,true,sedeP);
    Prenda r2=new Pantalon(new Fecha(1,1,23),Inez,false,true,sedeP);
    Prenda r3=new Pantalon(new Fecha(1,1,23),Sandra,false,true,sedeP);
    Prenda r4=new Pantalon(new Fecha(1,1,23),Sofia,false,true,sedeP);
    Prenda r5=new Pantalon(new Fecha(1,1,23),Mariana,false,true,sedeP);
    Prenda r6=new Pantalon(new Fecha(1,1,23),Bertha,false,true,sedeP);
    Prenda r7=new Camisa(new Fecha(1,1,23),Hugo,false,true,sedeP);
    Prenda r8=new Camisa(new Fecha(1,1,23),Inez,false,true,sedeP);
    Prenda r9=new Camisa(new Fecha(1,1,23),Sandra,false,true,sedeP);
    Prenda r10=new Camisa(new Fecha(1,1,23),Sofia,false,true,sedeP);
    Prenda r11=new Camisa(new Fecha(1,1,23),Mariana,false,true,sedeP);
    Prenda r12=new Camisa(new Fecha(1,1,23),Bertha,false,true,sedeP);
    Prenda r13=new Pantalon(new Fecha(1,1,23),Jenny,false,true,sede2);
    Prenda r14=new Pantalon(new Fecha(1,1,23),Karina,true,true,sede2);
    Prenda r15=new Pantalon(new Fecha(1,1,23),Adriana,false,true,sede2);
    Prenda r16=new Pantalon(new Fecha(1,1,23),Cecilia,false,true,sede2);
    Prenda r17=new Pantalon(new Fecha(1,1,23),Alejandra,false,true,sede2);
    Prenda r18=new Pantalon(new Fecha(1,1,23),Kenneth,false,true,sede2);
    Prenda r19=new Camisa(new Fecha(1,1,23),Jenny,false,true,sede2);
    Prenda r20=new Camisa(new Fecha(1,1,23),Karina,true,true,sede2);
    Prenda r21=new Camisa(new Fecha(1,1,23),Adriana,false,true,sede2);
    Prenda r22=new Camisa(new Fecha(1,1,23),Cecilia,false,true,sede2);
    Prenda r23=new Camisa(new Fecha(1,1,23),Alejandra,false,true,sede2);
    Prenda r24=new Camisa(new Fecha(1,1,23),Kenneth,false,true,sede2);
    Karina.setPericia(0.1F);

}


    static int nextIntSeguro(Scanner in){
        while (!in.hasNextInt()){
            System.out.println("Por favor, ingrese un número entero.");
            in.next();
        }
        return in.nextInt();
    }
    //Interacción 1 de Facturación
	public static Venta Vender(Scanner scanner) {
	    ArrayList<Prenda> productosSeleccionados = new ArrayList<>();
	    ArrayList<Integer> cantidadProductos = new ArrayList<>();

      System.out.println("Ingrese la fecha de la venta:");
      Fecha fechaVenta = ingresarFecha(scanner); 

      System.out.println("Seleccione el cliente al que se le realizará la venta:");
      Persona.imprimirNoEmpleados(); // Muestra la lista de clientes con índices
      int clienteSeleccionado = scanner.nextInt();
      scanner.nextLine(); 
      ArrayList<Persona> noEmpleados = new ArrayList<>();
      for (Persona persona : Persona.getListaPersonas()) { // Recorre listaPersonas para filtrar a los que no son empleados
          if (!(persona instanceof Empleado)) {
              noEmpleados.add(persona);
          }
      }
      
      Persona cliente = noEmpleados.get(clienteSeleccionado); 
      

	    System.out.println("Seleccione el número de la sede en la que se encuentra el cliente:");
	    for (int i = 0; i < Sede.getlistaSedes().size(); i++) {
	        System.out.println(i + ". " + Sede.getlistaSedes().get(i).getNombre());
	    }
	    int sedeSeleccionada = scanner.nextInt();
      scanner.nextLine();
      Sede sede = Sede.getlistaSedes().get(sedeSeleccionada);
      
      System.out.println("Seleccione el número del empleado ue se hará cargo del registro de la venta:");
      for(int i = 0; i < sede.getlistaEmpleados().size(); i++) {
          Empleado empleado = sede.getlistaEmpleados().get(i);
          if (empleado.getAreaActual().equals("Ventas")) {
              System.out.println(i + ". " + empleado.getNombre());
        }
      }
      int vendedorSeleccionado = scanner.nextInt();
      scanner.nextLine();
      Empleado vendedor = sede.getlistaEmpleados().get(vendedorSeleccionado);
      
      System.out.println("Seleccione el número del empleado que se hará cargo de asesorar la venta:");
      for(int i = 0; i < sede.getlistaEmpleados().size(); i++) {
          Empleado empleado = sede.getlistaEmpleados().get(i);
          if (empleado.getAreaActual().equals("Oficina")) {
              System.out.println(i + ". " + empleado.getNombre());
      
            }
       }
      int encargadoSeleccionado = scanner.nextInt();
      scanner.nextLine();
      Empleado encargado = sede.getlistaEmpleados().get(encargadoSeleccionado);
     
      System.out.println("Seleccione el número del producto que venderá:");
      int costosEnvio = 0;
      while(true) {	
      for(int i = 0; i < Sede.getPrendasInventadasTotales().size(); i++) {
          Prenda producto = Sede.getPrendasInventadasTotales().get(i);
              System.out.println(i + ". " + producto.getNombre() + " -Precio " + producto.getPrecio());
              }
      int productoSeleccionado = scanner.nextInt();
      scanner.nextLine();
      Prenda prendaSeleccionada = Sede.getPrendasInventadasTotales().get(productoSeleccionado);
      productosSeleccionados.add(prendaSeleccionada); 
      System.out.println("Ingrese la cantidad de unidades que se desea del producto elegido:");
      int cantidadPrenda = scanner.nextInt();
      scanner.nextLine();
      cantidadProductos.add(cantidadPrenda);
      int camisas = 0;
      int pantalones = 0;
      for(Prenda prenda: sede.getPrendasInventadas()) {//A mejorar
      	if(prenda instanceof Camisa) {
      		camisas++;
      		int camisasfaltantes = 0;
              if(cantidadPrenda > camisas ) {
              	int camisasFaltantes = cantidadPrenda-camisas;
                  costosEnvio += camisasFaltantes*1000;
              	for(int i = 0; i < Sede.getlistaSedes().size(); i++) {
              	   Sede otraSede = Sede.getlistaSedes().get(i);
              	   for(Prenda camisasfal: otraSede.getPrendasInventadas()) {
              		   if(camisasfal instanceof Camisa) {
              			   camisasfaltantes++;
              			   if(camisasfaltantes == camisasFaltantes) {
              			   sede.getPrendasInventadas().add(camisasfal);}
              			   }
              		   }
              	   }
                  }
              else if(prenda instanceof Pantalon) {//A mejorar
      		pantalones++;
      		int pantalonesfaltantes = 0;
      		 if(cantidadPrenda > pantalones) {
      	           int pantalonesFaltantes = cantidadPrenda-pantalones;
                     costosEnvio += pantalonesFaltantes*1000;
      	           for(int i = 0; i < Sede.getlistaSedes().size(); i++) {
      	        	   Sede otraSede = Sede.getlistaSedes().get(i);
      	        	   for(Prenda pantalonesfal: otraSede.getPrendasInventadas()) {
      	        		   if(pantalonesfal instanceof Camisa) {
      	        			   pantalonesfaltantes++;
      	        			   if(pantalonesfaltantes == pantalonesFaltantes) {
      	        				  sede.getPrendasInventadas().add(pantalonesfal);}
      	        			   }
      	        	        } 
      	                  }
      	                }
                     	}
                   }
      	}
          costosEnvio += 3000;
      scanner.nextLine();
      System.out.println("¿Deseas agregar otro producto a la venta?: (si/no)");
      String desicion = scanner.next().toLowerCase();;
      if(desicion.equals("no")) {
      	System.out.println("Selección finalizada");
      	break;
      }
      if(!desicion.equals("si")) {
      	break;
      }        
    }
    int subtotal = 0;
    int cantidadCamisas = 0;
    int cantidadPantalon = 0;
    for(int i = 0; i < productosSeleccionados.size(); i++) {//A mejorar
        Prenda index = productosSeleccionados.get(i);
        if(index instanceof Camisa) {
            cantidadCamisas++;
            int calculoCamisas = (int) (cantidadCamisas * index.getPrecio());
            if (cantidadCamisas >= 10) {int descuento = (int)(calculoCamisas * 0.05f);
            subtotal += calculoCamisas-descuento;}
            else if(cantidadCamisas < 10) {subtotal += calculoCamisas;}
            }
        else if(index instanceof Pantalon){//A mejorar
            cantidadPantalon++;
            int calculoPantalones = (int)(cantidadPantalon * index.getPrecio());
            if (cantidadPantalon >= 10) {int descuento = (int)(cantidadPantalon * 0.05f);
            subtotal += calculoPantalones-descuento;}
            else if(cantidadPantalon < 10) {subtotal += calculoPantalones;}}
        }
     int IVA = (int)(subtotal*0.19f);
  
     Venta venta = new Venta(sede, fechaVenta, cliente, encargado, vendedor, productosSeleccionados, cantidadProductos);
     venta.setCostoEnvio(costosEnvio);
     int monto = subtotal+IVA;
     int subTotal = (int) (monto - (monto * cliente.getMembresia().getPorcentajeDescuento()));
     venta.setSubtotal(subTotal);
     venta.setCantidades(cantidadProductos);
     int comisión = (int)(subTotal * 0.05f);
     encargado.setRendimientoBonificacion(comisión);
     
     return venta;}
    

    }


