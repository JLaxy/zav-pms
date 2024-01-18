/*
 * Parent Class of all Controllers
 */

package controllers;

import models.helpers.RootSwitcher;
import models.helpers.database.DBManager;

public class ParentController {

    protected ParentController previousController;
    protected RootSwitcher rootSwitcher;
    protected DBManager zavPMSDB;

    // Sets reference to controller of previous scene
    public void setPreviousController(ParentController prevController) {
        this.previousController = prevController;
    }

    // Deletes reference to controller of previous scene; used to make-iwas of
    // memory leak
    public void deletePreviousController() {
        this.previousController = null;
    }

    // Sets reference to root switcher
    private void setRootSwitcher(RootSwitcher rootSwitcher) {
        this.rootSwitcher = rootSwitcher;
    }

    // Returns reference of root switcher
    public RootSwitcher getRootSwitcher() {
        return this.rootSwitcher;
    }

    // Sets reference to DB Manager
    private void setDBManager(DBManager zavPMSDB) {
        this.zavPMSDB = zavPMSDB;
    }

    // Method to easily configure the references of the controller of the next view
    public void initializeReferences(DBManager dbManager, RootSwitcher rootSwitcher) {
        setDBManager(dbManager);
        setRootSwitcher(rootSwitcher);
    }
}
