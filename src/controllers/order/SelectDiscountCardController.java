package controllers.order;

import controllers.ParentController;
import enums.ScreenPaths;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import models.schemas.DiscountCard;
import models.schemas.User;
import models.order.SelectDiscountCardModel;

public class SelectDiscountCardController extends ParentController {
    @FXML
    private TableView<DiscountCard> discountCardTable;
    @FXML
    private TableColumn<DiscountCard, String> idNumberCol;
    @FXML
    private TableColumn<DiscountCard, String> fnameCol;
    @FXML
    private TableColumn<DiscountCard, String> mnameCol;
    @FXML
    private TableColumn<DiscountCard, String> lnameCol;
    @FXML
    private TableColumn<DiscountCard, String> suffixCol;
    @FXML
    private TableColumn<DiscountCard, String> typeCol;
    @FXML
    private TextField searchField;

    private SelectDiscountCardModel selectDiscountCardModel;
    private DiscountOrdersController discountOrdersController;

    public void initialize(User selectedUser, DiscountOrdersController discountOrdersController) {
        this.selectDiscountCardModel = new SelectDiscountCardModel(this);
        this.discountOrdersController = discountOrdersController;
        initializeTableColumns();
        loadDiscountCards();
    }

    private void initializeTableColumns() {
        idNumberCol.setCellValueFactory(cellData -> cellData.getValue().getIdNumberProperty());
        fnameCol.setCellValueFactory(cellData -> cellData.getValue().getFnameProperty());
        mnameCol.setCellValueFactory(cellData -> cellData.getValue().getMnameProperty());
        lnameCol.setCellValueFactory(cellData -> cellData.getValue().getLnameProperty());
        suffixCol.setCellValueFactory(cellData -> cellData.getValue().getSuffixProperty());
        typeCol.setCellValueFactory(cellData -> cellData.getValue().getTypeProperty());
    }

    public void loadDiscountCards() {
        ObservableList<DiscountCard> discountCards = selectDiscountCardModel.getDiscountCards();
        discountCardTable.setItems(discountCards);
    }

    @FXML
    private void search() {
        String searchText = searchField.getText().toLowerCase();
        ObservableList<DiscountCard> filteredCards = selectDiscountCardModel.searchDiscountCards(searchText);
        discountCardTable.setItems(filteredCards);
    }

    @FXML
    private void newdiscountcard() {
        NewDiscountCardController controller = (NewDiscountCardController) this
                .initializePopUpDialog(ScreenPaths.Paths.NEW_DISCOUNT_CARD.getPath(), this.loggedInUserInfo);
        controller.initialize(null, this);
    }

    @FXML
    private void goBack() {
        System.out.println("going back...");
        this.borderPaneRootSwitcher.goBack_BP();
    }

    @FXML
    private void selectdiscountcard() {
        DiscountCard selectedCard = discountCardTable.getSelectionModel().getSelectedItem();
        if (selectedCard != null) {
            discountOrdersController.updateDiscountCardDetails(selectedCard);
            goBack();
        }
    }
}
