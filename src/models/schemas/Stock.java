/*
 * Stock >:(
 */

package models.schemas;

import java.lang.reflect.Field;
import java.util.ArrayList;

import models.helpers.PopupDialog;
import models.helpers.database.DBManager;

public class Stock {
    private int id, unit_measure_id, stock_type_id, critical_level;
    private String stock_name, unit_measure_id_string, stock_type_id_string;
    private boolean isVoided;
    private double quantity;

    // Null Stock Constructor
    public Stock() {

    }

    public Stock(int id, String stock_name, double quantity, int unit_measure_id, int stock_type_id, int critical_level,
            boolean isVoided) {
        this.id = id;
        this.stock_name = stock_name;
        this.quantity = quantity;
        this.unit_measure_id = unit_measure_id;
        this.stock_type_id = stock_type_id;
        this.critical_level = critical_level;
        this.isVoided = isVoided;

        // Updates string equivalents of Unit Measure and Stock Type
        updateStringEquivalents();
    }

    public Stock getCopy() {
        return new Stock(this.id, this.stock_name, this.quantity, this.unit_measure_id, this.stock_type_id,
                this.critical_level, this.isVoided);
    }

    // Updates quantity of stock; used in stock seletion
    public void setSelectedQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Updates string equivalents of Unit Measure and Stock Type
    private void updateStringEquivalents() {
        DBManager database = new DBManager();
        this.unit_measure_id_string = database.query.getUnitMeasure(this.unit_measure_id);
        this.stock_type_id_string = database.query.getStockType(this.stock_type_id);
    }

    public int getId() {
        return this.id;
    }

    public String getStock_name() {
        return this.stock_name;
    }

    public double getQuantity() {
        return this.quantity;
    }

    public String getUnit_measure_id_string() {
        return this.unit_measure_id_string;
    }

    public String getStock_type_id_string() {
        return this.stock_type_id_string;
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

    public void toggleVoidStatus() {
        this.isVoided = !this.isVoided;
    }

    public void incrementQuantity(double count) {
        this.quantity += count;
    }

    // Returns string that describes account changes
    public static String[] getChangesMessages(Stock oldStock, Stock newStock) {

        ArrayList<String> changes = new ArrayList<String>();
        // Load all fields in the class (private included)
        Field[] attributes = oldStock.getClass().getDeclaredFields();

        // Iterate through attributes
        for (Field field : attributes) {
            try {
                // If value of attribute on previous is different to new
                if (field.get(oldStock).toString().compareTo(field.get(newStock).toString()) != 0) {
                    switch (field.getName()) {
                        // For account status id
                        case "unit_measure_id":
                            // Empty for string equivalent
                            break;
                        case "stock_type_id":
                            // Empty for string equivalent
                            break;
                        // For other attributes
                        default:
                            changes.add("changed " + field.getName() + " from " + field.get(oldStock) + " to "
                                    + field.get(newStock));
                            System.out.println("changed " + field.getName() + " from " + field.get(oldStock) + " to "
                                    + field.get(newStock));
                            break;
                    }
                }
            } catch (Exception e) {
                PopupDialog.showErrorDialog(e, "Stock");
            }
        }
        // Return list of changes in array
        return changes.toArray(new String[changes.size()]);
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
