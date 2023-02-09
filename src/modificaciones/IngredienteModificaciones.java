package modificaciones;

public class IngredienteModificaciones implements Cloneable {
    //Atributos
    private String nombre;
    private int costoAdicional;
    private int calorias;
    
    //Getters & Setters
    public String getNombre() {
        return nombre;
    }


    public int getCostoAdicional() {
        return costoAdicional;
    }

    public int getCalorias() {
        return calorias;
    }  

    //Constructor 
    
    public IngredienteModificaciones(String nombre, int costoAdicional, int calorias) {
        this.nombre = nombre;
        this.costoAdicional = costoAdicional;
        this.calorias = calorias;
    }    

    public Object clone()throws CloneNotSupportedException{  
        return super.clone();  
    }


    

}
