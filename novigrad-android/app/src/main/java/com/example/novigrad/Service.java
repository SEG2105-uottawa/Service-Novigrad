package com.example.novigrad;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.StringJoiner;

public class Service {
    public static String COLLECTION = "available_services";
    public static String NAME_KEY = "name";
    public static String PRICE_KEY = "price";
    public static String DRIVERS_LICENSE_KEY = "driversLicense";
    public static String PHOTO_ID_KEY = "photoID";
    public static String HEALTH_CARD_KEY = "healthCard";

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
        this.name = (String) userDocument.get(NAME_KEY);
        this.price = (double) userDocument.get(PRICE_KEY);
        this.driversLicenseRequired = (boolean) userDocument.get(DRIVERS_LICENSE_KEY);
        this.healthCardRequired = (boolean) userDocument.get(HEALTH_CARD_KEY);
        this.photoIDRequired = (boolean) userDocument.get(PHOTO_ID_KEY);
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

    public boolean getDriversLicenseRequired() {
        return driversLicenseRequired;
    }

    public void setDriversLicenseRequired(boolean driversLicenseRequired) {
        this.driversLicenseRequired = driversLicenseRequired;
    }

    public boolean getHealthCardRequired() {
        return healthCardRequired;
    }

    public void setHealthCardRequired(boolean healthCardRequired) {
        this.healthCardRequired = healthCardRequired;
    }

    public boolean getPhotoIDRequired() {
        return photoIDRequired;
    }

    public void setPhotoIDRequired(boolean photoIDRequired) {
        this.photoIDRequired = photoIDRequired;
    }

    public String getRequiredDocumentsString () {
        StringJoiner required = new StringJoiner(", ");
        if (getPhotoIDRequired()) {
            required.add("Photo ID");
        }

        if (getDriversLicenseRequired()) {
            required.add("Driver's License");
        }

        if (getHealthCardRequired()) {
            required.add("Health Card");
        }
        return required.toString();
    }
}
