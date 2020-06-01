package usuarios;

/**
 * Enumeración que engloba todos los permisos individuales que puede tener 
 * un perfil.
 * 
 * @author Héctor Luaces Novo
 */
public enum EnumPermisos {
    /**
     * Permite ver tus préstamos
     */
    PRESTAMO,
    
    /**
     * Permite buscar materiaes
     */
    BUSCAR,
    
    /**
     * Permite ver tus multas
     */
    MULTAS,
    
    /**
     * Permite suscribirse/desucribirse a publicaciones
     */
    SUSCRIPCIONES,
    
    /**
     * Ver la lista de medios
     */
    MEDIOS_VER,
    
    /**
     * Permite ver tus reservas
     */
    RESERVAR,
    
    /**
     * Gestión de materiales (consulta, alta, baja, modificación, eliminación)
     */
    GESTION_MEDIOS,
    
    /**
     * Gestión de préstamos (listados, revisión, etc)
     */
    GESTION_PRESTAMOS,
    
    /**
     * Permite ver los usuarios de la biblioteca
     */
    
    VER_USUARIOS,
    /**
     * Gestión de usuarios (consulta, alta, baja)
     */
    GESTION_USUARIOS,
    
    /**
     * Ver e imprimir tarjetas de usuario.
     */
    GESTION_TARJETAS,
    
    /**
     * Gestión de multas (creación, eliminación, cobro)
     */
    GESTION_MULTAS,
    
    /**
     * Gestión de reservas (consulta, alta, baja, modificación, eliminación)
     */
    GESTION_RESERVAS;
}
