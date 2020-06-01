package iu.swing;

import java.util.HashSet;
import usuarios.Usuario;

/**
 * {@link AbstractWidgetConTabla} que muestra el historial de préstamos
 * de un usuario, es decir: todos los préstamos sin importar su estado.
 * 
 * @author Héctor Luaces Novo
 */
public class WidgetPrestamosHistorial extends WidgetPrestamosActivos {

    public WidgetPrestamosHistorial(ControladorSwing c) {
        super(c);
    }

    @Override
    public void actualizar() {
        super.actualizar(); 
                
        this.setTitle("Historial de préstamos para " + this.getUsuario().getLogin());
    }

    
    public WidgetPrestamosHistorial(ControladorSwing c, Usuario u) {
        super(c, u);
    }

    @Override
    protected void cargarDatos() {
        this.datos = this.getControlador()
            .getBibliotecaActiva()
            .getPrestamosUsuario(this.getUsuario())
        ;
        
        if ( this.datos == null )
            this.datos = new HashSet<>();
    }
}
