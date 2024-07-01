package models.reusables;

import controllers.reusables.SetBeverageDecreaseQuantityController;
import enums.DatabaseLists;
import javafx.collections.ObservableList;

public class SetBeverageDecreaseQuantityModel {

    private SetBeverageDecreaseQuantityController controller;

    public SetBeverageDecreaseQuantityModel(SetBeverageDecreaseQuantityController controller) {
        this.controller = controller;
    }

    public ObservableList<String> retrieveReductionTypes() {
        return this.controller.getDBManager().query.getListOnDatabase(DatabaseLists.Lists.REDUCTION_TYPES);
    }

}
