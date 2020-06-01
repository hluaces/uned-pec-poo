package iu.swing;

import java.awt.Component;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import misc.StringUtil;
import usuarios.EnumPermisos;
import usuarios.Usuario;

/**
 * Héctor Luaces Novo
 * 
 * Widget básico con una tabla para mostrar datos diseñado para que otros 
 * hereden de él y no sea necesario tener que reescribirmucho código similar.
 * 
 * @author Héctor Luaces Novo
 * @param <K> El tipo de datos que representa la tabla. Pej: si mostramos una
 * tabla de datos de usuario, K será "usuario".
 */
public abstract class AbstractWidgetConTabla<K> extends javax.swing.JInternalFrame implements ModuloIUSwingIF {
    /**
     * El controladorSwing asociado al widget.
     */
    private ControladorSwing controlador;
    
    /**
     * El nombre de las columnas que tendrá la tabla.
     */
    protected String[] columnas;
    
    /**
     * Al mostrar el widget se cargarán los datos a representar en la tabla
     * y se almacenarán en este set.
     */
    protected Set<K> datos;
    
    /**
     * El usuario para el que mostrarems los datos de la tabla (por si es 
     * necsario reutilizar el widget no solo para ver nuestros datos, si no
     * para ver los datos de otro usuario).
     * 
     * Por defecto se establece el usuario activo.
     */
    private Usuario usuario;
    
    /**
     * Constructor protegido que inicializa los componentes gráficos.
     * 
     * @see WidgetConTabla#WidgetConTabla(iu.swing.ControladorSwing) 
     * @see WidgetConTabla#WidgetConTabla(iu.swing.ControladorSwing, usuarios.Usuario) 
     */
    protected AbstractWidgetConTabla() {
        initComponents();
    }

    /**
     * Crea un nuevo WidgetConTabla inyectándole un ControladorSwing.
     * 
     * Se usará como usuario el usuario activo.
     * 
     * @param c ControladorSwing a inyectar.
     */
    protected AbstractWidgetConTabla(ControladorSwing c) {
        this();
        
        this.setControlador(c);
        this.actualizar();
    }
    
    /**
     * Crea un nuevo WidgetConTabla, inyectándole un ControladorSwing
     * y un usuario para el que se mostrarán los datos.
     * 
     * @param c ControladorSwing a inyectar.
     * @param u Usuario a inyectar.
     */
    protected AbstractWidgetConTabla(ControladorSwing c, Usuario u) {
        this();
        
        this.usuario = u;
        this.setControlador(c);
        this.actualizar();
    }
    
    @Override
    public void setControlador(ControladorSwing c) {
        this.controlador = c;
    }
    
    @Override
    public void actualizar() {
        this.cargarUsuario();
        this.cargarDatos();
        this.prepararTabla(this.crearModeloTabla());
        
        for(K dato: datos)
            this.addRow(dato);
        this.actualizarBotones();
    }
    
    /**
     * Determina si el usuario activo en este widget tiene un permiso
     * pasado como parámetro.
     * 
     * @param p el permiso que queremos comprobar.
     * @return true si tiene dicho permiso o falso si no lo tiene.
     */
    protected final boolean tienePermiso(EnumPermisos p) {
        return this.getUsuario().hasPermiso(p);
    }
    
    /**
     * Compara un array de Strings con un Object[] o un vector y determina si 
     * ambos contienen la misma información.
     * 
     * Util para comparar un vector[] de un tableModel con un array de Strings
     * que puede venir, por ejemplo, de parsear un objeto determinado.
     * 
     * @param datos String[] de datos a comparar.
     * @param datosObjeto Object[] de objetos a comparar.
     * 
     * @return True si son iguales o falso de cualquier otra forma.
     */
    protected boolean compararArrayYObjeto(String datos[], Object datosObjeto[]) {
        if ( datos.length != datosObjeto.length )
            return false;
        
        for(int i = 0; i < datos.length; i++) {
            boolean ok = false;
            
            if ( datosObjeto[i] == null ) 
                ok = datos[i] == null || datos[i].toString().isEmpty();
            else if ( datosObjeto[i] instanceof Date )
                ok = datos[i].equals(StringUtil.fechaToString((Date) datosObjeto[i]));
            else if ( datosObjeto[i] instanceof Boolean )
                ok = datos[i].equals(this.getTextoBoolean((Boolean) datosObjeto[i]));
            else {
                ok = datos[i].equals(datosObjeto[i].toString());
            }
            
            if ( ! ok )
                return false;
        }
        
        return true;
    }
    
    /**
     * Convierte un boolean en un texto 'S' o 'N'
     * 
     * @param s Booleano a convertir
     * @return Texto covertido ("S" o "N")
     */
    protected final String getTextoBoolean(Boolean s) {
        return s ? "S" : "N";
    }
    
    /**
     * Llamado al actualizar.
     * 
     * Por defecto, si el usuario es 'null' se cargará el usuario 
     * por defecto.
     * 
     * De esta forma, si se establece antes otro usuario (con el constructor,
     * por ejemplo) no se sobreescribirá al actualizar.
     */
    protected void cargarUsuario() {
        if ( this.getUsuario() == null )
            this.usuario = this.getControlador().getUsuarioActivo();
    }
    
    /**
     * Establece un nuevo usuario activo y actualiza el Widget.
     * 
     * @param u Usuario a inyectar.
     */
    public void setUsuario(Usuario u) {
        this.usuario = u;
        this.actualizar();
    }
    
    /**
     * Prepara la tabla de datos que será mostrada en el Widget, estableciéndole
     * un modelo pasado como parámetro, una selección "single" y desactivando
     * la opción de que pueda ser editada.
     * 
     * Esta función está diseñada para ser sobrecargada y personalizada
     * en cada Widget.
     * 
     * @param modelo Modelo a inyectar en la tabla.
     * @return La tabla que hemos preparado.
     */
    protected JTable prepararTabla(DefaultTableModel modelo) {
        List keys = null;
        
        if ( this.tabla.getRowSorter() != null )
            keys = this.tabla.getRowSorter().getSortKeys();
        
        this.tabla.setModel(modelo);
        
        this.tabla.setDefaultEditor(Object.class, null);
        this.tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        if ( keys == null || keys.isEmpty() )
            this.tabla.setAutoCreateRowSorter(true);
        else
            this.tabla.getRowSorter().setSortKeys(keys);
        
        return this.tabla;
    }
    
    /**
     * Devuelve el modelo activo actualmente en la tabla, haciendole casting a
     * DefaultTableModel.
     * 
     * @return El modelo activo en la tabla.
     */
    protected DefaultTableModel getModeloTabla() {
        return (DefaultTableModel) this.tabla.getModel();
    }
    
    /**
     * Método diseñado para ser sobrecargado.
     * 
     * Carga los datos con los que trabajará la tabla para el usuario dado.
     */
    protected void cargarDatos() {
        datos = new HashSet<>();
    }

    /**
     * Método diseñado para ser sobrecargado.
     * 
     * Crea el modelo por defecto que será usado en la tabla de datos, 
     * estableciendo cualquier configuración inicial que sea necesaria.
     * 
     * Por defectoañade los nombres de las columnas que se hayan especificado
     * en {@link AbstractWidgetConTabla#columnas}
     * 
     * @return El modelo que será usado en la tabla de datos
     */
    protected DefaultTableModel crearModeloTabla() {
        DefaultTableModel modelo;
        
        modelo = new DefaultTableModel();
        modelo.setColumnIdentifiers(this.columnas);
        
        return modelo;
    }

    /**
     * Devuelve un vector con los datos de la tabla para una fila que pasemos
     * como primer parámetro (o null si no hay datos)
     * 
     * NOTA: se usa vector porque {@link  DefaultTableModel#getDataVector()} 
     * usa dicha colección obsoleta, no por un motivo de diseño.
     * 
     * @param fila Fila para la que queremos conocer los datos.
     * @return Vector Vector con los datos de la fila (o null, si no hay datos)
     */
    protected final Vector getDatosFila(int fila) {
        if ( fila < 0 )
            return null;
        
        fila = this.tabla.convertRowIndexToModel(fila);
        
        return (Vector) this.getModeloTabla().getDataVector().get(fila);
    }
    
    /**
     * Devuelve un vector con los datos de la fila seleccionada en la tabla.
     * 
     * @see AbstractWidgetConTabla#getDatosFila(int) 
     * @return El vector de datos para la fila seleccionada (o null, si no hay
     * datos o fila seleccionada)
     */
    protected final Vector getDatosFilaSeleccionada() {
        return this.getDatosFila(this.tabla.getSelectedRow());
    }
    
    /**
     * Función diseñada para ser sobrecargada.
     * 
     * Dada una fila de la tabla del widget, busca en el 
     * {@link AbstractWidgetConTabla#datos set de datos subyacente} el dato 
     * correspondiente y lo devuelve.
     * 
     * Pej: si tenemos una tabla de Usuarios y pedimos la fila 2, esta función
     * nos devolverá el Usuario correspondiente.
     * 
     * @param fila Fila para la que queremos saber el dato subyacente.
     * @return El dato subyacente o "null".
     */
    protected K getObjetoFila(int fila) {
        return null;
    }

    /**
     * Método de conveniencia para {@link AbstractWidgetConTabla#getObjetoFila} que
     * devuelve el objeto seleccionado actualmente o 'null'.
     * 
     * @return El objeto seleccionado actualmente en la tabla o 'null'.
     */
    protected final K getObjetoFilaSeleccionada() {
        return this.getObjetoFila(this.tabla.getSelectedRow());
    }
    
    /**
     * Método diseñado para ser sobrecargado.
     * 
     * Añade a la tabla una fila, dado un objeto del 
     * {@link AbstractWidgetConTabla#datos set de datos subyacente.}
     * 
     * Cuando se carga el Widget se llamará tantas veces a este método como 
     * datos tengamos para mostrar en la tabla.
     * 
     * La responsabilidad del método es la de añadir dicho dato a la tabla.
     * 
     * @param item dato a mostrar en la tabla.
     */
    protected void addRow(K item) {
    }
    
    /**
     * Devuelve la tabla de datos usada en el widget.
     * 
     * Existe para garantizar acceso a la misma, puesto que esta es private.
     * 
     * @return la tabla de datos usada en el widget.
     */
    protected JTable getTabla() {
        return this.tabla;
    }
    

    /**
     * Cierra este Widget
     */
    protected void cerrar() {
        this.getControlador().cerrarFrame(this);
    }
    
    /**
     * Método diseñado para ser sobrecargado.
     * 
     * Actualiza los botones de la tabla acorde a la fila que tengamos 
     * seleccionada, pej: nos puede intersar desactivar ciertos botones
     * si solo está seleccionado un objeto o esconder botones con funcionalidades
     * para las que un usuario no tiene permisos.
     *  
     * Se dispara al hacer 'click' o 'drag' en la tabla.
     */
    protected void actualizarBotones() {
        
    }
    
    /**
     * Añade un botón al panel de botones del Widget.
     * 
     * @param b Botón a añadir al widget.
     * @return El botón que se ha añadido
     */
    protected final JButton addBoton(JButton b) {
        // Eliminamos el de cerrar y lo volvemos a añadir para que el layout
        // controller lo mueva a la derecha
        this.panelBotones.remove(this.botonCerrar);
        this.panelBotones.add(b);
        this.panelBotones.add(this.botonCerrar);
        return b;
    }
    
    /**
     * Añade un componente swing a la cabecera del widget
     * @param c Componente a añadir.
     */
    protected final void addCabecera(Component c) {
        this.panelCabecera.add(c);
    }

    /**
     * Devuelve el controladorSwing del widget.
     * 
     * @return El ControladorsWing del widget.
     */
    public ControladorSwing getControlador() {
        return controlador;
    }

    /**
     * Devuelve el usuario activo del Widget.
     * 
     * @return El usuario activo del Widget.
     */
    public Usuario getUsuario() {
        return usuario;
    }
    
    /**
     * Dos AbstractWidgetConTabla serán iguales si su controlador y usuario
     * son el mismo, de esta forma podrá cachearse de forma eficiente en el
     * {@link ControladorSwing} y en el {@link UIBiblioteca}
     * 
     * @param obj El objeto a comparar
     * @return true si los objetos son iguales, false de cualquier otra forma.
     */
    @Override
    public boolean equals(Object obj) {
        AbstractWidgetConTabla ob;
        
        if ( obj == null || obj.getClass() != this.getClass() )
            return super.equals(obj); 
        
        ob = (AbstractWidgetConTabla) obj;
        
        return this.controlador == ob.getControlador() && 
               this.usuario     == ob.getUsuario() &&
                this.getClass() == ob.getClass()
        ;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 11 * hash + Objects.hashCode(this.getControlador());
        hash = 11 * hash + Objects.hashCode(this.getUsuario());
        return hash;
    }
    
    
    @SuppressWarnings("unchecked")
    private void initComponents() {

        panelCabecera = new javax.swing.JPanel();
        scroll = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        panelBotones = new javax.swing.JPanel();
        botonCerrar = new javax.swing.JButton();

        panelCabecera.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        tabla.setModel(new javax.swing.table.DefaultTableModel(
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
        tabla.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                tablaMouseDragged(evt);
            }
        });
        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaMouseClicked(evt);
            }
        });
        scroll.setViewportView(tabla);

        panelBotones.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        botonCerrar.setText("Cerrar");
        botonCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCerrarActionPerformed(evt);
            }
        });
        panelBotones.add(botonCerrar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scroll, javax.swing.GroupLayout.DEFAULT_SIZE, 662, Short.MAX_VALUE)
            .addComponent(panelBotones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panelCabecera, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelCabecera, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scroll, javax.swing.GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelBotones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        panelCabecera.getAccessibleContext().setAccessibleDescription("");

        pack();
    }

    private void botonCerrarActionPerformed(java.awt.event.ActionEvent evt) {
        this.cerrar();
    }

    private void tablaMouseDragged(java.awt.event.MouseEvent evt) {
        this.actualizarBotones();
    }

    private void tablaMouseClicked(java.awt.event.MouseEvent evt) {
        this.actualizarBotones();
    }

    private javax.swing.JButton botonCerrar;
    private javax.swing.JPanel panelBotones;
    private javax.swing.JPanel panelCabecera;
    private javax.swing.JScrollPane scroll;
    private javax.swing.JTable tabla;
}
