package controllers.inventory;

import javax.swing.JOptionPane;

import controllers.ParentController;
import enums.ProductServingSize;
import enums.ScreenPaths;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import models.helpers.NumberHelper;
import models.helpers.PopupDialog;
import models.inventory.RegisterNewFoodVariantDetailsModel;
import models.schemas.FoodVariant;
import models.schemas.Stock;

public class RegisterNewFoodVariantDetailsController extends ParentController {

    private String productName;
    private boolean isNewFoodVariant;
    private RegisterNewFoodVariantDetailsModel model;

    @FXML
    private ComboBox<String> servingSizeCBox;
    @FXML
    private TextField regularPriceField;
    @FXML
    private TableView<Stock> requiredStockTable;
    @FXML
    private TableColumn<Stock, String> stockNameCol, quantityCol;
    @FXML
    private Button removeStockButton;

    private ObservableList<Stock> selectedStocks;
    private Stock selectedStock;

    @FXML
    public void initialize(String productName, boolean isNewFoodVariant) {
        this.model = new RegisterNewFoodVariantDetailsModel(this);
        this.productName = productName;
        this.isNewFoodVariant = isNewFoodVariant;
        this.selectedStocks = FXCollections.observableArrayList();
        this.configureStockTable();
        this.configureOnClick();
        this.removeStockButton.setDisable(true);
        convertToNumberField(this.regularPriceField);
        this.getServingSizes();
    }

    // Configures on table click function
    private void configureOnClick() {
        this.requiredStockTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Stock>() {
            @Override
            public void changed(ObservableValue<? extends Stock> arg0, Stock arg1, Stock arg2) {
                // If has selected
                if (requiredStockTable.getSelectionModel().getSelectedItem() != null) {
                    selectedStock = requiredStockTable.getSelectionModel().getSelectedItem();
                    // Enable button if selected
                    removeStockButton.setDisable(false);
                    return;
                }
                // Disable button if none selected
                removeStockButton.setDisable(true);
            }
        });
    }

    // Configuring stock table
    private void configureStockTable() {
        stockNameCol.setCellValueFactory(new PropertyValueFactory<Stock, String>("stock_name"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<Stock, String>("quantity"));

        // Disable table reordering
        requiredStockTable.getColumns().forEach(e -> {
            e.setReorderable(false);
        });
    }

    // Returns true if all fields are valid
    private boolean areFieldsValid() {
        // Checking regular
        if (!isFloat(this.regularPriceField.getText(), "Regular Price"))
            return false;
        return true;
    }

    // Returns false if supplied is not float
    private boolean isFloat(String floatText, String fieldName) {
        try {
            Float.valueOf(floatText);
            return true;
        } catch (Exception e) {
            PopupDialog.showCustomErrorDialog(fieldName + " field is not valid!");
        }
        return false;
    }

    // Configures Cost field; Prevents letters from textfield
    private void convertToNumberField(TextField field) {
        field.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                    String newValue) {
                if (!newValue.matches("^\\d+(\\.\\d*)?$"))
                    field.setText(newValue.replaceAll("[^0-9.]", ""));
            }
        });
    }

    // Retrieves serving sizes fro mdatabase
    private void getServingSizes() {
        this.servingSizeCBox.setItems(this.model.getServingSizes());
        this.servingSizeCBox.getSelectionModel().selectFirst();
    }

    public void addRequiredStock(Stock selectedStock) {
        this.selectedStocks.add(selectedStock);
        this.requiredStockTable.setItems(this.selectedStocks);
        System.out.println(this.selectedStocks.size());
    }

    @FXML
    private void goBack(ActionEvent e) {
        // Show confirmation dialog
        if (PopupDialog
                .confirmOperationDialog("Are you sure you want to cancel this operation?") != JOptionPane.YES_OPTION)
            return;

        this.borderPaneRootSwitcher.goBack_BP();
    }

    @FXML
    private void register(ActionEvent e) {
        if (!areFieldsValid())
            return;

        if (this.selectedStocks.size() == 0) {
            PopupDialog.showCustomErrorDialog("Please add required stocks!");
            return;
        }

        String selectedServing = this.servingSizeCBox.getValue();

        if (isNewFoodVariant) {
            if (this.model.registerNewFood(productName, loggedInUserInfo)) {
                if (this.registerNewFoodVariant())
                    if (this.model.registerStockRequired(loggedInUserInfo, this.selectedStocks, productName,
                            getProductServingSizeID(selectedServing))) {
                        PopupDialog.showInfoDialog("Success", "Successfully registered new food variant");
                        this.borderPaneRootSwitcher.goBack_BP(2);
                        return;
                    }

            }
        } else {
            if (this.registerNewFoodVariant())
                if (this.model.registerStockRequired(loggedInUserInfo, selectedStocks, productName,
                        getProductServingSizeID(selectedServing))) {
                    PopupDialog.showInfoDialog("Success", "Successfully registered new food variant");
                    this.borderPaneRootSwitcher.goBack_BP(2);
                    return;
                }
        }

        PopupDialog.showCustomErrorDialog("An error has occured while registering food variant!");
    }

    // Returns product serving size id
    private int getProductServingSizeID(String selectedServing) {
        if (selectedServing.compareTo(ProductServingSize.ServingSize.SOLO.getString()) == 0)
            return 1;
        else if (selectedServing.compareTo(ProductServingSize.ServingSize.SHARING.getString()) == 0)
            return 2;
        else if (selectedServing.compareTo(ProductServingSize.ServingSize.LARGE_TRAY.getString()) == 0)
            return 3;
        else if (selectedServing.compareTo(ProductServingSize.ServingSize.EXTRA_LARGE_TRAY.getString()) == 0)
            return 4;
        return -1;
    }

    private boolean registerNewFoodVariant() {
        int productNameId = this.model.getProductNameId(productName);
        String selectedServing = this.servingSizeCBox.getValue();

        if (this.model.registerNewFoodVariant(loggedInUserInfo, new FoodVariant(0, productNameId,
                Double.valueOf(regularPriceField.getText()), getProductServingSizeID(selectedServing), 0,
                NumberHelper.getDiscountPrice(Double.valueOf(regularPriceField.getText())), false), this.productName))
            return true;
        return false;
    }

    @FXML
    private void addStock(ActionEvent e) {
        SelectStockController controller = (SelectStockController) this
                .initializeNextScreen_BP(ScreenPaths.Paths.SELECT_STOCK.getPath(), loggedInUserInfo, "SELECT STOCK");
        controller.initialize(this);
        controller.retrieveStocks(null);
    }

    // Removing stock on added stock
    @FXML
    private void removeStock(ActionEvent e) {
        this.selectedStocks.remove(this.selectedStock);
        this.requiredStockTable.setItems(this.selectedStocks);
        // Clear selection
        this.requiredStockTable.getSelectionModel().clearSelection();
    }

}
