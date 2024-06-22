package models.schemas;

import java.lang.reflect.Field;
import java.util.ArrayList;

import enums.PreferredUnits;
import models.helpers.PopupDialog;
import models.helpers.UnitConverter;

public class DrinkVariant {
    private int id, products_name_id, available_count, critical_level, preferred_unit_id;
    private double size, price, discounted_price;
    private boolean isVoided;
    private String product_name, size_string;

    public DrinkVariant(int id, String product_name, int products_name_id, double size, double price,
            int available_count,
            int critical_level,
            boolean isVoided, double discounted_price, int preferred_unit_id) {
        this.id = id;
        this.products_name_id = products_name_id;
        this.available_count = available_count;
        this.critical_level = critical_level;
        this.size = size;
        this.price = price;
        this.isVoided = isVoided;
        this.discounted_price = discounted_price;
        this.preferred_unit_id = preferred_unit_id;
        this.product_name = product_name;
        updateStringEquivalents();
    }

    public DrinkVariant getCopy() {
        return new DrinkVariant(this.id, this.product_name, this.products_name_id, this.size, this.price,
                this.available_count, this.critical_level, this.isVoided, this.discounted_price,
                this.preferred_unit_id);
    }

    public void increaseQuantity(int quantity) {
        this.available_count += quantity;
    }

    public void toggleVoidStatus() {
        this.isVoided = !this.isVoided;
    }

    private void updateStringEquivalents() {
        if (preferred_unit_id == PreferredUnits.Units.LITERS.getValue())
            this.size_string = UnitConverter.toTwoDecimalPlaces(UnitConverter.mililiterToLiter(size)) + "L";
        else if (preferred_unit_id == PreferredUnits.Units.MILILITERS.getValue())
            this.size_string = UnitConverter.toTwoDecimalPlaces(size) + "mL";
    }

    public int getProducts_name_id() {
        return this.products_name_id;
    }

    public String getSize_string() {
        return this.size_string;
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

    public String getProduct_name() {
        return this.product_name;
    }

    public int getId() {
        return this.id;
    }

    public static String[] getChangesMessage(DrinkVariant oldBeverage, DrinkVariant newBeverage) {
        ArrayList<String> changes = new ArrayList<String>();
        // Load all fields in the class (private included)
        Field[] attributes = oldBeverage.getClass().getDeclaredFields();

        // Iterate through attributes
        for (Field field : attributes) {
            try {
                // If value of attribute on previous is different to new
                if (field.get(oldBeverage).toString().compareTo(field.get(newBeverage).toString()) != 0) {
                    switch (field.getName()) {
                        // For other attributes
                        default:
                            changes.add("changed " + field.getName() + " from " + field.get(oldBeverage) + " to "
                                    + field.get(newBeverage));
                            System.out.println("changed " + field.getName() + " from " + field.get(oldBeverage) + " to "
                                    + field.get(newBeverage));
                            break;
                    }
                }
            } catch (Exception e) {
                PopupDialog.showErrorDialog(e, "DrinkVariant");
            }
        }
        // Return list of changes in array
        return changes.toArray(new String[changes.size()]);
    }
}
