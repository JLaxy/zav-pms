package models.schemas;

public class DrinkVariant {
    private int products_name_id, available_count, critical_level;
    private float size, price;
    private boolean isVoided;

    public DrinkVariant(int products_name_id, float size, float price, int available_count, int critical_level,
            boolean isVoided) {
        this.products_name_id = products_name_id;
        this.available_count = available_count;
        this.critical_level = critical_level;
        this.size = size;
        this.price = price;
        this.isVoided = isVoided;
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

    public float getSize() {
        return this.size;
    }

    public float getPrice() {
        return this.price;
    }

    public boolean isVoided() {
        return this.isVoided;
    }
}
