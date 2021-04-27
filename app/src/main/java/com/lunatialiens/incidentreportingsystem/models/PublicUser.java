package com.lunatialiens.incidentreportingsystem.models;

import java.io.Serializable;

/**
 * The type Public user.
 */
public class PublicUser extends User implements Serializable {

    private int incidentsReported;

    /**
     * Instantiates a new Public user.
     */
    public PublicUser() {
    }

    /**
     * Gets incidents reported.
     *
     * @return the incidents reported
     */
    public int getIncidentsReported() {
        return incidentsReported;
    }

    /**
     * Sets incidents reported.
     *
     * @param incidentsReported the incidents reported
     */
    public void setIncidentsReported(int incidentsReported) {
        this.incidentsReported = incidentsReported;
    }
}
