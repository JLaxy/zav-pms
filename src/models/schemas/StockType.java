/*
 * Stock Type :>
 */

package models.schemas;

import java.lang.reflect.Field;

import models.helpers.PopupDialog;

public class StockType {
    private int id, default_expiration;
    private String type;

    // Null Stock Type Constructor
    public StockType() {

    }

    public StockType(int id, String type, int default_expiration) {
        this.id = id;
        this.type = type;
        this.default_expiration = default_expiration;
    }

    public int getId() {
        return this.id;
    }

    public String getType() {
        return this.type;
    }

    public int getDefault_expiration() {
        return this.default_expiration;
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
