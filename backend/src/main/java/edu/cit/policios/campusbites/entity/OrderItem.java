package edu.cit.policios.campusbites.entity;

public class OrderItem {
    private String foodId;
    private String name;
    private int quantity;
    private double price;

    // Constructors
    public OrderItem() {}

    public OrderItem(String foodId, String name, int quantity, double price) {
        this.foodId = foodId;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    // Getters and Setters
    public String getFoodId() { return foodId; }
    public void setFoodId(String foodId) { this.foodId = foodId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}