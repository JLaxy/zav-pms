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
import models.helpers.RootSwitcher;
import models.helpers.database.DatabaseManager;

public class PMS extends Application {
    // Easy to change values
    private String APP_TITLE = "Zav's Kitchen and Bar: Product Management System";
    private String ICON_PATH = "file:../../assets/images/logoTransparent.png";
    private int stageWidth = 1366;
    private int stageHeight = 768;

    // Starting
    @Override
    public void start(Stage mainStage) throws Exception {
        try {

            // Connecting to Database
            DatabaseManager zavPMSDB = connectToDatabase();

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

            // Setting Stage Dimensions
            // mainStage.setWidth(stageWidth);
            // mainStage.setHeight(stageHeight);
            mainStage.setResizable(false);
            mainStage.setScene(scene);

            // Makes stage visible
            mainStage.show();

            // Initializing Root Switcher; allows to change View
            RootSwitcher rootSwitcher = new RootSwitcher(mainStage);
            // Passing RootSwitcher instance to next controller
            ParentController nextController = rootLoader.getController();
            nextController.setRootSwitcher(rootSwitcher);

        } catch (Exception e) {
            System.out.println("Error at: " + getClass());
            e.printStackTrace();
        }
    }

    // Connecting to Database
    public DatabaseManager connectToDatabase() {
        return new DatabaseManager();
    }
}
