package misc;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Clase estática que implementa métodos útiles para realizar 
 * operaciones criptográficas.
 * 
 * @author Hëctor Luaces Novo
 */
final public class CryptUtil {
    /**
     * Esta clase es de naturaleza estática y no puede ser instanciada.
     */
    private CryptUtil() {
        
    }
    
    /**
     * Convierte una cadena pasada como parámetro en un hash MD5 con el 
     * cotejamiento solicitado.
     * 
     * @param s Cadena a convertir en MD5
     * @param cotejamiento Nombre del cotejamiento a usar
     * @return Hash MD5 de la cadena solicitada. Null si sucede algún error.
     */
    public static String toMD5(String s, String cotejamiento) {
        MessageDigest dg;
        byte[] hash;
        
        try {
            dg   = MessageDigest.getInstance("MD5");
            hash = dg.digest(s.getBytes(cotejamiento));
            s    = Arrays.toString(hash);
        }
        catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            return null;
        }
        
        return s;
    }
    
    /**
     * Sobrecarga por conveniencia del método 
     * {@link CryptUtil#toMD5(java.lang.String, java.lang.String)} que usa
     * UTF-8 como cotejamiento por defecto.
     * 
     * @param s String a convertir en MD5
     * @return Hash de la cadena pasada como parámetro. Null si sucede algún
     * error.
     */
    public static String toMD5(String s) {
        return toMD5(s, "UTF-8");
    }
}
