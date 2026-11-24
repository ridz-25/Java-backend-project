package src.dto;

public class RatingSummaryDTO {

    private double averageRating;
    private int totalReviews;

    private int fiveStarCount;
    private int fourStarCount;
    private int threeStarCount;
    private int twoStarCount;
    private int oneStarCount;

    public RatingSummaryDTO(double averageRating, int totalReviews,
        int fiveStarCount, int fourStarCount,
        int threeStarCount, int twoStarCount,
        int oneStarCount) {

        this.averageRating = averageRating;
        this.totalReviews = totalReviews;
        this.fiveStarCount = fiveStarCount;
        this.fourStarCount = fourStarCount;
        this.threeStarCount = threeStarCount;
        this.twoStarCount = twoStarCount;
        this.oneStarCount = oneStarCount;
    }

    // Getters
    public double getAverageRating() { return averageRating; }
    public int getTotalReviews() { return totalReviews; }
    public int getFiveStarCount() { return fiveStarCount; }
    public int getFourStarCount() { return fourStarCount; }
    public int getThreeStarCount() { return threeStarCount; }
    public int getTwoStarCount() { return twoStarCount; }
    public int getOneStarCount() { return oneStarCount; }
}