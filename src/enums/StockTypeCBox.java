/*
 * Contains all of the pre-defined Unit Measure in the database
 * with their respective action_id
 * 
 * For better readability
 */

package enums;

public class StockTypeCBox {
    public enum StockType {
        // STOCK_TYPE_ID(stock_type_id_db)
        VEGETABLE(1),
        MEAT(2),
        CONDIMENT(3);

        private final int stock_type_id;

        // Constructor for ENUM
        StockType(int stock_type_id) {
            this.stock_type_id = stock_type_id;
        }

        // Returns the value of the unit_measure_id equivalent to database
        public int getValue() {
            return this.stock_type_id;
        }
    }
}
