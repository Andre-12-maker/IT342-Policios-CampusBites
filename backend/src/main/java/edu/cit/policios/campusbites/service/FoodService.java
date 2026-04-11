package edu.cit.policios.campusbites.service;

import edu.cit.policios.campusbites.entity.Food;
import edu.cit.policios.campusbites.repository.FoodRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FoodService {
    private final FoodRepository foodRepository;

    public FoodService(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    public List<Food> getAllFoods() {
        return foodRepository.findAll();
    }

    public List<Food> getFoodsByCategory(String category) {
        if ("All".equals(category)) {
            return getAllFoods();
        }
        return foodRepository.findByCategory(category);
    }

    public Food getFoodById(String id) {
        return foodRepository.findById(id).orElse(null);
    }

    public Food saveFood(Food food) {
        return foodRepository.save(food);
    }
}