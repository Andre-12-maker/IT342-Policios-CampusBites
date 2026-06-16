package edu.cit.policios.campusbites.features.food;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FoodRepository extends MongoRepository<Food, String> {
    List<Food> findByCategory(String category);
}