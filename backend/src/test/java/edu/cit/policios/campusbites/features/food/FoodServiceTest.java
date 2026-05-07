package edu.cit.policios.campusbites.features.food;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class FoodServiceTest {

    @Mock
    private FoodRepository foodRepository;

    @InjectMocks
    private FoodService foodService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllFoods_ReturnsList() {
        Food f1 = new Food("Greek Salad", "Healthy", 12.0, "Salad", "img1.png");
        Food f2 = new Food("Pasta", "Delicious", 15.0, "Pasta", "img2.png");
        when(foodRepository.findAll()).thenReturn(Arrays.asList(f1, f2));

        List<Food> result = foodService.getAllFoods();

        assertEquals(2, result.size());
        verify(foodRepository).findAll();
    }

    @Test
    void getFoodsByCategory_SpecificCategory_ReturnsFiltered() {
        Food salad = new Food("Veg Salad", "Fresh", 10.0, "Salad", "img.png");
        when(foodRepository.findByCategory("Salad")).thenReturn(Arrays.asList(salad));

        List<Food> result = foodService.getFoodsByCategory("Salad");

        assertEquals(1, result.size());
        assertEquals("Salad", result.get(0).getCategory());
    }

    @Test
    void getFoodsByCategory_All_ReturnsAll() {
        Food f1 = new Food("Salad", "Fresh", 10.0, "Salad", "img.png");
        Food f2 = new Food("Noodles", "Hot", 12.0, "Noodles", "img2.png");
        when(foodRepository.findAll()).thenReturn(Arrays.asList(f1, f2));

        List<Food> result = foodService.getFoodsByCategory("All");

        assertEquals(2, result.size());
        verify(foodRepository, never()).findByCategory(anyString());
    }

    @Test
    void getFoodById_Exists_ReturnsFood() {
        Food food = new Food("Burger", "Tasty", 8.0, "Sandwich", "img.png");
        food.setId("abc123");
        when(foodRepository.findById("abc123")).thenReturn(Optional.of(food));

        Food result = foodService.getFoodById("abc123");

        assertNotNull(result);
        assertEquals("Burger", result.getName());
    }

    @Test
    void getFoodById_NotFound_ReturnsNull() {
        when(foodRepository.findById("notexist")).thenReturn(Optional.empty());

        Food result = foodService.getFoodById("notexist");

        assertNull(result);
    }

    @Test
    void saveFood_ReturnsSavedFood() {
        Food food = new Food("Cake", "Sweet", 5.0, "Cake", "img.png");
        when(foodRepository.save(food)).thenReturn(food);

        Food result = foodService.saveFood(food);

        assertNotNull(result);
        assertEquals("Cake", result.getName());
    }
}