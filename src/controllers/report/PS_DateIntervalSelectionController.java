package controllers.report;

import java.time.LocalDate;

import controllers.ParentController;
import enums.ReportTimePeriods;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import models.helpers.DateHelper;
import models.helpers.PopupDialog;
import models.report.DateIntervalSelectionModel;

public class PS_DateIntervalSelectionController extends ParentController {

    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<String> dateIntervalCBox;

    private DateIntervalSelectionModel model;
    private ManualGenerateReportController manualGenerateReportController;

    @FXML
    public void initialize(ManualGenerateReportController manualGenerateReportController) {
        this.model = new DateIntervalSelectionModel(this);
        this.datePicker.setValue(LocalDate.now());
        this.manualGenerateReportController = manualGenerateReportController;
        this.initializeComboBox();
    }

    public void initializeComboBox() {
        Task<Void> intervalRetriever = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                ObservableList<String> retrievedInterval = model.getDateIntervals();
                Platform.runLater(() -> {
                    dateIntervalCBox.setItems(retrievedInterval);
                    dateIntervalCBox.getSelectionModel().selectFirst();
                });
                return null;
            }
        };

        intervalRetriever.setOnRunning(e -> this.borderPaneRootSwitcher.showLoadingScreen_BP());
        intervalRetriever.setOnSucceeded(e -> this.borderPaneRootSwitcher.exitLoadingScreen_BP());

        Thread myThread = new Thread(intervalRetriever);
        myThread.setDaemon(true);
        myThread.start();
    }

    @FXML
    private void confirm(ActionEvent e) {
        if (this.datePicker.getValue().isAfter(LocalDate.now())) {
            PopupDialog.showCustomErrorDialog("You cannot create a report from the future!");
            return;
        }

        // Retrieving currently selected time period
        ReportTimePeriods.TimePeriod selectedPeriod = null;
        if (this.dateIntervalCBox.getValue().compareTo(ReportTimePeriods.TimePeriod.DAILY.getPeriodString()) == 0) {
            selectedPeriod = ReportTimePeriods.TimePeriod.DAILY;
        } else if (this.dateIntervalCBox.getValue()
                .compareTo(ReportTimePeriods.TimePeriod.WEEKLY.getPeriodString()) == 0) {
            selectedPeriod = ReportTimePeriods.TimePeriod.WEEKLY;
        } else if (this.dateIntervalCBox.getValue()
                .compareTo(ReportTimePeriods.TimePeriod.MONTHLY.getPeriodString()) == 0) {
            selectedPeriod = ReportTimePeriods.TimePeriod.MONTHLY;
        }

        this.borderPaneRootSwitcher.exitPopUpDialog();

        System.out.println("datepicker: " + datePicker.getValue());
        System.out.println("time interval: " + this.dateIntervalCBox.getValue());

        this.manualGenerateReportController.generatePeriodicSales(selectedPeriod,
                DateHelper.dateToString(datePicker.getValue()));
    }

    @FXML
    private void cancel(ActionEvent e) {
        this.borderPaneRootSwitcher.exitPopUpDialog();
    }
}
