package aplicacion;

import iu.ControladorGraficoIF;
import java.io.FileNotFoundException;
import java.nio.file.FileSystemException;
import pobladores.PobladorAplicacion;
import iu.IUFactory;
import java.lang.Thread.UncaughtExceptionHandler;

/**
 * La clase principal de la herramienta.
 * 
 * Instanciará una {@link Aplicacion}, un controlador de errores, creará
 * datos de ejemplo para la aplicación y, por último, creará un 
 * {@link ControladorGraficoIF} que se encargará de dar vida a la aplicación.
 * 
 * @author Héctor Luaces Novo
 */ 
public class Main {
    public static void main(String[] args) {
        ControladorGraficoIF iu;
        Aplicacion a;
        
        try {
            /* Instanciamos una aplicación vacía y creamos una interfaz de 
             * usuario usando la factoría para tal fin. En ésta práctica
             * usaremos la interfaz "swing", pero si fuese necesario usar
             * otra interfaz (web, consola, etc) y ésta respeta la interfaz
             * ControladorGraficoIf no sería necesario cambiar nada más que
             * esta línea de la aplicación.
             */
            a  = new Aplicacion();
            iu = IUFactory.getControladorIU(a, "swing");
  
            /*
             * Creamos un controlador de errores para manejar las excepciones
             * que no hayan sido controladas en un nivel superior.
             *
             * El controlador propagará la excepción al entorno gráfico
             * para que lo gestione o muestre al usuario.
             */
            Thread.setDefaultUncaughtExceptionHandler(
                new UncaughtExceptionHandler() {
                    @Override
                    public void uncaughtException(Thread t, Throwable e) {
                        iu.procesarError(e);
                    }
                }
            );
            
            // Creamos datos de ejemplo para la aplicación.
            PobladorAplicacion.poblarAplicacion(a);
            
            /* Lanzamos el gestor de tareas programadas de la aplicación.
             * En un escenario real esto debería estar en un crontab (UNIX)
             * o cualquier equivalente del sistema operativo para que
             * se ejecute de forma periódica. Como en ésta práctica
             * esa funcionalidad escapa al alcance del enunciado, 
             * la lanzamos de forma 'inicial' para llevar a cabo el proceso,
             * ya que la creación de datos de ejemplo tiene un factor
             * aleatorio y es necesaria esta llamada para el envío de mensajes
             */
            a.cron();
            
            // Finalmente, iniciamos la interfaz de usuario.
            iu.iniciarIU(a);
        }
        // Este error solo debería llegar aquí si se produce un error
        // antes de que se inicialice la interfaz gráfica.
        catch(FileNotFoundException | FileSystemException ex) {
            ex.printStackTrace();
        }
    }
}
