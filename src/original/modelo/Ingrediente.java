package original.modelo;

public class Ingrediente implements Cloneable {
    //Atributos
    private String nombre;
    private int costoAdicional;
    
    //Getters & Setters
    public String getNombre() {
        return nombre;
    }


    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public int getCostoAdicional() {
        return costoAdicional;
    }

    public void setCostoAdicional(int costoAdicional) {
        this.costoAdicional = costoAdicional;
    }

    //Constructor 
    
    public Ingrediente(String nombre, int costoAdicional) {
        this.nombre = nombre;
        this.costoAdicional = costoAdicional;
    }    

    public Object clone()throws CloneNotSupportedException{  
        return super.clone();  
    }  

}
