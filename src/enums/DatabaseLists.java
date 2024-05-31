/*
 * Contains all of the pre-defined list contained in database
 * 
 * For better readability
 */

package enums;

public class DatabaseLists {
    public enum Lists {
        // LIST(action_id_in_database)
        SECURITY_QUESTIONS(1),
        ACCOUNT_STATUSES(2),
        LEVELS_OF_ACCESS(3);

        private final int database_list;

        // Constructor for ENUM
        Lists(int database_list) {
            this.database_list = database_list;
        }

        // Returns the value of the action_id equivalent to database
        public int getValue() {
            return this.database_list;
        }
    }
}
