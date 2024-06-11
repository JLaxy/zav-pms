package models.schemas;

import java.time.LocalDateTime;

import models.helpers.DateHelper;

public class DatabaseLog {
    private int id, user_id, backup_type_id;
    private String dateTime, system_name, dateString, timeString;

    public DatabaseLog(int id, int user_id, int backup_type_id, String dateTime, String system_name) {
        this.id = id;
        this.user_id = user_id;
        this.backup_type_id = backup_type_id;
        this.dateTime = dateTime;
        this.system_name = system_name;
        getStringEquivalents();
    }

    private void getStringEquivalents() {
        LocalDateTime timeObject = DateHelper.stringToDate(this.dateTime);
        this.dateString = timeObject.toLocalDate().toString();
        this.timeString = timeObject.toLocalTime().toString();
    }

    public String getDateString() {
        return this.dateString;
    }

    public String getTimeString() {
        return this.timeString;
    }

    public int getId() {
        return this.id;
    }

    public int getUser_id() {
        return this.user_id;
    }

    public int getBackup_type_id() {
        return this.backup_type_id;
    }

    public String getDateTime() {
        return this.dateTime;
    }

    public String getSystem_name() {
        return this.system_name;
    }
}
