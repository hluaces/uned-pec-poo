package biblioteca;

import java.util.Date;
import usuarios.Usuario;

/**
 * Clase que representa a un mensaje para de usuario.
 * 
 * Los mensajes contienen un cuerpo, una fecha de emisión y pueden estar
 * marcados como leídos o no leídos.
 *
 * 
 * @author Héctor Luaces Novo
 */
public class Mensaje {
    /**
     * Fecha en la que se creó el mensaje
     */
    private final Date fechaMensaje;
    
    /**
     * Texto del mensaje
     */
    private final String mensaje;
    
    /**
     * Determina si el mensaje está leido (true) o si no ha sido leído (false)
     */
    public boolean leido;
    
    /**
     * Destinatario del mensaje
     */
    private final Usuario destinatario;

    public Mensaje(String msj, Usuario u) {
        if ( msj == null || msj.isEmpty() || u == null )
            throw new IllegalArgumentException(
                "Parámetros de mensaje insuficientes."
            );
        
        this.mensaje      = msj;
        this.destinatario = u;
        this.leido        = false;
        this.fechaMensaje = new Date();
    }

    /**
     * Devuelve 'true' si el mensaje ha sido leído.
     * 
     * @return 'true' si el mensaje ha sido leído.
     */
    public boolean isLeido() {
        return leido;
    }

    /**
     * Marca el mensaje como 'leído'.
     */
    public void setLeido() {
        this.leido = true;
    }
    
    /**
     * Devuelve la fecha en la que se creó el mensaje.
     * 
     * @return La fecha de creación del mensaje.
     */
    public Date getFechaMensaje() {
        return fechaMensaje;
    }

    /**
     * Devuelve el texto del mensaje.
     * 
     * @return El texto del mensaje
     */
    public String getMensaje() {
        return mensaje;
    }
    
    /**
     * Devuelve el destinatario del mensaje.
     * 
     * @return El destinatario del mensaje.
     */
    public Usuario getDestinatario() {
        return destinatario;
    }
    
}
