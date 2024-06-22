package models.schemas;

public class DrinkVariant {
    private int products_name_id, available_count, critical_level, preferred_unit_id;
    private double size, price, discounted_price;
    private boolean isVoided;

    public DrinkVariant(int products_name_id, double size, double price, int available_count, int critical_level,
            boolean isVoided, double discounted_price, int preferred_unit_id) {
        this.products_name_id = products_name_id;
        this.available_count = available_count;
        this.critical_level = critical_level;
        this.size = size;
        this.price = price;
        this.isVoided = isVoided;
        this.discounted_price = discounted_price;
        this.preferred_unit_id = preferred_unit_id;
    }

    public int getProducts_name_id() {
        return this.products_name_id;
    }

    public int getAvailable_count() {
        return this.available_count;
    }

    public int getCritical_level() {
        return this.critical_level;
    }

    public double getSize() {
        return this.size;
    }

    public double getPrice() {
        return this.price;
    }

    public boolean isVoided() {
        return this.isVoided;
    }

    public double getDiscounted_price() {
        return this.discounted_price;
    }

    public double getPreferred_unit_id() {
        return this.preferred_unit_id;
    }
}
