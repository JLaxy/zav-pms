/*
 * Contains all of the data needed to generate reports
 */

package models.modules.report;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import enums.ProductTypes;
import enums.ReportTimePeriods;
import enums.ReportTimePeriods.TimePeriod;
import models.helpers.DateHelper;
import models.helpers.MapHelper;
import models.helpers.database.DBManager;
import models.schemas.DrinkVariant;
import models.schemas.FoodVariant;

public class ReportModel {

    private DBManager zavDBManager;

    public ReportModel() {
        this.zavDBManager = new DBManager();
    }

    public Map<String, Object> getPeriodicReportData(ReportTimePeriods.TimePeriod timeInterval,
            LocalDate dateSelected) {
        Map<String, Object> reportStats = new HashMap<String, Object>();

        // {
        // 1 : {
        // ID : {
        // quantity : 0,
        // name : xxx,
        // serving_size : size,
        // }
        // },
        // 2 : {
        // ID : {
        // quantity : 0,
        // name : xxx,
        // serving_size : (size + unit measurement)
        // }
        // },
        // "successful" : "",
        // "unsuccessful" : "",
        // "income" : "",
        // "expenses" : "",
        // "revenue" : "",
        // }

        System.out.println(
                "Periodic Report: " + timeInterval.getDays() + " at date " + DateHelper.dateToString(dateSelected));
        // Retrieve all list of food
        ArrayList<Integer> foodList = this.zavDBManager.query.getFoodIDs();
        ArrayList<Integer> beverageList = this.zavDBManager.query.getBeverageIDs();

        // Food Map {ID : DETAILS}
        Map<Integer, Map<String, Object>> foodMap = new HashMap<Integer, Map<String, Object>>();
        // For each food, iterate how many times was sold during current time period
        for (Integer foodID : foodList) {
            FoodVariant retrievedFood = this.zavDBManager.query.getFoodVariantByID(foodID);

            // Food Details
            Map<String, Object> foodDetails = new HashMap<String, Object>();
            foodDetails.put("quantity", this.zavDBManager.query.getProductOrderCount(timeInterval,
                    DateHelper.dateToString(dateSelected), ProductTypes.Type.FOOD, foodID));
            foodDetails.put("name", retrievedFood.getFood_name());
            foodDetails.put("serving_size", retrievedFood.getServing_size_id_string());

            foodMap.put(foodID, foodDetails);
        }

        // Drink Map {ID : DETAILS}
        Map<Integer, Map<String, Object>> drinkMap = new HashMap<Integer, Map<String, Object>>();
        for (Integer drinkID : beverageList) {
            DrinkVariant retrievedDrink = this.zavDBManager.query.getBeverageByID(drinkID);

            // Drink Details
            Map<String, Object> drinkDetails = new HashMap<String, Object>();
            drinkDetails.put("quantity", this.zavDBManager.query.getProductOrderCount(timeInterval,
                    DateHelper.dateToString(dateSelected), ProductTypes.Type.BEVERAGE, drinkID));
            drinkDetails.put("name", retrievedDrink.getProduct_name());
            drinkDetails.put("serving_size", retrievedDrink.getSize_string());

            drinkMap.put(drinkID, drinkDetails);
        }

        // Sorting to Product sold
        reportStats.put("1", (Object) MapHelper.sortByProductSold(foodMap));
        reportStats.put("2", (Object) MapHelper.sortByProductSold(drinkMap));

        // System.out.println(reportStats); DEBUG

        // Successful and Unsuccessful transaction

        Map<String, Object> transStats = this.zavDBManager.query.getTransactionsStats(timeInterval,
                DateHelper.dateToString(dateSelected));
        reportStats.put("successful", transStats.get("successful"));
        reportStats.put("unsuccessful", transStats.get("unsuccessful"));

        // Income and expenses
        reportStats.put("income", transStats.get("income"));
        reportStats.put("expenses",
                this.zavDBManager.query.getExpenses(timeInterval, DateHelper.dateToString(dateSelected)));

        // Total Revenue
        reportStats.put("revenue", Double.valueOf(String.valueOf(reportStats.get("income")))
                - Double.valueOf(String.valueOf(reportStats.get("expenses"))));
        System.out.println("revenue: " + reportStats.get("revenue"));

        return reportStats;
    }

//     public Map<String, Object> getReorderQueueData(ReportTimePeriods.TimePeriod timeInterval,
//     LocalDate dateSelected)

    public static void main(String[] args) {
        new ReportModel().getPeriodicReportData(TimePeriod.WEEKLY, LocalDate.now());
    }

}
