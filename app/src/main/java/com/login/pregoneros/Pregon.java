package com.login.pregoneros;

public class Pregon {

    private String companyName;
    private String pregonCode;
    private String endDate;
    private String object;
    private String id_pregon;
    private String id_campaign;
    private String id_client;

    public Pregon(String companyName, String pregonCode, String endDate, String object, String id_pregon, String id_campaign, String id_client) {
        this.companyName = companyName;
        this.pregonCode = pregonCode;
        this.endDate = endDate;
        this.object = object;
        this.id_pregon = id_pregon;
        this.id_campaign = id_campaign;
        this.id_client = id_client;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getPregonCode() {
        return pregonCode;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getObject() {
        return object;
    }

    public String getId_pregon() {
        return id_pregon;
    }

    public String getId_campaign() {
        return id_campaign;
    }

    public String getId_client() {
        return id_client;
    }
}
