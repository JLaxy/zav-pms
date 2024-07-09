/*
 * Contains pre-defined product types on the database
 * 
 * For better readability
 */

package enums;

public class ProductTypes {
    public enum Type {
        // TimePeriod(id)

        FOOD(1),
        BEVERAGE(2);

        private final int id;

        // Constructor for ENUM
        Type(int id) {
            this.id = id;
        }

        // Returns the value of product type
        public int getValue() {
            return this.id;
        }

    }
}