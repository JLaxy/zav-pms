/*
 * Contains all of the pre-defined list contained in database
 * 
 * For better readability
 */

package enums;

public class UserManualTypes {
    public enum ManualType {
        // Manualtype(id)
        ADMIN_MANUAL(1),
        KITCHEN_STAFF_MANUAL(2),
        CASHIER_MANUAL(3);

        private final int manualType;

        // Constructor for ENUM
        ManualType(int manualType) {
            this.manualType = manualType;
        }

        // Returns the value of the action_id equivalent to database
        public int getValue() {
            return this.manualType;
        }
    }
}
