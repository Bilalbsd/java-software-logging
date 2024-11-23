package org.example.repository;
import org.example.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
public interface ProductRepository extends MongoRepository<Product, String> {
    Product findTopByOrderByPriceDesc();
    boolean existsByName(String name);
}
