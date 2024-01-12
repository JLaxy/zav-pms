/*
 * Keeps track of root switching; allows for easy implementation of "BACK" button
 */

package models.helpers;

import java.util.Stack;

import javafx.scene.Parent;
import javafx.stage.Stage;

public class RootSwitcher {
    Stack<Parent> roots = new Stack<Parent>();
    Stage mainStage;

    public RootSwitcher(Stage mainStage) {
        this.mainStage = mainStage;
    }

    // Go back to previous View
    public void goBack() {
        this.mainStage.getScene().setRoot(this.roots.pop());
    }

    // Naavigate to next View
    public void nextView(Parent nextRoot) {
        this.roots.add(this.mainStage.getScene().getRoot());
        this.mainStage.getScene().setRoot(nextRoot);
    }

    // Debug function; shows number of roots in stack
    public void rootCount() {
        System.out.println(this.roots.size());
    }
}
