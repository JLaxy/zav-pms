/*
 * Stock >:(
 */

package models.schemas;

import java.lang.reflect.Field;

import enums.StockTypeCBox;
import enums.UnitMeasureCBox;
import models.helpers.PopupDialog;

public class Stock {
    private int id, quantity, unit_measure_id, stock_type_id, critical_level;
    private String stock_name, unit_measure_id_string, stock_type_id_string;
    private boolean isVoided;

    // Null Stock Constructor
    public Stock() {

    }

    public Stock(int id, String stock_name, int quantity, int unit_measure_id, int stock_type_id, int critical_level, boolean isVoided) {
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

    // Updates string equivalents of Unit Measure and Stock Type
    public void updateStringEquivalents() {
        if (this.unit_measure_id == UnitMeasureCBox.UnitMeasure.BOTTLE.getValue())
            this.unit_measure_id_string = "bottle";
        else if (this.unit_measure_id == UnitMeasureCBox.UnitMeasure.PACK.getValue())
        this.unit_measure_id_string = "pack";
        else if (this.unit_measure_id == UnitMeasureCBox.UnitMeasure.SACHET.getValue())
        this.unit_measure_id_string = "sachet";
        else if (this.unit_measure_id == UnitMeasureCBox.UnitMeasure.BOX.getValue())
        this.unit_measure_id_string = "box";

        if (this.stock_type_id == StockTypeCBox.StockType.VEGETABLE.getValue())
            this.stock_type_id_string = "vegetable";
        else if (this.stock_type_id == StockTypeCBox.StockType.MEAT.getValue())
            this.stock_type_id_string = "meat";
        else if (this.stock_type_id == StockTypeCBox.StockType.CONDIMENT.getValue())
            this.stock_type_id_string = "condiment";
    }

    public int getId() {
        return this.id;
    }

    public String getStock_name() {
        return this.stock_name;
    }

    public int getQuantity() {
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

    public boolean getisVoided(){
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
