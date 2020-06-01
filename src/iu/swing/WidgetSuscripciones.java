package iu.swing;

import java.awt.Component;
import java.util.Set;
import javax.swing.JCheckBox;
import usuarios.Usuario;

/**
 * Widget que muestra al usuario la lista de sus suscripciones, permitiéndole
 * eliminar cualquiera de ellas.
 * 
 * @author Héctor Luaces Novo
 */
class WidgetSuscripciones extends javax.swing.JInternalFrame implements ModuloIUSwingIF {
    /**
     * El ControladorSwing que usará éste Widget
     */
    private ControladorSwing controlador;
    
    /**
     * Inicializa los componentes gráficos.
     * 
     * Constructor privado.
     * 
     * @see WidgetSuscripciones#WidgetSuscripciones(iu.swing.ControladorSwing) 
     */
    private WidgetSuscripciones() {
        initComponents();
    }

    /**
     * Crea un nuevo widget de suscripciones.
     * 
     * @param c El ControladorSwing que usará éste widget.
     */
    public WidgetSuscripciones(ControladorSwing c) {
        this();
        
        this.setControlador(c);
        this.actualizar();
    }
    
    /**
     * Crea un checkbox con cada una de las suscripciones a las que está
     * añadido el usuario.
     */
    private void inicializarSuscripciones() {
        Usuario u = this.controlador.getAplicacion().getUsuarioActivo();
        Set<String> suscripciones = u.getSuscripciones();
        
        this.panelSuscripciones.removeAll();
        this.botonDesuscribirse.setEnabled(false);
        
        if ( suscripciones.size() == 0 )
            return;
        
        for(String s: suscripciones) {
            this.panelSuscripciones.add(new JCheckBox(s));
        }
        
        this.botonDesuscribirse.setEnabled(true);
    }
    
    /**
     * Desuscribe al usuario de las suscripciones cuyo checkbox esté marcado
     */
    private void desuscribirse() {
        Usuario u = this.controlador.getAplicacion().getUsuarioActivo();
        int i = 0;
        
        for(Component c: this.panelSuscripciones.getComponents()) {
            JCheckBox caja;
            
            if ( ! ( c instanceof JCheckBox) )
                continue;
            
            caja = (JCheckBox) c;
            
            if ( ! caja.isSelected() )
                continue;
            
            if ( ! u.isSuscrito(caja.getText()) )
                continue;
            
            u.removeSuscription(caja.getText());
            caja.setVisible(false);
            i++;
        }
        
        if ( i != 0 )
            this.controlador.mostrarAviso(
                "Desuscrito correctamente de " + i + " elemento" + 
                        (i > 1 ? "s" : "") + "."
            );
        
        if ( u.getSuscripciones().isEmpty() )
            this.botonDesuscribirse.setEnabled(false);
    }
    
    @Override
    public final void setControlador(ControladorSwing c) {
        this.controlador = c;
    }
    
    @Override
    public final void actualizar() {
        this.inicializarSuscripciones();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        panelSuscripciones = new javax.swing.JPanel();
        panelBotones = new javax.swing.JPanel();
        botonDesuscribirse = new javax.swing.JButton();
        botonCancelar = new javax.swing.JToggleButton();

        setResizable(true);
        setTitle("Suscripciones");

        botonDesuscribirse.setText("Eliminar suscripciones seleccionadas");
        botonDesuscribirse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonDesuscribirseActionPerformed(evt);
            }
        });

        botonCancelar.setText("Cancelar");
        botonCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelBotonesLayout = new javax.swing.GroupLayout(panelBotones);
        panelBotones.setLayout(panelBotonesLayout);
        panelBotonesLayout.setHorizontalGroup(
            panelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBotonesLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(botonDesuscribirse)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(botonCancelar)
                .addContainerGap())
        );
        panelBotonesLayout.setVerticalGroup(
            panelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBotonesLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(botonDesuscribirse)
                    .addComponent(botonCancelar))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelBotones, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panelSuscripciones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelSuscripciones, javax.swing.GroupLayout.DEFAULT_SIZE, 244, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelBotones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }

    private void botonCancelarActionPerformed(java.awt.event.ActionEvent evt) {
        this.dispose();
    }

    private void botonDesuscribirseActionPerformed(java.awt.event.ActionEvent evt) {
        this.desuscribirse();
    }

    private javax.swing.JToggleButton botonCancelar;
    private javax.swing.JButton botonDesuscribirse;
    private javax.swing.JPanel panelBotones;
    private javax.swing.JPanel panelSuscripciones;
}
