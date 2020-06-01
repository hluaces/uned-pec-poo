package iu.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import usuarios.EnumPermisos;
import usuarios.Usuario;

/**
 * Widget que permite ver los usuarios activos en la biblioteca, borrarlos y
 * además ofrece accesos para ver el historial de multas y de préstamos
 * de cada usuario.
 * 
 * @author Héctor Luaces Novo
 */
public class WidgetGestionUsuarios extends AbstractWidgetConTabla<Usuario> {
    /**
     * Posición del array de columnas que hace referencia al login de usuario
     */
    private final static int POSICION_LOGIN     = 0;
    /**
     * Posición del array de columnas que hace referencia al nombr del usuario
     */
    private final static int POSICION_NOMBRE    = 1;
    
    /**
     * Posición del array de columnas que hace referencia a los apellidos del 
     * usuario
     */
    private final static int POSICION_APELLIDOS = 2;
    
    /**
     * Posición del array de columnas que hace referencia al DNI del usuario
     */
    private final static int POSICION_DNI       = 3;
    
    /**
     * Posición del array de columnas que hace referencia al tipo de perfil
     * del usuario.
     */
    private final static int POSICION_PERFIL    = 4;
    
    /**
     * Tamaño total del array de columnas de la tabla de usuarios.
     */
    private final static int TAMANYO_ARRAY      = 5;

    /**
     * Botones para realizar las operaciones de borrado de usuario, ver 
     * historial de usuario, ver tarjeta de usuario y ver multas de usuario
     */
    private JButton botonBorrar, botonHistorial, botonMultas, botonTarjeta, botonReservas;
    
    public WidgetGestionUsuarios(ControladorSwing c) {
        super(c);
    }

    @Override
    protected void cargarDatos() {
        this.datos = this.getControlador().getBibliotecaActiva().getUsuarios();
        
        if ( this.datos == null )
            super.cargarDatos();
    }
    
    /**
     * Devuelve un array con los campos de la cabecera de la tabla.
     * 
     * @return Array con los campos de la cabecera de la tabla
     */
    static String[] getArrayCamposUsuario() {
        String cols[] = new String[TAMANYO_ARRAY];
        
        cols[POSICION_LOGIN]     = "Nombre de usuario";
        cols[POSICION_NOMBRE]    = "Nombre";
        cols[POSICION_APELLIDOS] = "Apellidos";
        cols[POSICION_DNI]       = "DNI";
        cols[POSICION_PERFIL]    = "Perfil";
        
        return cols;
    }
    
    /**
     * Dado un usuario, devuelve un array con todos los datos de dicho usuario
     * 
     * @param usuario El array con los datos del usuario
     * @return 
     */
    static String[] getArrayDatosUsuario(Usuario usuario) {
        String data[] = new String[TAMANYO_ARRAY];
        
        data[POSICION_LOGIN]     = usuario.getLogin();
        data[POSICION_NOMBRE]    = usuario.getNombre();
        data[POSICION_APELLIDOS] = usuario.getApellidos();
        data[POSICION_DNI]       = usuario.getDni();
        data[POSICION_PERFIL]    = usuario.getPerfil().getNombre();
        
        return data;
    }
    
    
    @Override
    public void actualizar() {
        this.columnas = WidgetGestionUsuarios.getArrayCamposUsuario();
                
        super.actualizar(); 
        this.setTitle("Gestión de usuarios");
    }

    @Override
    protected void addRow(Usuario item) {
        String []fila = WidgetGestionUsuarios.getArrayDatosUsuario(item);
        
        this.getModeloTabla().addRow(fila);
    }

    @Override
    protected Usuario getObjetoFila(int fila) {
        Vector datosFila = this.getDatosFila(fila);
        
        if ( datosFila == null || datosFila.isEmpty() )
            return null;
        
        for(Usuario u: this.datos) {
            boolean ok;
            ok = this.compararArrayYObjeto(
                (String[]) datosFila.toArray(new String[TAMANYO_ARRAY]),
                new Object[] {
                    u.getLogin(),
                    u.getNombre(),
                    u.getApellidos(),
                    u.getDni(),
                    u.getPerfil().getNombre()
                }
            );
            
            if ( ok )
                return u;
        }
        
        return null;
    }

    @Override
    protected void actualizarBotones() {
        Usuario u = this.getObjetoFilaSeleccionada();
        
        this.crearBotones();

        this.botonHistorial.setVisible(this.tienePermiso(EnumPermisos.GESTION_PRESTAMOS));
        this.botonBorrar.setVisible(this.tienePermiso(EnumPermisos.GESTION_USUARIOS));
        this.botonMultas.setVisible(this.tienePermiso(EnumPermisos.GESTION_MULTAS));
        this.botonTarjeta.setVisible(this.tienePermiso(EnumPermisos.GESTION_TARJETAS));
        
        this.botonMultas.setEnabled(u != null);
        this.botonTarjeta.setEnabled(u != null);
        this.botonHistorial.setEnabled(u != null);
                
        // No se puede borrar el usuario activo
        this.botonBorrar.setEnabled(u != null && u != this.getUsuario()); 
    }
 
    /** 
     * Muestra las reservas del usuario seleccionado
     */
    private void verReservas() {
        WidgetReservas wr;
        
        wr = new WidgetReservas(
            this.getControlador(), 
            this.getObjetoFilaSeleccionada()
        );
        this.getControlador().addPanel(wr);
    }
    
    /**
     * Muestra el historial del usuario seleccionado en la tabla
     */
    private void verHistorial() {
        WidgetPrestamosHistorial wg;
        
        wg = new WidgetPrestamosHistorial(
            this.getControlador(), 
            this.getObjetoFilaSeleccionada()
        );
        
        this.getControlador().addPanel((JInternalFrame) wg);
    }
    
    /**
     * Muestra las multas del usuario seleccionado en la tabla
     */
    private void verMultas() {
        WidgetMultas wg;
        
        wg = new WidgetMultas(
            this.getControlador(), 
            this.getObjetoFilaSeleccionada()
        );
        
        this.getControlador().addPanel((JInternalFrame) wg);
    }
    
    /**
     * Muestra la tarjeta del usuario seleccionado
     */
    private void verTarjeta() {
        WidgetTarjetaUsuario wg = new WidgetTarjetaUsuario(
            this.getControlador(), 
            this.getObjetoFilaSeleccionada()
        );
        
        this.getControlador().addPanel(wg);
    }
    
    /**
     * Borra el usuario seleccionado en la tabla.
     */
    private void borrar() {
        boolean ok;
        
        ok = this.getControlador().getBibliotecaActiva().borrarUsuario(
            this.getObjetoFilaSeleccionada()
        );
        
        if ( ok )
            this.getControlador().actualizaEscritorio();
    }
    
    /**
     * Crea los botones de ver historial, ver multas y borrar usuarios
     */
    private void crearBotones() {
        if ( this.botonHistorial == null ) {
            this.botonHistorial = new JButton("Préstamos");
            
            this.botonHistorial.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    verHistorial();
                }
            });
            
            this.addBoton(this.botonHistorial);
        }
        
        if ( this.botonMultas == null ) {
            this.botonMultas = new JButton("Multas");
            this.botonMultas.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    verMultas();
                }
                
            });
            this.addBoton(this.botonMultas);
        }
        
        if ( this.botonBorrar == null ) {
            this.botonBorrar = new JButton("Borrar");
            this.botonBorrar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    borrar();
                }
                
            });
            this.addBoton(this.botonBorrar);
        }
        
        if ( this.botonTarjeta == null ) {
            this.botonTarjeta = new JButton("Tarjeta");
            this.botonTarjeta.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    verTarjeta();
                }
                
            });
            this.addBoton(this.botonTarjeta);
        }
        
        if ( this.botonReservas == null ) {
            this.botonReservas = new JButton("Reservas");
            this.botonReservas.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    verReservas();
                }                
            });
            this.addBoton(this.botonReservas);
        }
    }
        
}
    
  