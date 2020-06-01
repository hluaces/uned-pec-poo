package busqueda;

import java.util.Set;

/**
 * Interfaz que ha de implementarse por cualquier objeto del que asumimos puede 
 * ser 'buscado' por un filtro de búsqueda.
 * 
 * @author Héctor Luaces Novo
 */
public interface BuscableIF {
    /**
     * Devuelve 'true' si el objeto tiene un campo por el que pueda ser buscado
     * con el nombre pasado como String.
     * 
     * @param campo El nombre del campo por el que queremos saber si el objeto 
     * puede ser buscado.
     * 
     * @return True (si el objeto puede ser buscado por ese campo) o falso (de
     * no ser así).
     */
    boolean tieneCampoBuscable(String campo);
    
    /**
     * Devuelve el valor del objeto para un campo de búsqueda especificado.
     * @param campo Nombre del campo de búsqueda que queremos localizar.
     * 
     * @return El valor del campo de búsqueda
     */
    Object getValorCampo(String campo);
    
    /**
     * Devuelve un Set con todos los campos buscables que posee el objeto.
     * 
     * @return El set de los campos buscables del objeto.
     */
    Set<String> getCamposBuscables();
}
