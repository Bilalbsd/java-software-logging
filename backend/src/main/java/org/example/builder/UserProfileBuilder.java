package org.example.builder;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import org.example.entity.UserProfile;
import lombok.Setter;
public class UserProfileBuilder {
    @Setter
    private String userName;

    @Setter
    private String userId;

    private final Map<String, Integer> actionCounts = new HashMap<>();

    // Chaînage pour définir le nom de l'utilisateur
    public UserProfileBuilder withUserName(String userName) {
        this.userName = userName;
        return this;
    }

    // Chaînage pour définir l'ID de l'utilisateur
    public UserProfileBuilder withUserId(String userId) {
        this.userId = userId;
        return this;
    }

    // Ajout ou mise à jour du nombre d'actions pour un type donné
    public void addAction(String actionType) {
        actionCounts.merge(actionType, 1, Integer::sum);
    }

    // Construction de l'objet UserProfile
    public UserProfile build() {
        UserProfile userProfile = new UserProfile();
        userProfile.setUserName(userName);
        userProfile.setUserId(userId);
        userProfile.setActionCounts(new HashMap<>(actionCounts));
        userProfile.calculateTotalActions();
        return userProfile;
    }
}
