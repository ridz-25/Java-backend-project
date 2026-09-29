package model;

enum UserRole {
    ONE("BUYER"),
    TWO("SELLER");

    private final String value;
    UserRole(String val){
        this.value=val;
    }

    String getValue(){
        return this.value;
    }
}
