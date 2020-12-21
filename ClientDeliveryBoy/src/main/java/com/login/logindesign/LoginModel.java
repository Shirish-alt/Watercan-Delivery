package com.login.logindesign;

public class LoginModel {
    String username,password,mobile,address,UserType;

    public LoginModel(){}

    public LoginModel(String username, String password, String mobile, String address, String userType) {
        this.username = username;
        this.password = password;
        this.mobile = mobile;
        this.address = address;
        UserType = userType;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getMobile() {
        return mobile;
    }

    public String getAddress() {
        return address;
    }

    public String getUserType() {
        return UserType;
    }
}
