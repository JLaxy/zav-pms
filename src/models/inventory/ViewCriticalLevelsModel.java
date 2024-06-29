package models.inventory;

import controllers.inventory.ViewCriticalLevelsController;
import javafx.collections.ObservableList;
import models.schemas.ExpiringItems;

public class ViewCriticalLevelsModel {
    private ViewCriticalLevelsController controller;

    public ViewCriticalLevelsModel(ViewCriticalLevelsController controller) {
        this.controller = controller;
    }

    public ObservableList<ExpiringItems> getItemsInCriticalLevels() {
        return this.controller.getDBManager().query.getItemsInCriticalLevel();
    }
}
