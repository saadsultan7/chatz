package com.example.chatz.Models;
public class Users {
     public String profilepic,username,email,password,userid,lastMessage;

    public Users(String profilepic, String username, String email, String password, String userid, String lastMessage) {
        this.profilepic = profilepic;
        this.username = username;
        this.email = email;
        this.password = password;
        this.userid = userid;
        this.lastMessage = lastMessage;
    }

    public Users() {}

    //signup constructor
    public Users(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
    public Users(String username, String userid , String profilepic,String email) {
        this.username = username;
        this.userid = userid;
        this.profilepic = profilepic;
    }
    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
