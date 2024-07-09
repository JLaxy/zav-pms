/*
 * Forecast Algorithm; Algorithm responsible for forecasting demand
 */

package models.modules.forecastalgorithm;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import enums.ProgramSettings;
import enums.StockProductType;
import models.helpers.JSONManager;
import models.helpers.MapHelper;
import models.helpers.PopupDialog;
import models.helpers.database.DBManager;

public class Forecast {

    private DBManager zavDBManager;
    private final int MONTH_INTERVAL;

    public Forecast() {
        this.zavDBManager = new DBManager();
        // Retrieving month interval from program setting
        this.MONTH_INTERVAL = Integer
                .valueOf(new JSONManager().getSetting(ProgramSettings.Setting.FORECAST_MONTH_INTERVAL.getValue()));
        System.out.println("forecast month interval: " + this.MONTH_INTERVAL);
    }

    public boolean updateCriticalLevels() {
        try {
            Map<Integer, Object> mapOfAverages = calculateMovingAverage();

            // Iterate
            for (Map.Entry<Integer, Object> stock_product_type : mapOfAverages.entrySet()) {
                switch (stock_product_type.getKey()) {
                    // If beverage
                    case 1:
                        Map<Integer, Integer> myBev = (Map<Integer, Integer>) stock_product_type.getValue();

                        for (Map.Entry<Integer, Integer> drinkProduct : myBev.entrySet()) {
                            this.zavDBManager.query.updateBeverageCriticalLevel(drinkProduct.getKey(),
                                    drinkProduct.getValue());
                        }

                        break;
                    // If stock
                    case 2:
                        Map<Integer, Integer> myStock = (Map<Integer, Integer>) stock_product_type.getValue();

                        for (Map.Entry<Integer, Integer> stockItem : myStock.entrySet()) {
                            this.zavDBManager.query.updateStockCriticalLevel(stockItem.getKey(),
                                    stockItem.getValue());
                        }
                        break;
                    default:
                        break;
                }
            }
            // TODO: Log to user logs as user SYSTEM
            return true;
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, getClass().getName());
        }
        return false;
    }

    public static void setupAutoUpdateCriticalLevelTask() {
        Timer myTimer = new Timer(true);
        TimerTask critUpdater = new TimerTask() {
            @Override
            public void run() {
                System.out.println("Updating critical levels...");
                if (new Forecast().updateCriticalLevels()) {
                    PopupDialog.showInfoDialog("Auto Update Critical Levels", "Updated Critical Levels!");
                } else {
                    PopupDialog.showCustomErrorDialog("Algorithm failed to update Critical Levels");
                }
            }
        };

        long interval = 30L * 24 * 60 * 60 * 1000;
        myTimer.scheduleAtFixedRate(critUpdater, 0, interval);
        System.out.println("Auto Update Critical Level now initialized!");
    }

    private Map<Integer, Object> calculateMovingAverage() {
        // {
        // STOCK_PRODUCT_TYPE_ID : (STOCK_PRODUCT_ID : USED_QUANTITY)
        // }

        Map<Integer, Object> mapOfAverages = new HashMap<Integer, Object>();
        mapOfAverages.put(1, MapHelper.SortStockBeverageForecast(getForcastedBeverageUsage()));
        System.out.println();

        mapOfAverages.put(2, MapHelper.SortStockBeverageForecast(getForcastedStockUsage()));
        System.out.println();

        mapOfAverages.put(3, MapHelper.SortFoodForecast(getForcastedFoodUsage()));
        System.out.println();

        return mapOfAverages;
    }

    private Map<Integer, Integer> getForcastedFoodUsage() {
        // For Food
        ArrayList<Integer> foodIDs = this.zavDBManager.query.getFoodIDs();
        Map<Integer, Integer> foodAverages = new HashMap<Integer, Integer>();
        for (Integer foodID : foodIDs) {
            // DEBUG PRINT
            // Get total used in recent 3 months
            // System.out
            // .println("foodID: " + foodID + " \tused: " +
            // this.zavDBManager.query.getFoodOrderQuantity(foodID));

            foodAverages.put(foodID,
                    new BigDecimal(
                            this.zavDBManager.query.getFoodOrderQuantity(foodID) / Double.valueOf(this.MONTH_INTERVAL))
                            .setScale(0, RoundingMode.HALF_UP).intValue());
        }
        return foodAverages;
    }

    private Map<Integer, Integer> getForcastedStockUsage() {
        // For Stock
        ArrayList<Integer> stockIDs = this.zavDBManager.query.getStockIDs();
        Map<Integer, Integer> stockAverages = new HashMap<Integer, Integer>();

        for (Integer stockID : stockIDs) {
            // DEBUG PRINT
            // Get total used in recent 3 months
            // System.out.println("stockID: " + stockID + " \tused: "
            // + this.zavDBManager.query.getRecentQuantityUsed(StockProductType.Type.STOCK,
            // stockID));

            // Rounded up to 2 decimal places
            stockAverages.put(stockID,
                    new BigDecimal(this.zavDBManager.query.getRecentQuantityUsed(StockProductType.Type.STOCK, stockID)
                            / this.MONTH_INTERVAL).setScale(0, RoundingMode.HALF_UP).intValue());
        }
        return stockAverages;
    }

    private Map<Integer, Integer> getForcastedBeverageUsage() {
        // For Beverage
        ArrayList<Integer> beverageIDs = this.zavDBManager.query.getBeverageIDs();
        Map<Integer, Integer> beverageAverages = new HashMap<Integer, Integer>();
        for (Integer beverageID : beverageIDs) {
            // DEBUG PRINT
            // Get total used in recent 3 months
            // System.out.println("BeverageID: " + beverageID + " \tused: "
            // +
            // this.zavDBManager.query.getRecentQuantityUsed(StockProductType.Type.BEVERAGE,
            // beverageID));

            // Rounded up to 2 decimal places
            beverageAverages.put(beverageID,
                    new BigDecimal(
                            this.zavDBManager.query.getRecentQuantityUsed(StockProductType.Type.BEVERAGE, beverageID)
                                    / this.MONTH_INTERVAL)
                            .setScale(0, RoundingMode.HALF_UP).intValue());
        }
        return beverageAverages;
    }

}