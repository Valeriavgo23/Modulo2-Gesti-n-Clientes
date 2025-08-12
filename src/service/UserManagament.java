package service;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import model.ActionHistory;
import model.Rol;
import model.User;

public class UserManagament {
    private ActionHistory[] actionHistories = new ActionHistory[100];
    private int historyCount = 0;

    private User[] users;
    private Integer countUsers = 0;

    public UserManagament(Integer maxUsers) {
        this.users = new User[maxUsers];
    }

    public ActionHistory[] getActionHistories() {
        return actionHistories;
    }

    // Método 1: Crear nuevos usuarios
    public void createNewUser(User newFullName, Rol roles, User currentUser) {
        if (roles.equals(Rol.ADMINISTRATOR)) {
            if (countUsers < users.length) {
                users[countUsers] = newFullName;
                countUsers++;
                System.out.println("Usuario creado correctamente");

                if (historyCount < actionHistories.length) {
                    String description = "El administrador " + currentUser.getFullName() +
                            " ha creado un nuevo usuario: " + newFullName.getFullName();
                    ActionHistory newLog = new ActionHistory(
                            description,
                            new User(currentUser.getId(),
                                    currentUser.getFullName(),
                                    currentUser.getUsername(),
                                    currentUser.getPassword(),
                                    currentUser.getRol()
                            )
                    );
                    actionHistories[historyCount++] = newLog;
                } else {
                    System.out.println("El historial de acciones está lleno.");
                }
            } else {
                System.out.println("Array de usuarios lleno");
            }
        } else {
            System.out.println("Acceso Denegado");
        }
    }

    // Método 2: Buscar usuario por ID o username
    public void findUser(Rol roles, User currentUser) {
        Scanner entrada = new Scanner(System.in);
        if (roles.equals(Rol.ADMINISTRATOR) || roles.equals(Rol.STANDARD)) {
            entrada.nextLine();
            System.out.print("Ingrese el nombre de usuario: ");
            var findName = entrada.nextLine();

            boolean found = false;

            for (int i = 0; i < users.length; i++) {
                if (users[i] != null && findName.equalsIgnoreCase(users[i].getUsername())) {
                    System.out.println("Usuario encontrado:");
                    System.out.printf("""
                            Nombre Completo: %s
                            ID: %s
                            Username: %s
                            """, users[i].getFullName(), users[i].getId(), users[i].getUsername());
                    found = true;
                    break;
                }
            }

            if (currentUser != null && historyCount < actionHistories.length && roles.equals(Rol.ADMINISTRATOR)) {
                String description = "El administrador " + currentUser.getFullName() + " realizó una búsqueda del usuario: " + findName;
                ActionHistory newLog = new ActionHistory(
                        description,
                        new User(currentUser.getId(),
                                currentUser.getFullName(),
                                currentUser.getUsername(),
                                currentUser.getPassword(),
                                currentUser.getRol()
                        )
                );
                actionHistories[historyCount++] = newLog;
            } else if (currentUser == null) {
                System.err.println("Error no hay un usuario actual registrado");
            } else if (historyCount >= actionHistories.length) {
                System.out.println("El historial de acciones está lleno");
            }

            if (!found) {
                System.out.println("Usuario no encontrado");
            }
        } else {
            System.out.println("Usted no tiene permisos para utilizar esta función");
        }
    }

    // Método 3: Actualizar usuario
    public void updateUser(Rol currentUserRol, User currentUser) {
        Scanner entrada = new Scanner(System.in);

        System.out.println("Ingrese el nombre completo: ");
        var updateName = entrada.nextLine();

        System.out.println("Ingrese la contraseña: ");
        var updatePassword = entrada.nextLine();

        int userIndex = -1;

        for (int i = 0; i < users.length; i++) {
            if (users[i] != null &&
                    updateName.equalsIgnoreCase(users[i].getFullName()) &&
                    updatePassword.equalsIgnoreCase(users[i].getPassword())) {
                userIndex = i;
                break;
            }
        }

        if (userIndex == -1) {
            System.out.println("Error: no se encontró un usuario con ese nombre y contraseña");
            return;
        }

        User userToUpdate = users[userIndex];

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

        System.out.println("Ingrese el nuevo nombre completo: ");
        var newName = entrada.nextLine();

        System.out.println("Ingrese la nueva contraseña: ");
        var newPassword = entrada.nextLine();

        userToUpdate.setFullName(newName);
        userToUpdate.setPassword(newPassword);

        System.out.println("Datos actualizados correctamente");

        if (currentUser != null) {
            String description;
            if (currentUserRol.equals(Rol.ADMINISTRATOR)) {
                description = "El administrador " + currentUser.getFullName() + " ha actualizado al usuario " + newName;
            } else {
                description = "El usuario estandar " + currentUser.getFullName() + " ha actualizado su propio perfil";
            }

            ActionHistory newLog = new ActionHistory(description,
                    new User(currentUser.getId(),
                            currentUser.getFullName(),
                            currentUser.getUsername(),
                            currentUser.getPassword(),
                            currentUser.getRol()));

            if (historyCount < actionHistories.length) {
                actionHistories[historyCount++] = newLog;
            } else {
                System.out.println("Historial lleno");
            }
        } else {
            System.err.println("Error: no hay un usuario actual registrado");
        }
    }

    // Método 4: Eliminar usuario
    public void deleteUser(Rol rolUser, User currentUser) {
        Scanner entrada = new Scanner(System.in);
        if (rolUser.equals(Rol.ADMINISTRATOR)) {
            System.out.print("Digite el nombre del usuario a eliminar: ");
            var deleteName = entrada.nextLine();

            boolean found = false;
            for (int i = 0; i < users.length; i++) {
                if (users[i] != null && deleteName.equalsIgnoreCase(users[i].getFullName())) {
                    users[i] = null;
                    countUsers--; // Decrementar el contador de usuarios
                    found = true;
                    break;
                }
            }
            if (found) {
                System.out.println("Usuario '" + deleteName + "' ha sido eliminado.");
                // Logica del historial, simplificada
                if (historyCount < actionHistories.length) {
                    String description = "El administrador " + currentUser.getFullName() + " ha eliminado al usuario " + deleteName;
                    ActionHistory newLog = new ActionHistory(
                            description,
                            new User(currentUser.getId(),
                                    currentUser.getFullName(),
                                    currentUser.getUsername(),
                                    currentUser.getPassword(),
                                    currentUser.getRol()
                            )
                    );
                    actionHistories[historyCount++] = newLog;
                } else {
                    System.out.println("El historial de acciones está lleno.");
                }
            } else {
                System.out.println("Usuario '" + deleteName + "' no encontrado.");
            }
        } else {
            System.out.println("Usted no tiene permisos para realizar esta acción.");
        }
    }

    // Método para ver todos los usuarios existentes
    public void showUser() {
        for (User u : users) {
            if (u != null) {
                System.out.printf("--%s (username: %s)\n ", u.getFullName(), u.getUsername());
            }
        }
    }

    // Método para el login del usuario
    public User login(String username, String password) {
        for (int i = 0; i < users.length; i++) {
            if (users[i] != null &&
                    users[i].getUsername().equals(username) &&
                    users[i].getPassword().equals(password)) {
                System.out.printf("Bienvenido %s, es un gusto verte de nuevo.%n", users[i].getFullName());
                return users[i];
            }
        }
        System.out.println("❌ Usuario o contraseña incorrectos.");
        return null;
    }

    // Método para mostrar el historial del usuario actual
    public void showHistory(User user) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        boolean found = false;
        System.out.println("--- Historial de acciones de " + user.getFullName() + " ---");
        for (int i = 0; i < historyCount; i++) {
            ActionHistory ah = actionHistories[i];
            if (ah != null && ah.getPerformigUser().getId().equals(user.getId())) {
                String formattedDate = sdf.format(new Date(ah.getTime()));
                System.out.printf("%s - %s\n", formattedDate, ah.getDescription());
                found = true;
            }
        }
        if (!found) {
            System.out.println("No hay acciones para mostrar");
        }
    }

    // Nuevo método para que el administrador pueda ver todo el historial
    public void showAllHistory(User currentUser) {
        if (currentUser.getRol().equals(Rol.ADMINISTRATOR)) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            System.out.println("--- Historial de todas las acciones del sistema ---");
            if (historyCount == 0) {
                System.out.println("El historial está vacío.");
                return;
            }
            for (int i = 0; i < historyCount; i++) {
                ActionHistory ah = actionHistories[i];
                if (ah != null) {
                    String formattedDate = sdf.format(new Date(ah.getTime()));
                    System.out.printf("%s - Usuario: %s - Acción: %s\n",
                            formattedDate, ah.getPerformigUser().getFullName(), ah.getDescription());
                }
            }
        } else {
            System.out.println("Acceso denegado. Esta función es solo para administradores.");
        }
    }

    // Método para comprobar que el nombre de usuario sea único
    public boolean usernameExist(String username) {
        for (int i = 0; i < countUsers; i++) {
            if (users[i] != null && users[i].getUsername().equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }
}