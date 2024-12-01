package org.example.entity;
import java.util.Map;
import java.util.function.ToIntFunction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {
    private String userName;

    private String userId;

    private Map<String, Integer> actionCounts;

    private int totalActions;

    public void calculateTotalActions() {
        totalActions = actionCounts.values().stream().mapToInt(Integer::intValue).sum();
    }
}
