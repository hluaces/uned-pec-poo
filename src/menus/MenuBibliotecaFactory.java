package menus;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import usuarios.EnumPermisos;
import usuarios.Usuario;

/**
 * Clase estática que implementa un patrón factoría para crear objetos de 
 * {@link MenuBiblioteca} acorde a los permisos de un usuario.
 * 
 * @author Héctor Luaces Novo
 */
public final class MenuBibliotecaFactory {
    /**
     * @see MenuBibliotecaFactory#getMenuUusario(usuarios.Usuario) 
     */
    private MenuBibliotecaFactory() {
    }
    
    /**
     * Dado un {@link Usuario} genera un nuevo menu de la biblioteca con
     * las opciones que permiten el {@link Perfil} de dicho usuario.
     * 
     * @param u Usuario para el que queremos generar el menú
     * @return Menú generado para dicho usuario
     */
    public static List<MenuBiblioteca> getMenuUusario(Usuario u) {
        Set<EnumPermisos> permisos = u.getPerfil().getPermisos();
        List<MenuBiblioteca> menus = new ArrayList();
        MenuBiblioteca m;
        
        m = new MenuBiblioteca("Biblioteca");
        
        m.addOpcion(new OpcionMenu(EnumAccionesMenu.MENSAJES));
        
        // Menú principal de usuario
        if ( permisos.contains(EnumPermisos.MEDIOS_VER) )
            m.addOpcion(new OpcionMenu(EnumAccionesMenu.MEDIOS_VER));

        if ( permisos.contains(EnumPermisos.BUSCAR) ) {
            m.addOpcion(new OpcionMenu(EnumAccionesMenu.MEDIOS_BUSCAR));
            m.addOpcion(new OpcionMenu(EnumAccionesMenu.MEDIOSC_BUSCAR_CRUZADOS));
        }

        if ( permisos.contains(EnumPermisos.SUSCRIPCIONES) )
            m.addOpcion(new OpcionMenu(EnumAccionesMenu.SUSCRIPCIONES));

        if ( permisos.contains(EnumPermisos.MULTAS) )
            m.addOpcion(new OpcionMenu(EnumAccionesMenu.MULTAS_VER));
        
        menus.add(m);
        
        if ( permisos.contains(EnumPermisos.PRESTAMO) ) {
            m = new MenuBiblioteca("Mis préstamos");
            
            m.addOpcion(new OpcionMenu(EnumAccionesMenu.PRESTAMOS_SOLICITAR));
            m.addOpcion(new OpcionMenu(EnumAccionesMenu.PRESTAMOS_HISTORIAL));
            m.addOpcion(new OpcionMenu(EnumAccionesMenu.PRESTAMOS_VER));
            m.addOpcion(new OpcionMenu(EnumAccionesMenu.PRESTAMOS_DEVOLVER));

            menus.add(m);            
        }
        

        if ( permisos.contains(EnumPermisos.RESERVAR) ) {
            m = new MenuBiblioteca("Mis reservas");
            
            m.addOpcion(new OpcionMenu(EnumAccionesMenu.RESERVAS_VER));
            m.addOpcion(new OpcionMenu(EnumAccionesMenu.RESERVAS_ANULAR));
            
            menus.add(m);
            
        }
        if ( permisos.contains(EnumPermisos.GESTION_MEDIOS) ) {
            m = new MenuBiblioteca("Medios");
        
            m.addOpcion(new OpcionMenu(EnumAccionesMenu.GESTION_MEDIOS));
            m.addOpcion(new OpcionMenu(EnumAccionesMenu.GESTION_MEDIOS_ALTA));

            menus.add(m);
        }
        
        if ( permisos.contains(EnumPermisos.GESTION_PRESTAMOS) ) {
            m = new MenuBiblioteca("Préstamos");
            
            m.addOpcion(new OpcionMenu(EnumAccionesMenu.GESTION_PRESTAMOS_VER));
            m.addOpcion(new OpcionMenu(EnumAccionesMenu.GESTION_PRESTAMOS_VER_FUERA_PLAZO));
            
            menus.add(m);
        }
        
        if ( permisos.contains(EnumPermisos.GESTION_RESERVAS) ) {
            m = new MenuBiblioteca("Reservas");
            
            m.addOpcion(new OpcionMenu(EnumAccionesMenu.GESTION_RESERVAS_VER));
            menus.add(m);
        }
        
        if ( permisos.contains(EnumPermisos.VER_USUARIOS) ) {
            m = new MenuBiblioteca("Usuarios");
            
            m.addOpcion(new OpcionMenu(EnumAccionesMenu.GESTION_USUARIOS));
            m.addOpcion(new OpcionMenu(EnumAccionesMenu.GESTION_USUARIOS_ALTA));
            
            if ( permisos.contains(EnumPermisos.GESTION_TARJETAS) )
                m.addOpcion(
                    new OpcionMenu(EnumAccionesMenu.GESTION_USUARIOS_TARJETAS)
                );
            
            menus.add(m);
        }
        return menus;
    }
}

