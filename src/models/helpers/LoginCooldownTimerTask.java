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

    @Override
    public void run() {
        Platform.runLater(() -> {
            if (DateHelper.getLoginCooldownSecs() > 0) {
                this.controller.getErrorLabel()
                        .setText(COOLDOWN_MSG + DateHelper.getLoginCooldownSecs() + " second(s)");
            } else {
                this.cancel();
                this.controller.checkCooldown();
            }
        });
    }

}
