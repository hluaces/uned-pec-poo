package ficheros;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Clase que representa a un 'importador de csv' que permite obtener objetos
 * de tipo {@link FilaCsv FilaCsv} de un inputStream dado usando un convertidor
 * facilitado.
 *
 * @see FilaCsv
 * @see ConvertidorCsvMedios
 * @author Héctor Luaces Novo
 */
public class ImportadorCSV {
    /**
     * InputStream que está asociado a éste importador.
     */
    private InputStream archivo;
    
    /**
     * Delimitador usado en el archivo CSV
     */
    private String delimitador;
    
    /**
     * Convertidor usado para el proceso de lectura
     */
    private final ConvertidorCsvMedios convertidor;
    
    /**
     * Crea un nuevo ImportadorCSV y le asocia un fichero con una ruta dada.
     * 
     * La ruta ha de ser absoluta.
     * 
     * @param ruta_fichero String con la ruta del recurso a leer
     * @param convertidor Convertidor que será usado en la clase.
     */
    public ImportadorCSV(String ruta_fichero, ConvertidorCsvMedios convertidor) throws FileNotFoundException 
    {
        File f = new File(ruta_fichero);
        
        if ( f.exists() ) {
            this.archivo = new FileInputStream(f);
        }
        else {
            this.archivo = this.getClass().getClassLoader().getResourceAsStream(ruta_fichero);
        }

        this.delimitador = "\t";
        this.convertidor = convertidor;
    }
    
    /**
     * Lee todo el fichero CSV y lo parsea, convirtiéndolo en una lista de
     * {@link FilaCsv filas de CSV}
     * 
     * @return Lista de filasCsv que representan los valores leídos en el 
     * archivo. Si no hay ninguna fila devolverá una lista vacía.
     * @throws FileNotFoundException Si el archivo asociado a este importador
     * no se encuentra
     */
    private List<FilaCsv> leerCsv() throws FileNotFoundException {
        List<FilaCsv> ret = new ArrayList<>();
        String [] cabeceras = null;
        Scanner s;
        

        s = new Scanner(this.archivo, "utf8");
        s.useDelimiter(this.getDelimitador());
        
        while (s.hasNextLine()) {
            String linea = s.nextLine();
            String [] splitted;
            FilaCsv c;
            
            splitted = linea.split(this.getDelimitador());
            
            if ( cabeceras == null ) {
                cabeceras = splitted;
                continue;
            }

            c = new FilaCsv(cabeceras);
            
            for(int i = 0; i < splitted.length; i++) {
                c.fijarDato(cabeceras[i], splitted[i]);
            }
            
            ret.add(c);
        }
    
        s.close();
        return ret;
    }
    
    /**
     * Lee todo el fichero CSV asociado a este importador y devuelve la lista
     * de objetos creados con el convertidor.
     * 
     * @return Lista de objetos del archivo CSV tras ser convertidos.
     * Si no hay ninguno devolverá una lista vacía.
     * @throws FileNotFoundException 
     */
    public List<Object> leer() throws FileNotFoundException {
        List<Object> ret = new ArrayList<>();
        
        for(FilaCsv csv: this.leerCsv()) {
            Object b;
            
            //b = ConvertidorCsvMedios.convertirDesdeCsv(csv);
            b = this.convertidor.convertirDesdeCsv(csv);

            if ( b != null )
                ret.add(b);
        }
       
        return ret;
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
