package models.reusables;

import controllers.reusables.SetDecreaseQuantityController;
import enums.DatabaseLists;
import javafx.collections.ObservableList;

public class SetDecreaseQuantityModel {
    private SetDecreaseQuantityController controller;

    public SetDecreaseQuantityModel(SetDecreaseQuantityController controller) {
        this.controller = controller;
    }

    public ObservableList<String> retrieveReductionTypeValues() {
        return this.controller.getDBManager().query.getListOnDatabase(DatabaseLists.Lists.REDUCTION_TYPES);
    }
}
