/*
 * Parent Class of all Controllers
 */

package controllers;

import java.io.IOException;
import java.util.Map;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import models.helpers.PopupDialog;
import models.helpers.RootSwitcher;
import models.helpers.database.DBManager;

public class ParentController {

    protected RootSwitcher rootSwitcher;
    protected DBManager zavPMSDB;

    protected Map<String, String> loggedInUserInfo;

    // Sets reference to root switcher
    private void setRootSwitcher(RootSwitcher rootSwitcher) {
        this.rootSwitcher = rootSwitcher;
    }

    // Sets reference to DB Manager
    private void setDBManager(DBManager zavPMSDB) {
        this.zavPMSDB = zavPMSDB;
    }

    // Initializes the View and Controller of the next screen
    public ParentController initializeNextScreen(String fxmlPath, Map<String, String> userInfo) {
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

    // Passes information of logged in user to other controller
    public void syncLoggedInUserInfo(Map<String, String> userInfo) {
        this.loggedInUserInfo = userInfo;
    }
}
