package original.modelo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Pedido {
    //Atributos

    private static int numeroPedidos = 0;
    private static ArrayList<Integer> idUsadosList = new ArrayList<>();

    private int idPedido;
    private String nombreCliente;
    private String direccionCliente;
    private ArrayList<Producto> itemsPedido;
    
    //Getters Setters

    public int getIdPedido() {
        return idPedido;
    }

    public String getProductosEnPedido() {

        String productosString = "";

        for (int i = 0; i < itemsPedido.size(); i++) {
            
            Producto itemActual = itemsPedido.get(i);

            String nombreItem = itemActual.getNombre();

            productosString = productosString + i + ") " + nombreItem + "\n";
        }

        return productosString;


    }

    public int getNumeroDeProductosPedido() {
        return itemsPedido.size();
    }

    //Metodos

    public void agregarProducto(Producto nuevoItem) {
        
        itemsPedido.add(nuevoItem);

    }

    private int getPrecioNetoPedido() {
        int precioNeto = 0;

        for (Producto producto : itemsPedido) {
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

        for (Producto producto : itemsPedido) {
            textoFactura += producto.generarTextoFactura();
        }
        
        textoFactura += "-------------------------------------";
        textoFactura += "\n Precio Neto: " + getPrecioNetoPedido() + "\n Precio IVA: " + getPrecioIVAPedido() + "\n Precio Total: " + getPrecioTotalPedido() + "\n";

        return textoFactura;
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

        for (Producto producto : itemsPedido) {
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

    //Constructor

    public Pedido(String nombreCliente, String direccionCliente) {
        this.nombreCliente = nombreCliente;
        this.direccionCliente = direccionCliente;
        this.idPedido = generarId();
        this.itemsPedido = new ArrayList<>();

        Pedido.idUsadosList.add(idPedido);
        Pedido.numeroPedidos += 1;
    }
}
