package controllers.order;

import controllers.ParentController;
import enums.ScreenPaths;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import models.schemas.DiscountCard;
import models.schemas.OrderProduct;

public class DiscountOrdersController extends ParentController {
    @FXML
    private TableView<OrderProduct> stockTable;
    @FXML
    private TableColumn<OrderProduct, String> productNameCol;
    @FXML
    private TableColumn<OrderProduct, Integer> quantityCol;
    @FXML
    private TableColumn<OrderProduct, Boolean> checkboxCol;

    @FXML
    private Label firstNameLabel;
    @FXML
    private Label middleNameLabel;
    @FXML
    private Label lastNameLabel;
    @FXML
    private Label suffixLabel;
    @FXML
    private Label cardTypeLabel;
    @FXML
    private Label idnumberLabel;

    private ObservableList<OrderProduct> orderProducts = FXCollections.observableArrayList();

    public void initialize() {
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("productName"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        checkboxCol.setCellValueFactory(cellData -> cellData.getValue().discountAppliedProperty());
        checkboxCol.setCellFactory(CheckBoxTableCell.forTableColumn(checkboxCol));

        stockTable.setItems(orderProducts);

        stockTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                newSelection.setDiscountApplied(!newSelection.isDiscountApplied());
                stockTable.refresh();
            }
        });
    }

    public void setOrderProducts(ObservableList<OrderProduct> orderProducts) {
        this.orderProducts.setAll(orderProducts);
    }

    @FXML
    private void binddiscountcard() {
        SelectDiscountCardController controller = (SelectDiscountCardController) 
        initializeNextScreen_BP(ScreenPaths.Paths.SELECT_DISCOUNT_CARD.getPath(), this.loggedInUserInfo, "BIND DISCOUNT CARD");
        controller.initialize(this.loggedInUserInfo, this);
        System.out.println("bind discount card...");
    }

    @FXML
    private void applydiscount() {
        System.out.println("apply discount");
    }

    @FXML
    private void search() {
        System.out.println("searching");
    }

    @FXML
    private void goBack() {
        System.out.println("going back...");
        this.borderPaneRootSwitcher.goBack_BP();
    }

    public void updateDiscountCardDetails(DiscountCard discountCard) {
        firstNameLabel.setText(discountCard.getFname());
        middleNameLabel.setText(discountCard.getMname());
        lastNameLabel.setText(discountCard.getLname());
        suffixLabel.setText(discountCard.getSuffix());
        cardTypeLabel.setText(discountCard.getType().getName());
        idnumberLabel.setText(discountCard.getIdNumber());
    }
}
