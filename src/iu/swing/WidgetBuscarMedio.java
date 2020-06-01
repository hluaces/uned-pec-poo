package iu.swing;

import busqueda.Criterio;
import busqueda.Filtro;
import java.util.Map;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import medios.EnumTiposAtributo;

/**
 * Widget que extiende a {@link AbstractWidgetMedio} para mostrar un diálogo
 * que permitirá a un usuario buscar un Medio en la biblioteca.
 * 
 * @author Héctor Luaces Novo
 */
public class WidgetBuscarMedio extends AbstractWidgetMedio {
    /**
     * Éste widget se diferencia del anterior en éste checkbox, que nos 
     * permitirá crear un {@link Filtro} de búsqueda {@link Filtro#absoluto 
     * absoluto} si está marcada.
     */
    private JCheckBox absoluto;
    
    /**
     * Constructor principal de la clase.
     * 
     * @param c ControladorSwing a inyectar en el widget.
     */
    public WidgetBuscarMedio(ControladorSwing c) {
        super(c);

        this.botonOk.setText("Buscar");
        this.setTitle("Búsqueda de medios");
    }

    @Override
    public void inicializarCombo() {
        super.inicializarCombo(); 
        
        this.comboTipo.addItem("Cualquiera");
        this.comboTipo.setSelectedItem("Cualquiera");
    }
    
            
    @Override
    protected void generarNuevosAtributos() {
        if ( this.comboTipo.getSelectedItem() != "Cualquiera" )
            super.generarNuevosAtributos();
      
        
        if ( this.absoluto == null ) {
            absoluto = new JCheckBox("Cumplir todos los criterios");
            absoluto.setSelected(true);
        }
        
        this.panelAtributosSecundarios.add(absoluto);
        this.panelAtributosSecundarios.revalidate();
        this.pack();
    }

    /**
     * Crea un {@link Filtro} de búsqueda en base a los datos introducidos
     * en los campos del Widget
     * 
     * @return Un filtro de búsqueda acorde a lo que hayamos escrito
     * en los campos del Widget
     */
    protected Filtro getFiltroFromCampos() {
        Filtro f = new Filtro(this.absoluto.isSelected());
        String txt;
        
        // Recorremos todos los textField, creando un criterio con todos aquellos
        // que no estén vacíos
        for(Map.Entry<EnumTiposAtributo, JTextField> a: this.controlesSecundarios.entrySet()) {
            txt = a.getValue().getText().trim();
            
            if ( txt.isEmpty() )
                continue;
            
            f.addCriterio(new Criterio(
                a.getKey().getNombre(),
                txt
            ));
        }
        
        // Al filtro ahora vamos a añadirle los criterios de los atributos
        // primarios
        
        // "Cualquiera" significa que NO añadiremos un filtro de éste criterio
        if ( ! this.comboTipo.getSelectedItem().equals("Cualquiera") )
            f.addCriterio(new Criterio(
                EnumTiposAtributo.TIPO.getNombre(),
                this.comboTipo.getSelectedItem().toString())
            );
        
        txt = this.txtTitulo.getText().trim();
        if ( ! txt.isEmpty() )
            f.addCriterio(new Criterio(
                EnumTiposAtributo.TITULO.getNombre(),
                txt
            ));
        
        txt = this.txtAutor.getText().trim();
        if ( ! txt.isEmpty() )
            f.addCriterio(new Criterio(
                EnumTiposAtributo.AUTOR.getNombre(),
                txt
            ));
        
        txt = this.txtGenero.getText().trim();
        if ( ! txt.isEmpty() )
            f.addCriterio(new Criterio(
                EnumTiposAtributo.GENERO.getNombre(),
                txt
            ));
        
        txt = this.txtFecha.getText().trim();
        
        if ( ! txt.isEmpty() )
            f.addCriterio(new Criterio(
                EnumTiposAtributo.FECHA.getNombre(),
                txt
            ));
        
        return f;
    }
    
    @Override
    protected void ejecutar() {
        Filtro f = this.getFiltroFromCampos();
        WidgetMedios frame;


        // Creamos una nueva biblioteca de medios...
        frame = new WidgetMedios(this.controlador);
        frame.setTitle("Resultados de la búsqueda");
        
        // Y le asignamos el filtro de búsqueda que hemos creado...
        frame.setFiltroModel(f);
       
        // Esto ya no tiene sentido, así que lo cerramos
        this.controlador.cerrarFrame(this);
        
        // Añadimos el panel        
        this.controlador.getUIBiblioteca().addPanel(frame);

    }
    
}
