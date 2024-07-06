package models.schemas;

import javafx.beans.property.*;

public class OrderProduct {
    private StringProperty productName;
    private StringProperty size;
    private IntegerProperty quantity;
    private DoubleProperty amount;
    private DoubleProperty discountedPrice;
    private BooleanProperty stockSufficient;
    private BooleanProperty discountApplied;
    private StringProperty discounted;
    private IntegerProperty discountedQuantity;
    private BooleanProperty grouped;
    public Double initialAmount; // Original amount for initial state
    public int initialQuantity; // Original quantity for initial state

    public OrderProduct(String productName, String size, int quantity, double amount, double discountedPrice, boolean stockSufficient) {
        this.productName = new SimpleStringProperty(productName);
        this.size = new SimpleStringProperty(size);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.amount = new SimpleDoubleProperty(amount);
        this.discountedPrice = new SimpleDoubleProperty(discountedPrice);
        this.stockSufficient = new SimpleBooleanProperty(stockSufficient);
        this.discountApplied = new SimpleBooleanProperty(false);
        this.discounted = new SimpleStringProperty("");
        this.discountedQuantity = new SimpleIntegerProperty(0);
        this.grouped = new SimpleBooleanProperty(false);
        this.initialAmount = amount; // Set initial amount
        this.initialQuantity = quantity; // Set initial quantity
    }      

    public OrderProduct(OrderProduct other) {
        this.productName = new SimpleStringProperty(other.getProductName());
        this.size = new SimpleStringProperty(other.getSize());
        this.quantity = new SimpleIntegerProperty(other.getQuantity());
        this.amount = new SimpleDoubleProperty(other.getAmount());
        this.discountedPrice = new SimpleDoubleProperty(other.getDiscountedPrice());
        this.stockSufficient = new SimpleBooleanProperty(other.isStockSufficient());
        this.discountApplied = new SimpleBooleanProperty(other.isDiscountApplied());
        this.discounted = new SimpleStringProperty(other.getDiscounted());
        this.discountedQuantity = new SimpleIntegerProperty(other.getDiscountedQuantity());
        this.grouped = new SimpleBooleanProperty(other.isGrouped());
        this.initialAmount = other.initialAmount;
        this.initialQuantity = other.initialQuantity;
    }

    public OrderProduct copy() {
        return new OrderProduct(this);
    }

    // Getters and setters...

    public String getProductName() {
        return productName.get();
    }

    public void setProductName(String productName) {
        this.productName.set(productName);
    }

    public StringProperty productNameProperty() {
        return productName;
    }

    public String getSize() {
        return size.get();
    }

    public void setSize(String size) {
        this.size.set(size);
    }

    public StringProperty sizeProperty() {
        return size;
    }

    public int getQuantity() {
        return quantity.get();
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    public IntegerProperty quantityProperty() {
        return quantity;
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
        return discountedPrice.get();
    }

    public void setDiscountedPrice(double discountedPrice) {
        this.discountedPrice.set(discountedPrice);
    }

    public DoubleProperty discountedPriceProperty() {
        return discountedPrice;
    }

    public boolean isStockSufficient() {
        return stockSufficient.get();
    }

    public void setStockSufficient(boolean stockSufficient) {
        this.stockSufficient.set(stockSufficient);
    }

    public BooleanProperty stockSufficientProperty() {
        return stockSufficient;
    }

    public boolean isDiscountApplied() {
        return discountApplied.get();
    }

    public void setDiscountApplied(boolean discountApplied) {
        this.discountApplied.set(discountApplied);
    }

    public BooleanProperty discountAppliedProperty() {
        return discountApplied;
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

    public int getDiscountedQuantity() {
        return discountedQuantity.get();
    }

    public void setDiscountedQuantity(int discountedQuantity) {
        this.discountedQuantity.set(discountedQuantity);
    }

    public IntegerProperty discountedQuantityProperty() {
        return discountedQuantity;
    }

    public int getRemainingQuantity() {
        return this.quantity.get() - this.discountedQuantity.get();
    }      

    public void incrementDiscountedQuantity(int incrementBy) {
        if (this.getRemainingQuantity() >= incrementBy) {
            this.discountedQuantity.set(this.discountedQuantity.get() + incrementBy);
        }
    }       

    public void setTotalQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    public boolean isGrouped() {
        return grouped.get();
    }

    public void setGrouped(boolean grouped) {
        this.grouped.set(grouped);
    }

    public BooleanProperty groupedProperty() {
        return grouped;
    }

    public double getInitialAmount() {
        return initialAmount;
    }

    public int getInitialQuantity() {
        return initialQuantity;
    }

    public void calculateAmount() {
        // Regular price per unit based on initial amount and initial quantity    400 / 5 = 80
        double regularPrice = this.initialAmount / this.initialQuantity;
    
        // Total discounted amount           50 x 1 = 50
        double discountedAmount = this.discountedPrice.get() * this.discountedQuantity.get();
    
        // Total regular amount    80 x (5 - 1) = 320
        double regularAmount = regularPrice * (this.quantity.get() - this.discountedQuantity.get());
    
        // Total amount after applying discount      320 + 50 = 370
        double totalAmount = regularAmount + discountedAmount;
        
        // Update the amount
        this.amount.set(totalAmount);
    
        // Print the calculation details
        System.out.println("Calculated Amount: " + totalAmount + " for product " + this.productName.get() +
                           " (Regular Price: " + regularPrice +
                           ", Discounted Quantity: " + this.discountedQuantity.get() +
                           ", Discounted Amount: " + discountedAmount +
                           ", Regular Amount: " + regularAmount + ")");
    }      
    
    public void applyDiscount(int discountQuantity) {
        if (this.getRemainingQuantity() >= discountQuantity) {
            this.incrementDiscountedQuantity(discountQuantity);
            this.calculateAmount();
            this.discounted.set("✓");
            this.discountApplied.set(true);
            System.out.println("Discount applied to product: " + this.productName.get() +
                               " (Discounted Quantity: " + this.discountedQuantity.get() +
                               ", Total Amount: " + this.amount.get() + ")");
        }
    }            

    public int getTotalQuantity() {
        return this.quantity.get();
    }
}
