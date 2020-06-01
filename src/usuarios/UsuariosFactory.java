package usuarios;

import java.util.EnumSet;

/**
 * Clase estática que permite crear {@link Perfil perfiles} y {@link Usuario 
 * usuarios} a partir de enumeraciones y otros datos.
 * 
 * @author Héctor Luaces Novo
 */
final public class UsuariosFactory {
    /**
     * @see UsuariosFactory#crearPerfil(usuarios.EnumPerfiles) 
     */
    private UsuariosFactory() {
    }
    
    /**
     * Patrón factoría para la creación dinámica de perfiles.
     * 
     * Facilitándole al método un {@link EnumPerfiles} devolverá un {@link 
     * Perfil} con todos los permisos debidos asignados.
     * 
     * @param perfil Enumeración del tipo de perfil que queremos crear
     * @return El perfil creado con sus permisos asociados
     */
    public static Perfil crearPerfil(EnumPerfiles perfil) {
        Perfil p = new Perfil(perfil);
        
        switch(perfil) {
            // Bibliotecario: todos los permisos existentes.
            case BIBLIOTECARIO:
                p.setPermisos(EnumSet.allOf(EnumPermisos.class));
                break;
            
            // Usuario muy pocos permisos.
            case USUARIO:
                p.addPermiso(EnumPermisos.SUSCRIPCIONES);
                p.addPermiso(EnumPermisos.MEDIOS_VER);
                p.addPermiso(EnumPermisos.PRESTAMO);
                p.addPermiso(EnumPermisos.RESERVAR);
                p.addPermiso(EnumPermisos.MULTAS);
                p.addPermiso(EnumPermisos.BUSCAR);
                break;
        }
        
        return p;
    }

    /**
     * Dado un login, password y {@link EnumPerfiles perfil} crea un usuario
     * con los datos facilitados.
     * 
     * @param login Login del nuevo usuario.
     * @param password Contraseña (sin encriptar) del nuevo usuario.
     * @param perfil Tipo de perfil del usuario que queremos.
     * 
     * @return Usuario creado con los datos facilitados.
     */
    public static Usuario crearUsuario(
            String login, String password, EnumPerfiles perfil
    )
    {
        Perfil p = UsuariosFactory.crearPerfil(perfil);
        
        return new Usuario(login, p, password);
    }
}
