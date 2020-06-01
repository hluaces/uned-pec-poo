package iu;

import iu.swing.ControladorSwing;
import aplicacion.Aplicacion;

/**
 * Clase estática que implementa el patrón factoría para seleccionar el 
 * controlador gráfico que será usado por la aplicación.
 * 
 * @author Héctor Luaces Novo
 */
final public class IUFactory {
    /**
     * Esta clase no puede ser instanciada. 
     * @see IUFactory#getControladorIU(biblioteca.Aplicacion, java.lang.String) 
     */
    private IUFactory() {       
    }
    
    /**
     * Dado una aplicación y un nombre de controlador gráfico, este método
     * factoría devuelve el {@link ControladorGraficoIF} que será usado
     * para mostrar la aplicación.
     * 
     * @param a La aplicación activa para la que queremos el entorno.
     * @param tipo Nombre del ipo de entorno a mostrar. Actualmente solo se 
     * soporta "swing".
     * @return El {@link ControladorGraficoIF} solicitado.
     */
    
    public static ControladorGraficoIF getControladorIU(Aplicacion a, String tipo) {
        if ( ! tipo.equals("swing") )
            throw new UnsupportedOperationException(
                "El tipo de controlador de interfaz de usuario '" + tipo + "' " +
                    "no está implementado."
            );
            
        return new ControladorSwing(a);
    }
}
