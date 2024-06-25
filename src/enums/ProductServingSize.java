/*
 * Contains all of the pre-defined product serving sizes
 * 
 * For better readability
 */

package enums;

public class ProductServingSize {
    public enum ServingSize {
        // ServingSize(ID)
        SOLO(1, "solo"),
        SHARING(2, "sharing"),
        LARGE_TRAY(3, "large tray"),
        EXTRA_LARGE_TRAY(4, "extra large tray");

        private final int id;
        private final String sizeString;

        // Constructor for ENUM
        ServingSize(int id, String sizeString) {
            this.id = id;
            this.sizeString = sizeString;
        }

        // Returns the value of serving size in database
        public int getValue() {
            return this.id;
        }

        public String getString() {
            return this.sizeString;
        }
    }
}