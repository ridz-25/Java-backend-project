package src.dto;

public class ProfileMetricsDTO {

    private int totalGigs;
    private int activeGigs;
    private int completedOrders;
    private int totalReviews;
    private double averageRating;
    private double totalEarnings;

    public ProfileMetricsDTO(int totalGigs, int activeGigs,
        int completedOrders, int totalReviews,
        double averageRating, double totalEarnings) {

        this.totalGigs = totalGigs;
        this.activeGigs = activeGigs;
        this.completedOrders = completedOrders;
        this.totalReviews = totalReviews;
        this.averageRating = averageRating;
        this.totalEarnings = totalEarnings;
    }

    // Getters
    public int getTotalGigs() { return totalGigs; }
    public int getActiveGigs() { return activeGigs; }
    public int getCompletedOrders() { return completedOrders; }
    public int getTotalReviews() { return totalReviews; }
    public double getAverageRating() { return averageRating; }
    public double getTotalEarnings() { return totalEarnings; }
}