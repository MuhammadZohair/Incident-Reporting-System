package com.lunatialiens.incidentreportingsystem.models;

import com.lunatialiens.incidentreportingsystem.utils.AppUtils;

/**
 * The type Incident.
 */
public class Incident {

    private String incidentId;
    private String userId;
    private String location;
    private String desc;
    private Long timestamp;

    /**
     * Instantiates a new Incident.
     */
    public Incident() {
        incidentId = AppUtils.generateUUID();
    }

    /**
     * Gets incident id.
     *
     * @return the incident id
     */
    public String getIncidentId() {
        return incidentId;
    }

    /**
     * Sets incident id.
     *
     * @param incidentId the incident id
     */
    public void setIncidentId(String incidentId) {
        this.incidentId = incidentId;
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
     * Gets location.
     *
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets location.
     *
     * @param location the location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Gets desc.
     *
     * @return the desc
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Sets desc.
     *
     * @param desc the desc
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * Gets timestamp.
     *
     * @return the timestamp
     */
    public Long getTimestamp() {
        return timestamp;
    }

    /**
     * Sets timestamp.
     *
     * @param timestamp the timestamp
     */
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Incident{" +
                "incidentId='" + incidentId + '\'' +
                ", userId='" + userId + '\'' +
                ", location='" + location + '\'' +
                ", desc='" + desc + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
