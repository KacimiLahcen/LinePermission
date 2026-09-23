package ma.youcode.lineperm.ui;

import ma.youcode.lineperm.service.LogAnalayzerService;
import java.util.Scanner;

public class LogAnalyzerApp {

    private final LogAnalayzerService logService = new LogAnalayzerService();
    private final Scanner scanner = new Scanner(System.in);

    public void start() {
        boolean back = false;

        while (!back) {
            System.out.println("\n=== LogAnalyzer ===");
            System.out.println("1) Nombre total d'actions");
            System.out.println("2) Nombre d'accès refusés");
            System.out.println("3) Utilisateurs distincts");
            System.out.println("4) Actions par utilisateur");
            System.out.println("5) Top consulted file");
            System.out.println("0) Quitter");
            System.out.print("Choix: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    System.out.println("Total actions: " + logService.getTotalActions());
                    break;
                case "2":
                    System.out.println("Accès refusés: " + logService.getRefusedAccessCount());
                    break;
                case "3":
                    System.out.println("Utilisateurs: " + logService.getDistinctUsers());
                    break;
                case "4":
                    System.out.println( logService.getActionsPerUser());
                    break;
                case "5":
                    System.out.println("fichier le plus consultes : " + logService.getTopConsultedFile());
                    break;
                case "0":
                    back = true;
                    break;
                default:
                    System.out.println("Choix invalide.");
            }
        }
    }
}