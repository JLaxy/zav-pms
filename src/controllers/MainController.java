package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.shape.Sphere;
import models.modules.MainModel;

public class MainController {

    @FXML
    private Sphere mySphere;
    private double MOVEMENT = 10.0;
    private MainModel model;

    // FXML Constructor; runs after default class constructor if exists.
    @FXML
    private void initialize() {
        // Pass values to models here
        System.out.println("ini" + this.mySphere);
        model = new MainModel(this.mySphere, this.MOVEMENT);
    }

    public void moveUp(ActionEvent e) {
        this.model.up();
    }

    public void moveDown(ActionEvent e) {
        this.model.down();
    }

    public void moveLeft(ActionEvent e) {
        this.model.left();
    }

    public void moveRight(ActionEvent e) {
        this.model.right();
    }

}
