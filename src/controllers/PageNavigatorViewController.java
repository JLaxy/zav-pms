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

import models.PageNavigatorViewModel;
import models.helpers.PopupDialog;
import models.helpers.RootSwitcher;

public class PageNavigatorViewController extends ParentController {
    @FXML
    private Label pageTitleLabel, unameLabel;
    @FXML
    private BorderPane mainBorderPane;

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
        if (this.loggedInUserInfo != null && this.loggedInUserInfo.containsKey("uname")) {
            String uname = this.loggedInUserInfo.get("uname");

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
            String loa = this.loggedInUserInfo.get("level_of_access_id");
            FXMLLoader rootLoader = null;

            // Assign FXML file to be loaded according to LOA
            switch (loa) {
                // Admin
                case "1":
                    rootLoader = new FXMLLoader(getClass().getResource("../views/fxmls/homepage/AdminHomePage.fxml"));
                    break;
                // Kitchen Staff
                case "2":
                    rootLoader = new FXMLLoader(
                            getClass().getResource("../views/fxmls/homepage/KitchenStaffHomePage.fxml"));
                    break;
                // Cashier
                case "3":
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
            if (loa.compareTo("1") == 0)
                ((AdminHomePageController) nextController).configureScreen();
            else if (loa.compareTo("2") == 0)
                ((KitchenStaffHomePageController) nextController).configureScreen();
            else if (loa.compareTo("3") == 0)
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
            this.model.logUserLogout(Integer.valueOf(this.loggedInUserInfo.get("id")),
                    this.loggedInUserInfo.get("uname"));
            System.out.println("logging out...");
            this.borderPaneRootSwitcher.getPageNavigatorViewController().rootSwitcher.logout();
        }
    }
}
