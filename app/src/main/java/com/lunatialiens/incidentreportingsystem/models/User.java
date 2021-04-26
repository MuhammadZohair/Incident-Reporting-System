package com.lunatialiens.incidentreportingsystem.models;


import com.lunatialiens.incidentreportingsystem.utils.AppUtils;

import java.io.Serializable;

public abstract class User implements Serializable {

    private String userId;
    private String name;
    private String emailAddress;
    private String password;
    private String phoneNumber;

    public User() {
        userId = AppUtils.generateUUID();
    }

    public User(String name, String emailAddress, String password, String phoneNumber) {
        this.userId = AppUtils.generateUUID();
        this.name = name;
        this.emailAddress = emailAddress;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "User{" +
                ", userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
