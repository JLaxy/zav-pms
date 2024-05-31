/*
 * User Object
 */

package models.schemas;

import enums.AccountStatuses;
import enums.LevelOfAccesses;

public class User {
    int id, level_of_access_id, account_status_id, unique_question_id;
    String uname, pass, email, fname, lname, unique_question_answer;

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

    public String getLevel_of_access_id() {
        if (this.level_of_access_id == LevelOfAccesses.AccessLevel.ADMIN.getValue())
            return "Admin";
        else if (this.level_of_access_id == LevelOfAccesses.AccessLevel.CASHIER.getValue())
            return "Cashier";
        else if (this.level_of_access_id == LevelOfAccesses.AccessLevel.KITCHEN_STAFF.getValue())
            return "Kitchen Staff";
        return "error";
    }

    public String getAccount_status_id() {
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
}
