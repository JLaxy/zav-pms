/*
 * Parent Class of all Controllers
 */

package controllers;

import models.helpers.RootSwitcher;

public class ParentController {

    protected ParentController previousController;
    protected RootSwitcher rootSwitcher;

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
    public void setRootSwitcher(RootSwitcher rootSwitcher) {
        this.rootSwitcher = rootSwitcher;
    }

    // Returns reference of root switcher
    public RootSwitcher getRootSwitcher() {
        return this.rootSwitcher;
    }
}
