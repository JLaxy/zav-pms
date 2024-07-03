package models.order;

import controllers.order.DiscountOrdersController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.schemas.DiscountCard;
import models.schemas.OrderProduct;
import models.helpers.database.DBManager;

public class DiscountOrdersModel {

    private DiscountOrdersController controller;

    public DiscountOrdersModel(DiscountOrdersController controller) {
        this.controller = controller;
    }

    public ObservableList<OrderProduct> fetchOrderProducts() {
        // Fetch order products from the database
        return this.controller.getDBManager().query.fetchOrderProducts();
    }

    public ObservableList<DiscountCard> fetchDiscountCards() {
        // Fetch discount cards from the database
        return this.controller.getDBManager().query.fetchDiscountCards();
    }

    public void applyDiscount(OrderProduct orderProduct, DiscountCard discountCard) {
        // Apply discount logic
        double discountRate = discountCard.getDiscountRate();
        double discountedPrice = orderProduct.getAmount() * (1 - discountRate);
        orderProduct.setDiscountedPrice(discountedPrice);
    }
}
