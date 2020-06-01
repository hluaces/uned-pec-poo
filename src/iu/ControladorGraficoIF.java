package iu;

import aplicacion.Aplicacion;

/**
 * Interfaz que ha de ser implementada por todos los controladores del entorno
 * gráfico de la {@link Aplicacion}.
 * 
 * Define la funcionalidad básica que se espera de un controlador gráfico
 * para que la aplicación pueda crearlo e interactuar con él de forma normal.
 * 
 * @author Héctor Luaces Novo
 */
public interface ControladorGraficoIF {
    /**
     * Dada una aplicación ya inicializada, inicia el bucle principal del 
     * programa y por ende, su interfaz de usuario.
     * 
     * @param a Instancia de la Aplicación que queremos mostrar.
     */
    void iniciarIU(Aplicacion a);
    
    /**
     * Cuando un objeto {@link Aplicacion} se encuentre con un error, se llamrá
     * a éste método para que el controlador gráfico se encargue de mostrar
     * el error y realizar cualquier otra operación que sea pertinente.
     * 
     * La responsabilidad de éste método variará en función de la implementación
     * de la propia interfaz. 
     * 
     * El método también puede ser usado internamente por la propia interfaz
     * si la implementación lo estima oportuno.
     * 
     * @param e El throwable que se ha generado y que se ha de gestionar.
     */
    public void procesarError(Throwable e);

}
