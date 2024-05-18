/*
 * Keeps track of root switching; allows for easy implementation of "BACK" button
 */

package models.helpers;

import java.io.IOException;
import java.util.Stack;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class RootSwitcher {
    private Stack<Parent> roots;
    private Stage mainStage;
    private Parent previousRoot;
    private Parent loadingScreenRoot;

    // Constructor; setting reference to Main Stage and initializing Stack
    public RootSwitcher(Stage mainStage) {
        // Intializing
        this.mainStage = mainStage;
        this.roots = new Stack<Parent>();

        // Loading loading screen root
        try {
            this.loadingScreenRoot = new FXMLLoader(getClass().getResource("../../views/fxmls/LoadingView.fxml"))
                    .load();
        } catch (IOException e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
        }
    }

    // Go back to previous View
    public void goBack() {
        this.mainStage.getScene().setRoot(this.roots.pop());
    }

    // Navigate to next View
    public void nextView(Parent nextRoot) {
        // Adding current View in Stack
        this.roots.add(this.mainStage.getScene().getRoot());
        this.mainStage.getScene().setRoot(nextRoot);
    }

    // Returns main stage
    public Stage getMainStage() {
        return this.mainStage;
    }

    // Show Loading Screen
    public void showLoadingScreen() {
        // Copy current root
        this.previousRoot = this.mainStage.getScene().getRoot();
        // Show loading screen
        this.mainStage.getScene().setRoot(this.loadingScreenRoot);
    }

    // Exit Loading Screen
    public void exitLoadingScreen() {
        this.mainStage.getScene().setRoot(this.previousRoot);
    }

    // Removes all roots in stack
    public void clearStack() {
        this.roots.clear();
    }

    // Debug function; shows number of roots in stack
    private void rootCount() {
        System.out.println(this.roots.size());
    }
}
