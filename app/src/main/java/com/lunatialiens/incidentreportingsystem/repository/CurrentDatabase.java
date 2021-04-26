package com.lunatialiens.incidentreportingsystem.repository;


import com.lunatialiens.incidentreportingsystem.models.PublicUser;

public class CurrentDatabase {

    private static PublicUser currentPublicUser;

    public static PublicUser getCurrentPublicUser() {
        return currentPublicUser;
    }

    public static void setCurrentPublicUser(PublicUser currentPublicUser) {
        CurrentDatabase.currentPublicUser = currentPublicUser;
    }

}