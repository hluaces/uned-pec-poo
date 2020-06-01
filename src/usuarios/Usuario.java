package usuarios;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import misc.CryptUtil;

/**
 * Clase que representa un usuario de la biblioteca.
 * 
 * Los usuarios tienen asignada una serie de metainformación (nombre, apellidos,
 * etc.), un "login" (o nombre de usuario) que les identifica de forma única
 * y un {@link Perfil} que determina el tipo de permisos que tiene dentro
 * de la biblioteca.
 * 
 * @see UsuariosFactory#crearUsuario(java.lang.String, java.lang.String, usuarios.EnumPerfiles) 
 * @author Héctor Luaces Novo
 */
public class Usuario {
    /**
     * El nombre de usuario del objeto. Ha de ser único en las bibliotecas.
     */
    private final String login;
    
    /**
     * El objeto perfil del usuario que contiene todos los permisos del mismo.
     */
    private final Perfil perfil;
    
    /**
     * La contraseña (encriptada) del usuario.
     */
    private String password;
    
    /**
     * Un set que almacena todas las suscripciones del usuario.
     */
    private final Set<String> suscripciones;
    
    /**
     * Metadatos de lusuario: nombre, apellidos y DNI.
     * 
     * No tienen por qué estar rellenados.
     */
    private String nombre, apellidos, dni;
    
    /**
     * Crea un nuevo usuario en base a los datos obligatorios de mismo:
     * login, perfil y contraseña (sin encriptar).
     * 
     * Visibilidad package-protected para implementar un patrón factoría 
     * estática.
     * 
     * @see UsuariosFactory#crearUsuario(java.lang.String, java.lang.String, usuarios.EnumPerfiles) 
     * @param login Nombre de usuario del nuevo usuario.
     * @param perfil Perfil a establecer en el nuevo usuario.
     * @param password Contraseña del nuevo usuario.
     */
    Usuario(String login, Perfil perfil, String password) {
        this.login = login;
        this.perfil = perfil;
        this.setPassword(password);
        this.suscripciones = new HashSet<>();
    }
    
    /**
     * @return El nombre de usuario del objeto.
     */
    public String getLogin() {
        return login;
    }

    /**
     * @return La contraseña encriptada del objeto.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Establece y encripta la contraseña del usuario.
     * 
     * @param password La contraseña (sin encriptar)
     */
    public final void setPassword(String password) {
        this.password = CryptUtil.toMD5(password);
    }

    /**
     * Devuelve el perfil del usuario.
     * 
     * @return El perfil del usuario.
     */
    public Perfil getPerfil() {
        return perfil;
    }

    /**
     * Determina si un usuario tiene un permiso especificado.
     * 
     * @param permiso Permiso que queremos comprobar.
     * @return True si el usuario tiene el permiso.
     */
    public boolean hasPermiso(EnumPermisos permiso) {
        return this.perfil.hasPermiso(permiso);
    }

    /**
     * @return Un set con las suscripciones del usuario.
     */
    public Set<String> getSuscripciones() {
        return suscripciones;
    }

    /**
     * Añade una nueva suscripción al usuario.
     * 
     * @param s La suscripción a añadir.
     * @return True si la suscripción se añade con éxito.
     */
    public boolean addSuscripcion(String s) {
        return this.suscripciones.add(s);
    }
    
    /**
     * Elimina una suscripción del usuario.
     * 
     * @param s La suscripción a añadir.
     * @return True si se elimina la suscripción con éxito.
     */
    public boolean removeSuscription(String s) {
        return this.suscripciones.remove(s);
    }
    
    /**
     * Determina si un usuario está suscrito a una suscripción.
     * 
     * @param s La suscripción que queremos comprobar.
     * @return True si el usuario está suscrito.
     */
    public boolean isSuscrito(String s) {
        return this.suscripciones.contains(s);
    }
            
    /**
     * Verifica que una contraseña pasada como parámetro en texto plano sea
     * igual que la contraseña cifrada del usuario.
     * 
     * @param p La contraseña a comprobar (sin encriptar).
     * @return True si ambas contraseñas son iguales.
     */
    public boolean comprobarPassword(String p) {
        if ( password == null || p == null )
            return false;
        
        return CryptUtil.toMD5(p).equals(password);
    }

    /**
     * Determina si un usuario es igual a otro.
     * 
     * Dos usuarios se considerarán iguales si sus "login" son iguales.
     * 
     * Esto es así para que los {@link Set} que puedan los usuarios en
     * las aplicaciones no permitan dos usuarios duplicados en la colección.
     * 
     * @param obj Objeto a comprobar si es igual a este.
     * @return True si ambos objetos son iguales.
     */
    @Override
    public boolean equals(Object obj) {
        Usuario u;
        
        if ( obj == null || ! (obj instanceof Usuario) )
            return super.equals(obj);
        
        u = (Usuario) obj;
        
        return u.getLogin() == this.getLogin();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + Objects.hashCode(this.login);
        return hash;
    }

    /**
     * @return El nombre real del usuario.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre (real) del usuario.
     * 
     * @param nombre El nuevo nombre del usuario.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return Los apellidos reales del usuario.
     */
    public String getApellidos() {
        return apellidos;
    }

    /**
     * Establece los apellidos del usuario.
     * 
     * @param apellidos El apellido a establecer.
     */
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    /**
     * @return El DNI del usuario.
     */
    public String getDni() {
        return dni;
    }

    /**
     * @param dni El nuevo DNI a establecer.
     */
    public void setDni(String dni) {
        this.dni = dni;
    }
}
