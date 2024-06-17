/*
 * Contains the paths of all of the screens
 * 
 * For better readability
 */

package enums;

public class StockProductType {
    public enum Type {
        // STOCKPRODUCTTYPE(id, type)

        BEVERAGE(1, "Beverage"),
        STOCK(2, "Stock"),
        FOOD(3, "Food");

        private final String type;
        private final int id;

        // Constructor for ENUM
        Type(int id, String type) {
            this.id = id;
            this.type = type;
        }

        // Returns the value of the stock_product_type_id equivalent to database
        public int getValue() {
            return this.id;
        }

        public String getType() {
            return this.type;
        }

    }
}