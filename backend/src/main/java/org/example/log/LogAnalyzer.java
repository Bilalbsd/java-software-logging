package org.example.log;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.example.builder.UserProfileBuilder;
import org.example.entity.UserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
@Service
public class LogAnalyzer {
    private static final Logger logger = LoggerFactory.getLogger(LogAnalyzer.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final Set<String> processedLogs = Collections.synchronizedSet(new HashSet<>());

    private final List<Map<String, Object>> readLogs = new ArrayList<>();

    private final List<Map<String, Object>> writeLogs = new ArrayList<>();

    private final List<Map<String, Object>> priceLogs = new ArrayList<>();

    public LogAnalyzer() throws IOException {
        parseLogs("logs/read.log", readLogs);
        parseLogs("logs/write.log", writeLogs);
        parseLogs("logs/mostExpensive.log", priceLogs);
    }

    private void parseLogs(String filePath, List<Map<String, Object>> logStorage) throws IOException {
        List<Map<String, Object>> parsedLogs = Files.lines(Paths.get(filePath)).map(this::parseJson).filter(Objects::nonNull).toList();
        logStorage.addAll(parsedLogs);
    }

    private Map<String, Object> parseJson(String jsonLine) {
        try {
            return OBJECT_MAPPER.readValue(jsonLine, new TypeReference<>() {});
        } catch (Exception e) {
            logger.error("Erreur lors du parsing JSON pour la ligne : {}", jsonLine, e);
            return null;
        }
    }

    public Map<String, UserProfile> analyzeUserActions() {
        Map<String, UserProfileBuilder> userProfiles = new HashMap<>();
        processLogs(readLogs, userProfiles, "READ");
        processLogs(writeLogs, userProfiles, "WRITE");
        processLogs(priceLogs, userProfiles, "MOST_EXPENSIVE");
        return userProfiles.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().build()));
    }

    private void processLogs(List<Map<String, Object>> logs, Map<String, UserProfileBuilder> profiles, String actionType) {
        for (Map<String, Object> log : logs) {
            String userId = getString(log, "UserID");
            String userName = getString(log, "User");
            String logKey = createLogKey(log);
            synchronized(processedLogs) {
                if ((!userId.isEmpty()) && processedLogs.add(logKey)) {
                    profiles.computeIfAbsent(userId, id -> new UserProfileBuilder().withUserId(id).withUserName(userName)).addAction(actionType);
                }
            }
        }
    }

    private String getString(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return value != null ? value.toString().trim() : "";
    }

    private String createLogKey(Map<String, Object> log) {
        return String.join("_", getString(log, "timestamp"), getString(log, "UserID"), getString(log, "method"));
    }
}
