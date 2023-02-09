package modificaciones;

public class Bebida implements ProductoModificaciones, Cloneable{

    private int precio;
    private String nombre;
    private int calorias;


    @Override
    public int getPrecio() {
        return precio;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public String generarTextoFactura() {
        String textoFactura = nombre + ": " + precio + " --- Cal: "+ calorias + "\n";
        return textoFactura;
    }

    @Override
    public int getCalorias() {
        return calorias;
    }

    public Object clone()throws CloneNotSupportedException{  
        return super.clone();  
    }

    public Bebida(String nombre, int precio, int calorias) {
        this.precio = precio;
        this.nombre = nombre;
        this.calorias = calorias;
    }
    
}
