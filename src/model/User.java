package model;
/**Clase User
 * Esta clase se encarga de manejar los atributos y metodos que tendran los usuarios
 */
public class User{
    //Atributos
    private String fullName; //Nombre completo
    private String id; //id usuario
    private static int countId = 1; // contador de los ids // estatico para que funcione de forma global
    private String username; // nombre de usuario == usuario unico
    private String password; // contraseña
    private Rol rol;
    


/**
 *  Crear un usuario con:
 * @param fullName nombre completo 
 * @param username nombre de usuario
 * @param password contraseña
 * @param rol rol del usuario
 */
    public User(String fullName, String username, String password,Rol rol) {
        this.fullName = fullName;
        this.id = "U0" + countId++;
        this.username = username;
        this.password = password;
        this.rol = rol;
    }

    //getters
    public String getFullName() {
        return fullName;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    public Rol getRol(){
        return rol;
    }

    //setters
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    


    

    

}