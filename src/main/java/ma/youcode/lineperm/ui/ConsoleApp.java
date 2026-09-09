package ma.youcode.lineperm.ui;

import ma.youcode.lineperm.service.UserService;
import java.util.Scanner;

public class ConsoleApp {

    private final UserService userService = new UserService();
    private String loggedUser = null;

    public void start() {
        System.out.println("=================================================");
        System.out.println("    LinePerm : gestion de fichiers & droits");
        System.out.println("=================================================");
        System.out.println("Non connecté. Commandes : signup | login | help | exit ");

        Scanner scanner = new Scanner(System.in);

        while (true) {

            if (loggedUser == null) {
                System.out.print("linperm> ");
            } else {
                System.out.print(loggedUser + "@linperm> ");
            }

            String choix = scanner.nextLine().trim();
            if (choix.isEmpty()) continue;

            if (choix.equals("exit")) {
                System.out.println("Au revoir.");
                break;
            }


            switch (choix) {
                case "signup":
                    handleSignup(scanner);
                    break;
                case "login":
                    handleLogin(scanner);
                    break;
                case "logout":
                        System.out.println("Au revoir.");
                    handleLogout();
                    break;
                case "help":
                    System.out.println("Commandes disponibles: signup, login, logout, help, exit");
                    break;
                default:
                    System.out.println("Commande inconnue.");
            }

        }
    }

    private void handleSignup(Scanner scanner) {
        if (loggedUser != null) {
            System.out.println("Already logged in, log-out first");
            return;
        }

        System.out.print("Login: ");
        String login = scanner.nextLine();
        System.out.print("Mot de passe: ");
        String password = scanner.nextLine();

        if (userService.signup(login, password)) {
            System.out.println("Account created with success.");
        } else {
            System.out.println("Erreur: Login invalide, contient des espaces ou existe deja.");
        }
    }

    private void handleLogin(Scanner scanner) {
        if (loggedUser != null) {
            System.out.println("Already connected");
            return;
        }

        System.out.print("Login: ");
        String login = scanner.nextLine();
        System.out.print("Mot de passe: ");
        String password = scanner.nextLine();

        if (userService.login(login, password)) {
            loggedUser = login;
            System.out.println("Bienvenue " + loggedUser + "!");
        } else {
          
            System.out.println("Login ou mot de passe incorrect.");
        }
    }

    private void handleLogout() {
        if (loggedUser == null) {
            System.out.println("Aucune session active.");
        } else {
            loggedUser = null;
            System.out.println("Deconnecte");
        }
    }
}