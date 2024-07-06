package controllers.help;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import controllers.ParentController;
import enums.UserManualTypes;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import models.helpers.PopupDialog;

public class UserManualViewerController extends ParentController {

    @FXML
    private ImageView manualView;

    @FXML
    private Button leftArrowButton, rightArrowButton;

    private List<Image> images;
    private int currentIndex = 0;

    public final String ADMIN_PATH = "assets/user_manual/admin";
    public final String KITCHEN_PATH = "assets/user_manual/kitchen";
    public final String CASHIER_PATH = "assets/user_manual/cashier";

    @FXML
    public void initialize(UserManualTypes.ManualType manualType) {
        this.leftArrowButton.setDisable(true);

        String path = "";

        this.images = new ArrayList<Image>();
        switch (manualType) {
            case UserManualTypes.ManualType.ADMIN_MANUAL:
                path = this.ADMIN_PATH;
                break;
            case UserManualTypes.ManualType.KITCHEN_STAFF_MANUAL:
                path = this.KITCHEN_PATH;
                break;
            case UserManualTypes.ManualType.CASHIER_MANUAL:
                path = this.CASHIER_PATH;
                break;
            default:
                PopupDialog.showCustomErrorDialog("Error retrieving manual!");
                return;
        }

        this.configureManualLoader(path);
    }

    // Starts thread of retrieving user manual in file system
    private void configureManualLoader(String path) {
        Task<Void> manualLoader = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                loadImages(path);
                return null;
            }
        };

        manualLoader.setOnRunning(e -> this.borderPaneRootSwitcher.showLoadingScreen_BP());
        manualLoader.setOnSucceeded(e -> this.borderPaneRootSwitcher.exitLoadingScreen_BP());

        Thread loader = new Thread(manualLoader);
        loader.setDaemon(true);
        loader.start();
    }

    private void loadImages(String manualPath) {
        File folder = new File(manualPath);
        File[] files = folder.listFiles((dir, name) -> {
            String lowerCaseName = name.toLowerCase();
            return lowerCaseName.endsWith(".jpg") || lowerCaseName.endsWith(".jpeg") || lowerCaseName.endsWith(".png")
                    || lowerCaseName.endsWith(".gif");
        });

        System.out.println(files.toString());

        if (files != null) {
            // Sort files by their names
            Arrays.sort(files, Comparator.comparingInt(this::extractNumberFromFilename));
            for (File file : files) {
                this.images.add(new Image(file.toURI().toString()));
            }
        }

        if (!images.isEmpty()) {
            manualView.setImage(images.get(0));
        }
    }

    private int extractNumberFromFilename(File file) {
        String name = file.getName();
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(name);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group());
        } else {
            return Integer.MAX_VALUE; // or any default value
        }
    }

    @FXML
    private void exit(ActionEvent e) {
        this.borderPaneRootSwitcher.goBack_BP();
    }

    @FXML
    private void previous(ActionEvent event) {
        if (!images.isEmpty()) {
            if (this.currentIndex < 1)
                return;

            --this.currentIndex;

            this.manualView.setImage(this.images.get(this.currentIndex));

            this.leftArrowButton.setDisable(false);
            this.rightArrowButton.setDisable(false);

            if (this.currentIndex == 0)
                this.leftArrowButton.setDisable(true);
        }
    }

    @FXML
    private void next(ActionEvent event) {
        if (!images.isEmpty()) {
            if (this.currentIndex >= this.images.size() - 1)
                return;

            ++this.currentIndex;

            this.manualView.setImage(this.images.get(this.currentIndex));

            this.leftArrowButton.setDisable(false);
            this.rightArrowButton.setDisable(false);

            if (this.currentIndex == this.images.size() - 1)
                this.rightArrowButton.setDisable(true);
        }
    }
}
