package busqueda;

import java.util.List;
import java.util.ListIterator;
import misc.StringUtil;

/**
 * Clase que implementa un 'buscador', un objeto que posee un filtro y que 
 * permite realizar búsquedas o filtrados de colecciones en las que queremos
 * hacer coincidir dicho filtro.
 *
 * @author Héctor Luaces Novo
 */
public class Buscador {
    /**
     * El filtro de búsqueda que tendrá éste buscador
     */
    private Filtro f;

    /**
     * Crea un nuevo buscador con un filtro facilitado como parámetro.
     * 
     * @param f Filtro que queremos asociar al buscador.
     */
    public Buscador(Filtro f) {
        if ( f == null )
            throw new IllegalArgumentException(
                "Imposible crear un buscador con un filtro vacío."
            );
        
        this.f = f;
    }
    
    /**
     * Dados dos valores determina si éstos son iguales.
     * 
     * En caso de comprobar Strings, no tendrá en cuenta la capitalización 
     * y solo hará búsquedas parciales (es decir, el texto "ojo" será igual a 
     * "rojo" o "cojo".
     * 
     * Puede ser sobrecargado por buscadores especializados que hereden de ésta
     * clase.
     * 
     * @param valor Primer valor que queremos comparar.
     * @param valor2 Segundo valor que queremos comparar.
     * @return True (si ambos valores son iguales) falso de cualquier otra forma.
     */
    protected boolean compararValores(Object valor, Object valor2) {
        String a, b;
        
        if ( valor == null || valor2 == null )
            return valor == valor2;
        
        if ( valor.equals(valor2) )
            return true;
        
        // Clases distintas -> falso
        //if ( valor.getClass() != valor2.getClass() )
        //    return false;
        
        // Clases iguales que no tienen .equals y no son String: false
        a = valor.toString().toLowerCase();
        b = valor2.toString().toLowerCase();

        if ( a.isEmpty() && b.isEmpty() )
            return true;

        if ( a.isEmpty() || b.isEmpty() )
            return false;

        if ( StringUtil.compararSinTildes(a, b) )
            return true;

        return a.contains(b) || b.contains(a);
    }
        
    /**
     * Dado un objeto buscable, determina si éste encaja con el criterio
     * de búsqueda del buscador.
     * 
     * @param b Objeto para el que queremos comprobar si encaja con el buscador.
     * @return True (si el objeto pasado encaja con el filtro del buscador) o 
     * falso si éste no lo hace.
     */
    public boolean isValidoPara(BuscableIF b) {
        if ( b == null )
            return false;
        
        // Recorremos todos los criterios del filtro.
        for(Criterio c: f.getCriterios()) {           
            boolean valido;
            Object valor;        
            
            valido = b.tieneCampoBuscable(c.getCampo());
            
            // El objeto no tiene un campo buscable y el filtro es
            // absoluto, por lo que paramos.
            if ( ! valido && f.isAbsoluto() )
                return false;
 
            // El objeto no tiene un campo buscable, pero el filtro no 
            // es absoluto así que es posible que encaje en otro criterio.
            if ( ! valido )
                continue;
            
            valor  = b.getValorCampo(c.getCampo());
            valido = this.compararValores(valor, c.getValor());
            
            // El filtro no es absoluto y hay un criterio que encaja
            // con el objeto: sabemos que sí encaja.
            if ( valido && ! f.isAbsoluto() )
                return true;
            
            // El filtro es absoluto y hay un solo criterio que NO encaja
            // con el objeto: sabemos que no encaja.
            if ( ! valido && f.isAbsoluto() )
                return false;
        }
        
        /*
         * Si llegamos aquí, 1 de dos:
         * 1- El filtro es absoluto y TODOS los criterios se cumplen, por lo que
         * podemos devolver 'true'.
         * 2- El filtro NO es absoluto y NINGÚN criterio se cumple, por lo que
         * podemos devolver 'false'
         */
        return f.isAbsoluto();
    }
    

    /**
     * Dado una lista de objetos buscables, el buscador la filtrará y devolverá
     * dicha lista filtrada.
     * 
     * Tener en cuenta que los parámetros se pasan por referencia.
     * 
     * @param lista Lista de objetos buscables a filtrar.
     * @return Lista de los objetos válidos encontrados en la lista (la litra
     * original también se verá modificada)
     */
    public List<BuscableIF> filtrarLista(List<BuscableIF> lista) {
        ListIterator<BuscableIF> i;
        
        if ( lista == null )
            return null;
        
        lista.removeIf(p -> ! this.isValidoPara(p));
        return lista;
    }
}
