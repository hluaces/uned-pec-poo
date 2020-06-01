package pobladores;

import aplicacion.Aplicacion;
import biblioteca.Biblioteca;
import java.io.FileNotFoundException;
import java.nio.file.FileSystemException;
import java.util.Iterator;

/** 
 * Esta clase estática se encarga de crear las bibliotecas de ejemplo que se
 * usarán la aplicación para, posteriormente, usar el {@link PobladorBibliotecas}
 * para insertar en ellas datos de demostración que podrán usarse con el fin
 * de evaluar la práctica en sí.
 * 
 * @author Héctor Luaces novo
 */
public final class PobladorAplicacion {
    /**
     * Clase estática no instanciable ni heredable.
     * @see PobladorAplicacion#poblarAplicacion(biblioteca.Aplicacion) 
     */
    private PobladorAplicacion() {
    }
    
    
    /**
     * Dado un objeto de {@link Aplicacion}, crea las bibliotecas de ejemplo
     * y posteriormente usa un {@link PobladorBibliotecas} para insertar
     * datos de ejemplo en cada una.
     * 
     * @param a La aplicación que queremos poblar
     * @throws FileNotFoundException Propagado desde 
     * {@link PobladorBibliotecas#poblarBiblioteca(biblioteca.Biblioteca)}
     * @throws FileSystemException Propagado desde 
     * {@link PobladorBibliotecas#poblarBiblioteca(biblioteca.Biblioteca)}
     */
    public static void poblarAplicacion(Aplicacion a) throws FileNotFoundException, FileSystemException {
        // Creamos las bibliotecas de la aplicación
        a.anyadirBiblioteca(new Biblioteca("Libros_y_periodicos"));
        a.anyadirBiblioteca(new Biblioteca("Musica_y_videos"));
        a.anyadirBiblioteca(new Biblioteca("Todo_junto"));

        // Añadimos contenido (usuarios + documentación)
        for(Iterator<Biblioteca> i = a.getBibliotecas().iterator(); i.hasNext();) {
            PobladorBibliotecas.poblarBiblioteca(i.next());
        }
    }
}
