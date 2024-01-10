import controllers.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class PMS extends Application {
    // Easy to change values
    String APP_TITLE = "Zav's Kitchen and Bar: Product Management System";
    String ICON_PATH = "file:../../assets/images/logoTransparent.png";
    int stageWidth = 1366;
    int stageHeight = 768;

    // Starting
    @Override
    public void start(Stage mainStage) throws Exception {
        // TODO Auto-generated method stub
        try {
            // Loading GUI built on Scene Builder
            FXMLLoader root = new FXMLLoader(getClass().getResource("views/fxmls/MainView.fxml"));
            // Adding to scene
            Scene scene = new Scene(root.load());

            // Getting reference of main controller for future use?
            MainController controller = root.getController();

            // Sets icon of application
            mainStage.getIcons().add(new Image(ICON_PATH));
            // Sets title of the Stage / Window
            mainStage.setTitle(APP_TITLE);
            // Setting Stage Dimensions
            mainStage.setWidth(stageWidth);
            mainStage.setHeight(stageHeight);
            mainStage.setResizable(false);
            mainStage.setScene(scene);

            // Makes stage visible
            mainStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
