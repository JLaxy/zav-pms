package models.modules;

import javafx.scene.shape.Sphere;

public class MainModel {
    private Sphere bilog;
    private double movement;

    public MainModel(Sphere bilog, double movement) {
        this.bilog = bilog;
        this.movement = movement;
    }

    public void up() {
        this.bilog.setLayoutY(this.bilog.getLayoutY() - this.movement);
        System.out.println("up");
    }

    public void down() {
        this.bilog.setLayoutY(this.bilog.getLayoutY() + this.movement);
        System.out.println("down");
    }

    public void left() {
        this.bilog.setLayoutX(this.bilog.getLayoutX() - this.movement);
        System.out.println("left");
    }

    public void right() {
        this.bilog.setLayoutX(this.bilog.getLayoutX() + this.movement);
        System.out.println("right");
    }
}
