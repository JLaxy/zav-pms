/*
 * Parent Class of all Controllers
 */

package controllers;

import models.helpers.RootSwitcher;
import models.helpers.database.DBManager;

public class ParentController {

    protected RootSwitcher rootSwitcher;
    protected DBManager zavPMSDB;

    // Sets reference to root switcher
    private void setRootSwitcher(RootSwitcher rootSwitcher) {
        this.rootSwitcher = rootSwitcher;
    }

    // Sets reference to DB Manager
    private void setDBManager(DBManager zavPMSDB) {
        this.zavPMSDB = zavPMSDB;
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
}
