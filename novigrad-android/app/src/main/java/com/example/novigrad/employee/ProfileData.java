package com.example.novigrad.employee;

import android.view.View;

import com.example.novigrad.Helper;

public class ProfileData {

    private String streetNumber, streetName, postalCode, municipality, phone;

    public ProfileData(ProfileEditorActivity activity) {
        this.streetNumber = Helper.getText(activity.streetNumber);
        this.streetName = Helper.getText(activity.streetName);
        this.postalCode = Helper.getText(activity.postalCode);
        this.municipality = Helper.getText(activity.municipality);
        this.phone = Helper.getText(activity.phone);
    }

    public ProfileData(String streetNumber, String streetName, String postalCode, String municipality, String phone) {
        this.streetNumber = streetNumber;
        this.streetName = streetName;
        this.postalCode = postalCode;
        this.municipality = municipality;
        this.phone = phone;
    }

    public boolean isValid(View view) {
        if (!Helper.stringIsValid(streetNumber)) {
            Helper.snackbar(view, "Please enter a valid street number.");
            return false;
        } else if (!Helper.stringIsValid(streetName)) {
            Helper.snackbar(view, "Please enter a valid street name.");
            return false;
        } else if (!postalIsValid(postalCode)) {
            Helper.snackbar(view, "Please enter a valid postal code.");
            return false;
        } else if (!Helper.stringIsValid(municipality)) {
            Helper.snackbar(view, "Please enter a valid municipality.");
            return false;
        } else if (!Helper.stringIsValid(phone)) {
            Helper.snackbar(view , "Please enter a valid phone number");
            return false;
        }

        return true;
    }

    public boolean postalIsValid(String postal) {
        if (Helper.stringIsValid(postal)) {
            if (postal.length() == 6) {
                for (int i = 0; i < 6; i++) {
                    char character = postal.charAt(i);
                    if (i % 2 == 0) {
                        if (Character.isDigit(character)) {
                            return false;
                        }
                    } else {
                        if (Character.isLetter(character)) {
                            return false;
                        }
                    }
                }

                return true;
            }
        }

        return false;
    }

    public boolean phoneIsValid(String phone) {
        if (Helper.stringIsValid(phone)) {
            if (phone.length() == 10) {
                for (int i = 0; i < 10; i++) {
                    char character = phone.charAt(i);
                    if (!Character.isDigit(character)) {
                        return false;
                    }
                }

                return true;
            }
        }

        return false;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public String getStreetName() {
        return streetName;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getMunicipality() {
        return municipality;
    }

    public String getPhone() {
        return phone;
    }
}
