/*
 * Parent Class of all Controllers
 */

package controllers;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import models.helpers.PopupDialog;
import models.helpers.RootSwitcher;
import models.helpers.database.DBManager;
import models.schemas.User;

public class ParentController {

    protected RootSwitcher rootSwitcher, borderPaneRootSwitcher;
    protected DBManager zavPMSDB;

    protected User loggedInUserInfo;

    // Sets reference to root switcher
    private void setRootSwitcher(RootSwitcher rootSwitcher) {
        this.rootSwitcher = rootSwitcher;
    }

    // Sets reference to border pane root switcher
    private void setRootSwitcher_BP(RootSwitcher rootSwitcher) {
        this.borderPaneRootSwitcher = rootSwitcher;
    }

    // Sets reference to DB Manager
    private void setDBManager(DBManager zavPMSDB) {
        this.zavPMSDB = zavPMSDB;
    }

    // Initializes the View and Controller of the next screen
    public ParentController initializeNextScreen(String fxmlPath, User userInfo) {
        try {
            // Loading View
            FXMLLoader rootLoader = new FXMLLoader(getClass().getResource(fxmlPath));
            // FXML can only be loaded once
            Parent root = rootLoader.load();
            // Retrieving Controller
            ParentController nextController = rootLoader.getController();
            // Passing References
            nextController.initializeReferences(this.zavPMSDB, this.rootSwitcher);
            nextController.syncLoggedInUserInfo(userInfo);
            // Changing view
            this.rootSwitcher.nextView(root);
            // Returning Controller
            return nextController;
        } catch (IOException e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
            return null;
        }
    }

    // Initializes the View and Controller of the next screen for MainBorderPane
    public ParentController initializeNextScreen_BP(String fxmlPath, User userInfo, String pageTitle) {
        try {
            // Loading View
            FXMLLoader rootLoader = new FXMLLoader(getClass().getResource(fxmlPath));
            // FXML can only be loaded once
            Parent root = rootLoader.load();
            // Retrieving Controller
            ParentController nextController = rootLoader.getController();
            // Passing References
            nextController.initializeReferences_BP(this.zavPMSDB, this.borderPaneRootSwitcher);
            nextController.syncLoggedInUserInfo(userInfo);
            // Changing view
            this.borderPaneRootSwitcher.nextView_BP(root, pageTitle);
            // Returning Controller
            return nextController;
        } catch (IOException e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
            return null;
        }
    }

    // Initializes the View and Controller of the popup dialog for MainBorderPane
    public ParentController initializePopUpDialog(String fxmlPath, User userInfo) {
        try {
            // Loading View
            FXMLLoader rootLoader = new FXMLLoader(getClass().getResource(fxmlPath));
            // FXML can only be loaded once
            Parent root = rootLoader.load();
            // Retrieving Controller
            ParentController nextController = rootLoader.getController();
            // Passing References
            nextController.initializeReferences_BP(this.zavPMSDB, this.borderPaneRootSwitcher);
            nextController.syncLoggedInUserInfo(userInfo);
            // Changing view
            this.borderPaneRootSwitcher.showPopUpDialog(root);
            // Returning Controller
            return nextController;
        } catch (IOException e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
            return null;
        }
    }

    // Returns reference to DBManager
    public DBManager getDBManager() {
        return this.zavPMSDB;
    }

    // Returns reference of root switcher
    public RootSwitcher getRootSwitcher() {
        return this.rootSwitcher;
    }

    // Method to easily configure the references of the controller of the next view
    public void initializeReferences(DBManager zavPMSDB, RootSwitcher rootSwitcher) {
        setDBManager(zavPMSDB);
        setRootSwitcher(rootSwitcher);
    }

    // Method to easily configure the references of the controller of the next
    // border pane view
    public void initializeReferences_BP(DBManager zavPMSDB, RootSwitcher rootSwitcher) {
        setDBManager(zavPMSDB);
        setRootSwitcher_BP(rootSwitcher);
    }

    // Passes information of logged in user to other controller
    public void syncLoggedInUserInfo(User userInfo) {
        this.loggedInUserInfo = userInfo;
    }

    // Debug function; shows all of the references the controller has
    public void showReferences() {
        System.out.println("---\nReferences of " + this.getClass().getName());
        System.out.println("RootSwitcher: " + this.rootSwitcher);
        System.out.println("Border Pane Root Switcher: " + this.borderPaneRootSwitcher);
        System.out.println("zavPMSDB: " + this.zavPMSDB);
        System.out.println("LoggedInUserInfo: " + this.loggedInUserInfo);
        System.out.println("---");
    }
}
