package models.schemas;

import java.time.LocalDate;

public class ExpiringItems {
    private String inventory_item;
    private LocalDate expiry_date;
    private int quantity;

    public ExpiringItems(String inventory_item, int quantity, LocalDate expiry_date) {
        this.inventory_item = inventory_item;
        this.quantity = quantity;
        this.expiry_date = expiry_date;
    }

    public String getInventory_item() {
        return this.inventory_item;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public LocalDate getExpiry_date() {
        return this.expiry_date;
    }

}
