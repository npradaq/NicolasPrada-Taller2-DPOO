package modificaciones;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Set;

public class RestauranteModificaciones {
    //Atributos

    private HashMap<Integer, PedidoModificaciones> pedidos; 
    private PedidoModificaciones pedidoEnCurso;
    
    private HashMap<String, IngredienteModificaciones> ingredientes;
    private HashMap<String, ProductoMenuModificaciones> menuBase;
    private HashMap<String, ComboModificaciones> combos;
    private HashMap<String, Bebida> bebidas;

    private String menuTexto;
    private String menuIngredientes;
    private HashMap<Integer, String> mapaOpcionesMenu;
    private HashMap<Integer, String> mapaOpcionesIngredientes;

    private int numeroMenu;
    private int numeroIngredientes;
    

    //Metodos

    public void iniciarPedido(String nombreCliente, String direccionCliente) throws CloneNotSupportedException {
      
        pedidoEnCurso = new PedidoModificaciones(nombreCliente, direccionCliente);

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

                Boolean pedidoSimilar = false;
                int idPedidoSimilar = 0;
                Set<Integer> llaves = pedidos.keySet();


                for (Integer llave : llaves) {
                    PedidoModificaciones pedidoPrueba = pedidos.get(llave);
                    pedidoSimilar = comprobarPedidosIguales(pedidoEnCurso, pedidoPrueba);
                    if(pedidoSimilar) {
                        idPedidoSimilar = pedidoPrueba.getIdPedido();
                        break;
                    }
                }

                if(pedidoSimilar) {
                    System.out.println("\nEl pedido actual es igual al pedido con ID " + idPedidoSimilar + "\n");
                }

                else {
                    System.out.println("\nEl pedido actual no es igual a ningún otro pedido cerrado\n");
                }

                pedidos.put(idPedidoActual, pedidoEnCurso);
                pedidoEnCurso = null;

            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }

    }

    public void cargarInformacionRestaurante(File archivoIngredientes, File archivoMenu, File archivoCombos, File archivoBebidas) throws FileNotFoundException, IOException, NumberFormatException, CloneNotSupportedException {

        cargarMenu(archivoMenu);
        cargarBebidas(archivoBebidas);
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
            int calorias = Integer.parseInt(partes[2]);
            
            IngredienteModificaciones nuevoIngrediente = new IngredienteModificaciones(nombreIngrediente, precio, calorias);

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
            int calorias = Integer.parseInt(partes[2]);

            
            ProductoMenuModificaciones nuevoProducto = new ProductoMenuModificaciones(nombreProducto, precio, calorias);

            menuBase.put(nombreProducto, nuevoProducto);

            menuTexto = menuTexto + numeroMenu +") " + nombreProducto + ": " + precio + "\n";
            mapaOpcionesMenu.put(numeroMenu, nombreProducto);

            linea = br.readLine();
            numeroMenu++;
        }
        
        br.close();
    }
    
    private void cargarCombos(File archivoCombos) throws FileNotFoundException, IOException, NumberFormatException, CloneNotSupportedException {
        BufferedReader br = new BufferedReader(new FileReader(archivoCombos));
		String linea = br.readLine();
		linea = br.readLine();

        //Crear combos
        menuTexto = menuTexto + "\n***Combos***\n";

        while(linea != null) {
            String[] partes = linea.split(";");
            String nombreCombo = partes[0];
            double descuento = Double.parseDouble(partes[1].replace("%","")) / 100;

            ComboModificaciones nuevoCombo = new ComboModificaciones(nombreCombo, descuento);

            String contenidos = "";

            for (int i = 2; i < partes.length; i++) {
                String nombreProducto = partes[i];

                if(menuBase.containsKey(nombreProducto)){
                    ProductoMenuModificaciones productoOriginal = menuBase.get(nombreProducto);

                    ProductoMenuModificaciones productoAgregar = (ProductoMenuModificaciones) productoOriginal.clone();

                    nuevoCombo.agregarItemACombo(productoAgregar);

                    contenidos = contenidos + nombreProducto + "\n";
                }
                else if (bebidas.containsKey(nombreProducto)) {
                    Bebida bebidaOriginal = bebidas.get(nombreProducto);

                    Bebida bebidaAgregar = (Bebida) bebidaOriginal.clone();
                    
                    nuevoCombo.agregarItemACombo(bebidaAgregar);

                    contenidos = contenidos + nombreProducto + "\n";
                }
                
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

    private void cargarBebidas(File archivoBebidas) throws FileNotFoundException, IOException, NumberFormatException {
        BufferedReader br = new BufferedReader(new FileReader(archivoBebidas));
		String linea = br.readLine();
		linea = br.readLine();

        //crear bebidas
        menuTexto = menuTexto + "\n***Bebidas***\n";

        while (linea != null) {
            String[] partes = linea.split(";");
            String nombreBebida = partes[0];
            int precio = Integer.parseInt(partes[1]);
            int calorias = Integer.parseInt(partes[2]);
            
            Bebida nuevaBebida = new Bebida(nombreBebida, precio, calorias);

            bebidas.put(nombreBebida, nuevaBebida);

            menuTexto = menuTexto + numeroMenu +") " + nombreBebida + ": " + precio + "\n";
            mapaOpcionesMenu.put(numeroMenu, nombreBebida);

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

        if (numProducto > numeroMenu || numProducto < 1){
            System.out.println("El numero ingresado no tiene un producto asociado");
        }

        else {
            String nombreProducto = mapaOpcionesMenu.get(numProducto);

            if(menuBase.containsKey(nombreProducto)){

                ProductoMenuModificaciones productoMenuBase = menuBase.get(nombreProducto);

                ProductoMenuModificaciones clonProducto = (ProductoMenuModificaciones)productoMenuBase.clone();

                boolean agregarEliminar = true;

                while (agregarEliminar){

                    String respuesta = input("Desea agregarle o eliminar alguno de los ingredientes al producto? (y/n)");

                    System.out.println(respuesta);

                    if(respuesta.equals("y") || respuesta.equals("Y")){

                        ProductoAjustadoModificaciones clonAjustado = new ProductoAjustadoModificaciones(clonProducto);

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
                                        IngredienteModificaciones ingredienteBase = ingredientes.get(nombreIngrediente);

                                        IngredienteModificaciones clonIngrediente = (IngredienteModificaciones)ingredienteBase.clone();

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
                                        IngredienteModificaciones ingredienteBase = ingredientes.get(nombreIngrediente);

                                        IngredienteModificaciones clonIngrediente = (IngredienteModificaciones)ingredienteBase.clone();

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

            else if(bebidas.containsKey(nombreProducto)){
                Bebida productoBebida = bebidas.get(nombreProducto);

                Bebida clonBebida = (Bebida)productoBebida.clone();

                pedidoEnCurso.agregarProducto(clonBebida);
            }

            else if(combos.containsKey(nombreProducto)){

                ComboModificaciones productoCombo = combos.get(nombreProducto);

                ComboModificaciones clonCombo = (ComboModificaciones)productoCombo.clone();

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
            
            PedidoModificaciones pedidoConsultar = pedidos.get(idPedido);

            System.out.println(pedidoConsultar.consultarPedido());

        }

        else {
            System.out.println("No se encontró ningún pedido con ese id");


        }


    }

    private boolean comprobarPedidosIguales(PedidoModificaciones pedidoActual, PedidoModificaciones pedidoPrueba) {
        return pedidoActual.equals(pedidoPrueba);
    }


    //Constructor

    public RestauranteModificaciones() {
        this.numeroMenu = 0;
        this.numeroIngredientes = 0;

        this.combos = new HashMap<>();
        this.ingredientes = new HashMap<>();
        this.menuBase = new HashMap<>();
        this.bebidas = new HashMap<>();
        this.mapaOpcionesIngredientes = new HashMap<>();
        this.mapaOpcionesMenu = new HashMap<>();
        this.pedidos = new HashMap<>();
        
        this.menuIngredientes = "";
        this.menuTexto = "";

    }

}