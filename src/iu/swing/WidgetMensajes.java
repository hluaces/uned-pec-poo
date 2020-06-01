package iu.swing;

import biblioteca.Mensaje;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;
import javax.swing.JButton;
import misc.StringUtil;

/**
 * {@link AbstractWidgetConTabla} que muestra los mensajes de un Usuario y 
 * le permite marcarlos como leídos o borrarlos.
 * 
 * @author Héctor Luaces Novo
 */
public class WidgetMensajes extends AbstractWidgetConTabla<Mensaje> {
    /**
     * Posición, en el array de la tabla, de la fecha del mensaje
     */
    private final int POSICION_FECHA   = 0;
    /**
     * Posición, en el array de la tabla, del texto del mensaje
     */
    private final int POSICION_MENSAJE = 1;
    /**
     * Posición, en el array de la tabla, de la flag que determina si el mensaje
     * está o no leído
     */
    private final int POSICION_LEIDO   = 2;
    
    /**
     * Tamaño del array de mensajes
     **/
    private final int TAMANYO_ARRAY = 3;
    /**
     * Botones utilizados para las funcionalidades de 'leer mensaje',
     * 'ver mensaje' y 'borrar mensaje'
     */
    private JButton botonLeer, botonBorrar, botonVer;
    
    /**
     * Constructor principal que permite inyectar un ControladorSwing
     * 
     * @param c ControladorSwing a inyectar.
     */
    public WidgetMensajes(ControladorSwing c) {
        super(c);
    }

    /**
     * Crea los botones de 'borrar mensaje' y 'leer mensaje', asignándole
     * los eventos pertinentes.
     */
    private void crearBotones() {
        if ( this.botonLeer == null ) {
            this.botonLeer = new JButton("Marcar como leído");
            this.addBoton(this.botonLeer);
            this.botonLeer.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    leer();
                }
            });
        }
        
        if ( this.botonBorrar == null ) {
            this.botonBorrar = new JButton("Borrar mensaje");
            this.addBoton(this.botonBorrar);
            this.botonBorrar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    borrar();
                }
            });
        }
        
        if ( this.botonVer == null ) {
            this.botonVer = new JButton("Ver mensaje");
            this.addBoton(this.botonVer);
            
            this.botonVer.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                   ver();
                }
                
            });
        }
    }

    @Override
    protected Mensaje getObjetoFila(int fila) {
        Vector datosFila = this.getDatosFila(fila);
        
        if ( datosFila == null || datosFila.isEmpty() )
            return null;
        
        for(Mensaje m: this.datos) {
            boolean ok;
            
            ok = this.compararArrayYObjeto(
                (String []) datosFila.toArray(new String[TAMANYO_ARRAY]),
                new Object[] {
                    m.getFechaMensaje(),
                    m.getMensaje(),
                    m.isLeido()
                }
            );
        
            if ( ok )
                return m;
        }
        
        return null;
    }

    @Override
    protected void addRow(Mensaje item) {
        String[] data = new String[3];
        
        data[POSICION_FECHA]   = StringUtil.fechaToString(item.getFechaMensaje());
        data[POSICION_LEIDO]   = this.getTextoBoolean(item.isLeido());
        data[POSICION_MENSAJE] = item.getMensaje();
        
        this.getModeloTabla().addRow(data);
    }
    
    
    @Override
    public void actualizar() {
        this.columnas = new String[TAMANYO_ARRAY];
        
        columnas[POSICION_FECHA]   = "Fecha";
        columnas[POSICION_MENSAJE] = "Mensaje";
        columnas[POSICION_LEIDO]   = "Leído S/N";

        super.actualizar();
        
        this.setTitle("Mensajes para " +this.getUsuario().getLogin());
    }

    
    @Override
    protected void cargarDatos() {
        List<Mensaje> lista;
        
        lista = this.getControlador()
            .getBibliotecaActiva()
            .getMensajesUsuario(this.getUsuario())
        ;
        
        if ( lista == null ) {
            super.cargarDatos();
            return;
        }
        
        this.datos = new HashSet(lista);
    }

    @Override
    protected void actualizarBotones() {
        
        this.crearBotones();
        
        this.botonVer.setEnabled(this.getTabla().getSelectedRowCount() > 0);
        this.botonLeer.setEnabled(this.getTabla().getSelectedRowCount() > 0);
        this.botonBorrar.setEnabled(this.getTabla().getSelectedRowCount() > 0);
    }

    /**
     * Intenta marcar como leído el mensaje seleccionado en la tabla
     */
    private void leer() {
        boolean ok = this.getControlador()
            .getBibliotecaActiva()
            .leerMensaje(this.getObjetoFilaSeleccionada())
        ;
        
        if ( ok )
            this.actualizar();
    }
    
    /**
     * Intenta borrar el mensaje seleccionado en la tabla
     */
    private void borrar() {
        boolean ok = this.getControlador()
            .getBibliotecaActiva()
            .borrarMensajeUsuario(this.getUsuario(),
                this.getObjetoFilaSeleccionada()
            );
        
        if ( ok )
            this.actualizar();
    }
    
    /**
     * Muestra por pantalla un diálogo con el texto del mensaje seleccionado.
     * 
     * Util si el mensaj es muy largo
     */
    private void ver() {
        Mensaje m = this.getObjetoFilaSeleccionada();
        
        if ( m != null )
            this.getControlador().mostrarAviso(m.getMensaje());
        else
            this.getControlador().mostrarError("Imposible cargar el mensaje");
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
