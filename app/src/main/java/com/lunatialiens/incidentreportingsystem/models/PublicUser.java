package com.lunatialiens.incidentreportingsystem.models;

import java.io.Serializable;

public class PublicUser extends User implements Serializable {

    private int incidentsReported;

    public PublicUser() {
    }

    public int getIncidentsReported() {
        return incidentsReported;
    }

    public void setIncidentsReported(int incidentsReported) {
        this.incidentsReported = incidentsReported;
    }
}
