package iu.swing;

import aplicacion.Aplicacion;
import biblioteca.Biblioteca;
import busqueda.Filtro;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import medios.EnumTiposAtributo;
import medios.MedioIF;

/**
 * Un widget para mostrar los resultados de una 
 * {@link Aplicacion#busquedaCruzadaMedios(busqueda.Filtro) búsqueda cruzada}
 * de medios.
 * 
 * @author Héctor Luaces Novo
 */
public class WidgetMediosCruzados extends AbstractWidgetConTabla<MedioIF> {
    /**
     * Un filtro que será usado para realizar la búsqueda cruzada.
     */
    private final Filtro filtro;
    
    /**
     * Una lista con los nombres de las columnas de medios.
     * 
     * Solo se mostrarán las columnas que tengan sentido con los medios a 
     * mostrar, por lo que se hará de forma dinámica.
     */
    private List<String> listaColumnas;
    
    /**
     * Crea un nuevo Widget inyectándole un ControladorSwing y un filtro
     * que se usará para la búsqueda cruzada.
     * 
     * @param c El controladorSwing a usar
     * @param f 
     */
    public WidgetMediosCruzados(ControladorSwing c, Filtro f) {
        super();
        
        this.filtro = f;
        this.setControlador(c);
        this.actualizar();
    }

    @Override
    public void actualizar() {
        super.actualizar(); 
        
        this.setTitle("Resultados de la búsqueda cruzada");
    }

    
    @Override
    protected void cargarDatos() {
        Set<String> cols;
        Map<Biblioteca, List<MedioIF>> resultado;
        
        cols               =  new HashSet<>();
        this.listaColumnas = new ArrayList<>();
        
        // Hacemos la búsqueda cruzada
        resultado     = this.getControlador()
            .getAplicacion()
            .busquedaCruzadaMedios(this.filtro)
        ;
        
        if ( resultado.isEmpty() ) {
            super.cargarDatos();
            return;
        }
        
        this.datos = new HashSet<>();
        
        // Tenemos la búsqueda cruzada, guardamos las columnas que necesitaremos
        // en un set (para que no haya duplicados)
        for(Biblioteca b: resultado.keySet()) {
            this.datos.addAll(resultado.get(b));
            
            for(MedioIF m: resultado.get(b)) {
                cols.addAll(m.getAtributosMedio().stream().map(
                    (c) -> c.getNombre().getNombre()
                ).collect(Collectors.toSet()));
            }
        }
        
        // Añadimos la columna "Biblioteca" en la posición 0 y posteriormente
        // convertimos el set de columnas en una lista
        this.listaColumnas = new ArrayList(cols);

        // Convertimos el set de columnas en un array y lo dejamos para que 
        // actualizar() genere las columnas
        this.columnas      = this.listaColumnas.toArray(
                new String[this.listaColumnas.size()]
        );
    }

    @Override
    protected void addRow(MedioIF item) {
        String []datos = new String[this.listaColumnas.size()];

        // Desde ahí, exceptuando la columna, procedemos a guardar en el array
        // los valores de los diferentes datos
        for(String nombreColumna: this.listaColumnas) {
            EnumTiposAtributo tipoAtr;
            Object value;
            
            
            tipoAtr = EnumTiposAtributo.valuePorNombre(nombreColumna);
            
            if ( ! item.hasAtributo(tipoAtr) )
                continue;
            
            value = item.getValorAtributo(tipoAtr);
            
            if ( value == null )
                continue;
            
            datos[this.listaColumnas.indexOf(nombreColumna)] = value.toString();
        }
        
        this.getModeloTabla().addRow(datos);
    }

    /**
     * Devuelve el filtro creado en éste Widget.
     * 
     * @return El filtro creado en éste widget.
     */
    protected Filtro getFiltro() {
        return this.filtro;
    }
    
    /**
     * Dos widgetsMediosCruzados serán iguales cuando sus controladores,
     * usuarios y FILTROS sean iguales.
     * 
     * @param obj Objeto con el que comparar a este widget.
     * @return true si son iguales. Falso de cualquier otra forma.
     */
    @Override
    public boolean equals(Object obj) {
        
        if ( ! super.equals(obj) )
            return false;
                
        return ((WidgetMediosCruzados) obj).getFiltro().equals(this.filtro);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.filtro);
        return hash;
    }
    
    
}
