package models.order;

import enums.StockProductType;
import javafx.collections.ObservableList;
import models.schemas.FoodVariant;
import models.schemas.Stock;
import models.schemas.DrinkVariant;

public class SelectSizeModel {

    private CreateOrderModel createOrderModel;

    public SelectSizeModel(CreateOrderModel createOrderModel) {
        this.createOrderModel = createOrderModel;
    }

    public int getProductId(String productName) {
        return createOrderModel.getProductId(productName);
    }

    public StockProductType.Type getProductType(int productId) {
        return createOrderModel.getProductType(productId);
    }

    public ObservableList<String> getDrinkSizes(int productId) {
        return createOrderModel.getDrinkSizes(productId);
    }

    public ObservableList<String> getFoodSizes(int productId) {
        return createOrderModel.getFoodSizes(productId);
    }

    public FoodVariant getFoodVariantBySize(int productId, String size) {
        return createOrderModel.getFoodVariantBySize(productId, size);
    }

    public ObservableList<Stock> getStockRequirements(FoodVariant foodVariant) {
        return createOrderModel.getStockRequirements(foodVariant);
    }

    public DrinkVariant getDrinkVariantBySize(int productId, String size) {
        return createOrderModel.getDrinkVariantBySize(productId, size);
    }
}
