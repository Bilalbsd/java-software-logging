package org.example.service;
import java.util.List;
import java.util.Optional;
import org.example.entity.User;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.Getter;
import lombok.Setter;
@Service
public class UserService {
    // Méthode pour récupérer l'utilisateur actuel
    @Getter
    @Setter
    private User currentUser;// Vous pouvez stocker l'utilisateur connecté ici


    @Autowired
    private UserRepository userRepository;// Injection du repository pour la gestion des utilisateurs


    // Méthode pour créer un utilisateur
    public User createUser(User user) {
        // Logique pour enregistrer un utilisateur
        currentUser = userRepository.save(user);// Sauvegarde de l'utilisateur et mise à jour de l'utilisateur actuel

        return currentUser;
    }

    // Méthode pour authentifier un utilisateur
    public User loginUser(String email, String password) {
        // Logique d'authentification
        User authenticatedUser = authenticateUser(email, password);
        currentUser = authenticatedUser;// Met à jour l'utilisateur actuel

        return authenticatedUser;
    }

    // Méthode pour authentifier un utilisateur à partir de son email et mot de passe
    private User authenticateUser(String email, String password) {
        // Exemple de logique d'authentification : rechercher l'utilisateur par email et vérifier le mot de passe
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent() && userOptional.get().getPassword().equals(password)) {
            return userOptional.get();
        } else {
            throw new RuntimeException("Authentification échouée");// Vous pouvez gérer cela plus proprement

        }
    }

    // Récupérer un utilisateur par son ID
    public User getUserById(String id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }

    // Récupérer la liste de tous les utilisateurs
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    // Supprimer un utilisateur par son ID
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
}
