package com.example.novigrad;

import com.example.novigrad.validation.ServiceRequestData;

import org.junit.Test;
import static org.junit.Assert.*;

public class ServiceRequestDataUnitTest {
    String validName = "validName";
    String invalidName = "1nvalidnam3";
    String validLicense = "G";
    String invalidLicense = "H";

    int validYear = 2018;
    int validMonth = 11;
    int validDay = 23;

    //subject to change
    int invalidYear = 2021;

    @Test
    public void isValid() {
        assertTrue(new ServiceRequestData(validName, validName, validName, validLicense, validName, validYear, validDay, validMonth, false, false, false, true, false,false).isValid(null));
    }

    @Test
    public void isValid2() {
        assertTrue(new ServiceRequestData(validName, validName, validName, validLicense, validName, validYear, validDay, validMonth, true, true, true, true, true,true).isValid(null));
    }

    @Test
    public void nameIsInvalid() {
        assertFalse(new ServiceRequestData(invalidName, invalidName, validName, validLicense, validName, validYear, validDay, validMonth, false, false, false, false, false,false).isValid(null));
    }

    @Test
    public void licenseIsInvalid() {
        assertFalse(new ServiceRequestData(validName, validName, validName, invalidLicense, validName, validYear, validDay, validMonth, false, false, false, false, false,false).isValid(null));
    }

    @Test
    public void dobIsInvalid() {
        assertFalse(new ServiceRequestData(validName, validName, validName, validLicense, validName, invalidYear, validDay, validMonth, false, false, false, false, false,false).isValid(null));
    }

    @Test
    public void reqIsInvalid() {
        assertFalse(new ServiceRequestData(validName, validName, validName, validLicense, validName, validYear, validDay, validMonth, true, false, false, false, false,false).isValid(null));
    }

    @Test
    public void nameAndLicenseIsInvalid() {
        assertFalse(new ServiceRequestData(invalidName, invalidName, validName, invalidLicense, validName, validYear, validDay, validMonth, false, false, false, false, false,false).isValid(null));
    }

    @Test
    public void dobAndReqIsInvalid() {
        assertFalse(new ServiceRequestData(validName, validName, validName, validLicense, validName, invalidYear, validDay, validMonth, true, true, true, true, false,false).isValid(null));
    }

    @Test
    public void nameAndDobIsInvalid() {
        assertFalse(new ServiceRequestData(invalidName, invalidName, validName, validLicense, validName, invalidYear, validDay, validMonth, false, false, false, false, false,false).isValid(null));
    }

    @Test
    public void licenseAndDobIsInvalid() {
        assertFalse(new ServiceRequestData(validName, validName, validName, invalidLicense, validName, invalidYear, validDay, validMonth, false, false, false, false, false,false).isValid(null));
    }
}