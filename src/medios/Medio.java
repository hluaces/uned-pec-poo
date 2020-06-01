package medios;

import prestamos.EnumEstadosPrestamo;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import busqueda.BuscableIF;

/**
 * Clase que implementa la funcionalidad de un medio.
 * 
 * Un medio es cada uno de los elementos que una biblioteca puede trabajar y 
 * entregar a los usuarios.
 * 
 * Estos pueden venir en diferentes formas: libros, música, documentales, etc.
 * 
 * La implementación de los medios se basa en Atributos. Un medio tendrá un 
 * conjunto de atributos (con sus respectivos valores) y los subtipos de 
 * medios irán especializando éste conjunto de atributos más y más.
 * 
 * La clase Medio sirve como factoría para crear medios externamente.
 * 
 * Los constructores externos están prohibidos.
 * 
 * @author Héctor Luaces Novo
 */

class Medio implements MedioIF, BuscableIF {
    /**
     * El conjunto de atributos, ordenado en un map catalogado por 
     * {@link EnumTiposAtributo tipo de atributo} para agilizar
     * el acceso.
    /*/
    private Map<EnumTiposAtributo, AtributoMedio> atributos;

    /**
     * El constructor por defecto de los medios que inicializa todos los datos
     * e inicializa los atributos por defecto.
     * 
     * Visibilidad package-protected.
     * @see MediosFactory#medioFactory(java.util.Set) 
     */
    Medio() {
        this.atributos = new HashMap<>();
    }
    
    /**
     * Añade un atributo al grupo de "atributos validados" del medio.
     * 
     * Un atributo validado es aquel que cuyo valor puede establecerse.
     * 
     * Un intento de fijar un valor que no está validado no se ejecutará
     * de forma normal.
     * 
     * @param atributo Enumeración del atributo que queremos validar
     * @return True si el atributo se validó con éxito.
     */
    protected boolean validarAtributo(EnumTiposAtributo atributo) {
        if ( this.hasAtributo(atributo) )
            return false;
        
        this.atributos.put(atributo, new AtributoMedio(atributo, null));
        return true;
    }
    
    /**
     * Devuelve un set con todos los atributos del medio.
     * 
     * @return Set con todos los atributos del medio
     */
    @Override
    public Set<AtributoMedio> getAtributosMedio() {
        return new HashSet(this.atributos.values());
    }

    /**
     * Devuelve el atributo del medio (si existe) para un nombre 
     * de atributo dado
     * 
     * @param atributo Enumeración con el nombre de atributo que queremos consultar
     * @return Atributo que buscamos (puede ser null)
     */
    @Override
    public AtributoMedio getAtributo(EnumTiposAtributo atributo) {
        return this.atributos.get(atributo);
    }

    /**
     * Determina si el medio posee un atributo validado con el nombre
     * especificado.
     * 
     * @param atributo Enumeración con el nombre de atributo a consultar
     * @return True si el atributo existe
     */
    @Override
    public boolean hasAtributo(EnumTiposAtributo atributo) {
        return this.atributos.keySet().contains(atributo);
    }
    
    /**
     * Método de conveniencia.
     * 
     * Determina si el medio posee un atributo validado que no tenga valor 
     * nulo con el nombre especificado.
     * *
     * @param atributo Enumeración del atributo que queremos consultar
     * @return True si el atributo está validado y tiene valor
     */
    public boolean hasAtributoNoNull(EnumTiposAtributo atributo) {
        if ( ! this.hasAtributo(atributo) )
            return false;
        
        return this.atributos.get(atributo) != null;
    }

    /**
     * Añade un atributo al medio y devuelve 'true' si la operación tiene éxito.
     * 
     * @param atributo Enumeración del atributo a añadir
     * @return True si la operación tine éxito. Si el atributo no está validado
     * se devolverá 'false'.
     */
    @Override
    public boolean addAtributo(AtributoMedio atributo) {
        if ( ! this.hasAtributo(atributo.getNombre()) )
            return false;
        
        this.atributos.put(atributo.getNombre(), atributo);
        return true;
    }

    /**
     * Devuelve el valor de un atributo si es que éste existe en el medio.
     * 
     * Devolverá null si éste no existe.
     * 
     * @param atributo Enumeración del atributo a consultar
     * @return Valor del atributo (puede ser null)
     */
    @Override
    public Object getValorAtributo(EnumTiposAtributo atributo) {
        return this.getValorAtributo(
            atributo,
            atributo.getClase()
        );
    }

    /**
     * Devuelve el valor de un atributo haciéndole un casting al tipo 
     * que especifiquemos.
     * 
     * Es un método de conveniencia para getValorAtributo y garantizar
     * que el type-hinting nos facilita el desarrollo.
     * 
     * @param <K> Tipo de la clase a la que queremos hacer casting.
     * 
     * @param nombreAtributo Enumeración del atributo a consultar.
     * @param clase Clase a la que queremos convertir el valor
     * @return El valor del objeto (al que se ha hecho casting) o null, si el
     * casting no pudo hacerse o el valor no existe.
     */
    @Override
    public <K> K getValorAtributo(EnumTiposAtributo nombreAtributo, Class<K> clase) {
        AtributoMedio atributo;
        
        if ( ! this.hasAtributoNoNull(nombreAtributo) || clase == null )
            return null;
        
        atributo = this.getAtributo(nombreAtributo);
        
        if ( atributo.getValor() == null )
            return null;
        
        if ( atributo.getValor().getClass() != clase )
            return null;
        
        return (K) atributo.getValor();
    }

    /**
     * Devuelve un set con el nombre (enumeración) de todos los atributos de 
     * éste medio.
     * 
     * @return Set con todas las enumeraciones del nombre de atributo de éste medio
     */
    @Override
    public Set<EnumTiposAtributo> getTiposAtributo() {
        return this.atributos.keySet();
    }

    /**
     * Método de conveniencia.
     * 
     * Devuelve el título de un medio.
     * 
     * @return El título de un medio
     */
    @Override
    public String getTitulo() {
        return this.getValorAtributo(EnumTiposAtributo.TITULO, String.class);
    }

    /**
     * Método de conveniencia.
     * 
     * Devuelve el estado de un medio.
     * 
     * @return El estado en el que se encuentra un medio.
     */
    @Override
    public EnumEstadosPrestamo getEstado() {
        return this.getValorAtributo(EnumTiposAtributo.ESTADO, EnumEstadosPrestamo.class
        );
    }

    
    /**
     * Método de conveniencia.
     * 
     * Devuelve el género de un medio.
     * 
     * @return El género del medio
     */
    @Override
    public String getGenero() {
        return this.getValorAtributo(EnumTiposAtributo.GENERO, String.class);
    }

    /**
     * Método de conveniencia.
     * 
     * Devuelve el autor de un medio.
     * 
     * @return El autor del medio
     */
    @Override
    public String getAutor() {
        return this.getValorAtributo(EnumTiposAtributo.AUTOR, String.class);
    }

    /**
     * Método de conveniencia.
     * 
     * Devuelve el tipode un medio.
     * 
     * @return El tipo del medio
     */
    @Override
    public String getTipo() {
        return this.getValorAtributo(
            EnumTiposAtributo.TIPO,
            EnumTiposMedios.class
        ).getNombre();
    }

    /**
     * Método de conveniencia.
     * 
     * Devuelve la fecha en la que se creó/publicó el medio.
     * 
     * @return La fecha de publicación
     */
    @Override
    public Date getFecha() {
        return this.getValorAtributo(EnumTiposAtributo.TIPO, Date.class);
    }

    /**
     * Devuelve el valor de un campo.
     * 
     * Implementa tanto la interfaz BuscableIF como la interfaz Medio.
     * 
     * @param campo Nombre del atributo a buscar.
     * @return Valor del atributo (puede ser null)
     */
    @Override
    public Object getValorCampo(String campo) {
        return this.getValorAtributo(EnumTiposAtributo.valuePorNombre(campo));
    }

    /**
     * Determina si el medio tiene un campo (de nombre pasado como parámetro)
     * por el que pueda ser buscado.
     * 
     * @param campo Nombre del campo que queremos comprobar si es válido para
     * buscar
     * @return True si es válido, falso de cualquier otra foram
     */
    @Override
    public boolean tieneCampoBuscable(String campo) {
        return this.hasAtributo(EnumTiposAtributo.valuePorNombre(campo));
    }
    
    /**
     * Devuelve un set con el nombre de todos los campos buscables que tiene
     * este medio.
     * 
     * @return Set con el nombre de todos los campos buscables que tiene este 
     * medio
     */
    @Override
    public Set<String> getCamposBuscables() {
        Set<String> nombres = new HashSet<>();
        
        for (EnumTiposAtributo e: this.getTiposAtributo()) 
            nombres.add(e.getNombre());

        return nombres;
    }

    @Override
    public boolean setValorAtributo(EnumTiposAtributo tipo, Object valor) {
        if ( ! this.hasAtributo(tipo) )
            return false;
        
        if ( ! valor.getClass().equals(tipo.getClase()) )
            return false;
            
        this.atributos.put(tipo, new AtributoMedio(tipo, valor));
        return true;
    }

    @Override
    public String getBibliotecaOrigen() {
         return this.getValorAtributo(EnumTiposAtributo.BIBLIOTECA, String.class);
    }
    
}
