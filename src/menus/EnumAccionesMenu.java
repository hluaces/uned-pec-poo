package menus;

/**
 * Enumeración que representa las posibles acciones asociadas a un menú.
 * 
 * Una acción hace referencia a cualquier función que se ejecute cuando
 * se invoque un menú.
 * 
 * La enumeración existe para poder facilitar la creación de una aplicación
 * cuya lógica de menús e interfaz de usuario esté desacoplada.
 * 
 * @author Héctor Luaces Novo
 */
public enum EnumAccionesMenu {
    /**
     * Buscar medios en la biblioteca activa
     */
    MEDIOS_BUSCAR ("Buscar medios"),
    
    /**
     * Buscar medios en todas las bibliotecas
     */
    MEDIOSC_BUSCAR_CRUZADOS ("Búsqueda cruzada de medios"),
    
    /**
     * Ver el catálogo de medios
     */
    MEDIOS_VER ("Ver catálogo"),
    
    /**
     * Ver los mensajes del usuario
     */
    MENSAJES ("Mis mensajes"),

    /**
     * Ver los préstamos activos del usuario
     */
    PRESTAMOS_VER ("Mis préstamos activos"),    
    
    /**
     * Ver el historial de préstamos del usuario
     */
    PRESTAMOS_HISTORIAL ("Mi historial de préstamos"),
    
    /**
     * Solicitar un préstamo a la biblioteca
     */
    PRESTAMOS_SOLICITAR ("Solicitar préstamo"),
    
    /**
     * Devolver uno de los préstamos activos del propio usuario a la biblioteca
     */
    PRESTAMOS_DEVOLVER ("Devolver préstamo"),
    
    /**
     * Muestra las multas al propio usuario
     */
    MULTAS_VER ("Mis multas"),
    
    /**
     * Muestra las suscripciones al propio usuario
     */
    SUSCRIPCIONES("Mis suscripciones"),
    
    /**
     * Muestra las suscripciones de lpropio usuario
     */
    RESERVAS_VER ("Mis reservas"),
    
    /**
     * Permite anular una reserva del propio usuario
     */
    RESERVAS_ANULAR ("Anular reserva"),
    
    /**
     * Muestra un listado de todos los préstamos de la biblioteca.
     */
    GESTION_PRESTAMOS_VER ("Historial de préstamos"),
    
    /**
     * Muestra un listado de todos los préstamos fuera de plazo en la 
     * biblioteca.
     */
    GESTION_PRESTAMOS_VER_FUERA_PLAZO ("Listado préstamos fuera de plazo"),
    
    /**
     * Da de alta un medio en la biblioteca.
     */
    GESTION_MEDIOS_ALTA ("Alta nuevo medio"),
    
    /**
     * Muestra la gestión de medios (alta / eliminación)
     */
    GESTION_MEDIOS ("Gestión medios"),
    
    /**
     * Ejecuta el alta de un nuevo usuario
     */
    GESTION_USUARIOS_ALTA ("Alta nuevo usuario"),
    
    /** 
     * Muestra la gestión de usuarios (alta / eliminación / consulta)
     */
    GESTION_USUARIOS ("Gestión usuarios"),
    
    /**
     * Muestra e imprime tarjetas de usuario
     */
    GESTION_USUARIOS_TARJETAS ("Gestión de tarjetas de usuario"),

    /**
     * Muestra todas las reservas activas en la biblioteca
     */
    GESTION_RESERVAS_VER ("Ver reservas"),
    ;
    
    /**
     * El nombre de la acción es el nombre que tendra una acción del menú
     * de cara al usuario.
     */
    private final String nombreAccion;

    private EnumAccionesMenu(String nombre_accion) {
        this.nombreAccion = nombre_accion;
    }
    
    /**
     * @return El nombre de cara al usuario asignado a esta enumeración.
     */
    public String getNombreAccion() {
        return this.nombreAccion;
    }
    
}
