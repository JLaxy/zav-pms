package models.schemas;

public class SoldProduct {
    private String productName;
    private int timesSold;

    public SoldProduct(String productName, int timesSold) {
        this.productName = productName;
        this.timesSold = timesSold;
    }

    public String getProductName() {
        return this.productName;
    }

    public int getTimesSold() {
        return this.timesSold;
    }
}
