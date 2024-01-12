package controllers;

import models.helpers.RootSwitcher;

public class ParentController {
    protected ParentController previousController;
    protected RootSwitcher rootSwitcher;

    public void setPreviousController(ParentController prevController) {
        this.previousController = prevController;
    }

    public void deletePreviousController() {
        this.previousController = null;
    }

    public void setRootSwitcher(RootSwitcher rootSwitcher) {
        this.rootSwitcher = rootSwitcher;
    }

    public RootSwitcher getRootSwitcher() {
        return this.rootSwitcher;
    }
}
