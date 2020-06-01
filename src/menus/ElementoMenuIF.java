package menus;

/**
 * Interfaz que representa, de forma abstracta, un elemento de un menú.
 * 
 * Los elementos de un menú tienen un nombre y una {@link EnumAccionesMenu
 * acción asociada).
 * 
 * @author Héctor Luaces Novo
 */
public interface ElementoMenuIF {
    /**
     * Devuelve el nombre del menú. El nombre es usado para ser mostrado
     * en las distintas aplicaciones.
     * 
     * @return El nombre del menú.
     */
    String getNombre();
    
    /**
     * Devuelve la acción asociada a éste menú.
     * 
     * @return La acción asociada al menú.
     */
    public EnumAccionesMenu getAccion();
}
