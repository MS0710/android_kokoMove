package com.example.kokomove_1;

public class UserInfo {
    private String account;
    private String password;
    private String name;
    private String userStatus;
    public UserInfo(String _account,String _password,String _name,String _userStatus){
        this.account = _account;
        this.password = _password;
        this.name = _name;
        this.userStatus = _userStatus;
    }

    public String getName() {
        return name;
    }

    public String getAccount() {
        return account;
    }

    public String getPassword() {
        return password;
    }

    public String getUserStatus() {
        return userStatus;
    }
}
