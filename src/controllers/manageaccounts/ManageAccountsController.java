package controllers.manageaccounts;

import controllers.ParentController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import models.manageaccounts.ManageAccountsModel;
import models.schemas.User;

public class ManageAccountsController extends ParentController {

    @FXML
    private TableView<User> accountsTableView;

    @FXML
    private TableColumn<User, Integer> userIdCol;
    @FXML
    private TableColumn<User, String> uNameCol, passCol, loaCol, statusCol;

    private ManageAccountsModel model;
    private User selectedUser;

    @FXML
    private void initialize() {
        this.model = new ManageAccountsModel(this);
        configureAccountsTable();

        // Defining on-select function
        this.accountsTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<User>() {
            @Override
            public void changed(ObservableValue<? extends User> arg0, User arg1, User arg2) {
                // Set selected user by the user as the selectedUser
                selectedUser = accountsTableView.getSelectionModel().getSelectedItem();
            }

        });
    }

    // Configures accounts table and displays to user
    private void configureAccountsTable() {
        // Configuring columns
        userIdCol.setCellValueFactory(new PropertyValueFactory<User, Integer>("id"));
        uNameCol.setCellValueFactory(new PropertyValueFactory<User, String>("uname"));
        passCol.setCellValueFactory(new PropertyValueFactory<User, String>("pass"));
        loaCol.setCellValueFactory(new PropertyValueFactory<User, String>("level_of_access_id"));
        statusCol.setCellValueFactory(new PropertyValueFactory<User, String>("account_status_id"));
    }

    // Returns all of the existing users from database
    public void syncUserTableView() {
        // Defining task; Retrieving users from database using another thread
        Service<ObservableList<User>> databaseService = new Service<ObservableList<User>>() {
            @Override
            protected Task<ObservableList<User>> createTask() {
                return new Task<ObservableList<User>>() {
                    @Override
                    protected ObservableList<User> call() throws Exception {
                        return model.getAllUsers();
                    }
                };
            }

        };

        // Do after completing service
        databaseService.setOnSucceeded(e -> {
            // Get retrieved value
            ObservableList<User> users = (ObservableList<User>) databaseService.getValue();
            // Muting user passwords
            users = this.model.mutePasswords(users);
            // Update table view
            accountsTableView.setItems(users);
            // Exit Loading Screen
            this.borderPaneRootSwitcher.exitLoadingScreen_BP();
        });

        // Start service
        databaseService.start();
        // Show loading Screen
        this.borderPaneRootSwitcher.showLoadingScreen_BP();
    }

    @FXML
    private void viewuserlog() {
        System.out.println("View User Log");
    }

    @FXML
    private void registernewuser() {
        System.out.println("registerNewUser");
    }

    @FXML
    private void edituser() {
        try {
            System.out.println("Editing user " + selectedUser.getPass());
        } catch (Exception e) {
            System.out.println("no selected user");
        }
    }

    @FXML
    private void goBack() {
        System.out.println("going back...");
        this.borderPaneRootSwitcher.goBack_BP();
    }
}
