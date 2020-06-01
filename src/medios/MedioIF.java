package medios;

import prestamos.EnumEstadosPrestamo;
import java.util.Date;
import java.util.Set;

/**
 * Interfaz que describe el comportamiento de un Medio.
 * 
 * Un medio es cualquier material que puede ser almacenado en una biblioteca y
 * que consta de varios {@link AtributoMedio atributos}.
 * 
 * @author Héctor Luaces Novo
 */
public interface MedioIF {
    /**
     * Devuelve todos los {@link AtributoMedio atributos} de los que consta
     * el medio.
     * 
     * @return Set de atributos del medio
     */
    Set<AtributoMedio> getAtributosMedio();
    
    /**
     * Dado un {@link EnumTiposAtributo tipo de atributo}, devuelve el objeto
     * {@link AtributoMedio} del medio -si lo tiene- o null -si no lo tiene.
     * 
     * @param tipoAtributo Tipo de atributo que queremos comprobar.
     * @return Objeto con los datos del atributo o null si el medio no tiene
     * dicho atributo.
     */
    AtributoMedio getAtributo(EnumTiposAtributo tipoAtributo);
    
    /**
     * Determina si el medio tiene un atributo del tipo especificado.
     * 
     * No todos los medios comparten los mismos atributos (pej: un vídeo tendrá
     * una 'duración' o 'formato' mientras que un libro no).
     * 
     * @param tipoAtributo Tipo de atributo que queremos saber si tiene el medio.
     * @return True (si el medio tiene dicho atributo) false si no lo tiene.
     */
    public boolean hasAtributo(EnumTiposAtributo tipoAtributo);
    
    /**
     * Añade un atributo al medio. Si ya existe un atributo de ese tipo se 
     * sobreescribirá (puesto que dos atributos del mismo tipo se consideran 
     * iguales)
     * 
     * @see AtributoMedio#equals(java.lang.Object) 
     * @param atributo Atributo a añadir al medio
     * @return True si se añade con éxito, false si no consigue añadirse (pej: 
     * porque el atributo no está inicializado.
     */
    public boolean addAtributo(AtributoMedio atributo);
    
    /**
     * Devuelve el valor de un tipo de atributo que posea el medio.
     * 
     * Si el medio no tiene dicho atributo devolverá null.
     * 
     * @param tipoAtributo Tipo de atributo a consultar.
     * @return Valor del atributo o null si no tiene dicho atributo.
     */
    public Object getValorAtributo(EnumTiposAtributo tipoAtributo);
    
    /**
     * Sobrecarga de {@link MedioIF#addAtributo(medios.AtributoMedio)} que
     * permite hacer un casting a un tipo de clase pasada como parámetro.
     * 
     * @param <K> Tipo de dato al que queremos convertir el resultado.
     * @param tipoAtributo Tipo de atributo que queremos consultar en el medio
     * @param clase Clase a la que convertiremos el valor del atributo
     * @return El valor del atributo (convertido a la clase especificada en el
     * genérico) o null, si no tiene dicho valor.
     */
    public <K extends Object> K getValorAtributo(EnumTiposAtributo tipoAtributo, Class<K> clase);
    
    /**
     * Devuelve un set con todos los tipos de atributo que posee el medio.
     * 
     * @return Set con los tipos de atributo que posee el medio.
     */
    Set<EnumTiposAtributo> getTiposAtributo();

    /**
     * Método de conveniencia.
     * 
     * Consulta el atributo {@link EnumTiposAtributo#ESTADO} de un medio y 
     * devuelve su valor.
     * 
     * @see EnumEstadosPrestamo
     * @return El estado del medio.
    */
    EnumEstadosPrestamo getEstado();
    
    /**
     * Método de conveniencia.
     * 
     * Consulta el atributo {@link EnumTiposAtributo#TITULO} de un medio y 
     * devuelve su valor
     * 
     * @return El título del medio.
     */
    String getTitulo();
    
    /**
     * Método de conveniencia.
     * 
     * Consulta el atributo {@link EnumTiposAtributo#GENERO} de un medio y 
     * devuelve su valor.
     * 
     * El género puede hacer referencia al género literario, musical o 
     * audiovisual de un medio.
     * 
     * @return El género del medio.
     */
    String getGenero();
    
    /**
     * Método de conveniencia
     * 
     * Consulta el atributo {@link EnumTiposAtributo#AUTOR} de un medio y 
     * devuelve su valor.
     * 
     * @return El autor del medio.
     */
    String getAutor();
    
    /**
     * Método de conveniencia
     * 
     * Consulta el atributo {@link EnumTiposAtributo#TIPO} de un medio y
     * devuelve su valor.
     * 
     * @return El tipo de medio de un medio.
     */
    String getTipo();
    
    /**
     * Método de conveniencia.
     * 
     * Consulta el atributo {@link EnumTiposAtributo#FECHA} de un medio y
     * devuelve su valor.
     * 
     * @return La fecha (de publicación) de un medio.
     */
    Date getFecha();

    
    /**
     * Establece un valor para un atributo de lmedio.
     * 
     * @param tipo El tipo de atributo a cambiar.
     * @param valor El nuevo valor del atributo.
     * @return True si la operación tiene éxito.
     */
    public boolean setValorAtributo(EnumTiposAtributo tipo, Object valor);
    
    /**
     * Determina la biblioteca origen del medio.
     * 
     * @return Nombre de la biblioteca origen del medio.
     */
    public String getBibliotecaOrigen();
    
}
