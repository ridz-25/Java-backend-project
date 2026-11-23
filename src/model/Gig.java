package src.model;

import java.sql.Time;
import java.sql.Timestamp;

public class Gig {
    private int gigId;
    private String title;
    private String description;
    private int sellerId;
    private int categoryId;
    private double price;
    private String deliveryTime;
    private GigStatus status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private double avgRating;
    private int reviewCount;

    public Gig(int gigId,String title,String description,
        int sellerId,int categoryId,double price,
        String deliveryTime,GigStatus status,Timestamp createdAt,
        Timestamp updatedAt,double avgRating,int reviewCount){
            this.gigId=gigId;
            this.title=title;
            this.description=description;
            this.sellerId=sellerId;
            this.categoryId=categoryId;
            this.price=price;
            this.deliveryTime=deliveryTime;
            this.status=status;
            this.createdAt=createdAt;
            this.updatedAt=updatedAt;
            this.avgRating=avgRating;
            this.reviewCount=reviewCount;
        }
    
    //getters
    public int getGigId(){
        return gigId;
    }
    public String getTitle(){
        return title;
    }
    public String getDescription(){
        return description;
    }
    public int getSellerId(){
        return sellerId;
    }
    public int getCategoryId(){
        return categoryId;
    }
    public double getPrice(){
        return price;
    }
    public String getDeliveryTime(){
        return deliveryTime;
    }
    public GigStatus getStatus(){
        return status;
    }
    public Timestamp getCreatedAt(){
        return createdAt;
    }
    public Timestamp getUpdatedAt(){
        return updatedAt;
    }
    public double getAvgRating(){
        return avgRating;
    }
    public int getReviewCount(){
        return reviewCount;
    }

    //setters
    public void setTitle(String title){
        this.title=title;
    }
    public void setDescription(String description){
        this.description=description;
    }
    public void setPrice(double price){
        this.price=price;
    }
    public void setDeliveryTime(String deliveryTime){
        this.deliveryTime=deliveryTime;
    }
    public void setCategoryId(int categoryId){
        this.categoryId=categoryId;
    }
    public void setUpdatedAt(Timestamp updatedAt){
        this.updatedAt=updatedAt;
    }

    //Methods
    public void updateGig(){
        this.updatedAt=new Timestamp(System.currentTimeMillis());
    }
    public void deleteGig(){
        this.status=GigStatus.DELETED;
        this.updatedAt=new Timestamp(System.currentTimeMillis());
    }
    public void activate(){
        this.status=GigStatus.ACTIVE;
        this.updatedAt=new Timestamp(System.currentTimeMillis());
    }
    public void deactivate(){
        this.status =GigStatus.INACTIVE;
        this.updatedAt=new Timestamp(System.currentTimeMillis());
    }
    public double getAverageRating(){
        return avgRating;
    }
    public boolean isAvailable(){
        return status==GigStatus.ACTIVE;
    }
    public void updateRating(double avgRating, int reviewCount) {
        this.avgRating = avgRating;
        this.reviewCount = reviewCount;
    }
}