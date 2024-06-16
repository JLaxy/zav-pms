/*
 * Blueprint for User Logs
 */
package models.schemas;

import java.time.LocalDateTime;

import enums.UserLogActions;
import models.helpers.DateHelper;

public class UserLog {
    private int id, user_id, action_id;
    private LocalDateTime dateObject;
    private String dateString, parameter, timeString, uname, actionString, date;

    public UserLog(int id, int user_id, int action_id, String date, String parameter) {
        this.id = id;
        this.user_id = user_id;
        this.action_id = action_id;
        this.date = date;
        this.parameter = parameter;
        getEquivalents();
    }

    // Sets String equivalents of time and date
    private void getEquivalents() {
        this.dateObject = DateHelper.stringToDateTime(this.date);
        this.dateString = dateObject.toLocalDate().toString();
        this.timeString = dateObject.toLocalTime().toString();
        this.actionString = getActionString(this.action_id);
    }

    // Returns the string equivalent of the action
    private String getActionString(int id) {
        for (UserLogActions.Actions action : UserLogActions.Actions.values()) {
            if (action.getValue() == id)
                return action.getString();
        }
        return null;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public int getId() {
        return this.id;
    }

    public int getUser_id() {
        return this.user_id;
    }

    public int getAction_id() {
        return this.action_id;
    }

    public LocalDateTime getDateObject() {
        return this.dateObject;
    }

    public String getParameter() {
        return this.parameter;
    }

    public String getDateString() {
        return this.dateString;
    }

    public String getTimeString() {
        return this.timeString;
    }

    public String getUname() {
        return this.uname;
    }

    public String getActionString() {
        return this.actionString;
    }

    public String getDate() {
        return date;
    }
}
