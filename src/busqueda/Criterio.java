package busqueda;

/**
 * Un criterio de búsqueda hace referencia a una relación 'campo de búsqueda' y
 * 'valor' por el que queremos buscar a un objeto en una colección.
 * 
 * Pej, si queremos buscar todos los coches cuyo "color" sea "rojo", tendremos
 * un criterio de búsqueda cuyo campo de búsqueda sea "color" y cuyo valor sea 
 * "rojo".
 * 
 * @see Filtro
 * @see BuscableIF
 * @author Héctor Luaces Novo
 */
public class Criterio {
    /**
     * Nombre del campo buscable al que queremos que haga referencia el criterio
     */
    private String campo;
    
    /**
     * Valor del campo buscable que queremos comprobar en un objeto buscable
     */
    private Object valor;
    
    /**
     * Crea un nuevo filtro con el campo y valor especificados.
     * 
     * @param campo Nombre del campo de búsqueda asociado al criterio
     * @param valor Valor del campo de búsqueda asociado al criterio
     */
    public Criterio(String campo, Object valor) {
        this.campo = campo;
        this.valor = valor;
    }

    public Criterio() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Devuelve el nombre del campo de búsqueda asociado al criterio.
     * 
     * @return El nombre del campo de búsqueda asociado al criterio
     */
    public String getCampo() {
        return campo;
    }

    /**
     * Devuelve el valor del campo de búsqueda asociado al criterio.
     * 
     * @return El valor del campo de búsqueda asociado al criterio.
     */
    public Object getValor() {
        return valor;
    }
}
