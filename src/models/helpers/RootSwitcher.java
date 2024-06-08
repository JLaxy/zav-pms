/*
 * Keeps track of root switching; allows for easy implementation of "BACK" button
 */

package models.helpers;

import java.io.IOException;
import java.util.Stack;

import controllers.PageNavigatorViewController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class RootSwitcher {
    private Stack<Parent> roots;
    private Stack<String> pageViewTitles;
    private Stage mainStage;
    private BorderPane mainBorderPane;
    private Parent previousRoot, loadingScreenRoot, loadingScreenRoot_BP;
    private PageNavigatorViewController pageNavigatorViewController;

    // Constructor for mainStage; setting reference to Main Stage and initializing
    // Stack
    public RootSwitcher(Stage mainStage) {
        // Intializing
        this.mainStage = mainStage;
        configureRootSwitcher();
    }

    // Constructor for BorderPane; setting reference to Main Stage and initializing
    // Stack
    public RootSwitcher(BorderPane mainBorderPane, PageNavigatorViewController controller) {
        this.mainBorderPane = mainBorderPane;
        this.pageNavigatorViewController = controller;
        configureRootSwitcher();
    }

    // Function for configuring root stack and loading screen
    private void configureRootSwitcher() {
        this.roots = new Stack<Parent>();
        this.pageViewTitles = new Stack<String>();

        // Loading loading screen root
        try {
            this.loadingScreenRoot = new FXMLLoader(getClass().getResource("../../views/fxmls/LoadingView.fxml"))
                    .load();
            this.loadingScreenRoot_BP = new FXMLLoader(getClass().getResource("../../views/fxmls/LoadingView_BP.fxml"))
                    .load();
        } catch (IOException e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
        }
    }

    // Go back to previous View
    public void goBack() {
        this.mainStage.getScene().setRoot(this.roots.pop());
    }

    // Go back to previous view for mainBorderPane
    public void goBack_BP() {
        this.pageNavigatorViewController.getPageTitleLabel().setText(this.pageViewTitles.pop());
        this.mainBorderPane.setCenter(this.roots.pop());
    }

    // Go back to previous View in X number of times
    public void goBack(int numberOfScreens) {
        for (int i = 0; i < numberOfScreens; i++) {
            this.mainStage.getScene().setRoot(this.roots.pop());
        }
    }

    // Go back to previous View in X number of times for mainBorderPane
    public void goBack_BP(int numberOfScreens) {
        for (int i = 0; i < numberOfScreens; i++) {
            this.mainBorderPane.setCenter(this.roots.pop());
            this.pageNavigatorViewController.getPageTitleLabel().setText(this.pageViewTitles.pop());
        }
    }

    // Navigate to next View
    public void nextView(Parent nextRoot) {
        // Adding current View in Stack
        this.roots.add(this.mainStage.getScene().getRoot());
        this.mainStage.getScene().setRoot(nextRoot);
    }

    // Navigate to next View for mainBorderPane
    public void nextView_BP(Parent nextRoot, String pageTitle) {
        // Adding current View and Page label in Stack
        try {
            this.roots.add((Parent) this.mainBorderPane.getCenter());
            this.pageViewTitles.add(this.pageNavigatorViewController.getPageTitleLabel().getText());
        } catch (Exception e) {
            System.out.println("ignoring empty border pane...");
        }
        // Update Page title then navigate to new view
        this.pageNavigatorViewController.getPageTitleLabel().setText(pageTitle);
        this.mainBorderPane.setCenter(nextRoot);
    }

    // Returns main stage
    public Stage getMainStage() {
        return this.mainStage;
    }

    // Returns MainBorderPane
    public BorderPane getMainBorderPane() {
        return this.mainBorderPane;
    }

    // Show Loading Screen
    public void showLoadingScreen() {
        // Copy current root
        this.previousRoot = this.mainStage.getScene().getRoot();
        // Show loading screen
        this.mainStage.getScene().setRoot(this.loadingScreenRoot);
    }

    // Show Loading Screen above borderpane
    public void showLoadingScreen_BP() {
        // Show loading screen
        this.pageNavigatorViewController.getMainStackPane().getChildren().add(loadingScreenRoot_BP);
    }

    // Display Popup Dialog on stackpane of pagenavigatorview
    public void showPopUpDialog(Parent dialogRoot) {
        this.pageNavigatorViewController.getMainStackPane().getChildren().add(dialogRoot);
    }

    // Remove Popup Dialog
    public void exitPopUpDialog() {
        this.pageNavigatorViewController.getMainStackPane().getChildren()
                .remove(this.pageNavigatorViewController.getMainStackPane().getChildren().getLast());
    }

    // Exit Loading Screen
    public void exitLoadingScreen() {
        this.mainStage.getScene().setRoot(this.previousRoot);
    }

    // Remove Loading Screen above borderpane
    public void exitLoadingScreen_BP() {
        this.pageNavigatorViewController.getMainStackPane().getChildren().remove(loadingScreenRoot_BP);
    }

    // Navigates user back to login screen
    public void logout() {
        // Pop screens until empty
        while (this.roots.size() > 0) {
            goBack();
        }
    }

    // Returns PageNavigatorViewController
    public PageNavigatorViewController getPageNavigatorViewController() {
        return this.pageNavigatorViewController;
    }

    // Removes all roots in stack
    public void clearStack() {
        this.roots.clear();
    }
}
