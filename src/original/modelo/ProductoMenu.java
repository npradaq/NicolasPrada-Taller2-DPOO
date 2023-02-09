package original.modelo;

public class ProductoMenu implements Producto, Cloneable{
    //Atributos

    private String nombre;
    private int precioBase;
    
    //Getters & Setters
    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPrecioBase() {
        return precioBase;
    }

    public void setPrecioBase(int precioBase) {
        this.precioBase = precioBase;
    } 

    //Metodos

    public String generarTextoFactura() {     
        String textoFactura = nombre + ": " + precioBase + "\n";
        return textoFactura;
    }

    //Generator

    public ProductoMenu(String nombre, int precioBase) {
        this.nombre = nombre;
        this.precioBase = precioBase;
    }

    public Object clone()throws CloneNotSupportedException{  
        return super.clone();  
    }

    @Override
    public int getPrecio() {
        return precioBase;
    }  

}
