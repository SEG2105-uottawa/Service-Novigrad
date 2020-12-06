package com.example.novigrad.validation;


import android.os.Build;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.example.novigrad.Helper;
import com.example.novigrad.customer.BranchActivity;

import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServiceRequestData {
    private String firstName, lastName, streetName, license, streetNum;
    private int year, day, month;
    private boolean driversLicenseReq, photoIDReq, healthCardReq;
    private boolean residenceAdded, photoIDAdded, citizenAdded;

    public ServiceRequestData(BranchActivity activity){
        this.firstName = Helper.getText(activity.firstName);
        this.lastName = Helper.getText(activity.lastName);
        this.license = Helper.getText(activity.license);
        this.streetName = Helper.getText(activity.streetName);
        this.streetNum = Helper.getText(activity.streetNum);
        this.year = activity.dateOfBirth.getYear();
        this.month = activity.dateOfBirth.getMonth();
        this.day = activity.dateOfBirth.getDayOfMonth();
        this.driversLicenseReq = activity.isRequiredDriversLicense();
        this.photoIDReq = activity.isRequiredPhotoID();
        this.healthCardReq = activity.isRequiredHealthCard();
        this.photoIDAdded = activity.isPhotoIDDocAdded;
        this.citizenAdded = activity.isCitizenshipDocAdded;
        this.residenceAdded = activity.isResidenceDocAdded;
    }


    public boolean isValid(View view){

        // First name validation
        if (!Helper.stringIsValid(firstName)) {
            Helper.snackbar(view, "Invalid first name");
            return false;
        }
        if (!nameIsValid(firstName)) {
            Helper.snackbar(view, "Invalid characters in first name");
            return false;
        }

        // Last name validation
        if (!Helper.stringIsValid(lastName)) {
            Helper.snackbar(view, "Invalid last name");
            return false;
        }
        if (!nameIsValid(lastName)){
            Helper.snackbar(view, "Invalid characters in last name");
            return false;
        }


        // Address validation
        if (!Helper.stringIsValid(streetNum)) {
            Helper.snackbar(view, "Invalid Street Number");
            return false;
        }
        if (!Helper.stringIsValid(streetName)) {
            Helper.snackbar(view, "Invalid Street Name");
            return false;
        }

        // Date of Birth Validation
        if (!dateOfBirthIsValid()) {
            Helper.snackbar(view, "Please input a valid date of birth");
            return false;
        }

        // License Validation
        if (!Helper.stringIsValid(license) && driversLicenseReq && !licenseIsValid()){
            Helper.snackbar(view, "Invalid License Type");
            return false;
        }

        // Check if documents are present
        if (!residenceAdded) {
            Helper.snackbar(view, "Please add image of proof of residence");
            return false;
        }

        if (photoIDReq && !photoIDAdded) {
            Helper.snackbar(view, "Please add image of yourself");
            return false;
        }

        if (healthCardReq && !citizenAdded) {
            Helper.snackbar(view, "Please add image of proof of status");
            return false;
        }
        return true;
    }

    private boolean nameIsValid(String name) {
        /* Name is valid - letters only */
        Pattern allowedCharacters = Pattern.compile("[a-zA-z]+");
        Matcher nameAsMatcher = allowedCharacters.matcher(name);
        return nameAsMatcher.matches();
    }

    private boolean licenseIsValid(){
        String[] licenseTypes = {"G1", "G2", "G"};

        for (String type :licenseTypes) {
            if (license.equalsIgnoreCase(type)) {
                return true;
            }
        }
        return false;
    }

    private boolean dateOfBirthIsValid(){
        return Period.between(LocalDate.of(year, month+1, day), LocalDate.now()).getYears() > 0;
    }

}
