package model;

import java.sql.Timestamp;
import java.util.List;

public class Seller extends User{
    private String bio;
    private String skills;
    private String experience;
    private boolean isVerified;

    Seller(int userId,String name,String email,
        String password,String phone,Timestamp createdAt,
        boolean active,String bio,String skills,String experience,boolean isVerified){
            
            super(userId, name, email, password, UserRole.TWO, phone, createdAt, active);
            
            this.bio=bio;
            this.skills=skills;
            this.experience=experience;
            this.isVerified=isVerified;
        }

    //getter
    public String getBio(){
        return bio;
    }
    public String getSkills(){
        return skills;
    }
    public String getExperience(){
        return experience;
    }
    public boolean isVerified(){
        return isVerified;
    }

    //setter
    public void setBio(String bio){
        this.bio=bio;
    }
    public void setSkills(String skills){
        this.skills=skills;
    }
    public void setExperience(String experience){
        this.experience=experience;
    }
    public void setVerified(boolean isVerified){
        this.isVerified=isVerified;
    }

    //methods
    public boolean createGig(Gig gig){
        //gig creation will be implemented later
        return false;
    }
    public boolean updateGig(Gig gig){
        //gigupdation will be implemented later
        return false;
    }
    public boolean deleteGig(Gig gig){
        //gig deletion will be implemented later
        return false;
    }
    public List<Gig> viewMyGig(){
        //gig retrieval will be implemented later
        return null;
    }
    public List<Order> viewOrders(){
        //order retrieval will be implemted later
        return null;
    }

    @Override 
    public void updateProfile(){
        //seller-specific profile update will be implemented later
    }

    public double calculateEarnings(){
        //earning calculation will be implemrted later
        return 0.0;
    }

    @Override 
    public void displayDashboard(){
        System.out.println("Welcome to the seller dashboard");
    }
}