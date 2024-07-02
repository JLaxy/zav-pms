package models.order;

import javafx.collections.ObservableList;
import models.helpers.database.DBQuery;
import models.schemas.DiscountCard;
import models.schemas.DiscountCardType;
import models.schemas.SeniorPwdId;
import models.helpers.database.DBManager;

public class DiscountCardModel {
    private final DBQuery dbQuery;

    public DiscountCardModel(DBManager zavPMSDB) {
        this.dbQuery = new DBQuery(zavPMSDB);
    }

    public ObservableList<DiscountCard> getDiscountCards() {
        return dbQuery.getDiscountCards();
    }

    public boolean addNewDiscountCard(SeniorPwdId newCard) {
        if (isDiscountCardExists(newCard.getIdNumber())) {
            return false;
        }
        return dbQuery.saveDiscountCard(newCard);
    }

    public boolean isDiscountCardExists(String idNumber) {
        return dbQuery.isDiscountCardExists(idNumber);
    }

    public ObservableList<DiscountCardType> getDiscountCardTypes() {
        return dbQuery.getDiscountCardTypes();
    }
}
