package views;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainView extends Application {

    // Easy to change values
    String APP_TITLE = "Zav's Kitchen and Bar: Product Management System";
    String ICON_PATH = "file:../../assets/images/logoTransparent.png";
    int stageWidth = 1366;
    int stageHeight = 768;

    // Starting
    @Override
    public void start(Stage mainStage) throws Exception {
        // TODO Auto-generated method stub
        // Creating root node
        Group root = new Group();
        // Adding to scene
        Scene scene = new Scene(root, Color.AQUA);

        Text text = new Text("Hello World!");
        text.setX(100);
        text.setY(100);
        text.setFont(Font.font("Times New Roman", 28));
        text.setFill(Color.WHITE);

        Line linee = new Line(200, 200, 500, 500);
        linee.setStrokeWidth(10);

        Rectangle rect = new Rectangle(0, 0, 100, 200);
        rect.setStroke(Color.BEIGE);
        rect.setStrokeWidth(20);

        Polygon tri = new Polygon(250, 500, 300, 150, 400, 500, 300, 600);
        tri.setFill(Color.BLANCHEDALMOND);
        tri.setStroke(Color.WHITE);
        tri.setStrokeWidth(5);

        Circle circ = new Circle(700, 500, 50, Color.ORANGERED);
        circ.setStroke(Color.BLACK);
        circ.setStrokeWidth(10);

        ImageView img = new ImageView(new Image("file:../../assets/images/logo.png"));
        img.setX(500);
        img.setY(200);

        root.getChildren().add(img);
        root.getChildren().add(circ);
        root.getChildren().add(tri);
        root.getChildren().add(text);
        root.getChildren().add(rect);
        root.getChildren().add(linee);
        // Sets icon of application
        mainStage.getIcons().add(new Image(ICON_PATH));
        // Sets title of the Stage / Window
        mainStage.setTitle(APP_TITLE);
        mainStage.setWidth(stageWidth);
        mainStage.setHeight(stageHeight);
        mainStage.setResizable(false);
        mainStage.setScene(scene);

        // Makes stage visible
        mainStage.show();
    }

}