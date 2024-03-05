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

public class ParentController {

    protected RootSwitcher rootSwitcher;
    protected DBManager zavPMSDB;

    protected int loggedInUser;

    // Sets reference to root switcher
    private void setRootSwitcher(RootSwitcher rootSwitcher) {
        this.rootSwitcher = rootSwitcher;
    }

    // Sets reference to DB Manager
    private void setDBManager(DBManager zavPMSDB) {
        this.zavPMSDB = zavPMSDB;
    }

    // Initializes the View and Controller of the next screen
    public ParentController initializeNextScreen(String fxmlPath, int userID) {
        try {
            // Loading View
            FXMLLoader rootLoader = new FXMLLoader(getClass().getResource(fxmlPath));
            // FXML can only be loaded once
            Parent root = rootLoader.load();
            // Retrieving Controller
            ParentController nextController = rootLoader.getController();
            // Passing References
            nextController.initializeReferences(this.zavPMSDB, this.rootSwitcher);
            nextController.setLoggedInUser(userID);
            // Changing view
            this.rootSwitcher.nextView(root);
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

    // Keeps track of the user currently logged in
    private void setLoggedInUser(int id) {
        this.loggedInUser = id;
    }
}
