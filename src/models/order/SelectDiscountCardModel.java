package models.order;

import controllers.order.SelectDiscountCardController;
import javafx.collections.ObservableList;
import models.schemas.DiscountCard;
import models.schemas.DiscountCardType;
import models.schemas.SeniorPwdId;

public class SelectDiscountCardModel {

    private SelectDiscountCardController controller;

    public SelectDiscountCardModel(SelectDiscountCardController controller) {
        this.controller = controller;
    }

    public ObservableList<DiscountCard> getDiscountCards() {
        return this.controller.getDBManager().query.getDiscountCards();
    }

    public boolean addNewDiscountCard(SeniorPwdId newCard) {
        if (isDiscountCardExists(newCard.getIdNumber())) {
            return false;
        }
        return this.controller.getDBManager().query.saveDiscountCard(newCard);
    }

    public boolean isDiscountCardExists(String idNumber) {
        return this.controller.getDBManager().query.isDiscountCardExists(idNumber);
    }

    public ObservableList<DiscountCardType> getDiscountCardTypes() {
        return this.controller.getDBManager().query.getDiscountCardTypes();
    }

    public ObservableList<DiscountCard> searchDiscountCards(String searchText) {
        ObservableList<DiscountCard> allCards = getDiscountCards();
        return allCards.filtered(card -> 
            (card.getIdNumber() != null && card.getIdNumber().toLowerCase().contains(searchText)) ||
            (card.getFname() != null && card.getFname().toLowerCase().contains(searchText)) ||
            (card.getMname() != null && card.getMname().toLowerCase().contains(searchText)) ||
            (card.getLname() != null && card.getLname().toLowerCase().contains(searchText)) ||
            (card.getSuffix() != null && card.getSuffix().toLowerCase().contains(searchText))
        );
    }
}
