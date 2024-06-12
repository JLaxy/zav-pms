/*
 * Product Management System Application
 */

import controllers.ParentController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import models.helpers.JSONManager;
import models.helpers.PopupDialog;
import models.helpers.RootSwitcher;
import models.helpers.database.DBManager;
import models.maintenance.AutoBackup;

public class PMS extends Application {
    // Easy to change values
    private final String APP_TITLE = "Zav's Kitchen and Bar: Product Management System";
    private final String ICON_PATH = "file:../../assets/images/logoTransparent.png";

    // Starting
    @Override
    public void start(Stage mainStage) throws Exception {
        try {
            // Comment to disable database save on exit
            AutoBackup.enableDatabaseSaveOnExit(mainStage);

            // Connecting to Database
            DBManager zavPMSDB = connectToDatabase();

            // Initializing settings file; makes sure it exists
            new JSONManager().initializeSettingsFile();

            // Loading GUI built on Scene Builder
            FXMLLoader rootLoader = new FXMLLoader(getClass().getResource("views/fxmls/StartView.fxml"));

            // Retrieving root / where controller is also initialized
            Parent root = rootLoader.load();

            // Adding to scene
            Scene scene = new Scene(root);

            // Sets icon of application
            mainStage.getIcons().add(new Image(ICON_PATH));

            // Sets title of the Stage / Window
            mainStage.setTitle(APP_TITLE);

            mainStage.setResizable(false);
            mainStage.setScene(scene);

            // Makes stage visible
            mainStage.show();

            // Initializing Root Switcher; allows to change View
            RootSwitcher rootSwitcher = new RootSwitcher(mainStage);
            // Passing RootSwitcher instance to next controller
            ParentController nextController = rootLoader.getController();
            // Configuring controller of next view; passing in DBManager and RootSwitcher
            // References
            nextController.initializeReferences(zavPMSDB, rootSwitcher);

        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
        }
    }

    // Connecting to Database
    private DBManager connectToDatabase() {
        return new DBManager();
    }
}
