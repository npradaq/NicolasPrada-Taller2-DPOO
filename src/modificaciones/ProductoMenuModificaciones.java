package modificaciones;

public class ProductoMenuModificaciones implements ProductoModificaciones, Cloneable{
    //Atributos

    private String nombre;
    private int precioBase;
    private int calorias;
    
    //Getters & Setters
    
    public String getNombre() {
        return nombre;
    }

    //Metodos

    public String generarTextoFactura() {     
        String textoFactura = nombre + ": " + precioBase + " --- Cal: "+ calorias + "\n";
        return textoFactura;
    }

    //Generator

    public ProductoMenuModificaciones(String nombre, int precioBase, int calorias) {
        this.nombre = nombre;
        this.precioBase = precioBase;
        this.calorias = calorias;
    }

    public Object clone()throws CloneNotSupportedException{  
        return super.clone();  
    }

    @Override
    public int getPrecio() {
        return precioBase;
    }

    @Override
    public int getCalorias() {
        return calorias;
    }  

}
