/*
 * Contains all of the pre-defined Account Statuses in the database
 * with their respective action_id
 * 
 * For better readability
 */

package enums;

public class AccountStatuses {
    public enum Status {
        // ACCOUNT_STATUS(account_status_id_in_database)
        ACTIVE(1),
        DISABLED(2);

        private final int account_status_id;

        // Constructor for ENUM
        Status(int account_status_id) {
            this.account_status_id = account_status_id;
        }

        // Returns the value of the account_status_id equivalent to database
        public int getValue() {
            return this.account_status_id;
        }
    }
}
