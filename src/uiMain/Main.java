package uiMain;

import gestorAplicacion.Persona;
import gestorAplicacion.Sede;
import gestorAplicacion.Fecha;
import gestorAplicacion.Venta;
import gestorAplicacion.Administracion.Area;
import gestorAplicacion.Administracion.Banco;
import gestorAplicacion.Administracion.Deuda;
import gestorAplicacion.Administracion.Empleado;
import gestorAplicacion.Administracion.Evaluacionfinanciera;
import gestorAplicacion.Administracion.Rol;
import gestorAplicacion.Administracion.Resultado;
import gestorAplicacion.Bodega.Prenda;
import gestorAplicacion.Bodega.Insumo;
import gestorAplicacion.Bodega.Maquinaria;
import gestorAplicacion.Bodega.Proveedor;
import gestorAplicacion.Bodega.Repuesto;

import java.util.ArrayList;
import java.util.Scanner;

import baseDatos.Deserializador;
import baseDatos.Serializador;


public class Main {
    public static void main (String[] args){
        Deserializador.deserializar();
        new Persona("Julia", 0, null, 0, false, null);
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
            ArrayList<Empleado> despedidos = despedirEmpleados(in);
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
    static public ArrayList<Empleado> despedirEmpleados(Scanner scanner) {
        System.out.println("Obteniendo lista sugerida de empleados");
        ArrayList<Empleado> aDespedir = Empleado.listaInicialDespedirEmpleado();
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
    Repuesto PiePrensaTela = new Repuesto("Pie Prensa Tela", 1000);
    Repuesto CorreDeTransmision = new Repuesto("Correa de Transmision", 3000);

    Repuesto Cuchillas = new Repuesto("Cuchillas", 60);
    Repuesto Afiladores = new Repuesto("Afiladores", 750);
    Repuesto Rodamientos = new Repuesto("Rodamientos", 1500);

    Repuesto ResistenciaElectrica = new Repuesto("Resistencia Electrica", 1500);
    Repuesto MangueraDeVapor = new Repuesto("Manguera de Vapor", 750);

    Repuesto AgujasBI = new Repuesto("Agujas de la Bordadora Industrial", 25);

    Repuesto BandasDeTransmision = new Repuesto("Bandas de Transmision", 2500);

        //AGRUPACION DE LOS REPUESTOS EN LISTAS PARA ENVIARLOS A LAS MAQUINAS CORRESPONDIENTES
    ArrayList<Repuesto> repuestosMC = new ArrayList<>();
    ArrayList<Repuesto> repuestosMCorte = new ArrayList<>();
    ArrayList<Repuesto> repuestosPI = new ArrayList<>();
    ArrayList<Repuesto> repuestosBI = new ArrayList<>();
    ArrayList<Repuesto> repuestosMTermofijado = new ArrayList<>();
    ArrayList<Repuesto> repuestosMTijereado = new ArrayList<>();

    repuestosMC.add(AgujasMC);
    repuestosMC.add(Aceite);
    repuestosMC.add(PiePrensaTela);
    repuestosMC.add(CorreDeTransmision);

    repuestosMCorte.add(Cuchillas);
    repuestosMCorte.add(Afiladores);
    repuestosMCorte.add(Rodamientos);

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

        //AGRUPACION DE LAS MAQUINAS EN LISTAS PARA ENVIARLAS A LAS SEDES CORRESPONDIENTES
    ArrayList<Maquinaria> maqSedeP = new ArrayList<>();
    ArrayList<Maquinaria> maqSede2 = new ArrayList<>();
    ArrayList<Maquinaria> maqSede3 = new ArrayList<>();
    ArrayList<Maquinaria> maqSede4 = new ArrayList<>();
    ArrayList<Maquinaria> maqSede5 = new ArrayList<>();
    ArrayList<Maquinaria> maqSede6 = new ArrayList<>();

    maqSedeP.add(MaquinaDeCoser);
    maqSedeP.add(MaquinaDeCorte);
    maqSedeP.add(PlanchaIndustrial);
    maqSedeP.add(BordadoraIndustrial);
    maqSedeP.add(MaquinaDeTermofijado);
    maqSedeP.add(MaquinaDeTijereado);

    maqSede2.add(MaquinaDeCoser.copiar());
    maqSede2.add(MaquinaDeCorte.copiar());
    maqSede2.add(PlanchaIndustrial.copiar());
    maqSede2.add(BordadoraIndustrial.copiar());
    maqSede2.add(MaquinaDeTermofijado.copiar());
    maqSede2.add(MaquinaDeTijereado.copiar());

    maqSede3.add(MaquinaDeCoser.copiar());
    maqSede3.add(MaquinaDeCorte.copiar());
    maqSede3.add(PlanchaIndustrial.copiar());
    maqSede3.add(BordadoraIndustrial.copiar());
    maqSede3.add(MaquinaDeTermofijado.copiar());
    maqSede3.add(MaquinaDeTijereado.copiar());

    maqSede4.add(MaquinaDeCoser.copiar());
    maqSede4.add(MaquinaDeCorte.copiar());
    maqSede4.add(PlanchaIndustrial.copiar());
    maqSede4.add(BordadoraIndustrial.copiar());
    maqSede4.add(MaquinaDeTermofijado.copiar());
    maqSede4.add(MaquinaDeTijereado.copiar());

    maqSede5.add(MaquinaDeCoser.copiar());
    maqSede5.add(MaquinaDeCorte.copiar());
    maqSede5.add(PlanchaIndustrial.copiar());
    maqSede5.add(MaquinaDeTijereado.copiar());

    maqSede6.add(MaquinaDeCoser.copiar());
    maqSede6.add(MaquinaDeCorte.copiar());
    maqSede6.add(PlanchaIndustrial.copiar());
    maqSede6.add(MaquinaDeTijereado.copiar());
    
        //CREACION DE LAS SEDES QUE MANEJAREMOS, CON SUS RESPECTIVAS MAQUINAS EN CADA UNA DE ELLAS
    Sede sedeP = new Sede("Sede Principal", maqSedeP);
    Sede sede2 = new Sede("Sede 2", maqSede2);
    Sede sede3 = new Sede("Sede 3", maqSede3);
    Sede sede4 = new Sede("Sede 4", maqSede4);
    Sede sede5 = new Sede("Sede 5", maqSede5);
    Sede sede6 = new Sede("Sede 6", maqSede6);
    
}

}


