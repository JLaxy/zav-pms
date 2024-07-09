package models.report;

import controllers.report.PS_DateIntervalSelectionController;
import enums.DatabaseLists;
import javafx.collections.ObservableList;

public class DateIntervalSelectionModel {

    private PS_DateIntervalSelectionController controller;

    public DateIntervalSelectionModel(PS_DateIntervalSelectionController controller) {
        this.controller = controller;
    }

    public ObservableList<String> getDateIntervals() {
        return this.controller.getDBManager().query.getListOnDatabase(DatabaseLists.Lists.REPORT_TIME_INTERVALS);
    }

}
