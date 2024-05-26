/*
 * Contains all of the pre-defined User Log Actions in the database
 * with their respective action_id
 * 
 * For better readability
 */

package enums;

public class UserLogActions {
    public enum Actions {
        // ACTION_NAME(action_id_in_database)
        LOGIN_ATTEMPT(1),
        INITIATED_OTP(2),
        CANCELLED_OTP(3),
        FAILED_OTP(4),
        INITIATED_PASSWORD_RESET(5),
        CANCELLED_PASSWORD_RESET(6),
        SUCCESS_PASSWORD_RESET(7),
        SUCCESSFUL_LOGIN(8),
        USER_LOGOUT(9);

        private final int action_id;

        // Constructor for ENUM
        Actions(int action_id) {
            this.action_id = action_id;
        }

        // Returns the value of the action_id equivalent to database
        public int getValue() {
            return this.action_id;
        }
    }
}
