package prestamos;

/**
 * Enumeración que representa los distintos {@link EnumTiposAtributo#ESTADO 
 * estados} en los que puede estar un medio.
 * 
 * @author Héctor Luaces Novo
 */
public enum EnumEstadosPrestamo {
    /**
    * Prestado: el medio no está actualmente en la biblioteca.
    */
    PRESTADO ("Prestado"),

    /**
    * Disponible: el medio está en la biblioteca y puede ser prestado
    */
    DISPONIBLE ("Disponible"),

    /**
     * Prestado a otra biblioteca: el medio se ha cedido temporalmente
     * a otra biblioteca.
     */
    PRESTADO_BIBLIOTECA ("En otra biblioteca")
    ;
    
    /**
     * Nombre del estado que será usado de cara al usuario final
     */
    private final String estado;
    
    EnumEstadosPrestamo(String tx) {
        this.estado = tx;
    }
    
    /**
     * Devuelve el nombre del estado que será usado de cara al usuario final
     * 
     * @return El nombre del estado
     */
    public String getNombreEstado() {
        return this.estado;
    }

    /**
     * Método de conveniencia equivalente a {@link EnumEstadosPrestamo#getNombreEstado()}
     * 
     * @return El nombre del estado
     */
    @Override
    public String toString() {
        return this.getNombreEstado();
    }
    
    /**
     * Dado un nombre de cara al usuario para un estado de préstamo, busca en 
     * las enumeraciones si hay alguna que coincida con dicho nombre.
     * 
     * De existir, la devuelve.
     * 
     * @param tx El nombre de enumeración para el que queremos un objeto
     * de enumeración.
     * @return El objeto de enumeración encontrada o 'null'.
     */
    public static EnumEstadosPrestamo getEstadoPorNombre(String tx) {
        for(EnumEstadosPrestamo e: EnumEstadosPrestamo.values()) {
            if ( e.getNombreEstado().equals(tx) )
                return e;
        }
        
        return null;
    }
}
