package iu.swing;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.swing.JTextField;
import medios.AtributoMedio;
import medios.EnumTiposAtributo;
import medios.MediosFactory;
import medios.MedioIF;

/**
 * Widget que permite a un administrador añadir un nuevo medio al sistema.
 * 
 * @author Héctor Luaces Novo
 */
class WidgetNuevoMedio extends AbstractWidgetMedio {

    /**
     * Permite crear un nuevo WidgetNuevoMedio inyectándole un controladorSwing
     * @param c 
     */
    public WidgetNuevoMedio(ControladorSwing c) {
        super(c);
        
        this.setTitle("Alta de nuevo medio");
    }

    @Override
    protected Set<AtributoMedio> dameAtributosParaCampos(String tipo) {
        Set<AtributoMedio> ret;
        
        ret = super.dameAtributosParaCampos(tipo);
        ret.removeIf((c) -> c.getNombre() == EnumTiposAtributo.ESTADO );
        ret.removeIf((c) -> c.getNombre() == EnumTiposAtributo.BIBLIOTECA);
        return ret;
    }

    
    @Override
    protected void ejecutar() {
        Set<AtributoMedio> atributos = new HashSet<>();
        MedioIF m;
        
        for(Map.Entry<EnumTiposAtributo, JTextField> a: this.controlesSecundarios.entrySet()) {
            atributos.add(new AtributoMedio(
                    a.getKey(), a.getValue().getText()
            ));
        }
        
        atributos.add(new AtributoMedio(
            EnumTiposAtributo.AUTOR, this.txtAutor.getText()
        ));
        atributos.add(new AtributoMedio(
            EnumTiposAtributo.GENERO, this.txtGenero.getText()
        ));
        atributos.add(new AtributoMedio(
            EnumTiposAtributo.TITULO, this.txtTitulo.getText()
        ));
        atributos.add(new AtributoMedio(
            EnumTiposAtributo.TIPO, this.comboTipo.getSelectedItem().toString()
        ));
        atributos.add(new AtributoMedio(
            EnumTiposAtributo.FECHA, this.txtFecha.getText()
        ));
        
        m = MediosFactory.getMedio(atributos);
        
        if ( m == null ) {
            return;
        }
        
        this.controlador
            .getAplicacion()
            .getBibliotecaActiva()
            .addMedio(m)
        ;
        this.controlador.actualizaEscritorio();
        this.controlador.cerrarFrame(this);
    }
}
