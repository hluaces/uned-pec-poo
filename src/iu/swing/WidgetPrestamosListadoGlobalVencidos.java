package iu.swing;

import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * Widget que permitirá ver el listado de préstamos vencidos y no devueltos
 * de la biblioteca.
 * 
 * Hereda casi toda su funcionalidad de {@link WidgetListadoPrestamos}
 * 
 * @author Héctor Luaces Novo
 */
public class WidgetPrestamosListadoGlobalVencidos extends WidgetPrestamosListadoGlobal {
    public WidgetPrestamosListadoGlobalVencidos(ControladorSwing c) {
        super(c);
    }

    @Override
    public void actualizar() {
        super.actualizar(); 
        
        this.setTitle("Listado de préstamos vencidos sin devolver");
    }

    
    @Override
    protected void cargarDatos() {
        this.datos = this.getControlador()
            .getBibliotecaActiva()
            .getPrestamos()
            .stream()
            .filter((c) -> c.isVencido())
            .collect(Collectors.toSet())
        ;
        
        if ( this.datos == null )
            this.datos = new HashSet<>();
    }
}
