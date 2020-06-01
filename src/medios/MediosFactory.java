package medios;

import prestamos.EnumEstadosPrestamo;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Clase factoría para interactuar con el paquete de 'medios'.
 * 
 * Permite instanciar y obtener información de los medios.
 * @author Héctor Luaces Novo
 */
public final class MediosFactory {
    
    /**
     * La clase es estática y no puede ser instanciada o heredada.
     */
    private MediosFactory() {
        
    }

    /**
     * Método factoría, de uso interno para ésta clase, que devuelve un objeto
     * medio sin inicializar para el nombre de un 
     * {@link EnumTiposAtributo#TIPO tipo} de medio.
     * 
     * @deprecated
     * @param medio El nombre del tipo de medio que queremos crear
     * @return El objeto medio del tipo solicitado o 'null' si el nombre no 
     * se corresponde con un tipo de medio.
     */
    private static MedioIF getMedioPorNombre(String medio) {
        if ( null == EnumTiposMedios.getTipoPorNombre(medio) )
            return null;
        
        return new Medio();
    }
    
    /**
     * Devuelve un array de Strings con los nombres de todos los tipos de medio
     * disponibles en la aplicación.
     * 
     * @deprecated
     * @return Un array de strings con los nombres de los distintos tipos de medio.
     */
    public static String[] getTiposMedio() {
        EnumTiposMedios[] tipos = EnumTiposMedios.values();
        
        return Arrays
            .asList(tipos)
            .stream()
            .map((c) -> c.getNombre())
            .toArray(String[]::new)
        ;
    }

    /**
     * Devuelve un Set con los Atributos que usa un medio de un tipo pasado
     * como parámetro (String). Los atributos estarán inicializados como
     * una instancia de uno de esos medios.
     * 
     * @param a_buscar Tipo de medio del que queremos saber su conjunto de atributos.
     * @return Set con los atributos del parámetro.
     */
    public static Set<AtributoMedio> dameAtributosMedio(String a_buscar) {
        EnumTiposMedios tipo;
        
        tipo = EnumTiposMedios.getTipoPorNombre(a_buscar);
        Set<AtributoMedio> ret;
        
        if ( tipo == null )
            return null;
       
        ret = new HashSet<>();
        
        // Conjunto de atributos principales
        ret.add(new AtributoMedio(EnumTiposAtributo.TIPO  , null));
        ret.add(new AtributoMedio(EnumTiposAtributo.AUTOR , null));
        ret.add(new AtributoMedio(EnumTiposAtributo.TITULO, null));
        ret.add(new AtributoMedio(EnumTiposAtributo.ESTADO, null));
        ret.add(new AtributoMedio(EnumTiposAtributo.FECHA , null));
        ret.add(new AtributoMedio(EnumTiposAtributo.GENERO, null));
        ret.add(new AtributoMedio(EnumTiposAtributo.BIBLIOTECA, null));
        
        // Atributos específicos
        switch(tipo) {
            case LIBRO:
                ret.add(new AtributoMedio(EnumTiposAtributo.EDITORIAL, null));
                ret.add(new AtributoMedio(EnumTiposAtributo.ISBN, null));
                break;
            
            case AUDIO:
                ret.add(new AtributoMedio(EnumTiposAtributo.ALBUM  , null));
                // No hay break. Es intencional.
                
            case VIDEO:
                ret.add(new AtributoMedio(EnumTiposAtributo.FORMATO, null));
                ret.add(new AtributoMedio(EnumTiposAtributo.DURACION, null));
                break;
                
            case REVISTA:
            case PERIODICO:
                //ret.add(new AtributoMedio(EnumTiposAtributo.PUBLICACION, null));
                ret.add(new AtributoMedio(EnumTiposAtributo.SUSCRIPCION, null));
                break;
        }
        
        return ret;
    }
    
    /**
     * Devuelve un objeto de medio vacío, es decir, sin ningún atributo 
     * establecido más allá del {@link EnumTiposAtributo#TIPO tipo}, que será
     * el que pasemos por parámetro.
     * 
     * @param m El tipo de objeto vacío que queremos crear.
     * @return El medio vacio a crear.
     */
    private static Medio getMedioVacio(EnumTiposMedios m) {
        Set<AtributoMedio> atr;
        Medio medio;
        
        atr = m != null ? dameAtributosMedio(m.getNombre()) : null;
        
        if ( atr == null )
            return null;
        
        medio = new Medio();
        
        // Validamos todos los atributos válidos para este tipo de medio
        for(AtributoMedio a: atr) {
            medio.validarAtributo(a.getNombre());
            medio.addAtributo(a);
        }
        
        medio.addAtributo(new AtributoMedio(EnumTiposAtributo.TIPO, m));
        return medio;
    }
    
    /**
     * Dado un conjunto de atributos, intenta crear la instancia de un medio 
     * con los datos facilitados.
     * 
     * Es imprescindible que en el set facilitado se incluya un atributo 
     * {@link EnumTiposAtributo#TIPO tipo} o, de lo contrario, el método
     * será incapaz de determinar que medio queremos crear.
     * 
     * @param atributos Set de atributos para los que queremos crear un medio.
     * @return Un medio con todos los atributos especificados o null si 
     * no se consigue crear.
     */
    public static MedioIF getMedio(Set<AtributoMedio> atributos) {
        EnumTiposMedios valorTipo;
        AtributoMedio tipo;
        Medio m;
        
        // Primero localizamos si hemos especificado el tipo de medio que 
        // queremos crear
        tipo = atributos.stream()
            .filter((c) -> c.getNombre().equals(EnumTiposAtributo.TIPO))
            .findFirst()
            .orElse(null)
        ;
        
        // No se ha especificado
        if ( tipo == null )
            return null;
        
        // Generamos un medio vacío (o lo intentamos)
        m = getMedioVacio(EnumTiposMedios.getTipoPorNombre(tipo.getValor().toString()));
        
        if ( m == null )
            return null;
        
        valorTipo = EnumTiposMedios.getTipoPorNombre(tipo.getValor().toString());
        
        if ( valorTipo == null )
            return null;
        
        // Establecemos valores por defecto (tipo y estado)
        tipo.setValor(valorTipo);
        atributos.add(new AtributoMedio(
            EnumTiposAtributo.ESTADO, 
            EnumEstadosPrestamo.DISPONIBLE
        ));
        
        // Añadimos el resto de atributos al medio
        for(AtributoMedio atr: atributos) {           
            m.addAtributo(atr);
        }

        return m;
    }
}
