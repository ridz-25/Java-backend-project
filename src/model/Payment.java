package src.model;

import java.sql.Timestamp;

public class Payment {

    private int paymentId;
    private int orderId;
    private int buyerId;
    private double amount;
    private String paymentMethod;
    private PaymentStatus paymentStatus;
    private String transactionId;
    private Timestamp paymentDate;

    public Payment(int paymentId, int orderId, int buyerId,
        double amount, String paymentMethod,
        PaymentStatus paymentStatus, String transactionId,
        Timestamp paymentDate) {

        this.paymentId = paymentId;
        this.orderId = orderId;
        this.buyerId = buyerId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.transactionId = transactionId;
        this.paymentDate = paymentDate;
    }

    // Getters
    public int getPaymentId() { return paymentId; }
    public int getOrderId() { return orderId; }
    public int getBuyerId() { return buyerId; }
    public double getAmount() { return amount; }
    public String getPaymentMethod() { return paymentMethod; }
    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public String getTransactionId() { return transactionId; }
    public Timestamp getPaymentDate() { return paymentDate; }

    // Setters
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    // Payment operations
    public void completePayment() {
        this.paymentStatus = PaymentStatus.COMPLETED;
    }

    public void failPayment() {
        this.paymentStatus = PaymentStatus.FAILED;
    }

    public void refundPayment() {
        if (this.paymentStatus == PaymentStatus.COMPLETED) {
            this.paymentStatus = PaymentStatus.REFUNDED;
        }
    }

    public boolean isSuccessful() {
        return this.paymentStatus == PaymentStatus.COMPLETED;
    }
}