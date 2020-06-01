package iu.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.JButton;
import misc.StringUtil;
import multas.Multa;
import usuarios.EnumPermisos;
import usuarios.Usuario;

/**
 * {@link AbstractWidgetConTabla} que muestra la información de Multas para un usuario.
 * 
 * @author Héctor Luaces Novo
 */
public class WidgetMultas extends AbstractWidgetConTabla<Multa> {   
    /**
     * Posición en las columnas de la tabla de la fecha de emisión de la multa
     */
    protected final int POSICION_FECHA   = 0;
    
    /**
     * Posición en las columnas de la tabla del nombre del medio que generó
     * la multa.
     */
    protected final int POSICION_MEDIA   = 1;
    
    /**
     * Posición en las columnas de la tabla que mostrará si la multa está o no
     * vigente.
     */
    protected final int POSICION_VIGENTE = 2;
    
    /**
     * Tamaño del array de Multas
     */
    protected final int TAMANYO_ARRAY = 3;
            
    /**
     * Botón que permitirá a un administrador anular una multa.
     */
    protected JButton botonAnularMulta;

    /**
     * Crea un nuevo Widget de multas inyectándole un controladorswing y tomando
     * como usuario el usuario activo.
     * 
     * @param c ControladorSwing a inyectar
     */
    public WidgetMultas(ControladorSwing c) {
        super(c);
    }

    /**
     * Crea un nuevo Widget de multas inyectándole un controladorSwing
     * y un usuario.
     * 
     * @param c ControladorSwing a inyectar en el Widget.
     * @param u Usuario a inyectar en el Widget
     */
    public WidgetMultas(ControladorSwing c, Usuario u) {
        super(c, u);
    }
    
    /**
     * Este widget cargará los datos de multas para un usuario.
     */
    @Override
    protected void cargarDatos() {
        // Cargamos las multas para el usuario del widget
        this.datos = this.getControlador()
            .getBibliotecaActiva()
            .getMultasUsuario(this.getUsuario())
        ;
        
        if ( this.datos == null )
            super.cargarDatos();
    }
    
    @Override
    public void actualizar() {
        String [] cols = new String[TAMANYO_ARRAY];
        
        cols[POSICION_FECHA]   = "Fecha emisión";
        cols[POSICION_MEDIA]   = "Medio no devuelto";
        cols[POSICION_VIGENTE] = "Vigente S/N";
        
        this.columnas = cols;
        
        super.actualizar();
        this.setTitle("Multas para " + this.getUsuario().getLogin());
    }

    @Override
    protected void addRow(Multa item) {
        this.getModeloTabla().addRow(new String[] {
            StringUtil.fechaToString(item.getFechaEmision()),
            item.getPrestamo().getMedio().getTitulo(),
            //item.isVigente() ? "S" : "N"
            this.getTextoBoolean(item.isVigente())
        });
    }

    @Override
    protected Multa getObjetoFila(int fila) {
        Vector datosFila = this.getDatosFila(fila);
        
        if ( datosFila == null || datosFila.isEmpty() )
            return null;
        
        for(Multa m: this.datos) {
            boolean ok ;
            
            ok = this.compararArrayYObjeto(
                (String[]) datosFila.toArray(new String[TAMANYO_ARRAY]), 
                new Object[] {
                    m.getFechaEmision(),
                    m.getPrestamo().getMedio().getTitulo(),
                    m.isVigente()
                }
            );
            
            if ( ok )
                return m;
        }
        
        return null;
    }

    /**
     * Anula una multa seleccionada
     */
    private void anular() {
        Multa m = this.getObjetoFilaSeleccionada();
        
        if ( m == null ) {
            this.getControlador().mostrarAviso(
                "Ha sido imposible cargar la multa seleccionada."
            );
            return;
        }
        
        if ( this.getControlador().getBibliotecaActiva().anularMulta(m) )
            this.actualizar();
    }
    
    /**
     * Crea el botón de 'anular' y le asigna los eventos correspondientes.
     */
    private void crearBotonMulta() {
        JButton boton = new JButton("Anular multa");

        boton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                anular();
            }
        });
        
        this.botonAnularMulta = this.addBoton(boton);
    }
    
    @Override
    protected void actualizarBotones() {
        boolean ok;
        
        super.actualizarBotones(); 
        
        if ( this.botonAnularMulta != null )
            this.botonAnularMulta.setVisible(false);
        
        ok = this.getControlador()
            .getUsuarioActivo()
            .hasPermiso(EnumPermisos.GESTION_MULTAS)
        ;
        
        if ( ! ok )
            return;
            
        if ( this.botonAnularMulta == null )
            this.crearBotonMulta();
        
        this.botonAnularMulta.setVisible(true);
        this.botonAnularMulta.setEnabled(
            this.getTabla().getSelectedRowCount() == 1
        );
    }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 394, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 274, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
