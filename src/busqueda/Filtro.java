package busqueda;

import java.util.HashSet;
import java.util.Set;

/**
 * Clase que implementa un filtro de búsqueda.
 * 
 * Entendemos como filtro un conjunto de {@link Criterio criterios} que servirán 
 * para determinar si un objeto que implementa la interfaz 
 * {@link BuscableIF buscable} es válido o no para el filtro.
 *
 * Los filtros pueden ser absolutos (tienen que cumplirse todos sus criterios 
 * para que el filtro sea válido) o normales (con que se cumpla un criterio 
 * es suficiente para que el objeto sea válido).
 *
 * @see Criterio
 * @author Héctor Luaces Novo
 */
public class Filtro {
    /**
     * Un set de criterios únicos que serán encapsulados por el filtro.
     */
    private Set<Criterio> criterios;
    
    /**
     * Determina si el filtro es absoluto (true) o no
     */
    private boolean absoluto;
    
    /**
     * Crea un filtro normal (no-absoluto)
     */
    public Filtro() {
        this.criterios = new HashSet<>();
        this.absoluto  = false;
    }

    /**
     * Crea un filtro absoluto o no en función del valor del parámetro
     * pasado al constructor.
     * 
     * @param absoluto Determina si el filtro es o no absoluto.
     */
    public Filtro(boolean absoluto) {
        this();
        
        this.absoluto = absoluto;
    }
    
    /**
     * Devuelve el set de criterios de Éste filtro.
     * 
     * @return Los criterios del filtro.
     */
    public Set<Criterio> getCriterios() {
        return criterios;
    }

    /**
     * Añade un nuevo criterio al filtro.
     * 
     * @param c El criterio a añadir.
     */
    public void addCriterio(Criterio c) {
        this.criterios.add(c);
    }

    /**
     * Devuelve true si el filtro es absoluto.
     * 
     * @return True si el filtro es absoluto.
     */
    public boolean isAbsoluto() {
        return absoluto;
    }

    /**
     * Permite cambiar el tipo de filtro (absoluto / normal)
     * 
     * @param absoluto El nuevo valor del filtro.
     */
    public void setAbsoluto(boolean absoluto) {
        this.absoluto = absoluto;
    }    
    
}
