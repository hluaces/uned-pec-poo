package menus;

import java.util.HashSet;
import java.util.Set;

/**
 * Clase que representa al menú de la biblioteca.
 * 
 * Un menú de biblioteca hace referencia al padre de varias {@link OpcionMenu
 * opciones del menú} y no tiene una acción asociada a si mismo.
 * 
 * En el siguiente ejemplo, "Archivo" y "Editar" son menús de biblioteca:
 * 
 *      - Archivo           - Editar
 *          > abrir             > copiar
 *          > guardar           > pegar
 *          > cerrar
 * 
 * @author Hëctor Luaces Novo
 */
public class MenuBiblioteca implements ElementoMenuIF {
    /**
     * Los opciones 'hijos' asociados a este menú.
     */
    private final Set<OpcionMenu> opciones;
    
    /**
     * El nombre con el que se mostrará este menú
     */
    private final String nombre;
    
    /**
     * Crea un nuevo menú con un nombre dado y un conjunto vacio de opciones
     * de menú.
     * 
     * Visibilidad package-protected para implementar el patrón factoría 
     * estática.
     * 
     * @see MenuBibliotecaFactory#getMenuUusario(usuarios.Usuario) 
     * @param nombre El nombre a asociar a este menú
     */
    MenuBiblioteca(String nombre) {
        this.nombre   = nombre;
        this.opciones = new HashSet<>();
    }
    
    /**
     * @return El conjunto de opciones asociadas a este menú.
     */
    public Set<OpcionMenu> getOpciones() {
        return opciones;
    }

    /**
     * Añade una nueva opción a éste menú.
     * 
     * La visibilidad es "package-protected" para implementar un patrón factoría
     * 
     * @see MenuBibliotecaFactory#crearMenuUsuario(usuarios.Usuario) 
     * @param opciones Las nueva opción a añadir al menú.
     */
    boolean addOpcion(OpcionMenu e) {
        return this.opciones.add(e);
    }

    /**
     * @return El nombre asociado a este menú
     */
    @Override
    public String getNombre() {
        return nombre;
    }

    /**
     * Devuelve la acción asociada a este menú.
     * 
     * Al tratarse de un menú superior, nunca tendrá una acción asociada.
     * 
     * Éste método existe para sastisfacer la interfaz {@link ElementoMenuIF}
     * 
     * @return null
     */
    @Override
    public EnumAccionesMenu getAccion() {
        return null;
    }
}
