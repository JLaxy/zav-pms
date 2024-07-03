package controllers.order;

import controllers.ParentController;
import enums.ScreenPaths;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.application.Platform;
import models.helpers.PopupDialog;
import models.schemas.DiscountCard;
import models.schemas.OrderProduct;

public class DiscountOrdersController extends ParentController {
    @FXML
    private AnchorPane discountcardDetailsPane;
    @FXML
    private TextField searchField;
    @FXML
    private TableView<OrderProduct> stockTable;
    @FXML
    private TableColumn<OrderProduct, String> productNameCol;
    @FXML
    private TableColumn<OrderProduct, Integer> quantityCol;
    @FXML
    private TableColumn<OrderProduct, String> sizeCol;
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
    private DiscountCard boundDiscountCard;

    public void initialize() {
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("productName"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        sizeCol.setCellValueFactory(new PropertyValueFactory<>("size"));
        checkboxCol.setCellValueFactory(cellData -> cellData.getValue().discountAppliedProperty());
        checkboxCol.setCellFactory(CheckBoxTableCell.forTableColumn(checkboxCol));

        stockTable.setItems(orderProducts);

        stockTable.setRowFactory(tv -> {
            TableRow<OrderProduct> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) {
                    OrderProduct clickedRow = row.getItem();
                    clickedRow.setDiscountApplied(!clickedRow.isDiscountApplied());
                    stockTable.refresh();
                }
            });
            return row;
        });

        discountcardDetailsPane.setVisible(false);
    }

    public void setOrderProducts(ObservableList<OrderProduct> orderProducts) {
        // Reset discount applied state for all products
        for (OrderProduct product : orderProducts) {
            product.setDiscountApplied(false);
        }
        this.orderProducts = orderProducts;
        stockTable.setItems(orderProducts);
        stockTable.refresh();  // Refresh the table to ensure the checkboxes are cleared
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
        if (boundDiscountCard == null) {
            PopupDialog.showCustomErrorDialog("You must bind a discount card before applying a discount.");
            return;
        }

        boolean hasSelectedProducts = false;
        for (OrderProduct orderProduct : orderProducts) {
            if (orderProduct.isDiscountApplied()) {
                orderProduct.setAmount(orderProduct.getDiscountedPrice());
                orderProduct.setDiscounted(true);
                hasSelectedProducts = true;
            }
        }

        if (!hasSelectedProducts) {
            PopupDialog.showCustomErrorDialog("No products selected for discount.");
            return;
        }

        stockTable.refresh();
        PopupDialog.showInfoDialog("Success", "Discount applied successfully.");
        Platform.runLater(() -> this.borderPaneRootSwitcher.goBack_BP());
    }

    @FXML
    private void search() {
        String searchText = searchField.getText();
        if (searchText == null || searchText.isEmpty()) {
            stockTable.setItems(orderProducts);
            return;
        }

        ObservableList<OrderProduct> filteredList = FXCollections.observableArrayList();
        for (OrderProduct product : orderProducts) {
            if (product.getProductName().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(product);
            }
        }
        stockTable.setItems(filteredList);
    }

    @FXML
    private void goBack() {
        System.out.println("going back...");
        this.borderPaneRootSwitcher.goBack_BP();
    }

    public void updateDiscountCardDetails(DiscountCard discountCard) {
        this.boundDiscountCard = discountCard;
        firstNameLabel.setText(discountCard.getFname());
        middleNameLabel.setText(discountCard.getMname());
        lastNameLabel.setText(discountCard.getLname());
        suffixLabel.setText(discountCard.getSuffix());
        cardTypeLabel.setText(discountCard.getType().getName());
        idnumberLabel.setText(discountCard.getIdNumber());

        discountcardDetailsPane.setVisible(true);
    }

    // Add the missing method
    public void getPreviousScreen() {
        this.borderPaneRootSwitcher.goBack_BP();
    }
}
