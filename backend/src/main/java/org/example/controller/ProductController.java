package org.example.controller;

import org.example.entity.Product;
import org.example.entity.User;
import org.example.service.ProductService;
import org.example.service.UserService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/product")
public class ProductController {
    private static final Logger mostExpensiveLogger = org.slf4j.LoggerFactory.getLogger("mostExpensiveLogger");

    private static final Logger writeLogger = org.slf4j.LoggerFactory.getLogger("writeLogger");

    private static final Logger readLogger = org.slf4j.LoggerFactory.getLogger("readLogger");

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;// Injection du service UserService


    private User getCurrentUser() {
        User user = userService.getCurrentUser();
        readLogger.info("\"message\": \"getCurrentUser\", \"User\": \"{}\", \"UserID\": \"{}\"", user.getName(), user.getId());
        return userService.getCurrentUser();// Supposons que UserService gère l'utilisateur actuel

    }

    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody
    Product product) {
        User user = getCurrentUser();
        writeLogger.info("\"message\": \"addProduct\", \"User\": \"{}\", \"UserID\": \"{}\"", user.getName(), user.getId());
        return ResponseEntity.ok(productService.addProduct(product));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id")
    String id) {
        User user = getCurrentUser();
        Product product = productService.getProductById(id);
        if (product != null) {
            boolean isMostExpensive = productService.isProductTheMostExpensive(id);
            Logger currentLogger = isMostExpensive ? mostExpensiveLogger : readLogger;
            if (user != null)
                currentLogger.info("\"message\": \"getProductById\", \"User\": \"{}\", \"UserID\": \"{}\", \"Product\": \"{}\", \"ProductID\": \"{}\", \"Price\": {}", user.getName(), user.getId(), product.getName(), product.getId(), product.getPrice());

            return ResponseEntity.ok(product);
        } else
            return (ResponseEntity<?>) ResponseEntity.notFound();

    }

    @GetMapping
    public ResponseEntity<List<Product>> getProducts() {
        User user = getCurrentUser();
        readLogger.info("\"message\": \"getProducts\", \"User\": \"{}\", \"UserID\": \"{}\"", user.getName(), user.getId());
        return ResponseEntity.ok(productService.getProducts());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable
    String id) {
        User user = getCurrentUser();
        writeLogger.info("\"message\": \"deleteProduct\", \"User\": \"{}\", \"UserID\": \"{}\"", user.getName(), user.getId());
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable String id, @RequestBody
    Product product) {
        User user = getCurrentUser();
        writeLogger.info("\"message\": \"updateProduct\", \"User\": \"{}\", \"UserID\": \"{}\"", user.getName(), user.getId());
        return ResponseEntity.ok(productService.updateProduct(id, product));
    }
}
