package modificaciones;

import java.util.ArrayList;

public class ProductoAjustadoModificaciones implements ProductoModificaciones{
    //Atributos 

    private ProductoMenuModificaciones base;
    private ArrayList<IngredienteModificaciones> agregados;
    private ArrayList<IngredienteModificaciones> eliminados;


    //Constructor

    public ProductoAjustadoModificaciones(ProductoMenuModificaciones base) {
        this.base = base;

        this.agregados = new ArrayList<>();
        this.eliminados = new ArrayList<>();

    }


    @Override
    public int getPrecio() {
        int costoAdicional = base.getPrecio();


        for (IngredienteModificaciones ingrediente : agregados) {
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

        String textoFactura = base.generarTextoFactura();

        if(agregados.size() > 0){
            textoFactura += "Con adición de: \n";
            
            for (IngredienteModificaciones ingrediente : agregados) {
                textoFactura += " ---" + ingrediente.getNombre() + ": " + ingrediente.getCostoAdicional() + "---\n";
            }
        }

        if (eliminados.size() > 0) {
            textoFactura += "Sin: \n";
            for (IngredienteModificaciones ingrediente : eliminados) {
                textoFactura += "---" + ingrediente.getNombre() + "---\n";
            }
        }

        return textoFactura;
    }


	public void agregarIngrediente(IngredienteModificaciones ingrediente) {
        agregados.add(ingrediente);
    }


    public void quitarIngrediente(IngredienteModificaciones ingrediente) {
        eliminados.add(ingrediente);
    }


    @Override
    public int getCalorias() {
        int caloriasReturn = base.getCalorias();

        for (IngredienteModificaciones ingredienteModificaciones : agregados) {
            caloriasReturn += ingredienteModificaciones.getCalorias();
        }

        for (IngredienteModificaciones ingredienteModificaciones : eliminados) {
            caloriasReturn -= ingredienteModificaciones.getCalorias();
        }


        return caloriasReturn;
    }

}
