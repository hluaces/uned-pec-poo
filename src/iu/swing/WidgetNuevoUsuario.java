package iu.swing;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JTextField;
import misc.StringUtil;
import usuarios.EnumPerfiles;
import usuarios.Usuario;
import usuarios.UsuariosFactory;

/**
 * Widget que permite añadir un nuevo usuario a una biblioteca.
 * 
 * @author Héctor Luaces Novo
 */
public class WidgetNuevoUsuario extends javax.swing.JInternalFrame implements ModuloIUSwingIF {
    /**
     * ControladorSwing asignado a éste Widget
     */
    private ControladorSwing controlador;
    
    /**
     * Clase interna que usada en los métodos de validación de campos
     */
    private static class ErrorValidacionCampo extends Exception {
        public ErrorValidacionCampo(String message) {
            super(message);
        }
    }
        
    /**
     * Constructor privado
     * 
     * @see WidgetNuevoUsuario#WidgetNuevoUsuario(iu.swing.ControladorSwing) 
     */
    private WidgetNuevoUsuario() {
        initComponents();
    }

    /**
     * Constructor principal de la clase, que crea un nuevo Widget inyectándole
     * un controladorSwing
     * 
     * @param c El controladorSwing que asignaremos a la clase
     */
    public WidgetNuevoUsuario(ControladorSwing c) {
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
        this.actualizarCombo();
        this.resetearCampos();
        
        // Añadimos un evento para que el formulario sea más responsivo
        for(JTextField c: this.getCamposDeTexto()) {
            c.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {
                }

                @Override
                public void keyPressed(KeyEvent e) {
                    if ( e.getKeyCode() == KeyEvent.VK_ENTER )
                        salvar();
                }

                @Override
                public void keyReleased(KeyEvent e) {
                }
            }) ;
        }
    }
    
    /**
     * Devuelve una lista de todos los jTextField que están en el panel
     * de campos de introducción.
     * 
     * @return La lista de todos los campos de texto en el panel de campos
     * de introducción. Si no hay ninguno, se devolverá una lista vacía.
     */
    private List<JTextField> getCamposDeTexto() {
        List<JTextField> lista = new ArrayList<>();
        
        for(Component c: this.panelDatos.getComponents()) {
            if ( ! (c instanceof JTextField) )
                continue;
            
            lista.add((JTextField) c);
        }
        
        return lista;
    }
    
    /**
     * Borra el texto de todos los jTextField del Widget.
     */
    private void resetearCampos() {
        this.getCamposDeTexto().forEach((c) -> c.setText(""));
    }
    
    /**
     * Verifica que todos los campos introducidos por el usuario sean correctos.
     * 
     * Necesitarán estar todos cumplimentados y superar ciertas comprobaciónes
     * en función del tipo de campo.
     * 
     * @throws iu.swing.WidgetNuevoUsuario.ErrorValidacionCampo Si ocurre algún
     * error de validación.
     */
    private void comprobarCampos() throws ErrorValidacionCampo {
        String aux, aux2;
        Usuario u;
        
        for(JTextField f: this.getCamposDeTexto()) {
            if ( f.getText().isEmpty() ) {
                throw new ErrorValidacionCampo(
                    "Hay campos sin rellenar, por favor, rellénalos todos."
                );
            }
        }
        
        aux  = Arrays.toString(this.password.getPassword());
        aux2 = Arrays.toString(this.passwordConfirmado.getPassword());
        
        if ( ! aux.equals(aux2) )
            throw new ErrorValidacionCampo("Las contraseñas no coinciden.");
        
        if ( ! StringUtil.isDniValido(this.txtDNI.getText()) )
            throw new ErrorValidacionCampo(
                "El DNI no es válido (ha de incluír letra)"
            );
        
        aux = this.txtLogin.getText();
        
        if ( null != this.controlador.getBibliotecaActiva().getUsuario(aux) )
            throw new ErrorValidacionCampo(
                "Ya existe un usuario en la biblioteca con ese Login."
            );
    }
    
    /**
     * Intenta crear un nuevo usuario con los campos de la biblioteca.
     * 
     * Gestiona los errores y los muestra por pantalla, si sucede.
     * 
     * En caso de éxito, añade el usuario y cierra el frame.
     */
    private void salvar() {
        Usuario u;
        
        try {
            this.comprobarCampos();
        }
        catch (ErrorValidacionCampo e) {
            this.controlador.mostrarAviso(e.getMessage());
            return;
        }
        
        u = UsuariosFactory.crearUsuario(
            this.txtLogin.getText(),
            String.valueOf(this.password.getPassword()),
            EnumPerfiles.getPerfilPorNombre(
                this.comboPerfil.getSelectedItem().toString()
            )
        );
        
        u.setNombre(this.txtNombre.getText());
        u.setApellidos(this.txtApellidos.getText());
        u.setDni(this.txtDNI.getText());
         
        this.controlador.getBibliotecaActiva().addUsuario(u);
        this.controlador.mostrarAviso("Usuario guardado con éxito.");
        this.controlador.cerrarFrame(this);
        this.controlador.actualizaEscritorio();        
    }
    
    /**
     * Inicializa el Combo de selección de perfil de usuario con los nombres
     * de los tipos de perfil.
     */
    private void actualizarCombo() {
        this.comboPerfil.removeAllItems();
        
        for(EnumPerfiles p: EnumPerfiles.values())
            this.comboPerfil.addItem(p.getNombrePerfil());
        
        this.comboPerfil.setSelectedItem(EnumPerfiles.USUARIO.getNombrePerfil());
    }
    

    @SuppressWarnings("unchecked")
    private void initComponents() {

        panelDatos = new javax.swing.JPanel();
        labelLogin = new javax.swing.JLabel();
        txtLogin = new javax.swing.JTextField();
        labelNombre = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        labelApellidos = new javax.swing.JLabel();
        txtApellidos = new javax.swing.JTextField();
        txtDNI = new javax.swing.JTextField();
        labelDNI = new javax.swing.JLabel();
        labelPerfil = new javax.swing.JLabel();
        comboPerfil = new javax.swing.JComboBox<>();
        labelPassword = new javax.swing.JLabel();
        password = new javax.swing.JPasswordField();
        jLabel2 = new javax.swing.JLabel();
        passwordConfirmado = new javax.swing.JPasswordField();
        panelBotones = new javax.swing.JPanel();
        botonCerrar = new javax.swing.JButton();
        botonGuardar = new javax.swing.JButton();

        setTitle("Alta nuevo usuario");

        labelLogin.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labelLogin.setLabelFor(txtLogin);
        labelLogin.setText("Nombre de usuario");

        labelNombre.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labelNombre.setLabelFor(txtNombre);
        labelNombre.setText("Nombre");

        labelApellidos.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labelApellidos.setLabelFor(txtApellidos);
        labelApellidos.setText("Apellidos");

        labelDNI.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labelDNI.setLabelFor(labelDNI);
        labelDNI.setText("DNI");

        labelPerfil.setText("Perfil de usuario");

        comboPerfil.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        labelPassword.setText("Contraseña");

        jLabel2.setText("Repetir contraseña");

        javax.swing.GroupLayout panelDatosLayout = new javax.swing.GroupLayout(panelDatos);
        panelDatos.setLayout(panelDatosLayout);
        panelDatosLayout.setHorizontalGroup(
            panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDatosLayout.createSequentialGroup()
                .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelDatosLayout.createSequentialGroup()
                        .addComponent(labelNombre)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelDatosLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(labelLogin)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelDatosLayout.createSequentialGroup()
                        .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(labelPassword)
                            .addComponent(labelDNI))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(password, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                            .addComponent(txtDNI))))
                .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelDatosLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(labelPerfil)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(comboPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelDatosLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(passwordConfirmado, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelDatosLayout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addComponent(labelApellidos)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtApellidos)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelDatosLayout.setVerticalGroup(
            panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDatosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelLogin)
                    .addComponent(txtLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelPerfil)
                    .addComponent(comboPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelNombre)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(labelApellidos)
                        .addComponent(txtApellidos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(passwordConfirmado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(labelPassword)
                        .addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelDNI)
                    .addComponent(txtDNI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        botonCerrar.setText("Cancelar");
        botonCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCerrarActionPerformed(evt);
            }
        });

        botonGuardar.setText("Salvar");
        botonGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonGuardarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelBotonesLayout = new javax.swing.GroupLayout(panelBotones);
        panelBotones.setLayout(panelBotonesLayout);
        panelBotonesLayout.setHorizontalGroup(
            panelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBotonesLayout.createSequentialGroup()
                .addContainerGap(445, Short.MAX_VALUE)
                .addComponent(botonGuardar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(botonCerrar)
                .addContainerGap())
        );
        panelBotonesLayout.setVerticalGroup(
            panelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBotonesLayout.createSequentialGroup()
                .addContainerGap(15, Short.MAX_VALUE)
                .addGroup(panelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(botonCerrar)
                    .addComponent(botonGuardar))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelDatos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 10, Short.MAX_VALUE)
                .addComponent(panelBotones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelDatos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelBotones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }

    private void botonCerrarActionPerformed(java.awt.event.ActionEvent evt) {
        this.controlador.cerrarFrame(this);
    }

    private void botonGuardarActionPerformed(java.awt.event.ActionEvent evt) {
        this.salvar();
    }

    private javax.swing.JButton botonCerrar;
    private javax.swing.JButton botonGuardar;
    private javax.swing.JComboBox<String> comboPerfil;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel labelApellidos;
    private javax.swing.JLabel labelDNI;
    private javax.swing.JLabel labelLogin;
    private javax.swing.JLabel labelNombre;
    private javax.swing.JLabel labelPassword;
    private javax.swing.JLabel labelPerfil;
    private javax.swing.JPanel panelBotones;
    private javax.swing.JPanel panelDatos;
    private javax.swing.JPasswordField password;
    private javax.swing.JPasswordField passwordConfirmado;
    private javax.swing.JTextField txtApellidos;
    private javax.swing.JTextField txtDNI;
    private javax.swing.JTextField txtLogin;
    private javax.swing.JTextField txtNombre;

}
