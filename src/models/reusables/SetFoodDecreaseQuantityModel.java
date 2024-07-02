package models.reusables;

import controllers.reusables.SetFoodDecreaseQuantityController;
import enums.DatabaseLists;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SetFoodDecreaseQuantityModel {
    private SetFoodDecreaseQuantityController controller;

    public SetFoodDecreaseQuantityModel(SetFoodDecreaseQuantityController controller) {
        this.controller = controller;
    }

    public ObservableList<String> retrieveReductionTypeValues() {
        ObservableList<String> list = FXCollections.observableArrayList();

        list = this.controller.getDBManager().query.getListOnDatabase(DatabaseLists.Lists.REDUCTION_TYPES);

        return list;
    }
}
