/*Equipo 4 grupo 1
 *  Representa el resultado de una busqueda de un insumo en una sede
 */
package src.gestorAplicacion.Administracion;
import src.gestorAplicacion.Sede;

public class Resultado {
    // Resultado de una busqueda de un Insumo en una Sede.

    private boolean encontrado;
    private int index;
    private int precio;
    private Sede sede;

    public Resultado(){}
    public Resultado(boolean encontrado, int indice) {
        this.encontrado = encontrado;
        this.index = indice;
    }
    public Resultado(boolean encontrado, int indice, Sede sede, int precio) {
        this.encontrado = encontrado;
        this.index = indice;
        this.sede = sede;
        this.precio = precio;
    }

    public int getPrecio(){
        return precio;
    } 
	public void setPrecio(int precio){
        this.precio=precio;
    } 
    public boolean getEncontrado(){
        return encontrado;
    } 
	public void setEncontrado(boolean enc){
        this.encontrado=enc;
    } 
    public int getIndex(){
        return index;
    } 
	public void setIndex(int index){
        this.index=index;
    } 
    public Sede getSede(){
        return sede;
    } 
	public void setSede(Sede sede){
        this.sede=sede;
    } 

}
