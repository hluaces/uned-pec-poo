package iu.swing;

/**
 * Los módulos de interfaz de usuario son las ventanas adicionales usadas por
 * el {@link ControladorSwing} para representar las diferentes ventanas 
 * principales de la aplicación.
 * 
 * @author Héctor Luaces Novo
 */
interface ModuloIUSwingIF {
    /**
     * Establece el {@link ControladorSwing} para éste módulo.
     * @param controlador El controlador a fijar en el módulo
     */
    public void setControlador(ControladorSwing controlador);
    
    /**
     * Actualiza el estado de los datos mostrados por el módulo
     */
    public void actualizar();
}
