package models.helpers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.schemas.OrderProduct;

public class ObservableListHelper {

    public static ObservableList<OrderProduct> deepCopy(ObservableList<OrderProduct> originalList) {
        ObservableList<OrderProduct> copyList = FXCollections.observableArrayList();

        for (OrderProduct item : originalList) {
            copyList.add(item.copy());
        }

        return copyList;
    }

    public static ObservableList<OrderProduct> getDiscountedOnList(ObservableList<OrderProduct> originalList) {
        ObservableList<OrderProduct> copyList = FXCollections.observableArrayList();

        for (OrderProduct item : originalList) {

            if (!item.isDiscountApplied()) {
                continue;
            }

            copyList.add(item.copy());
        }

        return copyList;
    }

    public static ObservableList<OrderProduct> getRegularOnList(ObservableList<OrderProduct> originalList) {
        ObservableList<OrderProduct> copyList = FXCollections.observableArrayList();

        for (OrderProduct item : originalList) {

            if (item.isDiscountApplied()) {
                continue;
            }

            copyList.add(item.copy());
        }

        return copyList;
    }
}
