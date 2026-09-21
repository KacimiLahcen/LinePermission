package ma.youcode.lineperm.service;

import ma.youcode.lineperm.model.AccessLog;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LogAnalayzerService {

    private final List<AccessLog> logs = new ArrayList<>();
    private final Path logFilePath = Path.of("src/main/resources/access.log");

    public LogAnalayzerService() {
        loadLogs();
    }

    private void loadLogs() {

        try {
            List<String> lines = Files.readAllLines(logFilePath);
            for (String line : lines) {
                if (line.trim().isEmpty())
                    continue;

                String[] parts = line.split(";");

                if (parts.length == 6) {
                    AccessLog log = new AccessLog(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
                    logs.add(log);
                }

            }
        } catch (IOException e) {
            System.out.println("Erreur de chargement du fichier access.log");
        }
    }

    public long getTotalActions() {
        return logs.stream().count();
    }

    public long getRefusedAccessCount() {
        return logs.stream().filter(log -> log.getResult().equals("REFUSE")).count();
    }

    public List<String> getDistinctUsers() {
        return logs.stream().map(AccessLog::getUser).distinct().collect(Collectors.toList());
    }

    
}
