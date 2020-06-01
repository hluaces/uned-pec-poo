package iu.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.JButton;
import misc.StringUtil;
import prestamos.Reserva;
import usuarios.Usuario;

/**
 * {@link AbstractWidgetConTabla} que muestra las reservas de un usuario.
 * 
 * Permite anular las reservas existentes.
 * 
 * @author Héctor Luaces Novo
 */
public class WidgetReservas extends AbstractWidgetConTabla<Reserva> {
    /**
     * Posición del array de columnas de la fecha de creación de la reserva
     */
    private static final int POSICION_FECHA  = 0;
    
    /**
     * Posición del array de columnas del título del medio de la reserva
     */
    private static final int POSICION_TITULO = 1;
    
    /**
     * Posición del array de columnas del estado actual del medio reservado
     */
    private static final int POSICION_ESTADO = 2;
    
    /**
     * Tamaño total del array de reservas
     */
    private static final int TAMANYO_ARRAY   = 3;
    
    /**
     * Botón que será usado para anular una reserva
     */
    private JButton botonAnular;

    /**
     * Crea un nuevo WidgetReservas mostrando las reservas de un usuario dado.
     * 
     * @param c ControladorSwing a utilizar.
     * @param u Usuario del que queremos ver las reservas.
     */
    public WidgetReservas(ControladorSwing c, Usuario u) {
        super(c, u);
    }

    @Override
    protected void cargarDatos() {
        this.datos = this.getControlador()
            .getBibliotecaActiva()
            .getReservasUsuario(this.getUsuario())
        ;
        
        if ( this.datos == null )
            super.cargarDatos();
    }

    @Override
    public void actualizar() {
        this.columnas = new String[TAMANYO_ARRAY];
        
        this.columnas[POSICION_FECHA]  = "Fecha de la reserva";
        this.columnas[POSICION_TITULO] = "Medio reservado";
        this.columnas[POSICION_ESTADO] = "Estado actual del medio";
        
        super.actualizar();
        this.setTitle("Reservas de " + this.getUsuario().getLogin());
    }
    
    @Override
    protected void addRow(Reserva item) {
        String [] cols = new String[TAMANYO_ARRAY];
        
        cols[POSICION_FECHA]  = StringUtil.fechaToString(item.getFecha());
        cols[POSICION_TITULO] = item.getMedio().getTitulo();
        cols[POSICION_ESTADO] = item.getMedio().getEstado().getNombreEstado();
        
        this.getModeloTabla().addRow(cols);
    }

    @Override
    protected Reserva getObjetoFila(int fila) {
        Vector datosFila = this.getDatosFila(fila);
        String []datos;
        
        if ( datosFila == null || datosFila.isEmpty() )
            return null;
        
        datos = (String[]) datosFila.toArray(new String[TAMANYO_ARRAY]);
        for(Reserva r: this.datos) {
            boolean ok;
            
            ok = this.compararArrayYObjeto(
                datos, 
                new Object[] {
                    r.getFecha(),
                    r.getMedio().getTitulo(),
                    r.getMedio().getEstado().getNombreEstado()
                }
            );
            
            if ( ok )
                return r;
        }
        
        return null;
    }
    /*
    private void prestamo() {
        Reserva r = this.getObjetoFilaSeleccionada();
        Prestamo p;
        
        p = this.getControlador()
            .getBibliotecaActiva()
            .pedirPrestamo(this.getUsuario(), r.getMedio())
        ;
        
        if ( p != null )
            this.getControlador().actualizaEscritorio();
    }
    */
    private void anular() {
        boolean ok;
        
        ok = this.getControlador()
            .getBibliotecaActiva()
            .borrarReserva(this.getObjetoFilaSeleccionada()
        );
        
        // No actualizamos todo el escritorio porque las reservas
        // no se muestran en otro sitio que no sea este panel
        if ( ok )
            this.actualizar();
    }
    
    private void crearBotones() {
        if ( this.botonAnular == null ) {
            this.botonAnular = new JButton("Anular reserva");
            this.addBoton(this.botonAnular);
            
            this.botonAnular.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    anular();
                }                
            });
        }
    }

    @Override
    protected void actualizarBotones() {
        Reserva r = this.getObjetoFilaSeleccionada();
        
        this.crearBotones();
        this.botonAnular.setEnabled(r != null);
    }
    
    
}
