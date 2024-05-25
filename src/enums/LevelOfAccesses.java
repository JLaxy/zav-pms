/*
 * Contains all of the pre-defined Levels of Access in the database
 * with their respective action_id
 * 
 * For better readability
 */

package enums;

public class LevelOfAccesses {
    public enum AccessLevel {
        // ACTION_NAME(action_id_in_database)
        ADMIN(1),
        KITCHEN_STAFF(2),
        CASHIER(3);

        private final int action_id;

        // Constructor for ENUM
        AccessLevel(int action_id) {
            this.action_id = action_id;
        }

        // Returns the value of the action_id equivalent to database
        public int getValue() {
            return this.action_id;
        }
    }
}
