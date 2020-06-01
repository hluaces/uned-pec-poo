package aplicacion;

import biblioteca.Biblioteca;
import biblioteca.Catalogo;
import busqueda.Filtro;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import usuarios.Usuario;
import java.util.Set;
import medios.MedioIF;
import prestamos.Prestamo;


/**
 * Clase que representa una instancia (ejecución) de la aplicación.
 * 
 * En ella se inicializarán las {@link Biblioteca bibliotecas} y se guardará
 * memoria la información del {@link Usuario} y biblioteca activa.
 * 
 * La clase será usada por la vistas (la interfaz de usuario) para acceder a
 * los objetos de biblioteca activa y usuario activo. También ofrecerá
 * métodos para ayudar con la gestión de login/logout y establecimiento
 * de las operaciones activas.
 * 
 * 
 * @author Héctor Manuel Luaces Novo
 */
public class Aplicacion {
    /** 
     * El listado de bibliotecas cargadas en la aplicación
     */
    private final Set<Biblioteca> bibliotecas;

    /**
     * La biblioteca activa en cada momento. Puede ser null si aún no se eligió
     * ninguna.
     */
    private Biblioteca bibliotecaActiva;
    
    /**
     * Determina el usuario activo en cada momento.
     * 
     * Puede ser null si el usuario aún no se identificó.
     * 
     * ¡¡Ojo!! las bibliotecas tienen usuarios distintos.
     */
    private Usuario usuarioActivo;

    
    /**
     * Constructor de la aplicación que inicializa todos los campos necesarios
     * para su correcto funcionamiento.
     */
    public Aplicacion() {
        this.bibliotecas      = new HashSet<>();
        this.bibliotecaActiva = null;
        this.usuarioActivo    = null;
    }

    /**
     * Añade una nueva biblioteca al grupo de bibliotecas de la aplicación.
     * 
     * @param b Biblioteca a añadir al pool de bibliotecas
     * @return 'true' si la operación se realiza con éxito. False si no es así
     * (por lo general, si se intentan añadir bibliotecas duplicadas)
     */
    public boolean anyadirBiblioteca(Biblioteca b) {
        return this.bibliotecas.add(b);
    }
    
    /**
     * Devuelve un set con el pool de bibliotecas disponibles en la aplicación.
     * 
     * @return Set de bibliotecas disponibles en la aplicación. Si no hay, 
     * el set será vacío, pero no 'null'
     */
    public Set<Biblioteca> getBibliotecas() {
        return this.bibliotecas;
    }

    /**
     * Busca por nombre una biblioteca en el pool de bibliotecas de la 
     * aplicación.
     * 
     * Si no la encuentra devolverá null.
     * 
     * @param nombre String con el nombre de la biblioteca a buscar
     * @return Objeto de la biblioteca encontrada (si existe)  o null si no 
     * se encuentra.
     * Si hay dos bibliotecas con el mismo nombre se devolverá la primera
     * encontrada.
     * 
     * Hay que tener en cuenta que el nombre es sensible a mayúsculas/minúsculas
     */
    public Biblioteca getBiblioteca(String nombre) {
        for(Biblioteca b: this.bibliotecas) {
            if ( b.getNombre().equals(nombre) )
                return b;
        }
        
        return null;
    }

    /**
     * Devuelve la biblioteca activa en la aplicación, que puede ser null
     * si aún no se ha inicializado.
     * 
     * @return La biblioteca activa en la aplicación (o null)
     */
    public Biblioteca getBibliotecaActiva() {
        return bibliotecaActiva;
    }

    /**
     * Establece la biblioteca activa en la aplicación, acpetando como 
     * parámetros la nueva biblioteca.
     * 
     * La biblioteca facilitada ha de estar en el pool de bibliotecas
     * válidas de la aplicación.
     * 
     * @param bibliotecaActiva La biblioteca activa a establecer
     * @throws IllegalArgumentException si se intenta establecer una biblioteca
     * como activa que no es válida
     */
    public void setBibliotecaActiva(Biblioteca bibliotecaActiva) {
        if ( ! this.bibliotecas.contains(bibliotecaActiva) )
            throw new IllegalArgumentException(
                "Se ha intentado activar una biblioteca no validada."
            );
        
        this.bibliotecaActiva = bibliotecaActiva;
    }

    /**
     * Dado el nombre de una biblioteca, intenta establecer como activa
     * una biblioteca con dicho nombre (es una combinación por conveniencia
     * de los métodos {@link Aplicacion#getBiblioteca(java.lang.String)) y
     * {@link Aplicacion#setBibliotecaActiva(biblioteca.Biblioteca)).
     * 
     * @param nombre Nombre de la biblioteca a acitvar
     * @return true si se activa con éxito o false si no consigue hacerlo.
     */
    public boolean setBibliotecaActiva(String nombre) {
        Biblioteca b = this.getBiblioteca(nombre);
        
        if ( b == null )
            return false;
        
        this.setBibliotecaActiva(b);
        return true;
    }
    
    /**
     * Devuelve el usuario activo de la aplicación, el cual puede ser null si
     * nadie se ha identificado aún.
     * 
     * @return El usuario activo de la aplicación
     */
    public Usuario getUsuarioActivo() {
        return usuarioActivo;
    }

    /**
     * Dado un nombre de usuario (login) y una contraseña, intenta logear
     * en la biblioteca activa con dichas credenciales y devuelve un booleano
     * en función de si ha habido éxito o no.
     * 
     * Si la operación tiene éxito, se establecerá el usuario activo de la 
     * aplicación al usuario que se ha identificado.
     *  
     * @param login Nombre de usuario a intentar logear
     * @param password Contraseña de lusuario
     * @return true (si el login tiene éxito) o false ( si el usuario o 
     * contraseña son incorrectos)
     */
    public boolean login(String login, String password) {
        Usuario u;
        
        if ( this.bibliotecaActiva == null )
            return false;
        
        u = this.bibliotecaActiva.loginUsuario(login, password);
        
        if ( u == null )
            return false;
        
        this.usuarioActivo = u;
        return true;
    }
    
    /**
     * Cierra la sesión del usuario activo (si es que existe)
     */
    public void logout() {
        this.usuarioActivo = null;
    }
    
    /**
     * Método que debería ejecutarse apoyándose en una tarea programada del 
     * sistema operativo.
     * 
     * La tarea se encarga de realizar operaciones de mantenimiento y de
     * envío de mensajes (si estos no han sido generados).
     * 
     * En esta práctica solo se ejecuta una única vez (al iniciar la aplicación)
     * pero de convertirse en una aplicación real, este método debería ofrecer 
     * una API externa que permita invocarse frecuentemente (cada hora, pej).
     * 
     * Tareas del cron:
     * - Verifica que no haya préstamos fuera de plazo. Si los hay, envía un 
     * mensaje al usuario y genera una multa.
     */
    public void cron() {
        for(Biblioteca b: this.getBibliotecas()) {
            this.cronBiblioteca(b);
        }
    }
    
    /**
     * Función usada internamente por {@link Aplicacion#cron} y que se encarga
     * de ejecutar las tareas programadas individualmente en cada biblioteca.
     * 
     * @param b La biblioteca para la que queremos ejecutar tareas programadas.
     */
    private void cronBiblioteca(Biblioteca b) {
        // Recorremos los préstamos y buscamos los que están fuera de plazo.
        // De haberlos, se multa al usuario.
        for(Prestamo p: b.getPrestamos()) {
            if ( ! p.isVencido() || p.isAvisado() )
                continue;

            b.multarUsuario(p);
        }
    }
    
    /**
     * Realiza una búsqueda de medios cruzada en todas las bibliotecas
     * de la aplicación. 
     * 
     * Una búsqueda cruzada hace referencia al hecho de buscar en todas 
     * las bibliotecas a la vez y se apoya en el método 
     * {@link Catalogo#buscarEn(busqueda.Filtro)) del catálogo de cada 
     * {@link Biblioteca}.
     * 
     * @param f Filtro a buscar en todas las bibliotecas.
     * @return Map que relaciona una biblioteca con una lista de los medios que 
     * encajan en ese criterio que se han encontrado. Si no se encuentran medios
     * en una biblioteca se garantiza una lista vacía.
     */
    public Map<Biblioteca, List<MedioIF>> busquedaCruzadaMedios(Filtro f) {
        Map<Biblioteca, List<MedioIF>> ret = new HashMap<>();
        
        for(Biblioteca b: this.bibliotecas) {
            ret.put(
                b, 
                b.getCatalogo().buscarEn(f)
            );
        }
                
        return ret;  
    }
}
