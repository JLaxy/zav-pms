package models.schemas;

import models.helpers.DateHelper;
import models.helpers.database.DBManager;

public class Payment {
    private int id, transaction_id, mode_of_payment_id, payment_type_id, ewallet_vendor_id;
    private Double change, paid;
    private String customer_name, contact_number, reference_number, mode_of_payment_id_string, payment_type_id_string,
            ewallet_vendor_id_string, date_paid_formatted, date_paid;

    public Payment(int id, String customer_name, String contact_number, int transaction_id, int mode_of_payment_id,
            String date_paid,
            Double change, Double paid,
            int payment_type_id, String reference_number, int ewallet_vendor_id) {
        this.id = id;
        this.customer_name = customer_name;
        this.contact_number = contact_number;
        this.transaction_id = transaction_id;
        this.mode_of_payment_id = mode_of_payment_id;
        this.date_paid = date_paid;
        this.change = change;
        this.paid = paid;
        this.payment_type_id = payment_type_id;
        this.reference_number = reference_number;
        this.ewallet_vendor_id = ewallet_vendor_id;
        getStringEquivalents();
    }

    private void getStringEquivalents() {
        DBManager db = new DBManager();

        this.mode_of_payment_id_string = db.query.getModeOfPaymentByID(this.mode_of_payment_id);
        this.payment_type_id_string = db.query.getPaymentTypeByID(this.payment_type_id);
        this.ewallet_vendor_id_string = db.query.getEwallet_vendor_id(this.ewallet_vendor_id);
        this.date_paid_formatted = DateHelper.dateToFormattedDate(DateHelper.stringToDate(this.date_paid));
    }

    public String getDate_paid_formatted() {
        return this.date_paid_formatted;
    }

    public String getMode_of_payment_id_string() {
        return this.mode_of_payment_id_string;
    }

    public String getPayment_type_id_string() {
        return this.payment_type_id_string;
    }

    public String getEwallet_vendor_id_string() {
        return this.ewallet_vendor_id_string;
    }

    public int getId() {
        return this.id;
    }

    public String getCustomer_name() {
        return this.customer_name;
    }

    public int getTransaction_id() {
        return this.transaction_id;
    }

    public String getContact_number() {
        return this.contact_number;
    }

    public String getReferenceNumber() {
        return this.reference_number;
    }

    public int getMode_of_payment_id() {
        return this.mode_of_payment_id;
    }

    public int getEwallet_vendor_id() {
        return this.ewallet_vendor_id;
    }

    public int getPayment_type_id() {
        return this.payment_type_id;
    }

    public String getDate_paid() {
        return this.date_paid;
    }

    public Double getChange() {
        return this.change;
    }

    public Double getPaid() {
        return this.paid;
    }
}
