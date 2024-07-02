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

        /// // // Inventory
        INVENTORY("../../views/fxmls/inventory/InventoryView.fxml"),
        INVENTORY_REFILL_HISTORY("../../views/fxmls/inventory/InventoryRefillHistoryView.fxml"),
        EXPIRING_ITEMS("../../views/fxmls/inventory/ViewExpiringItemsView.fxml"),
        CRITICAL_ITEMS("../../views/fxmls/inventory/ViewCriticalLevelsView.fxml"),
        // // Product
        PRODUCT_INVENTORY("../../views/fxmls/inventory/ProductInventoryView.fxml"),
        // Food
        REGISTER_NEW_FOOD_VARIANT("../../views/fxmls/inventory/RegisterNewFoodVariantView.fxml"),
        REGISTER_NEW_FOOD_POPUP("../../views/fxmls/inventory/RegisterNewFoodPopupView.fxml"),
        REGISTER_NEW_FOOD_VARIANT_DETAILS("../../views/fxmls/inventory/RegisterNewFoodVariantDetailsView.fxml"),
        VIEW_FOOD_PRODUCT("../../views/fxmls/inventory/ViewFoodProductView.fxml"),
        EDIT_FOOD_PRODUCT("../../views/fxmls/inventory/EditFoodPopup.fxml"),
        // Beverage
        REGISTER_NEW_BEVERAGE_VARIANT("../../views/fxmls/inventory/RegisterNewBeverageVariantView.fxml"),
        REGISTER_NEW_BEVERAGE_POPUP("../../views/fxmls/inventory/RegisterNewBeveragePopupView.fxml"),
        REGISTER_NEW_BEVERAGE_VARIANT_DETAILS("../../views/fxmls/inventory/RegisterNewBeverageVariantDetailsView.fxml"),
        VIEW_BEVERAGE_PRODUCT("../../views/fxmls/inventory/ViewBeverageProductView.fxml"),
        EDIT_BEVERAGE_PRODUCT("../../views/fxmls/inventory/EditBeveragePopup.fxml"),
        INCREASE_BEVERAGE_PRODUCT("../../views/fxmls/inventory/IncreaseBeveragePopup.fxml"),
        SELECT_BEVERAGE_DECREASE("../../views/fxmls/inventory/SelectBeverageDecrease.fxml"),
        // Stock
        STOCK_INVENTORY("../../views/fxmls/inventory/StockInventoryView.fxml"),
        REGISTER_NEW_STOCK("../../views/fxmls/inventory/RegisterNewStockView.fxml"),
        REGISTER_NEW_STOCK_TYPE("../../views/fxmls/inventory/RegisterNewStockTypeView.fxml"),
        VIEW_STOCK("../../views/fxmls/inventory/ViewStockInventoryView.fxml"),
        EDIT_STOCK("../../views/fxmls/inventory/EditStockInventoryView.fxml"),
        INCREMENT_STOCK_INVENTORY("../../views/fxmls/inventory/IncrementStockInventoryView.fxml"),
        SELECT_STOCK("../../views/fxmls/inventory/SelectStockView.fxml"),
        SELECT_STOCK_DECREASE("../../views/fxmls/inventory/SelectStockDecrease.fxml"),

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
        ORDER("../../views/fxmls/order/OrderView.fxml"),
        CREATE_ORDER("../../views/fxmls/order/CreateOrder.fxml"),
        VIEW_ORDER("../../views/fxmls/order/ViewOrder.fxml"),
        ADDING_ORDER_PROMPT("../../views/fxmls/order/AddingOrderPrompt.fxml"),
        DISCOUNT_ORDERS("../../views/fxmls/order/DiscountOrders.fxml"),
        SELECT_DISCOUNT_CARD("../../views/fxmls/order/SelectDiscountCard.fxml"),
        NEW_DISCOUNT_CARD("../../views/fxmls/order/NewDiscountCard.fxml"),
        VIEW_BACKORDER("../../views/fxmls/order/ViewBackorder.fxml"),
        SELECT_SIZE("../../views/fxmls/order/SelectSize.fxml"),

        // Reusable
        SET_QUANTITY("../../views/fxmls/reusables/SetQuantityView.fxml"),
        SET_DECREASE_QUANTITY("../../views/fxmls/reusables/SetDecreaseQuantityView.fxml"),
        SET_BEVERAGE_DECREASE_QUANTITY("../../views/fxmls/reusables/SetBeverageDecreaseQuantityView.fxml"),
        SET_FOOD_DECREASE_QUANTITY("../../views/fxmls/reusables/SetFoodDecreaseQuantityView.fxml"),

        //HELP
        HELP("../../views/fxmls/help/HelpPageView.fxml"),
        GENERAL("../../views/fxmls/help/GenPageView.fxml"),
        FAQS("../../views/fxmls/help/FaqPageView.fxml"),
        HOW_TO("../../views/fxmls/help/HelpPageView.fxml"),

        //ABOUT
        ABOUT("../../views/fxmls/about/AboutPage.fxml");

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