package iu.swing;

import biblioteca.Catalogo;
import busqueda.Filtro;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import medios.AtributoMedio;
import medios.EnumTiposAtributo;
import medios.MedioIF;

/**
 * Modelo usado por la tabla principal de gestión de medios de la biblioteca.
 * 
 * @author Héctor Luaces Novo
 */
class TablaMediosModel extends AbstractTableModel {
    /**
     * Lista de columnas de la tabla
     */
    private List<EnumTiposAtributo> columnas;
    
    /**
     * Lista de medios en la tabla
     */
    private List<MedioIF> medios; 
    
    /**
     * Determina si la tabla será editable (true) o no.
     */
    private boolean editable;
    
    /**
     * El catálogo asociado a ésta tabla, para poder realizar las operaciones
     * de eliminación oportunas.
     */
    private Catalogo catalogo;
    
    /**
     * En el caso de que solo se quiera mostrar una parte filtrada del catálogo,
     * éste {@link Filtro} estará establecido 
     */
    private Filtro filtro;
    
    /**
     * Constructor privado que inicializa las listas internas de la herramienta.
     * 
     * @see TablaMediosModel#TablaMediosModel(biblioteca.Catalogo) 
     * @see TablaMediosModel#TablaMediosModel(biblioteca.Catalogo, busqueda.Filtro) 
     */
    private TablaMediosModel() {
        this.columnas = new ArrayList<>();
        this.medios   = new ArrayList<>();
    }
    
    /**
     * Recalcula las columnas asociadas al modelo. 
     * 
     * Se usa en el caso de que eliminemos todo un tipo de medios y, por lo 
     * tanto, las columnas específicas de ese tipo de medios ya no sean 
     * necesarias
     */
    private void recalcularColumnas() {
        this.columnas = new ArrayList<>();
        
        for(MedioIF m: this.medios) {
            for(AtributoMedio a: m.getAtributosMedio()) {
                if ( columnas.contains(a.getNombre()) )
                    continue;
                
                columnas.add(a.getNombre());
            }
        }
                
        this.columnas.sort(new Comparator<EnumTiposAtributo>() {
            @Override
            public int compare(EnumTiposAtributo o1, EnumTiposAtributo o2) {
                return Integer.compare(o1.getOrden(), o2.getOrden());
            }
        });
        
        this.fireTableStructureChanged();
    }
    
    /**
     * Constructor que asocia un catálogo al modelo de tabla.
     * @param c El catálogo que asociaremos a la tabla.
     */
    public TablaMediosModel(Catalogo c) {
        this();
        
        this.catalogo = c;
        this.filtro   = null;
        this.medios.addAll(c.getMedios());
        this.recalcularColumnas();
    }
    
    /**
     * Constructor que asocia un catálogo al modelo de tabla, así como un 
     * {@link Filtro} (usado por si solo queremos mostrar un número
     * reducido de los medios del catálogo)
     * 
     * @param c El catálogo que asociaremos a la tabla
     * @param f El filtro de búsqueda de la tabla
     */
    public TablaMediosModel(Catalogo c, Filtro f) {
        this();
        
        this.catalogo = c;        
        this.setFiltro(f);
    }

    public void setFiltro(Filtro f) {
        this.filtro = f;
        
        if ( f == null ) {
            this.medios = this.catalogo.getMedios();
        }
        else {
            this.medios = this.catalogo.buscarEn(f);
        }
        
        //this.recalcularColumnas();
        //this.fireTableDataChanged();
    }
    
    @Override
    public String getColumnName(int column) {
        return this.columnas.get(column).toString();
    }

    
    @Override
    public int getRowCount() {
        return this.medios.size();
    }

    @Override
    public int getColumnCount() {
        return this.columnas.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        try {
            MedioIF medio;
            Object valor;
            
            if ( rowIndex > this.medios.size() )
                return null;
            
            if ( columnIndex > this.columnas.size() )
                return null;
            
            medio = this.medios.get(rowIndex);
            valor = medio.getValorAtributo(
                this.columnas.get(columnIndex)
            );
            
            return valor == null ? valor : valor.toString();
        }
        catch (IndexOutOfBoundsException e) {
            //e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if ( this.columnas.get(columnIndex) == EnumTiposAtributo.TIPO )
            return false;
        
        if ( this.columnas.get(columnIndex) == EnumTiposAtributo.ESTADO )
            return false;
        
        if ( this.columnas.get(columnIndex) == EnumTiposAtributo.BIBLIOTECA )
            return false;
        
        return this.isEditable();
    }

    
    
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        EnumTiposAtributo atributo;
        MedioIF medio;
        
        try {
            medio    = this.medios.get(rowIndex);
            atributo = this.columnas.get(columnIndex);
            
            medio.addAtributo(new AtributoMedio(atributo, aValue));
            this.fireTableDataChanged();
        }
        catch (IndexOutOfBoundsException e) {
            //e.printStackTrace();
        }
    }

    @Override
    public int findColumn(String columnName) {
        return this.columnas.indexOf(EnumTiposAtributo.valuePorNombre(columnName)
        );
    }

    /**
     * Determina si la tabla es editable o no.
     * 
     * @return True si la tabla es editable, false si no lo es.
     */
    public boolean isEditable() {
        return editable;
    }

    /**
     * Permite hacer que la tabla sea editable o solo de lectura.
     * 
     * @param editable true si queremos que la tabla sea editable o false
     * si queremos que sea de solo lectura.
     */
    public void setEditable(boolean editable) {
        this.editable = editable;
    }
    
    /**
     * Dada una fila del modelo, devuelve el medio asociado a la misma, si 
     * existe.
     * 
     * @param row Fila de lmodelo para la que queremos conocer el Medio.
     * @return Fila del modelo para la que queremos conocer el medio.
     */
    public MedioIF getMedioAt(int row) {
        if ( this.medios.size() < row || row < 0 )
            return null;
        
        return this.medios.get(row);
    }
    
    /**
     * Elimina la filas de cuyo número coincida con aquellas pasadas en un 
     * array de filas.
     * 
     * @param rows Array de filas de modelo que queremos eliminar
     */
    public void removeRows(int []rows) {
        Deque<MedioIF> cola = new ArrayDeque<>();
        
        // Los quitamos a la vez porque de lo contrario al eliminar 
        // una fila estaríamos desplazando la lista y podríamos borrar lo que
        // no queremos
        for(int i: rows) {
            MedioIF m;
            
            try {
                m = this.medios.get(i);
                cola.add(m);
                this.catalogo.removeMedio(m);
                this.fireTableRowsDeleted(i, i);
            }
            catch (ArrayIndexOutOfBoundsException e) {
                continue;
            }
        }
        
        this.medios.removeAll(cola);
        this.recalcularColumnas();
        
        this.fireTableDataChanged();
        this.fireTableStructureChanged();
    }
}
