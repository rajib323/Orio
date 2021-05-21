package com.google.orio.Model;

public class User {
    private String id;
    private String username;
    private String pin;
    private String imageURL;
    public User(String id,String username,String pin,String imageURL){
        this.id=id;
        this.username=username;
        this.pin=pin;
        this.imageURL=imageURL;
    }
    public User(){

    }

    public String getId() {
        return id;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getPin() {
        return pin;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
