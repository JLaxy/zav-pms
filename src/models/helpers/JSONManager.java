/*
 * Class that handles JSON files used in the program
 */

package models.helpers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JSONManager {

    // Retrieves Login Cooldown in Stored in settings file
    public String getLoginCooldown() {
        try (Reader myReader = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream("../../settings.json")))) {
            JsonObject myObject = JsonParser.parseReader(myReader).getAsJsonObject();
            JsonObject program_settings = myObject.getAsJsonObject("program_settings");
            return program_settings.get("cooldown").getAsString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
