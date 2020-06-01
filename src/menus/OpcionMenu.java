package menus;

/**
 * Clase que representa, de forma abstracta, la opción de un menú.
 * 
 * Una opción es cualquier hijo de un menú superior, por poner un ejemplo,
 * en el siguiente esquema las opciones "abrir", "guardar" y "salir" son
 * opciones de menú.
 * 
 *  - Archivo
 *      > abrir
 *      > guardar
 *      > salir
 * 
 * Una opción tiene un nombre que se mostrará al usuario, así como una acción
 * asociada que será lo que se dispare una vez se ejecute el elemento del menú.
 * 
 * @author Héctor Luaces Novo
 */
public class OpcionMenu implements ElementoMenuIF {
    /**
     * Acción asociada a este elemento del menú.
     */
    private final EnumAccionesMenu accion;
    
    /**
     * Crea una nueva opción de menú asociándole una acción.
     * 
     * Visibilidad package-protected para implementar el patrón factoría
     * estática.
     * 
     * @see MenuBibliotecaFactory#getMenuUusario(usuarios.Usuario) 
     * @param accion Acción a asociar a este elemento del menú.
     */
    OpcionMenu(EnumAccionesMenu accion) {
        this.accion = accion;
    }
    
    /**
     * @return El nombre de este menú.
     */
    @Override
    public String getNombre() {
        return accion.getNombreAccion();
    }
    
    /**
     * La acción asociada a este elemento del menú.
     * @return 
     */
    @Override
    public EnumAccionesMenu getAccion() {
        return accion;
    }

}
