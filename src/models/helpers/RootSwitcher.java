/*
 * Keeps track of root switching; allows for easy implementation of "BACK" button
 */

package models.helpers;

import java.util.Stack;

import javafx.scene.Parent;
import javafx.stage.Stage;

public class RootSwitcher {
    private Stack<Parent> roots;
    private Stage mainStage;

    // Constructor; setting reference to Main Stage and initializing Stack
    public RootSwitcher(Stage mainStage) {
        this.mainStage = mainStage;
        this.roots = new Stack<Parent>();
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

    // Removes all roots in stack
    public void clearStack() {
        this.roots.clear();
    }

    // Debug function; shows number of roots in stack
    public void rootCount() {
        System.out.println(this.roots.size());
    }
}
