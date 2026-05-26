package com.campusbites.product.repository;

import com.campusbites.product.model.Product;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface ProductRepository extends MongoRepository<Product, String> {
    Page<Product> findByAvailableTrue(Pageable pageable);
    Page<Product> findByCategoryIgnoreCaseAndAvailableTrue(String category, Pageable pageable);

    @Query("{ 'available': true, 'name': { $regex: ?0, $options: 'i' } }")
    Page<Product> searchByName(String nameRegex, Pageable pageable);

    @Query("{ 'available': true, 'category': { $regex: ?0, $options: 'i' }, 'name': { $regex: ?1, $options: 'i' } }")
    Page<Product> searchByCategoryAndName(String category, String nameRegex, Pageable pageable);
}