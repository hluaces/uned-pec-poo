package ficheros;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Clase que representa una línea de un fichero 'csv'.
 * 
 * La clase tendrá asociada unas cabeceras y unos valores para cada una de éstas
 * cabeceras.
 * 
 * @author Héctor Luaces Novo
 */
public class FilaCsv {
    /**
     * Map que asocia una cabecera (String) con un dato (String)
     * 
     * Los datos están en formato 'raw', es decir, String.
     * 
     * No es responsabilidad de la clase realizar ningún tipo de conversión
     * u operación sobre los mismos.
     */
    private final Map<String, String> datos;
    
    /**
     * Dado una cabecera, la normaliza para permitir que no haya problemas de
     * comparación mayúsculas/minúsculas entre los archivos .csv y la 
     * programación que use ésta clase
     *
     * @param cabecera Nombre de la cabecera a normalizar
     * @return Valor normalizado ce la cabecera
     */
    private String normalizarCabecera(String cabecera) {
        return cabecera.toLowerCase();
    }
            
    /**
     * Crea una nueva clase CSV con un conjunto de cabeceras pasadas como 
     * parámetro establecidas en 'null'.
     * 
     * @param cabeceras Array con las cabeceras asociadas a esta fila del CSV.
     */
    public FilaCsv(String [] cabeceras) {
        this.datos = new HashMap<>();
        
        for(String s: cabeceras) {
            this.datos.put(this.normalizarCabecera(s), null);
        }
    }
    
    /**
     * Establece el valor de ésta fila de CSV para una cabecera dada.
     * Si la cabecera no es válida la función no hará nada.
     * 
     * @param cabecera Nombre de la cabecera para la que que queremos modificar
     * el valor.
     * @param valor El nuevo valor de ésta fila del Csv para la cabecera dada
     * @return True si el cambio tiene éxito o 'false' si no es así.
     */
    public boolean fijarDato(String cabecera, String valor) {
        cabecera = this.normalizarCabecera(cabecera);

        if ( ! this.datos.keySet().contains(cabecera) )
            return false;
        
        this.datos.put(cabecera, valor);
        return true;
    }
    
    /**
     * Devuelve el valor de ésta fila de CSV para una cabecera dada.
     * 
     * @param cabecera Nombre de la cabecera que queremos consultar en 
     * el CSV.
     * @return El valor de la cabecera, que puede ser null si éste no está
     * inicializado o si la cabecera no existe en la fila.
     */
    public String getDato(String cabecera) {
        return this.datos.get(this.normalizarCabecera(cabecera));
    }
    
    /**
     * Devuelve un set con las cabeceras de ésta fila de CSV.
     * @return Set con el nombre de las cabeceras de ésta fila del CSV.
     */
    public Set<String> getCabeceras() {
        return this.datos.keySet();
    }
}

