package iu.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.JButton;
import misc.StringUtil;
import prestamos.Reserva;
import usuarios.EnumPermisos;

/**
 * Widget que muestra todas las reservas de la biblioteca, permitiendo a un
 * bibliotecario anularlas directamente desde aquí.
 * 
 * @author Héctor Luaces Novo
 */
public class WidgetListadoReservas extends AbstractWidgetConTabla<Reserva> {
    /**
     * Posición en el array de columnas de la fecha de reserva
     */
    private final static int POSICION_FECHA   = 0;
    /**
     * Posición en el array de columnas del usuario que pidió la reserva
     */
    private final static int POSICION_USUARIO = 1;
    /**
     * Posición en el array del medio solicitado en la reserva
     */
    private final static int POSICION_MEDIO   = 2;
    /**
     * Posición en el array de columnas del estado actual del medio
     */
    private final static int POSICION_ESTADO  = 3;
     /**
     * Tamaño del array de columnas
     */
    private final static int TAMANYO_ARRAY    = 4;
    
    /**
     * Botón que permite anular una reserva
     */
    private JButton botonAnularReserva;
            
    /**
     * Constructor básico del widget que inyecta un ControladorSwing
     * en el objeto.
     * 
     * @param c ControladorSwing a inyectar.
     */
    public WidgetListadoReservas(ControladorSwing c) {
        super(c);
    }

    @Override
    public void actualizar() {
        super.actualizar(); 
        
        this.columnas = new String[TAMANYO_ARRAY];
        this.columnas[POSICION_FECHA]   = "Fecha de la reserva";
        this.columnas[POSICION_USUARIO] = "Usuario";
        this.columnas[POSICION_MEDIO]   = "Medio reservado";
        this.columnas[POSICION_ESTADO]  = "Estado del medio";
        
        this.setTitle("Listado de reservas");
    }
    
    @Override
    protected void cargarDatos() {
        this.datos = this.getControlador().getBibliotecaActiva().getReservas();
    }

    private String[] getArrayReserva(Reserva item) {
        String [] datosFila = new String[TAMANYO_ARRAY];
        
        datosFila[POSICION_FECHA]   = StringUtil.fechaToString(item.getFecha());
        datosFila[POSICION_USUARIO] = item.getUsuario().getLogin();
        datosFila[POSICION_MEDIO]   = item.getMedio().getTitulo();
        datosFila[POSICION_ESTADO]  = item.getMedio().getEstado().getNombreEstado();
        
        return datosFila;
    }
    
    @Override
    protected void addRow(Reserva item) {
        this.getModeloTabla().addRow(this.getArrayReserva(item));
    }

    @Override
    protected Reserva getObjetoFila(int fila) {
        Vector datosFila = this.getDatosFila(fila);
        
        for(Reserva r: this.datos) {
            boolean ok;
            
            ok = this.compararArrayYObjeto(
                (String []) datosFila.toArray(new String[TAMANYO_ARRAY]), 
                this.getArrayReserva(r)
            );
            
            if ( ok )
                return r;
        }
        
        return null;
    }
    
    /**
     * Anula la reserva seleccionada
     */
    private void anularReserva() {
        Reserva r = this.getObjetoFilaSeleccionada();
        boolean ok;
        
        ok = this.getControlador().getBibliotecaActiva().borrarReserva(r);
        
        if ( ok )
            this.getControlador().actualizaEscritorio();
    }
    
    /**
     * Crea el botón de 'anular reserva'
     */
    private void crearBotones() {
        if ( this.botonAnularReserva != null )
            return;
        
        this.botonAnularReserva = new JButton("Anular reserva");
        this.addBoton(this.botonAnularReserva);
        this.botonAnularReserva.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                anularReserva();
            }            
        });
    }

    @Override
    protected void actualizarBotones() {
        this.crearBotones();
        
        this.botonAnularReserva.setVisible(
            this.tienePermiso(EnumPermisos.GESTION_RESERVAS)
        );
    }
    
    
    
}
