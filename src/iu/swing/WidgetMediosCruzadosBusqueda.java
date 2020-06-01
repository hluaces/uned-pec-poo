package iu.swing;

import busqueda.Filtro;

/**
 * Widget que especializa {@link WidgerBuscarMedio} para realizar una búsqueda
 * cruzada y no una búsqueda normal.
 * 
 * @author Héctor Luaces Novo
 */
public class WidgetMediosCruzadosBusqueda extends WidgetBuscarMedio {

    public WidgetMediosCruzadosBusqueda(ControladorSwing c) {
        super(c);
        
        this.setTitle("Búsqueda cruzada de medios");
    }
    
    @Override
    protected void ejecutar() {
        WidgetMediosCruzados wg;
        Filtro f;
        
        f  = this.getFiltroFromCampos();
        wg = new WidgetMediosCruzados(this.controlador, f);
        
        this.controlador.cerrarFrame(this);
        this.controlador.addPanel(wg);
    }
    
}
