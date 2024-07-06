/*
 * Used to display ordered products in a transaction
 */

package models.schemas;

public class OrderedProduct {
    private int id, transaction_id, product_id, required_quantity, current_quantity, product_type_id;
    private String special_instruction;

    public OrderedProduct(int id, int transaction_id, int product_id, int required_quantity, int current_quantity,
            int product_type_id, String special_instruction) {
        this.id = id;
        this.transaction_id = transaction_id;
        this.product_id = product_id;
        this.required_quantity = required_quantity;
        this.current_quantity = current_quantity;
        this.product_type_id = product_type_id;
        this.special_instruction = special_instruction;
    }

    public int getId() {
        return this.id;
    }

    public int getTransaction_id() {
        return this.transaction_id;
    }

    public int getProduct_id() {
        return this.product_id;
    }

    public int getRequired_quantity() {
        return this.required_quantity;
    }

    public int getCurrent_quantity() {
        return this.current_quantity;
    }

    public int getProduct_type_id() {
        return this.product_type_id;
    }

    public String getSpecial_instruction() {
        return this.special_instruction;
    }
}
