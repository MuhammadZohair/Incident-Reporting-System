package com.lunatialiens.incidentreportingsystem.models;


import com.lunatialiens.incidentreportingsystem.utils.AppUtils;

import java.io.Serializable;

/**
 * The type User.
 */
public abstract class User implements Serializable {

    private String userId;
    private String name;
    private String emailAddress;
    private String password;

    /**
     * Instantiates a new User.
     */
    public User() {
        userId = AppUtils.generateUUID();
    }

    /**
     * Instantiates a new User.
     *
     * @param name         the name
     * @param emailAddress the email address
     * @param password     the password
     */
    public User(String name, String emailAddress, String password) {
        this.userId = AppUtils.generateUUID();
        this.name = name;
        this.emailAddress = emailAddress;
        this.password = password;
    }

    /**
     * Gets user id.
     *
     * @return the user id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets user id.
     *
     * @param userId the user id
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets email address.
     *
     * @return the email address
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Sets email address.
     *
     * @param emailAddress the email address
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
     * Gets password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets password.
     *
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                ", userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
