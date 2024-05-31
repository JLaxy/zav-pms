/*
 * Contains all of the pre-defined Levels of Access in the database
 * with their respective action_id
 * 
 * For better readability
 */

package enums;

public class LevelOfAccesses {
    public enum AccessLevel {
        // LEVEL_OF_ACCESS(level_of_access_id_in_db)
        ADMIN(1),
        KITCHEN_STAFF(2),
        CASHIER(3);

        private final int level_of_access_id;

        // Constructor for ENUM
        AccessLevel(int level_of_access_id) {
            this.level_of_access_id = level_of_access_id;
        }

        // Returns the value of the level_of_access equivalent to database
        public int getValue() {
            return this.level_of_access_id;
        }
    }
}
