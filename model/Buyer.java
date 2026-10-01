package model;

import java.sql.Timestamp;
import java.util.List;

public class Buyer extends User {
    private String address;

    public Buyer(int userId,String name,String email,String password,
        String phone,Timestamp createdAt,boolean active,String address){

            super(userId, name, email, password,
                UserRole.ONE, phone, createdAt, active);
            
            this.address=address;
        }

    //getter
    public String getAddress(){
        return address;
    }

    //setter
    public void setAddress(String address){
        this.address=address;
    }

    //methods
    public Order placeOrder(int gigId){
        //order creation will be done later
        return null;
    }
    public List<Order> viewOrders(){
        //order retrieval will be done later
        return null;
    }
    public boolean addReview(int orderId,int rating,String comment){
        //review functionality will be added later
    }

    @Override 
    public void displayDashboard(){
        System.out.println("Welcome to the buyer dashboard");
    }
}
