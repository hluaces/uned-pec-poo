package ficheros;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.FileSystemException;
import java.util.ArrayList;
import java.util.List;
import medios.MedioIF;

/**
 * Clase que permite exportar una serie de objetos a un ficheroCSV.
 * 
 * Usará un {@link ConvertidorCsvIF} para convertir los objetos en {@link 
 * FilaCsv} que posteriormente serán escritas al fichero.
 * 
 * @author Hëctor Luaces Novo
 */
public class ExportadorCSV {
    /**
     * Archivo que está asociado a éste exportador.
     */
    private File archivo;
    
    /**
     * Delimitador usado en el archivo CSV
     */
    private String delimitador;
    
    /**
     * Columnas que se usarán
     */
    private List<String> columnas;
    
    /**
     * Convertidor usado para el proceso de escritura
     */
    private final ConvertidorCsvMedios convertidor;
    
    /**
     * Crea un nuevo exportador de CSV que escribirá a un fichero facilitado
     * como parámetro.
     * 
     * @param ruta_fichero Ruta del fichero donde se guardarán los datos.
     * @param convertidor Convertidor que utilizará el exportador.
     * @param cols Lista de columnas ordenadas que usará el exportador.
     * 
     * @throws FileSystemException Si ocurre algún error a la hora de intentar
     * abrir el fichero.
     */
    public ExportadorCSV(
        String ruta_fichero, ConvertidorCsvMedios convertidor, List<String> cols
    ) throws FileSystemException 
    {
        this.archivo = new File(ruta_fichero);
        
        try {
            if ( archivo.exists() && ! archivo.isFile() )
                throw new FileSystemException(ruta_fichero + " no es un fichero.");
        
            if ( archivo.exists() && ! archivo.canWrite() )
                throw new FileSystemException(
                    "Imposible escribir en el fichero " + ruta_fichero + "."
                );
        }
        catch (FileSystemException e) {
            this.archivo = null;
            throw e;
        }

        this.delimitador = "\t";
        this.convertidor = convertidor;
        this.columnas    = cols;
    }
    
    /**
     * Dada una lisa de {@link FilaCsv}, intenta escribirla en el fichero
     * asociado a este exportador.
     * 
     * @param lista Lista de FilaCsv a escribir.
     * @return true si la operación tiene éxito.
     * 
     * @throws FileNotFoundException Si ocurre algún error al intentar escribir
     * al fichero especificado.
     */
    public boolean escribir(List<MedioIF> lista) throws FileNotFoundException {
        List <FilaCsv> filas;
        PrintWriter writer;

        filas = new ArrayList<>();        
    
        for(MedioIF m: lista) {
            filas.add(this.convertidor.convertirACsv(
                m, 
                this.columnas.toArray(new String[this.columnas.size()])
            ));
        }
        
        try {
            writer = new PrintWriter(this.archivo, "utf8");
        } catch (UnsupportedEncodingException ex) {
            return false;
        }
        
        // Imprimimos cabeceras
        writer.println(String.join(
                this.delimitador, 
                this.columnas
            )
        );
        
        for(FilaCsv f: filas) {
            String fila = "";
            
            for(String columna: this.columnas) {
                fila = fila + (f.getDato(columna) != null ? f.getDato(columna) : "");
                fila += this.delimitador;
            }
            
            writer.println(fila);
        }

        writer.close();
        return true;
    }
    
    /**
     * Devuelve el delimitador usado por éste importadorCsv para leer
     * los archivos .csv.
     * 
     * @return El delimitador usado.
     */
    public String getDelimitador() {
        return delimitador;
    }

    /**
     * Establece el delimitador usado por éste importadorCsv
     * 
     * @param delimitador Nuevo valor del delimitador usado
     */
    public void setDelimitador(String delimitador) {
        this.delimitador = delimitador;
    }
}
