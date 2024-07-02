package models.schemas;

import enums.DiscountCardTypes.CardType;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DiscountCard {
    private final StringProperty idNumber;
    private final StringProperty fname;
    private final StringProperty mname;
    private final StringProperty lname;
    private final StringProperty suffix;
    private final StringProperty type;
    private final int typeId;

    public DiscountCard(String idNumber, String fname, String mname, String lname, String suffix, String type, int typeId) {
        this.idNumber = new SimpleStringProperty(idNumber);
        this.fname = new SimpleStringProperty(fname);
        this.mname = new SimpleStringProperty(mname);
        this.lname = new SimpleStringProperty(lname);
        this.suffix = new SimpleStringProperty(suffix);
        this.type = new SimpleStringProperty(type);
        this.typeId = typeId;
    }

    public String getIdNumber() {
        return idNumber.get();
    }

    public StringProperty getIdNumberProperty() {
        return idNumber;
    }

    public String getFname() {
        return fname.get();
    }

    public StringProperty getFnameProperty() {
        return fname;
    }

    public String getMname() {
        return mname.get();
    }

    public StringProperty getMnameProperty() {
        return mname;
    }

    public String getLname() {
        return lname.get();
    }

    public StringProperty getLnameProperty() {
        return lname;
    }

    public String getSuffix() {
        return suffix.get();
    }

    public StringProperty getSuffixProperty() {
        return suffix;
    }

    public CardType getType() {
        return CardType.fromString(type.get());
    }

    public StringProperty getTypeProperty() {
        return type;
    }

    public int getTypeId() {
        return typeId;
    }
}
