package com.example.campusbites.features.home.data.model

val CATEGORIES = listOf(
    "All", "Salad", "Rolls", "Desserts",
    "Sandwich", "Cake", "Pure Veg", "Pasta", "Noodles"
)

val CATEGORY_IMAGES = mapOf(
    "Salad"    to "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=80&h=80&fit=crop",
    "Rolls"    to "https://images.unsplash.com/photo-1562802378-063ec186a863?w=80&h=80&fit=crop",
    "Desserts" to "https://images.unsplash.com/photo-1551024601-bec78aea704b?w=80&h=80&fit=crop",
    "Sandwich" to "https://images.unsplash.com/photo-1528735602780-2552fd46c7af?w=80&h=80&fit=crop",
    "Cake"     to "https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=80&h=80&fit=crop",
    "Pure Veg" to "https://images.unsplash.com/photo-1540420773420-3366772f4999?w=80&h=80&fit=crop",
    "Pasta"    to "https://images.unsplash.com/photo-1473093295043-cdd812d0e601?w=80&h=80&fit=crop",
    "Noodles"  to "https://images.unsplash.com/photo-1569718212165-3a8278d5f624?w=80&h=80&fit=crop",
)

val FOOD_IMAGES = listOf(
    "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=400&h=300&fit=crop",
    "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=400&h=300&fit=crop",
    "https://images.unsplash.com/photo-1555126634-323283e090fa?w=400&h=300&fit=crop",
    "https://images.unsplash.com/photo-1565557623262-b51c2513a641?w=400&h=300&fit=crop",
    "https://images.unsplash.com/photo-1504674900247-0877df9cc836?w=400&h=300&fit=crop",
    "https://images.unsplash.com/photo-1567620905732-2d1ec7ab7445?w=400&h=300&fit=crop",
    "https://images.unsplash.com/photo-1540189549336-e6e99c3679fe?w=400&h=300&fit=crop",
    "https://images.unsplash.com/photo-1476224203421-9ac39bcb3327?w=400&h=300&fit=crop",
)

val MOCK_PRODUCTS = listOf(
    Product("1",  "Greek Salad",       "Salad",    12.0, "Food provides essential nutrients for overall health and well-being."),
    Product("2",  "Veg Salad",         "Salad",    10.0, "Food provides essential nutrients for overall health and well-being."),
    Product("3",  "Clover Salad",      "Salad",    16.0, "Food provides essential nutrients for overall health and well-being.", rating = 4.0),
    Product("4",  "Chicken Salad",     "Salad",    24.0, "Food provides essential nutrients for overall health and well-being."),
    Product("5",  "Pan Pan Rolls",     "Rolls",    13.0, "Food provides essential nutrients for overall health and well-being."),
    Product("6",  "Chicken Rolls",     "Rolls",    20.0, "Food provides essential nutrients for overall health and well-being."),
    Product("7",  "Veg Rolls",         "Rolls",    13.0, "Food provides essential nutrients for overall health and well-being."),
    Product("8",  "Espresso Cream",    "Desserts", 26.0, "Food provides essential nutrients for overall health and well-being."),
    Product("9",  "Fruitas Cream",     "Desserts", 27.0, "Food provides essential nutrients for overall health and well-being."),
    Product("10", "Ice Ice Cream",     "Desserts", 10.0, "Food provides essential nutrients for overall health and well-being."),
    Product("11", "Vanilla Cone",      "Desserts", 12.0, "Food provides essential nutrients for overall health and well-being."),
    Product("12", "Chicken Sandwich",  "Sandwich", 12.0, "Food provides essential nutrients for overall health and well-being."),
    Product("13", "Vegan Sandwich",    "Sandwich", 18.0, "Food provides essential nutrients for overall health and well-being.", rating = 4.0),
    Product("14", "Grilled Sandwich",  "Sandwich", 16.0, "Food provides essential nutrients for overall health and well-being."),
    Product("15", "Cup Cake",          "Cake",     14.0, "Food provides essential nutrients for overall health and well-being."),
    Product("16", "Vegan Cake",        "Cake",     13.0, "Food provides essential nutrients for overall health and well-being."),
    Product("17", "Butterscotch Cake", "Cake",     20.0, "Food provides essential nutrients for overall health and well-being."),
    Product("18", "Garlic Mushroom",   "Pure Veg", 14.0, "Food provides essential nutrients for overall health and well-being."),
    Product("19", "Fried Cauliflower", "Pure Veg", 22.0, "Food provides essential nutrients for overall health and well-being."),
    Product("20", "Mix Veg Pulao",     "Pure Veg", 10.0, "Food provides essential nutrients for overall health and well-being."),
    Product("21", "Tomato Pasta",      "Pasta",    23.0, "Food provides essential nutrients for overall health and well-being."),
    Product("22", "Creamy Pasta",      "Pasta",    13.0, "Food provides essential nutrients for overall health and well-being."),
    Product("23", "Chicken Pasta",     "Pasta",    29.0, "Food provides essential nutrients for overall health and well-being."),
    Product("24", "Cheese Pasta",      "Pasta",    12.0, "Food provides essential nutrients for overall health and well-being."),
    Product("25", "Ramen Noodles",     "Noodles",  20.0, "Food provides essential nutrients for overall health and well-being."),
    Product("26", "Cooked Noodles",    "Noodles",  15.0, "Food provides essential nutrients for overall health and well-being."),
    Product("27", "Butter Noodles",    "Noodles",  18.0, "Food provides essential nutrients for overall health and well-being."),
    Product("28", "Veg Noodles",       "Noodles",  12.0, "Food provides essential nutrients for overall health and well-being."),
)