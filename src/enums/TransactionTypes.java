/*
 * Contains all of the transaction types
 * 
 * For better readability
 */

package enums;

public class TransactionTypes {
    public enum Type {
        // STOCKPRODUCTTYPE(id, type)

        DELIVERY(1, "Delivery"),
        PICK_UP(2, "Pick Up"),
        RESERVATION(3, "Reservation");

        private final String type;
        private final int id;

        // Constructor for ENUM
        Type(int id, String type) {
            this.id = id;
            this.type = type;
        }

        // Returns the value of Transaction Type equivalent to database
        public int getValue() {
            return this.id;
        }

        public String getType() {
            return this.type;
        }

    }
}