package ma.youcode.lineperm.service;

import ma.youcode.lineperm.model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserService {

    private final Map<String, User> users = new HashMap<>();
    private final Path usersFile = Path.of("src/main/resources/users.txt");

    public UserService() {
        loadUsers();
    }


    private void loadUsers() {

        try {
            List<String> lines = Files.readAllLines(usersFile);
            
            for (String line : lines) {
                if (line.trim().isEmpty()) {
                     continue;
                }
                
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    User user = new User(parts[0], parts[1]);
                    users.put(parts[0],user);
                }
            }
        } catch (IOException e) {
            System.out.println("Erreur while loading users");
        }
    }

    
    private void saveUsers() {

        List<String> lines = new ArrayList<>();

        for (User user : users.values()) {
            lines.add(user.getLogin() + ":" + user.getPassword());
        }
        try {
            Files.write(usersFile, lines);
        } catch (IOException e) {
            System.out.println("Erreur while saving");
        }
    }


    public boolean signup(String login, String password) {

        if (login == null || login.trim().isEmpty() || login.contains(" ")) {
            return false;
        }
        if (password == null || password.trim().isEmpty()) {
            return false;
        }
        if (users.containsKey(login)) {
            return false; // already logged
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        User newUser = new User(login, hashedPassword);
        
        users.put(login, newUser);
        saveUsers();
        return true;
    }


    public boolean login(String login, String password) {
        if (login == null || password == null) {
            return false;
            }

        User user = users.get(login);
        if (user == null) {
            return false;
        }

        return BCrypt.checkpw(password, user.getPassword());
    }
}