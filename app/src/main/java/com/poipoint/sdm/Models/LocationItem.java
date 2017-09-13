package com.poipoint.sdm.Models;

/**
 * Created by Tanmay on 5/30/2016.
 */
public class LocationItem {

    private int  id;
    private String city;
    private String street;
    private double latitude;
    private double longitude;
    private String description;
    private String subCatId;

    public LocationItem(int id, String city, String street, double latitude, double longitude, String description, String subCatId) {
        this.id = id;
        this.city = city;
        this.street = street;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.subCatId=subCatId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSubCatId() {
        return subCatId;
    }

    public void setSubCatId(String subCatId) {
        this.subCatId = subCatId;
    }
}
