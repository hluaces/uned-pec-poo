package ficheros;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import medios.AtributoMedio;
import medios.EnumTiposAtributo;
import medios.MediosFactory;
import misc.StringUtil;
import medios.MedioIF;
import prestamos.EnumEstadosPrestamo;

/**
 * Convertidor de filas CSV que genera objetos de tipo {@link MedioIF} a partir
 * de las {@link FilaCsv FilasCsv} de las que se nutre.
 * 
 * @author Héctor Luaces Novo
 */
public final class ConvertidorCsvMedios implements ConvertidorCsvIF<MedioIF> {
    /**
     * Crea un nuevo convertidor de csv para medios
     */
    public ConvertidorCsvMedios()
    {
    }
   
    /**
     * Dado un String pasado como parámetro intentará crear un objeto de tipo 
     * 'date' a través de la misma.
     * 
     * Por el momento, el único formato de fecha admitido es dd/mm/yy 
     * (20/05/2017)
     * 
     * @param valor String que queremos parsear
     * @return Objeto Date con la fecha interpretada o null si no ha consegido 
     * interpretarse.
     */
    private Date parsearFecha(String valor) {
        SimpleDateFormat fmt;

        if ( valor == null )
            return null;
        
        fmt = new SimpleDateFormat("dd/mm/yy");
        
        try {
            return fmt.parse(valor);
        } catch (ParseException ex) {
            return null;
        }
    }
    
    /**
     * Dado un valor y una clase, éste método se encargará de converit dicho
     * valor a la clase pasada como segundo parámetro.
     * 
     * @param <K> Tipo de dato que será parseado
     * @param valor Valor a parsear
     * @param tipo Tipo de clase a la que queremos convertir el valor
     * 
     * @return Nuevo valor del objeto (ya convertido a la clase correspondiente)
     */
    private <K> Object parsearValor(Object valor, Class<K> tipo) {
        if ( valor == null )
            return null;
        
        if ( tipo == String.class )
            return valor.toString();
        
        if ( tipo == EnumTiposAtributo.class )
            return EnumTiposAtributo.valuePorNombre(valor.toString());
        
        if ( tipo == EnumEstadosPrestamo.class )
            return EnumEstadosPrestamo.getEstadoPorNombre(valor.toString());
            
        if ( tipo == Integer.class ) {
            try {
                return Integer.parseInt((String) valor);
            }
            catch (NumberFormatException e) {
                return null;
            }
        }
        
        if ( tipo != Date.class )
            return valor;
        
        return parsearFecha(valor.toString());
    }
    
    @Override
    public MedioIF convertirDesdeCsv(FilaCsv csv) {
        Set<AtributoMedio> atributos = new HashSet();

        /*
         * Vamos a construír un set de atributos para crear un medio
         * usando la factoría. Para ello recorremos todos los atributos
         * y tratamos de identificarlos con un valor del CSV.
         */
        for(EnumTiposAtributo e: EnumTiposAtributo.values()) {
            String nombre_real = e.toString().toLowerCase().trim();
            
            // Para cada atributo recorremos cabeceras...
            for(String cabecera: csv.getCabeceras()) {
                String nombre_csv = cabecera.toLowerCase().trim();
                AtributoMedio atr;
                
                if ( ! StringUtil.compararSinTildes(nombre_csv, nombre_real) )
                    continue;
                
                if ( e.getClase() == Integer.class ) {
                    int i = 1 + 1;   
                }
                
                atr = new AtributoMedio(
                    e, 
                    this.parsearValor(csv.getDato(cabecera), e.getClase())
                );
                
                // Tenemos una cabecera coincidente, creamos un atributo 
                // y lo guardamos en el set
                atributos.add(atr);
            }

        }
        
        return MediosFactory.getMedio(atributos);
    }

    @Override
    public FilaCsv convertirACsv(MedioIF m, String[] columnas) {
        FilaCsv c = new FilaCsv(columnas);
        
        for(String cabecera: columnas) {
            EnumTiposAtributo atr;
            Object valor;
            
            atr = EnumTiposAtributo.valuePorNombre(cabecera);
            
            if ( atr == null )
                continue;
            
            valor = m.getValorAtributo(atr);
            
            c.fijarDato(cabecera, valor != null ? valor.toString() : null);
        }
        
        return c;
    }
}
