package model;

import java.sql.Timestamp;

public class Order {
    private int orderId;
    private int buyerId;
    private int gigId;
    private int sellerId;
    private double amount;
    private OrderStatus status;
    private Timestamp orderDate;
    private Timestamp deliveryDate;
    private String notes;

    Order(int orderId,int buyerId,int gigId,int sellerId,
        double amount,OrderStatus status,Timestamp orderDate,
        Timestamp deliveryDate,String notes){
            this.orderId=orderId;
            this.buyerId=buyerId;
            this.gigId=gigId;
            this.sellerId=sellerId;
            this.amount=amount;
            this.status=status;
            this.orderDate=orderDate;
            this.deliveryDate=deliveryDate;
            this.notes=notes;
        }

    //getters
    public int getOrderId(){
        return orderId;
    }
    public int getBuyerId(){
        return buyerId;
    }
    public int getGigId(){
        return gigId;
    }
    public int getSellerId(){
        return sellerId;
    }
    public double getAmount(){
        return amount;
    }
    public OrderStatus getStatus(){
        return status;
    }
    public Timestamp getOrderDate(){
        return orderDate;
    }
    public Timestamp getDeliveryDate(){
        return deliveryDate;
    }
    public String getNotes(){
        return notes;
    }

    //setters 
    public void setAmount(double amount){
        this.amount=amount;
    }
    public void setDeliveryDate(Timestamp deliveryDate){
        this.deliveryDate=deliveryDate;
    }
    public void setNotes(String notes){
        this.notes=notes;
    }

    //methods 
    public boolean placeOrder(){
        //order creation will be handlled by Order service
        return false;
    }
    public void cancelOrder(){
        this.status=OrderStatus.CANCELLED;
    }
    public void updateStatus(OrderStatus status){
        this.status=status;
    }
    public Order getOrderDetails(){
        return this;
    }
}
