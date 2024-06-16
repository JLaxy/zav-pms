/*
 * Contains the paths of all of the screens
 * 
 * For better readability
 */

package enums;

public class ScreenPaths {
    public enum Paths {
        // SCREEN_NAME(fxml_path)

        // Inventory
        INCREMENT_STOCK_INVENTORY("../../views/fxmls/inventory/IncrementStockInventoryView.fxml");

        private final String path;

        // Constructor for ENUM
        Paths(String path) {
            this.path = path;
        }

        // Returns the value of the level_of_access equivalent to database
        public String getPath() {
            return this.path;
        }
    }
}