package pobladores;

import biblioteca.Biblioteca;
import ficheros.ConvertidorCsvMedios;
import ficheros.ImportadorCSV;
import java.io.FileNotFoundException;
import java.nio.file.FileSystemException;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import usuarios.EnumPerfiles;
import usuarios.UsuariosFactory;
import medios.MedioIF;
import misc.DateUtil;
import prestamos.Prestamo;
import prestamos.Reserva;
import usuarios.Usuario;

/**
 * Clase usada por el poblador de la aplicación.
 * 
 * Dada una biblioteca, se encarga de inicializarla con datos de ejemplo
 * para que la aplicación pueda ser probada.
 * 
 * La mayoría de datos de ejemplo (a excepción de los medios) se generan
 * de forma aleatoria.
 * 
 * @author Hëctor Luaces Novo
 */
final class PobladorBibliotecas {
    /**
     * El convertidor que se usará en los procesos de importación.
     * 
     * Lo almacenamos como estático para no tener que instanciarlo en 
     * cada importación.
     */
    private static ConvertidorCsvMedios convertidor;
    
    /**
     * Clase estática no instanciable.
     */
    private PobladorBibliotecas() {
    }
    
    /**
     * Crea una instancia de {@link Usuario} con un login y contraseña igual al
     * dato pasado como primer parámetro.
     * 
     * Se le establecerá el perfil {@link EnumPerfiles#USUARIO}.
     * 
     * Solo usado para crear usuarios de prueba, puesto que es muy inseguro
     * crear usuarios así.
     * @param tx Login / Password usado para el nuevo usuario.
     * @return Nuevo usuario instanciado.
     */
    private static Usuario crearUsuario(String tx) {
        return UsuariosFactory.crearUsuario(tx, tx, EnumPerfiles.USUARIO);
    }
    
    /**
     * Dada una biblioteca, genera unos cuantos usuarios de varios perfiles en 
     * la misma que, posteriormente, serán usados para generar préstamos, etc.
     * 
     * @param b La biblioteca en la que queremos añadir los distintos objetos.
     */
    private static void poblarUsuarios(Biblioteca b) {
        Usuario u;
        
        // Vamos a tener un usuario bibliotecario
        // en todas las bibliotecas (son usuarios distintos, a pesar de
        // compartir el mismo login, ya que cada biblioteca tiene su propio
        // pool de usuarios)
        b.addUsuario(UsuariosFactory.crearUsuario(
            "admin", "admin", EnumPerfiles.BIBLIOTECARIO
        ));
        
        // En función de las distintas bibliotecas crearemos distintos usuarios
        switch(b.getNombre()) {
            case "Musica_y_videos":
                // RIP
                u = crearUsuario("ronnie");
                u.setNombre("Ronnie");
                u.setApellidos("James Dio");
                u.setDni("35639005E");
                b.addUsuario(u);
                
                u = crearUsuario("satyr");
                u.setNombre("Sigurd");
                u.setApellidos("Wongraven");
                u.setDni("15150190K");
                b.addUsuario(u);
                
                u = crearUsuario("ozzy");
                u.setNombre("John Michael");
                u.setApellidos("Osbourne");
                u.setDni("80362657J");
                b.addUsuario(u);
                break;
                
            case "Libros_y_periodicos":
                u = crearUsuario("john");
                u.setNombre("John");
                u.setApellidos("Doe");
                u.setDni("86593355B");
                b.addUsuario(u);
                
                u = crearUsuario("maria");
                u.setNombre("María");
                u.setApellidos("Martínez González");
                u.setDni("25033252Y");
                b.addUsuario(u);
                
                u = crearUsuario("alfonso");
                u.setNombre("Alfonso");
                u.setApellidos("Sideral de la Fuente");
                u.setDni("95603800E");
                b.addUsuario(u);
                break;
                
            case "Todo_junto":
                u = crearUsuario("antonio");
                u.setNombre("Antonio");
                u.setApellidos("Fernández Teruel");
                u.setDni("81165403J");
                b.addUsuario(u);
                
                u = crearUsuario("isabel");
                u.setNombre("Isabel");
                u.setApellidos("Clara de la Luz");
                u.setDni("02656160M");
                b.addUsuario(u);
                
                u = crearUsuario("mariano");
                u.setNombre("Mariano");
                u.setApellidos("Martinez González");
                u.setDni("64947871B");
                b.addUsuario(u);
                break;
        }
    }
    
    private static Usuario getUsuarioAleatorio(Set<Usuario> usuarios, Usuario excluido) {
        Usuario []ret;
        Random r;
        Usuario u;
        
        if ( usuarios.size() == 1 )
            return null;
        
        ret = usuarios.toArray(new Usuario[usuarios.size()]);
        r   = new Random();
        
        do {
            u = ret[r.nextInt(ret.length)];
            
        } while (u == excluido);
        
        return u;
    }

    private static void intentarCrearReserva(
        Biblioteca b, MedioIF m, Set<Usuario> usuarios, Usuario u
    ) 
    {
        Random r = new Random();
        
        if ( r.nextInt() == 0) 
            return;
        
        Usuario usr = getUsuarioAleatorio(usuarios, u);      
        System.out.println(
            "Creada una reserva de " + usr.getLogin() + " para " + m.getTitulo() + "."
        );
        b.addReserva(new Reserva(usr, m));
    }
    
    private static void poblarPrestamos(Biblioteca b) 
    {
        Set<Usuario> usuarios;
        List<MedioIF> medios;
        Random rand;
        
        rand     = new Random();
        medios   = b.getMedios();
        usuarios = b.getUsuarios()
                .stream()
                .filter((c) -> c.getPerfil().getNombre().equals(EnumPerfiles.USUARIO.getNombrePerfil()))
                .collect(Collectors.toSet())
        ;
        
        System.out.println("------------------------------");
        System.out.println("Generando préstamos para biblioteca " + b.getNombre() + ".");
        System.out.println("------------------------------");
        
        // Primeros creamos un historial de préstamos para cada usuario de forma
        // aleatoria
        for(Usuario u: usuarios) {
            int prestamos = 0;
            
            // Cada usuario tendrá 5-24 préstamos
            for(int i = 0; i < rand.nextInt(20) + 5; i++) {
                // Buscamos algo a pedir prestado...
                MedioIF m = medios.get(rand.nextInt(medios.size()));
                Prestamo p;
                
                try {
                    // Pedimos el préstamo
                    p = b.pedirPrestamo(u, m, Biblioteca.DIAS_PRESTAMO);
                    
                    // Lo devolvemos (se establece ésta fecha como devolución)
                    b.devolverPrestamo(p);
                    
                    // Ponemos una fecha dentro del rango permitido
                    p.setFechaInicio(DateUtil.addDias(
                        new Date(),
                        -1-rand.nextInt(90)
                    ));
                    p.setFechaDevolucion(DateUtil.addDias(
                        p.getFechaInicio(),
                        1+rand.nextInt(b.DIAS_PRESTAMO-1)
                    ));
                    prestamos++;
                }
                catch (Exception e) {
                    continue;
                }
            }
            
            System.out.println(
                "Generados " + prestamos + " préstamos para " + u.getLogin() + "."
            );
        }
        
        // Ahora generamos unos cuantos préstamos 'activos', es decir,
        // que aún no se han devuelto
        for(Usuario u: usuarios) {
            int prestamos = 0;
            
            for(int i = 0; i < rand.nextInt(b.MAXIMO_PRESTAMOS); i++) {
                // Buscamos algo a pedir prestado...
                MedioIF m = medios.get(rand.nextInt(medios.size()));
                
                try {
                    // Pedimos el préstamo
                    b.pedirPrestamo(u, m, Biblioteca.DIAS_PRESTAMO);
                    
                    // Como lo dejamos abierto, eliminamos el medio de la lista
                    // para que no lo intente pedir prestado otro usuario
                    // del poblador
                    medios.remove(m);
                    prestamos++;
                    
                    intentarCrearReserva(b, m, usuarios, u);

                }
                catch(Exception p) {
                    continue;
                }
                
            }
            
            System.out.println(
                "Generados " + prestamos + " activos para " + u.getLogin() + "."
            );
        }
        
        // Ahora intentamos generar unos cuantos préstamos 'caducados', es
        // decir, que aún no se han devuelto y HAN PASADO su fecha de devolución
        for(Usuario u: usuarios) {
            int prestamos = 0;
            
            for(int i = 0; i < rand.nextInt(3); i++) {
                MedioIF m = medios.get(rand.nextInt(medios.size()));
                Prestamo p;
                
                // Este no puede hacer más préstamos...
                if (b.getPrestamosActivosUsuario(u).size() >= b.MAXIMO_PRESTAMOS )
                    break;
            
                try {
                    // Pedimos préstamo...
                    p = b.pedirPrestamo(u, m, Biblioteca.MAXIMO_PRESTAMOS);
                    
                    // Establecemos la fecha de inicio en el pasado
                    p.setFechaInicio(
                        DateUtil.addDias(
                            new Date(), 
                            - b.MAXIMO_PRESTAMOS *2 - rand.nextInt(10)
                        )
                    );
                    p.setFechaVencimiento(
                        DateUtil.addDias(
                            new Date(), 
                            - b.MAXIMO_PRESTAMOS  - rand.nextInt(10)
                        )
                    );
                    prestamos++;
                    
                    intentarCrearReserva(b, m, usuarios, u);
                }
                catch(Exception e) {
                    continue;
                }
            }
            
            System.out.println(
                "Generados " + prestamos + " caducados para " + u.getLogin() + "."
            );
        }
    }
    /**
     * Dad una biblioteca busca un fichero ".csv" almacenado en el directorio
     * del paquete de ésta misma clase que sea igual a su nombre.
     * 
     * De existir, intentará importar los medios de esos ficheros dentro
     * del catálogo de la biblioteca pasada como parámetro.
     * 
     * @param b La biblioteca en la que queremos importar el contenido.
     * @throws FileNotFoundException Si el fichero .csv de la biblioteca no 
     * existe.
     * @throws FileSystemException Si ocurre algún error al intentar leer el 
     * fichero.
     */
    private static void poblarMedios(Biblioteca b) throws FileNotFoundException, FileSystemException {
        ImportadorCSV im;
        
        // Creamos un importador para el fichero .csv correspondiente
        im = new ImportadorCSV(
            //PobladorBibliotecas.class.getResource(
                    "pobladores/" + b.getNombre() + ".csv",
            //).getFile(),
            convertidor
        );        
        
        // Añadimos todos los medios creados al catálogo de la biblioteca
        im.leer().forEach((c) -> b.addMedio((MedioIF) c));
    }
    
    /**
     * Inicializa el {@link ConvertidorCsvMedios} usado internamente por la 
     * clase.
     */
    private static void crearConvertidor() {
        if ( convertidor != null )
            return;
        
        convertidor = new ConvertidorCsvMedios();
    }
    
    /**
     * Dada una biblioteca le añadirá datos de ejemplo, que podrán o no, constar
     * de:
     * 
     * - Usuarios
     * - Medios
     * - Préstamos (vigentes, cerrados y caducados)
     * - Multas
     * 
     * @param b Biblioteca a la que añadir los datos de ejemplo.
     * 
     * @throws FileNotFoundException Propagado desde 
     * {@link PobladorBibliotecas#poblarBiblioteca(biblioteca.Biblioteca))
     * @throws FileSystemException Propagado desde 
     * {@link PobladorBibliotecas#poblarBiblioteca(biblioteca.Biblioteca) 
     */
    public static void poblarBiblioteca(Biblioteca b) throws FileNotFoundException, FileSystemException {
        // Inicializamos el convertidor, que será reusado posteriormente
        crearConvertidor();
        
        // Creamos usuarios (que están hardcoded en este fichero)
        poblarUsuarios(b);
        
        // Creamos medios, importados a partir de los ficheros en este mismo
        // paquete
        poblarMedios(b);
        
        // Poblamos la biblioteca con préstamos generados de forma aleatoria
        // en base al pool de usuarios/medios de la biblioteca
        poblarPrestamos(b);
    }
}
