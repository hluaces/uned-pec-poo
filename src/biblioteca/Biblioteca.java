package biblioteca;

import ficheros.ConvertidorCsvMedios;
import ficheros.ExportadorCSV;
import ficheros.ImportadorCSV;
import java.io.FileNotFoundException;
import java.nio.file.FileSystemException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import medios.AtributoMedio;
import medios.EnumTiposAtributo;
import prestamos.EnumEstadosPrestamo;
import medios.MedioIF;
import multas.Multa;
import prestamos.Prestamo;
import prestamos.Reserva;
import usuarios.Usuario;

/**
 * Clase que representa una biblioteca. 
 * 
 * La biblioteca es una entidad que engloba un catálogo de 
 * {@link MedioIF medios}, un colectivo de {@link Usuario usuarios} y todas
 * sus relaciones, como son:
 * 
 * - {@link Mensaje Mensajes}
 * - {@link Prestamo Préstamos}
 * - {@link Reserva Reservas}
 * - {@link Multa Multas}
 * 
 * @author Héctor Luaces Novo
 */
public class Biblioteca {
    /**
     * El máximo de préstamos activos que puede tener un usuario.
     */
    public static final int MAXIMO_PRESTAMOS = 6;
    
    /**
     * El máximo de días que se puede prestar un medio de la biblioteca. 
     */
    public static final int DIAS_PRESTAMO    = 14;
    
    /**
     * El {@link Catalogo} de la biblioteca
     */
    private final Catalogo catalogo;
    
    /**
     * El conjunto de usuarios válidos de la biblioteca.
     * 
     * Usamos un HashSet porque no tiene sentido tener usuarios duplicados. 
     * 
     * Un usuario es igual a otro si éste tiene el mismo login que otro, como
     * define {@link Usuario#equals(java.lang.Object) la clase Usuario}
     */
    private final Set<Usuario> usuarios;
    
    /**
     * El nombre de la biblioteca, pej: "biblioteca pondal de Lugo"
     */
    private final String nombre;

    /**
     * El map de {@link Prestamo préstamos} de la biblioteca.
     * 
     * Está catalogado en un map por usuario.
     * 
     * Un usuario sin préstamos tendrá una entrada 'null'.
     */
    private final Map<Usuario, Set<Prestamo>> prestamos;
    
    /**
     * {@link Mensaje Mensajes} destinados a los usuarios, clasificados en una 
     * lista agrupada en un map bajo cada usuario.
     * 
     * Un usuario sin mensajes tendrá una entrad a 'null' en su lista de 
     * mensajes.
     */
    private final Map<Usuario, List<Mensaje>> mensajes;
    
    /** 
     * {@link Multa Multas} de los usuarios agrupados en un Map.
     * 
     * Un usuario sin multas tendrá una entrada 'null'
     */
    private final Map<Usuario, Set<Multa>> multas;
    
    /**
     * Las {@link Reserva reservas} de los usuarios.
     * 
     * Un usuario sin reservas tendrá una entrada 'null' en el Map.
     */
    private final Map<Usuario, Set<Reserva>> reservas;
    
    /**
     * Constructor principal de la biblioteca, que asigna un nombre a 
     * la misma.
     * 
     * Inicializa todas las colecciones para que éstas estén en disposición
     * de trabajar.
     * 
     * @param nombre Nombre a establecer en la biblioteca
     */
    public Biblioteca(String nombre) {
        this.nombre = nombre;
        
        this.usuarios  = new HashSet<>();
        this.catalogo  = new Catalogo();
        this.prestamos = new HashMap<>();
        this.mensajes  = new HashMap<>();
        this.multas    = new HashMap<>();
        this.reservas  = new HashMap<>();
    }
    
    /**
     * Devuelve el nombre de la biblioteca.
     * 
     * @return El nombre de la biblioteca.
     */
    public String getNombre() {
        return nombre;
    }


    /**
     * Devuelve el {@link Catalogo} completo de la biblioteca.
     * 
     * @return El catálogo de medios de ésta biblioteca
     */
    public Catalogo getCatalogo() {
        return this.catalogo;
    }
    
    /**
     * Añade un usuario a la lista de usuarios válidos en ésta biblioteca.
     * 
     * @param s Nuevo usuario de la biblioteca
     * 
     * @return true si la operación tiene éxito.
     * @throws IllegalArgumentException Si se produce algún error al añadir el 
     * usuario.
     */
    public boolean addUsuario(Usuario s) {
        if ( s == null )
            throw new IllegalArgumentException(
                "Usuario inválido."
            );
        
        if ( this.usuarios.contains(s) )
            throw new IllegalArgumentException(
                "El usuario " + s.getLogin() + " ya existe."
            );
        
        return this.usuarios.add(s);
    }

    /**
     * Borra un usuario de la biblioteca, eliminando todos sus préstamos, 
     * multas, mensajes y reservas.
     * 
     * El usuario no podrá borrarse si tiene préstamos activos.
     * 
     * @param u Usuario a borrar
     * @return True si tiene éxito, false de cualquier otra forma
     * @throws IllegalArgumentException si es imposible borrar el usuario
     */
    public boolean borrarUsuario(Usuario u) {
        Set<Prestamo> p;
        
        if ( u == null )
            throw new IllegalArgumentException("Usuario no válido.");
        
        if ( ! this.usuarios.contains(u) )
            throw new IllegalArgumentException(
                "El usuario no existe en la biblioteca."
            );
        
        p = this.getPrestamosActivosUsuario(u);
        
        if ( p != null && ! p.isEmpty() )
            throw new IllegalArgumentException(
                "El usuario " + u.getLogin() + " tiene préstamos activos " +
                    "y no podrá ser borrado hasta que los devuelva."
            );  
        
        this.multas.remove(u);
        this.usuarios.remove(u);
        this.mensajes.remove(u);
        this.prestamos.remove(u);        
        return true;
    }
    
    /**    
     * Dado un nombre de usuario (o login) devuelve un objeto Usuario si hay
     * alguno en la biblioteca que coincida con dicho nombre. 
     * 
     * @param login Nombre de usuario a buscar
     * @return Objeto usuario que corresponde a ese login (o null, si no existe
     * ningún usuario en ésta biblioteca con ese login)
     */
    public Usuario getUsuario(String login) {
        Optional<Usuario> o = this.usuarios.stream()
            .filter(x -> x.getLogin().equals(login))
            .findFirst()
        ;
        
        return o.isPresent() ? o.get() : null;
    }
    
    /** 
     * Devuelve un set con todos los usuarios de la biblioteca.
     * 
     * @return Set con todos los usuarios de la biblioteca
     */
    public Set<Usuario> getUsuarios() {
        return this.usuarios;
    }
    
    /**
     * Dado un nombre usuario (login) y una contraseña, busca en la lista
     * de usuarios de la biblioteca alguno que coindida con los datos 
     * facilitados.
     * 
     * Si existe, devuelve su instancia. Si no existe, devolverá null.
     * 
     * @param login Nombre de usuario a buscar en la biblioteca
     * @param password Contraseña del usuario a buscar
     * @return Objeto usuario del usuario que se ha intentado identificar o 
     * 'null' si no hay ningún usuario que coincida con esa combinación de 
     * credenciales
     */
    public Usuario loginUsuario(String login, String password) {
        Usuario u = getUsuario(login);
        
        if ( u == null )
            return u;

        if ( ! u.comprobarPassword(password) )
            return null;

        return u;
    }
    
    /**
     * Devuelve un set con todas las reservas de la herramienta.
     * 
     * @return Un set con las reservas de la aplicación. Si no hay reservas
     * devolverá un set vacío.
     */
    public Set<Reserva> getReservas() {
        Set<Reserva> ret = new HashSet();
        
        this.reservas.values().forEach((c) -> ret.addAll(c));
        return ret;
    }
    
    /**
     * Devuelve las reservas existentes para un usuario.
     * 
     * Si no hay reservas se devolverá null.
     * 
     * @param u Usuario para el que queremos conocer las reservas.
     * @return Set con las reservas del usuario o 'null' si no tiene ninguna.
     */
    public Set<Reserva> getReservasUsuario(Usuario u) {
        if ( ! this.reservas.keySet().contains(u) )
            return null;
        
        return this.reservas.get(u);
    }
    
    /**
     * Añade una nueva reserva al sistema, realizando comprobaciones de 
     * integridad y enviando un mensaje al usuario que posea el medio
     * actualmente.
     * 
     * @param r Reserva a añadir
     * @return true si la operación tiene éxito
     * @throws IllegalArgumentException Si ocurre algún error
     */
    public boolean addReserva(Reserva r) {
        Prestamo p;
        
        if ( r == null )
            throw new IllegalArgumentException("Reserva no válida.");
        
        if ( ! this.reservas.keySet().contains(r.getUsuario()) )
            this.reservas.put(r.getUsuario(), new HashSet<>());
        
        if ( this.reservas.get(r.getUsuario()).contains(r) )
            throw new IllegalArgumentException(
                "El usuario ya tiene esa reserva."
            );
        
        if ( r.getMedio().getEstado() == EnumEstadosPrestamo.DISPONIBLE )
            throw new IllegalArgumentException(
                "No se pueden crear reservas para medios que están " + 
                    "actualmente disponibles."
            );
        
        if ( this.tieneMedioPrestado(r.getUsuario(), r.getMedio()) )
            throw new IllegalArgumentException(
                "El medio solicitado ya ha sido prestado al usuario "+
                    r.getUsuario().getLogin() + "."
            );
        
        this.reservas.get(r.getUsuario()).add(r);
        p = this.getPrestamoMedio(r.getMedio(), EnumEstadosPrestamo.PRESTADO);
        
        if ( p != null )
            this.addMensajeUsuario(
                p.getUsuario(), 
                "Otro usuario ha reservado el medio '"
                    + p.getMedio().getTitulo() + "', por lo que le rogamos lo "
                    + "devuelva tan pronto le sea posible."  
            );
        
        return true;
    }
    
    /**
     * Sobrecarga de {@link Biblioteca#addReserva(prestamos.Reserva)} para 
     * permitir un mayor desacoplamiento al no necesitar crear una reserva
     * desde el exterior.
     * 
     * @param m Medio a solicitar en la reserva.
     * @param u Usuario que hace la reserva.
     * @return true si tiene éxito.
     * @trows IllegalArgumentException Si ocurre algún error
     */
    public boolean addReserva(MedioIF m, Usuario u) {
        return this.addReserva(new Reserva(u,m));
    }
    
    /**
     * Borra una reserva existente de la biblioteca.
     * 
     * @param r Reserva que queremos borrar.
     * @return True si la operación tiene éxito.
     * @throws IllegalArgumentException Si ocurre cualquier error en el proceso.
     */
    public boolean borrarReserva(Reserva r) {
        if ( r == null )
            throw new IllegalArgumentException("Reserva no válida.");
        
        if ( ! this.reservas.keySet().contains(r.getUsuario()) )
            throw new IllegalArgumentException(
                "Imposible cargar la reserva."
            );
        
        if ( ! this.reservas.get(r.getUsuario()).contains(r) )
            throw new IllegalArgumentException(
                "Imposible encontrar la reserva."
            );
        
        this.reservas.get(r.getUsuario()).remove(r);
        return true;
    }
    
    /**
     * Devuelve todas las reservas de la biblioteca que existen para un medio
     * dado.
     * 
     * @param m Medio para el que queremos consultar la reserva.
     * @return Set con las reservas. Set vacío si no hay ningún resultado.
     */
    public Set<Reserva> getReservasMedio(MedioIF m) {
        Set<Reserva> ret = new HashSet<>();
        
        if ( m == null )
            return ret;
        
        for(Entry<Usuario, Set<Reserva>> entry: this.reservas.entrySet()) {
            for(Reserva r: entry.getValue()) {
                if ( r.getMedio() != m )
                    continue;
                
                ret.add(r);
            }
        }
        
        return ret;
    }
    
    /**
     * Devuelve un conjunto con todos los préstamos de la biblioteca.
     * 
     * @return Set con todos los préstamos de la biblioteca.
     */    
    public Set<Prestamo> getPrestamos() {
        Set<Prestamo> ret = new HashSet<>();

        this.prestamos
            .values()
            .stream()
            .forEach((c) -> ret.addAll(c))
        ;
        
        return ret;
    }
    
    /**
     * Devuelve un conjunto con todos los préstamos de un usuario dado
     * 
     * @param u Usuario del que queremos conocer los préstamos
     * @return Conjunto con los préstamos del usuario o null si no tiene préstamos.
     */
    public Set<Prestamo> getPrestamosUsuario(Usuario u) {
        return this.prestamos.get(u);
    }
    
    /**
     * Determina si un usuario tiene un préstamo para un medio pasado como 
     * parámetro.
     * 
     * @param u Usuario para el que queremos comprobar el préstamo.
     * @param m Medio que queremos verificar si está prestado al usuario.
     * @return True si está prestado, falso si no
     */
    public boolean tieneMedioPrestado(Usuario u, MedioIF m) {
        Set<Prestamo> pr = this.getPrestamosUsuario(u);
        
        if ( pr == null || pr.isEmpty() )
            return false;
        
        return pr.stream()
            .filter((c) -> c.getMedio().equals(m) && ! c.isDevuelto() )
            .findFirst()
            .orElse(null) != null
        ;        
    }
    
    /**
     * Busca én los préstamos de la biblioteca uno que haga referencia un 
     * medio especificado y que esté en un estado también especificado
     * 
     * @param m Medio para el que queremos saber si existe un préstamo.
     * @param estado Estado en el que debe estar el préstamo 
     * @return Prestamo que cumple lo exigido o null, si no existe ninguno
     */
    public Prestamo getPrestamoMedio(MedioIF m, EnumEstadosPrestamo estado) {
        for(Set<Prestamo> st: this.prestamos.values()) {
            for(Prestamo p: st) {
                if ( p.getMedio() != m)
                    continue;

                if ( p.getMedio().getEstado() != estado )
                    continue;

                return p;
            }
        }

        return null;
    }
    
    /**
     * Devuelve un conjunto con todos los préstamos activos de un usuario
     * 
     * @param u Usuario para el que queremos conocer los préstamos.
     * @return Conjunto con los préstamos activos. Null si no tiene préstamos.
     */
    public Set<Prestamo> getPrestamosActivosUsuario(Usuario u) {
        Set<Prestamo> ret = this.getPrestamosUsuario(u);
        
        if ( ret == null )
            return null;
        
        return ret.stream()
            .filter((c) -> ! c.isDevuelto() )
            .collect(Collectors.toSet())
        ;
    }
    
    /**
     * Pide a la biblioteca el préstamo de un medio para un usuario dado.
     * 
     * Si el usuario tiene una reserva para dicho medio, la reserva se anulará.
     * 
     * @param u Usuario que solicita el préstamo
     * @param m Medio que solicita el usuario
     * @param dias Nº de días para el que se solicita el préstamo
     * @return El objeto préstamo si tiene éxito. Null en otro caso.
     * @throws IllegalArgumentException Se lanza si ocurre algún error, como
     * que la biblioteca no tenga el medio que se pide, que éste no esté 
     * disponible o que el usuario haya llegado al máximo de préstamos
     */
    public Prestamo pedirPrestamo(Usuario u, MedioIF m, int dias)  {
        Set<Prestamo> ret;
        Prestamo p;
        
        if ( u  == null || m == null )
            return null;
        
        if ( dias < 1 || dias > DIAS_PRESTAMO )
            throw new IllegalArgumentException("Número de días de préstamo inválido.");
        
        if ( ! this.catalogo.getMedios().contains(m) )
            throw new IllegalArgumentException("La biblioteca no posee ese medio.");
        
        if ( m.getEstado() != EnumEstadosPrestamo.DISPONIBLE )
            throw new IllegalArgumentException(
                "Ese medio no está disponible para préstamo."
            );
        
        ret = this.getPrestamosActivosUsuario(u);
        
        if ( ret != null && ret.size() > MAXIMO_PRESTAMOS - 1 )
            throw new IllegalArgumentException(
                "No se pueden pedir más de " + MAXIMO_PRESTAMOS + " préstamos."
            );
        
        if ( ret == null )
            this.prestamos.put(u, new HashSet<>());
        
        p = new Prestamo(dias, u, m);
        this.prestamos.get(u).add(p);
        m.addAtributo(new AtributoMedio(
            EnumTiposAtributo.ESTADO, EnumEstadosPrestamo.PRESTADO)
        );
        
        if ( ! this.reservas.keySet().contains(u) )
            return p;
        
        for(Iterator<Reserva> it = this.reservas.get(u).iterator(); it.hasNext(); ) {
            Reserva r = it.next();
            
            if ( r.getMedio() != m )
                continue;
            
            it.remove();
        }
        
        return p;
    }
    
    /**
     * Intenta devolver un medio que previamente ha sido prestado a un usuario.
     * 
     * Además, informará a todos aquellos usuarios que tengan una reserva
     * para el medio del préstamo de que ahora está disponible.
     * 
     * @param p El préstamo a devolver
     * @return True si se devuelve con éxito, false en cualquier otro caso.
     */
    public boolean devolverPrestamo(Prestamo p) {
        Set<Reserva> res;
        
        if ( p == null )
            throw new IllegalArgumentException("Préstamo no válido.");
        
        if ( this.prestamos.get(p.getUsuario()) == null )
            throw new IllegalArgumentException(
                "Imposible cargar el usuario del préstamo."
            );
        
        if ( ! this.prestamos.get(p.getUsuario()).contains(p) )
            throw new IllegalArgumentException(
                "Imposible encontrar el préstamo del usuario."
            );
        
        if ( p.isDevuelto() )
            throw new IllegalArgumentException(
                "El préstamo ya está devuelto."
            );
        
        p.devolver();
        p.getMedio().addAtributo(new AtributoMedio(
            EnumTiposAtributo.ESTADO, EnumEstadosPrestamo.DISPONIBLE)
        );
        
        res = this.getReservasMedio(p.getMedio());
        
        if ( res.isEmpty())
            return true;
        
        for(Reserva r: res) {
            this.addMensajeUsuario(
                r.getUsuario(), 
                "Una de sus reservas está ahora disponible: "
                    + "'" + r.getMedio().getTitulo() + "'."
            );
        }
        
        return true;
    }
    
    /**
     * Devuelve las multas de un usuario
     * 
     * @param u Usuario para el que queremos las multas.
     * @return Un set con las multas (o null, si no tiene ninguna)
     */
    public Set<Multa> getMultasUsuario(Usuario u) {
        return this.multas.get(u);
    }
    
    /**
     * Dada una multa vigente, la anula.
     * 
     * @param m Multa a anular
     * @return True si se anula con éxito, false si no se consigue
     */
    public boolean anularMulta(Multa m) {
        if ( m == null )
            throw new IllegalArgumentException();
        
        if ( ! m.isVigente() )
            throw new IllegalArgumentException("Esa multa no está vigente");
        
        if ( this.multas.get(m.getPrestamo().getUsuario()) == null )
            throw new IllegalArgumentException("Imposible cargar esa multa.");
        
        if ( ! this.multas.get(m.getPrestamo().getUsuario()).contains(m) )
            throw new IllegalArgumentException("Imposible cargar esa multa.");
        
        m.pagar();
        this.addMensajeUsuario(
            m.getPrestamo().getUsuario(), 
            "Se ha anulado la multa que tenías sobre el medio '"
                + m.getPrestamo().getMedio().getTitulo() + "'."
        );
        return true;
    }
    
    /**
     * Crea una multa para un usuario a raíz de un préstamo.
     * 
     * Si tiene éxito, se le envia un mensaje al usuario para informarle
     * de la multa.
     * 
     * @param p Prestamo que origina la situación.
     * @return true si se añade con éxito, false de cualquier otra forma.
     */
    public boolean multarUsuario(Prestamo p) {
        if ( this.multas.get(p.getUsuario()) == null )
            this.multas.put(p.getUsuario(), new HashSet<>());
        
        if ( ! this.multas.get(p.getUsuario()).add(new Multa(p)) )
            return false;
        
        this.addMensajeUsuario(p.getUsuario(), 
            "Has sido multado debido a tu tardanza al devolver '" 
                + p.getMedio().getTitulo() + "'"
        );
        p.setAvisado(true);
        return true;
    }
    
    /**
     * Devuelve los mensajes de un usuario (si es que tiene alguno).
     * 
     * Si no hay ningún mensaje para el usuario devolverá 'null'.
     * 
     * @param u El usuario para el que queremos consultar los mensajes.
     * @return Lista de los mensajes del usuario o 'null'
     */
    public List<Mensaje> getMensajesUsuario(Usuario u) {
        return this.mensajes.get(u);
    }
    
    /**
     * Marca un mensaje como leído.
     * 
     * @param m Mensaje que queremos marcar como leído.
     * @return True si se lee con éxito, falso si no es así.
     * @throws IllegalArgumentException Si ocurre algúne rror
     */
    public boolean leerMensaje(Mensaje m) {
        if ( m == null )
            throw new IllegalArgumentException("Mensaje no válido.");
        
        if ( m.isLeido() )
            throw new IllegalArgumentException("Ese mensaje ya está leído.");
        
        if ( this.mensajes.get(m.getDestinatario()) == null )
            throw new IllegalArgumentException("Imposible recipiente del mensaje.");
        
        if ( ! this.mensajes.get(m.getDestinatario()).contains(m) )
            throw new IllegalArgumentException("Imposibel cargar mensaje");
        
        m.setLeido();
        return true;
    }
    
    /**
     * Devuelve el número de mensajes de leer de un usuario.
     * 
     * @param u El usuario para el que queremos contar los mensajes.
     * @return Número de mensajes sin leer.
     */
    public int getTotalMensajesSinLeer(Usuario u) {
        List<Mensaje> m = this.getMensajesUsuario(u);
        
        if ( u == null || m == null )
            return 0;
       
        return (int) m.stream().filter((c) -> ! c.isLeido()).count();
    }
    
    /**
     * Borra un mensaje del usuario.
     * 
     * @param u Usuario del que queremos borrar el mensaje
     * @param msj Mensaje a borrar
     * @return True si la operación tiene éxito
     * @throws IllegalArgumentException Si ocurre algún error
     */
    public boolean borrarMensajeUsuario(Usuario u, Mensaje msj) {
        if ( u == null )
            throw new IllegalArgumentException("Imposible datos de usuario.");
        
        if ( msj == null )
            throw new IllegalArgumentException("Mensaje inválido.");
        
        if ( ! this.mensajes.keySet().contains(u) )
            throw new IllegalArgumentException("Imposible cargar usuario.");
        
        if ( ! this.mensajes.get(u).contains(msj) )
            throw new IllegalArgumentException("Imposible cargar mensaje.");
        
        return this.mensajes.get(u).remove(msj);
    }
    
    /**
     * Crea un nuevo mensaje para el usuario a partir de una cadena de texto.
     * 
     * @param u Usuario para quien queremos añadir el mensaje.
     * @param msj Mensaje que queremos añadir al usuario.
     * @return True si la operación tiene éxito.
     */
    public boolean addMensajeUsuario(Usuario u, String msj) {
        if ( ! this.mensajes.keySet().contains(u) )
            this.mensajes.put(u, new ArrayList<>());
        
        return this.mensajes.get(u).add(new Mensaje(msj, u));
    }
    
    /**
     * Marca un mensaje de un usuario como 'leído'.
     * 
     * @param u Usuario para el que queremos marcar el mensaje.
     * @param msj Mensaje que queremos marcar como leído.
     * @return True si la operación tiene éxito, falso de cualquier otra forma.
     */
    public boolean leerMensaje(Usuario u, Mensaje msj) {
        if ( ! this.mensajes.keySet().contains(u) )
            return false;
        
        if ( ! this.mensajes.get(u).contains(msj) )
            return false;
        
        if ( msj.isLeido() )
            return false;
        
        msj.setLeido();
        return true;
    }
    
    /**
     * Añade un nuevo medio al catálogo de la biblioteca.
     * 
     * Si el medio no tiene biblioteca origen, se establece esta propia
     * biblioteca como propietaria.
     * 
     * @param medio Medio a añadir.
     * @return true si la operación tiene éxito
     * @throws IllegalArgumentException Si ocurren errores lógicos al añadirlo.
     */
    public boolean addMedio(MedioIF medio) {
        if ( null == medio.getBibliotecaOrigen() )
            medio.setValorAtributo(EnumTiposAtributo.BIBLIOTECA, this.nombre);
        
        return this.catalogo.addMedio(medio);
    }
    
    /**
     * Devuelve una lista con los medios asociados al catálogo de esta 
     * biblioteca que son de un tipo determinado.
     * 
     * @param tipo Tipo de medio que queremos consultar.
     * @return Lista de los medios del tipo especificado, si no hay ninguno,
     * se devolverá una lista vacía.
     */
    public List<MedioIF> getMedios(String tipo) {
        return this.catalogo.getMedios(tipo);
    }

   /**
    * Devuelve una lista con todos los medios asociados al catálogo de ésta 
    * biblioteca.
    * 
    * @return Lista con todos los elementos, o lista vacía
    */
    public List<MedioIF> getMedios() {       
        return this.catalogo.getMedios();
    }
    
    /**
     * Devuelve todos los medios que nos han sido cedidos 
     * 
     * @return Lista (puede ser vacía) con los medios cedidos desde otras 
     * bibliotecas.
     */
    public List<MedioIF> getMediosCedidos() {
        return this.getMedios()
            .stream()
            .filter((c) -> ! c.getBibliotecaOrigen().equals(this.nombre))
            .collect(Collectors.toList())
        ;    
    }

    /**
     * Devuelve todos los medios que hemos cedido a otras bibliotecas.
     * 
     * @return Lista (puede ser vacía) de los medios que hemos cedido
     * a otras bibliotecas.
     */
    public List<MedioIF> getMediosCedidosAOtras() {
        return this.getMedios()
            .stream()
            .filter((c) -> c.getBibliotecaOrigen().equals(this.nombre))
            .filter((c) -> c.getEstado() == EnumEstadosPrestamo.PRESTADO_BIBLIOTECA)
            .collect(Collectors.toList())
        ;
    }
    
    /**
     * Dado un medio importado desde un archivo .csv, este método intenta 
     * encontrar una referencia al mismo entre la lista de medios cedidos
     * a otras bibliotecas.
     * 
     * De encontrarla, la devuelve. 
     * 
     * @param m El medio recién importado que queremos buscar en la lista de
     * medios cedidos a otras bibliotecas.
     * @return La referencia al medio buscado en nuestra biblioteca o 'null'
     * si no la encontramos.
     */
    public MedioIF encontrarMedioCedido(MedioIF m) {
        return this.getMediosCedidosAOtras()
            .stream()
            .filter((c) -> c.getTitulo().equals(m.getTitulo()))
            .filter((c) -> c.getTipo().equals(m.getTipo()))
            .filter((c) -> c.getAutor().equals(m.getAutor()))
            .filter((c) -> c.getGenero().equals(m.getGenero()))
            .findFirst()
            .orElse(null)
        ;
    }
    
    /**
     * Determina si un medio pasado como parámetro nos ha sido desde otra
     * biblioteca.
     * 
     * @param m Medio a comprobar si nos ha ha sido cedido.
     * @return True si el medio nos ha sido cedido.
     */
    public boolean isCedido(MedioIF m) {
        return this.getMediosCedidos().contains(m);
    }
    
    /**
     * Determina si hemos prestado a otra biblioteca un medio pasado como 
     * parámetro 
     * 
     * @param m Medio a comprobar si ha sido prestado.
     * @return true si el medio ha sido prestado a otra biblioteca.
     */
    public boolean isCedidoAOtra(MedioIF m) {
        return this.getMediosCedidosAOtras().contains(m);
    }
    
    /**
     * Intenta exportar un conjunto de medios de la biblioteca a un fichero
     * csv pasado como parámetro (el fichero será sobreescrito).
     * 
     * Los medios han de estar en la biblioteca y éstos han de estar 
     * disponibles.
     * 
     * Si tiene éxito, los medios se guardarán en el CSV y serán eliminados
     * de la biblioteca a todos los efectos.
     * 
     * @param fichero Ruta absoluta del fichero en el que queremos guardar
     * el .csv.
     * @param medios Lista de los medios a exportar.
     * @return true si la operación tiene éxito
     * @throws java.nio.file.FileSystemException Si ocurre algún error al 
     * escribir en el archivo.
     * @throws java.io.FileNotFoundException Si ocurre algún error al 
     * escribir en el archivo.
     * @throws IllegalArgumentException Si ocurre algún error lógico (medios no
     * válidos, por ejemplo)
     */
    public boolean exportarMedios(String fichero, List<MedioIF> medios) throws FileSystemException, FileNotFoundException {
        ExportadorCSV export;
        Set<String> cols = new HashSet<>();
        
        for(MedioIF m: medios) {
            if ( ! this.catalogo.hasMedio(m) )
                throw new IllegalArgumentException(
                    "El medio '" + m.getTitulo() + " no pertenece a esta "
                        + "biblioteca."
                );
            
            if ( m.getEstado() != EnumEstadosPrestamo.DISPONIBLE )
                throw new IllegalArgumentException(
                    "El medio '" + m.getTitulo() + " no está disponible y "
                        + "no puede ser exportado."
                );
            
            cols.addAll(m.getTiposAtributo()
                .stream()
                .map((c) -> c.getNombre())
                .collect(Collectors.toSet())
            );
        }
        
        export = new ExportadorCSV(
                fichero,
                new ConvertidorCsvMedios(),
                new ArrayList(cols)
        );
        
        if ( ! export.escribir(medios) )
            return false;
        
        for(MedioIF m :medios) {
            // Si exportamos un medio nuestro lo marcamos como cedido.
            if ( m.getBibliotecaOrigen().equals(this.nombre) )
                m.setValorAtributo(
                    EnumTiposAtributo.ESTADO, 
                    EnumEstadosPrestamo.PRESTADO_BIBLIOTECA
                );
            // Si quitamos un medio que NO es nuestro, lo borramos.
            else
                this.catalogo.removeMedio(m);
        }
        
                    
        return true;
    }
    
    /**
     * Dado un archivo CSV, intenta importar todos los medios que encuentre
     * en el mismo.
     * 
     * @param fichero Ruta absoluta del fichero a importar.
     * @return True si tiene éxito.
     */
    public boolean importarMedios(String fichero) throws FileNotFoundException, FileSystemException {
        ImportadorCSV i;
        List<Object> leidos;
        
        i      = new ImportadorCSV(fichero, new ConvertidorCsvMedios());
        leidos = i.leer();
 
        for(Object m: leidos) {
            MedioIF medio = (MedioIF) m;

            // Estamos importando algo que nos devuelven
            if ( medio.getBibliotecaOrigen().equals(this.nombre) ) {
                MedioIF medio_real = this.encontrarMedioCedido(medio);
                
                if ( medio_real != null ) {
                    medio_real.setValorAtributo(
                        EnumTiposAtributo.ESTADO, 
                        EnumEstadosPrestamo.DISPONIBLE
                    );
                }
            }
            // Nos estan trayendo un nuevo medio
            else {
                this.addMedio(medio);
            }
        }
        return true;
    }
            
            
}
