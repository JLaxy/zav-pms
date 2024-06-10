package models;

public class RegisterNewFoodProductModel {
    private String name;
    private int stockRequired;
    private double regularPrice;
    private double discountedPrice;
    private String productType;
    private String servingSize;
    private int criticalLevel;

    // Constructor
    public RegisterNewFoodProductModel(String name, int stockRequired, double regularPrice, double discountedPrice, String productType, String servingSize, int criticalLevel) {
        this.name = name;
        this.stockRequired = stockRequired;
        this.regularPrice = regularPrice;
        this.discountedPrice = discountedPrice;
        this.productType = productType;
        this.servingSize = servingSize;
        this.criticalLevel = criticalLevel;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStockRequired() {
        return stockRequired;
    }

    public void setStockRequired(int stockRequired) {
        this.stockRequired = stockRequired;
    }

    public double getRegularPrice() {
        return regularPrice;
    }

    public void setRegularPrice(double regularPrice) {
        this.regularPrice = regularPrice;
    }

    public double getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(double discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getServingSize() {
        return servingSize;
    }

    public void setServingSize(String servingSize) {
        this.servingSize = servingSize;
    }

    public int getCriticalLevel() {
        return criticalLevel;
    }

    public void setCriticalLevel(int criticalLevel) {
        this.criticalLevel = criticalLevel;
    }
}
