package org.example.controller;
import java.util.List;
import org.example.entity.Product;
import org.example.entity.User;
import org.example.service.ProductService;
import org.example.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/product")
public class ProductController {
    private static final Logger mostExpensiveLogger = LoggerFactory.getLogger("mostExpensiveLogger");

    private static final Logger writeLogger = LoggerFactory.getLogger("writeLogger");

    private static final Logger readLogger = LoggerFactory.getLogger("readLogger");

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;// Injection du service UserService


    private User getCurrentUser() {
        return userService.getCurrentUser();
    }

    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody
    Product product) {
        User user = getCurrentUser();
        writeLogger.info("\"method\": \"addProduct\", \"username\": \"" + user.getName() + "\", \"UserID\": \"" + user.getId() + "\"");
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
            if (user != null) {    currentLogger.info("\"method\": \"getProductById\", \"username\": \"{}\", \"UserID\": \"{}\", \"productName\": \"{}\", \"productID\": \"{}\", \"price\": {}", user.getName(), user.getId(), product.getName(), product.getId(), product.getPrice());};
            return ResponseEntity.ok(product);
        } else
            return (ResponseEntity<?>) ResponseEntity.notFound();

    }

    @GetMapping
    public ResponseEntity<List<Product>> getProducts() {
        User user = getCurrentUser();
        readLogger.info("\"method\": \"getProducts\", \"username\": \"" + user.getName() + "\", \"UserID\": \"" + user.getId() + "\"");
        return ResponseEntity.ok(productService.getProducts());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable
    String id) {
        User user = getCurrentUser();
        writeLogger.info("\"method\": \"deleteProduct\", \"username\": \"" + user.getName() + "\", \"UserID\": \"" + user.getId() + "\"");
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable
    String id, @RequestBody
    Product product) {
        User user = getCurrentUser();
        writeLogger.info("\"method\": \"updateProduct\", \"username\": \"" + user.getName() + "\", \"UserID\": \"" + user.getId() + "\"");
        return ResponseEntity.ok(productService.updateProduct(id, product));
    }
}
