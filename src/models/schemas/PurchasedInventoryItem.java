/*
 * Represents a row in the stock_product_expenses table
 */

package models.schemas;

public class PurchasedInventoryItem {
    private int id, stock_id, quantity, stock_product_type_id;
    private double total_cost, size;
    private String date_purchased, expiry_date, inventory_item_name, unit_measure;

    public PurchasedInventoryItem(int id, int stock_id, int quantity, double total_cost, String date_purchased,
            int stock_product_type_id, String expiry_date, String inventory_item_name, String unit_measure,
            double size) {
        this.id = id;
        this.stock_id = stock_id;
        this.quantity = quantity;
        this.total_cost = total_cost;
        this.date_purchased = date_purchased;
        this.stock_product_type_id = stock_product_type_id;
        this.expiry_date = expiry_date;
        this.inventory_item_name = inventory_item_name;
        this.unit_measure = unit_measure;
        this.size = size;
    }

    public int getId() {
        return this.id;
    }

    public int getStock_id() {
        return this.stock_id;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public double getTotal_cost() {
        return this.total_cost;
    }

    public String getDate_purchased() {
        return this.date_purchased;
    }

    public int getStock_product_type_id() {
        return this.stock_product_type_id;
    }

    public String getExpiry_date() {
        return this.expiry_date;
    }

    public String getInventory_item_name() {
        return this.inventory_item_name;
    }

    public String getUnit_measure() {
        return this.unit_measure;
    }

    public double getSize() {
        return this.size;
    }
}
