package iu.swing;

import java.awt.print.PrinterException;
import javax.swing.table.DefaultTableModel;
import usuarios.Usuario;

/**
 * Widget que muestra una versión muy primitiva de la tarjeta de un usuario
 * y permite imprimirla.
 * 
 * @author Héctor Luaces Novo
 */
public class WidgetTarjetaUsuario extends javax.swing.JInternalFrame implements ModuloIUSwingIF {
    /**
     * ControladorSwing asociado a éste widget
     */
    private ControladorSwing controlador;
    
    /**
     * Usuario para el que estamos mostrando la tarjeta
     */
    private Usuario usuario;
    
    /**
     * Constructor privado que inicializa los elementos gráficos.
     * 
     * @see WidgetTarjetaUsuario#WidgetTarjetaUsuario(iu.swing.ControladorSwing) 
     */
    private WidgetTarjetaUsuario() {
        initComponents();
    }

    /**
     * Crea un nuevo widget para mostrar la tarjeta.
     * 
     * @param c ControladorSwing para inyectar al widget.
     * @param u Usuario para el que mostraremos la tarjeta.
     */
    public WidgetTarjetaUsuario(ControladorSwing c, Usuario u) {
        this();
        
        controlador = c;
        usuario     = u;
                
        this.setControlador(c);
        this.actualizar();
    }
    
    @Override
    public final void setControlador(ControladorSwing c) {
        this.controlador = c;
    }
    
    @Override
    public final void actualizar() {
        DefaultTableModel model = new DefaultTableModel();
        String columnas[], campos[];
        
        this.setTitle("Tarjeta de " + usuario.getLogin());
        columnas = WidgetGestionUsuarios.getArrayCamposUsuario();
        campos   = WidgetGestionUsuarios.getArrayDatosUsuario(usuario);
        model.setColumnIdentifiers(new String[] {"", ""});
        
        for(int i = 0; i < columnas.length; i++) {
            model.addRow(new String[] {columnas[i], campos[i]});
        }
        
        this.tablaTarjeta.setModel(model);
        this.tablaTarjeta.setDefaultEditor(Object.class, null);        
    }
    
    /**
     * Muestra un diálogo de impresión para imprimir la tabla que contiene
     * los datos del usuario.
     */
    private void imprimir() {
        try {
            this.tablaTarjeta.print();
        }
        catch(PrinterException e) {
            this.controlador.procesarError(e);
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        scroll = new javax.swing.JScrollPane();
        tablaTarjeta = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        botonImprimir = new javax.swing.JButton();
        botonCerrar = new javax.swing.JButton();

        tablaTarjeta.setModel(new javax.swing.table.DefaultTableModel(
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
        tablaTarjeta.setShowHorizontalLines(false);
        tablaTarjeta.setShowVerticalLines(false);
        scroll.setViewportView(tablaTarjeta);

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        botonImprimir.setText("Imprimir");
        botonImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonImprimirActionPerformed(evt);
            }
        });
        jPanel1.add(botonImprimir);

        botonCerrar.setText("Cerrar");
        botonCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCerrarActionPerformed(evt);
            }
        });
        jPanel1.add(botonCerrar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scroll, javax.swing.GroupLayout.DEFAULT_SIZE, 302, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(scroll, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }

    private void botonImprimirActionPerformed(java.awt.event.ActionEvent evt) {
        this.imprimir();
    }

    private void botonCerrarActionPerformed(java.awt.event.ActionEvent evt) {
        this.controlador.cerrarFrame(this);
    }


    private javax.swing.JButton botonCerrar;
    private javax.swing.JButton botonImprimir;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane scroll;
    private javax.swing.JTable tablaTarjeta;
}
