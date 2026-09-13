package ma.youcode.lineperm.service;

import ma.youcode.lineperm.access.ControleAcces;
import ma.youcode.lineperm.model.FichierProtege;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileService {

    private final Map<String, FichierProtege> fichiers = new HashMap<>();
    private final Path permissionsPath = Path.of("data/permissions.txt");
    private final Path dataDir = Path.of("data");

    public FileService() {
        initStorage();
        loadPermissions();
    }

    private void initStorage() {
        try {
            if (!Files.exists(dataDir)) {
                Files.createDirectories(dataDir);
            }
            if (!Files.exists(permissionsPath)) {
                Files.createFile(permissionsPath);
            }
        } catch (IOException e) {
            System.out.println("Erreur d'initialisation du stockage.");
        }
    }

    private void loadPermissions() {
        if (!Files.exists(permissionsPath)) return;
        try {
            List<String> lines = Files.readAllLines(permissionsPath);
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(";");
                if (parts.length == 4) {
                    String nom = parts[0];
                    String prop = parts[1];
                    String blocProp = parts[2];
                    String blocOther = parts[3];

                    boolean propR = blocProp.contains("r");
                    boolean propW = blocProp.contains("w");
                    boolean propD = blocProp.contains("d");

                    boolean otherR = blocOther.contains("r");
                    boolean otherW = blocOther.contains("w");
                    boolean otherD = blocOther.contains("d");

                    FichierProtege f = new FichierProtege(nom, prop, propR, propW, propD, otherR, otherW, otherD);
                    fichiers.put(nom, f);
                }
            }
        } catch (IOException e) {
            System.out.println("Erreur de chargement des permissions.");
        }
    }

    private void savePermissions() {
        List<String> lines = new ArrayList<>();
        for (FichierProtege f : fichiers.values()) {
            String propRights = (f.isPropR() ? "r" : "-") + (f.isPropW() ? "w" : "-") + (f.isPropD() ? "d" : "-");
            String otherRights = (f.isOtherR() ? "r" : "-") + (f.isOtherW() ? "w" : "-") + (f.isOtherD() ? "d" : "-");
            lines.add(f.getNom() + ";" + f.getProprietaire() + ";" + propRights + ";" + otherRights);
        }
        try {
            Files.write(permissionsPath, lines);
        } catch (IOException e) {
            System.out.println("Erreur lors de la sauvegarde des permissions.");
        }
    }

    public List<FichierProtege> listerFichiers() {
        return new ArrayList<>(fichiers.values());
    }

    public boolean touch(String nomFichier, String currentLogin) {
        if (nomFichier.contains("/") || nomFichier.contains("\\")) {
            System.out.println("Erreur: Le nom du fichier ne doit pas contenir de chemin.");
            return false;
        }
        if (fichiers.containsKey(nomFichier)) {
            System.out.println("Erreur: Ce nom de fichier existe déjà.");
            return false;
        }

        FichierProtege nouveauFichier = new FichierProtege(nomFichier, currentLogin);
        fichiers.put(nomFichier, nouveauFichier);
        savePermissions();

        try {
            Path fileContentPath = dataDir.resolve(nomFichier);
            if (!Files.exists(fileContentPath)) {
                Files.createFile(fileContentPath);
            }
        } catch (IOException e) {
            System.out.println("Erreur de création du fichier sur le disque.");
        }

        System.out.println("Fichier '" + nomFichier + "' créé.");
        return true;
    }

    public String cat(String nomFichier, String currentLogin) {
        FichierProtege f = fichiers.get(nomFichier);
        if (f == null) return "Fichier inexistant.";

        if (!ControleAcces.estAutorise(currentLogin, f, 'r')) {
            return "Permission denied.";
        }

        try {
            Path filePath = dataDir.resolve(nomFichier);
            if (!Files.exists(filePath)) return "(fichier vide)";
            String content = Files.readString(filePath);
            return content.isEmpty() ? "(fichier vide)" : content;
        } catch (IOException e) {
            return "Erreur lors de la lecture du fichier.";
        }
    }

    public boolean peutEditer(String nomFichier, String currentLogin) {
        FichierProtege f = fichiers.get(nomFichier);
        if (f == null) {
            System.out.println("Fichier inexistant.");
            return false;
        }
        if (!ControleAcces.estAutorise(currentLogin, f, 'w')) {
            System.out.println("Permission denied.");
            return false;
        }
        return true;
    }

    public String getContentForNano(String nomFichier, String currentLogin) {
        FichierProtege f = fichiers.get(nomFichier);
        if (f == null) return "";

        if (!ControleAcces.estAutorise(currentLogin, f, 'r')) {
            return "(contenu masqué - pas de droit de lecture)";
        }

        try {
            Path filePath = dataDir.resolve(nomFichier);
            if (!Files.exists(filePath)) return "(fichier vide)";
            String content = Files.readString(filePath);
            return content.isEmpty() ? "(fichier vide)" : content;
        } catch (IOException e) {
            return "";
        }
    }

    public void saveContent(String nomFichier, String newContent) {
        try {
            Path filePath = dataDir.resolve(nomFichier);
            Files.writeString(filePath, newContent);
        } catch (IOException e) {
            System.out.println("Erreur de sauvegarde du contenu.");
        }
    }

    public void chmod(String option, String nomFichier, String currentLogin) {
        FichierProtege f = fichiers.get(nomFichier);
        if (f == null) {
            System.out.println("Fichier inexistant.");
            return;
        }

        if (!currentLogin.equals(f.getProprietaire())) {
            System.out.println("Permission denied.");
            return;
        }

        boolean add = !option.startsWith("-");
        char droit = option.replace("-", "").replace("+", "").charAt(0);

        String oldFormat = f.getPermissionsFormat();

        switch (droit) {
            case 'r': f.setOtherR(add); break;
            case 'w': f.setOtherW(add); break;
            case 'd': f.setOtherD(add); break;
            default:
                System.out.println("Droit invalide.");
                return;
        }

        savePermissions();
        System.out.println(nomFichier + ": " + oldFormat + " -> " + f.getPermissionsFormat());
    }
}