package com.example.novigrad;

import com.example.novigrad.validation.ProfileData;

import org.junit.Test;
import static org.junit.Assert.*;

public class ProfileDataUnitTest {
    String validPostal = "A1B2C3";
    String invalidPostal1 = "A1B23C";
    String invalidPostal2 = "A1B2C";

    String validPhone = "1234567890";
    String invalidPhone1 = "123456789";
    String invalidPhone2 = "ABCDEFGHIJ";

    ProfileData nullData = new ProfileData(null, null, null, null, null);

    @Test
    public void postal() {
        assertTrue(nullData.postalIsValid(validPostal));
        assertFalse(nullData.postalIsValid(invalidPostal1));
        assertFalse(nullData.postalIsValid(invalidPostal2));
    }

    @Test
    public void phone() {
        assertTrue(nullData.phoneIsValid(validPhone));
        assertFalse(nullData.phoneIsValid(invalidPhone1));
        assertFalse(nullData.phoneIsValid(invalidPhone2));
    }
}
