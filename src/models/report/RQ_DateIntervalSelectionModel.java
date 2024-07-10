package models.report;

import controllers.report.RQ_DateIntervalSelectionController;
import enums.DatabaseLists;
import javafx.collections.ObservableList;

public class RQ_DateIntervalSelectionModel {

    private RQ_DateIntervalSelectionController controller;

    public RQ_DateIntervalSelectionModel(RQ_DateIntervalSelectionController controller) {
        this.controller = controller;
    }

    public ObservableList<String> getDateIntervals() {
        return this.controller.getDBManager().query.getListOnDatabase(DatabaseLists.Lists.REPORT_TIME_INTERVALS);
    }

}
