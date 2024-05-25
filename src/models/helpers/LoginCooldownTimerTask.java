/*
 * Responsible for the implementation of the cooldown in the login screen
 */
package models.helpers;

import java.util.TimerTask;

import controllers.LoginController;
import javafx.application.Platform;

public class LoginCooldownTimerTask extends TimerTask {

    private LoginController controller;
    private final String COOLDOWN_MSG = "You are not allowed to log-in until: ";

    // Constructor
    public LoginCooldownTimerTask(LoginController controller) {
        this.controller = controller;
    }

    // Tasked performed by timer
    @Override
    public void run() {
        Platform.runLater(() -> {
            // If there login cooldown is still active
            if (DateHelper.getLoginCooldownSecs() > 0) {
                this.controller.getErrorLabel()
                        .setText(COOLDOWN_MSG + calculateTime(DateHelper.getLoginCooldownSecs()));
            } else {
                // Self destruct
                this.cancel();
                this.controller.checkCooldown();
            }
        });
    }

    // Returns the number of minutes and seconds left in the cooldown
    private String calculateTime(long cooldownSeconds) {
        // Get minutes
        long mins = cooldownSeconds / 60;
        // If more than or equal to (a minute) / (60 seconds)
        if (mins >= 1) {
            long secs = cooldownSeconds % 60;
            return mins + "m " + secs + "s";
        }
        return cooldownSeconds + "s";
    }

}
