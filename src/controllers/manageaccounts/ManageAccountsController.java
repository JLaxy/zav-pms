package controllers.manageaccounts;

import controllers.ParentController;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import models.helpers.PopupDialog;
import models.manageaccounts.ManageAccountsModel;
import models.schemas.User;

public class ManageAccountsController extends ParentController {

    @FXML
    private TableView<User> accountsTableView;

    @FXML
    private TableColumn<User, Integer> userIdCol;
    @FXML
    private TableColumn<User, String> uNameCol, passCol, loaCol, statusCol, emailCol;

    @FXML
    private TextField searchField;

    private ObservableList<User> userList;
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
                if (accountsTableView.getSelectionModel().getSelectedItem() != null) {
                    // Set selected user by the user as the selectedUser
                    selectedUser = accountsTableView.getSelectionModel().getSelectedItem();

                    // Find selected user in userlist
                    for (User iteratedUser : userList) {
                        // If found
                        if (selectedUser.getUname().compareTo(iteratedUser.getUname()) == 0) {
                            // Select
                            selectedUser = iteratedUser;
                        }
                    }
                }
            }

        });
    }

    // Configures accounts table and displays to user
    private void configureAccountsTable() {
        // Configuring columns
        userIdCol.setCellValueFactory(new PropertyValueFactory<User, Integer>("id"));
        uNameCol.setCellValueFactory(new PropertyValueFactory<User, String>("uname"));
        passCol.setCellValueFactory(new PropertyValueFactory<User, String>("pass"));
        loaCol.setCellValueFactory(new PropertyValueFactory<User, String>("level_of_access_id_string"));
        statusCol.setCellValueFactory(new PropertyValueFactory<User, String>("account_status_id_string"));
        emailCol.setCellValueFactory(new PropertyValueFactory<User, String>("email"));

    }

    @FXML
    private void search(ActionEvent e) {
        syncUserTableView();
    }

    // Returns all of the existing users from database
    public void syncUserTableView() {
        try {
            this.userList = FXCollections.observableArrayList();
            // Show loading Screen
            this.borderPaneRootSwitcher.showLoadingScreen_BP();

            // Create thread
            Thread tableViewSyncer = new Thread(new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    Platform.runLater(() -> {
                        // Get retrieved value
                        userList = model.getAllUsers(searchField == null ? null
                                : (searchField.getText().isBlank() ? null : searchField.getText()));
                        // Update table view with list of users where password is muted
                        accountsTableView.setItems(model.mutePasswords(model.copyUserList(userList)));
                        // Exit Loading Screen
                        borderPaneRootSwitcher.exitLoadingScreen_BP();
                    });
                    return null;
                }
            });

            tableViewSyncer.setDaemon(true);
            tableViewSyncer.start();

        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
        }
    }

    @FXML
    private void viewuserlog() {
        System.out.println("View User Log");
    }

    @FXML
    private void registernewuser() {
        UserDetailsController controller = (UserDetailsController) this
                .initializePopUpDialog("../../views/fxmls/manageaccounts/UserDetailsView.fxml", this.loggedInUserInfo);
        controller.initialize(null, this);
    }

    @FXML
    private void edituser() {
        try {
            System.out.println("Editing user " + selectedUser.getUname());
            // Logging user view on database
            this.model.logViewingUserDetails(loggedInUserInfo.getId(), loggedInUserInfo.getUname(),
                    selectedUser.getUname());

            // Configuring controller
            UserDetailsController controller = (UserDetailsController) this.initializePopUpDialog(
                    "../../views/fxmls/manageaccounts/UserDetailsView.fxml", this.loggedInUserInfo);
            controller.initialize(selectedUser, this);
        } catch (Exception e) {
            PopupDialog.showCustomErrorDialog("Please select a user first!");
        }
    }

    @FXML
    private void goBack() {
        System.out.println("going back...");
        this.borderPaneRootSwitcher.goBack_BP();
    }
}
