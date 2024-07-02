package models.schemas;

public class DiscountCardType {
    private int id;
    private String type;

    public DiscountCardType(int id, String type) {
        this.id = id;
        this.type = type;
    }

    // Getters and setters...

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }
}