package misc;

import java.util.Calendar;
import java.util.Date;

/**
 * Clase estática que encapsula funciones útile spara trabajar con fechas.
 * 
 * @author Héctor Luaces Novo
 */
final public class DateUtil {
    /**
     * Clase estática que no puede ser instanciada.
     */
    private DateUtil() {
        
    }
    
    /**
     * Añade un número de diás a una fecha dada.
     * 
     * @param d Fecha a la que queremos añadir días.
     * @param dias Número de días que queremos añadir.
     * @return Nueva fecha generada a partir de realizar la operación
     * especificada.
     */
    public static Date addDias(Date d, int dias) {
        Calendar c = Calendar.getInstance();
        
        c.setTime(d);
        c.add(Calendar.DAY_OF_MONTH, dias);
        
        return c.getTime();
    }
}
