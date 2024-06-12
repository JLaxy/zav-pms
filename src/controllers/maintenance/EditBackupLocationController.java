package controllers.maintenance;

import java.io.File;

import controllers.ParentController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;
import models.helpers.JSONManager;
import models.helpers.PopupDialog;
import models.maintenance.EditBackupLocationModel;

public class EditBackupLocationController extends ParentController {

    @FXML
    private Label selectedLocLabel, currentLocLabel;

    private EditBackupLocationModel model;

    @FXML
    public void initialize() {
        this.model = new EditBackupLocationModel(this);
        System.out.println("initialized");
        this.currentLocLabel.setText(new JSONManager().getSetting("backupLocation"));
    }

    @FXML
    private void selectLocation(ActionEvent e) {
        DirectoryChooser dc = new DirectoryChooser();
        File location = dc.showDialog(
                this.borderPaneRootSwitcher.getPageNavigatorViewController().getRootSwitcher().getMainStage());

        if (location == null)
            return;

        selectedLocLabel.setText(location.getAbsolutePath());
    }

    @FXML
    private void goBack(ActionEvent e) {
        this.borderPaneRootSwitcher.goBack_BP();
    }

    @FXML
    private void save(ActionEvent e) {
        System.out.println("saving...");

        // Checking if file path is still valid
        if (!new File(selectedLocLabel.getText()).exists()) {
            PopupDialog.showCustomErrorDialog("There is a problem with the selected directory. \nPlease try again");
            return;
        }

        // Saving to settings file
        if (new JSONManager().writeToSetting("backupLocation", selectedLocLabel.getText())) {
            PopupDialog.showInfoDialog("Updated Database Export Location",
                    "Successfully Updated Database Export Location");
            this.model.logBackupLocationEdit(loggedInUserInfo, selectedLocLabel.getText());
            this.borderPaneRootSwitcher.goBack_BP();
        }
    }
}
