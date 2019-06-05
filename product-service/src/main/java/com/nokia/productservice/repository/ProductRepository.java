package com.nokia.productservice.repository;

import com.nokia.productservice.schemas.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    // get all the product with ids in
    List<Product> findByIdIn(List<String> productIds);
}
