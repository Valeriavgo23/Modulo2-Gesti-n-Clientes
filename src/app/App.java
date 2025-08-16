package app;
import java.util.Scanner;

import model.Rol;
import model.User;
import service.UserManagament;

public class App {

    public static void main(String[] args) throws Exception {
        //Llamar al scanner
        Scanner entrada = new Scanner(System.in);
        /**
     * Aqui se instancia la clase UserManagament con la cantidad de usuarios que se podran crear
     */
        UserManagament gestorUsuarios = new UserManagament(20);
        userLogin(entrada,gestorUsuarios);
    }
    public static void userLogin(Scanner entrada, UserManagament gestorUsuarios) {
        //Se pregunta solo una vez si hay usuarios en el sistema
        System.out.print("¿Hay usuarios registrados en el sistema (1.NO, 2.SI) ?\n");
        System.out.print("Opción: ");
        int opcion = entrada.nextInt();

        /**
         * Si la respuesta es SI, envia al usuario al login si es NO, lo envia a crear al primer usuario
         */
        if (opcion == 1){
            System.out.println("--Creación del primer usuario Administrativo");
            entrada.nextLine(); //limpieza del buffers
            System.out.print("Digite su nombre completo: ");
            String fullName = entrada.nextLine();
            System.out.print("Digite un nombre de usuario: ");
            String user = entrada.nextLine();
            System.out.print("Digite su contraseña: ");
            String pass = entrada.nextLine();

            //Se crea el usuario en User
            User firtsUser = new User(fullName, user, pass, Rol.ADMINISTRATOR);
            //Se guarda en gestorUsuarios
            gestorUsuarios.createNewUser(firtsUser, Rol.ADMINISTRATOR, firtsUser);

            System.out.println("El primer usuario ha sido creado exitosamente");
            Menu(entrada, gestorUsuarios);
        }
        else{
            Menu(entrada, gestorUsuarios);
        }
    }

    /**
     * Metodo que maneja el menu (interfaz grafica)
     */
    public static void Menu(Scanner entrada, UserManagament gestorUsuarios) {
    int menu;
    User newUser = null; // newuUser declarado incialmente como null
    Rol roles = Rol.STANDARD; //roles declarados inicialmente como rol estandar
    do { //bucle
        System.out.println("..:--Menu Principal--:..");
        System.out.print("""
                1. Iniciar Sesión 
                0. Salir
                Opción: 
                """);
        menu = entrada.nextInt();
        entrada.nextLine();

        if (menu == 1) {
            // LOGIN
            System.out.print("👤 Usuario: ");
            String usernameIngresado = entrada.nextLine();
            System.out.print("🔑 Contraseña: ");
            String contrasenaIngresada = entrada.nextLine();

            User usuarioLogueado = gestorUsuarios.login(usernameIngresado, contrasenaIngresada); //valida al usuario con el meotod login
            if (usuarioLogueado == null) {
                System.out.println("Usuario o contraseña incorrectas, por favor vuelva a ingresar");
                continue;
            }

            // Submenú después de iniciar sesión
            int opcionMenu;
            do {
                System.out.print("""
                        ¿Qué desea hacer?
                        1. ➕ Crear nuevo usuario
                        2. ✏ Actualizar información
                        3. 🔍 Buscar usuario
                        4. 🗑 Eliminar usuario
                        5. 📜 Visualizar historial
                        6. Cerrar sesión
                        ➡  Opción: 
                        """);
                opcionMenu = entrada.nextInt();
                entrada.nextLine();

                switch (opcionMenu) {
                    case 1:
                        System.out.println("---Usted va a crear un nuevo usuario---");
                        System.out.print("Digite su nombre completo: ");
                        String newFullName = entrada.nextLine();
                    String usern;
                    do {System.out.print("Digite un nombre de usuario: ");
                        usern = entrada.nextLine();
                        if(gestorUsuarios.usernameExist(usern)){
                            System.out.println("Nombre de usuario en uso, por favor elija otro");
                        }
                        }
                    while (gestorUsuarios.usernameExist(usern)); 
                        System.out.print("Digite su contraseña: ");
                        String newPass = entrada.nextLine();
                        System.out.print("Rol del usuario: \n1. Administrativo \n2. Estandar\nRol: ");
                        int newRol = entrada.nextInt();
                        entrada.nextLine();

                        //si rol es igual a 1 guarda al usuario con el rol administrativo
                        if (newRol == 1) {
                            roles = Rol.ADMINISTRATOR;
                        } else if (newRol == 2) { // si es igual a 2 con el rol estandar
                            roles = Rol.STANDARD;
                        } else {//si no es ninguno de los, envia un mensaje de error y asigna un rol por defecto
                            System.out.println("Opción inválida. Asignando rol por defecto: Estandar");
                            roles = Rol.STANDARD;
                        }
                        newUser = new User(newFullName, usern, newPass, roles); //envia los datos 
                        if (newUser != null){
                        gestorUsuarios.createNewUser(newUser, usuarioLogueado.getRol(), usuarioLogueado); // crea al nuevo usuario
                        }
                    
                        break;

                    case 2:
                        System.out.print("--Actualizar información de un usuario--\n");
                        gestorUsuarios.updateUser(usuarioLogueado.getRol(),usuarioLogueado); //llama al metodo actualizar usuario
                        break;

                    case 3:
                        System.out.printf("--Buscar información de un usuario--\n");
                        gestorUsuarios.findUser(usuarioLogueado.getRol(),usuarioLogueado); //llama al metodo buscar usuario
                        break;

                    case 4:
                        System.out.printf("--Eliminar a un usuario--\n");
                    
                        gestorUsuarios.deleteUser(usuarioLogueado.getRol(),usuarioLogueado); //llama al metodo eliminar usuario
                        break;

                    case 5:
                        System.out.printf("--Visualizar historial--\n");
                        gestorUsuarios.showHistory(usuarioLogueado);//llama al metoodo mostrar historial
                        break;
                    

                    case 6:
                        System.out.println("Cerrando sesión...");//cierra sesión
                        break; 

                    default:
                        System.err.println("Opción no válida");
                        break;
                }
            } while (opcionMenu != 6); //repite todo el ciclo siempre y cuando la opción ingresada no sea 6

        } else if (menu != 0) {
            System.out.println("Opción no válida");
        }
    } while (menu != 0);

    System.out.println("Hasta pronto...");
}


}