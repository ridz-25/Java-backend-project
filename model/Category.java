package model;

import java.util.List;

public class Category {
    private int categoryId;
    private String name;
    private String description;

    Category(int categoryId,String name,String description){
        this.categoryId=categoryId;
        this.name=name;
        this.description=description;
    }

    //getters
    public int getCategoryId(){
        return categoryId;
    }
    public String getName(){
        return name;
    }
    public String getDescription(){
        return description;
    }

    //setters
    public void setCategoryId(int categoryId){
        this.categoryId=categoryId;
    }
    public void setName(String name){
        this.name=name;
    }
    public void setDescription(String description){
        this.description=description;
    }

    //methods
    public List<Gig> getGig(){
        //gig retrieval will be imlemented later
        return null;
    }
    public void updateCategoryName(String name,String description){
        this.name=name;
        this.description=description;
    }
    public Category getCategoryById(int id){
        //database lookup will be implemented later
        return null;
    }
}
