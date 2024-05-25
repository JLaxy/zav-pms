/*
 * Contains all of the pre-defined Account Statuses in the database
 * with their respective action_id
 * 
 * For better readability
 */

package enums;

public class AccountStatuses {
    public enum Status {
        // ACTION_NAME(action_id_in_database)
        ACTIVE(1),
        DISABLED(2);

        private final int action_id;

        // Constructor for ENUM
        Status(int action_id) {
            this.action_id = action_id;
        }

        // Returns the value of the action_id equivalent to database
        public int getValue() {
            return this.action_id;
        }
    }
}
