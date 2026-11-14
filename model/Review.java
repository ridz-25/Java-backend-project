package model;

import java.sql.Timestamp;

public class Review {
    private int reviewId;
    private int orderId;
    private int gigId;
    private int reviewerId;
    private int rating;
    private String comment;
    private Timestamp createdAt;

    Review(int reviewId,int orderId,int gigId,
        int reviewerId,int rating,String comment,
        Timestamp createdAt){
            this.reviewId=reviewId;
            this.orderId=orderId;
            this.gigId=gigId;
            this.reviewerId=reviewerId;
            this.rating=rating;
            this.comment=comment;
            this.createdAt=createdAt;
        }
    
    //getters
    public int getReviewId(){
        return reviewId;
    }
    public int getOrderId(){
        return orderId;
    }
    public int getGigId(){
        return gigId;
    }
    public int getReviewerId(){
        return reviewerId;
    }
    public int getRating(){
        return rating;
    }
    public String getComment(){
        return comment;
    }
    public Timestamp getCreatedAt(){
        return createdAt;
    }

    //setters
    public void setRating(int rating){
        this.rating=rating;
    }
    public void setComment(String comment){
        this.comment=comment;
    }

    //methods
    public void addReview(){
        //review will be added through database insertion by review DAO 
        System.out.println("Review is ready to be saved");
    }
    public void updateReview(){
        //review will be updated through database update by review DAO 
        System.out.println("Review updated");
    }
    public void deleteReview(){
        //review will be deleted through database deletion by review DAO 
        System.out.println("Review deleted");
    }
    public boolean isValidRating(){
        return rating>=1 && rating<=5;
    }
}
