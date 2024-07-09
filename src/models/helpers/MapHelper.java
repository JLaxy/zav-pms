package models.helpers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MapHelper {
    public static Map<Integer, Integer> SortStockBeverageForecast(Map<Integer, Integer> map) {
        // Convert map entries to a list
        List<Map.Entry<Integer, Integer>> list = new ArrayList<>(map.entrySet());

        // Sort the list by values in descending order
        list.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        // Create a new LinkedHashMap to preserve the order of the sorted entries
        Map<Integer, Integer> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<Integer, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        // Print the sorted map
        for (Map.Entry<Integer, Integer> entry : sortedMap.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        return sortedMap;
    }

    public static Map<Integer, Integer> SortFoodForecast(Map<Integer, Integer> map) {
        // Convert map entries to a list
        List<Map.Entry<Integer, Integer>> list = new ArrayList<>(map.entrySet());

        // Sort the list by values in descending order
        list.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        // Create a new LinkedHashMap to preserve the order of the sorted entries
        Map<Integer, Integer> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<Integer, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        // Print the sorted map
        for (Map.Entry<Integer, Integer> entry : sortedMap.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        return sortedMap;
    }

    public static Map<Integer, Map<String, Object>> sortByProductSold(Map<Integer, Map<String, Object>> productList) {
        // Convert the map entries to a list
        List<Map.Entry<Integer, Map<String, Object>>> entryList = new ArrayList<>(productList.entrySet());

        // Sort the list by 'quantity' in descending order
        entryList.sort((entry1, entry2) -> {
            Integer quantity1 = (Integer) entry1.getValue().get("quantity");
            Integer quantity2 = (Integer) entry2.getValue().get("quantity");
            return quantity2.compareTo(quantity1); // Descending order
        });

        // Optionally, convert the sorted list back to a LinkedHashMap to preserve order
        Map<Integer, Map<String, Object>> sortedSubMap = new LinkedHashMap<>();
        for (Map.Entry<Integer, Map<String, Object>> entry : entryList) {
            sortedSubMap.put(entry.getKey(), entry.getValue());
        }

        return sortedSubMap;
    }
}
