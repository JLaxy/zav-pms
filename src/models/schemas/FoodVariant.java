package models.schemas;

import java.lang.reflect.Field;
import java.util.ArrayList;

import enums.ProductServingSize;

public class FoodVariant {
    private int id, product_name_id, available_count, serving_size_id;
    private double regular_price, discounted_price;
    private boolean is_voided;
    private String serving_size_id_string, food_name;

    public FoodVariant(int id, int product_name_id, double regular_price, int serving_size_id, int available_count,
            double discounted_price, boolean is_voided) {
        this.id = id;
        this.product_name_id = product_name_id;
        this.regular_price = regular_price;
        this.serving_size_id = serving_size_id;
        this.available_count = available_count;
        this.discounted_price = discounted_price;
        this.is_voided = is_voided;
        getStringEquivalents();
    }

    public FoodVariant getCopy() {
        return new FoodVariant(this.id, this.product_name_id, this.regular_price, this.serving_size_id,
                this.available_count, this.discounted_price, this.is_voided);
    }

    public void toggleVoidStatus() {
        this.is_voided = !this.is_voided;
    }

    private void getStringEquivalents() {
        if (this.serving_size_id == ProductServingSize.ServingSize.SOLO.getValue())
            this.serving_size_id_string = ProductServingSize.ServingSize.SOLO.getString();
        else if (this.serving_size_id == ProductServingSize.ServingSize.SHARING.getValue())
            this.serving_size_id_string = ProductServingSize.ServingSize.SHARING.getString();
        else if (this.serving_size_id == ProductServingSize.ServingSize.LARGE_TRAY.getValue())
            this.serving_size_id_string = ProductServingSize.ServingSize.LARGE_TRAY.getString();
        else if (this.serving_size_id == ProductServingSize.ServingSize.EXTRA_LARGE_TRAY.getValue())
            this.serving_size_id_string = ProductServingSize.ServingSize.EXTRA_LARGE_TRAY.getString();
    }

    public void setFood_name(String name) {
        this.food_name = name;
    }

    public String getFood_name() {
        return this.food_name;
    }

    public String getServing_size_id_string() {
        return this.serving_size_id_string;
    }

    public int getId() {
        return this.id;
    }

    public int getProduct_name_id() {
        return this.product_name_id;
    }

    public int getAvailable_count() {
        return this.available_count;
    }

    public int getServing_size_id() {
        return this.serving_size_id;
    }

    public double getRegular_price() {
        return this.regular_price;
    }

    public double getDiscounted_price() {
        return this.discounted_price;
    }

    public boolean getIs_voided() {
        return this.is_voided;
    }

    // Returns string that describes account changes
    public static String[] getChangesMessages(FoodVariant oldFood, FoodVariant newFood) {

        ArrayList<String> changes = new ArrayList<String>();
        // Load all fields in the class (private included)
        Field[] attributes = oldFood.getClass().getDeclaredFields();

        // Iterate through attributes
        for (Field field : attributes) {
            try {
                // If value of attribute on previous is different to new
                if (field.get(oldFood).toString().compareTo(field.get(newFood).toString()) != 0) {
                    switch (field.getName()) {
                        // For other attributes
                        default:
                            changes.add("changed " + field.getName() + " from " + field.get(oldFood) + " to "
                                    + field.get(newFood));
                            System.out.println("changed " + field.getName() + " from " + field.get(oldFood) + " to "
                                    + field.get(newFood));
                            break;
                    }
                }
            } catch (Exception e) {
                // PopupDialog.showErrorDialog(e, "FoodVariant");
            }
        }
        // Return list of changes in array
        return changes.toArray(new String[changes.size()]);
    }
}
