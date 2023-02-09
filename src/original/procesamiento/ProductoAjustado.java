package original.procesamiento;

import java.util.ArrayList;

import original.modelo.Ingrediente;
import original.modelo.Producto;
import original.modelo.ProductoMenu;

public class ProductoAjustado implements Producto{
    //Atributos 

    private ProductoMenu base;
    private ArrayList<Ingrediente> agregados;
    private ArrayList<Ingrediente> eliminados;


    //Constructor

    public ProductoAjustado(ProductoMenu base) {
        this.base = base;

        this.agregados = new ArrayList<>();
        this.eliminados = new ArrayList<>();

    }


    @Override
    public int getPrecio() {
        int costoAdicional = base.getPrecio();


        for (Ingrediente ingrediente : agregados) {
            costoAdicional += ingrediente.getCostoAdicional();
        }

        return costoAdicional;
    }


    @Override
    public String getNombre() {
        return base.getNombre();
    }


    @Override
    public String generarTextoFactura() {

        String textoFactura = base.getNombre() + ": " + base.getPrecio() + "\n";

        if(agregados.size() > 0){
            textoFactura += "Con adición de: \n";
            
            for (Ingrediente ingrediente : agregados) {
                textoFactura += "---" + ingrediente.getNombre() + ": " + ingrediente.getCostoAdicional() + "---\n";
            }
        }

        if (eliminados.size() > 0) {
            textoFactura += "Sin: \n";
            for (Ingrediente ingrediente : eliminados) {
                textoFactura += "---" + ingrediente.getNombre() + "---\n";
            }
        }

        return textoFactura;
    }


	public void agregarIngrediente(Ingrediente ingrediente) {
        agregados.add(ingrediente);
    }


    public void quitarIngrediente(Ingrediente ingrediente) {
        eliminados.add(ingrediente);
    }

}
