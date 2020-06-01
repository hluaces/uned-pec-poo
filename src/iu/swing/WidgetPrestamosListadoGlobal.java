package iu.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.DefaultRowSorter;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.RowFilter;
import medios.EnumTiposMedios;
import misc.StringUtil;
import prestamos.Prestamo;
import usuarios.EnumPermisos;

/**
 * {@link AbstractWidgetConTabla} que permite ver el listado de todos los 
 * préstamos de la biblioteca (sin importar su estado).
 * 
 * Implementa botones de conveniencia según permisos para ver el historial,
 * ver las multas y devolver péstamos, así como un combo que permite filtrar
 * el contenido de la tabla por tipo de medio.
 * 
 * @author Héctor Luaces Novo
 */
public class WidgetPrestamosListadoGlobal extends AbstractWidgetConTabla<Prestamo> {
    /**
     * Posición del array de Préstamos que hará referencia al tipo de medio
     * prestado
     */
    private final int POSICION_TIPO        = 0;
    
    /**
     * Posición del array de Préstamos que hará referencia al título del medio
     * prestado
     */
    private final int POSICION_MEDIO       = 1;
    
    /**
     * Posición del array de Préstamos que hará referencia al usuario al
     * que se ha entregado el préstamo
     */
    private final int POSICION_USUARIO     = 2;
    
    /**
     * Posición del array de Préstamos que hará referencia a la fecha de 
     * creación del préstamo.
     */
    private final int POSICION_FECHA       = 3;
    
    /**
     * Posición del array de Préstamos que hará referencia a la fecha de 
     * vencimiento del préstamo.
     */
    private final int POSICION_VENCIMIENTO = 4;
    
    /**
     * Posición del array de Préstamos que hará referencia a la fecha de
     * devolución del préstamo
     */
    private final int POSICION_DEVOLUCION  = 5;
    
    /**
     * Posición del array de Préstamos que hará referencia al hecho de que
     * el préstamo esté o no vencido
     */
    private final int POSICION_VENCIDO     = 6;
    
    /**
     * Tamaño del array del préstamo (nº total de columnas de la tabla)
     */
    private final int TAMANYO_ARRAY        = 7;

    /**
     * Botones que se usarán para ver el historial y las multas de un 
     * usuario, así como intentar devolver un préstamo.
     */    
    private JButton botonHistorial, botonMultas, botonDevolver;
    
    /** 
     * Etiqueta que se mostrará al lado del combo que permite filtrar la tabla.
     */
    private JLabel labelFiltro;
    
    /**
     * Combobox que permitirá filtrar la tabla de préstamos por tipo de medio.
     */
    private JComboBox comboFiltro;
    
    public WidgetPrestamosListadoGlobal(ControladorSwing c) {
        super(c);
    }

    @Override
    protected void cargarDatos() {
        this.datos = this.getControlador().getBibliotecaActiva().getPrestamos();
        
        if ( this.datos == null )
            super.cargarDatos();
    }
    
    @Override
    public void actualizar() {
        this.columnas = new String[TAMANYO_ARRAY];
        
        this.columnas[POSICION_TIPO]        = "Tipo medio";
        this.columnas[POSICION_MEDIO]       = "Título medio";
        this.columnas[POSICION_USUARIO]     = "Usuario préstamo";
        this.columnas[POSICION_FECHA]       = "Fecha préstamo";
        this.columnas[POSICION_VENCIMIENTO] = "Fecha vencimiento";
        this.columnas[POSICION_DEVOLUCION]  = "Fecha devolución";
        this.columnas[POSICION_VENCIDO]     = "Vencido S/N";
        
        super.actualizar();
        this.setTitle("Listado de préstamos");
        
        this.filtrar();
    }

    @Override
    protected void addRow(Prestamo item) {
        String []fila = new String[TAMANYO_ARRAY];
        
        fila[POSICION_TIPO]        = item.getMedio().getTipo();
        fila[POSICION_MEDIO]       = item.getMedio().getTitulo();
        fila[POSICION_USUARIO]     = item.getUsuario().getLogin();
        fila[POSICION_FECHA]       = StringUtil.fechaToString(item.getFechaInicio());
        fila[POSICION_VENCIMIENTO] = StringUtil.fechaToString(item.getFechaVencimiento());
        fila[POSICION_DEVOLUCION]  = StringUtil.fechaToString(item.getFechaDevolucion());
        fila[POSICION_VENCIDO]     = this.getTextoBoolean(item.isVencido());
        
        this.getModeloTabla().addRow(fila);
    }

    @Override
    protected Prestamo getObjetoFila(int fila) {
        Vector datosFila = this.getDatosFila(fila);
        
        if( datosFila == null || datosFila.isEmpty() )
            return null;
        
        for(Prestamo p: this.datos) {
            boolean ok;
            
            ok = this.compararArrayYObjeto(
                (String []) datosFila.toArray(new String[TAMANYO_ARRAY]), 
                new Object[] {
                    p.getMedio().getTipo(),
                    p.getMedio().getTitulo(),
                    p.getUsuario().getLogin(),
                    p.getFechaInicio(),
                    p.getFechaVencimiento(),
                    p.getFechaDevolucion(),
                    p.isVencido()
                }
            );
            
            if ( ok )
                return p;
        }
        
        return null;
    }

    /**
     * Permite ver el historial de préstamos del usuario asociado al préstamo
     * que tengamos seleccionado actualmente en la tabla.
     */
    private void verHistorial() {
        Prestamo p = this.getObjetoFilaSeleccionada();
        WidgetPrestamosHistorial prs;
        
        if ( p == null ) {
            return;
        }
        
        prs = new WidgetPrestamosHistorial(this.getControlador(), p.getUsuario());
        this.getControlador().getUIBiblioteca().addPanel(prs);
    }
    
    /**
     * Permite ver las multas del usuario asociado al préstamo
     * que tengamos seleccionado actualmente en la tabla.
     */
    private void verMultas() {
        Prestamo p = this.getObjetoFilaSeleccionada();
        WidgetMultas prs;
        
        if ( p == null ) {
            return;
        }
        
        prs = new WidgetMultas(this.getControlador(), p.getUsuario());
        this.getControlador().getUIBiblioteca().addPanel(prs);
    }
    
    /**
     * Filtra la tabla en función del valor seleccionado en el comboBox
     */
    protected void filtrar() {
        DefaultRowSorter sorter;
        String tx;
        
        sorter = (DefaultRowSorter) this.getTabla().getRowSorter();
        tx     = this.comboFiltro.getSelectedItem().toString();
        
        if ( tx.equals("Cualquiera") ) {
            sorter.setRowFilter(null);
            return;
        }
        
        sorter.setRowFilter(RowFilter.regexFilter(tx, POSICION_TIPO));
    }
      
    /**
     * Intenta devolver el préstamo seleccionado
     */
    private void devolver() {
        boolean ok;
        
        ok = this.getControlador()
            .getBibliotecaActiva()
            .devolverPrestamo(this.getObjetoFilaSeleccionada()
        );
        
        if ( ok )
            this.getControlador().actualizaEscritorio();
    }

    /**
     * Crea los botones de historial, devolver y multas; así como el combo 
     * de filtrado de la tabla.
     */    
    private void crearBotones() {
        if ( this.botonHistorial == null ) {
            this.botonHistorial = new JButton("Ver historial usuario");
            
            this.botonHistorial.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    verHistorial();
                }                
            });
            
            this.addBoton(this.botonHistorial);
        }
        
        if ( this.botonMultas == null ) {
            this.botonMultas = new JButton("Ver multas del usuario");
            
            this.botonMultas.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    verMultas();
                }
            });
            this.addBoton(this.botonMultas);
        }
        
        if ( this.comboFiltro == null ) {
            this.comboFiltro = new JComboBox<>();
            this.labelFiltro = new JLabel("Filtrar préstamos por tipo de medio");
            
            this.comboFiltro.removeAllItems();
            
            for(EnumTiposMedios e: EnumTiposMedios.values()) {
                this.comboFiltro.addItem(e.getNombre());
            }
            
            this.comboFiltro.addItem("Cualquiera");
            this.comboFiltro.setSelectedItem("Cualquiera");
                        
            this.comboFiltro.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    filtrar();
                }
            });
            
            this.addCabecera(this.labelFiltro);
            this.addCabecera(this.comboFiltro);
        }
        
        if ( this.botonDevolver == null ) {
            this.botonDevolver = new JButton("Devolver préstamo");
            
            this.botonDevolver.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    devolver();
                }
                
            });
            this.addBoton(this.botonDevolver);
        }
    }
    
    
    @Override
    protected void actualizarBotones() {
        Prestamo p = this.getObjetoFilaSeleccionada();
        this.crearBotones();
        
        this.botonMultas.setVisible(this.tienePermiso(EnumPermisos.GESTION_MULTAS));
        this.botonHistorial.setVisible(this.tienePermiso(EnumPermisos.GESTION_PRESTAMOS));
        this.botonMultas.setEnabled(p != null);
        this.botonHistorial.setEnabled(p != null);
        this.botonDevolver.setEnabled(p != null && ! p.isDevuelto());
    }
    
    
}
