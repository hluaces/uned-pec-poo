package misc;

import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Clase estática que implementa métodos útiles para trabajar con cadenas.
 * 
 * @author Héctor Luaces Novo
 */
final public class StringUtil {
    /**
     * La tabla de letras del DNI acorde  al artículo 11 del Real Decreto 
     * 1553/2005, de 23 de  diciembre, 
     * @see http://www.interior.gob.es/web/servicios-al-ciudadano/normativa/reales-decretos/real-decreto-1553-2005-de-23-de-diciembre#art11
     */
    public final static String TABLA_LETRAS_DNI = "TRWAGMYFPDXBNJZSQVHLCKE";
    
    /**
     * La clase no puede ser instanciada o heredada. 
     */
    private StringUtil(){
    }
    
    /**
     * Dada una cadena, la devuelve capitalizada, es decir, con la primera letra
     * en mayúscula y el resto en minúscula.
     * 
     * @param s Cadena a capitalizar.
     * @return Cadena capitalizada.
     */
    public static String capitalizar(String s)
    {
        String inicial;
        
        s = s.toLowerCase();
        inicial = s.substring(0, 1).toUpperCase();
        
        return inicial + s.substring(1);
    }
    
    /**
     * Compara dos cadenas sin tener en cuenta las tildes, haciendo que 
     * "más" sea igual que "mas".
     * 
     * Se asume local es_ES.
     * 
     * @param a Primera cadena a comparar.
     * @param b Segunda cadena a comparar.
     * @return True si ambas cadenas son iguales, sin importar las tildes, false
     * de cualquier otra forma.
     */
    public static boolean compararSinTildes(String a, String b) {
        Collator c = Collator.getInstance(new Locale("es", "ES"));
        
        c.setStrength(Collator.PRIMARY);
        return 0 == c.compare(b, a);
    }
    
    /**
     * Convierte una fecha en un String legible acorde a la local del sistema.
     * 
     * @param d Fecha a convertir
     * @return Valor en formato texto de dicha fecha acorde a la fecha del 
     * sistema
     */
    public static String fechaToString(Date d) {
        SimpleDateFormat formateador;
        String pattern;
        Locale local;
        
        if ( d == null )
            return null;
        
        pattern     = "d/M/y";
        local       = new Locale("es", "ES");
        formateador = new SimpleDateFormat(pattern, local);
        
        return formateador.format(d);
    }
    
    /**
     * Dada la cadena numérica de un DNI devuelve otra cadena con el dígito de 
     * control (conocido vulgarmente como "la letra") asociado a dicho DNI.
     * 
     * @param dni El número de DNI a comprobar.
     * @return El dígito de control de dicho DNI.
     */
    public static String getLetraDni(String dni) 
    {
        int numero;
        
        if ( dni == null )
            return null;
        
        try {
            numero = Integer.parseInt(dni);
        }
        catch (NumberFormatException e) {
            return null;
        }
        
        return TABLA_LETRAS_DNI.substring(numero % 23, numero % 23 + 1);
    }
    
    /**
     * Dado un DNI (con la letra incluída) comprueba si éste es válido ono.
     * 
     * @param tx El DNI a comprobar (pej: 12345123F)
     * @return True si el dni es correcto, falso de cualquier otra forma
     */
    public static boolean isDniValido(String tx) 
    {
        String letra;
        
        if ( tx == null || tx.length() < 2)
            return false;
        
        letra  = getLetraDni(tx.substring(0, tx.length() - 1));
        
        if ( letra == null )
            return false;
        
        return letra.equalsIgnoreCase(tx.substring(tx.length() - 1));
    }
}
