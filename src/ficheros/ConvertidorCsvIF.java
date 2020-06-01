package ficheros;
/**
 * Interfaz para ser implementada por los convertidores de CSV, clases que 
 * sirven de intermediarias entre una {@link FilaCsv} y un 
 * {@link ImportadorCSV}.
 * 
 * Se encarga de realizar la conversión de una fila a un tipo de objeto dado
 * que se establece como genérico.
 * 
 * @param <K> Tipo de valor que con el que trabajarán las operaciones de esta
 * clase.
 * 
 * @author Héctor Luaces Novo
 */
public interface ConvertidorCsvIF<K> {
    /**
     * Dado una FilaCsv devuelve un objeto de un tipo facilitado que será
     * creado a través de la fila de Csv pasada.
     * @param csv La fila de Csv a ser convertida
     * @return El nuevo objeto creado a partir de la fila pasada.
     */
    
    public K convertirDesdeCsv(FilaCsv csv);
    
    /**
     * Dado un objeto K convertirá dicho objeto en una FilaCsv que devolverá
     * como valor de retorno.
     * 
     * @param objeto Objeto a convertir en una FilaCsv
     * @param columnas Lista de columnas que se usarán en el objeto
     * @return FilaCsv convertido (o null)
     */
    public FilaCsv convertirACsv(K objeto, String[] columnas);
}
