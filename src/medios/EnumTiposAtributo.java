package medios;

import prestamos.EnumEstadosPrestamo;
import java.util.NoSuchElementException;
import misc.StringUtil;

/**
 * Enumeración que encapsula los tipos de {@link AtributoMedio} que se usarán 
 * en los medios, así como el tipo de dato que usa cada uno y algún que otro 
 * tipo de metadato.
 *
 * @author Héctor Luaces Novo
 */
public enum EnumTiposAtributo {
    /**
     * Tipo que clasifica al medio en una familia (libro, audio, vídeo, etc)
     *//**
     * Tipo que clasifica al medio en una familia (libro, audio, vídeo, etc)
     *//**
     * Tipo que clasifica al medio en una familia (libro, audio, vídeo, etc)
     *//**
     * Tipo que clasifica al medio en una familia (libro, audio, vídeo, etc)
     */
    TIPO        ("Tipo"       , EnumTiposMedios.class, 0, true),
    
    /**
     * Estado que representa si el medio está disponible, prestado, reservado, 
     * etc.
     * 
     * @see EnumEstadosPrestamo
     */
    ESTADO      ("Estado"    , EnumEstadosPrestamo.class,  1, false),
    
    /**
     * El autor del medio. 
     * 
     * Entendemos como autor la persona que escribe un libro, el artista o grupo
     * que hace un disco o el director que se encarga de una película.
     */
    AUTOR       ("Autor"      , String.class, 1, true),
    
    /**
     * El título del medio.
     */
    TITULO      ("Título"     , String.class, 2, true),
    
    /**
     * El álbum en una pista musical.
     */
    ALBUM       ("Álbum"      , String.class, 3, false),
    
    /**
     * El número de publicación o edición de una revista o periódico.
     */
    //PUBLICACION ("Publicación", Integer.class, 4, false),
    
    /**
     * La cadena que identifica a un grupo de revistas o periódicos y que 
     * permite a un usuario suscribirse a ellas.
     */
    SUSCRIPCION ("Suscripción", String.class, 5, false),
    
    /**
     * Género literario, musical o cinematográfico de un medio (horror, ciencia
     * ficción, comedia, etc)
     */
    GENERO      ("Género"     , String.class, 6, true),
    
    /**
     * Fecha de publicación del medio.
     */
    FECHA       ("Fecha"      , String.class, 7, true),
    
    /**
     * Duración (en minutos) de un medio audiovisual.
     */
    DURACION    ("Duración"   , Integer.class, 8, false),
    
    /**
     * Nombre de la editorial de un libro.
     */
    EDITORIAL   ("Editorial"  , String.class, 9, false),
    
    /**
     * Formato de un medio audiovisual (mp4, vhs, mp3, mkv, flv, etc)
     */
    FORMATO     ("Formato"    , String.class, 7, false),
    
    /**
     * Identificador único de un libro.
     */
    ISBN        ("ISBN"       , String.class, 9, false),
    
    /**
     * Si el medio ha sido cedido, determina la biblioteca origen
     * del medio.
     */
    BIBLIOTECA      ("Biblioteca", String.class, 10, false)
    ;
    
    /**
     * El nombre de un Tipo de atributo hace referencia al texto que será usado
     * de cara al usuario para representar un tipo de atributo.
     */
    private final String nombre;
    
    /**
     * La clase representa el tipo de dato que será usado en los valores de 
     * éste atributo.
     */
    private final Class clase;
    
    /**
     * éste dato servirá para ordenar los atributos de los medios en los 
     * listados. Los órdenes más bajos aparecerán antes que los más altos.
     */
    private final int orden;
    
    /**
     * Determina si un atributo es principal o no.
     * 
     * Los atributos principales son comunes a todos los medios.
     */
    private final boolean principal;
    
    EnumTiposAtributo(String nombre, Class c, int orden, boolean principal) {
        this.nombre    = nombre;
        this.clase     = c;
        this.orden     = orden;
        this.principal = principal;
    }
    
    /**
     * Devuelve el nombre a mostrar al usuario de un tipo de atributo.
     * 
     * @return El nombre de un tipo de atributo.
     */
    public String getNombre() {
        return this.nombre;
    }
    
    /**
     * Devuelve la clase del tipo de dato de los valores de un tipo de 
     * atributo.
     * 
     * @return La clase usada por los valores de un tipo de atributo.
     */
    public  Class getClase() {
        return this.clase;
    }

    /**
     * Devuelve el orden del tipo de atributo.
     * 
     * El orden es usado para disponer antes o después los atributos en los
     * diferentes listados de la aplicación.
     * 
     * @return El orden del tipo de atributo.
     */
    public  int getOrden() {
        return orden;
    }
    
    /**
     * Determina si un atributo es principal o no.
     * 
     * @return True si el atributo es principal, false is no lo es.
     */
    public boolean isPrincipal() {
        return principal;
    }
    
    /**
     * Método de conveniencia equivalente a {@link EnumTiposAtributo#getNombre()}
     * 
     * @return El nombre del tipo de atributo.
     */
    @Override
    public String toString() {
        return this.getNombre();
    }
    
    /**
     * Dado un nombre de atributo el método devuelve la enumeración cuyo
     * nombre coincida con el parámetro pasado.
     * 
     * @param nombre Nombre que queremos convertir en un tipo de atributo
     * @return El tipo de atributo. Se lanzará un
     * @throws NoSuchElementException Si no hay ningún elemento con ese nombre.
     */
    public static EnumTiposAtributo valuePorNombre(String nombre) {
        for(EnumTiposAtributo e: EnumTiposAtributo.values()) {
            if (StringUtil.compararSinTildes(e.getNombre(), nombre))
                return e;
        }
        
        throw new NoSuchElementException(
            "No existe un Atributo con el nombre '" + nombre + "'."
        );
    }
}
