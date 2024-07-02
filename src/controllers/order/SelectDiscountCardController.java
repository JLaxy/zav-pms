package controllers.order;

import controllers.ParentController;
import enums.ScreenPaths;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import models.schemas.DiscountCard;
import models.schemas.User;
import models.order.DiscountCardModel;
import models.helpers.database.DBManager;

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

    private DiscountCardModel discountCardModel;
    private ObservableList<DiscountCard> allDiscountCards;
    private DiscountOrdersController discountOrdersController;

    @FXML
    public void initialize(User selectedUser, DiscountOrdersController discountOrdersController) {
        DBManager dbManager = new DBManager(); // Initialize DBManager as needed
        discountCardModel = new DiscountCardModel(dbManager);
        this.discountOrdersController = discountOrdersController;

        idNumberCol.setCellValueFactory(cellData -> cellData.getValue().getIdNumberProperty());
        fnameCol.setCellValueFactory(cellData -> cellData.getValue().getFnameProperty());
        mnameCol.setCellValueFactory(cellData -> cellData.getValue().getMnameProperty());
        lnameCol.setCellValueFactory(cellData -> cellData.getValue().getLnameProperty());
        suffixCol.setCellValueFactory(cellData -> cellData.getValue().getSuffixProperty());
        typeCol.setCellValueFactory(cellData -> cellData.getValue().getTypeProperty());

        loadDiscountCards();
    }

    public void loadDiscountCards() {
        allDiscountCards = discountCardModel.getDiscountCards();
        discountCardTable.setItems(allDiscountCards);
    }

    @FXML
    private void search() {
        String searchText = searchField.getText().toLowerCase();
        ObservableList<DiscountCard> allCards = discountCardModel.getDiscountCards();
        ObservableList<DiscountCard> filteredCards = allCards.filtered(card -> 
            (card.getIdNumber() != null && card.getIdNumber().toLowerCase().contains(searchText)) ||
            (card.getFname() != null && card.getFname().toLowerCase().contains(searchText)) ||
            (card.getMname() != null && card.getMname().toLowerCase().contains(searchText)) ||
            (card.getLname() != null && card.getLname().toLowerCase().contains(searchText)) ||
            (card.getSuffix() != null && card.getSuffix().toLowerCase().contains(searchText))
        );
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
