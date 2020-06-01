package prestamos;

import java.util.Date;
import java.util.Objects;
import medios.MedioIF;
import usuarios.Usuario;

/**
 * Claseque representa una reserva.
 * 
 * La reserva asocia un medio con un usuario y tiene una fecha de creación.
 * 
 * @author Héctor Luaces Novo
 */
public class Reserva {
    /**
     * El usuario que hace la reserva
     */
    private Usuario usuario;
    
    /**
     * El medio que reserva el usuario
     */
    private MedioIF medio;
    
    /**
     * La fecha de creación de la reserva
     */
    private Date fecha;

    /**
     * Crea una reserva de un usuario para un medio usando como fecha
     * la fecha actual.
     *  
     * @param usuario Usuario que hace la reserva.
     * @param medio Medio que se solicita en la reserva.
     */
    public Reserva(Usuario usuario, MedioIF medio) {
        this.usuario = usuario;
        this.medio   = medio;
        this.fecha   = new Date();
    }
    
    /**
     * Crea una reserva de un usuario para un medio usando una fecha
     * de creación alternativa.
     * 
     * @param usuario Usuario que hace la reserva.
     * @param medio Medio que solicita el usuario.
     * @param f Fecha de creación de la reserva.
     */
    public Reserva(Usuario usuario, MedioIF medio, Date f) {
        this(usuario, medio);
        
        this.fecha  = f;
    }

    /**
     * Devuelve el usuario que hace la reserva.
     * 
     * @return El usuario que hace la reserva.
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Devuelve el medio que está asociado a la reserva.
     * 
     * @return  El medio asociado a la reserva.
     */
    public MedioIF getMedio() {
        return medio;
    }

    /**
     * Devuelve la fecha de creación de la reserva.
     * 
     * @return La fecha de creación de la reserva.
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * Dos reservas son iguales si su usuario y medio son iguales.
     * 
     * @param obj La reserva a comparar
     * @return True si son iguales, falso de cualquier otra forma
     */
    @Override
    public boolean equals(Object obj) {
        Reserva r;
        
        if ( obj == null || obj.getClass() != this.getClass() )
            return super.equals(obj); //To change body of generated methods, choose Tools | Templates.
        
        r = (Reserva) obj;
        
        return r.getUsuario() == this.getUsuario() && r.getMedio() == this.getMedio();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.usuario);
        hash = 29 * hash + Objects.hashCode(this.medio);
        return hash;
    }
    
    
}
