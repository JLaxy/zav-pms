package controllers.reusables;

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.function.UnaryOperator;

import controllers.ParentController;
import controllers.inventory.SelectStockController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.IntegerStringConverter;

public class SetQuantityController extends ParentController {

    @FXML
    private Spinner<Integer> quantitySpinner;

    private SelectStockController selectStockController;

    @FXML
    public void initialize(SelectStockController selectStockController) {
        this.selectStockController = selectStockController;
        this.configureSpinner();
    }

    private void configureSpinner() {
        // get a localized format for parsing
        NumberFormat format = NumberFormat.getIntegerInstance();
        UnaryOperator<TextFormatter.Change> filter = c -> {
            if (c.isContentChange()) {
                ParsePosition parsePosition = new ParsePosition(0);
                // NumberFormat evaluates the beginning of the text
                format.parse(c.getControlNewText(), parsePosition);
                if (parsePosition.getIndex() == 0 ||
                        parsePosition.getIndex() < c.getControlNewText().length()) {
                    // reject parsing the complete text failed
                    return null;
                }
            }
            return c;
        };

        TextFormatter<Integer> priceFormatter = new TextFormatter<Integer>(
                new IntegerStringConverter(), 1, filter);

        this.quantitySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000000000, 0));
        this.quantitySpinner.getEditor().setTextFormatter(priceFormatter);
    }

    @FXML
    private void confirm(ActionEvent e) {
        this.selectStockController.confirmSelection(this.quantitySpinner.getValue());
        this.borderPaneRootSwitcher.exitPopUpDialog();
        this.borderPaneRootSwitcher.goBack_BP();
    }

    @FXML
    private void cancel(ActionEvent e) {
        this.borderPaneRootSwitcher.exitPopUpDialog();
    }
}
