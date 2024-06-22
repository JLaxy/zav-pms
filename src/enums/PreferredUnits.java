/*
 * Contains all of the pre-defined preferred units
 * 
 * For better readability
 */

package enums;

public class PreferredUnits {
    public enum Units {
        MILILITERS(2),
        LITERS(3);

        private final int id;

        // Constructor for ENUM
        Units(int id) {
            this.id = id;
        }

        // Returns the value of preferred units in database
        public int getValue() {
            return this.id;
        }
    }
}