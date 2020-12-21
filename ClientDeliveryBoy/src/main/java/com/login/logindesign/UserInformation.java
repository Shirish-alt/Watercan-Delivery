package com.login.logindesign;

public class UserInformation {
    private String id;
    private String name;
    private String Username;
    private String Mobile;
    private String Address;

    public UserInformation(String id, String name, String username, String mobile, String address) {
        this.id = id;
        this.name = name;
        Username = username;
        Mobile = mobile;
        Address = address;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return Username;
    }

    public String getMobile() {
        return Mobile;
    }

    public String getAddress() {
        return Address;
    }
}
