package service;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import model.ActionHistory;
import model.Rol;
import model.User;

public class UserManagament {
    /**Esta claase se encarga de manejar todos los metodos o acciones que puede realizar un usuario dentro del sistema
     * Atributos privados
     */
    private ActionHistory[] actionHistories = new ActionHistory[100]; // Es un arreglo que guarda un maximo de 100 registros de acciones (ActionHistory)
    private int historyCount = 0; //Contador para manejar la cantidad de acciones que se van creando

    private User[] users; // creo un array vacio de la clase User
    private Integer countUsers = 0; // Contador para manejar la cantidad de usuarios que se van creando

    // Constructor
    /**
     * Crear un arreglo vacio de tipo User con el tamaño indicado por maxUsers
     * @param maxUsers //cantidad maxima de usuarios
     */
    public UserManagament(Integer maxUsers) {
        this.users = new User[maxUsers];
    }

    //getter
    /**
     * Este metodo devuelve el arreglo actionHistories de la clase
     */
    public ActionHistory[] getActionHistories() {
        return actionHistories;
    }

    // Método 1: Crear nuevos usuarios
    public void createNewUser(User newUsuario, Rol roles, User currentUser) {
    // Solo permitir si el que ejecuta es administrador
    if (roles.equals(Rol.ADMINISTRATOR)) {

        // Verificar espacio para usuarios nuevos
        if (countUsers  < users.length) {
            users[countUsers] = newUsuario;
            countUsers++;
            if(currentUser != null)
            System.out.println("Usuario creado correctamente");


            // ---- Registro en historial ----
            if (currentUser != null && currentUser.getRol() == Rol.ADMINISTRATOR) {
                
                if (historyCount < actionHistories.length) { //si hay espacio me guarda un nuevo historial
                    String description = "El administrador " + currentUser.getFullName() +
                                        " creó al usuario: " + newUsuario.getFullName();

                    ActionHistory newLog = new ActionHistory(description, currentUser);

                    System.out.println("Guardando en historial en posición: " + historyCount); // <-- Debug
                    actionHistories[historyCount] = newLog;
                    historyCount++; // Avanzar para que no se sobrescriba el anterior

                } else {
                    System.out.println("El historial de acciones está lleno");
                }

            } else if (currentUser == null) {//Si el usuario logueado es igual a null, es decir esta vacio me muestra un error
                System.err.println("Error: no hay un usuario logueado, no se registrará en historial");
            }

        } else {
            System.out.println("No hay espacio para más usuarios");
        }


    } else {
        System.out.println("Acceso denegado: solo administradores pueden crear usuarios");
    }
}

    // Método 2: Buscar usuario por ID o username
    public void findUser(Rol roles, User currentUser) {
        Scanner entrada = new Scanner(System.in);
        if (roles.equals(Rol.ADMINISTRATOR) || roles.equals(Rol.STANDARD)) { //Con esta condición indicio que tanto el administrador como el Estandar pueden utilizar este metodo

            entrada.nextLine();

            System.out.print("Ingrese el nombre de usuario: ");
            var findName = entrada.nextLine(); //Pregunto el nombre de usuario de la persona a buscar

            boolean found = false; //Como aun no se ha encontrado el usuario guardo el valor de false en la variables found


            for (int i = 0; i < users.length; i++) { //recorro cada usuario hasta encontrar al usuario
                if (users[i] != null && findName.equalsIgnoreCase(users[i].getUsername())) {

                    //Aqui muestro la información
                    System.out.println("Usuario encontrado:");
                    System.out.printf("""
                            Nombre Completo: %s
                            ID: %s
                            Username: %s
                            """, users[i].getFullName(), users[i].getId(), users[i].getUsername());
                    found = true; // cambio false a true porque se encontro al usuario
                
                }
            }
            //Aqui se valida el historial, si cumple con todas las condiciones, me guarda la acción
            if (currentUser != null && historyCount < actionHistories.length && roles != null && roles.equals(Rol.ADMINISTRATOR)) {
                    String description = "El administrador " + currentUser.getFullName() +
                            " realizó una búsqueda del usuario: " + findName;
                            //Aqui se crea un objeto de tipo ActionHistory llamado newLog y le paso, la descripción y sus datos personales
                ActionHistory newLog = new ActionHistory(
                description,
                new User(currentUser.getId(),
                currentUser.getFullName(),
                currentUser.getUsername(),
                currentUser.getPassword(),
                currentUser.getRol()
                )
            );
                    actionHistories[historyCount] = newLog;//Aqui se inserta el nuevo registro, en la posición historyCount del arreglo
                    historyCount++;
                } else if(currentUser == null){ //Si el usuario actual es igual a nulo es decir no existe, lanza un error
                    System.err.println("Error no hay un usuario actual registrado");
                }
                else if (historyCount >= actionHistories.length) { // si el contador de acciones es mayor o igual a la cantidad de acciones permitidas, envia un error
                System.out.println("El historial de acciones está lleno");
            }

        if (!found) { //si no encuentra al usuario
                System.out.println("Usuario no encontrado");
            }

        } else {
            System.out.println("Usted no tiene permisos para utilizar esta función");
        }
    }

    // Método 3: Actualizar usuario
    public void updateUser(Rol currentUserRol, User currentUser) {
        Scanner entrada = new Scanner(System.in);

        //Se piden los datos como el nombre y contraseña, para validar que ese usuario exista
        System.out.println("Ingrese el nombre completo: ");
        var updateName = entrada.nextLine();

        System.out.println("Ingrese la contraseña: ");
        var updatePassword = entrada.nextLine();

        int userIndex = -1; //defino la posicion del usuario actual

        for (int i = 0; i < users.length; i++) { // recorro cada usuario hasta encontrarlo
            if (users[i] != null &&
                    updateName.equalsIgnoreCase(users[i].getFullName()) &&
                    updatePassword.equalsIgnoreCase(users[i].getPassword())) { // si cumple con estas condiciones, lo guarda en la posicion i
                    userIndex = i;
                break; // se termina el ciclo
            }
        }

        if (userIndex == -1) { // si el usuario es igual a la posición envia un error
            System.out.println("Error: no se encontró un usuario con ese nombre y contraseña");
            return;
        }

        /**
         * Obtiene el usuario encontrado en la posición  "userIndex" del array  "users"
         * Esto No crea un nuevo usuario, sino que guarda una referencia al existente
         * para poder acceder y modificar sus datos mas facilmente
         */
        User userToUpdate = users[userIndex]; 
        
        /**
         * Solo los usuarios de tipos Administrador pueden actualizar cualquirer perfil
         * Aqui se crea una condición si usuario es del rol administrativo, puede actualizar cualquier perfil
         * si no, envia un error
         */
        if (currentUserRol.equals(Rol.ADMINISTRATOR)) {
            System.out.println("Permiso concedido: Administrador");
        } else if (currentUserRol.equals(Rol.STANDARD)) {
            if (!userToUpdate.equals(currentUser)) {
                System.out.println("Permiso denegado: Los usuarios estándar solo pueden actualizar su propio perfil.");
                return;
            }
            System.out.println("Permiso concedido: Usuario estándar actualizando su perfil");
        } else {
            System.out.println("Permiso denegado: Rol no válido");
            return;
        }

        //Se actualizan los nuevos datos
        System.out.println("Ingrese el nuevo nombre completo: ");
        var newName = entrada.nextLine();

        System.out.println("Ingrese la nueva contraseña: ");
        var newPassword = entrada.nextLine();

        userToUpdate.setFullName(newName);
        userToUpdate.setPassword(newPassword);

        System.out.println("Datos actualizados correctamente");

        //logica del historial
        if (currentUser != null && historyCount < actionHistories.length && currentUserRol!= null && currentUserRol.equals(Rol.ADMINISTRATOR)) {
                    String description = "El administrador " + currentUser.getFullName() +
                            " actualizo al usuario: " + updateName;
                ActionHistory newLog = new ActionHistory(
                description,
                new User(currentUser.getId(),
                currentUser.getFullName(),
                currentUser.getUsername(),
                currentUser.getPassword(),
                currentUser.getRol()
                )
            );
                    actionHistories[historyCount] = newLog;
                    historyCount++;
                } else if(currentUser == null){ //Si el usuario actual es igual a nulo es decir no existe, lanza un error
                    System.err.println("Error no hay un usuario actual registrado");
                }
                else if (historyCount >= actionHistories.length) { // si el contador de acciones es mayor o igual a la cantidad de acciones permitidas, envia un error
                System.out.println("El historial de acciones está lleno");
            }
    
        

    }

    // Método 4: Eliminar usuario
    public void deleteUser(Rol rolUser, User currentUser) {
        Scanner entrada = new Scanner(System.in);

        if (rolUser.equals(Rol.ADMINISTRATOR)) {
            System.out.print("Digite el nombre del usuario a eliminar: ");
            var deleteName = entrada.nextLine();

            boolean found = false; //se definide found en false inicialmente antes de buscar al usuario

            for (int i = 0; i < users.length; i++) { // se recorre cada usuario
                if (users[i] != null && deleteName.equalsIgnoreCase(users[i].getFullName())) {// si lo encuentra y tienen el mismo nombre lo elimina
                    users[i] = null;
                    found = true; // se cambia found a true porque ya encontro al usuario
                    break; //terminae el ciclo
                }
            }

            if (found) {
                System.out.println("Usuario '" + deleteName + "' ha sido eliminado."); // envia un mensaje si lo encuentra

            //logica de las acciones
            if (currentUser != null && historyCount < actionHistories.length && rolUser!= null && rolUser.equals(Rol.ADMINISTRATOR)) {
                    String description = "El administrador " + currentUser.getFullName() +
                            " elimino al usuario: " + deleteName;
                ActionHistory newLog = new ActionHistory(
                description,
                new User(currentUser.getId(),
                currentUser.getFullName(),
                currentUser.getUsername(),
                currentUser.getPassword(),
                currentUser.getRol()
                )
            );
                    actionHistories[historyCount] = newLog;
                    historyCount++;
                } else if(currentUser == null){ //Si el usuario actual es igual a nulo es decir no existe, lanza un error
                    System.err.println("Error no hay un usuario actual registrado");
                }
                else if (historyCount >= actionHistories.length) { // si el contador de acciones es mayor o igual a la cantidad de acciones permitidas, envia un error
                System.out.println("El historial de acciones está lleno");
            }

        }if(!found){//si es diferente a found, me muestra un mensaje
            System.out.println("Usuario no encontrado");
        }
            
            
        }
    }
        



    //Metodo para ver todos los usuarios existentes
    public void showUser(){
        for(User u : users){
            if(u != null){
                System.out.printf("--%s (username: %s)\n ", u.getFullName(), u.getUsername());
            }
        }
    }

    //metodo para el login del usuario
    public User login(String username, String password) { //creo dos paratmetros, el nombre de usuario y la contraseña
    for (int i = 0; i < users.length; i++) { // recorro cada usuario hasta encontrarlo
        if (users[i] != null &&
            users[i].getUsername().equals(username) &&
            users[i].getPassword().equals(password)) { 

            System.out.printf("Bienvenido %s, es un gusto verte de nuevo.%n", users[i].getFullName());
            return users[i]; // Retorna el usuario encontrado
        }
    }
    System.out.println("❌ Usuario o contraseña incorrectos.");
    return null; // Si no se encontró
}

//Metodo para mostrar el historial
/**
 * Muestra el historial de acciones
 */
public void showHistory(User user){
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    boolean found = false;
    for (int i = 0; i < historyCount; i++){
        ActionHistory ah = actionHistories[i];
        if (ah != null && ah.getPerformigUser().getId().equals(user.getId())){
            String formattedDate = sdf.format(new Date(ah.getTime()));
            System.out.printf("%s - %s\n", formattedDate, ah.getDescription());
            found = true;
        }
    }
    if (!found) {
        System.out.println("No hay acciones para mostrar");
    }
}
    /**
     * Metodo para comprobar que el nombre de usuario sea unico
     */
    public boolean usernameExist(String username){
        for(int i = 0; i < countUsers; i++){ //recorre el contador de usuarios
            if (users[i] != null && users[i].getUsername().equalsIgnoreCase(username)){ // Valida si hay dos usernames iguales
                return true; // devuelve true

            }
        }
        return false; // si no cumple con esa condición devuelve false
    }
}

