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
import gestorAplicacion.Bodega.Camisa;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import baseDatos.Deserializador;
import baseDatos.Serializador;
import java.util.Collections;
import java.util.Locale;


public class Main {
    static Collator comparador = Collator.getInstance(new Locale("es"));
    static {
        comparador.setStrength(Collator.PRIMARY);
    }
    static Scanner in = new Scanner(System.in);
    static{
        Main.fecha = ingresarFecha();
    }
    Sede sedeP, sede2;
    private static Proveedor proveedorBdelmain;
    public static Fecha fecha;
    public static void main(String[] args) {
        System.out.println("Ecomoda a la orden, ¿Quieres volver a cargar tus datos?");
        String respuesta = in.next();
        if (!respuesta.equals("no")) {
            Deserializador.deserializar();
        } else {
            System.out.println("¿Crear datos por defecto?");
            respuesta = in.next();
            if (respuesta.equals("si")) {
                new Main().crearSedesMaquinasRepuestos();
            }
        }
        buclePrincipal: while (true) {
            Main.actualizarProveedores();
            System.out.println("\n"+"¿Que operación desea realizar?");
            System.out.println("1.Reemplazar empleados");
            System.out.println("2.Adquirir insumos para produccion");
            System.out.println("3.Ver el desglose economico de la empresa");
            System.out.println("4.Vender un producto");
            System.out.println("5.Producir prendas");
            System.out.println("6.Salir");
            System.out.println("7. Inspeccionar memoria");

            int opcion = nextIntSeguro(in);
            switch (opcion) {
                case 1:
                    ArrayList<Empleado> despedidos = despedirEmpleados(in, Main.fecha);
                    ArrayList<Empleado> aContratar = reorganizarEmpleados(in, despedidos);
                    contratarEmpleados(aContratar, in, fecha);
                    break;

                case 2:

                    ArrayList<Object> retorno = planificarProduccion(Main.fecha);
                    ArrayList<Object> listaA = coordinarBodegas(retorno);
                    System.out.println(comprarInsumos(Main.fecha, listaA));

                    break;

                case 3:
                    Evaluacionfinanciera balanceAnterior = calcularBalanceAnterior(Main.fecha, in);
                    long diferenciaEstimada = calcularEstimado(Main.fecha, balanceAnterior, in);
                    String analisisFuturo = planRecuperacion(diferenciaEstimada, Main.fecha, in);

                    String retorna = "\nSegún la evaluación del estado Financiero actual: " + "\n"+balanceAnterior.Informe() +
                            "\n\nSe realizó un análisis sobre la posibilidad de aplicar descuentos. \n"+ analisisFuturo +
                            "\n\nEste resultado se usó para estimar la diferencia entre ventas y deudas futuras, \nque fue de: $"
                            + diferenciaEstimada +
                            " y por tanto el nuevo porcentaje de pesimismo de la producción es:" + Venta.getPesimismo()
                            + ".";
                    System.out.println(retorna);
                    break;

                case 4:
                    Venta venta = vender(in);
                    Main.realizarVenta(in, venta);
                    Main.tarjetaRegalo(in, venta);
                    Sede sede = venta.getSede();
                    sede.getHistorialVentas().add(venta);
                    break;

                case 5:
                    Maquinaria maquina = new Maquinaria();
                    Sede sedePrueba = new Sede(1);
                    
                    ArrayList<ArrayList<ArrayList<Integer>>> plan = sedePrueba.planProduccion(maquina.agruparMaquinasDisponibles(fecha), fecha, in);
                    Prenda.producirPrendas(plan,Main.fecha);
                    break;

                case 6:
                    Serializador.serializar();
                    System.exit(0);
                    in.close();
                    break buclePrincipal;

                case 7:
                    Desarrollo.menu(in);

                default:
                    System.out.println("Esa opción no es valida.");
            }

        }
        in.close();
    }

    // Metodo ingresarFecha
    static public Fecha ingresarFecha() {
        Scanner scanner = new Scanner(System.in);
        int dia = -1;
        int mes = -1;
        while (dia <= 0 || dia > 31) {
            System.out.println("Ingrese día");
            dia = scanner.nextInt();
        }
        while (mes <= 0 || mes > 12) {
            System.out.println("Ingrese mes");
            mes = scanner.nextInt();
        }
        System.out.println("Ingrese año");
        int año = scanner.nextInt();
        Fecha fecha = new Fecha(dia, mes, año);
        return fecha;
    }

    // Interaccion 1 de Gestion Humana
    // https://docs.google.com/document/d/1IomqwzQR1ZRXw9dFlHx5mA_2oOowyIbxauZeJ6Rqy6Q/edit?tab=t.0#heading=h.z9eys2stm4gz
    static public ArrayList<Empleado> despedirEmpleados(Scanner scanner, Fecha fecha) {
        System.out.println("Obteniendo lista sugerida de empleados");
        ArrayList<Object> infoDespidos = Empleado.listaInicialDespedirEmpleado(fecha);
        ArrayList<Empleado> aDespedir = (ArrayList<Empleado>) infoDespidos.get(0);
        ArrayList<String> mensajes = (ArrayList<String>) infoDespidos.get(1); // Canal para imprimir cosas mas que todo
                                                                              // para debuggear.

        for (String mensaje : mensajes) {
            System.out.println(mensaje);
        }


        System.out.println("Esta es una lista de empleados que no estan rindiendo correctamente, ¿que deseas hacer?");
        int diferenciaSalarios = -Persona.diferenciaSalarios();
        if (diferenciaSalarios>0){
            System.out.println("Tus empleados estan "+String.format("%,d", Persona.diferenciaSalarios())+" sobre el promedio de salarios");
        } else if (diferenciaSalarios<0){
            System.out.println("Tus empleados estan "+String.format("%,d", Persona.diferenciaSalarios())+" bajo el promedio de salarios");
        } else {
            System.out.println("Tus empleados estan en el promedio de salarios");
        }
        for (Empleado emp : aDespedir) {
            System.out.println(emp.getNombre() + " " + emp.getAreaActual() + " " + emp.getDocumento());
        }
        int opcion = 2;
        while (opcion == 2) {
            System.out.println("1. Elegir a los despedidos");
            System.out.println("2. Añadir a alguien mas");
            opcion = nextIntSeguro(scanner);
            if (opcion == 2) {
                System.out.println("¿De que sede quieres añadir al empleado?"); // Para no imprimir una lista demasiado
                                                                                // larga
                for (int i = 0; i < Sede.getlistaSedes().size(); i++) {
                    System.out.println(i + ". " + Sede.getlistaSedes().get(i).getNombre());
                }
                int sede = nextIntSeguro(scanner);
                System.out.println("¿Que empleado quieres despedir? Pon su nombre completo o documento, esto lo añañdirá a la lista de despedibles.");
                for (Empleado emp : Sede.getlistaSedes().get(sede).getlistaEmpleados()) {
                    System.out.println(emp.getNombre() + " " + emp.getAreaActual() + " " + emp.getDocumento());
                }
                String nombre = scanner.nextLine().trim();
                for (Empleado emp : Sede.getlistaSedes().get(sede).getlistaEmpleados()) {
                    if (comparador.compare(emp.getNombre(), nombre)==0 || (nombre.matches("\\d+") && emp.getDocumento() == Integer.parseInt(nombre))) {
                        aDespedir.add(emp);
                    }
                }
            }
        }

        // Ya tenemos la lista definitiva de despedibles, incluidos los que el usuario
        // quiera.
        ArrayList<Empleado> seleccion = new ArrayList<Empleado>();
        System.out.println("¿Que empleados quieres despedir? Pon su nombre completo, documento o FIN para terminar.");
        for (Empleado emp : aDespedir) {
            System.out.println(emp.getNombre() + " " + emp.getAreaActual() + " " + emp.getDocumento());
        }
        String nombre = scanner.nextLine().trim();
        while (!nombre.equalsIgnoreCase("fin")) {
            for (Empleado emp : aDespedir) {
                if (comparador.compare(emp.getNombre(), nombre)==0 || (nombre.matches("\\d+") && emp.getDocumento() == Integer.parseInt(nombre))) {
                    seleccion.add(emp);
                }
            }
            nombre = scanner.nextLine().trim();
        }

        // Ya tenemos la lista de empleados a despedir.

        System.out.println("Despidiendo empleados...");
        Empleado.despedirEmpleados(seleccion, true, fecha);
        System.out.println("Listo!");
        return seleccion;
    }

    // Interaccion 2 de Gestion Humana
    // https://docs.google.com/document/d/1IomqwzQR1ZRXw9dFlHx5mA_2oOowyIbxauZeJ6Rqy6Q/edit?tab=t.0#heading=h.iadm7mr7n689

    static public ArrayList<Empleado> reorganizarEmpleados(Scanner in, ArrayList<Empleado> despedidos) {
        System.out.println("Todavía nos quedan " + despedidos.size() + " empleados por reemplazar, revisamos la posibilidad de transferir empleados.");
        ArrayList<Object> necesidades = Sede.obtenerNececidadTransferenciaEmpleados(despedidos);
        // Desempacamos los datos dados por GestorAplicacion
        ArrayList<Rol> rolesATransferir = (ArrayList<Rol>) necesidades.get(0);
        ArrayList<Sede> transferirDe = (ArrayList<Sede>) necesidades.get(1);
        ArrayList<Empleado> aContratar = (ArrayList<Empleado>) necesidades.get(2);

        // Lista de empleados a tranferir de sede, seleccionados por el usuario.
        ArrayList<Empleado> aTransferir = new ArrayList<Empleado>();

        for (int rolidx = 0; rolidx < rolesATransferir.size(); rolidx++) {
            Rol rol = rolesATransferir.get(rolidx);
            Sede sede = transferirDe.get(rolidx);
            System.out.println("Se necesita transferir " + rol + " de " + sede.getNombre()
                    + ", estos son los candidatos: Ingresa su nombre completo para hacerlo.");
            for (Empleado emp : sede.getlistaEmpleados()) {
                if (emp.getRol().equals(rol)) {
                    String descripcion = emp.getNombre() + " Documento:" + emp.getDocumento();
                    switch (emp.getRol()) {
                        case VENDEDOR:
                            descripcion += " Ventas asesoradas: " + Venta.acumuladoVentasAsesoradas(emp);
                            break;
                        case MODISTA:
                            descripcion += " Pericia: " + emp.getPericia();
                            break;
                        default:
                            descripcion += " contratado en " + emp.getFechaContratacion();
                    }
                    System.out.println(descripcion);
                }
            }

            // Obtenemos la cantidad de empleados a seleccionar
            int cantidad = 0;
            for (Empleado emp : despedidos) {
                if (emp.getRol().equals(rol)) {
                    cantidad++;
                }
            }
            for (int i = 0; i < cantidad; i++) {
                String nombre = in.nextLine().trim();
                for (Empleado emp : sede.getlistaEmpleados()) {
                    if (comparador.compare(emp.getNombre(), nombre) == 0) {
                        aTransferir.add(emp);
                    }
                }
            }

        }

        Sede.reemplazarPorCambioSede(despedidos, aTransferir);

        return aContratar;
    }

    // Interaccion 3 gestion Humana
    // https://docs.google.com/document/d/1IomqwzQR1ZRXw9dFlHx5mA_2oOowyIbxauZeJ6Rqy6Q/edit?tab=t.0#heading=h.zgpssvyj8l65
    static public void contratarEmpleados(ArrayList<Empleado> aReemplazar, Scanner in, Fecha fecha) {
        ArrayList<Object> elecciones = Persona.entrevistar(aReemplazar);
        ArrayList<Persona> aptos = (ArrayList<Persona>) elecciones.get(0);
        ArrayList<Rol> rolesAReemplazar = (ArrayList<Rol>) elecciones.get(1);
        ArrayList<Integer> cantidad = (ArrayList<Integer>) elecciones.get(2);

        ArrayList<Persona> aContratar = new ArrayList<Persona>();
        for (int i = 0; i < rolesAReemplazar.size(); i++) {
            Rol rol = rolesAReemplazar.get(i);
            int cantidadNecesaria = cantidad.get(i);

            System.out.println("Se nececitan " + cantidadNecesaria + " " + rol + "s, estos son los candidatos:");

            for (Persona persona : aptos) {
                System.out.println("Nombre: " + persona.getNombre() + " Documento: " + persona.getDocumento() + " con "
                        + persona.getExperiencia() + " años de experiencia.");
            }

            System.out.println("Ingresa el nombre de los que quieres contratar.");

            for (int cantidadContratada = 0; cantidadContratada < cantidadNecesaria; cantidadContratada++) {
                String nombre = in.nextLine().trim();
                for (Persona persona : aptos) {
                    if (comparador.compare(persona.getNombre(), nombre)==0) {
                        aContratar.add(persona);
                        System.out.println("Seleccionaste a " + persona.getNombre()+" con "+(persona.calcularSalario()-persona.valorEsperadoSalario())+" de diferencia salarial sobre el promedio");
                    }
                }
            }

        }

        Persona.contratar(aContratar, aReemplazar, fecha);
    }

    // Interacción 1 Sistema Financiero
    public static Evaluacionfinanciera calcularBalanceAnterior(Fecha fecha, Scanner in) {
        System.out.println("\n"+"Obteniendo balance entre Ventas y Deudas para saber si las ventas cubren los gastos de la producción de nuestras prendas...");
        double balanceCostosProduccion = Venta.calcularBalanceVentaProduccion(fecha);
        int eleccion = 0;
        while (eleccion <= 0 || eleccion > 3) {
            System.out.println("\n"+"Ingrese las deudas que quiere calcular");
            System.out.println("Ingrese 1 para proveedor, 2 para Banco o 3 para ambos");
            eleccion = in.nextInt();
        }
        double deudaCalculada = Deuda.calcularDeudaMensual(fecha, eleccion);
        double balanceTotal = balanceCostosProduccion - deudaCalculada;
        Empleado empleado = null;
        ArrayList<Empleado> elegible = new ArrayList<Empleado>();
        for (Empleado emp : Sede.getListaEmpleadosTotal()) {
            if (emp.getAreaActual().equals(Area.DIRECCION)) {
                elegible.add(emp);
            }
        }
        int i = -1;
        while (i < 0 || i >= elegible.size()) {
            for (int ii = 0; ii < elegible.size(); ii++) {
                System.out.println(ii + " " + elegible.get(ii));
            }
            System.out.println("\n"+"Ingrese número de 0 a " + (elegible.size() - 1 + " según el Directivo que escoja para registrar el balance"));
            i = Main.nextIntSeguro(in);
            empleado = elegible.get(i);
        }

        Evaluacionfinanciera nuevoBalance = new Evaluacionfinanciera(balanceTotal, empleado);
        return nuevoBalance;
    }

    // Interaccion 2 Sistema Financiero
    public static long calcularEstimado(Fecha fecha, Evaluacionfinanciera balanceAnterior, Scanner in) {
        System.out.println("\n"+"Calculando estimado entre Ventas y Deudas para ver el estado de endeudamiento de la empresa...");
        float porcentaje = -1F;
        while (porcentaje < 0.0F && porcentaje > 1) {
            System.out.println(
                    "\n"+"Ingrese porcentaje a modificar para fidelidd de oro entre 0.6 y 1, o 0.0 si no desea modificarlo");
            porcentaje = in.nextFloat();
        }
        long diferenciaEstimada = Evaluacionfinanciera.estimadoVentasGastos(fecha, porcentaje, balanceAnterior);
        // Un mes se puede dar por salvado si el 80% de los gastos se pueden ver
        // cubiertos por las ventas predichas
        return diferenciaEstimada;
    }

    // Interaccion 3 Sistema Financiero
    public static String planRecuperacion(long diferenciaEstimada, Fecha fecha, Scanner in) {
        if (diferenciaEstimada > 0) {
            System.out.println("\n"+"El estimado es positivo, las ventas superan las deudas");
            System.out.println("Hay dinero suficiente para hacer el pago de algunas Deudas");
            Deuda.compararDeudas(fecha);
        }
        else if (diferenciaEstimada <= 0) {
            System.out.println("\n"+"El estimado es negativo, la deuda supera las ventas");
            System.out.println("No hay Dinero suficiente para cubrir los gastos de la empresa, tendremos que pedir un préstamo");
            int i = -1;
            String Nombrebanco = null;
            while (i < 0 || i >= Banco.getListaBancos().size()) {
                for (Banco banco:Banco.getListaBancos()){System.out.println(banco);}
                System.out.println("\n"+"Ingrese número de 0 a " + (Banco.getListaBancos().size() - 1 + " para solicitar el prestamo al Banco de su elección"));
                i = in.nextInt();
                Nombrebanco = Banco.getListaBancos().get(i).getNombreEntidad();
            }
            int cuotas = 0;
            while (i <= 0 || i >= 18) {
                System.out.println("Ingrese número de 1 a 18 para las cuotas en que se dividirá la deuda");
                cuotas = in.nextInt();
            }
            Deuda deudaAdquirir = new Deuda(fecha, diferenciaEstimada, Nombrebanco, "Banco", cuotas);
        }
        System.out.println("\n"+"Analizando posibilidad de hacer descuentos para subir las ventas...");
        float descuento = Venta.blackFriday(fecha);
        String BFString = null;
        if (descuento <= 0.0F) {
            BFString = "El análisis de ventas realizado sobre el Black Friday arrojó que la audiencia no reacciona tan bien a los descuentos, \npropusimos no hacer descuentos";
            System.out.println("\n"+"Según las Ventas anteriores,  aplicar descuentos no funcionará");
        } else {
            BFString = "El análisis de ventas realizado sobre el Black Friday arrojó que la audiencia reacciona bien a los descuentos, \npropusimos un descuento del "
                    + descuento * 100 + "%";
            System.out.println("\n"+"Según las Ventas anteriores, aplicar descuentos si funcionará");
        }
        System.out.println("¿Desea Cambiar el siguiente descuento: " + (descuento)*100 + "? marque 1 para Si, 2 para no ");
        int num = in.nextInt();
        float nuevoDescuento = -0.1F;
        if (num == 1) {
            while (nuevoDescuento < 0.0 || descuento > 0.5) {
                System.out.println("Ingrese descuento entre 0% y 5%");
                nuevoDescuento = in.nextInt()/100F;
            }
        }
        Prenda.prevenciones(descuento, nuevoDescuento, fecha);
        String analisisFuturo = "\n"+BFString + ", sin embargo su desición fue aplicar un descuento de: "
                + nuevoDescuento * 100 + "%.";
        return analisisFuturo;
    }

    // Funcionalidad Insumos

    // Interacción 1 de Insumos
    static public ArrayList<Object> planificarProduccion(Fecha fecha) {
        // ArrayList<Insumo> listaGuia = new ArrayList<>();
        ArrayList<Object> retorno = new ArrayList<>();

        for (Sede x : Sede.getlistaSedes()) {
            System.out.println("\n"+"Para la " + x.getNombre());
            System.out.println("Tenemos un porcentaje de pesimismo: " + Math.round(Venta.getPesimismo() * 100) +"%");
            System.out.println("\n"+"Seleccione una de las siguientes opciones:");
            System.out.println("1. Estoy de acuerdo con el porcentaje de pesimismo");
            System.out.println("2. Deseo cambiar el porcentaje de pesimismo");

            Scanner in = new Scanner(System.in);
            int opcion = in.nextInt();
            switch (opcion) {
                case 1:
                    break;
                case 2:
                    System.out.println("Ingrese el nuevo porcentaje de pesimismo % ");
                    Scanner porcentaje = new Scanner(System.in);
                    int newPesimism = porcentaje.nextInt();
                    Venta.setPesimismo(newPesimism / 100f);
                    break;
                default:
                    System.out.println("Esa opción no es valida.");
            }

            ArrayList<Object> listaXSede = new ArrayList<>();
            ArrayList<Insumo> insumoXSede = new ArrayList<>();
            ArrayList<Integer> cantidadAPedir = new ArrayList<>();
            int contador1 = 0;
            int contador2 = 0;
            float prediccionp=0;
            float prediccionc=0;

            for (Prenda prenda : x.getPrendasInventadas()) {
                System.out.println(prenda);
                System.out.println(contador1);
                System.out.println(contador2);
                if (prenda instanceof Pantalon && contador1 == 0) {

                    int proyeccion = Venta.predecirVentas(fecha, x, prenda.getNombre());
                    prediccionp = proyeccion * (1 - Venta.getPesimismo());
                    System.out.println("\n"+ "La predicción de ventas para "+prenda + " es de "+ Math.ceil(prediccionp));
                    
                    for (Insumo insumo : prenda.getInsumo()) {
                        insumoXSede.add(insumo);
                    }
                    for (int i : Pantalon.getCantidadInsumo()) {
                        cantidadAPedir.add((int)(Math.ceil(i * prediccionp)));
                    }
                    contador1++;
                }
                if (prenda instanceof Pantalon && contador1>0){
                    for (int i=0;i<prenda.getInsumo().size();i++) {
                        for (int j=0;j<insumoXSede.size();j++) {
                            if (!prenda.getInsumo().get(i).getNombre().equals(insumoXSede.get(j).getNombre())){
                            insumoXSede.add(prenda.getInsumo().get(i));
                            cantidadAPedir.add((int)(Math.ceil(Pantalon.getCantidadInsumo().get(i) * prediccionp)));}
                            else {
                                cantidadAPedir.add(j,(cantidadAPedir.get(j)+(int)(Math.ceil(Pantalon.getCantidadInsumo().get(i) * prediccionp))));
                            }
                        } 
                    }
                    contador1++;
                    }
                if (prenda instanceof Camisa && contador2 == 0) {
                    int proyeccion = Venta.predecirVentas(fecha, x, prenda.getNombre());
                    prediccionc = proyeccion * (1 - Venta.getPesimismo());

                    System.out.println("\n"+ "La predicción de ventas para "+prenda + " es de "+ Math.ceil(prediccionc));

                    
                    for (int i = 0; i < prenda.getInsumo().size(); i++) {
                        Insumo insumo = prenda.getInsumo().get(i);
                        int cantidad = (int) Math.ceil(Camisa.getCantidadInsumo().get(i) * prediccionc);
                
                        int index = insumoXSede.indexOf(insumo);
                        if (index == -1) {
                            insumoXSede.add(insumo);
                            cantidadAPedir.add(cantidad);
                        } else {
                            cantidadAPedir.set(index, cantidadAPedir.get(index) + cantidad);
                        }
                    }
                    contador2++;
                }
            if (prenda instanceof Camisa && contador2>0){
                for (int i=0;i<prenda.getInsumo().size();i++) {
                    for (int j=0;j<insumoXSede.size();j++) {
                        if (!prenda.getInsumo().get(i).getNombre().equals(insumoXSede.get(j).getNombre())){
                        insumoXSede.add(prenda.getInsumo().get(i));
                        cantidadAPedir.add((int)(Math.ceil(Camisa.getCantidadInsumo().get(i) * prediccionc)));}
                        else {
                        cantidadAPedir.add(j,(cantidadAPedir.get(j)+(int)(Math.ceil(Camisa.getCantidadInsumo().get(i) * prediccionc))));
                        }
                    } 
                }
                contador2++;
            }
        }
            
            listaXSede.add(0,insumoXSede);
            listaXSede.add(1,cantidadAPedir);
            retorno.add(listaXSede);
        }        

        return retorno;
    }

    // Interacción 2 de Insumos
    static public ArrayList<Object> coordinarBodegas(ArrayList<Object> retorno) {
        ArrayList<Object> listaXSede = new ArrayList<>(); // Lista extraida de retorno
        ArrayList<Insumo> listaInsumos = new ArrayList<>(); // Extraida de listaXSede
        ArrayList<Integer> listaCantidades = new ArrayList<>(); // Extraida de listaXSede
        ArrayList<Object> listaA = new ArrayList<Object>();

        for (Object sede : retorno) {
            ArrayList<Insumo> insumosAPedir = new ArrayList<>();
            ArrayList<Integer> cantidadAPedir = new ArrayList<>(); // Ambas calculadas en este bucle
            ArrayList<Object> listaSede = new ArrayList<>(); // Acumula la info de este bucle.
            // Convertir cada elemento en un ArrayList<Object> correspondiente a una sede
            listaXSede = (ArrayList<Object>) sede;

            // Extraer las listas internas: insumos y cantidades
            listaInsumos = (ArrayList<Insumo>) listaXSede.get(0);
            listaCantidades = (ArrayList<Integer>) listaXSede.get(1);

            for (Sede s : Sede.getlistaSedes()) {
                for (Insumo i : listaInsumos) {
                    Resultado productoEnBodega = Sede.verificarProductoBodega(i, s);
                    if (productoEnBodega.getEncontrado() == true) {
                        int restante = Sede.restarInsumo(i, s, listaCantidades.get((int) productoEnBodega.getIndex()));
                        if (restante != 0) {
                            Resultado productoEnOtraSede = Sede.verificarProductoOtraSede(i);
                            if (productoEnOtraSede.getEncontrado() == true) {
                                System.out.println("Tenemos el insumo " + i.getNombre() + " en nuestra "
                                                    + productoEnOtraSede.getSede() + ".");
                                System.out.println("El insumo tiene un costo de " + productoEnOtraSede.getPrecio());
                                System.out.println("\n"+"Seleccione una de las siguientes opciones:");
                                System.out.println("1. Deseo transferir el insumo desde la " + productoEnOtraSede.getSede());
                                System.out.println("2. Deseo comprar el insumo");

                                Scanner in = new Scanner(System.in);
                                int opcion = in.nextInt();
                                switch (opcion) {
                                    case 1:
                                        int restante2 = Sede.restarInsumo(i, s, restante);
                                        System.out.println("\n"+i+" transferido desde "+s+" con éxito");
                                        if (restante2 != 0) {
                                            insumosAPedir.add(i);
                                            cantidadAPedir.add(restante2);
                                            if(i.getNombre().equals("Tela")){
                                            System.out.println("Tenemos una cantidad de "+restante2+"cm de tela restantes a pedir ");}
                                            else if (i.getNombre().equals("Boton")){
                                                System.out.println("Tenemos una cantidad de "+restante2+" botones restantes a pedir ");}
                                            else if (i.getNombre().equals("Cremallera")){
                                                 System.out.println("Tenemos una cantidad de "+restante2+" cremalleras restantes a pedir ");}
                                                else {
                                                System.out.println("Tenemos una cantidad de "+restante2+" cm de hilo restantes a pedir ");}
                                        }
                                        else{
                                            System.out.println("Insumo transferido en su totalidad");
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
            }
            listaSede.add(insumosAPedir);
            listaSede.add(cantidadAPedir);
            listaA.add(listaSede);
        }

        return listaA;
    }

    // Interacción 3 de Insumos
    static public String comprarInsumos(Fecha fecha, ArrayList<Object> listaA) {
        ArrayList<Object> sede = new ArrayList<>();
        ArrayList<Insumo> insumos = new ArrayList<>();
        ArrayList<Integer> cantidad = new ArrayList<>();
        ArrayList<Proveedor> proveedores = new ArrayList<>();
        ArrayList<Integer> precios = new ArrayList<>();
        ArrayList<Deuda> deudas = new ArrayList<>();
        for (Object s : listaA) {
            sede = (ArrayList<Object>) s;
            insumos = (ArrayList<Insumo>) sede.get(0);
            cantidad = (ArrayList<Integer>) sede.get(1);

            for (Sede sedee : Sede.getlistaSedes()) {

                for (int i = 0; i < insumos.size(); i++) {
                    Proveedor mejorProveedor = null;
                    int mejorPrecio = 0;
                    int cantidadAñadir = 0;

                    for (Proveedor proveedor : Proveedor.getListaProveedores()) {

                        if (proveedor.getInsumo().equals(insumos.get(i))) {
                            proveedores.add(proveedor);
                            precios.add(Proveedor.costoDeLaCantidad(insumos.get(i), cantidad.get(i)));
                        }
                    }

                    for (Proveedor x : proveedores) {
                        int precio = x.costoDeLaCantidad(insumos.get(i), cantidad.get(i));
                        if ((precio != 0) && (precio < mejorPrecio)) {
                            mejorPrecio = precio;
                            mejorProveedor = x;
                            insumos.get(i).setProveedor(x);
                        }
                    }

                    System.out.println("\n"+"Tenemos el insumo " + insumos.get(i).getNombre() + " con nuestro proveedor "
                            + insumos.get(i).getProveedor().getNombre() + ".");

                    if (insumos.get(i).getPrecioIndividual() < insumos.get(i).getUltimoPrecio()) {
                        System.out.println("\n"+
                        "Dado que el costo de la venta por unidad es menor al ultimo precio por el que compramos el insumo");
                        System.out.println("\n"+"Desea pedir mas de la cantidad necesaria para la producción? ");
                        System.out.println("Cantidad: " + cantidad.get(i));
                        System.out.println("1. Si");
                        System.out.println("2. No");

                        Scanner in = new Scanner(System.in);
                        int opcion = in.nextInt();
                        switch (opcion) {
                            case 1:
                                if (opcion >= 0) {
                                    System.out.println("\n"+
                                            "Cuanta cantidad más desea pedir del insumo " + insumos.get(i).getNombre());
                                    Scanner cant = new Scanner(System.in);
                                    cantidadAñadir = cant.nextInt();
                                    cant.close();
                                } else {
                                    System.out.println("Esa opción no es valida.");
                                }
                                break;
                            case 2:
                                break;
                            default:
                                System.out.println("Esa opción no es valida.");
                        }
                    }
                    cantidad.set(i, ((cantidad.get(i)) + cantidadAñadir));

                    Sede.añadirInsumo(insumos.get(i), sedee, cantidad.get(i));
                    System.out.println("\n"+"Insumo "+insumos.get(i)+" comprado con éxito");

                    for (Proveedor proveedor : Proveedor.getListaProveedores()) {
                        int montoDeuda = 0;
                        if (insumos.get(i).getProveedor().getNombre().equals(proveedor.getNombre())) {
                            montoDeuda += insumos.get(i).getPrecioIndividual() * cantidad.get(i);
                        }
                        Deuda deuda = null;
                        if (montoDeuda > 0) {
                            if (!(proveedor.getDeuda().getEstadodePago())) {
                                proveedor.unificarDeudasXProveedor(fecha, montoDeuda, proveedor.getNombre());
                                deuda = proveedor.getDeuda();
                            } else {
                                deuda = new Deuda(fecha, montoDeuda, proveedor.getNombre(), "Proveedor",
                                        Deuda.calcularCuotas(montoDeuda));
                            }
                            deudas.add(deuda);
                        }

                    }
                }

            }
        }
        
        return "Ahora nuestras deudas con los proveedores lucen asi:"+"\n"+deudas;
    }

    // METODO PARA CREAR LAS SEDES, LAS MAQUINAS Y LOS REPUESTOS,
    // REQUERIDO PARA LA INTERACCION 1 DE LA FUNCIONALIDAD DE PRODUCCION....

    public void crearSedesMaquinasRepuestos() {

        // Episodio 43
        Proveedor p1 = new Proveedor(800, "Rag Tela");
        p1.setInsumo(new Insumo("Tela", p1));
        Proveedor p2 = new Proveedor(1000, "Macro Telas");
        p2.setInsumo(new Insumo("Hilo", p2));
        Proveedor p4 = new Proveedor(15000, "Insumos textileros");
        p4.setInsumo(new Insumo("Cremallera", p4));
        Proveedor p3 = new Proveedor(5000, "San Remo");
        p3.setInsumo(new Insumo("Boton", p3));
        Proveedor p5 = new Proveedor(900, "Fatelares");
        p5.setInsumo(new Insumo("Tela", p5));
        Proveedor p6 = new Proveedor(900, "Macro Textil");
        p6.setInsumo(new Insumo("Tela", p6));
        Proveedor p9 = new Proveedor(1200, "Hilos Venus");
        p9.setInsumo(new Insumo("Hilo", p9));
        Proveedor p7 = new Proveedor(10000, "Insumos para Confección");
        p7.setInsumo(new Insumo("Cremallera", p7));
        Proveedor p8 = new Proveedor(8000, "InduBoton");
        p8.setInsumo(new Insumo("Boton", p8));
        Proveedor p10 = new Proveedor(5000, "Primavera");
        p10.setDescuento(0.06F);
        p10.setInsumo(new Bolsa("Bolsa", p10));
        Proveedor p11 = new Proveedor(8000, "Empaques y Cartones");
        p11.setInsumo(new Bolsa("Bolsa", p11));
        p11.setDescuento(0.1F);
        Proveedor p12 = new Proveedor(6000, "Plastienda");
        p10.setDescuento(0.05F);
        p12.setInsumo(new Bolsa("Bolsa", p12));
        // PROVEEDORES QUE VENDEN AGUJAS DE LA MAQUINA DE COSER:
        Proveedor p13 = new Proveedor(80500, "Solo Agujas");
        p13.setInsumo(new Insumo("Agujas de la Maquina de Coser", p13));
        Proveedor p14 = new Proveedor(92000, "Agujas y mas");
        p14.setInsumo(new Insumo("Agujas de la Maquina de Coser", p14));
        Proveedor p15 = new Proveedor(70000, "Las propias agujas");
        p15.setInsumo(new Insumo("Agujas de la Maquina de Coser", p15));
        p1.setDeuda(new Deuda(new Fecha(15, 1, 24), 500_000, p1.getNombre(), "Proveedor", 5));
        p2.setDeuda(new Deuda(new Fecha(15, 1, 24), 1_000_000, p2.getNombre(), "Proveedor", 10));
        // PROVEEDORES QUE VENDEN ACEITE:
        Proveedor p16 = new Proveedor(24000, "Aceites y mas");
        p16.setInsumo(new Insumo("Aceite 946 ml", p16));
        Proveedor p17 = new Proveedor(30000, "Aceitunas");
        p17.setInsumo(new Insumo("Aceite 946 ml", p17));
        Proveedor p18 = new Proveedor(20000, "El barato del Aceite");
        p18.setInsumo(new Insumo("Aceite 946 ml", p18));

        // PROVEEDORES QUE VENDEN CUCHILLAS
        Proveedor p19 = new Proveedor(32000, "El de las Cuchillas");
        p19.setInsumo(new Insumo("Cuchillas", p19));
        Proveedor p20 = new Proveedor(28000, "El barato de las cuchillas");
        p20.setInsumo(new Insumo("Cuchillas", p20));
        Proveedor p21 = new Proveedor(37000, "El carero de las cuchillas");
        p21.setInsumo(new Insumo("Cuchillas", p21));

        // PROVEEDORES QUE VENDEN AFILADORES
        Proveedor p22 = new Proveedor(72000, "El afilador");
        p22.setInsumo(new Insumo("Afiladores", p22));
        Proveedor p23 = new Proveedor(60000, "La bodega del afilador");
        p23.setInsumo(new Insumo("Afiladores", p23));
        Proveedor p24 = new Proveedor(80000, "Afilamos caro");
        p24.setInsumo(new Insumo("Afiladores", p24));

        // PROVEEDORES QUE VENDEN RESISTENCIAS ELECTRICAS:
        Proveedor p25 = new Proveedor(160000, "Resistencias y mas");
        p25.setInsumo(new Insumo("Resistencia electrica", p25));
        Proveedor p26 = new Proveedor(140000, "Electricos");
        p26.setInsumo(new Insumo("Resistencia electrica", p26));

        // PROVEEDORES QUE VENDEN MANGUERAS DE VAPOR:
        Proveedor p27 = new Proveedor(120000, "Mangueras y mas");
        p27.setInsumo(new Insumo("Manguera de vapor", p27));
        Proveedor p28 = new Proveedor(140000, "Mangueras Don Diego");
        p28.setInsumo(new Insumo("Manguera de vapor", p28));

        // PROVEEDORES QUE VENDEN AGUJAS DE LA BORDADORA INDUSTRIAL:
        Proveedor p29 = new Proveedor(55000, "El Agujero");
        p29.setInsumo(new Insumo("Agujas de la Bordadora industrial", p29));
        Proveedor p30 = new Proveedor(45000, "La bodega del Agujero");
        p30.setInsumo(new Insumo("Agujas de la Bordadora industrial", p30));

        // PROVEEDORES QUE VENDEN BANDAS DE TRANSMISION:
        Proveedor p31 = new Proveedor(200000, "El de las Bandas");
        p31.setInsumo(new Insumo("Bandas de transmision", p31));
        Proveedor p32 = new Proveedor(250000, "El carero de las Bandas");
        p32.setInsumo(new Insumo("Bandas de transmision", p32));

        // PROVEEDORES QUE VENDEN TINTA NEGRA PARA IMPRESORA:
        Proveedor p33 = new Proveedor(44000, "Tinta por aqui");
        p33.setInsumo(new Insumo("Tinta Negra 90 ml", p33));
        Proveedor p34 = new Proveedor(50000, "El tintoso");
        p34.setInsumo(new Insumo("Tinta Negra 90 ml", p34));

        // PROVEEDORES QUE VENDEN LECTORES DE BARRAS:
        Proveedor p35 = new Proveedor(120000, "Mega tecnologies");
        p35.setInsumo(new Insumo("Lector de barras", p35));
        Proveedor p36 = new Proveedor(160000, "HP");
        p36.setInsumo(new Insumo("Lector de barras", p36));

        // PROVEEDORES QUE VENDEN PAPEL QUIMICO:
        Proveedor p37 = new Proveedor(40000, "Panamericana");
        p37.setInsumo(new Insumo("Papel quimico", p37));
        Proveedor p38 = new Proveedor(50000, "SSKaisen");
        p38.setInsumo(new Insumo("Papel quimico", p38));

        // PROVEEDORES QUE VENDEN CARGADORES PARA PORTATILES:
        Proveedor p39 = new Proveedor(150000, "Homecenter");
        p39.setInsumo(new Insumo("Cargador Computador", p39));
        Proveedor p40 = new Proveedor(180000, "Todo en cargadores");
        p40.setInsumo(new Insumo("Cargador Computador", p40));

        // PROVEEDORES QUE VENDER MOUSE PARA PORTATILES:
        Proveedor p41 = new Proveedor(20000, "Mecado Libre");
        p41.setInsumo(new Insumo("Mouse Computador", p41));
        Proveedor p42 = new Proveedor(30000, "Asus");
        p42.setInsumo(new Insumo("Mouse Computador", p42));

        // CREACION DE TODOS LOS REPUESTOS QUE MANEJAREMOS PARA LA FUNCIONALIDAD
        // PRODUCCION
        Repuesto AgujasMC = new Repuesto("Agujas de la Maquina de coser", 12, p13);
        Repuesto Aceite = new Repuesto("Aceite", 60, p16);

        Repuesto Cuchillas = new Repuesto("Cuchillas", 60, p19);
        Repuesto Afiladores = new Repuesto("Afiladores", 750, p22);

        Repuesto ResistenciaElectrica = new Repuesto("Resistencia Electrica", 1500, p25);
        Repuesto MangueraDeVapor = new Repuesto("Manguera de Vapor", 750, p27);

        Repuesto AgujasBI = new Repuesto("Agujas de la Bordadora Industrial", 25, p29);

        Repuesto BandasDeTransmision = new Repuesto("Bandas de Transmision", 2500, p31);

        Repuesto TintaN = new Repuesto("Tinta Negra Impresora", 3000, p33);

        Repuesto Lector = new Repuesto("Lector de barras", 3000, p35);
        Repuesto PapelQuimico = new Repuesto("Papel químico", 72, p37);

        Repuesto Cargador = new Repuesto("Cargador Computador", 6000, p39);
        Repuesto Mouse = new Repuesto("Mouse Computador", 9000, p41);

        // CREACION DE LAS SEDES QUE MANEJAREMOS, CON SUS RESPECTIVAS MAQUINAS EN CADA
        // UNA DE ELLAS
        sedeP = new Sede("Sede Principal");
        sede2 = new Sede("Sede 2");

        // AGRUPACION DE LOS REPUESTOS EN LISTAS PARA ENVIARLOS A LAS MAQUINAS
        // CORRESPONDIENTES
        ArrayList<Repuesto> repuestosMC = new ArrayList<>();
        ArrayList<Repuesto> repuestosMCorte = new ArrayList<>();
        ArrayList<Repuesto> repuestosPI = new ArrayList<>();
        ArrayList<Repuesto> repuestosBI = new ArrayList<>();
        ArrayList<Repuesto> repuestosMTermofijado = new ArrayList<>();
        ArrayList<Repuesto> repuestosMTijereado = new ArrayList<>();
        ArrayList<Repuesto> repuestosImp = new ArrayList<>();
        ArrayList<Repuesto> repuestosRe = new ArrayList<>();
        ArrayList<Repuesto> repuestosComp = new ArrayList<>();

        ArrayList<Repuesto> repuestosMC2 = new ArrayList<>();
        ArrayList<Repuesto> repuestosMCorte2 = new ArrayList<>();
        ArrayList<Repuesto> repuestosPI2 = new ArrayList<>();
        ArrayList<Repuesto> repuestosBI2 = new ArrayList<>();
        ArrayList<Repuesto> repuestosMTermofijado2 = new ArrayList<>();
        ArrayList<Repuesto> repuestosMTijereado2 = new ArrayList<>();
        ArrayList<Repuesto> repuestosImp2 = new ArrayList<>();
        ArrayList<Repuesto> repuestosRe2 = new ArrayList<>();
        ArrayList<Repuesto> repuestosComp2 = new ArrayList<>();

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

        // respuestos para las maquinas de la sede2
        repuestosImp2.add(TintaN.copiar());

        repuestosRe2.add(PapelQuimico.copiar());
        repuestosRe2.add(Lector.copiar());

        repuestosComp2.add(Mouse.copiar());
        repuestosComp2.add(Cargador.copiar());

        repuestosMC2.add(AgujasMC.copiar());
        repuestosMC2.add(Aceite.copiar());

        repuestosMCorte2.add(Cuchillas.copiar());
        repuestosMCorte2.add(Afiladores.copiar());

        repuestosPI2.add(ResistenciaElectrica.copiar());
        repuestosPI2.add(MangueraDeVapor.copiar());

        repuestosBI2.add(AgujasBI.copiar());
        repuestosBI2.add(Aceite.copiar());

        repuestosMTermofijado2.add(BandasDeTransmision.copiar());
        repuestosMTermofijado2.add(ResistenciaElectrica.copiar());

        repuestosMTijereado2.add(Cuchillas.copiar());
        repuestosMTijereado2.add(Aceite.copiar());

        // CREACION DE LAS MAQUINAS QUE MANEJAREMOS CON SUS RESPECTIVOS RESPUESTOS
        // sedeP
        Maquinaria MaquinaDeCoser = new Maquinaria("Maquina de Coser Industrial", 4250000, 600, repuestosMC, sedeP);
        Maquinaria MaquinaDeCorte = new Maquinaria("Maquina de Corte", 6000000, 700, repuestosMCorte, sedeP);
        Maquinaria PlanchaIndustrial = new Maquinaria("Plancha Industrial", 2000000, 900, repuestosPI, sedeP);
        Maquinaria BordadoraIndustrial = new Maquinaria("Bordadora Industrial", 31000000, 500, repuestosBI, sedeP);
        Maquinaria MaquinaDeTermofijado = new Maquinaria("Maquina de Termofijado", 20000000, 1000,
                repuestosMTermofijado, sedeP);
        Maquinaria MaquinaDeTijereado = new Maquinaria("Maquina de Tijereado", 5000000, 600, repuestosMTijereado,
                sedeP);
        Maquinaria Impresora = new Maquinaria("Impresora", 800000, 2000, repuestosImp, sedeP);
        Maquinaria Registradora = new Maquinaria("Caja Registradora", 700000, 17000, repuestosRe, sedeP);
        Maquinaria Computador = new Maquinaria("Computador", 2_000_000, 10000, repuestosImp, sedeP);

        // sede2
        Maquinaria MaquinaDeCoser2 = new Maquinaria("Maquina de Coser Industrial", 4250000, 600, repuestosMC2, sede2);
        Maquinaria MaquinaDeCorte2 = new Maquinaria("Maquina de Corte", 6000000, 700, repuestosMCorte2, sede2);
        Maquinaria PlanchaIndustrial2 = new Maquinaria("Plancha Industrial", 2000000, 900, repuestosPI2, sede2);
        Maquinaria BordadoraIndustrial2 = new Maquinaria("Bordadora Industrial", 31000000, 500, repuestosBI2, sede2);
        Maquinaria MaquinaDeTermofijado2 = new Maquinaria("Maquina de Termofijado", 20000000, 1000,
                repuestosMTermofijado2, sede2);
        Maquinaria MaquinaDeTijereado2 = new Maquinaria("Maquina de Tijereado", 5000000, 600, repuestosMTijereado2,
                sede2);
        Maquinaria Impresora2 = new Maquinaria("Impresora", 800000, 2000, repuestosImp2, sede2);
        Maquinaria Registradora2 = new Maquinaria("Caja Registradora", 700000, 17000, repuestosRe2, sede2);
        Maquinaria Computador2 = new Maquinaria("Computador", 2_000_000, 10000, repuestosImp2, sede2);

        Banco bp = new Banco("principal", "Banco Montreal", 400_000_000, 0.05F);
        Banco b1 = new Banco("secundaria", "Banco Montreal", 5_000_000, 0.05F);
        Banco b3 = new Banco("principal", "Bancolombia", 140_000_000, 0.09F);
        Banco b4 = new Banco("principal", "Banco Davivienda", 80_000_000, 0.1F);
        Banco b2 = new Banco("principal", "Banco de Bogotá", 125_000_000, 0.07F);
        Banco tm = new Banco("principal", "Inversiones Terramoda", 160_000_000, 0.0F);
        Banco.setCuentaPrincipal(bp);
        sede2.setCuentaSede(b1);
        sedeP.setCuentaSede(b4);

        Deuda d1 = new Deuda(new Fecha(15, 1, 20), 20_000_000, "Bancolombia", "Banco", 1);
        b3.actualizarDeuda(d1);
        d1.setEstadodePago(true);
        d1.setCapitalPagado(20_000_000);
        Deuda d2 = new Deuda(new Fecha(15, 1, 20), 100_000_000, "Banco Montreal", "Banco", 18);
        d2.setCapitalPagado(100_000_000 / 2);
        b1.actualizarDeuda(d2);
        b2.actualizarDeuda(new Deuda(new Fecha(10, 1, 24), 5_000_000, "Banco de Bogotá", "Banco", 10));
        tm.actualizarDeuda(new Deuda(new Fecha(30, 9, 22), 150_000_000, "Inversiones Terramoda", "Banco", 18));
        tm.actualizarDeuda(new Deuda(new Fecha(20, 2, 23), 800_000, "Inversiones Terramoda", "Banco", 18));

        Insumo i1 = new Insumo("Tela", 1 * 20, p5, sedeP);
        Insumo i2 = new Insumo("Tela", 1 * 20, p5, sede2);
        Insumo i3 = new Insumo("Boton", 4 * 20, p3, sedeP);
        Insumo i4 = new Insumo("Boton", 4 * 20, p3, sede2);
        Insumo i5 = new Insumo("Cremallera", 1 * 20, p4, sedeP);
        Insumo i6 = new Insumo("Cremallera", 1 * 20, p4, sede2);
        Insumo i7 = new Insumo("Hilo", 100 * 20, p2, sedeP);
        Insumo i8 = new Insumo("Hilo", 100 * 20, p2, sede2);
        Bolsa i9 = new Bolsa("Bolsa", 1 * 20, p10, sedeP, 8);
        Bolsa i10 = new Bolsa("Bolsa", 1 * 20, p10, sede2, 8);
        Bolsa i11 = new Bolsa("Bolsa", 1 * 20, p10, sedeP, 3);
        Bolsa i12 = new Bolsa("Bolsa", 1 * 20, p10, sede2, 3);
        Bolsa i13 = new Bolsa("Bolsa", 1 * 20, p10, sedeP, 1);
        Bolsa i14 = new Bolsa("Bolsa", 1 * 20, p10, sede2, 1);

        Empleado betty = new Empleado(Area.DIRECCION, new Fecha(1, 1, 23), sedeP, "Beatriz Pinzón", 4269292,
                Rol.PRESIDENTE, 10, Membresia.NULA, Computador);
        Empleado Armando = new Empleado(Area.DIRECCION, new Fecha(30, 11, 20), sedeP, "Armando Mendoza", 19121311,
                Rol.PRESIDENTE, 15, Membresia.PLATA, Computador.copiar());
        Empleado Cata = new Empleado(Area.OFICINA, new Fecha(1, 6, 16), sedeP, "Catalina Ángel", 7296957, Rol.ASISTENTE,
                20, Membresia.ORO, Impresora);
        Empleado Mario = (new Empleado(Area.OFICINA, new Fecha(30, 11, 20), sedeP, "Mario Calderón", 19256002,
                Rol.EJECUTIVO, 4, Membresia.PLATA, Impresora.copiar()));
        Empleado Hugo = (new Empleado(Area.CORTE, new Fecha(1, 5, 14), sedeP, "Hugo Lombardi", 7980705, Rol.DISEÑADOR,
                20, Membresia.ORO, MaquinaDeCorte));
        Empleado Inez = (new Empleado(Area.CORTE, new Fecha(1, 5, 14), sedeP, "Inez Ramirez", 23103023, Rol.MODISTA, 2,
                Membresia.NULA, MaquinaDeCoser));
        Empleado Aura = (new Empleado(Area.VENTAS, new Fecha(1, 2, 23), sedeP, "Aura Maria", 4146118, Rol.SECRETARIA, 2,
                Membresia.NULA, Registradora));
        Empleado Sandra = (new Empleado(Area.CORTE, new Fecha(15, 9, 23), sedeP, "Sandra Patiño", 5941859, Rol.MODISTA,
                5, Membresia.NULA, PlanchaIndustrial));
        Empleado Sofia = (new Empleado(Area.CORTE, new Fecha(30, 9, 22), sedeP, "Sofía Lopez", 5079239, Rol.MODISTA, 6,
                Membresia.NULA, MaquinaDeTermofijado));
        Empleado Mariana = (new Empleado(Area.CORTE, new Fecha(1, 5, 23), sedeP, "Mariana Valdéz", 4051807, Rol.MODISTA,
                10, Membresia.BRONCE, MaquinaDeTijereado));
        Empleado Bertha = (new Empleado(Area.CORTE, new Fecha(25, 2, 20), sedeP, "Bertha Muñoz", 7137741, Rol.MODISTA,
                15, Membresia.BRONCE, BordadoraIndustrial));
        Empleado Wilson = (new Empleado(Area.VENTAS, new Fecha(4, 4, 22), sedeP, "Wilson Sastoque", 9634927, Rol.PLANTA,
                5, Membresia.NULA, Registradora.copiar()));

        Empleado Gutierrez = (new Empleado(Area.DIRECCION, new Fecha(5, 8, 19), sede2, "Saul Gutierrez", 9557933,
                Rol.EJECUTIVO, 11, Membresia.NULA, Computador2.copiar()));
        Empleado Marcela = (new Empleado(Area.DIRECCION, new Fecha(30, 11, 20), sede2, "Marcela Valencia", 8519803,
                Rol.EJECUTIVO, 10, Membresia.ORO, Computador2.copiar()));
        Empleado Gabriela = new Empleado(Area.VENTAS, new Fecha(1, 1, 24), sede2, "Gabriela Garza", 5287925,
                Rol.VENDEDOR, 9, Membresia.PLATA, Registradora2.copiar());
        Empleado Patricia = (new Empleado(Area.OFICINA, new Fecha(5, 2, 23), sede2, "Patricia Fernandez", 4595311,
                Rol.SECRETARIA, 6, Membresia.BRONCE, Impresora2.copiar()));
        Empleado Kenneth = (new Empleado(Area.CORTE, new Fecha(1, 1, 24), sede2, "Kenneth Johnson", 7494184,
                Rol.MODISTA, 8, Membresia.ORO, PlanchaIndustrial2.copiar()));
        Empleado Robles = (new Empleado(Area.OFICINA, new Fecha(12, 10, 24), sede2, "Miguel Robles", 7518004,
                Rol.VENDEDOR, 7, Membresia.BRONCE, Impresora2.copiar()));
        Empleado Alejandra = (new Empleado(Area.CORTE, new Fecha(1, 2, 24), sede2, "Alejandra Zingg", 6840296,
                Rol.MODISTA, 2, Membresia.BRONCE, BordadoraIndustrial2.copiar()));
        Empleado Cecilia = (new Empleado(Area.CORTE, new Fecha(1, 2, 23), sede2, "Cecilia Bolocco", 7443886,
                Rol.MODISTA, 10, Membresia.PLATA, MaquinaDeCoser2.copiar()));
        Empleado Freddy = (new Empleado(Area.VENTAS, new Fecha(31, 1, 22), sede2, "Freddy Contreras", 6740561,
                Rol.PLANTA, 5, Membresia.NULA, Registradora2.copiar()));
        Empleado Adriana = (new Empleado(Area.CORTE, new Fecha(18, 6, 25), sede2, "Adriana arboleda", 5927947,
                Rol.MODISTA, 20, Membresia.ORO, MaquinaDeCorte2.copiar()));
        Empleado Karina = (new Empleado(Area.CORTE, new Fecha(9, 3, 25), sede2, "Karina Larson", 5229381, Rol.MODISTA,
                2, Membresia.PLATA, MaquinaDeTermofijado2.copiar()));
        Empleado Jenny = (new Empleado(Area.CORTE, new Fecha(1, 8, 24), sede2, "Jenny Garcia", 4264643, Rol.MODISTA, 1,
                Membresia.ORO, MaquinaDeTijereado2.copiar()));
        Empleado ol = new Empleado(Area.DIRECCION, new Fecha(1, 2, 20), sede2, "Gustavo Olarte", 7470922, Rol.EJECUTIVO,
                3, Membresia.NULA, Computador2.copiar());
        ol.setTraslados(3);
        ArrayList<Area> a = new ArrayList<Area>();
        a.add(Area.VENTAS);
        a.add(Area.OFICINA);
        ol.setAreas(a);
        new Evaluacionfinanciera(-200_000, ol);
        new Evaluacionfinanciera(-120_000, ol);
        new Evaluacionfinanciera(-50_000, ol);
        new Evaluacionfinanciera(1_000, ol);
        new Evaluacionfinanciera(-70_000, ol);
        new Evaluacionfinanciera(50_000_000, betty);
        new Evaluacionfinanciera(1_000_000, betty);
        new Evaluacionfinanciera(500_000, Armando);
        new Evaluacionfinanciera(-10_000, Armando);
        new Evaluacionfinanciera(100_000, Armando);

        Persona c1 = new Persona("Claudia Elena Vásquez", 5162307, Rol.MODISTA, 2, false, Membresia.BRONCE);
        Persona c2 = new Persona("Michel Doniel", 9458074, Rol.ASISTENTE, 4, false, Membresia.BRONCE);
        Persona c3 = new Persona("Claudia Bosch", 5975399, Rol.MODISTA, 4, false, Membresia.ORO);
        Persona c4 = new Persona("Mónica Agudelo", 8748155, Rol.MODISTA, 8, false, Membresia.ORO);
        Persona c5 = new Persona("Daniel Valencia", 9818534, Rol.EJECUTIVO, 10, false, Membresia.BRONCE);
        Persona c6 = new Persona("Efraín Rodriguez", 8256519, Rol.VENDEDOR, 10, false, Membresia.NULA);
        Persona c7 = new Persona("Mauricio Brightman", 8823954, Rol.ASISTENTE, 10, false, Membresia.ORO);
        Persona c8 = new Persona("Nicolás Mora", 4365567, Rol.EJECUTIVO, 8, false, Membresia.NULA);
        Persona c9 = new Persona("Roberto Mendoza", 28096740, Rol.PRESIDENTE, 2, false, Membresia.ORO);
        Persona c10 = new Persona("Hermes Pinzón", 21077781, Rol.ASISTENTE, 2, false, Membresia.NULA);
        Persona c11 = new Persona("Julia Solano", 28943158, Rol.SECRETARIA, 10, false, Membresia.BRONCE);
        Persona c12 = new Persona("Maria Beatriz Valencia", 6472799, Rol.ASISTENTE, 2, false, Membresia.BRONCE);
        Persona c13 = new Persona("Antonio Sanchéz", 8922998, Rol.VENDEDOR, 12, false, Membresia.NULA);
        ArrayList<String> tiposp = new ArrayList<String>();
        ArrayList<Integer> cantidadesp = new ArrayList<Integer>();
        ArrayList<String> tiposc = new ArrayList<String>();
        ArrayList<Integer> cantidadesc = new ArrayList<Integer>();
        tiposp.add("Tela");
        tiposp.add("Boton");
        tiposp.add("Cremallera");
        tiposp.add("Hilo");
        cantidadesp.add(200);
        cantidadesp.add(1);
        cantidadesp.add(1);
        cantidadesp.add(300);
        Pantalon.setCantidadInsumo(cantidadesp);
        Pantalon.setTipoInsumo(tiposp);
        tiposc.add("Tela");
        tiposc.add("Boton");
        tiposc.add("Hilo");
        cantidadesc.add(100);
        cantidadesc.add(3);
        cantidadesc.add(90);
        Camisa.setCantidadInsumo(cantidadesc);
        Camisa.setTipoInsumo(tiposc);

        ArrayList<Insumo> tiposca = new ArrayList<Insumo>();
        tiposca.add(i1);
        tiposca.add(i3);
        tiposca.add(i7);
        ArrayList<Insumo> tiposcb = new ArrayList<Insumo>();
        tiposcb.add(i2);
        tiposcb.add(i4);
        tiposcb.add(i8);
        ArrayList<Insumo> tipospa = new ArrayList<Insumo>();
        tipospa.add(i1);
        tipospa.add(i3);
        tipospa.add(i5);
        tipospa.add(i7);
        ArrayList<Insumo> tipospb = new ArrayList<Insumo>();
        tipospb.add(i2);
        tipospb.add(i4);
        tipospb.add(i6);
        tipospb.add(i8);

        Prenda r1 = new Pantalon(new Fecha(1, 1, 23), Hugo, false, true, sedeP,tipospa);
        Prenda r2 = new Pantalon(new Fecha(1, 1, 23), Inez, false, true, sedeP,tipospa);
        Prenda r3 = new Pantalon(new Fecha(1, 1, 23), Sandra, false, true, sedeP,tipospa);
        Prenda r4 = new Pantalon(new Fecha(1, 1, 23), Sofia, false, true, sedeP,tipospa);
        Prenda r5 = new Pantalon(new Fecha(1, 1, 23), Mariana, false, true, sedeP,tipospa);
        Prenda r6 = new Pantalon(new Fecha(1, 1, 23), Bertha, false, true, sedeP,tipospa);
        Prenda r7 = new Camisa(new Fecha(1, 1, 23), Hugo, false, true, sedeP,tiposca);
        Prenda r8 = new Camisa(new Fecha(1, 1, 23), Inez, false, true, sedeP,tiposca);
        Prenda r9 = new Camisa(new Fecha(1, 1, 23), Sandra, false, true, sedeP,tiposca);
        Prenda r10 = new Camisa(new Fecha(1, 1, 23), Sofia, false, true, sedeP,tiposca);
        Prenda r11 = new Camisa(new Fecha(1, 1, 23), Mariana, false, true, sedeP,tiposca);
        Prenda r12 = new Camisa(new Fecha(1, 1, 23), Bertha, false, true, sedeP,tiposca);
        Prenda r13 = new Pantalon(new Fecha(1, 1, 23), Jenny, false, true, sede2,tipospb);
        Prenda r14 = new Pantalon(new Fecha(1, 1, 23), Karina, true, true, sede2,tipospb);
        Prenda r15 = new Pantalon(new Fecha(1, 1, 23), Adriana, false, true, sede2,tipospb);
        Prenda r16 = new Pantalon(new Fecha(1, 1, 23), Cecilia, false, true, sede2,tipospb);
        Prenda r17 = new Pantalon(new Fecha(1, 1, 23), Alejandra, false, true, sede2,tipospb);
        Prenda r18 = new Pantalon(new Fecha(1, 1, 23), Kenneth, false, true, sede2,tipospb);
        Prenda r19 = new Camisa(new Fecha(1, 1, 23), Jenny, false, true, sede2,tiposcb);
        Prenda r20 = new Camisa(new Fecha(1, 1, 23), Karina, true, true, sede2,tiposcb);
        Prenda r21 = new Camisa(new Fecha(1, 1, 23), Adriana, false, true, sede2,tiposcb);
        Prenda r22 = new Camisa(new Fecha(1, 1, 23), Cecilia, false, true, sede2,tiposcb);
        Prenda r23 = new Camisa(new Fecha(1, 1, 23), Alejandra, false, true, sede2,tiposcb);
        Prenda r24 = new Camisa(new Fecha(1, 1, 23), Kenneth, false, true, sede2,tiposcb);

        Karina.setPericia(0.1F);

        ArrayList<Prenda> ps1 = new ArrayList<Prenda>();
        ps1.add(r13);
        Venta v1 = new Venta(sede2, new Fecha(28, 11, 24), c8, Gabriela, Patricia, ps1, 200000, 250000);
        v1.setCostoEnvio(20000);
        b1.setAhorroBanco(b1.getAhorroBanco() + 250000);
        int com1 = (int) (250000 * 0.05f);
        Gabriela.setRendimientoBonificacion(com1);

        ArrayList<Prenda> ps2 = new ArrayList<Prenda>();
        ps2.add(r16);
        Venta v2 = new Venta(sede2, new Fecha(29, 11, 24), c13, Freddy, Patricia, ps2, 300000, 350000);
        v2.setCostoEnvio(100000);
        b2.setAhorroBanco(b2.getAhorroBanco() + 350000);
        int com2 = (int) (350000 * 0.05f);
        Freddy.setRendimientoBonificacion(com2);

        ArrayList<Prenda> ps3 = new ArrayList<Prenda>();
        ps3.add(r15);
        Venta v3 = new Venta(sede2, new Fecha(29, 1, 25), c13, Freddy, Patricia, ps3, 300000, 350000);
        v3.setCostoEnvio(100000);
        b3.setAhorroBanco(b2.getAhorroBanco() + 350000);
        int com3 = (int) (350000 * 0.05f);
        Freddy.setRendimientoBonificacion(com3);

        ArrayList<Prenda> ps4 = new ArrayList<Prenda>();
        ps4.add(r1);
        ps4.add(r2);
        Venta v4 = new Venta(sedeP, new Fecha(30, 11, 24), c6, Aura, Cata, ps4, 300000, 350000);
        v4.setCostoEnvio(100000);
        b1.setAhorroBanco(b1.getAhorroBanco() + 350000);
        int com4 = (int) (350000 * 0.05f);
        Aura.setRendimientoBonificacion(com3);

        ArrayList<Prenda> ps5 = new ArrayList<Prenda>();
        ps5.add(r7);
        Venta v5 = new Venta(sedeP, new Fecha(30, 1, 25), c6, Aura, Cata, ps5, 300000, 350000);
        v5.setCostoEnvio(100000);
        b1.setAhorroBanco(b1.getAhorroBanco() + 350000);
        int com5 = (int) (350000 * 0.05f);
        Aura.setRendimientoBonificacion(com3);

        ArrayList<Prenda> ps6 = new ArrayList<Prenda>();
        ps6.add(r15);
        ps6.add(r16);
        Venta v6 = new Venta(sedeP, new Fecha(25, 11, 24), c4, Wilson, Mario, ps6, 400000, 60000);
        v6.setCostoEnvio(100000);
        b3.setAhorroBanco(b3.getAhorroBanco() + 600000);
        int com6 = (int) (600000 * 0.05f);
        Wilson.setRendimientoBonificacion(com6);
    }

    static void crearVentaAleatoria(int deTantosProductos,int aTantosProductos, Fecha fecha, Empleado asesor, Empleado encargado, int cantidad,Sede sede){
        for (int idxVenta=0;idxVenta<cantidad;idxVenta++){
            int precio = 0;
            int costoEnvio = 0;
            int cantidadProductos = (int) (Math.random() * (aTantosProductos - deTantosProductos + 1) + deTantosProductos);
            ArrayList<Prenda> articulos= new ArrayList<>();
            for (int idxProducto=0;idxProducto<cantidadProductos; idxProducto++){
                int tipoProducto = (int) (Math.random() * 2);
                if (tipoProducto==0){
                    Camisa producto = new Camisa(fecha, asesor, false, true, sede,Camisa.getInsumosNecesariosAleatorios());
                    precio+=50_000;
                    costoEnvio+=1_000;
                    articulos.add(producto);
                }
                if (tipoProducto==1){
                    Pantalon producto = new Pantalon(fecha, asesor, false, true, sede,Pantalon.getInsumosNecesariosAleatorios());
                    precio+=60_000;
                    costoEnvio+=1_000;
                    articulos.add(producto);
                }
            }
            Persona cliente = Persona.getListaPersonas().get((int) (Math.random() * Persona.getListaPersonas().size()));
            Venta venta = new Venta(sede,fecha,cliente,asesor, encargado, articulos, precio, precio+costoEnvio);
            asesor.setRendimientoBonificacion((int)( precio*0.05f));
            venta.setCostoEnvio(costoEnvio);
        }
    }

    // para la interaccion 1 de produccion
    public void dondeRetirar() {

        Scanner scanner = new Scanner(System.in);

        System.out.println("¿Desde cual sede quieres comprar el repuesto requerido?");
        System.out.println("1. " + sedeP.getNombre() + " tiene disponible: " + sedeP.getCuentaSede().getAhorroBanco());
        System.out.println("2. " + sede2.getNombre() + " tiene disponible: " + sede2.getCuentaSede().getAhorroBanco());

        int opcion = 0;
        while(opcion != 1 && opcion != 2){
            opcion = scanner.nextInt();
            if (opcion == 1) {
                long nuevoDineroSede = (sedeP.getCuentaSede().getAhorroBanco()
                        - proveedorBdelmain.getInsumo().getPrecioIndividual());
                sedeP.getCuentaSede().setAhorroBanco(nuevoDineroSede);

                System.out.println(
                        "El repuesto se compro exitosamente desde la sede " + sedeP.getNombre() + ", saldo disponible:");
                System.out.println(sedeP.getNombre() + " = " + sedeP.getCuentaSede().getAhorroBanco());
                System.out.println(sede2.getNombre() + " = " + sede2.getCuentaSede().getAhorroBanco());

            } else if (opcion == 2) {
                long nuevoDineroSede = (sede2.getCuentaSede().getAhorroBanco()
                        - proveedorBdelmain.getInsumo().getPrecioIndividual());
                sede2.getCuentaSede().setAhorroBanco(nuevoDineroSede);

                System.out.println(
                        "El repuesto se compro exitosamente desde la sede " + sede2.getNombre() + ", saldo disponible:");
                System.out.println(sedeP.getNombre() + " = " + sedeP.getCuentaSede().getAhorroBanco());
                System.out.println(sede2.getNombre() + " = " + sede2.getCuentaSede().getAhorroBanco());
            } else {
                System.out.println("Opcion incorrecta, marque 1 o 2 segun desee");
            }
        }
    }

    public static void recibeProveedorB(Proveedor proveedorB) {
        Main.proveedorBdelmain = proveedorB;
    }

    public static Proveedor getProveedorBDelMain() {
        return Main.proveedorBdelmain;
    }

    static int nextIntSeguro(Scanner in) {
        while (!in.hasNextInt()) {
            System.out.println("Por favor, ingrese un número entero.");
            in.nextLine();
        }
        int retorno = in.nextInt();
        in.nextLine();
        return retorno;
    }

    // Interacción 1 de Facturación
    public static Venta vender(Scanner scanner) {
        Venta venta = null;
        ArrayList<Prenda> productosSeleccionados = new ArrayList<>();
        ArrayList<Integer> cantidadProductos = new ArrayList<>();
        System.out.println("\n"+"Ingrese la fecha de la venta:");
        Fecha fechaVenta = Main.fecha;
      System.out.println("\n"+"Seleccione el cliente al que se le realizará la venta:");
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
      

	    System.out.println("\n"+"Seleccione el número de la sede en la que se encuentra el cliente:");
	    for (int i = 0; i < Sede.getlistaSedes().size(); i++) {
	        System.out.println(i + ". " + Sede.getlistaSedes().get(i).getNombre());
	    }
	    int sedeSeleccionada = scanner.nextInt();
      scanner.nextLine();
      Sede sede = Sede.getlistaSedes().get(sedeSeleccionada);
      
      System.out.println("\n"+"Seleccione el número del empleado que se hará cargo del registro de la venta:");
      for(int i = 0; i < sede.getlistaEmpleados().size(); i++) {
        Empleado empleado = sede.getlistaEmpleados().get(i);
        if (empleado.getAreaActual()==Area.OFICINA) {
            System.out.println(i + ". " + empleado.getNombre());
    
          }
     }
    int encargadoSeleccionado = scanner.nextInt();
    scanner.nextLine();
    Empleado encargado = sede.getlistaEmpleados().get(encargadoSeleccionado);      
      System.out.println("\n"+"Seleccione el número del empleado que se hará cargo de asesorar la venta:");
      for(int i = 0; i < sede.getlistaEmpleados().size(); i++) {
        Empleado empleado = sede.getlistaEmpleados().get(i);
        if (empleado.getAreaActual()==Area.VENTAS) {
            System.out.println(i + ". " + empleado.getNombre());
      }
    }
    int vendedorSeleccionado = scanner.nextInt();
    scanner.nextLine();
    Empleado vendedor = sede.getlistaEmpleados().get(vendedorSeleccionado);
         
      int costosEnvio = 0;   

        bucleAgregarPrenda:
        while (true) {
            System.out.println("\n"+"Seleccione el número del producto que venderá:");
            System.out.println(0 + ". Camisa" + " -Precio " + Camisa.precioVenta());
            System.out.println(1 + ". Pantalón" + " -Precio " + Pantalon.PrecioVenta());
            //for (int i = 0; i < Prenda.getPrendasInventadas().size(); i++) {
                //Prenda producto = Prenda.getPrendasInventadas().get(i);
                //System.out.println(i + ". " + producto.getNombre() + " -Precio " + producto.getPrecio());
            //}
            int productoSeleccionado = scanner.nextInt();
            scanner.nextLine();
            Prenda prendaSeleccionada = Prenda.getPrendasInventadas().get(productoSeleccionado);
            String nombrePrendaSeleccionada = prendaSeleccionada.getNombre();
            System.out.println("Ingrese la cantidad de unidades que se desea del producto elegido:");
            int cantidadPrenda = scanner.nextInt();
            cantidadProductos.add(cantidadPrenda);
            int cantidadDisponible = 0;
            for(Prenda prenda: sede.getPrendasInventadas()){
                if(prenda.getNombre().equals(prendaSeleccionada.getNombre())){cantidadDisponible+=1;}
            }
            manejarFaltantes(sede, cantidadPrenda, cantidadDisponible, nombrePrendaSeleccionada, costosEnvio);//Método que aumenta el stock faltante
            //De la prenda seleccionada en la sede.
            if (cantidadPrenda > 0 && cantidadPrenda < Prenda.getPrendasInventadas().size()) {
                int eliminadas = 0;
                for (int i = 0;  i < Prenda.getPrendasInventadas().size() && eliminadas < cantidadPrenda; i++) {
                    if (Prenda.getPrendasInventadas().get(i) == prendaSeleccionada) {
                    Prenda eliminada = Prenda.getPrendasInventadas().get(i);
                    Prenda.getPrendasInventadas().remove(i);// Remueve de la lista de prendasInventadas de la empresa
                    sede.getPrendasInventadas().remove(eliminada); // Remueve de la lista de prendasInventadas de la sede
                    productosSeleccionados.add(eliminada); // Agregarla a productosSeleccionados
                    eliminadas++;
                    i--;
                }}}
                scanner.nextLine();
                System.out.println("\n"+"¿Deseas agregar otro producto a la venta?: (si/no)");
                String desicion = scanner.next().toLowerCase();
                if (desicion.equals("no")) {
                    System.out.println("Selección finalizada");
                    break bucleAgregarPrenda;
                }
                if (!desicion.equals("si")) {
                    break bucleAgregarPrenda;
                }
            }

        int sumaPreciosPrendas = 0;
        int cantidadCamisas = 0;
        int cantidadPantalon = 0;
        for (int i = 0; i < productosSeleccionados.size(); i++) {// A mejorar
            Prenda index = productosSeleccionados.get(i);
            if (index instanceof Camisa) {
                cantidadCamisas++;
                int calculoCamisas = (int) (cantidadCamisas * Camisa.precioVenta());
                if (cantidadCamisas >= 10) {
                    int descuento = (int) (calculoCamisas * 0.05f);
                    sumaPreciosPrendas += calculoCamisas - descuento;
                } else if (cantidadCamisas < 10) {
                    sumaPreciosPrendas += calculoCamisas;
                }
            } else if (index instanceof Pantalon) {// A mejorar
                cantidadPantalon++;
                int calculoPantalones = (int) (cantidadPantalon * Pantalon.PrecioVenta());
                if (cantidadPantalon >= 10) {
                    int descuento = (int) (cantidadPantalon * 0.05f);
                    sumaPreciosPrendas += calculoPantalones - descuento;
                } else if (cantidadPantalon < 10) {
                    sumaPreciosPrendas += calculoPantalones;
                }
            }
        }
        int IVA = (int) ((costosEnvio + sumaPreciosPrendas) * 0.19f);
        venta = new Venta(sede, fechaVenta, cliente, encargado, vendedor, productosSeleccionados);
        venta.setCostoEnvio(costosEnvio);
        sumaPreciosPrendas += costosEnvio;
        int monto = sumaPreciosPrendas + IVA + costosEnvio;
        int MontoPagar = (int) (monto - (monto * cliente.getMembresia().getPorcentajeDescuento()));
        venta.setMontoPagado(MontoPagar);
        venta.setSubtotal(sumaPreciosPrendas);
        System.out.println("Subtotal: " + sumaPreciosPrendas);
        int comisión = (int) (MontoPagar * 0.05f);
        vendedor.setRendimientoBonificacion(comisión);
        return venta;}

     //Interacción 2 Facturación
    public static Venta realizarVenta(Scanner scanner,  Venta venta) {

        ArrayList<Prenda> productosSeleccionados = venta.getArticulos();
        Sede sede = venta.getSede();
        Banco banco = sede.getCuentaSede();
        int totalPrendas = productosSeleccionados.size();

        ArrayList<Insumo> InsumosBodega = sede.getListaInsumosBodega();
        ArrayList<Integer> cantidadInsumosBodega = sede.getCantidadInsumosBodega();

        ArrayList<Bolsa> bolsasSeleccionadas = new ArrayList<>();
        int capacidadTotal = 0;

        Insumo insumo = null;
        boolean bp=false;
        boolean bm=false;
        boolean bg=false;
        while (capacidadTotal <= totalPrendas) {
            System.out.println("\n"+"Seleccione el tamaño de bolsa:");
            // Muestra opciones disponibles

            for (int i = 0; i < InsumosBodega.size(); i++) {
                Insumo bolsaI = InsumosBodega.get(i);
                if (bolsaI instanceof Bolsa) {
                    Bolsa bolsa = (Bolsa) bolsaI;
                    int capacidad = bolsa.getCapacidadMaxima();
                    int cantidad = sede.getCantidadInsumosBodega().get(i);
                    if (capacidad == 1 && cantidad > 0) {
                        bp=true;
                    }
                    if (capacidad == 3 && cantidad > 0) {
                        bm=true;
                    }
                    if (capacidad == 8 && cantidad > 0) {
                        bg=true;
                    }
                }
            }

            if (bp) {System.out.println("1. Bolsa pequeña (1 producto)");} 
            if (bm) {System.out.println("2. Bolsa mediana (3 productos)");} 
            if (bg) {System.out.println("3. Bolsa grande (8 productos)");}

            int opcionBolsa = scanner.nextInt();
            scanner.nextLine();

            int capacidadBolsa = 0;
            String nombreBolsa = null;

            switch (opcionBolsa) {
                case 1 -> {
                    capacidadBolsa = 1;
                    nombreBolsa = "Bolsa pequeña";
                    break;
                }
                case 2 -> {
                    capacidadBolsa = 3;
                    nombreBolsa = "Bolsa mediana";
                    break;
                }
                case 3 -> {
                    capacidadBolsa = 8;
                    nombreBolsa = "Bolsa grande";
                    break;
                }
                default -> {
                    System.out.println("Opción inválida. Intente nuevamente.");
                    continue;
                }
            }
            // Busca la bolsa requerida y se lo asigna a la lista de bolsas seleccionadas
            boolean bolsaEncontrada = false;
            int cantidadDisponible = 0;
            for (int i = 0; i < sede.getListaInsumosBodega().size(); i++) {
                Insumo bolsa = sede.getListaInsumosBodega().get(i);
                if (bolsa instanceof Bolsa) {
                    Bolsa bolsaEncontrar = (Bolsa) bolsa;
                    if (bolsaEncontrar.getCapacidadMaxima() == capacidadBolsa) {
                        cantidadDisponible += cantidadInsumosBodega.get(i);
                        if (cantidadDisponible > 0) {
                            bolsasSeleccionadas.add(bolsaEncontrar);
                            capacidadTotal += capacidadBolsa;
                            int cantidadActual = cantidadInsumosBodega.get(i);
                            cantidadInsumosBodega.set(i, cantidadActual - 1);
                            bolsaEncontrada = true;
                            totalPrendas-=capacidadTotal;
                            break;
                        }
                    }
                }
             if(capacidadTotal == totalPrendas)
             break;
            }

            // Si no hay suficiente stock, compra más bolsas al proveedor
            for (Sede revisarSedes : Sede.getlistaSedes()) {
                ArrayList<Insumo> listaInsumos = sede.getListaInsumosBodega();
                ArrayList<Integer> cantidadInsumos = sede.getCantidadInsumosBodega();
                for (int i = 0; i < listaInsumos.size(); i++) {
                    if (listaInsumos.get(i) instanceof Bolsa) {
                        if (cantidadInsumos.get(i) < 10) {
                            System.out.println("La sede " + sede.getNombre() +
                                    " tiene un menos de 10 bolsas en stock " +
                                    " (Cantidad: " + cantidadInsumos.get(i) + ").");
                            System.out.println("Comprando al proveedor...");
                            for (int e = 0; e < sede.getListaInsumosBodega().size(); e++) {
                                insumo = sede.getListaInsumosBodega().get(e);
                                if (insumo instanceof Bolsa && insumo.getNombre().equals(nombreBolsa)) {
                                    System.out.println("¿Cuántas bolsa de " + insumo.getNombre() + " desea comprar?");
                                    int cantidadComprar = scanner.nextInt();
                                    scanner.nextLine();
                                    int costoCompra = Proveedor.costoDeLaCantidad(insumo, cantidadComprar);
                                    banco.setAhorroBanco(banco.getAhorroBanco() - costoCompra); // Reducir los ahorros
                                                                                                // de la empresa
                                                                                                // respecto a la compra
                                    cantidadInsumosBodega.set(e, cantidadInsumosBodega.get(e) + cantidadComprar);
                                    insumo.setPrecioCompra(costoCompra);
                                    insumo.setUltimoPrecio(costoCompra);
                                    System.out.println("Se compraron " + cantidadComprar + " " + nombreBolsa
                                            + " por un costo total de " + costoCompra);
                                    break;
                                }
                            }
                        } else {
                            break;
                        }
                    }
                }
            }
        }
        venta.setBolsas(bolsasSeleccionadas);

        int totalVenta = venta.getMontoPagado() + bolsasSeleccionadas.size() * 2000;
        venta.setMontoPagado(totalVenta);

        System.out.println("\n"+"Venta realizada. Total de la venta con bolsas: " + totalVenta);
        sede.getHistorialVentas().add(venta);
        return venta;
    }

    // Interacción 3 Facturación
    public static String tarjetaRegalo(Scanner scanner, Venta venta) {
        Sede sede = venta.getSede();
        Banco banco = sede.getCuentaSede();

        System.out.println("\n"+"¿Desea usar una tarjeta de regalo? (si/no)");
        String respuesta = scanner.nextLine().toLowerCase();
        int nuevoIntento = 1;
        while (nuevoIntento == 1){
            if (respuesta.equals("si")) {
                System.out.println("Ingrese el código de la tarjeta de regalo:");
                String codigoIngresado = scanner.nextLine();

                if (Venta.getCodigosRegalo().contains(codigoIngresado)) {
                    System.out.println("Código válido. Procesando tarjeta de regalo...");
                    int indice = Venta.getCodigosRegalo().indexOf(codigoIngresado);
                    int montoTarjeta = Venta.getMontosRegalo().get(indice);
                    int montoVenta = venta.getMontoPagado();

                    if (montoTarjeta >= montoVenta) {
                        System.out.println("El monto de la tarjeta cubre la totalidad de la venta.");
                        int saldoRestante = montoTarjeta - montoVenta;
                        Venta.getMontosRegalo().add(indice, saldoRestante);
                        venta.setMontoPagado(0);// La compra fue cubierta por una tarjeta de regalo

                        System.out.println("Venta pagada con tarjeta de regalo.");
                        System.out.println("Saldo restante en la tarjeta de regalo: $" + saldoRestante);
                    } else {

                        int montoFaltante = montoVenta - montoTarjeta;// Verificar cuánto le faltó a la tarjeta por
                                                                      // cubrir
                        Venta.getMontosRegalo().add(indice, 0);
                        venta.setMontoPagado(montoFaltante);// Lo que faltó por cubrir con la tarjeta es lo que debe
                                                            // pagar el cliente
                        System.out.println("El monto de la tarjeta no es suficiente para cubrir la venta.");
                        System.out.println("Monto restante a pagar: $" + montoFaltante);
                    }
                    if (Venta.getMontosRegalo().get(indice) == 0) {
                        Venta.getCodigosRegalo().remove(indice);// Ya que la tarjeta ha sido usada, se desactiva y
                                                                // elimina de la lista
                        Venta.getMontosRegalo().remove(indice);
                        System.out.println("La tarjeta de regalo se ha agotado y ha sido desactivada.");
                    }
                } 
                else {
                    System.out.println(
                            "El código ingresado no es válido. Por favor, intentar de nuevo o pagar el monto total");
                    System.out.println("Ingresa 1 para intentar de nuevo.");
                    System.out.println("Ingresa 2 para salir del intento");
                    nuevoIntento = scanner.nextInt();
                }
            }
            else if(respuesta.equals("no")){
                nuevoIntento--;
                break;}
            }

        System.out.println("\n"+"¿Desea comprar una tarjeta de regalo? (si/no)");
        String compraTarjeta = scanner.nextLine().toLowerCase();

        if (compraTarjeta.equals("si")) {
            System.out.println("¿Por cuánto será la tarjeta de regalo? (monto en pesos)");
            int montoTarjeta = scanner.nextInt();
            String codigoGenerado = generarCodigoAleatorio();
            Venta.getCodigosRegalo().add(codigoGenerado);
            Venta.getMontosRegalo().add(montoTarjeta);
            banco.setAhorroBanco(banco.getAhorroBanco() + montoTarjeta);// Se agrega la cuenta de ahorros de la sede
            // el monto a pagar por la tarjeta de regalo de acuerdo al monto del que se
            // desea, sea

            System.out.println("Tarjeta de regalo generada exitosamente.");
            System.out.println("Código: " + codigoGenerado);
            System.out.println("Monto: $" + montoTarjeta);
        }

        // Calcular el ingreso de la venta
        int ingreso = venta.getMontoPagado();
        System.out.println("Ingreso calculado: $" + ingreso);
        banco.setAhorroBanco(banco.getAhorroBanco() + ingreso);

        System.out.println("Monto total en la cuenta de la sede: $" + banco.getAhorroBanco());
        Banco bancoRecibir = sede.getCuentaSede();
        Banco bancoTransferir = sede.getCuentaSede();
        if (!bancoTransferir.equals(Banco.getCuentaPrincipal())) {
            System.out.println("\n"+"¿Desea transferir fondos a la cuenta principal? (si/no)");
            String transferirFondos = scanner.nextLine().toLowerCase();
            if (transferirFondos.equals("si")) {
                System.out.println("¿Qué porcentaje desea transferir? (20% o 60%)");
                int porcentaje = nextIntSeguro(scanner);
                if (porcentaje == 20 || porcentaje == 60) {
                    long montoTransferencia = (bancoTransferir.getAhorroBanco() * porcentaje / 100) - 50000;
                    if (montoTransferencia > 0) {
                        if (bancoRecibir.getNombreCuenta().equals("principal")) {
                            bancoRecibir
                                    .setAhorroBanco(bancoTransferir.getAhorroBanco() - (montoTransferencia + 50000));
                            bancoRecibir.setAhorroBanco(bancoRecibir.getAhorroBanco() + montoTransferencia);
                            System.out.println("Transferencia exitosa.");
                            System.out.println("Monto transferido: $" + montoTransferencia);
                            System.out.println("Costo de transferencia: $50000");
                        }
                    } else {
                        System.out.println("Fondos insuficientes para cubrir la transferencia y el costo.");
                    }
                } else {
                    System.out.println("Porcentaje no válido. No se realizará la transferencia.");
                }
            }
        }

        if (bancoTransferir != null) {
            System.out.println("Estado final de la cuenta de la sede: $" + bancoTransferir.getAhorroBanco());
        }
        if (bancoRecibir != null) {
            System.out.println("Estado final de la cuenta principal: $" + bancoRecibir.getAhorroBanco());
        }
        ArrayList<Prenda> productosSeleccionados = venta.getArticulos();
        int MontoPagar = venta.getMontoPagado();
        int valorBase = (int)(MontoPagar/1+0.19f);
        int IVA = MontoPagar - valorBase;
        System.out.println("\n"+"---- FACTURA ----");
        System.out.println("Prendas compradas:");
        int cantidadCamisas = 0;
        int cantidadPantalon = 0;
        int subtotalCamisas = 0;
        int subtotalPantalon = 0;
        
        // Recorre la lista para calcular cantidades y subtotales por tipo de prenda
        for (Prenda prenda : productosSeleccionados) {
            if (prenda instanceof Camisa) {
                cantidadCamisas++;
                subtotalCamisas += Camisa.precioVenta();
            } else if (prenda instanceof Pantalon) {
                cantidadPantalon++;
                subtotalPantalon += Pantalon.PrecioVenta();
            }
        }
        
        // Imprime las prendas y montos por estas de la venta
        if (cantidadCamisas > 0) {
            System.out.println("Camisas - Cantidad: " + cantidadCamisas + " - Subtotal: $" + subtotalCamisas);
        }

        for (int i = 0; i < productosSeleccionados.size(); i++) {
            Prenda prenda = productosSeleccionados.get(i);
            if (prenda instanceof Camisa){
            System.out.println(prenda.getNombre() + " - Cantidad: " + cantidadCamisas + " - Subtotal: $"
                    + (Camisa.precioVenta() * cantidadCamisas));}
            else if(prenda instanceof Pantalon){
                System.out.println(prenda.getNombre() + " - Cantidad: " + cantidadPantalon + " - Subtotal: $"
                + (Pantalon.PrecioVenta() * cantidadPantalon));}

        System.out.println("Valor total a pagar: $" + MontoPagar);
        System.out.println("Subtotal prendas: $" + venta.getsubtotal());
        System.out.println("IVA: $" + IVA);
        System.out.println("Venta registrada por: " + venta.getEncargado());
        System.out.println("Asesor de la compra: " + venta.getAsesor());
        
        return "El monto total a pagar por parte del cliente es " + MontoPagar + 
       " y el estado final de la cuenta de la sede es $" + bancoTransferir.getAhorroBanco();
        }}

    //Método auxiliar para transferencia de prendas
    private static void manejarFaltantes(Sede sede, int cantidadPrenda, int disponibles, String tipoPrenda, int costosEnvio) { 
    int faltantes = cantidadPrenda - disponibles;

    if (faltantes > 0) {
        costosEnvio += 3000 + (faltantes * 1000);
        System.out.println("Valor de costos de envío: " + costosEnvio);    
        int prendasTransferidas = 0;
        for (Sede otraSede : Sede.getlistaSedes()) {
            if (otraSede != sede) {
                for (int i = 0; i < otraSede.getPrendasInventadas().size() && prendasTransferidas < faltantes; i++) {
                    Prenda prenda = otraSede.getPrendasInventadas().get(i);
                    if (prenda.getNombre().equals(tipoPrenda)) {
                        otraSede.getPrendasInventadas().remove(prenda);
                        sede.getPrendasInventadas().add(prenda);
                        prendasTransferidas++;
                    }
                }
    
                if (prendasTransferidas == faltantes) {
                    break;
                }
            }
        }
    
        if (prendasTransferidas < faltantes) {
            System.out.println("No se pudieron transferir todas las prendas faltantes. Faltan " + (faltantes - prendasTransferidas) + " unidades.");
        }
    }
}


    // Método auxiliar para generar un código ante la compra de una nueva tarjeta de
    // regalo
    private static String generarCodigoAleatorio() {
        Random random = new Random();
        StringBuilder codigo = new StringBuilder();
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        for (int i = 0; i < 8; i++) {
            int indice = random.nextInt(caracteres.length());
            codigo.append(caracteres.charAt(indice));
            while (Venta.getCodigosRegalo().contains(codigo.toString())) {
                codigo.setLength(0); // Limpia el StringBuilder
                for (int e = 0; e < 8; e++) {
                    int index = random.nextInt(caracteres.length());
                    codigo.append(caracteres.charAt(index));
                }
                if (!Venta.getCodigosRegalo().contains(codigo.toString())) {
                    break;
                }
            }
            Venta.getCodigosRegalo().add(codigo.toString());
        }
        return codigo.toString();
    }

    public static void actualizarProveedores() {
        Sede sedeP = null;
        for (Sede sede : Sede.getlistaSedes()) {
            if (sede.getNombre().equals("Sede Principal")) {
                sedeP = sede;
            }
        }
        for (Insumo insumo : sedeP.getListaInsumosBodega()) {
            ArrayList<Proveedor> compatibles = new ArrayList<Proveedor>();
            for (Proveedor prov : Proveedor.getListaProveedores()) {
                if (prov.getInsumo().getNombre().equals(insumo.getNombre())) {
                    compatibles.add(prov);
                }
                ArrayList<Proveedor> nuevos = (ArrayList<Proveedor>) compatibles.clone();
                Collections.shuffle(nuevos);
                for (int i = 0; i < nuevos.size(); i++) {
                    compatibles.get(i).setPrecio(nuevos.get(i).getPrecio());
                }
            }

        }
    }

    // Metodo auxiliar de la interacción 3 de producción
    public static Empleado pedirModista(int cantidadPrendas,Sede sede, int idxTanda){
        System.out.println("Seleccione el modista que se encargará de la tanda #"+idxTanda+ "de producción de "+cantidadPrendas+" prendas:");

        Empleado modista = null;
        ArrayList<Empleado> modistas = new ArrayList<Empleado>();
        for (Empleado empleado : sede.getlistaEmpleados()) {
            if (empleado.getRol() == Rol.MODISTA) {
                System.out.println(modistas.size()+". " + empleado.getNombre());
                modistas.add(empleado);
            }
        }
        Scanner scanner = new Scanner(System.in);
        int seleccion = nextIntSeguro(scanner);
        if (seleccion >= 0 && seleccion < modistas.size()) {
            modista = modistas.get(seleccion);
        } else {
            System.out.println("Opción inválida. Intente nuevamente.");
        }
        return modista;
    }

    public static void printsInt2(int senall){
        if (senall == 1) {
            System.out.println("La Sede 2 no está trabajando por falta de maquinaria disponible...\n");
			System.out.println("1. ¿Desea producir todo hoy desde la Sede Principal?");
			System.out.println("2. ¿Desea producir mañana lo de la Sede 2 desde la sede Principal?");
        } else if(senall == 2){
            System.out.println("\n Marque una opcion correcta entre 1 o 2...\n");
        } else if(senall == 3){
            System.out.println("La Sede Principal no esta trabajando por falta de maquinaria disponible...");
			System.out.println("1. ¿Desea producir todo hoy desde la Sede 2");
			System.out.println("2. ¿Desea producir mañana lo de la Sede Principal desde la sede 2?");
        } else if(senall == 4){
            System.out.println("\n Marque una opcion correcta entre 1 o 2...\n");
        } else if(senall == 5){
            System.out.println("La Sede Principal esta sobrecargada, ¿Que desea hacer? \n");
			System.out.println("1. Enviar parte de la produccion a la Sede 2, para producir por partes iguales.");
			System.out.println("2. Ejecutar produccion, asumiendo todo el costo por sobrecarga en la Sede Principal.");
        } else if(senall == 6){
            System.out.println("Coloca una opcion indicada entre 1 o 2...");
        } else if(senall == 7){
            System.out.println("La Sede 2 esta sobrecargada, ¿Que desea hacer? \n");
			System.out.println("1. Enviar parte de la produccion a la Sede Principal, para producir por partes iguales.");
			System.out.println("2. Ejecutar produccion, asumiendo todo el costo por sobrecarga en la Sede 2.");
        } else if(senall == 8){
            System.out.println("Coloca una opcion indicada entre 1 o 2...");
        } else if(senall == 9){
            System.out.println("Las dos sedes estan sobrecargadas, ¿Que quieres hacer?...");
			System.out.println("1. Producir mañana las prendas que generan sobrecarga.");
			System.out.println("2. Producir todo hoy, asumiendo el costo por sobrecarga.");
        } else if(senall == 10){
            System.out.println("Seleccione una opcion indicada entre 1 o 2...");
        } else if(senall == 11){
            System.out.println("\n Lo sentimos, se debe arreglar la maquinaria en alguna de las dos sedes para comenzar a producir...\n");
        }

    }
}


