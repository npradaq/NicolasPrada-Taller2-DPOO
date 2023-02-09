package original.modelo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;

import original.procesamiento.Combo;
import original.procesamiento.ProductoAjustado;

public class Restaurante {
    //Atributos

    private HashMap<Integer, Pedido> pedidos; 
    private Pedido pedidoEnCurso;
    
    private HashMap<String, Ingrediente> ingredientes;
    private HashMap<String, ProductoMenu> menuBase;
    private HashMap<String, Combo> combos;

    private String menuTexto;
    private String menuIngredientes;
    private HashMap<Integer, String> mapaOpcionesMenu;
    private HashMap<Integer, String> mapaOpcionesIngredientes;

    private int numeroMenu;
    private int numeroIngredientes;
    

    //Metodos

    public void iniciarPedido(String nombreCliente, String direccionCliente) throws CloneNotSupportedException {
      
        pedidoEnCurso = new Pedido(nombreCliente, direccionCliente);

        boolean haciendoPedido = true;

        while(haciendoPedido){

            imprimirOpciones();

            int opcion_seleccionada = Integer.parseInt(input("Por favor seleccione una opción"));

            System.out.println();
            if (opcion_seleccionada == 1) {
                imprimirMenuTexto();
            }
    
            else if (opcion_seleccionada == 2) {
                agregarProducto();
            }
    
            else if (opcion_seleccionada == 3) {
                quitarProducto();
            }
    
            else if (opcion_seleccionada == 4) {
                haciendoPedido = false;
                cerrarYGuardarPedidoEnCurso();
            }

            else{
                System.out.println("\n La opción seleccionada no es válida \n");
            }

        }


    }

    public void cerrarYGuardarPedidoEnCurso() {
        
        if(pedidoEnCurso != null){
            int idPedidoActual = pedidoEnCurso.getIdPedido();
            String nombreArchivo = "data/Facturas/Factura-" + idPedidoActual + ".txt";

            try {
                File myObj = new File(nombreArchivo);
                
                if (myObj.createNewFile()) {
                    pedidoEnCurso.guardarFactura(myObj);
                } 
                
                else {
                    PrintWriter writer = new PrintWriter(myObj);
                    writer.print("");
                    writer.close();
                    pedidoEnCurso.guardarFactura(myObj);

                }

                pedidos.put(idPedidoActual, pedidoEnCurso);
                pedidoEnCurso = null;

            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }

    }

    public void cargarInformacionRestaurante(File archivoIngredientes, File archivoMenu, File archivoCombos) throws FileNotFoundException, IOException, NumberFormatException {

        cargarMenu(archivoMenu);
        cargarCombos(archivoCombos);
        CargarIngredientes(archivoIngredientes);
    }

    private void CargarIngredientes(File archivoIngredientes) throws FileNotFoundException, IOException, NumberFormatException {
        BufferedReader br = new BufferedReader(new FileReader(archivoIngredientes));
		String linea = br.readLine();
		linea = br.readLine();

        //crear lista ingredientes
        menuIngredientes = menuIngredientes + "\n***Ingredientes Extra***\n";

        while (linea != null) {
            String[] partes = linea.split(";");
            String nombreIngrediente = partes[0];
            int precio = Integer.parseInt(partes[1]);
            
            Ingrediente nuevoIngrediente = new Ingrediente(nombreIngrediente, precio);

            ingredientes.put(nombreIngrediente, nuevoIngrediente);

            menuIngredientes = menuIngredientes + numeroIngredientes +") " + nombreIngrediente + ": " + precio + "\n";
            
            mapaOpcionesIngredientes.put(numeroIngredientes, nombreIngrediente);

            linea = br.readLine();
            numeroIngredientes ++;
        }

        br.close();

    }

    private void cargarMenu(File archivoMenu) throws FileNotFoundException, IOException, NumberFormatException {
        
        BufferedReader br = new BufferedReader(new FileReader(archivoMenu));
		String linea = br.readLine();
		linea = br.readLine();

        //crear menú base
        menuTexto = menuTexto + "***Productos***\n";

        while (linea != null) {
            String[] partes = linea.split(";");
            String nombreProducto = partes[0];
            int precio = Integer.parseInt(partes[1]);
            
            ProductoMenu nuevoProducto = new ProductoMenu(nombreProducto, precio);

            menuBase.put(nombreProducto, nuevoProducto);

            menuTexto = menuTexto + numeroMenu +") " + nombreProducto + ": " + precio + "\n";
            mapaOpcionesMenu.put(numeroMenu, nombreProducto);

            linea = br.readLine();
            numeroMenu++;
        }
        
        br.close();
    }
    
    private void cargarCombos(File archivoCombos) throws FileNotFoundException, IOException, NumberFormatException {
        BufferedReader br = new BufferedReader(new FileReader(archivoCombos));
		String linea = br.readLine();
		linea = br.readLine();

        //Crear combos
        menuTexto = menuTexto + "\n***Combos***\n";

        while(linea != null) {
            String[] partes = linea.split(";");
            String nombreCombo = partes[0];
            double descuento = Double.parseDouble(partes[1].replace("%","")) / 100;

            Combo nuevoCombo = new Combo(nombreCombo, descuento);

            String contenidos = "";

            for (int i = 2; i < partes.length; i++) {
                String nombreProducto = partes[i];
                ProductoMenu productoAgregar = menuBase.get(nombreProducto);

                nuevoCombo.agregarItemACombo(productoAgregar);

                contenidos = contenidos + nombreProducto + "\n";
            }

            combos.put(nuevoCombo.getNombre(), nuevoCombo);

            int precioCombo = nuevoCombo.getPrecio();

            menuTexto = menuTexto + numeroMenu +") " + nombreCombo + ": " + precioCombo + "\n" + contenidos + "\n";
            mapaOpcionesMenu.put(numeroMenu, nombreCombo);

            linea = br.readLine();
            numeroMenu++;

        }

        br.close();


    }

    private void imprimirOpciones() {
        System.out.println("\n");
        System.out.println("1) Consultar Menú");
        System.out.println("2) Agregar Producto");
        System.out.println("3) Quitar Producto");
        System.out.println("4) Cerrar Pedido");

    }

    public void imprimirMenuTexto() {
        System.out.println(menuTexto);
    }

    public String input(String mensaje)
	{
		try
		{
			System.out.print(mensaje + ": ");
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			return reader.readLine();
		}
		catch (IOException e)
		{
			System.out.println("Error leyendo de la consola");
			e.printStackTrace();
		}
		return null;
	}

    private void agregarProducto() throws CloneNotSupportedException {
        
        int numProducto = Integer.parseInt(input("Que producto desea agregar? "));

        if (numProducto > numeroMenu || numProducto < 0){
            System.out.println("El numero ingresado no tiene un producto asociado");
        }

        else {
            String nombreProducto = mapaOpcionesMenu.get(numProducto);

            if(menuBase.containsKey(nombreProducto)){

                ProductoMenu productoMenuBase = menuBase.get(nombreProducto);

                ProductoMenu clonProducto = (ProductoMenu)productoMenuBase.clone();

                boolean agregarEliminar = true;

                while (agregarEliminar){

                    String respuesta = input("Desea agregarle o eliminar alguno de los ingredientes al producto? (y/n)");

                    System.out.println(respuesta);

                    if(respuesta.equals("y") || respuesta.equals("Y")){

                        ProductoAjustado clonAjustado = new ProductoAjustado(clonProducto);

                        while(agregarEliminar){

                            int opcion = Integer.parseInt(input("1) Agregar ingrediente \n2) Quitar ingrediente \n3) Terminar de agregar y eliminar ingredientes"));

                            if (opcion == 1){
                                System.out.println("---Menú Ingredientes---");
                                System.out.print(menuIngredientes);
                                System.out.println();

                                boolean opcionCorrecta = true;

                                while(opcionCorrecta){

                                    int opcionIngrediente = Integer.parseInt(input("ingrese el numero de ingrediente que desea agregar"));

                                    if (opcionIngrediente > numeroIngredientes || opcionIngrediente < 0) {
                                        System.out.println("La opción ingresada no es válida");
                                    }

                                    else {
                                        opcionCorrecta = false;

                                        String nombreIngrediente = mapaOpcionesIngredientes.get(opcionIngrediente);
                                        Ingrediente ingredienteBase = ingredientes.get(nombreIngrediente);

                                        Ingrediente clonIngrediente = (Ingrediente)ingredienteBase.clone();

                                        clonAjustado.agregarIngrediente(clonIngrediente);

                                    }

                                }
                                
                            }

                            else if (opcion == 2){
                                System.out.println("---Menú Ingredientes---");
                                System.out.print(menuIngredientes);
                                System.out.println();

                                boolean opcionCorrecta = true;

                                while(opcionCorrecta){

                                    int opcionIngrediente = Integer.parseInt(input("ingrese el numero de ingrediente que desea agregar"));

                                    if (opcionIngrediente > numeroIngredientes || opcionIngrediente < 0) {
                                        System.out.println("Opción escogida no es válida");
                                    }

                                    else {
                                        opcionCorrecta = false;

                                        String nombreIngrediente = mapaOpcionesIngredientes.get(opcionIngrediente);
                                        Ingrediente ingredienteBase = ingredientes.get(nombreIngrediente);

                                        Ingrediente clonIngrediente = (Ingrediente)ingredienteBase.clone();

                                        clonAjustado.quitarIngrediente(clonIngrediente);

                                    }
                                
                                }

                            }

                            else if (opcion == 3) {
                                agregarEliminar = false;

                                pedidoEnCurso.agregarProducto(clonAjustado);

                                System.out.println("Su producto se ha agregado a su pedido");  
                            }

                            else{
                                System.out.println("\nLa opción que ingresó no es válida\n"); 
                            }


                        }


                    }

                    else if (respuesta.equals("n")|| respuesta.equals("N")){
                        agregarEliminar = false;

                        pedidoEnCurso.agregarProducto(clonProducto);
                    }

                    else {
                        System.out.println("\nLa opción que ingresó no es válida\n");
                    }

                }
                
            }

            else if(combos.containsKey(nombreProducto)){

                Combo productoCombo = combos.get(nombreProducto);

                Combo clonCombo = (Combo)productoCombo.clone();

                pedidoEnCurso.agregarProducto(clonCombo);

            }

        }

    }

    private void quitarProducto() {

        int numeroProductosPedido = pedidoEnCurso.getNumeroDeProductosPedido();

        if(numeroProductosPedido > 0) {
            boolean proceso = true;
            while(proceso) {
                String listaProductos = pedidoEnCurso.getProductosEnPedido();

                System.out.println(listaProductos);
                int opcionProducto = Integer.parseInt(input("Ingrese el numero del producto que desea eliminar"));

                if (opcionProducto >= numeroProductosPedido || opcionProducto < 0) {
                    System.out.println("Opción escogida no es válida");
                }

                else{
                    proceso = false;
                    pedidoEnCurso.eliminarProducto(opcionProducto);
                    System.out.println("Producto eliminado exitosamente");
                }

            }
        }

        else {
            System.out.println("El pedido en curso actual no tiene ningún producto");
        }

    }

    public void consultarPedido(int idPedido) {

        if(pedidos.containsKey(idPedido)) {
            
            Pedido pedidoConsultar = pedidos.get(idPedido);

            System.out.println(pedidoConsultar.consultarPedido());

        }

        else {
            System.out.println("No se encontró ningún pedido con ese id");


        }


    }

    //Constructor

    public Restaurante() {
        this.numeroMenu = 0;
        this.numeroIngredientes = 0;

        this.combos = new HashMap<>();
        this.ingredientes = new HashMap<>();
        this.menuBase = new HashMap<>();
        this.mapaOpcionesIngredientes = new HashMap<>();
        this.mapaOpcionesMenu = new HashMap<>();
        this.pedidos = new HashMap<>();
        
        this.menuIngredientes = "";
        this.menuTexto = "";

    }

}