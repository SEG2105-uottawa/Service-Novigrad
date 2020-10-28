package com.example.novigrad;

import org.junit.Test;
import static org.junit.Assert.*;

public class RegisterDataUnitTest {
    public static String validName = "validname";
    public static String validEmail = "test@test.com";
    public static String validPassword = "password";
    public static String validRole1 = "Employee";
    public static String validRole2 = "Customer";

    public static String invalidName1 = "invalid name";
    public static String invalidName2 = "invalidname2";
    public static String invalidName3 = "invalid.name";

    public static String invalidPassword = "123"; // too short

    @Test
    public void valid() {
        assertTrue(new RegisterData(validName, validName, validEmail, validPassword, validPassword, validRole1).isValid(null));
        assertTrue(new RegisterData(validName, validName, validEmail, validPassword, validPassword, validRole2).isValid(null));
    }

    @Test
    public void name() {
        assertFalse(new RegisterData(invalidName1, validName, validEmail, validPassword, validPassword, validRole1).isValid(null));
        assertFalse(new RegisterData(invalidName2, validName, validEmail, validPassword, validPassword, validRole1).isValid(null));
        assertFalse(new RegisterData(invalidName3, validName, validEmail, validPassword, validPassword, validRole1).isValid(null));

        assertFalse(new RegisterData(validName, invalidName1, validEmail, invalidPassword, invalidPassword, validRole1).isValid(null));
        assertFalse(new RegisterData(validName, invalidName2, validEmail, invalidPassword, invalidPassword, validRole1).isValid(null));
        assertFalse(new RegisterData(validName, invalidName3, validEmail, invalidPassword, invalidPassword, validRole1).isValid(null));
    }

    @Test
    public void password() {
        assertFalse(new RegisterData(validName, validName, validEmail, invalidPassword, invalidPassword, validRole1).isValid(null));
        assertFalse(new RegisterData(validName, validName, validEmail, "notmatching", "matching", validRole1).isValid(null));
    }

    @Test
    public void roles() {
        assertFalse(new RegisterData(validName, validName, validEmail, validPassword, validPassword, "Invalid Role").isValid(null));
        assertTrue(new RegisterData(validName, validName, validEmail, validPassword, validPassword, "Customer").isValid(null));
        assertTrue(new RegisterData(validName, validName, validEmail, validPassword, validPassword, "Employee").isValid(null));
    }

}
