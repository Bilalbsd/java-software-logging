package org.example.service;
import java.util.List;
import org.example.entity.Product;
import org.example.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public Product addProduct(Product product) {
        boolean existingProduct = productRepository.existsByName(product.getName());
        if (existingProduct) {
            throw new RuntimeException("Product already exists");
        }
        return productRepository.save(product);
    }

    public Product getProductById(String id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public void deleteProduct(String id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found");
        }
        productRepository.deleteById(id);
    }

    public Product updateProduct(String id, Product product) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found");
        }
        product.setId(id);
        return productRepository.save(product);
    }

    public boolean isProductTheMostExpensive(String id) {
        Product mostExpensiveProduct = productRepository.findTopByOrderByPriceDesc();
        if (mostExpensiveProduct != null) {
            Product currentProduct = productRepository.findById(id).orElse(null);
            return (currentProduct != null) && (currentProduct.getPrice() == mostExpensiveProduct.getPrice());
        }
        return false;
    }
}
