package com.example.novigrad;

import org.junit.Test;
import static org.junit.Assert.*;

public class LoginDataUnitTest {
    @Test
    public void isValid_emailNull_false() {
        assertFalse(new LoginData(null, "password").isValid());
    }

    @Test
    public void isValid_passwordNull_false() {
        assertFalse(new LoginData("email", null).isValid());
    }

    @Test
    public void isValid_bothNull_false() {
        assertFalse(new LoginData(null, null).isValid());
    }

    @Test
    public void isValid_bothNonNull_true() {
        assertTrue(new LoginData("email", "password").isValid());
    }

    @Test
    public void isAdmin_false() {
        assertFalse(new LoginData("email", "password").isAdmin());
    }

    @Test
    public void isAdmin_true() {
        assertTrue(new LoginData("admin", "admin").isAdmin());
    }

}