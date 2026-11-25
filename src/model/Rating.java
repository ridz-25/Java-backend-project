package src.model;

public class Rating {
    private int ratingId;
    private int gigId;
    private double averageRating;
    private int totalReviews;

    public Rating(int ratingId,int gigId,
        double averageRating,int totalReviews){
            this.ratingId=ratingId;
            this.gigId=gigId;
            this.averageRating=averageRating;
            this.totalReviews=totalReviews;
        }

    //getters
    public int getRatingId(){
        return ratingId;
    }
    public int getGigId(){
        return gigId;
    }
    public double getAverageRating(){
        return averageRating;
    }
    public int getTotalReiews(){
        return totalReviews;
    }

    //setters
    public void setAverageRating(double averageRating){
        this.averageRating=averageRating;
    }
    public void setTotalReviews(int totalReviews){
        this.totalReviews=totalReviews;
    }

    //methods
    public double calculateAverage(){
        return averageRating;
    }
}
