package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class TestController extends ParentController implements Initializable {

    @FXML
    ListView<String> myListView;
    @FXML
    Label myLabel;

    String[] carStrings = { "Lexus LX 500", "Porsche 911 GT3 RS", "Toyota Camry 2023" };

    public void selected(ActionEvent e) {
        try {

        } catch (Exception ex) {
            System.out.println("Error at: " + getClass());
            ex.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        myListView.getItems().addAll(carStrings);
        myListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
                myLabel.setText(myListView.getSelectionModel().getSelectedItem());
            }

        });
    }

}
