/*
 * Controller handling navigation on logged in view
 */

package controllers;

import javax.swing.JOptionPane;

import controllers.homepage.AdminHomePageController;
import controllers.homepage.CashierHomePageController;
import controllers.homepage.KitchenStaffHomePageController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import models.PageNavigatorViewModel;
import models.helpers.PopupDialog;
import models.helpers.RootSwitcher;

public class PageNavigatorViewController extends ParentController {
    @FXML
    private Label pageTitleLabel, unameLabel;
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private StackPane mainStackPane;

    private RootSwitcher borderPaneRootSwitcher;
    private PageNavigatorViewModel model;

    @FXML
    public void initialize() {
        // Initializing rootswitcher for borderpane
        this.borderPaneRootSwitcher = new RootSwitcher(this.mainBorderPane, this);
        System.out.println("Initialized border pane switcher!");
        this.model = new PageNavigatorViewModel(this);
    }

    // Syncs screen elements with passed user info
    public void configureScreen() {
        System.out.println("Initializing HomePage with user info: " + this.loggedInUserInfo);
        // Checks if valid user info
        if (this.loggedInUserInfo != null && this.loggedInUserInfo.getUname() != null) {
            String uname = this.loggedInUserInfo.getUname();

            System.out.println("Initializing with username: " + uname); // Debugging statement

            unameLabel.setText(uname);
        } else {
            System.out.println("User info is not set or does not contain 'uname' key."); // Debugging statement
        }

        showReferences();
        // Display appropriate homepage according to LOA
        navigateToHomepage();
    }

    // Navigates Center BorderPane to Appropriate Homepage
    private void navigateToHomepage() {
        try {
            // Get level of access
            String loa = this.loggedInUserInfo.getLevel_of_access_id_string();
            FXMLLoader rootLoader = null;

            // Assign FXML file to be loaded according to LOA
            switch (loa) {
                // Admin
                case "Admin":
                    rootLoader = new FXMLLoader(getClass().getResource("../views/fxmls/homepage/AdminHomePage.fxml"));
                    break;
                // Kitchen Staff
                case "Kitchen Staff":
                    rootLoader = new FXMLLoader(
                            getClass().getResource("../views/fxmls/homepage/KitchenStaffHomePage.fxml"));
                    break;
                // Cashier
                case "Cashier":
                    rootLoader = new FXMLLoader(getClass().getResource("../views/fxmls/homepage/CashierHomePage.fxml"));
                    break;
                default:
                    break;
            }

            // Initialize Controller
            Parent root = rootLoader.load();
            ParentController nextController = rootLoader.getController();
            // Pass references
            nextController.initializeReferences_BP(this.zavPMSDB, this.borderPaneRootSwitcher);
            nextController.syncLoggedInUserInfo(this.loggedInUserInfo);

            // Configure Homescreens
            if (loa.compareTo("Admin") == 0)
                ((AdminHomePageController) nextController).configureScreen();
            else if (loa.compareTo("Kitchen Staff") == 0)
                ((KitchenStaffHomePageController) nextController).configureScreen();
            else if (loa.compareTo("Cashier") == 0)
                ((CashierHomePageController) nextController).configureScreen();

            // Change View in BorderPane
            this.borderPaneRootSwitcher.nextView_BP(root, "HOMEPAGE");

        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
        }
    }

    // Returns page title label to allow to change the label
    public Label getPageTitleLabel() {
        return this.pageTitleLabel;
    }

    // Logout function
    @FXML
    private void logout(ActionEvent e) {
        // Show confirmation dialog
        if (PopupDialog.confirmOperationDialog("Are you sure you want to logout?") == JOptionPane.YES_OPTION) {
            // Logging user logout action to database
            this.model.logUserLogout(this.loggedInUserInfo.getId(),
                    this.loggedInUserInfo.getUname());
            System.out.println("logging out...");
            this.borderPaneRootSwitcher.getPageNavigatorViewController().rootSwitcher.logout();
        }
    }

    // Forces user to logout
    public void forceLogout() {
        PopupDialog.showInfoDialog("Forced Logout", "You will be logged out.");
        // Logging user logout action to database
        this.model.logUserLogout(this.loggedInUserInfo.getId(),
                this.loggedInUserInfo.getUname());
        System.out.println("logging out...");
        this.borderPaneRootSwitcher.getPageNavigatorViewController().rootSwitcher.logout();
    }

    // Returns main stack pane
    public StackPane getMainStackPane() {
        return this.mainStackPane;
    }

}