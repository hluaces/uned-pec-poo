package usuarios;

/**
 * Enumeración que lista todos los distintos tipos de {@link Perfil} de la 
 * aplicación.
 * 
 * Los perfiles tienen asociado un nombre que será el usado de cara al 
 * usuario.
 * 
 * @author Héctor Luaces Novo
 */
public enum EnumPerfiles {
    /**
     * Los bibliotecarios son usuarios que llevan a cabo las tareas de gestión
     * del propio negocio (préstamos, devoluciones, etc).
     */
    BIBLIOTECARIO ("bibliotecario"),
    
    /**
     * Los usuarios son aquellos que usan la biblioteca para pedir préstamos.
     */
    USUARIO       ("usuario");
    
    /**
     * Nombre de cara al público que tendrá cada perfil.
     */
    private final String nombrePerfil;
    
    EnumPerfiles(String perfil) {
        this.nombrePerfil = perfil;
    }
    
    /**
     * @return El nombre de cara al público de éste perfil
     */
    public String getNombrePerfil() {
        return this.nombrePerfil;
    }
    
    /**
     * Dado un nombre de perfil, busca si éste coincide con alguno de los 
     * creados en las enumeraciones.
     * 
     * De existir, devuelve el objeto de la enumeración.
     * 
     * @param tx Nombre de perfil para el que queremos obtener una enumeración.
     * @return Enumeración que coincide con ese nombre de perfil, o null
     * si no hay coincidencia.
     */
    public static EnumPerfiles getPerfilPorNombre(String tx) {
        for(EnumPerfiles p: values()) {
            if (p.getNombrePerfil().equals(tx))
                return p;
        }
        
        return null;
    }
}
