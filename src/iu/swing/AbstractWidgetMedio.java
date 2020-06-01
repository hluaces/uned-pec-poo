package iu.swing;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import medios.AtributoMedio;
import medios.EnumTiposAtributo;
import medios.MediosFactory;

/**
 * Clase abstracta que representa un Widget que presenta al usuario un textField
 * por cada posible campo que pueda tener un medio (incluyendo un combobox
 * para discriminar).
 * 
 * La idea de la clase abstracta es que ésta pueda ser reutilizada en distintos
 * tipos de módulos, pej:
 * 
 * - Un módulo que pida al usuario datos para cada uno de los campos y 
 * posteriormente cree un nuevo medio con esos datos.
 * - Un módulo que pida al usuario datos para crear una búsqueda por cada uno 
 * de los distintos campos.
 * 
 * @author Héctor Luaces Novo
 */
abstract class AbstractWidgetMedio extends javax.swing.JInternalFrame implements ModuloIUSwingIF {
    /**
     * El controladorSwing utilizado por el frame
     */
    protected ControladorSwing controlador;
    
    /**
     * Aquí guardaremos dinámicamente la referencia de los controles secundarios.
     * 
     * Un 'control secundario' es todo aquel que no pertenece a un 
     * {@link EnumTiposAtributo#principal atributo principal}
     */
    protected Map<EnumTiposAtributo, JTextField> controlesSecundarios;
    
    /**
     * Inicializa los componentes gráficos.
     * 
     * Constructor privado.
     * 
     * @see AbstractWidgetMedio#AbstractWidgetMedio(iu.swing.ControladorSwing) 
     */
    private AbstractWidgetMedio() {
        initComponents();
    }

    /**
     * Constructor principal de la clase que toma como parámetro el 
     * controladorSwing de la ejecución actual del programa (inyección de 
     * dependencias).
     * 
     * @param c El ControladorSwing ante el que responderá el frame
     */
    public AbstractWidgetMedio(ControladorSwing c) {
        this();
        
        this.setControlador(c);
    }
    
    @Override
    public void setControlador(ControladorSwing c) {
        this.controlador = c;
        this.actualizar();
    }
    
    @Override
    public void actualizar() {
        this.inicializarCombo();
    }
    
    /**
     * Inicializa el combo de la herramienta añadiendo el nombre de todos
     * los tipos de medio para que los controles de los campos secundarios
     * se adapten a la selección de éste.
     */
    protected void inicializarCombo() {
        this.comboTipo.removeAllItems();
        
        for(String tipo: MediosFactory.getTiposMedio())
            this.comboTipo.addItem(tipo);
    }

    /**
     * Dado un nombre de tipo de medio, devuelve un Set con la lista de 
     * atributos vacíos que soporta dicho medio.
     * 
     * @param tipo El nombre de un tipo de medio
     * @return Un Set con los atributos de ese tipo de medio con valores
     * sin inicializar.
     */
    protected Set<AtributoMedio> dameAtributosParaCampos(String tipo) {
        Set<AtributoMedio> ret;
        
        ret = MediosFactory.dameAtributosMedio(tipo);
        ret.removeIf((c) -> c.getNombre().isPrincipal());
        
        return ret;
    }
    
    /**
     * Cada vez que se pulse 'enter' o 'escape' en un control primario o
     * secundario se llamará a éste evento.
     * 
     * Existe para hacer la interfaz de usuario más fluída e intuitiva
     * para el usuario.
     * 
     * @param evt El KeyEvent que se ha disparado.
     */
    protected void gestionarKeyEvent(KeyEvent evt) {
        switch(evt.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                this.cerrar();
                break;
            
            case KeyEvent.VK_ENTER:
                this.ejecutar();
                break;
        }
    }

    /**
     * Llamada al cambiar el tipo de medio en el combo de medios.
     * 
     * Vaciará los controles secundarios y generará nuevos acorde
     * al tipo de medio seleccionado.
     */
    protected void generarNuevosAtributos() {
        String tipo;

        if ( this.comboTipo.getSelectedItem() == null )
            return;
        
        tipo = this.comboTipo.getSelectedItem().toString();
        
        if ( tipo == null )
            return;
        
        this.controlesSecundarios = new HashMap<>();
        this.panelAtributosSecundarios.removeAll();
        
        for (AtributoMedio atr: this.dameAtributosParaCampos(tipo)) {
            JTextField txt;
            JLabel label;
            JPanel panel;

            label  = new JLabel(atr.getNombre().getNombre());
            txt    = new JTextField(20);
            
            panel  = new JPanel();
            panel.add(label);
            panel.add(txt);
            this.panelAtributosSecundarios.add(panel);
            this.controlesSecundarios.put(atr.getNombre(), txt);
            
            txt.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {
                }

                @Override
                public void keyPressed(KeyEvent e) {
                    gestionarKeyEvent(e);
                }

                @Override
                public void keyReleased(KeyEvent e) {
                }
            });
            
        }
        
        this.panelAtributosSecundarios.revalidate();
        this.pack();
    }
    
    /**
     * Cierra esta ventana.
     */
    private void cerrar() {
        this.controlador.cerrarFrame(this);
    }
    
    /**
     * Método abstracto que se llamará cuando se pulse el botón de acción (
     * guardar, salvar, etc).
     * 
     * La implementación viaará en función de las clases hija, pudiendo ésta
     * ser capaz de crear nuevos medios, ejecutar búsquedas, etc.
     */
    abstract protected void ejecutar();

    @SuppressWarnings("unchecked")
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panelAtributosPrimarios = new javax.swing.JPanel();
        labelTipo = new javax.swing.JLabel();
        comboTipo = new javax.swing.JComboBox<>();
        labelAutor = new javax.swing.JLabel();
        txtAutor = new javax.swing.JTextField();
        labelTitulo = new javax.swing.JLabel();
        txtTitulo = new javax.swing.JTextField();
        labelGenero = new javax.swing.JLabel();
        txtGenero = new javax.swing.JTextField();
        labelFecha = new javax.swing.JLabel();
        txtFecha = new javax.swing.JTextField();
        panelAtributosSecundarios = new javax.swing.JPanel();
        panelBotones = new javax.swing.JPanel();
        botonOk = new javax.swing.JButton();
        botonCancelar = new javax.swing.JButton();

        panelAtributosPrimarios.setLayout(new java.awt.GridBagLayout());

        labelTipo.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labelTipo.setText("Tipo medio");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        panelAtributosPrimarios.add(labelTipo, gridBagConstraints);

        comboTipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboTipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboTipoActionPerformed(evt);
            }
        });
        comboTipo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                comboTipoKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        panelAtributosPrimarios.add(comboTipo, gridBagConstraints);

        labelAutor.setText("Autor");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 15, 0, 0);
        panelAtributosPrimarios.add(labelAutor, gridBagConstraints);

        txtAutor.setColumns(15);
        txtAutor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtAutorKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 120;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        panelAtributosPrimarios.add(txtAutor, gridBagConstraints);

        labelTitulo.setText("Título");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 15, 0, 0);
        panelAtributosPrimarios.add(labelTitulo, gridBagConstraints);

        txtTitulo.setColumns(15);
        txtTitulo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTituloKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 120;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        panelAtributosPrimarios.add(txtTitulo, gridBagConstraints);

        labelGenero.setText("Género");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 29, 0, 0);
        panelAtributosPrimarios.add(labelGenero, gridBagConstraints);

        txtGenero.setColumns(15);
        txtGenero.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtGeneroKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 120;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        panelAtributosPrimarios.add(txtGenero, gridBagConstraints);

        labelFecha.setText("Fecha publicación");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 5, 0, 0);
        panelAtributosPrimarios.add(labelFecha, gridBagConstraints);

        txtFecha.setColumns(15);
        txtFecha.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtFechaKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 120;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        panelAtributosPrimarios.add(txtFecha, gridBagConstraints);

        panelAtributosSecundarios.setLayout(new java.awt.GridLayout(3, 4, 15, 15));

        botonOk.setText("Salvar");
        botonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonOkActionPerformed(evt);
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
                .addComponent(botonOk)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(botonCancelar)
                .addContainerGap())
        );
        panelBotonesLayout.setVerticalGroup(
            panelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBotonesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(botonOk)
                    .addComponent(botonCancelar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelAtributosPrimarios, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 791, Short.MAX_VALUE)
            .addComponent(panelBotones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panelAtributosSecundarios, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelAtributosPrimarios, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelAtributosSecundarios, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(panelBotones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }

    private void comboTipoActionPerformed(java.awt.event.ActionEvent evt) {
        this.generarNuevosAtributos();
    }

    private void botonCancelarActionPerformed(java.awt.event.ActionEvent evt) {
        this.cerrar();
    }

    private void botonOkActionPerformed(java.awt.event.ActionEvent evt) {
        this.ejecutar();
    }

    private void txtAutorKeyPressed(java.awt.event.KeyEvent evt) {
        gestionarKeyEvent(evt);
    }

    private void txtGeneroKeyPressed(java.awt.event.KeyEvent evt) {
        gestionarKeyEvent(evt);
    }

    private void txtTituloKeyPressed(java.awt.event.KeyEvent evt) {
        gestionarKeyEvent(evt);
    }

    private void txtFechaKeyPressed(java.awt.event.KeyEvent evt) {
        gestionarKeyEvent(evt);
    }

    private void comboTipoKeyPressed(java.awt.event.KeyEvent evt) {
        gestionarKeyEvent(evt);
    }

    protected javax.swing.JButton botonCancelar;
    protected javax.swing.JButton botonOk;
    protected javax.swing.JComboBox<String> comboTipo;
    protected javax.swing.JLabel labelAutor;
    protected javax.swing.JLabel labelFecha;
    protected javax.swing.JLabel labelGenero;
    protected javax.swing.JLabel labelTipo;
    protected javax.swing.JLabel labelTitulo;
    protected javax.swing.JPanel panelAtributosPrimarios;
    protected javax.swing.JPanel panelAtributosSecundarios;
    protected javax.swing.JPanel panelBotones;
    protected javax.swing.JTextField txtAutor;
    protected javax.swing.JTextField txtFecha;
    protected javax.swing.JTextField txtGenero;
    protected javax.swing.JTextField txtTitulo;
}

