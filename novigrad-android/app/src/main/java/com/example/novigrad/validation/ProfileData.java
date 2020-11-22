package com.example.novigrad.validation;

import android.view.View;

import com.example.novigrad.Helper;
import com.example.novigrad.employee.ProfileEditorActivity;

import java.util.ArrayList;
import java.util.Map;

public class ProfileData {
    public String streetNumber, streetName, postalCode, municipality, phone;
    public int fromTime, toTime;
    public ArrayList<String> days;

    public ProfileData(ProfileEditorActivity activity) {
        this.streetNumber = Helper.getText(activity.streetNumber);
        this.streetName = Helper.getText(activity.streetName);
        this.postalCode = Helper.getText(activity.postalCode);
        this.municipality = Helper.getText(activity.municipality);
        this.phone = Helper.getText(activity.phone);
        this.fromTime = activity.fromTimeListener.getTime();
        this.toTime = activity.toTimeListener.getTime();
        this.days = activity.getCheckedDays();
    }

    public static ProfileData createProfileDataFromFirebase(Object subdocument) {
        if (subdocument == null) {
            return null;
        } else {
            Map<String, Object> map = (Map<String, Object>) subdocument;
            return new ProfileData(map);
        }
    }
    private ProfileData(Map<String, Object> map) {
        this.streetNumber = (String) map.get("streetNumber");
        this.streetName = (String) map.get("streetName");
        this.postalCode = (String) map.get("postalCode");
        this.municipality = (String) map.get("municipality");
        this.phone = (String) map.get("phone");
        this.fromTime = (int) (long) map.get("fromTime");
        this.toTime = (int) (long) map.get("toTime");
        this.days = (ArrayList<String>) map.get("days");
    }

    public ProfileData(String streetNumber, String streetName, String postalCode, String municipality, String phone, int fromTime, int toTime, ArrayList<String> days) {
        this.streetNumber = streetNumber;
        this.streetName = streetName;
        this.postalCode = postalCode;
        this.municipality = municipality;
        this.phone = phone;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.days = days;
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
