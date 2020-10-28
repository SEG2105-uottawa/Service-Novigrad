package com.example.novigrad;

import com.google.firebase.firestore.DocumentSnapshot;

public class Service {
    private String id;
    private String name;
    private double price;
    private boolean driversLicenseRequired;
    private boolean healthCardRequired;
    private boolean photoIDRequired;

    Service(String id, String name, float price, boolean driversLicenseRequired, boolean healthCardRequired, boolean photoIDRequired) {
        this.name = name;
        this.price = price;
        this.driversLicenseRequired = driversLicenseRequired;
        this.healthCardRequired = healthCardRequired;
        this.photoIDRequired = photoIDRequired;
    }

    public Service(DocumentSnapshot userDocument) {
        /* Create a user from a firebase document (their toDocument method wasn't working) */
        this.id = userDocument.getId();
        this.name = (String) userDocument.get("name");
        this.price = (double) userDocument.get("price");
        this.driversLicenseRequired = (boolean) userDocument.get("driversLicense");
        this.healthCardRequired = (boolean) userDocument.get("healthCard");
        this.photoIDRequired = (boolean) userDocument.get("photoID");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isDriversLicenseRequired() {
        return driversLicenseRequired;
    }

    public void setDriversLicenseRequired(boolean driversLicenseRequired) {
        this.driversLicenseRequired = driversLicenseRequired;
    }

    public boolean isHealthCardRequired() {
        return healthCardRequired;
    }

    public void setHealthCardRequired(boolean healthCardRequired) {
        this.healthCardRequired = healthCardRequired;
    }

    public boolean isPhotoIDRequired() {
        return photoIDRequired;
    }

    public void setPhotoIDRequired(boolean photoIDRequired) {
        this.photoIDRequired = photoIDRequired;
    }
}
