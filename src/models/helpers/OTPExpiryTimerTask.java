/*
 * Responsible for the implementation of the cooldown in the login screen
 */
package models.helpers;

import java.time.LocalDateTime;
import java.util.TimerTask;

import controllers.login.OTPLoginController;
import javafx.application.Platform;

public class OTPExpiryTimerTask extends TimerTask {

    private OTPLoginController controller;
    private LocalDateTime expiryDate;

    // Constructor
    public OTPExpiryTimerTask(OTPLoginController controller, LocalDateTime expiryDate) {
        this.controller = controller;
        this.expiryDate = expiryDate;
    }

    // Tasked performed by timer
    @Override
    public void run() {
        Platform.runLater(() -> {
            // If it is expiry date
            if (DateHelper.isDateTimeBeforeNow(this.expiryDate)) {
                // Telling controller OTP has expired
                this.controller.otpHasExpired();
                // Self Destruct
                this.cancel();
            }
        });
    }
}
