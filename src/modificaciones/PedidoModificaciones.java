package modificaciones;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class PedidoModificaciones {
    //Atributos

    private static int numeroPedidos = 0;
    private static ArrayList<Integer> idUsadosList = new ArrayList<>();

    private int idPedido;
    private String nombreCliente;
    private String direccionCliente;
    private ArrayList<ProductoModificaciones> itemsPedido;
    
    //Getters Setters

    public int getIdPedido() {
        return idPedido;
    }

    public String getProductosEnPedido() {

        String productosString = "";

        for (int i = 0; i < itemsPedido.size(); i++) {
            
            ProductoModificaciones itemActual = itemsPedido.get(i);

            String nombreItem = itemActual.getNombre();

            productosString = productosString + i + ") " + nombreItem + "\n";
        }

        return productosString;


    }

    public int getNumeroDeProductosPedido() {
        return itemsPedido.size();
    }

    //Metodos

    public void agregarProducto(ProductoModificaciones nuevoItem) {
        
        itemsPedido.add(nuevoItem);

    }

    private int getPrecioNetoPedido() {
        int precioNeto = 0;

        for (ProductoModificaciones producto : itemsPedido) {
            precioNeto += producto.getPrecio();
        }

        return precioNeto;

    }

    private int getPrecioTotalPedido() {
    
        return getPrecioNetoPedido() + getPrecioIVAPedido();

    }

    private int getPrecioIVAPedido() {
        
        int precioIva = (int)(0.19 * getPrecioNetoPedido());

        return precioIva;
    }

    private String generarTextoFactura() {
        String textoFactura = "Nombre Cliente: " + nombreCliente + " --- Dirección Cliente: " + direccionCliente + " --- Id Pedido: " + idPedido + "\n";

        for (ProductoModificaciones producto : itemsPedido) {
            textoFactura += producto.generarTextoFactura();
        }
        
        textoFactura += "-------------------------------------";
        textoFactura += "\n Precio Neto: " + getPrecioNetoPedido() + "\n Precio IVA: " + getPrecioIVAPedido() + "\n Precio Total: " + getPrecioTotalPedido() + "\n";
        textoFactura += "-------------------------------------\n";
        textoFactura += "Total Calorias: " + getCaloriasNeto();

        return textoFactura;
    }

    private int getCaloriasNeto() {
        
        int caloriasNeto = 0;

        for (ProductoModificaciones producto : itemsPedido) {
            caloriasNeto += producto.getCalorias();
        }

        return caloriasNeto;

    }

    public void guardarFactura(File archivo) {
        try {
            FileWriter myWriter = new FileWriter(archivo);
            String textoFactura = generarTextoFactura();
            myWriter.write(textoFactura);
            myWriter.close();
            System.out.println("Se ha guardado la factura de forma exitosa.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public String consultarPedido() {
        String pedidoTexto = "Nombre Cliente: " + nombreCliente + " --- Dirección Cliente: " + direccionCliente + " --- Id Pedido: " + idPedido + "\n";

        for (ProductoModificaciones producto : itemsPedido) {
            pedidoTexto += producto.getNombre() + "\n";
        }

        return pedidoTexto;
    }

    private int generarId(){

        boolean idEncontrado = true;

        int idNumero = 0;

        while(idEncontrado) {

            String stringId = "";
            Random rand = new Random(); //instance of random class
            int upperbound = 9;

            stringId += (rand.nextInt(8) + 1);
        
            for (int i = 0; i < 6; i++) {
                stringId += rand.nextInt(upperbound);
            }

            idNumero = Integer.parseInt(stringId);

            if (!idUsadosList.contains(idNumero)){
                idEncontrado = false;
                idUsadosList.add(idNumero);
            }
        }

        return idNumero;
        
    }

    public void eliminarProducto(int indiceProducto) {  
        itemsPedido.remove(indiceProducto);
    }

    @Override  
    public boolean equals(Object obj)   
    {  
        if (obj == null)   
        return false;  
        
        if (obj == this)  
        return true;  
        
        else {

            if(this.itemsPedido.size() == ((PedidoModificaciones)obj).itemsPedido.size()){
                boolean todosIguales = true;
                
                for (int i = 0; i < this.itemsPedido.size(); i++) {
                    todosIguales = this.itemsPedido.get(i).equals(((PedidoModificaciones)obj).itemsPedido.get(i));
                    
                }

                return todosIguales;

            }
            else{
                return false;
            }
        }
          
    }  

    //Constructor

    public PedidoModificaciones(String nombreCliente, String direccionCliente) {
        this.nombreCliente = nombreCliente;
        this.direccionCliente = direccionCliente;
        this.idPedido = generarId();
        this.itemsPedido = new ArrayList<>();

        PedidoModificaciones.idUsadosList.add(idPedido);
        PedidoModificaciones.numeroPedidos += 1;
    }
}
