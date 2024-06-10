package controllers.inventory;

import controllers.ParentController;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import models.inventory.RegisterNewStockModel;
import models.schemas.Stock;

public class RegisterNewStockController extends ParentController {

    @FXML
    private TextField stocknameField, criticalLevelField;

    @FXML 
    private ComboBox<String> unitmeasureCBox, stockTypeCBox;

    private RegisterNewStockModel model;

    @FXML
    public void initialize() {
        this.model = new RegisterNewStockModel(this);
    }

    @FXML
    private void register() {
        
        System.out.println("Register");
    }

    @FXML
    private void registernewstocktype() {
        initializeNextScreen_BP("../../views/fxmls/inventory/RegisterNewStockTypeView.fxml", this.loggedInUserInfo, "STOCK INVENTORY");
        System.out.println("Register New Stock Type");
    }

    @FXML
    private void goBack() {
        System.out.println("going back...");
        this.borderPaneRootSwitcher.goBack_BP();
    }

}
