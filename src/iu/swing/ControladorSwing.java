package iu.swing;

import aplicacion.Aplicacion;
import javax.swing.JFrame;
import java.awt.Component;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import menus.EnumAccionesMenu;
import menus.MenuBibliotecaFactory;
import iu.ControladorGraficoIF;
import biblioteca.Biblioteca;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import menus.MenuBiblioteca;
import menus.ElementoMenuIF;
import usuarios.Usuario;


/**
 * Controlador que se encarga de permitir que la aplicación de biblioteca pueda
 * usarse usando la librería gráfica 'Swing' de Java.
 * 
 * El controlador tendrá una caché con todas las ventanas comunes que usa
 * para que no sea necesario instanciarlas una y otra vez.
 * 
 * Al cerrar una ventana, ésta se quedará en modo "dispose" y se guardará en 
 * caché.
 * 
 * Si fuese necesario crear una nueva instancia de dicha ventana, se reutilizará
 * desde la caché.
 * @author Héctor Luaces Novo
 */
final public class ControladorSwing implements ControladorGraficoIF {
    /**
     * Referencia al controlador de 'ModuloLogin' que será usado por el controlador.
     */
    private ModuloLogin login;

    /**
     * Referencia a la interfaz general usada por la herramienta (el escritorio
     * o 'dashboard' de la biblioteca).
     */
    private UIBiblioteca escritorio;
    
    /**
     * Referencia a la ventana de selección de biblioteca que será usada por
     * el controlador.
     */
    private ModuloSelectorBiblioteca selector;
    
    /**
     * Referencia a la aplicación que queremos utilizar en la aplicación.
     */
    private Aplicacion aplicacion;
    
    /**
     * Un map que referenciará una {@link EnumAccionesMenu acción del menú} con
     * un {@link ModiloIUBiblioteca Módulo de la biblioteca} y que permitirá
     * ahorrar recursos a la hora de reutilizar dichos componentes gráficos
     */
    private Map<EnumAccionesMenu, ModuloIUSwingIF> cache;
    
    /**
     * Crea un nuevo controlador SWING para la aplicación dada.
     * 
     * @param a Instancia de la aplicación para la que queremos mostrar
     * el entorno Swing.
     */
    public ControladorSwing(Aplicacion a) {
        this.cache      = new HashMap<>();
        this.aplicacion = a;
    }
    
    /**
     * Vacía la caché del controladorSwing. Cuando se intente reutilizar un 
     * nuevo componente se creará una nueva instancia en lugar de ser 
     * reutilizados.
     */
    public void borrarCache() {
        this.escritorio = null;
        this.cache      = new HashMap<>();
    }
    
    /**
     * Muestra la ventana de ModuloLogin de usuario, si existe.
     */
    public void mostrarLogin() {
        if ( this.login == null )
            this.login = new ModuloLogin(this);
        
        this.login.setVisible(true);
        this.login.actualizar();
    }

    /**
     * Devuelve el componente SWING con la ventana activa de la aplicación.
     * 
     * Puede ser null si ocurriese un error en algún punto de la aplicación
     * que causara que no existiese ninguna ventana.
     * 
     * @return El componente SWING activo en la aplicación
     */
    public Component dameVentanaActiva() {
        if ( this.escritorio != null ) 
            return this.escritorio;
        
        if ( this.login != null )
            return this.login;
        
        if ( this.selector != null )
            return this.selector;
        
        return null;
    }
    
    /**
     * Muestra el selector de bibliotecas para la aplicación.
     */
    public void mostrarSelector() {
        if ( this.selector == null )
            this.selector = new ModuloSelectorBiblioteca(this);
        
        this.selector.actualizar();
        this.selector.setVisible(true);
    }
    
    /**
     * Dado una lista de {@link MenuBiblioteca}, éste método se encarga de
     * cargarlos en el {@link UIBiblioteca escritorio} como menús que
     * implementan sus acciones.
     * 
     * @param menus Lista de menús a añadir al escritorio.
     */
    private void cargarMenus(List<MenuBiblioteca> menus) {
        JMenuBar menusFrame = new JMenuBar();
        escritorio.setMenuBar(null);

        for(MenuBiblioteca menu: menus) {
            JMenu m = new JMenu(menu.getNombre());
            
            for(ElementoMenuIF o: menu.getOpciones()) {
                JMenuItem j;
                
                if ( this.dameClaveCache(o.getAccion()) != o.getAccion() )
                    continue;
                
                j = new JMenuItem(o.getNombre());
                m.add(j);
                j.addActionListener((ActionEvent e) -> {
                    escritorio.addPanel(this.dameFrameAccion(
                            e.getActionCommand()
                    ));
                });
            }
            
            menusFrame.add(m);
        }
        
        
        escritorio.setJMenuBar(menusFrame);
    }

    /**
     * Muestra el escritorio o 'dashboard' de la aplicación para el usuario
     * que está activo.
     */
    public void mostrarEscritorio() {
        int sin_leer;
        
        if ( this.escritorio == null ) 
            this.escritorio = new UIBiblioteca(this);
        
        // Se actualiza el escritorio y se recargan los menús acorde a los
        // permisos de lusuario
        this.escritorio.actualizar();
        this.cargarMenus(MenuBibliotecaFactory.getMenuUusario(
            aplicacion.getUsuarioActivo()
        ));
        this.escritorio.setVisible(true);
        
        sin_leer = this.getBibliotecaActiva()
            .getTotalMensajesSinLeer(
                this.getUsuarioActivo()
            );
        
        if ( sin_leer == 0 )
            return;
        
        this.mostrarAviso("Tienes " + sin_leer + " mensajes sin leer.");
    }
    
    @Override
    public void iniciarIU(Aplicacion a) {
        this.aplicacion = a;

        this.mostrarSelector();
    }

    /**
     * Devuelve el objeto aplicación que está usando este controlador gráfico.
     * 
     * @return El objeto aplicación usado por éste controlador gráfico.
     */
    public Aplicacion getAplicacion() {
        return this.aplicacion;
    }
    
    /**
     * Dado un jFrame activo se encarga de cerrarlo.
     * 
     * @param f el JFrame a cerrar.
     */
    public void cerrarFrame(JFrame f)  {
        f.dispose();
    }
    
    /**
     * Sobrecarga de {@link ControladorSwing#cerrarFrame(javax.swing.JFrame)} 
     * que permite esconder JInternalFrames del {@link UIBiblioteca escritorio}
     * 
     * @param f Widget que queremos esconder en el escritorio.
     */
    public void cerrarFrame(JInternalFrame f) {
        f.dispose();
    }
    
    /**
     * La interfaz gráfica permite "agrupar" algunas acciones en una única 
     * ventana.
     * 
     * Para que no haya varias entradas innecesarias en la cache, agrupamos
     * las acciones agrupables bajo una única clave.
     * 
     * Esta lfun devuelve la clave de una Acción, o la propia acción si ésta
     * no está agrupada.
     * 
     * @param b La acción para que queremos conocer la clave de caché
     * @return La clave de caché de la acción
     */
    private EnumAccionesMenu dameClaveCache(EnumAccionesMenu b) {
        switch(b) {
            // La vista y devolución de préstamos se controla desde un único
            // widget
            case PRESTAMOS_VER:
            case PRESTAMOS_DEVOLVER:
                return EnumAccionesMenu.PRESTAMOS_VER;

            /// Todo ésto se hace a través del widget de medios
            case MEDIOS_VER:
            case GESTION_MEDIOS:
            case PRESTAMOS_SOLICITAR:
                return EnumAccionesMenu.MEDIOS_VER;

            case GESTION_USUARIOS:
            case GESTION_USUARIOS_TARJETAS:
                return EnumAccionesMenu.GESTION_USUARIOS;
                
            case RESERVAS_VER:
            case RESERVAS_ANULAR:
                return EnumAccionesMenu.RESERVAS_VER;
                
            default:
                return b;
        }
    }
    
    /**
     * Dado el nombre de un {@Link EnumAccionMenu acción de menú}, ésta función
     * devuelve el JInternalFrame que será usado en el 'dashboard' de la 
     * biblioteca para representar dicha funcionalidad.
     * 
     * Ésta función usa la caché del controlador.
     * 
     * @param id_menu El nombre de la EnumAccionMenu para la que queremos el 
     * frame de acción.
     * @return El frame de acción, si existe, para dicha acción de menú.
     */
    public JInternalFrame dameFrameAccion(String id_menu) {
        EnumAccionesMenu a = null;
        ModuloIUSwingIF j;
        
        for (EnumAccionesMenu e: EnumAccionesMenu.values()) {
            if ( e.getNombreAccion().equals(id_menu) ) {
                a = e;
                break;
            }
        }

        a = this.dameClaveCache(a);
        
        if ( this.cache.keySet().contains(a) ) {
            j = this.cache.get(a);
            
            if ( j != null ) {
                ((Component) j).setVisible(true);
                j.actualizar();
                return (JInternalFrame) j;
            }
        }

        switch(a) {
            case SUSCRIPCIONES:
                j = new WidgetSuscripciones(this);
                break;
            
            // Todo esto a través del widget de préstamos
            case PRESTAMOS_VER:
            case PRESTAMOS_DEVOLVER:
                //j = new WidgetPrestamosActivos(this);
                j = new WidgetPrestamosActivos(this);
                break;

            case MULTAS_VER:
                j = new WidgetMultas(this);
                break;
                
            case PRESTAMOS_HISTORIAL:
                j = new WidgetPrestamosHistorial(this);
                break;
                
            case GESTION_PRESTAMOS_VER_FUERA_PLAZO:
                //j = new WidgetListadoPrestamosCaducados(this);
                j = new WidgetPrestamosListadoGlobalVencidos(this);
                break;
                
            case GESTION_PRESTAMOS_VER:
                j = new WidgetPrestamosListadoGlobal(this);
                break;
                
            // Gestión de mensajes a usuarios
            case MENSAJES:
                j = new WidgetMensajes(this);
                break;
                
            // Todo ésto se hace a través del widget de medios
            case MEDIOS_VER:
            case GESTION_MEDIOS:
            case PRESTAMOS_SOLICITAR:
                j = new WidgetMedios(this);
                break;

            case MEDIOS_BUSCAR:
                j = new WidgetBuscarMedio(this);
                break;
            
            case MEDIOSC_BUSCAR_CRUZADOS:
                j = new WidgetMediosCruzadosBusqueda(this);
                break;
                
            case GESTION_USUARIOS:
            case GESTION_USUARIOS_TARJETAS:
                j = new WidgetGestionUsuarios(this);
                break;
            
            case GESTION_USUARIOS_ALTA:
                j = new WidgetNuevoUsuario(this);
                break;
                
            case GESTION_MEDIOS_ALTA:
                j = new WidgetNuevoMedio(this);
                break;

            case GESTION_RESERVAS_VER:
                j = new WidgetListadoReservas(this);
                break;
                
            case RESERVAS_VER:
                j = new WidgetReservas(this, this.getUsuarioActivo());
                break;
                
            default:
                throw new NoSuchElementException("Acción aún no implementada");
        }
        
        j.actualizar();
        this.cache.put(a, j);
        return (JInternalFrame) j;
    }

    @Override
    public void procesarError(Throwable e) {
        String tx = "";
        int i = 0;
        
        
        if ( e.getMessage() != null )
            tx = tx + e.getMessage() + "\n";
        
        /*if ( e.getLocalizedMessage() != null )
            tx = tx + e.getLocalizedMessage();*/
        
        
        if ( tx.isEmpty() )
            tx = "Ha ocurrido un error: " + e.toString();
        
        /*else 
            tx = "Ha ocurrido un error: " + tx + " (" + e.toString() + ").";
        
        
        for(StackTraceElement x: e.getStackTrace()) {
            tx += x.getClassName() + " (" + x.getFileName() + ":" 
                + x.getLineNumber() + ") " + x.getMethodName() + "\n"
            ;
            
            if ( ++i > 5 )
                break;

        }
        System.out.println(tx);
        
        */
        e.printStackTrace();  // FIXME
        this.mostrarError(tx);
    }
    
    /**
     * Añade un nuevo JInternalFrame al panel principal de UI en la biblioteca.
     * 
     * @see UIBiblioteca#addPanel(javax.swing.JInternalFrame) 
     * @param f Nuevo JInternalFrame a añadir
     * @return True si la operación tiene éxito, false de cualquier otra forma
     */
    public boolean addPanel(JInternalFrame f) {
        if ( this.escritorio == null )
            return false;
        
        this.escritorio.addPanel(f);
        return true;
    }
    
    /**
     * Muestra un aviso por pantalla.
     * 
     * @param tx El texto del aviso que queremos mostrar.
     */
    public void mostrarAviso(String tx) {
        JOptionPane.showMessageDialog(
            this.dameVentanaActiva(), 
            tx,
            "Aviso",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    /**
     * Muestra un error por pantalla.
     * 
     * @param tx El texto del error que queremos mostrar.
     */
    public void mostrarError(String tx) {
        JOptionPane.showMessageDialog(
            this.dameVentanaActiva(), 
            tx,
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
    }
    
    /**
     * Actualiza todos los elementos del escritorio
     */
    public void actualizaEscritorio() {
        if ( this.escritorio == null )
            return;
        
        this.escritorio.actualizar();
    }
    
    /**
     * Devuelve el escritorio de biblioteca usado por éste controladorSwing
     * 
     * @return El escritorio -o dashboard- de la biblioteca
     */
    public UIBiblioteca getUIBiblioteca() {
        return this.escritorio;
    }

    /**
     * Devuelve el usuario activo de la {@link Aplicacion}, que puede ser
     * null si aún no se ha identificado nadie.
     * 
     * @return El usuario activo de la aplicación o 'null'.
     */
    public Usuario getUsuarioActivo() {
        return this.aplicacion.getUsuarioActivo();
    }

    /**
     * Devuelve la biblioteca activa (seleccionada) en la {@link Aplicacion).
     * 
     * Puede ser null si todavía no se ha iniciado una biblioteca desde el 
     * selector.
     * 
     * @return La biblioteca activa o 'null' si no hay ninguna.
     */
    public Biblioteca getBibliotecaActiva() {
        return this.aplicacion.getBibliotecaActiva();
    }
}
