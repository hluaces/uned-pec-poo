package iu.swing;

import biblioteca.Biblioteca;
import biblioteca.Catalogo;
import busqueda.Filtro;
import java.io.FileNotFoundException;
import java.nio.file.FileSystemException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import medios.AtributoMedio;
import medios.EnumTiposAtributo;
import prestamos.EnumEstadosPrestamo;
import usuarios.EnumPermisos;
import usuarios.Usuario;
import medios.MedioIF;
import misc.StringUtil;
import prestamos.Prestamo;

/**
 * Widget que presenta la biblioteca de medios.
 * 
 * Esencialmente, la biblioteca de medios permite ojear el {@link Catalogo} de 
 * una {@link Biblioteca}, pudiendo interactuar con el para realizar acciones
 * como: reserva, préstamo, borrado, edición, búsqueda, etc.
 * 
 * @author Héctor Luaces Novo
 */
class WidgetMedios extends javax.swing.JInternalFrame implements ModuloIUSwingIF {
    /**
     * El controladorSwing inyectado en el Widget
     */
    private ControladorSwing controlador;
    
    /**
     * El filtro de búsqueda asociado al catálogo. Si es null, se mostrará
     * toda la biblioteca.
     */
    private Filtro filtro_activo;
    
    /**
     * Constructor privado de la clase que inicializa los componentes gráficos
     */
    private WidgetMedios() {
        initComponents();
    }

    /**
     * Crea una nueva biblioteca de medios inyectándole un ControladorSwing
     * @param c 
     */
    public WidgetMedios(ControladorSwing c) {
        this();

        this.setControlador(c);
    }

    /**
     * Devuelve el modelo activo en la tabla de medios.
     * 
     * @return El modelo activo en la tabla de medios.
     */
    private TablaMediosModel getModeloTabla() {
        return (TablaMediosModel) this.tablaMedios.getModel();
    }
    
    /**
     * Actualiza los controles de la tabla, habilitando las opciones de 
     * editar medios y borrarlos en función de los permisos de lusuario activo
     */
    private void actualizarPermisos() {
        Usuario u;
        TablaMediosModel modelo;
        
        u      = this.controlador.getUsuarioActivo();
        modelo = this.getModeloTabla();
        modelo.setEditable(false);       
        this.botonBorrar.setVisible(false);

        if ( u == null || ! u.hasPermiso(EnumPermisos.GESTION_MEDIOS) )
            return;
        
        modelo.setEditable(true);
        this.botonBorrar.setVisible(true);
    }
    
    @Override
    public void setControlador(ControladorSwing c) {
        this.controlador = c;
        this.actualizar();
    }

    @Override
    public void actualizar() {
        TablaMediosModel modelo;
        List keys = null;
        
        if ( this.tablaMedios.getRowSorter() != null )
            keys = this.tablaMedios.getRowSorter().getSortKeys();
        
        modelo = new TablaMediosModel(controlador
            .getAplicacion()
            .getBibliotecaActiva()
            .getCatalogo()
        );
        
        this.tablaMedios.setModel(modelo);
        
        if ( keys == null ||keys.isEmpty())
            this.tablaMedios.setAutoCreateRowSorter(true);
        else
            this.tablaMedios.getRowSorter().setSortKeys(keys);
        
        this.tablaMedios.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                controlador.actualizaEscritorio();
            }
        });
        this.actualizarPermisos();
        this.actualizarBotones();
        
        if ( this.filtro_activo != null )
            this.setFiltroModel(this.filtro_activo);
    }

    /**
     * Devuelve un objeto Medio que se corresponde con la fila de la vista de 
     * la tabla.
     * 
     * @return El medio que corresponde a la fila o 'null' si no hay ningún 
     * medio asociado a esa fila.
     */
    private MedioIF getMedioFila(int fila) {
        fila = this.tablaMedios.convertRowIndexToModel(fila);
        
        return ((TablaMediosModel) this.tablaMedios.getModel()).getMedioAt(fila);
    }
    
    /**
     * Devuelve un objeto Medio que se corresponde con la fila seleccionada
     * en la tabla,  o 'null' si no hay nada seleccionado.
     * 
     * @return El medio seleccionado o 'null' si no hay nada seleccionado.
     */
    private MedioIF getMedioSeleccionado() {
        int fila = this.tablaMedios.getSelectedRow();

        if ( fila < 0 )
            return null;
        
        return this.getMedioFila(this.tablaMedios.getSelectedRow());
    }
    
    /**
     * Establece el filtro de búsqueda de este widget, permitiendo que solo
     * se muestre parcialmente el catálogo de la biblioteca.
     * 
     * @param f El filtro a establecer en el widget
     */
    public void setFiltroModel(Filtro f) {
        TablaMediosModel model;
        
        model = this.getModeloTabla();
        
        if ( model == null )
            return;
        
        model.setFiltro(f);
        this.filtro_activo = f;
    }
    
    /**
     * Intenta reservar el medio que está seleccionado actualmente.
     */
    private void reservar() {
        MedioIF m = this.getMedioSeleccionado();
        Usuario u;
        boolean ok;
        
        if ( m == null ) {
            this.controlador.mostrarError(
                "Imposible cargar medio seleccionado."
            );
            return;
        }
        
        try {
            u = this.pedirUsuario();
        } catch (Exception ex) {
            this.controlador.procesarError(ex);
            return;
        }
        
        ok = this.controlador
            .getBibliotecaActiva()
            .addReserva(m, u)
        ;
        
        if ( ok )
            this.controlador.actualizaEscritorio();
    }
    
    /**
     * Intenta borrar el medio seleccionado actualmente
     */
    private void borrar() {
        TablaMediosModel model;
        int [] rows;
        MedioIF m;
        
        model = this.getModeloTabla();
        rows  = this.tablaMedios.getSelectedRows();
        
        for(int i = 0; i < rows.length; i++) {
            rows[i] = this.tablaMedios.convertRowIndexToModel(rows[i]);
            m = model.getMedioAt(rows[i]);
            
            if ( m.getEstado() != EnumEstadosPrestamo.DISPONIBLE ) {
                this.controlador.mostrarAviso(
                    "No puedes eliminar medios prestados."
                );
                return;
            }
        }
        
        model.removeRows(rows);
        this.controlador.actualizaEscritorio();
    }

    /**
     * Intenta pedir un préstamo del medio seleccionado actualmente
     */
    private void prestamo() {
        TablaMediosModel model;
        Prestamo p;
        MedioIF m;
        int row;
        int dias;
        Usuario u;
        
        if ( this.tablaMedios.getSelectedColumnCount() < 0 )
            return;
        
        if ( this.tablaMedios.getSelectedColumnCount()> 1 ) {
            this.controlador.mostrarAviso(
                "Has de seleccionar una única fila para pedir un préstamo."
            );
            return;
        }
        
        try {
            u    = pedirUsuario();
            dias = pedirDias();
        } catch (Exception ex) {
            this.controlador.procesarError(ex);
            return;
        }

        model = this.getModeloTabla();
        row   = this.tablaMedios.getSelectedRow();
        row   = this.tablaMedios.convertRowIndexToModel(row);
        m     = model.getMedioAt(row);
        
        if ( m == null ) {
            this.controlador.mostrarError(
                "El medio seleccionado no puede cargarse."
            );
        }
        
        try {
            p = this.controlador
                .getAplicacion()
                .getBibliotecaActiva()
                .pedirPrestamo(u, m, dias)
            ;
        }
        catch(Exception e) {
            this.controlador.mostrarError(e.getMessage());
            return;
        }
        
        this.controlador.mostrarAviso(
            "Has solicitado el préstamo '" + m.getTitulo() + "' para '"
                + u.getLogin() + "' hasta " +
                "la fecha '" + StringUtil.fechaToString(p.getFechaVencimiento()) 
                + "'."
        );
        this.controlador.actualizaEscritorio();
    }
    
    /**
     * Actualiza los botones del widget, habilitándolos y deshabilitandolos
     * acorde al contexto para hacer una interfaz de usuario más 
     * fluida.
     */
    private void actualizarBotones() 
    {
        TablaMediosModel model;
        int row, count;
        MedioIF m;
        
        this.botonPrestamo.setEnabled(false);
        this.botonSuscribirse.setEnabled(false);
        this.botonBorrar.setEnabled(false);
        this.botonReservar.setEnabled(false);
        this.botonExportar.setEnabled(false);
        
        this.botonPrestamo.setVisible(false);
        this.botonExportar.setVisible(false);
        this.botonBorrar.setVisible(false);
        this.botonImportar.setVisible(false);
        this.botonReservar.setVisible(false);
        
        if ( this.controlador.getUsuarioActivo().hasPermiso(EnumPermisos.GESTION_RESERVAS) )
            this.botonReservar.setVisible(true);
        
        if ( this.controlador.getUsuarioActivo().hasPermiso(EnumPermisos.GESTION_PRESTAMOS) ) {
            this.botonPrestamo.setVisible(true);
            this.botonExportar.setVisible(true);
            this.botonImportar.setVisible(true);
        }
        
        if ( this.controlador.getUsuarioActivo().hasPermiso(EnumPermisos.GESTION_MEDIOS))
            this.botonBorrar.setVisible(true);
        
        count = this.tablaMedios.getSelectedRowCount();
        row   = this.tablaMedios.getSelectedRow();
        
        if ( count < 1 || row < 0 )
            return;
        
        row   = this.tablaMedios.convertRowIndexToModel(row);
        model = this.getModeloTabla();
        m     = model.getMedioAt(row);
 
        if ( m == null ) 
            return;
        
        this.botonExportar.setEnabled(true);
        this.botonBorrar.setEnabled(true);
        
        if ( count > 1 )
            return;
        
        if ( m.getEstado() == EnumEstadosPrestamo.DISPONIBLE )
            this.botonPrestamo.setEnabled(true);
        else
            this.botonReservar.setEnabled(true);
        
        if ( m.getAtributo(EnumTiposAtributo.SUSCRIPCION) != null )
            this.botonSuscribirse.setEnabled(true);
    }
    
    /**
     * Pide al usuario que introduzca el nombre de un usuario válido.
     * 
     * @return Un usuario válido.
     * @throws Exception Si ocurre un error a la hora de intentar encontrar
     * al usuario o procesar la entrada de datos.
     */
    private Usuario pedirUsuario() throws Exception {
        Usuario u = null;
        String tx;

        tx = JOptionPane.showInputDialog(
            this, 
            "Introduce el login del usuario para el que se realizará el " +
                    "préstamo", 
            "Introduce usuario"
        );
        
        if ( tx != null && ! tx.isEmpty() )
            u = this.controlador.getBibliotecaActiva().getUsuario(tx);
        
        if ( u == null )
            throw new Exception("Usuario inválido.");
        
        return u;
    }
    
    /**
     * Pide al usuario que introduzca un número entero que equivale al tiempo
     * de duración de un préstamo.
     * 
     * @return Entero entre 1 y Biblioteca.DIAS_PRESTAMO
     * @throws Exception Si se introduce un valor erróneo o sucede algún error 
     * en la solicitud.
     */
    private int pedirDias() throws Exception {
        String tx;
        int i;
        
        tx = JOptionPane.showInputDialog(
            this, 
            "Introduzca el número de días que durará el préstamo " +
                "(máximo " + Biblioteca.DIAS_PRESTAMO + ")",
            "Introduce los días del préstamo"
        );
        
        try {
            i = Integer.parseInt(tx);
            
            if ( i < 1 || i > Biblioteca.DIAS_PRESTAMO )
                throw new NumberFormatException();
        }
        catch (NumberFormatException e) {
            throw new Exception("Número de días inválido");
        }
        
        return i;
    }
    
    /**
     * Muestra un diálogo de selección de fichero que será usado para guardar
     * el fichero de exportación.
     * 
     * @return La ruta absoluta del fichero o 'null'
     */
    private String pedirRutaExportar() {
        int resultado;
        
        this.fileChooserExportar.setDialogTitle(
            "Elige ruta del fichero de exportación."
        );
        resultado = this.fileChooserExportar.showSaveDialog(this);

        if ( resultado != JFileChooser.APPROVE_OPTION )
            return null;
        
        return this.fileChooserExportar.getSelectedFile().getAbsolutePath();
    }
    
    /**
     * Pide al usuario que seleccione un archivo existente que será usado
     * para importar.
     * 
     * @return La ruta absoluta del fichero o 'null'
     */
    private String pedirRutaImportar() {
        int resultado;
        
        this.fileChooserExportar.setDialogTitle(
            "Elige el archivo de importación."
        );
        resultado = this.fileChooserExportar.showOpenDialog(this);
        
        if ( resultado != JFileChooser.APPROVE_OPTION )
            return null;
        
        return this.fileChooserExportar.getSelectedFile().getAbsolutePath();
    }
    
    /**
     * Intenta exportar los medios seleccionados a un archivo .csv
     */
    private void exportar() {
        List<MedioIF> medios;
        boolean ok;
        String path;
        
        ok   = false;
        path = this.pedirRutaExportar();
        
        if ( path == null )
            return;
        
        if ( this.tablaMedios.getSelectedRowCount() < 1 )
            return;
        
        medios = new ArrayList<>();
        for(int fila: this.tablaMedios.getSelectedRows()) {
            MedioIF m;
            
            m = this.getMedioFila(fila);
            
            if ( m == null ) {
                this.controlador.mostrarAviso(
                    "El medio de la fila " + fila + " no puede cargarse."
                );
                return;
            }
            medios.add(this.getMedioFila(fila));
        }
        
        try {
            ok = this.controlador.getBibliotecaActiva().exportarMedios(
                path + ".csv" ,
                medios
            );
        }
        catch (FileSystemException |FileNotFoundException e) {
            this.controlador.procesarError(e);
        }
        
        if ( ! ok )
            return;
        
        this.controlador.actualizaEscritorio();
        this.controlador.mostrarAviso("Exportación realizada con éxito.");
    }
    
    /**
     * Intenta importar un fichero de medios exportados desde otra biblioteca.
     */
    private void importar(){
        String path = this.pedirRutaImportar();
        int count, cedidos;
        
        if ( path == null )
            return;
        
        count   = this.controlador.getBibliotecaActiva().getMedios().size();
        cedidos = this.controlador.getBibliotecaActiva().getMediosCedidosAOtras().size();
        
        try {
            if ( ! this.controlador.getBibliotecaActiva().importarMedios(path) )
                return;
        } catch (FileNotFoundException | FileSystemException ex) {
            this.controlador.procesarError(ex);
            return;
        }
        
        count   = count   - this.controlador.getBibliotecaActiva().getMedios().size();
        cedidos = cedidos - this.controlador.getBibliotecaActiva().getMediosCedidosAOtras().size();
                
        this.controlador.actualizaEscritorio();
        this.controlador.mostrarAviso(
            "Importados " + Math.abs(count) + " nuevos medios.\n" +
            "Recuperados " + Math.abs(cedidos) + " medios previamente cedidos.\n"
        );
    }
    
    /**
     * Intenta suscribirse al medio seleccionado.
     */
    private void suscribirse() {
        TablaMediosModel model;
        AtributoMedio suscrip;
        Usuario u;
        MedioIF m;
        int i;
        
        u = this.controlador.getAplicacion().getUsuarioActivo();
        
        if ( this.tablaMedios.getSelectedRowCount() < 1) {
            this.controlador.mostrarAviso(
                "Has de seleccionar un medio al que quieras suscribirte."
            );
            return;
        }
        
        if ( this.tablaMedios.getSelectedRowCount() > 1 ) {
            this.controlador.mostrarAviso(
                "No puedes suscribirte a más de un elemento a la vez."
            );
            return;
        }
            
        
        model   = this.getModeloTabla();
        i       = this.tablaMedios.getSelectedRow();
        i       = this.tablaMedios.convertRowIndexToModel(i);
        m       = model.getMedioAt(i);
        
        if ( m == null ) {
            this.controlador.mostrarError(
                "Ocurrió un error al cargar el elemento seleccionado."
            );
            return;
        }
            
        suscrip = m.getAtributo(EnumTiposAtributo.SUSCRIPCION);
             
        if ( suscrip == null || suscrip.getValor() == null ) {
            this.controlador.mostrarAviso(
                "No puedes suscribirte a " + m.getTitulo() + " porque " +
                    "el medio no ha definido una suscripción."
            );
            return;
        }
            
        if ( u.isSuscrito(suscrip.getValor().toString()) ) {
            this.controlador.mostrarAviso(
                "Ya estás sucrito a " + suscrip.getValor().toString() + "."
            );
            return;
        }
        
        u.addSuscripcion(suscrip.getValor().toString());
        this.controlador.mostrarAviso(
            "Te has suscrito con éxito a " + suscrip.getValor().toString() + "."
        );
        this.controlador.actualizaEscritorio();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        fileChooserExportar = new javax.swing.JFileChooser();
        panelIzquierdo = new javax.swing.JPanel();
        botonImportar = new javax.swing.JButton();
        botonExportar = new javax.swing.JButton();
        botonBorrar = new javax.swing.JButton();
        botonPrestamo = new javax.swing.JButton();
        botonReservar = new javax.swing.JButton();
        botonSuscribirse = new javax.swing.JButton();
        scrollTabla = new javax.swing.JScrollPane();
        tablaMedios = new javax.swing.JTable();

        fileChooserExportar.setApproveButtonText("Guardar");
        fileChooserExportar.setDialogTitle("Exportar a fichero");

        setTitle("Medios");

        panelIzquierdo.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        botonImportar.setText("Importar");
        botonImportar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonImportarActionPerformed(evt);
            }
        });
        panelIzquierdo.add(botonImportar);

        botonExportar.setText("Exportar");
        botonExportar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonExportarActionPerformed(evt);
            }
        });
        panelIzquierdo.add(botonExportar);

        botonBorrar.setText("Borrar");
        botonBorrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonBorrarActionPerformed(evt);
            }
        });
        panelIzquierdo.add(botonBorrar);

        botonPrestamo.setText("Pedír préstamo");
        botonPrestamo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonPrestamoActionPerformed(evt);
            }
        });
        panelIzquierdo.add(botonPrestamo);

        botonReservar.setText("Reservar");
        botonReservar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonReservarActionPerformed(evt);
            }
        });
        panelIzquierdo.add(botonReservar);

        botonSuscribirse.setText("Suscribirse");
        botonSuscribirse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonSuscribirseActionPerformed(evt);
            }
        });
        panelIzquierdo.add(botonSuscribirse);

        tablaMedios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tablaMedios.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                tablaMediosMouseDragged(evt);
            }
        });
        tablaMedios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaMediosMouseClicked(evt);
            }
        });
        scrollTabla.setViewportView(tablaMedios);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelIzquierdo, javax.swing.GroupLayout.DEFAULT_SIZE, 852, Short.MAX_VALUE)
            .addComponent(scrollTabla)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(scrollTabla, javax.swing.GroupLayout.DEFAULT_SIZE, 475, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelIzquierdo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }

    private void botonBorrarActionPerformed(java.awt.event.ActionEvent evt) {
        this.borrar();
    }

    private void botonSuscribirseActionPerformed(java.awt.event.ActionEvent evt) {
        this.suscribirse();
    }

    private void botonPrestamoActionPerformed(java.awt.event.ActionEvent evt) {
        this.prestamo();
    }

    private void tablaMediosMouseClicked(java.awt.event.MouseEvent evt) {
        this.actualizarBotones();
    }

    private void tablaMediosMouseDragged(java.awt.event.MouseEvent evt) {
        this.actualizarBotones();
    }

    private void botonReservarActionPerformed(java.awt.event.ActionEvent evt) {
        this.reservar();
    }

    private void botonExportarActionPerformed(java.awt.event.ActionEvent evt) {
        this.exportar();
    }

    private void botonImportarActionPerformed(java.awt.event.ActionEvent evt) {
        this.importar();
    }

    private javax.swing.JButton botonBorrar;
    private javax.swing.JButton botonExportar;
    private javax.swing.JButton botonImportar;
    private javax.swing.JButton botonPrestamo;
    private javax.swing.JButton botonReservar;
    private javax.swing.JButton botonSuscribirse;
    private javax.swing.JFileChooser fileChooserExportar;
    private javax.swing.JPanel panelIzquierdo;
    private javax.swing.JScrollPane scrollTabla;
    private javax.swing.JTable tablaMedios;
}
