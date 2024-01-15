package controllers;

import javafx.event.ActionEvent;

public class LoginController extends ParentController {

    // Go Back Button Action
    public void goBack(ActionEvent e) {
        try {
            System.out.println("going back... ");
            // Calling Root Switcher to go back to previous page.
            rootSwitcher.goBack();
        } catch (Exception ex) {
            System.out.println("Error at: " + getClass());
            ex.printStackTrace();
        }
    }
}
