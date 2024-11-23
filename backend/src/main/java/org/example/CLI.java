package org.example;

import org.example.entity.Product;
import org.example.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

@Component
public class CLI implements CommandLineRunner {

    @Autowired
    private RestTemplate restTemplate;

    private final String userApiUrl = "http://localhost:8080/user"; // URL de l'API pour les utilisateurs
    private final String productApiUrl = "http://localhost:8080/product"; // URL de l'API pour les produits

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Menu:");
            System.out.println("1. Créer un utilisateur");
            System.out.println("2. Connecter l'utilisateur");
            System.out.println("3. Afficher tous les utilisateurs");
            System.out.println("4. Déconnecter l'utilisateur");
            System.out.println("5. Ajouter un produit");
            System.out.println("6. Afficher tous les produits");
            System.out.println("7. Récupérer un produit par ID");
            System.out.println("8. Mettre à jour un produit");
            System.out.println("9. Supprimer un produit");
            System.out.println("10. Quitter");
            System.out.print("Choisissez une option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consommer la nouvelle ligne

            switch (choice) {
                case 1:
                    createUser(scanner);
                    break;
                case 2:
                    loginUser(scanner);
                    break;
                case 3:
                    displayAllUsers();
                    break;
                case 4:
                    logoutUser();
                    break;
                case 5:
                    addProduct(scanner);
                    break;
                case 6:
                    displayAllProducts();
                    break;
                case 7:
                    fetchProductById(scanner);
                    break;
                case 8:
                    updateProduct(scanner);
                    break;
                case 9:
                    deleteProduct(scanner);
                    break;
                case 10:
                    System.out.println("Bye!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Option invalide.");
            }
        }
    }

    private void createUser(Scanner scanner) {
        System.out.print("Entrez le nom de l'utilisateur: ");
        String name = scanner.nextLine();
        System.out.print("Entrez l'âge de l'utilisateur: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // Consommer la nouvelle ligne

        System.out.print("Entrez l'email de l'utilisateur: ");
        String email = scanner.nextLine();
        System.out.print("Entrez le mot de passe de l'utilisateur: ");
        String password = scanner.nextLine();

        User user = new User(name, age, email, password);
        // Appel API pour créer un utilisateur
        restTemplate.postForEntity(userApiUrl + "/register", user, User.class);
        System.out.println("Utilisateur créé avec succès.");
    }

    private void displayAllUsers() {
        ResponseEntity<List> response = restTemplate.exchange(userApiUrl, HttpMethod.GET, null, List.class);
        List users = response.getBody();
        System.out.println("Liste des utilisateurs:");
        assert users != null;
        for (Object user : users) {
            System.out.println(user);
        }
    }

    private void addProduct(Scanner scanner) {
        System.out.print("Entrez le nom du produit: ");
        String name = scanner.nextLine();
        System.out.print("Entrez le prix du produit: ");
        double price = scanner.nextDouble();
        scanner.nextLine(); // Consommer la nouvelle ligne

        System.out.print("Entrez la date d'expiration du produit (format: dd-MM-yyyy): ");
        String expirationDateStr = scanner.nextLine();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date expirationDate = sdf.parse(expirationDateStr);
            Product product = new Product(name, price, expirationDate);
            // Appel API pour ajouter un produit
            restTemplate.postForEntity(productApiUrl, product, Product.class);
            System.out.println("Produit ajouté avec succès.");
        } catch (Exception e) {
            System.out.println("Erreur lors de l'ajout du produit: " + e.getMessage());
        }
    }

    private void displayAllProducts() {
        ResponseEntity<List> response = restTemplate.exchange(productApiUrl, HttpMethod.GET, null, List.class);
        List products = response.getBody();
        System.out.println("Liste des produits:");
        assert products != null;
        for (Object product : products) {
            System.out.println(product);
        }
    }

    private void updateProduct(Scanner scanner) {
        System.out.print("Entrez l'ID du produit à mettre à jour: ");
        String productId = scanner.nextLine();
        System.out.print("Entrez le nouveau nom du produit: ");
        String name = scanner.nextLine();
        System.out.print("Entrez le nouveau prix du produit: ");
        double price = scanner.nextDouble();
        scanner.nextLine(); // Consommer la nouvelle ligne

        System.out.print("Entrez la nouvelle date d'expiration du produit (format: dd-MM-yyyy): ");
        String expirationDateStr = scanner.nextLine();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date expirationDate = sdf.parse(expirationDateStr);
            Product product = new Product(name, price, expirationDate);
            // Appel API pour mettre à jour le produit
            restTemplate.put(productApiUrl + "/" + productId, product);
            System.out.println("Produit mis à jour avec succès.");
        } catch (Exception e) {
            System.out.println("Erreur lors de la mise à jour du produit: " + e.getMessage());
        }
    }

    private void deleteProduct(Scanner scanner) {
        System.out.print("Entrez l'ID du produit à supprimer: ");
        String productId = scanner.nextLine();
        // Appel API pour supprimer le produit
        restTemplate.delete(productApiUrl + "/" + productId);
        System.out.println("Produit supprimé avec succès.");
    }

    private void fetchProductById(Scanner scanner) {
        System.out.print("Entrez l'ID du produit à récupérer: ");
        String productId = scanner.nextLine();
        // Appel API pour récupérer un produit par ID
        Product product = restTemplate.getForObject(productApiUrl + "/" + productId, Product.class);
        System.out.println("Produit récupéré: " + product);
    }

    private void loginUser(Scanner scanner) {
        System.out.print("Entrez l'email de l'utilisateur: ");
        String email = scanner.nextLine();
        System.out.print("Entrez le mot de passe de l'utilisateur: ");
        String password = scanner.nextLine();

        // Création d'un objet de requête pour login
        User loginRequest = new User(email, password); // Assurez-vous que votre API accepte cet objet
        // Appel API pour la connexion de l'utilisateur
        ResponseEntity<User> response = restTemplate.postForEntity(userApiUrl + "/login", loginRequest, User.class);
        User user = response.getBody();

        if (user != null) {
            System.out.println("Connexion réussie. Bienvenue, " + user.getName() + "!");
        } else {
            System.out.println("Échec de la connexion.");
        }
    }

    private void logoutUser() {
        restTemplate.getForObject(userApiUrl + "/logout", String.class);
        System.out.println("Déconnexion réussie !");
    }
}
