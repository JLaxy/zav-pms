package models.schemas;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class OrderProduct {
    private String productName;
    private String size;
    private int quantity;
    private double amount;
    private double discountedPrice;
    private BooleanProperty discountApplied;
    private boolean stockSufficient; // Add this field

    public OrderProduct(String productName, String size, int quantity, double amount, double discountedPrice, boolean stockSufficient) {
        this.productName = productName;
        this.size = size;
        this.quantity = quantity;
        this.amount = amount;
        this.discountedPrice = discountedPrice;
        this.discountApplied = new SimpleBooleanProperty(false);
        this.stockSufficient = stockSufficient;
    }

    // Getters and setters

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(double discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public BooleanProperty discountAppliedProperty() {
        return discountApplied;
    }

    public boolean isDiscountApplied() {
        return discountApplied.get();
    }

    public void setDiscountApplied(boolean discountApplied) {
        this.discountApplied.set(discountApplied);
    }

    public boolean isStockSufficient() {
        return stockSufficient;
    }

    public void setStockSufficient(boolean stockSufficient) {
        this.stockSufficient = stockSufficient;
    }
}
