package org.example;

import org.example.entity.Product;
import org.example.entity.User;
import org.example.service.ProductService;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.text.SimpleDateFormat;

@Component
public class CLI implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

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
                    System.out.println("Bye !");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Option invalide.");
            }
        }
    }

    private void createUser(Scanner scanner) throws Exception {
        System.out.print("Entrez le nom de l'utilisateur: ");
        String name = scanner.nextLine();
        System.out.print("Entrez l'âge de l'utilisateur: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // Consommer la nouvelle ligne
        System.out.print("Entrez l'email de l'utilisateur: ");
        String email = scanner.nextLine();
        System.out.print("Entrez le mot de passe de l'utilisateur: ");
        String password = scanner.nextLine();

        User user = new User();
        user.setName(name);
        user.setAge(age);
        user.setEmail(email);
        user.setPassword(password);

        userService.createUser(user);
        System.out.println("Utilisateur créé avec succès.");
    }

    private void displayAllUsers() {
        List<User> users = userService.getUsers();
        System.out.println("Liste des utilisateurs:");
        for (User user : users) {
            System.out.println(user);
        }
    }

    private void addProduct(Scanner scanner) {
        System.out.print("Entrez l'ID du produit: ");
        String id = scanner.nextLine();
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
            
            Product product = new Product();
            product.setId(id);
            product.setName(name);
            product.setPrice(price);
            product.setExpirationDate(expirationDate);
            
            productService.addProduct(product);
            System.out.println("Produit ajouté avec succès.");
        } catch (Exception e) {
            System.out.println("Erreur lors de l'ajout du produit: " + e.getMessage());
        }
    }

    private void displayAllProducts() {
        List<Product> products = productService.getProducts();
        System.out.println("Liste des produits:");
        for (Product product : products) {
            System.out.println(product);
        }
    }

    private void updateProduct(Scanner scanner) {
        System.out.print("Entrez l'ID du produit à mettre à jour: ");
        String id = scanner.nextLine();
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
            
            Product product = new Product();
            product.setId(id);
            product.setName(name);
            product.setPrice(price);
            product.setExpirationDate(expirationDate);
            
            productService.updateProduct(product);
            System.out.println("Produit mis à jour avec succès.");
        } catch (Exception e) {
            System.out.println("Erreur lors de la mise à jour du produit: " + e.getMessage());
        }
    }

    private void deleteProduct(Scanner scanner) {
        System.out.print("Entrez l'ID du produit à supprimer: ");
        String id = scanner.nextLine();
        productService.deleteProduct(id);
        System.out.println("Produit supprimé avec succès.");
    }

    private void fetchProductById(Scanner scanner) {
        System.out.print("Entrez l'ID du produit à récupérer: ");
        String id = scanner.nextLine();
        Product product = productService.getProductById(id);
        System.out.println("Produit récupéré: " + product);
    }

    private void loginUser(Scanner scanner) {
        System.out.print("Entrez l'email de l'utilisateur: ");
        String email = scanner.nextLine();
        System.out.print("Entrez le mot de passe de l'utilisateur: ");
        String password = scanner.nextLine();

        try {
            User user = userService.loginUser(email, password);
            System.out.println("Connexion réussie. Bienvenue, " + user.getName() + "!");
        } catch (Exception e) {
            System.out.println("Échec de la connexion: " + e.getMessage());
        }
    }

    private void logoutUser() {
        // Logique de déconnexion (si nécessaire)
        System.out.println("Déconnexion réussie.");
    }
}