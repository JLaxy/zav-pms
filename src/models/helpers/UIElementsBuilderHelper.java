/*
 * Contains helper functions related to dynamically building UI elements
 */

package models.helpers;

import javafx.collections.ObservableList;
import javafx.scene.Node;

public class UIElementsBuilderHelper {
    // Applying styles to label on popup dialogs
    public static void applyPopUpDialogLabelStyles(ObservableList<Node> labelsList) {
        for (Node node : labelsList) {
            node.setStyle("-fx-font-size: 20");
        }
    }

    public static void applyRedToText(Node textLabel) {
        textLabel.setStyle("-fx-text-fill: red");
    }

    public static void applyBlackToText(Node textLabel) {
        textLabel.setStyle("-fx-text-fill: black");
    }
}
