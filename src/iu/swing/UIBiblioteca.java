package iu.swing;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;

/**
 * La clase principal que dibujará el 'dashboard' de la aplicación.
 * 
 * En ella se abrirán widgets para ejecutar las diferentes acciones del menú y,
 * en definitiva, interactuar con la aplicación.
 * 
 * Cada dashboard solo permitirá interactuar con una biblioteca.
 *
 * @author Héctor Luaces Novo
 */
class UIBiblioteca extends javax.swing.JFrame implements ModuloIUSwingIF {
    /**
     * El ControladorSwing que estará asociado al escritorio
     */
    private ControladorSwing controlador;
    
    /**
     * Constructor privado que inicializa los componentes gráficos.
     * 
     * @see UIBiblioteca#UIBiblioteca(iu.swing.ControladorSwing) 
     */
    private UIBiblioteca() {
        initComponents();
    }

    /**
     * Constructor que crea un nuevo Escritorio, asociándole un ControladorSwing
     * 
     * @param c ControladorSwing a inyectar en el escritorio.
     */
    public UIBiblioteca(ControladorSwing c) {
        this();

        //this.desktopMain.removeAll();
        this.setControlador(c);
        this.actualizar();
    }

    @Override
    public final void setControlador(ControladorSwing c) {
        this.controlador = c;
    }

    @Override
    public final void actualizar() {
        this.setTitle(
            controlador
                .getAplicacion()
                .getBibliotecaActiva()
                .getNombre() + ": Escritorio"
        );
        
        for(JInternalFrame j: this.desktopMain.getAllFrames()) {
            
            if ( ! (j instanceof ModuloIUSwingIF) )
                continue;
            
            ( (ModuloIUSwingIF) j ).actualizar();
        }
    }
    
    /**
     * Añade un JInternalFrame al escritorio de la biblioteca.
     * 
     * Si lo que queremos añadir ya existe, en su lugar haremos que se
     * centre el foco en la ventana.
     * 
     * @param f JInternalFrame que queremos añadir.
     */
    public void addPanel(JInternalFrame f) {
        int frames;

        if ( this.getPanel(f) ) {
            f.toFront();
            f.requestFocus();
            return;
        }
        
        frames = this.desktopMain.getAllFrames().length;
        this.desktopMain.add(f);
        f.setVisible(true);
        
        if ( ! f.isMaximum() ) {
            f.setLocation(10 + f.getX() + frames * 25, 10 + f.getY() + frames * 25);
        }
        
        f.setClosable(true);
        f.setMaximizable(true);
        f.setIconifiable(true);
        f.setResizable(true);
        f.toFront();
        f.requestFocus();
    }
    
    /**
     * Busca si existe un panel 
     * 
     * @param a_buscar Objeto del panel a buscar
     * 
     * @return True si el panel ya existe en el escritorio
     */
    private boolean getPanel(JInternalFrame a_buscar) {
        for (JInternalFrame j: this.desktopMain.getAllFrames()) {
            if ( j.equals(a_buscar) )
                return true;
        }
        
        return false;
    }
    
    @SuppressWarnings("unchecked")
    private void initComponents() {

        desktopMain = new javax.swing.JDesktopPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        javax.swing.GroupLayout desktopMainLayout = new javax.swing.GroupLayout(desktopMain);
        desktopMain.setLayout(desktopMainLayout);
        desktopMainLayout.setHorizontalGroup(
            desktopMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 777, Short.MAX_VALUE)
        );
        desktopMainLayout.setVerticalGroup(
            desktopMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 550, Short.MAX_VALUE)
        );

        getContentPane().add(desktopMain, java.awt.BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }

    private void cerrar()
    {
        this.controlador.mostrarLogin();
        this.controlador.getAplicacion().logout();
        this.controlador.borrarCache();
    }
    
    private void formWindowClosing(java.awt.event.WindowEvent evt) {
        this.cerrar();
    }

    private javax.swing.JDesktopPane desktopMain;
}
