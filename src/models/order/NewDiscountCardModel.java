package models.order;

import controllers.order.NewDiscountCardController;
import javafx.collections.ObservableList;
import models.schemas.DiscountCard;
import models.schemas.DiscountCardType;
import models.schemas.SeniorPwdId;

public class NewDiscountCardModel {

    private NewDiscountCardController controller;

    public NewDiscountCardModel(NewDiscountCardController controller) {
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
}
