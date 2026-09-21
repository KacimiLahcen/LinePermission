package ma.youcode.lineperm.ui;

import ma.youcode.lineperm.model.FichierProtege;
import ma.youcode.lineperm.service.FileService;
import ma.youcode.lineperm.service.UserService;

import java.util.List;
import java.util.Scanner;

public class ConsoleApp {

    private final UserService userService = new UserService();
    private final FileService fileService = new FileService();

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
            if (choix.isEmpty())
                continue;

            if (choix.equals("exit")) {
                System.out.println("Au revoir.");
                break;
            }

            String[] parts = choix.split("\\s+");
            String command = parts[0];

            switch (command) {
                case "signup":
                    handleSignup(scanner);
                    break;
                case "login":
                    handleLogin(scanner);
                    break;
                case "logout":
                    handleLogout();
                    break;
                case "help":
                    if (loggedUser == null) {
                        System.out.println("Commandes disponibles: signup, login, help, exit");
                    } else {
                        System.out.println("Commandes disponibles: stats, ls -l, touch, cat, nano, chmod, help, logout, exit");
                    }
                    break;

                case "ls":
                    if (loggedUser == null) {
                        System.out.println("Commande inconnue. Connecte-toi d'abord.");
                    } else if (parts.length > 1 && parts[1].equals("-l")) {
                        handleLs();
                    } else {
                        System.out.println("Usage: ls -l");
                    }
                    break;

                case "touch":
                    if (loggedUser == null) {
                        System.out.println("Commande inconnue. Connecte-toi d'abord.");
                    } else if (parts.length == 2) {
                        fileService.touch(parts[1], loggedUser);
                    } else {
                        System.out.println("Usage: touch <filename>");
                    }
                    break;

                case "cat":
                    if (loggedUser == null) {
                        System.out.println("Commande inconnue. Connecte-toi d'abord.");
                    } else if (parts.length == 2) {
                        System.out.println(fileService.cat(parts[1], loggedUser));
                    } else {
                        System.out.println("Usage: cat <filename>");
                    }
                    break;

                case "nano":
                    if (loggedUser == null) {
                        System.out.println("Commande inconnue. Connecte-toi d'abord.");
                    } else if (parts.length == 2) {
                        handleNano(scanner, parts[1]);
                    } else {
                        System.out.println("Usage: nano <filename>");
                    }
                    break;

                case "stats":
                    LogAnalyzerApp logApp = new LogAnalyzerApp();
                    logApp.start();
                    break;

                case "chmod":
                    if (loggedUser == null) {
                        System.out.println("Commande inconnue. Connecte-toi d'abord.");
                    } else if (parts.length == 3) {
                        fileService.chmod(parts[1], parts[2], loggedUser);
                    } else {
                        System.out.println("Usage: chmod <+r|+w|+d|-r|-w|-d> <filename>");
                    }
                    break;

                default:
                    System.out.println("Commande inconnue. Tape 'help'.");
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

    private void handleLs() {
        List<FichierProtege> liste = fileService.listerFichiers();
        for (FichierProtege f : liste) {
            System.out.println(f.getPermissionsFormat() + " " + f.getProprietaire() + " " + f.getNom());
        }
    }

    private void handleNano(Scanner scanner, String nomFichier) {
        if (!fileService.peutEditer(nomFichier, loggedUser)) {
            return;
        }

        System.out.println("Mode edition");
        System.out.println(fileService.getContentForNano(nomFichier, loggedUser));
        System.out.println(nomFichier);
        System.out.println("--- Saisis ton texte. Tape EOF seul sur une ligne pour enregistrer.");

        StringBuilder sb = new StringBuilder();
        int linesCount = 0;
        while (true) {
            String line = scanner.nextLine();
            if (line.equals("EOF"))
                break;
            if (sb.length() > 0)
                sb.append("\n");
            sb.append(line);
            linesCount++;
        }

        fileService.saveContent(nomFichier, sb.toString());
        System.out.println("Fichier '" + nomFichier + "' enregistré (" + linesCount + " ligne"
                + (linesCount > 1 ? "s" : "") + ").");
    }
}