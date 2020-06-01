package biblioteca;

import busqueda.Buscador;
import busqueda.Filtro;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import busqueda.BuscableIF;
import medios.MedioIF;

/**
 * Objeto que representa una colección de medios de una biblioteca.
 * 
 * El catálogo 'cataloga' -valga la redundancia- una lista de medios en familias
 * y permite la gestión de los medios que encapsula.
 * 
 * @author Héctor Luaces Novo
 */
public class Catalogo {
    /**
     * La lista de objetos que se guarda.
     * 
     * Se usa un Map<String,Medio> donde String hace referencia al tipo de medio
     * (revista, libro, etc) y la lista a los medios de ese tipo que tiene
     * la biblioteca.
     */
    private final Map<String, List<MedioIF>> medios;

    /**
     * Crea un nuevo catálogo vacío
     */
    public Catalogo() {
        this.medios = new HashMap<>();
    }
    
    /**
     * Inicializa una categoría en el Map de medios de la biblioteca, si es que
     * ésta no estaba inicializada.
     * 
     * @param categoria Nombre de la categoría a inicializar
     */
    private void inicializarCategoria(String categoria) {
        if ( ! this.medios.keySet().contains(categoria) )
            this.medios.put(categoria, new ArrayList<>());
    }
    
    /**
     * Devuelve 'true' si el catálogo tiene una categoría de medios cuyo nombre 
     * sea igual al string pasado como parámetro.
     * 
     * Este método solo comprueba si la categoría existe, no si tiene contenido,
     * es decir, si ésta ha sido inicializada.
     * *
     * @param tipo Nombre de la categoría que queremos consultar
     * @return True (si la categoría existe) false, si no lo hace.
     */
    public boolean hasCategoria(String tipo) {
        return this.medios.keySet().contains(tipo);
    }
    
    /**
     * Añade un nuevo medio al catálogo.
     *
     * Importante tener en cuenta que un catálogo no permite medios duplicados. 
     * ¡¡OJO!!, un medio duplicado se comprueba por referencia; esto significa 
     * que dos instancias de libros iguales si tienen cabida en un catálogo 
     * (una biblioteca puede tener varias unidades de un mismo libro). Lo que no
     * se permite son referencias duplicadas por integridad.
     * 
     * @param m Medio a añadir.
     * @return True si la operación tiene éxito.
     * @throws IllegalArgumentException Si ocurre algún error al intentar añadir
     * el medio.
     */
    public boolean addMedio(MedioIF m) {
        if ( m == null )
            throw new IllegalArgumentException(
                "Se ha intentado añadir un medio nulo."
            );        
        
        // No permitimos medios duplicados
        if ( this.hasMedio(m) )
            throw new IllegalArgumentException(
                "Se ha intentado añadir un medio duplicado al catálogo."
            );
        
        // Inicializamos la categoría si ésta no lo está ya
        if ( ! this.hasCategoria(m.getTipo()) )
            this.inicializarCategoria(m.getTipo());

        return this.medios.get(m.getTipo()).add(m);
    }
    
    /**
     * Comprueba si una biblioteca tiene un medio que se ha pasado como 
     * parámetro.
     * 
     * La comprobación se hace por referencia, no comprueba los valores y ésta
     * comprobación se hace únicamente para garantizar la integridad referencial
     * de la información.
     * 
     * @param m Medio que queremos comprobar si existe.
     * @return True (si el medio existe) false (si no existe)
     */
    public boolean hasMedio(MedioIF m) {
        if ( ! this.hasCategoria(m.getTipo()) )
            return false;
        
        return this.medios.get(m.getTipo()).contains(m);
    }
    
    /**
     * Elimina un medio dado que se supone existe en el catálogo.
     * 
     * @param m Medio que queremos eliminar del catálogo
     * @return True (si se elimina con éxito) falso (si no se elimina)
     */
    public boolean removeMedio(MedioIF m) {
        if ( ! this.hasMedio(m) )
            return false;
        
        return this.medios.get(m.getTipo()).remove(m);
    }
    
    /**
     * Devuelve un set de Strings con los tipos de medio inicializados de éste
     * catálogo.
     * 
     * No se garantiza que los tipos tengan contenido, solo que estén
     * inicializados.
     * 
     * @return Set con los tipos de medio.
     */
    public Set<String> getTiposMedio() {
        return this.medios.keySet();
    }
    
    /**
     * Devuelve una lista con los medios de éste catálogo que pertenecen a 
     * un tipo dado.
     *
     * @param tipo Nombre del tipo de medios que queremos recuperar.
     * @return La lista de medios que cumplen con el tipo especificado.
     */
    public List<MedioIF> getMedios(String tipo) {
        if ( ! this.medios.keySet().contains(tipo) )
            return new ArrayList<>();
        
        return this.medios.get(tipo);
    }
    
    
    /**
     * Devuelve una lista con todos los medios de éste catálogo, sin importar
     * a qué tipo de medio pertenezcan.
     * 
     * @return La lista de medios de éste catálogo.
     */
    public List<MedioIF> getMedios() {
        List<MedioIF> m = new ArrayList<>();
        
        this.medios.entrySet().stream().forEach((e) -> {
            m.addAll(e.getValue());
        });
        
        return m;
    }
    
    
    /**
     * Dado un filtro de búsqueda, devuelve una lista con todos los medios
     * del catálogo que cumplan con dicho filtro.
     * 
     * @param f El filtro de búsqueda
     * @return La lista de medios del catálogo que cumplen con dicho filtro.
     */
    public List<MedioIF> buscarEn(Filtro f) {
        Buscador b = new Buscador(f);
        List<MedioIF> retorno;
        List<MedioIF> filtro;
        
        retorno = new ArrayList<>();
        
        for (Entry <String, List<MedioIF>> e: this.medios.entrySet()) {
            filtro = new ArrayList(e.getValue());
            
            filtro.removeIf(p -> ! b.isValidoPara((BuscableIF) p));
            retorno.addAll(filtro);
        }
        
        return retorno;
    }
}
