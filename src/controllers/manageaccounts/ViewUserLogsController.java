package controllers.manageaccounts;

import java.time.LocalDate;

import controllers.ParentController;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import models.helpers.PopupDialog;
import models.manageaccounts.ViewUserLogsModel;
import models.schemas.User;
import models.schemas.UserLog;

public class ViewUserLogsController extends ParentController {

    @FXML
    private TableColumn<UserLog, String> timeCol, dateCol, unameCol, actionCol;

    @FXML
    private TextField searchField;

    @FXML
    private TableView<UserLog> logTable;

    @FXML
    private DatePicker logDate;

    @FXML
    private VBox userLogDetailsVBox;

    @FXML
    private Label usernameLabel, accountOwnerLabel, levelOfAccessLabel, actionLabel, timestampLabel, detailsLabel;

    @FXML
    private Button rightArrowButton, leftArrowButton;

    private UserLog selectedLog;
    private ViewUserLogsModel model;
    private ObservableList<UserLog> listOfUserLogs;

    @FXML
    public void initialize() {
        showReferences();
        this.model = new ViewUserLogsModel(this);

        // Set default date as current date
        this.logDate.setValue(LocalDate.now());
        // Disable right arrow
        this.rightArrowButton.setDisable(true);

        // Hide userlog details
        this.userLogDetailsVBox.setVisible(false);
        configureLogClickListener();
        configureDatePickerListener();
    }

    // Configures the listener function to be fired on click of an item in table
    private void configureLogClickListener() {
        this.logTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<UserLog>() {
            @Override
            public void changed(ObservableValue<? extends UserLog> arg0, UserLog arg1, UserLog arg2) {
                // If there is a selection
                if (logTable.getSelectionModel().getSelectedItem() != null) {
                    selectedLog = logTable.getSelectionModel().getSelectedItem();
                    updateDetailsShown();
                }
            }
        });
    }

    // Configures the listener function to be fired on change of date in datepicker
    private void configureDatePickerListener() {
        this.logDate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                retrieveUserLogs();
            }
        });
    }

    // Updates the details shown on the view
    private void updateDetailsShown() {
        // Set values
        User selectedUser = this.model.getUser(this.selectedLog.getUname());
        this.usernameLabel.setText(selectedUser.getUname());
        this.accountOwnerLabel.setText(selectedUser.getFName() + " " + selectedUser.getLName());
        this.levelOfAccessLabel.setText(selectedUser.getLevel_of_access_id_string());
        this.actionLabel.setText(this.selectedLog.getActionString());
        this.timestampLabel.setText(this.selectedLog.getDate());
        this.detailsLabel.setText(
                this.selectedLog.getParameter().replace(",", "\n-").replace(";", "\n-"));

        // Show
        this.userLogDetailsVBox.setVisible(true);
    }

    // Configuring table
    public void configureUserLogsTable() {
        timeCol.setCellValueFactory(new PropertyValueFactory<UserLog, String>("timeString"));
        dateCol.setCellValueFactory(new PropertyValueFactory<UserLog, String>("dateString"));
        unameCol.setCellValueFactory(new PropertyValueFactory<UserLog, String>("uname"));
        actionCol.setCellValueFactory(new PropertyValueFactory<UserLog, String>("actionString"));

        // Disable table reordering
        logTable.getColumns().forEach(e -> {
            e.setReorderable(false);
        });

        retrieveUserLogs();
    }

    // Retrieve user logs and show on UI
    private void retrieveUserLogs() {
        this.userLogDetailsVBox.setVisible(false);

        // Removes sort
        this.logTable.getSortOrder().clear();
        try {
            this.listOfUserLogs = FXCollections.observableArrayList();

            Service<Void> userLogsRetriever = new Service<Void>() {
                @Override
                protected Task<Void> createTask() {
                    return new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            listOfUserLogs = model
                                    .getUserLogs(searchField.getText().isBlank() ? null : searchField.getText(),
                                            logDate.getValue().toString());
                            logTable.setItems(listOfUserLogs);
                            System.out.println("size::" + listOfUserLogs.size());
                            return null;
                        }
                    };
                }
            };
            userLogsRetriever.setOnSucceeded(e -> {
                borderPaneRootSwitcher.exitLoadingScreen_BP();
                // Configure Sort
                logTable.getSortOrder().add(logTable.getColumns().get(logTable.getColumns().indexOf(timeCol)));
            });

            borderPaneRootSwitcher.showLoadingScreen_BP();
            userLogsRetriever.start();
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
        }
    }

    @FXML
    private void goBack(ActionEvent e) {
        this.borderPaneRootSwitcher.goBack_BP();

    }

    @FXML
    private void search(ActionEvent e) {
        retrieveUserLogs();
    }

    @FXML
    private void arrowChangeDate(ActionEvent e) {
        // If right arrow was clicked
        if ((Button) e.getSource() == this.rightArrowButton)
            this.logDate.setValue(this.logDate.getValue().plusDays(1));
        // If left arrow was clicked
        else if ((Button) e.getSource() == this.leftArrowButton)
            this.logDate.setValue(this.logDate.getValue().minusDays(1));

        // If date is current date, disable right arrow button
        if (this.logDate.getValue().isEqual(LocalDate.now()))
            rightArrowButton.setDisable(true);
        else
            rightArrowButton.setDisable(false);
    }

}
