package controllers;

import javafx.event.ActionEvent;

public class LoginController extends ParentController {

    public void goBack(ActionEvent e) {
        try {
            System.out.println("going back... ");
            rootSwitcher.goBack();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
