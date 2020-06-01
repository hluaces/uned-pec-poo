package medios;

import misc.StringUtil;

/**
 * Enumeración que representa los distintos tipos de medios que pueden existir.
 * 
 * @author Héctor Luaces NOvo
 */
public enum EnumTiposMedios {
    VIDEO       ("Vídeo"),
    LIBRO       ("Libro"),
    PERIODICO   ("Periódico"),
    REVISTA     ("Revista"),
    AUDIO       ("Audio");
    
    /**
     * El nombre de la enumeración de cara a l usuario.
     */
    private String nombre;
    
    
    EnumTiposMedios(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return El nombre del tipo de medio
     */
    public String getNombre() {
        return this.nombre;
    }
            
    @Override
    public String toString() {
        return this.nombre;
    }
    
    /**
     * Dado el nombre a mostrar de un tipo de medio devuelve el objeto 
     * enumeración correspondiente.
     * 
     * @param tipo El nombre de una enumeración de tipo de medio a buscar.
     * @return El objeto de la enumeración cuyo nombre coincide con el pasado
     * como parámetro, o null si no hay ninguno coincidente.
     */
    public static EnumTiposMedios getTipoPorNombre(String tipo) {
        EnumTiposMedios [] tipos = EnumTiposMedios.values();
        
        for(int i = 0; i < tipos.length; i++) {
            String a, b;
            
            a = tipo.toLowerCase().trim();
            b = tipos[i].getNombre().toLowerCase().trim();
            
            if ( StringUtil.compararSinTildes(a, b) )
                return tipos[i];
        }
        
        return null;
    }
}
