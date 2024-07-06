package models.schemas;

import java.time.LocalDateTime;

public class Transaction {
    private int id, transaction_type_id;
    private Double discount_amount, total_amount_payable, balance;
    private Boolean isVoided;
    private String customer_name, contact_number;
    private LocalDateTime order_date, target_date, fulfillment_date;

    public Transaction(int id, String customer_name, LocalDateTime order_date, LocalDateTime target_date,
            String contact_number, int transaction_type_id, Double discount_amount, boolean isVoided,
            Double total_amount_payable, LocalDateTime fulfillment_date, Double balance) {
        this.id = id;
        this.transaction_type_id = transaction_type_id;
        this.discount_amount = discount_amount;
        this.total_amount_payable = total_amount_payable;
        this.balance = balance;
        this.isVoided = isVoided;
        this.customer_name = customer_name;
        this.contact_number = contact_number;
        this.order_date = order_date;
        this.target_date = target_date;
        this.fulfillment_date = fulfillment_date;
    }

    public int getId() {
        return this.id;
    }

    public int getTransaction_type_id() {
        return this.transaction_type_id;
    }

    public Double getDiscount_amount() {
        return this.discount_amount;
    }

    public Double getTotal_amount_payable() {
        return this.total_amount_payable;
    }

    public Double getBalance() {
        return this.balance;
    }

    public Boolean getIsvoided() {
        return this.isVoided;
    }

    public String getCustomer_name() {
        return this.customer_name;
    }

    public String getContact_number() {
        return this.contact_number;
    }

    public LocalDateTime getOrder_date() {
        return this.order_date;
    }

    public LocalDateTime getTarget_date() {
        return this.target_date;
    }

    public LocalDateTime getFulfillment_date() {
        return this.fulfillment_date;
    }
}
