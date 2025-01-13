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
import gestorAplicacion.Bodega.Prenda;

import java.util.ArrayList;
import java.util.Scanner;

import baseDatos.Deserializador;
import baseDatos.Serializador;

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
            ArrayList<Object> retorno = GestionInsumos.planificarProduccion(fecha);
            ArrayList<Object> listaA = GestionInsumos.coordinarBodegas(retorno);
            ArrayList<Deuda> deuda = GestionInsumos.comprarInsumos(fecha, listaA);
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

        Empleado.despedirEmpleados(seleccion);
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
}
