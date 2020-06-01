package multas;

import java.util.Date;
import prestamos.Prestamo;

/**
 * Clase que representa una multa.
 * 
 * Una multa es una sanción que se emite a un usuario cuando este incumple
 * los acuérdamos de préstamo de la biblioteca.
 * 
 * La naturaleza de la Multa es abstracta en esta aplicación.
 * 
 * @author Héctor Luaces Novo
 */
public class Multa {
    /**
     * Préstamo que generó la multa
     */
    private final Prestamo prestamo;
    
    /**
     * La fecha en la que se emite la multa
     */
    private Date fechaEmision;
    
    /**
     * Determina si la multa está vigente o no
     */
    private boolean vigente;

    /**
     * Constructor principal de la clase
     * 
     * @param p Prestamo que generó la multa
     */
    public Multa(Prestamo p) {
        this.fechaEmision = new Date();
        this.vigente      = true;
        this.prestamo     = p;
    }

    /**
     * Devuelve la fecha en la que fue emitida la multa.
     * 
     * @return Fecha de emisión de la multa.
     */
    public Date getFechaEmision() {
        return this.fechaEmision;
    }
    
    /**
     * Devuelve 'true' si la multa aún está vigente, o falso si no lo está.
     * @return the vigente
     */
    public boolean isVigente() {
        return vigente;
    }

    /**
     * Paga la multa, haciendo que deje de estar vigente.
     */
    public void pagar() {
        this.vigente = false;
    }

    /**
     * Devuelve el {@link Prestamo} asociado a esta multa.
     * 
     * @return El préstamo asociado a la multa.
     */
    public Prestamo getPrestamo() {
        return prestamo;
    }
}
