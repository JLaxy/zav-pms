package controllers.report;

import controllers.ParentController;
import enums.ProgramSettings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import models.helpers.JSONManager;
import models.helpers.PopupDialog;

public class EditAutoReportTimeIntervalController extends ParentController {

    @FXML
    private ComboBox<String> timeIntervalComboBox;

    @FXML
    private void initialize() {
        this.configureComboBox();
    }

    private void configureComboBox() {
        ObservableList<String> timeIntervals = FXCollections.observableArrayList();
        timeIntervals.add("daily");
        timeIntervals.add("weekly");
        timeIntervals.add("monthly");

        this.timeIntervalComboBox.setItems(timeIntervals);
        this.timeIntervalComboBox.getSelectionModel().selectFirst();
    }

    @FXML
    private void goBack() {
        System.out.println("going back...");
        this.borderPaneRootSwitcher.goBack_BP();
    }

    @FXML
    private void update() {
        String interval = this.timeIntervalComboBox.getValue();
        if (new JSONManager().writeToSetting(ProgramSettings.Setting.REPORT_TIME_INTERVAL.getValue(),
                this.timeIntervalComboBox.getValue())) {
            PopupDialog.showInfoDialog("Updated Report Time Interval",
                    "Updated Automatic Report Time Interval to " + interval
                            + ".\nPlease relaunch the program to save the changes.");
            this.borderPaneRootSwitcher.goBack_BP();
        }
    }

}
