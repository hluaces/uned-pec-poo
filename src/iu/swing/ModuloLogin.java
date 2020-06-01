package iu.swing;

import aplicacion.Aplicacion;
import com.sun.glass.events.KeyEvent;

/**
 * Módulo que se encarga de mostrar los controles para permitir a un usuario
 * hacer login en una biblioteca.
 * 
 * @author Héctor Luaces Novo
 */
class ModuloLogin extends javax.swing.JFrame implements ModuloIUSwingIF {
    /**
     * El {@link ControladorSwing} asociado a éste módulo.
     */
    private ControladorSwing controlador;
    
    /**
     * Constructor privado. 
     * Usar {@link Login#Login(iu.swing.ControladorSwing)} en su lugar.
     */
    private ModuloLogin() {
        initComponents();
    }

    /**
     * Crea el nuevo módulo de login.
     * 
     * @param c El ControladorSwing a asignar a ésta biblioteca
     */
    public ModuloLogin(ControladorSwing c) {
        this();
        
        this.setControlador(c);
        this.actualizar();
    }

    @Override
    public final void setControlador(ControladorSwing c) {     
        this.controlador = c;
    }
    
    @Override
    public final void actualizar() {
        this.limpiarCampos();  
        this.labelTitulo.setText("Acceso a " + 
            this.controlador.getAplicacion().getBibliotecaActiva().getNombre()
        );
        this.setTitle(this.labelTitulo.getText());
    }
    
    /**
     * Limpia los campos del JFRame.
     */
    private void limpiarCampos() {
        this.txtUser.setText("");
        this.password.setText("");
        this.labelError.setText("");
    }
    
    /**
     * Llamado al pulsar el botón 'cancelar': cierra éste frame y vuelve al
     * {@link ModuloSelectorBiblioteca}
     */
    private void cancelar() {
        this.controlador.mostrarSelector();
        this.controlador.cerrarFrame(this);
    }
    
    /**
     * Llamado al pulsar el botón 'aceptar': intentar logearse en la aplicación
     * y biblioteca activa con los datos facilitados.
     * 
     * Si lo consigue, se establecerá el {@link Aplicacion#usuarioActivo} de 
     * la aplicación.
     * 
     * De no ser así, se mostrará un error.
     */
    private void aceptar() {
        boolean ok;
        
        ok = this.controlador
            .getAplicacion()
            .login(txtUser.getText(), new String(password.getPassword()))
        ;
        
        if ( ! ok ) {
            this.labelError.setText("Usuario o contraseña inválida.");
            return;
        }
        
        this.controlador.borrarCache();
        this.controlador.mostrarEscritorio();
        this.controlador.cerrarFrame(this);
    }
    
    @SuppressWarnings("unchecked")
    private void initComponents() {

        panelControles = new javax.swing.JPanel();
        labelUser = new javax.swing.JLabel();
        txtUser = new javax.swing.JTextField();
        labelPassword = new javax.swing.JLabel();
        password = new javax.swing.JPasswordField();
        botonOk = new javax.swing.JButton();
        botonCancelar = new javax.swing.JButton();
        labelTitulo = new javax.swing.JLabel();
        labelError = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        panelControles.setLayout(new java.awt.GridLayout(3, 2, 10, 10));

        labelUser.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labelUser.setLabelFor(labelUser);
        labelUser.setText("Usuario");
        panelControles.add(labelUser);

        txtUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUserActionPerformed(evt);
            }
        });
        txtUser.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtUserKeyPressed(evt);
            }
        });
        panelControles.add(txtUser);

        labelPassword.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labelPassword.setLabelFor(labelPassword);
        labelPassword.setText("Contraseña");
        panelControles.add(labelPassword);

        password.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passwordActionPerformed(evt);
            }
        });
        password.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                passwordKeyPressed(evt);
            }
        });
        panelControles.add(password);

        botonOk.setText("Acceder");
        botonOk.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botonOkMouseClicked(evt);
            }
        });
        botonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonOkActionPerformed(evt);
            }
        });
        panelControles.add(botonOk);

        botonCancelar.setText("Volver a bibliotecas");
        botonCancelar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botonCancelarMouseClicked(evt);
            }
        });
        panelControles.add(botonCancelar);

        labelTitulo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labelTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        labelError.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labelError.setForeground(new java.awt.Color(255, 0, 0));
        labelError.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(labelTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(panelControles, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(labelError, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(labelError, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelControles, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }

    private void txtUserActionPerformed(java.awt.event.ActionEvent evt) {
        this.aceptar();
    }

    private void botonCancelarMouseClicked(java.awt.event.MouseEvent evt) {
        this.cancelar();
    }

    private void botonOkMouseClicked(java.awt.event.MouseEvent evt) {
        this.aceptar();
    }

    private void botonOkActionPerformed(java.awt.event.ActionEvent evt) {
        this.aceptar();
    }

    private void formWindowClosing(java.awt.event.WindowEvent evt) {
        this.cancelar();
    }

    private void passwordActionPerformed(java.awt.event.ActionEvent evt) {
        this.aceptar();
    }

    private void txtUserKeyPressed(java.awt.event.KeyEvent evt) {
        if ( evt.getKeyCode() == KeyEvent.VK_ESCAPE )
            this.cancelar();
    }

    private void passwordKeyPressed(java.awt.event.KeyEvent evt) {
        if ( evt.getKeyCode() == KeyEvent.VK_ESCAPE )
            this.cancelar();
    }

    private javax.swing.JButton botonCancelar;
    private javax.swing.JButton botonOk;
    private javax.swing.JLabel labelError;
    private javax.swing.JLabel labelPassword;
    private javax.swing.JLabel labelTitulo;
    private javax.swing.JLabel labelUser;
    private javax.swing.JPanel panelControles;
    private javax.swing.JPasswordField password;
    private javax.swing.JTextField txtUser;
}
