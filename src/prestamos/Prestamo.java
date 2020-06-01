package prestamos;

import java.util.Calendar;
import java.util.Date;
import usuarios.Usuario;
import medios.MedioIF;

/**
 *
 * @author mordisko
 */
public class Prestamo {
    private Date fechaInicio, fechaVencimiento, fechaDevolucion;
    private Usuario usuario;
    private MedioIF medio;
    private boolean avisado;
    
    public Prestamo(int dias_prestamo, Usuario a_quien, MedioIF que) {
        Calendar c = Calendar.getInstance();
        
        c.setTime(new Date());
        c.add(Calendar.DAY_OF_YEAR, dias_prestamo);
        
        this.fechaVencimiento = c.getTime();
        this.usuario          = a_quien;
        this.medio            = que;
        this.fechaInicio      = new Date();
    }

    public boolean isAvisado() {
        return this.avisado;
    }
    
    public boolean isDevuelto() {
        return this.fechaDevolucion != null;
    }
    
    public void setAvisado(boolean a) {
        avisado = a;
    }
    
    public boolean isVencido() 
    {
        if ( this.fechaDevolucion != null )
            return false;
        
        return 0 > this.fechaVencimiento.compareTo(new Date());
    }
    
    public boolean devolver() {
        if ( this.fechaDevolucion != null )
            return false;
        
        this.fechaDevolucion = new Date();
        return true; 
    }
    /**
     * @return the fecha_inicio
     */
    public Date getFechaInicio() {
        return fechaInicio;
    }

    /**
     * @param fechaInicio the fecha_inicio to set
     */
    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    /**
     * @return the fecha_fin
     */
    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    /**
     * @param fechaVencimiento the fecha_fin to set
     */
    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    /**
     * @return the fecha_devolucion
     */
    public Date getFechaDevolucion() {
        return fechaDevolucion;
    }

    /**
     * @param fechaDevolucion the fecha_devolucion to set
     */
    public void setFechaDevolucion(Date fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    /**
     * @return the a_quien
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * @param usuario the a_quien to set
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * @return the que
     */
    public MedioIF getMedio() {
        return medio;
    }

    /**
     * @param medio the que to set
     */
    public void setMedio(MedioIF medio) {
        this.medio = medio;
    }
    
    
}
