package models.order;

import javafx.collections.ObservableList;
import models.helpers.database.DBManager;
import models.helpers.database.DBQuery;
import models.schemas.FetchedOrderProduct;

public class OrderProductModel {
    private DBQuery dbQuery;

    public OrderProductModel(DBManager dbManager) {
        this.dbQuery = new DBQuery(dbManager);
    }

    public ObservableList<FetchedOrderProduct> fetchOrderProducts() {
        return null;
    }
}
