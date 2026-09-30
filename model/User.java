package model;

import java.sql.Timestamp;

public abstract class User {
    private int userId;
    private String name;
    private String email;
    private String password;
    private UserRole role;
    private String phone;
    private Timestamp createdAt;
    private boolean active;

    //constructor

    User(int userId,String name,String email,String password,
        UserRole role,String phone,Timestamp createdAt,boolean active){
            this.userId=userId;
            this.name=name;
            this.email=email;
            this.password=password;
            this.role=role;
            this.phone=phone;
            this.createdAt=createdAt;
            this.active=active;
        }

    //getters

    public int getUserId(){
        return userId;
    }
    public String getName(){
        return name;
    }
    public String getEmail(){
        return email;
    }
    public String getPassword(){
        return password;
    }
    public UserRole getRole(){
        return role;
    }
    public String getPhone(){
        return phone;
    }
    public Timestamp getCreatedAt(){
        return createdAt;
    }
    public boolean isActive(){
        return active;
    }

    //setters

    public void setName(String name){
        this.name=name;
    }
    public void setEmail(String email){
        this.email=email;
    }
    public void setPassword(String password){
        this.password=password;
    }
    public void setPhone(String phone){
        this.phone=phone;
    }
    public void setActive(boolean active){
        this.active=active;
    }

    //methods

    public boolean login(String email,String password){
        return this.email.equals(email) && this.password.equals(password) && this.active;
    }
    public void logout(){
        System.out.println("User logged out successfully");
    }
    public void updateProfile(){
        System.out.println("Profile updated successfully");
    }
    public void changePassword(String newPassword){
        this.password=newPassword;
        System.out.println("Password changed successfully");
    }

    //abstract method displayDashboard
    public abstract void displayDashboard();
}
