package medios;

import java.util.Objects;

/**
 * Clase que representa el valor de un Atributo para un medio.
 * 
 * Un Atributo puede ser cualquier valor intrínseco al medio, pej:
 * 'autor', 'título', 'fecha de publicación', 'duración, etc.
 * 
 * Todos los campos tienen un valor (que puede ser de un tipo diferente)
 * y un {@link EnumTiposAtributo tipo de atributo}
 * 
 * @author Héctor Luaces Novo
 * @param <V> Tipo del valor que se va a almacenar (PEJ: 'String')
 */
public class AtributoMedio<V> {
    private EnumTiposAtributo nombre;
    private V valor;

    /**
     * Crea un valor de campo con un nombre y un valor asociado.
     * 
     * @param nombre Nombre del valor del campo
     * @param valor Valor del campo
     */
    public AtributoMedio(EnumTiposAtributo nombre, V valor) {
        this.nombre = nombre;
        this.valor  = valor;
    }

    public AtributoMedio() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Devuelve el nombre del campo (es sensible a mayúsculas/minúsculas)
     * 
     * @return Cl nombre del campo
     */
    public EnumTiposAtributo getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del campo (es sensible a mayúsculas/minúsculas)
     * @param nombre the nombre to set
     */
    public void setNombre(EnumTiposAtributo nombre) {
        this.nombre = nombre;
    }

    /**
     * Devuelve el valor de éste campo del medio.
     * 
     * @return El valor del campo.
     */
    public V getValor() {
        return valor;
    }

    /**
     * Fija el valor de éste campo de medio.
     * 
     * @param valor El nuevo valor a fijar
     */
    public void setValor(V valor) {
        this.valor = valor;
    }
    
    /**
     * Devuelve el tipo de clase del valor del medio o 'null' si éste no está
     * establecido
     * 
     * @return Clase del valor almacenado (o null)
     */
    public Class getClassTipo() {
        if ( this.valor == null )
            return null;
        
        return this.valor.getClass();
    }

    /**
     * Dos valores de campo se consideran iguales si su nombre es el mismo, 
     * sin importar el valor, de esta forma las colecciones Set impedirán
     * tener dos ValoresCampo con mismo nombre y valores distintos, lo cual
     * no tiene sentido. 
     * 
     * @param obj Objeto a comparar con ésta instancia
     * @return True si los objetos son necesarios
     */
    @Override
    public boolean equals(Object obj) {
        AtributoMedio c;
        
        if ( obj == null )
            return super.equals(obj);
        
        if (  !( obj instanceof AtributoMedio ) )
            return super.equals(obj);
        
        c = (AtributoMedio) obj;
        
        return c.getNombre().equals(getNombre());
    }

    /**
     * Código autogenerado para el correcto funcionamiento de equals()
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.nombre);
        hash = 41 * hash + Objects.hashCode(this.valor);
        return hash;
    }
    
    
}
