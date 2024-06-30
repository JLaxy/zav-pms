package models.schemas;

import java.time.LocalDate;

public class DeprecatedItem {
    private String inventory_item;
    private LocalDate expiry_date;
    private int critical_level;
    private double quantity;

    // Null constructor
    public DeprecatedItem() {
    }

    public DeprecatedItem(String inventory_item, double quantity, LocalDate expiry_date, int critical_level) {
        this.inventory_item = inventory_item;
        this.quantity = quantity;
        this.expiry_date = expiry_date;
        this.critical_level = critical_level;
    }

    public String getInventory_item() {
        return this.inventory_item;
    }

    public int getCritical_level() {
        return this.critical_level;
    }

    public double getQuantity() {
        return this.quantity;
    }

    public LocalDate getExpiry_date() {
        return this.expiry_date;
    }

}
