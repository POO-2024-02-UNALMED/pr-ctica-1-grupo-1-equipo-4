package gestorAplicacion.Bodega;

public enum Repuesto {
	AGUJASMC(12),   //MC significa que es de la máquina de coser
    ACEITEMC(60),
    PIEPRENSATELAMC(1000),
    CORREADETRANSMISIONMC(3000),
    CUCHILLASMCORTE(60),    //MCORTE significa que es de la máquina de corte
    AFILADORESMCORTE(750),
    RODAMIENTOSMCORTE(1500),
    RESISTENCIAELECTRICAPI(1500),   //PI significa que es de la plancha industrial
    MANGUERADEVAPORPI(750),
    AGUJASBI(25),       // BI significa que es de la Bordadora Industrial
    ACEITEBI(75),
    BANDASDETRANSMISIONMT(2500),        //MT signigica que es de la Maquina de Termofijado
    RESISTENCIASELECTRICASMT(1750),
    CUCHILLASMTIJEREADO(70),      //MTIJEREADO signigica que es de la Máquina de Tijereado
    ACEITEMTIJEREADO(50);


    private final int horasDeVidaUtil;

    Repuesto(int horasDeVidaUtil){
        this.horasDeVidaUtil = horasDeVidaUtil;
    }

    public int getHorasDeVidaUtil(){
        return horasDeVidaUtil;
    }

    public float calcularGastoMensual(){
        return 0.0F;
    }
}