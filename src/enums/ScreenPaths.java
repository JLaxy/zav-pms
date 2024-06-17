/*
 * Contains the paths of all of the screens
 * 
 * For better readability
 */

package enums;

public class ScreenPaths {
    public enum Paths {
        // SCREEN_NAME(fxml_path)

        // Login
        LOGIN("../views/fxmls/login/LoginView.fxml"),
        OTP_LOGIN("../../views/fxmls/login/OTPLoginView.fxml"),
        FORGOT_PASSWORD("../../views/fxmls/login/ForgotPasswordView.fxml"),
        PASSWORD_QUESTION("../../views/fxmls/login/PasswordQuestionView.fxml"),
        NEW_PASSWORD("../../views/fxmls/login/NewPasswordView.fxml"),
        PAGE_NAVIGATOR("../../views/fxmls/PageNavigatorView.fxml"),

        // Homepage
        ADMIN_HOMEPAGE("../views/fxmls/homepage/AdminHomePage.fxml"),
        KITCHEN_STAFF_HOMEPAGE("../views/fxmls/homepage/KitchenStaffHomePage.fxml"),
        CASHIER_HOMEPAGE("../views/fxmls/homepage/CashierHomePage.fxml"),

        // // Inventory
        INVENTORY("../../views/fxmls/inventory/InventoryView.fxml"),
        INVENTORY_REFILL_HISTORY("../../views/fxmls/inventory/InventoryRefillHistoryView.fxml"),
        // Product
        PRODUCT_INVENTORY("../../views/fxmls/inventory/ProductInventoryView.fxml"),
        REGISTER_NEW_FOOD_PRODUCT("../../views/fxmls/inventory/RegisterNewFoodProductView.fxml"),
        REGISTER_NEW_BEVERAGE_PRODUCT("../../views/fxmls/inventory/RegisterNewBeverageProductView.fxml"),
        // Stock
        STOCK_INVENTORY("../../views/fxmls/inventory/StockInventoryView.fxml"),
        REGISTER_NEW_STOCK("../../views/fxmls/inventory/RegisterNewStockView.fxml"),
        REGISTER_NEW_STOCK_TYPE("../../views/fxmls/inventory/RegisterNewStockTypeView.fxml"),
        VIEW_STOCK("../../views/fxmls/inventory/ViewStockInventoryView.fxml"),
        EDIT_STOCK("../../views/fxmls/inventory/EditStockInventoryView.fxml"),
        INCREMENT_STOCK_INVENTORY("../../views/fxmls/inventory/IncrementStockInventoryView.fxml"),

        // Report
        REPORT("../../views/fxmls/report/ReportView.fxml"),
        MANUAL_GENERATE_REPORT("../../views/fxmls/report/ManualGenerateReportView.fxml"),
        EDIT_AUTO_REPORT_GENERATION("../../views/fxmls/report/EditAutoReportGenerationView.fxml"),
        EDIT_AUTO_REPORT_DESTINATION_EMAIL("../../views/fxmls/report/EditAutoReportDestinationEmailView.fxml"),
        EDIT_AUTO_REPORT_TIME_INTERVAL("../../views/fxmls/report/EditAutoReportTimeIntervalView.fxml"),

        // Manage Accounts
        MANAGE_ACCOUNTS("../../views/fxmls/manageaccounts/ManageAccountsView.fxml"),
        VIEW_USER_LOGS("../../views/fxmls/manageaccounts/ViewUserLogsView.fxml"),
        USER_DETAILS("../../views/fxmls/manageaccounts/UserDetailsView.fxml"),

        // Maintenance
        MAINTENANCE("../../views/fxmls/maintenance/MaintenanceView.fxml"),
        MANUAL_BACKUP("../../views/fxmls/maintenance/ManualBackupView.fxml"),

        // Transactions
        TRANSACTION("../../views/fxmls/transactions/TransactionsView.fxml"),

        // Order
        ORDER("../../views/fxmls/order/OrderView.fxml");

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