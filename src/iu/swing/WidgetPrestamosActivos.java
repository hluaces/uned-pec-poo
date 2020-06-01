package iu.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Vector;
import javax.swing.JButton;
import misc.StringUtil;
import prestamos.Prestamo;
import usuarios.EnumPermisos;
import usuarios.Usuario;

/**
 * {@link AbstractWidgetConTabla} que muestra los préstamos activos (es decir,
 * no entregados) de un usuario.
 * 
 * Adicionalmente, permitirá tanto al usuario como al administrador, devolver
 * un préstamo que esté activo.
 * 
 * @author Héctor Luaces Novo
 */
public class WidgetPrestamosActivos extends AbstractWidgetConTabla<Prestamo> {
    /**
     * Posición del array de columnas que corresponde con la fecha de préstamo
     * de un préstamo.
     */
    private final int POSICION_FECHA       = 0;
    
    /**
     * Posición del array de columnas que corresponde con el título de un medio
     * asociado a un préstamo.
     */
    private final int POSICION_TITULO      = 1;
    
    /**
     * Posición del array de columnas que corresponde con la fecha de devolución
     * asociada a un préstamo.
     */
    private final int POSICION_DEVOLUCION  = 2;
    
    /**
     * Posición del array de columnas que corresponde con la fecha de 
     * vencimiento de un préstamo.
     */
    private final int POSICION_VEMCIMIENTO = 3;
    
    /**
     * Posición del array de columnas que corresponde con el campo booleano
     * que determina que un préstamo ha sido vencido.
     */
    private final int POSICION_VENCIDO     = 4;
    
    /**
     * Tamaño del array de columnas de este Widget
     */
    private final int TAMANYO_ARRAY        = 5;
    
    /**
     * Botón utilizado para la acción de 'devolver' un préstamo
     */
    private JButton botonDevolver;
    
    /**
     * Crea un nuevo WidgetPrestamos y le inyecta un ControladorSwing
     * @param c ControladorSwing a inyectar
     */
    public WidgetPrestamosActivos(ControladorSwing c) {
        super(c);
    }

    /**
     * Crea un nuevo WidgetPrestamos y le inyecta un ControladorSwing, así
     * como el usuario para el que se mostrarán los préstamos.
     * 
     * @param c El ControladorSwing a inyectar.
     * @param u El usuario para el que se mostrarán los préstamos.
     */
    public WidgetPrestamosActivos(ControladorSwing c, Usuario u) {
        super(c, u);
    }

    @Override
    public void actualizar() {
        String []columnas;

        columnas = new String[TAMANYO_ARRAY];
        columnas[POSICION_FECHA]      = "Fecha de préstamo";
        columnas[POSICION_TITULO]     = "Título del medio prestado";
        columnas[POSICION_DEVOLUCION] = "Fecha de devolución";
        columnas[POSICION_VEMCIMIENTO]= "Fecha vencimiento";
        columnas[POSICION_VENCIDO]    = "Préstamo vencido S/N";
        this.columnas = columnas;
        
        super.actualizar();

        
        this.setTitle("Préstamos activos para " + this.getUsuario().getLogin());
    }
    
    @Override
    protected void cargarDatos() {
        this.datos = this.getControlador()
            .getBibliotecaActiva()
            //.getPrestamosUsuario(this.usuario)
            .getPrestamosActivosUsuario(this.getUsuario())
        ;
        
        if ( this.datos == null )
            this.datos = new HashSet<>();
    }

    @Override
    protected void addRow(Prestamo item) {
        String []fila;
        
        fila = new String[TAMANYO_ARRAY];
        fila[POSICION_FECHA]       = StringUtil.fechaToString(item.getFechaInicio());
        fila[POSICION_TITULO]      = item.getMedio().getTitulo();
        fila[POSICION_DEVOLUCION]  = StringUtil.fechaToString(item.getFechaDevolucion());
        fila[POSICION_VEMCIMIENTO] = StringUtil.fechaToString(item.getFechaVencimiento());
        fila[POSICION_VENCIDO]     = item.isVencido() ? "S" : "N";
        
        this.getModeloTabla().addRow(fila);
    }

    @Override
    protected Prestamo getObjetoFila(int fila) {
        Vector datosFila = this.getDatosFila(fila);
        
        if ( datosFila == null || datosFila.isEmpty() )
            return null;
        
        // No queremos comparar la devolución
        datosFila = (Vector) datosFila.clone();
        datosFila.remove(POSICION_DEVOLUCION);
            
        for(Prestamo p: this.datos) {
            boolean ok;

            ok = this.compararArrayYObjeto(
                (String[]) datosFila.toArray(new String[TAMANYO_ARRAY -1]),
                new Object[] {
                    p.getFechaInicio(),
                    p.getMedio().getTitulo(),
                    p.getFechaVencimiento(),
                    p.isVencido()
                }
            );
            
            if ( ok )
                return p;
        }
        
        return null;
    } 

    /**
     * Método lanzado cuando se pulsa el botón 'devolver' del widget.
     */
    private void devolver() {
        boolean ok;
        
        ok = this.getControlador()
            .getBibliotecaActiva()
            .devolverPrestamo(
                this.getObjetoFilaSeleccionada()
            );
        
        if ( ok )
            this.getControlador().actualizaEscritorio();
    }
    
    @Override
    protected void actualizarBotones() {
        Prestamo p;
        
        super.actualizarBotones(); 
        
        if ( this.botonDevolver == null ) {
            this.botonDevolver = new JButton("Devolver préstamo");
            this.addBoton(this.botonDevolver);
            
            this.botonDevolver.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    devolver();
                }
            }
            );
        }
        
        this.botonDevolver.setVisible(this.getControlador()
                .getUsuarioActivo()
                .hasPermiso(EnumPermisos.GESTION_PRESTAMOS)
        );
        this.botonDevolver.setEnabled(false);
        p = this.getObjetoFilaSeleccionada();
        
        if ( p != null && ! p.isDevuelto() )
            this.botonDevolver.setEnabled(true);
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
