/*
 * Stock >:(
 */

package models.schemas;

import java.lang.reflect.Field;

import models.helpers.PopupDialog;

public class Stock {
    private int id, unit_measure_id, stock_type_id, critical_level;
    private String stock_name;
    private Float quantity;
    private boolean isVoided;

    // Null Stock Constructor
    public Stock() {

    }

    public Stock(int id, String stock_name, Float quantity, int unit_measure_id, int stock_type_id, int critical_level, boolean isVoided) {
        this.id = id;
        this.stock_name = stock_name;
        this.quantity = quantity;
        this.unit_measure_id = unit_measure_id;
        this.stock_type_id = stock_type_id;
        this.critical_level = critical_level;
        this.isVoided = isVoided;
    }

    public int getId() {
        return this.id;
    }

    public String getStock_name() {
        return this.stock_name;
    }

    public Float getQuantity() {
        return this.quantity;
    }

    public int getUnit_measure_id() {
        return this.unit_measure_id;
    }

    public int getStock_type_id() {
        return this.stock_type_id;
    }

    public int getCritical_level() {
        return this.critical_level;
    }

    public boolean getisVoided() {
        return this.isVoided;
    }

    public void showValues() {
        // Load all fields in the class (private included)
        Field[] attributes = this.getClass().getDeclaredFields();

        for (Field field : attributes) {
            try {
                System.out.println(field.getName() + ": " + field.get(this));
            } catch (Exception e) {
                PopupDialog.showErrorDialog(e, this.getClass().getName());
            }
        }
    }
}
