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
        LOGIN_ATTEMPT(1, "Login Attempted"),
        INITIATED_OTP(2, "Initiated OTP"),
        CANCELLED_OTP(3, "Cancelled OTP"),
        FAILED_OTP(4, "Failed OTP"),
        INITIATED_PASSWORD_RESET(5, "Initiated Password Reset"),
        CANCELLED_PASSWORD_RESET(6, "Cancelled Password Reset"),
        SUCCESS_PASSWORD_RESET(7, "Successully Reset Password"),
        SUCCESSFUL_LOGIN(8, "Login Success"),
        USER_LOGOUT(9, "Logout Success"),
        VIEWED_ACCOUNT_DETAILS(10, "Viewed Account Details"),
        UPDATED_USER_ACCOUNT(11, "Updated Account Details"),
        REGISTERED_NEW_USER(12, "Registered New User"),
        VIEWED_USER_LOGS(13, "Viewed User Logs"),
        REGISTERED_NEW_STOCK_TYPE(14, "Registered New Stock Type"),
        REGISTERED_NEW_STOCK(15, "Registered New Stock"),
        MANUAL_DATABASE_BACKUP(16, "Manually Backed-Up Database"),
        AUTO_DATABASE_BACKUP(17, "Automatically Backed-Up Database"),
        EDITED_DATABASE_BACKUP_LOCATION(18, "Edited Database Backup Location"),
        RESTORED_DATABASE(19, "Restored Database using Backup"),
        ENABLED_DATABASE_AUTOBACKUP(20, "Enabled Database Auto Backup"),
        DISABLED_DATABASE_AUTOBACKUP(21, "Disabled Database Auto Backup"),
        EDITED_STOCK(22, "Edited Stock"),
        LOGGED_STOCK_PRODUCT_PURCHASE(23, "Logged New Stock/Product Purchase"),
        REGISTERED_NEW_BEVERAGE_PRODUCT(24,
                "Registered New Beverage Product"),
        REGISTERED_NEW_BEVERAGE_PRODUCT_VARIANT(25, "Registered New Beverage Product Variant"),
        REGISTERED_NEW_FOOD_PRODUCT(26, "Registered New Food Product"),
        REGISTERED_NEW_FOOD_PRODUCT_VARIANT(27, " Registered New Food Product Variant");

        private final int action_id;
        private final String action_string;

        // Constructor for ENUM
        Actions(int action_id, String action_string) {
            this.action_id = action_id;
            this.action_string = action_string;
        }

        // Returns the value of the action_id equivalent to database
        public int getValue() {
            return this.action_id;
        }

        public String getString() {
            return this.action_string;
        }
    }
}
