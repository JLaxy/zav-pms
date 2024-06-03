/*
 * User Object
 */

package models.schemas;

import java.lang.reflect.Field;
import java.util.ArrayList;

import enums.AccountStatuses;
import enums.LevelOfAccesses;

public class User {
    private int id, level_of_access_id, account_status_id, unique_question_id;
    private String uname, pass, email, fname, lname, unique_question_answer, level_of_access_id_string,
            account_status_id_string;

    // Null User Constructor
    public User() {

    }

    public User(int id, String uname, String pass, String email, int level_of_access_id, String fname, String lname,
            int account_status_id, int unique_question_id, String unique_question_answer) {
        this.id = id;
        this.uname = uname;
        this.pass = pass;
        this.email = email;
        this.level_of_access_id = level_of_access_id;
        this.fname = fname;
        this.lname = lname;
        this.account_status_id = account_status_id;
        this.unique_question_id = unique_question_id;
        this.unique_question_answer = unique_question_answer;

        // Updates string equivalents of LOA and Account Status
        updateStringEquivalents();
    }

    // Updates string equivalents of LOA and Account Status
    public void updateStringEquivalents() {
        if (this.level_of_access_id == LevelOfAccesses.AccessLevel.ADMIN.getValue())
            this.level_of_access_id_string = "Admin";
        else if (this.level_of_access_id == LevelOfAccesses.AccessLevel.CASHIER.getValue())
            this.level_of_access_id_string = "Cashier";
        else if (this.level_of_access_id == LevelOfAccesses.AccessLevel.KITCHEN_STAFF.getValue())
            this.level_of_access_id_string = "Kitchen Staff";

        if (this.account_status_id == AccountStatuses.Status.ACTIVE.getValue())
            this.account_status_id_string = "Active";
        else if (this.account_status_id == AccountStatuses.Status.DISABLED.getValue())
            this.account_status_id_string = "Disabled";
    }

    // Returns copy of this user
    public User getCopy() {
        return new User(this.id, this.uname, this.pass, this.email, this.level_of_access_id, this.fname, this.lname,
                this.account_status_id, this.unique_question_id,
                this.unique_question_answer);
    }

    public int getId() {
        return this.id;
    }

    public int getLevel_of_access_id() {
        return this.level_of_access_id;
    }

    public String getLevel_of_access_string() {
        if (this.level_of_access_id == LevelOfAccesses.AccessLevel.ADMIN.getValue())
            return "Admin";
        else if (this.level_of_access_id == LevelOfAccesses.AccessLevel.CASHIER.getValue())
            return "Cashier";
        else if (this.level_of_access_id == LevelOfAccesses.AccessLevel.KITCHEN_STAFF.getValue())
            return "Kitchen Staff";
        return "error";
    }

    public int getAccount_status_id() {
        return this.account_status_id;
    }

    public String getAccount_status_string() {
        if (this.account_status_id == AccountStatuses.Status.ACTIVE.getValue())
            return "Active";
        else if (this.level_of_access_id == AccountStatuses.Status.DISABLED.getValue())
            return "Disabled";
        return "error";
    }

    public int getUniqueQuestionID() {
        return this.unique_question_id;
    }

    public String getUname() {
        return this.uname;
    }

    public String getPass() {
        return this.pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getEmail() {
        return this.email;
    }

    public String getFName() {
        return this.fname;
    }

    public String getLName() {
        return this.lname;
    }

    public String getUniqueQuestionAnswer() {
        return this.unique_question_answer;
    }

    public String getLevel_of_access_id_string() {
        return this.level_of_access_id_string;
    }

    public String getAccount_status_id_string() {
        return this.account_status_id_string;
    }

    // Activates / Disables Account
    public void toggleAccountStatus() {
        // If active, then disable
        if (this.account_status_id == AccountStatuses.Status.ACTIVE.getValue()) {
            this.account_status_id = AccountStatuses.Status.DISABLED.getValue();
            return;
        }
        // Activate
        this.account_status_id = AccountStatuses.Status.ACTIVE.getValue();
    }

    // Returns string that describes account changes
    public static String[] getAccountChangesMessage(User oldUserInfo, User newUserInfo) {

        ArrayList<String> changes = new ArrayList<String>();
        // Load all fields in the class (private included)
        Field[] attributes = oldUserInfo.getClass().getDeclaredFields();

        // Iterate through attributes
        for (Field field : attributes) {
            try {
                // If value of attribute on previous is different to new
                if (field.get(oldUserInfo).toString().compareTo(field.get(newUserInfo).toString()) != 0) {
                    switch (field.getName()) {
                        // For account status id
                        case "account_status_id":
                            changes.add("changed account status" + " from "
                                    + (field.get(oldUserInfo).toString().compareTo("2") == 0 ? "Disabled" : "Enabled")
                                    + " to "
                                    + (field.get(newUserInfo).toString().compareTo("2") == 0 ? "Disabled" : "Enabled"));
                            System.out.println("changed account status" + " from "
                                    + (field.get(oldUserInfo).toString().compareTo("2") == 0 ? "Disabled" : "Enabled")
                                    + " to "
                                    + (field.get(newUserInfo).toString().compareTo("2") == 0 ? "Disabled" : "Enabled"));
                            break;
                        // For other attributes
                        default:
                            changes.add("changed " + field.getName() + " from " + field.get(oldUserInfo) + " to "
                                    + field.get(newUserInfo));
                            System.out.println("changed " + field.getName() + " from " + field.get(oldUserInfo) + " to "
                                    + field.get(newUserInfo));
                            break;
                    }
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        // Return list of changes in array
        return changes.toArray(new String[changes.size()]);
    }

    public void showValues(){
        // Load all fields in the class (private included)
        Field[] attributes = this.getClass().getDeclaredFields();

        for (Field field : attributes) {
            try {
                System.out.println(field.getName() + ": " + field.get(this));
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }

}
