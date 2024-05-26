package models;

import controllers.PageNavigatorViewController;
import enums.UserLogActions;
import models.helpers.DateHelper;

public class PageNavigatorViewModel {

    private PageNavigatorViewController controller;

    public PageNavigatorViewModel(PageNavigatorViewController controller) {
        this.controller = controller;
    }

    // Logs user logout to database
    public void logUserLogout(int id, String uname) {
        this.controller.getDBManager().query.logAction(id, uname, UserLogActions.Actions.USER_LOGOUT.getValue(),
                DateHelper.getCurrentDateTimeString());
    }

}
