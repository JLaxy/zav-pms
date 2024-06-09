/*
 * Contains all of the pre-defined values in Settings
 * 
 * For better readability
 */

package enums;

public class ProgramSettings {
    public enum Setting {
        // SETTING_NAME(setting_in_json)
        COOLDOWN("cooldown"),
        BACKUP_LOCATION("backupLocation");

        private final String setting;

        // Constructor for ENUM
        Setting(String setting) {
            this.setting = setting;
        }

        // Returns the value of the level_of_access equivalent to database
        public String getValue() {
            return this.setting;
        }
    }
}