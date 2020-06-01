package usuarios;

import java.util.EnumSet;
import java.util.Set;

/**
 * Clase que representa un perfil de {@link Usuario}.
 * 
 * Los perfiles permiten agrupar un conjunto de {@link EnumPermisos permisos}
 * que determinarán a que tiene acceso y a que no un usuario.
 * 
 * @see UsuariosFactory#crearPerfil(usuarios.EnumPerfiles) 
 * @author Héctor Luaces Novo
 */
public class Perfil {
    /**
     * El conjunto de permisos asociados a este perfil.
     */
    private Set<EnumPermisos> permisos;
    
    /**
     * El tipo de perfil de éste perfil.
     */
    private final EnumPerfiles perfil;
    
    /**
     * Crea un perfil sin ningún permiso con un nombre facilitado.
     * 
     * La visibilidad del contructor es 'package-protected' para poder
     * implementar una factoría estática.
     * 
     * @see UsuariosFactory#crearPerfil(usuarios.EnumPerfiles) 
     */
    Perfil(EnumPerfiles perfil) {
        this.permisos = EnumSet.noneOf(EnumPermisos.class);
        this.perfil   = perfil;
    }
    
    /**
     * Devuelve un set con los permisos de los que dispone el perfil.
     * 
     * @return Permisos que tiene el perfil.
     */
    public Set<EnumPermisos> getPermisos() 
    {
        return this.permisos;
    }
    
    /**
     * Permite especificar todos los permisos del perfil de golpe.
     * 
     * Por seguridad, el método solo está disponible para las clases hijas.
     * 
     * Visibilidad package-protected.
     * 
     * @param permisos El nuevo set de permisos a establecer para el perfil
     */
    void setPermisos(Set<EnumPermisos> permisos)
    {
        this.getClass().getName();
        this.permisos = (EnumSet<EnumPermisos>) permisos;
    }
    
    /**
     * Añade un nuevo permiso al perfil y devuelve un valor en función
     * del éxito de la operación
     * 
     * No se permitirán elementos duplicados o nulos.
     * 
     * Visibilidad package-protected.
     * 
     * @param permiso Nuevo permiso a Añadir
     * @return True si se añade con éxito, falso de cualquier otra forma.
     */
    boolean addPermiso(EnumPermisos permiso) 
    {
        if ( permiso == null )
            throw new IllegalArgumentException();
        
        return permisos.add(permiso);
    }
    
    /**
     * Verifica si un perfil tiene el permiso especificado.
     * 
     * @param permiso Permiso a comprobar
     * @return True si el perfil dispone de dicho permiso, falso de cualquier 
     * otra forma.
     */
    public boolean hasPermiso(EnumPermisos permiso) {
        return permisos.contains(permiso);
        
    }

    /**
     * @return El nombre del tipo de perfil asociado a este objeto
     */
    @Override
    public String toString()
    {
        return this.getNombre();
    }

    /**
     * @return El nombre del tipo de perfil asociado a este objeto
     */
    public String getNombre() {
        return this.perfil.getNombrePerfil();
    }
}
