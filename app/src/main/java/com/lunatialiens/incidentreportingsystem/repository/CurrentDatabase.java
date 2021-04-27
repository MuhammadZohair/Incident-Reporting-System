package com.lunatialiens.incidentreportingsystem.repository;


import com.lunatialiens.incidentreportingsystem.models.PublicUser;

/**
 * The type Current database.
 */
public class CurrentDatabase {

    private static PublicUser currentPublicUser;

    /**
     * Gets current public user.
     *
     * @return the current public user
     */
    public static PublicUser getCurrentPublicUser() {
        return currentPublicUser;
    }

    /**
     * Sets current public user.
     *
     * @param currentPublicUser the current public user
     */
    public static void setCurrentPublicUser(PublicUser currentPublicUser) {
        CurrentDatabase.currentPublicUser = currentPublicUser;
    }

}