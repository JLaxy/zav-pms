package models.schemas;

public class FoodVariant {
    private int id, product_name_id, available_count, serving_size_id;
    private double regular_price, discounted_price;
    private boolean is_voided;

    public FoodVariant(int id, int product_name_id, double regular_price, int serving_size_id, int available_count,
            double discounted_price, boolean is_voided) {
        this.id = id;
        this.product_name_id = product_name_id;
        this.regular_price = regular_price;
        this.serving_size_id = serving_size_id;
        this.available_count = available_count;
        this.discounted_price = discounted_price;
        this.is_voided = is_voided;
    }

    public int getId() {
        return this.id;
    }

    public int getProduct_name_id() {
        return this.product_name_id;
    }

    public int getAvailable_count() {
        return this.available_count;
    }

    public int getServing_size_id() {
        return this.serving_size_id;
    }

    public double getRegular_price() {
        return this.regular_price;
    }

    public double getDiscounted_price() {
        return this.discounted_price;
    }

    public boolean getIs_voided() {
        return this.is_voided;
    }
}
