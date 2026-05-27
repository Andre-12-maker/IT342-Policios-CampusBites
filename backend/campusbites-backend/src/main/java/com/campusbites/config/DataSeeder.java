package com.campusbites.config;

import com.campusbites.auth.model.User;
import com.campusbites.auth.repository.UserRepository;
import com.campusbites.product.model.Product;
import com.campusbites.product.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * Seeds default admin account and sample products on first startup.
 * Skips silently if data already exists — safe to run on every boot.
 *
 * SECURITY: Admin credentials come from constants here for dev convenience.
 * In production, override ADMIN_PASSWORD via an environment variable or
 * change it immediately after first login via the admin panel.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private static final String ADMIN_EMAIL    = "admin@campusbites.com";
    private static final String ADMIN_PASSWORD = "Admin@123456";

    private final UserRepository    userRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder   passwordEncoder;

    public DataSeeder(UserRepository userRepository,
                      ProductRepository productRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository    = userRepository;
        this.productRepository = productRepository;
        this.passwordEncoder   = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        seedAdmin();
        seedProducts();
    }

    // ── Admin ─────────────────────────────────────────────────────────────────

    private void seedAdmin() {
        if (userRepository.existsByEmail(ADMIN_EMAIL)) {
            log.info("Admin already exists — skipping seed.");
            return;
        }
        User admin = new User();
        admin.setEmail(ADMIN_EMAIL);
        admin.setPassword(passwordEncoder.encode(ADMIN_PASSWORD));
        admin.setFirstname("Campus");
        admin.setLastname("Admin");
        admin.setRole(User.Role.ADMIN);
        admin.setEnabled(true);
        userRepository.save(admin);
        log.info("Admin seeded: {}", ADMIN_EMAIL);
    }

    // ── Products ──────────────────────────────────────────────────────────────

    private void seedProducts() {
        if (productRepository.count() > 0) {
            log.info("Products already exist — skipping seed.");
            return;
        }
        List<Product> products = List.of(
                p("Greek Salad",       "Salad",    12, 4.5, "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=400&h=300&fit=crop"),
                p("Veg Salad",         "Salad",    10, 4.5, "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=400&h=300&fit=crop"),
                p("Clover Salad",      "Salad",    16, 4.0, "https://images.unsplash.com/photo-1555126634-323283e090fa?w=400&h=300&fit=crop"),
                p("Chicken Salad",     "Salad",    24, 4.5, "https://images.unsplash.com/photo-1565557623262-b51c2513a641?w=400&h=300&fit=crop"),
                p("Pan Pan Rolls",     "Rolls",    13, 4.5, "https://images.unsplash.com/photo-1562802378-063ec186a863?w=400&h=300&fit=crop"),
                p("Chicken Rolls",     "Rolls",    20, 4.5, "https://images.unsplash.com/photo-1504674900247-0877df9cc836?w=400&h=300&fit=crop"),
                p("Veg Rolls",         "Rolls",    13, 4.5, "https://images.unsplash.com/photo-1567620905732-2d1ec7ab7445?w=400&h=300&fit=crop"),
                p("Espresso Cream",    "Desserts", 26, 4.5, "https://images.unsplash.com/photo-1551024601-bec78aea704b?w=400&h=300&fit=crop"),
                p("Fruitas Cream",     "Desserts", 27, 4.5, "https://images.unsplash.com/photo-1540420773420-3366772f4999?w=400&h=300&fit=crop"),
                p("Ice Ice Cream",     "Desserts", 10, 4.5, "https://images.unsplash.com/photo-1476224203421-9ac39bcb3327?w=400&h=300&fit=crop"),
                p("Vanilla Cone",      "Desserts", 12, 4.5, "https://images.unsplash.com/photo-1473093295043-cdd812d0e601?w=400&h=300&fit=crop"),
                p("Chicken Sandwich",  "Sandwich", 12, 4.5, "https://images.unsplash.com/photo-1528735602780-2552fd46c7af?w=400&h=300&fit=crop"),
                p("Vegan Sandwich",    "Sandwich", 18, 4.0, "https://images.unsplash.com/photo-1540189549336-e6e99c3679fe?w=400&h=300&fit=crop"),
                p("Grilled Sandwich",  "Sandwich", 16, 4.5, "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=400&h=300&fit=crop"),
                p("Cup Cake",          "Cake",     14, 4.5, "https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=400&h=300&fit=crop"),
                p("Vegan Cake",        "Cake",     13, 4.5, "https://images.unsplash.com/photo-1504674900247-0877df9cc836?w=400&h=300&fit=crop"),
                p("Butterscotch Cake", "Cake",     20, 4.5, "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=400&h=300&fit=crop"),
                p("Garlic Mushroom",   "Pure Veg", 14, 4.5, "https://images.unsplash.com/photo-1567620905732-2d1ec7ab7445?w=400&h=300&fit=crop"),
                p("Fried Cauliflower", "Pure Veg", 22, 4.5, "https://images.unsplash.com/photo-1540189549336-e6e99c3679fe?w=400&h=300&fit=crop"),
                p("Mix Veg Pulao",     "Pure Veg", 10, 4.5, "https://images.unsplash.com/photo-1565557623262-b51c2513a641?w=400&h=300&fit=crop"),
                p("Tomato Pasta",      "Pasta",    23, 4.5, "https://images.unsplash.com/photo-1473093295043-cdd812d0e601?w=400&h=300&fit=crop"),
                p("Creamy Pasta",      "Pasta",    13, 4.5, "https://images.unsplash.com/photo-1504674900247-0877df9cc836?w=400&h=300&fit=crop"),
                p("Chicken Pasta",     "Pasta",    29, 4.5, "https://images.unsplash.com/photo-1562802378-063ec186a863?w=400&h=300&fit=crop"),
                p("Cheese Pasta",      "Pasta",    12, 4.5, "https://images.unsplash.com/photo-1476224203421-9ac39bcb3327?w=400&h=300&fit=crop"),
                p("Ramen Noodles",     "Noodles",  20, 4.5, "https://images.unsplash.com/photo-1569718212165-3a8278d5f624?w=400&h=300&fit=crop"),
                p("Cooked Noodles",    "Noodles",  15, 4.5, "https://images.unsplash.com/photo-1555126634-323283e090fa?w=400&h=300&fit=crop"),
                p("Butter Noodles",    "Noodles",  18, 4.5, "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=400&h=300&fit=crop"),
                p("Veg Noodles",       "Noodles",  12, 4.5, "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=400&h=300&fit=crop")
        );
        productRepository.saveAll(products);
        log.info("Seeded {} products.", products.size());
    }

    private Product p(String name, String category, int price,
                      double rating, String imageUrl) {
        Product p = new Product();
        p.setName(name);
        p.setDescription("Fresh and delicious " + name.toLowerCase() + " prepared daily at Campus Bites.");
        p.setCategory(category);
        p.setPrice(new BigDecimal(price));
        p.setRating(rating);
        p.setRatingCount(100);
        p.setImageUrl(imageUrl);
        p.setAvailable(true);
        p.setStock(50);   // stock tracked — decrements on order, restores on cancel
        return p;
    }
}