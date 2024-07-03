package models.schemas;

import javafx.beans.property.*;

public class OrderProduct {
    private String productName;
    private String size;
    private int quantity;
    private DoubleProperty amount;
    private double discountedPrice;
    private boolean stockSufficient;
    private BooleanProperty discountApplied = new SimpleBooleanProperty(false);
    private StringProperty discounted = new SimpleStringProperty("");

    public OrderProduct(String productName, String size, int quantity, double amount, double discountedPrice, boolean stockSufficient) {
        this.productName = productName;
        this.size = size;
        this.quantity = quantity;
        this.amount = new SimpleDoubleProperty(amount);
        this.discountedPrice = discountedPrice;
        this.stockSufficient = stockSufficient;
    }

    // Getters and Setters for all properties

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
        return amount.get();
    }

    public void setAmount(double amount) {
        this.amount.set(amount);
    }

    public DoubleProperty amountProperty() {
        return amount;
    }

    public double getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(double discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public boolean isStockSufficient() {
        return stockSufficient;
    }

    public void setStockSufficient(boolean stockSufficient) {
        this.stockSufficient = stockSufficient;
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

    public StringProperty discountedProperty() {
        return discounted;
    }

    public String getDiscounted() {
        return discounted.get();
    }

    public void setDiscounted(boolean discounted) {
        this.discounted.set(discounted ? "✓" : "");
    }

    public int getTotalQuantity() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTotalQuantity'");
    }

    public void setTotalQuantity(int i) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setTotalQuantity'");
    }
}
