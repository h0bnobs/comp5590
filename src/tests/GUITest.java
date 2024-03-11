package src.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import src.DBManager;
import src.GUI;

public class GUITest {
    private DBManager dbManager;
    static GUI gui;

    //TODO wallahi we're cooked:
    //Test for empty name input: Ensure that the validation method returns false when an empty string is provided as the name input.
    //Test for name with special characters: Verify that the validation method returns false when the name contains special characters that are not allowed.
    //Test for valid password: Add more test cases to cover different valid password combinations, such as passwords with various lengths, different combinations of uppercase and lowercase letters, numbers, and special characters.
    //Test for empty password input: Ensure that the validation method returns false when an empty string is provided as the password input.
    //Test for password with invalid special characters: Verify that the validation method returns false when the password contains special characters that are not allowed.
    //Test for password with invalid length: Ensure that the validation method returns false when the password length is less than 8 characters or more than 45 characters.
    //Test for valid username: Add test cases to verify that usernames containing valid characters are accepted.
    //Test for invalid username: Ensure that usernames containing invalid characters are rejected.
    //Test for successful login: Create test cases to simulate successful login scenarios, ensuring that the appropriate actions are taken when valid credentials are provided.
    //Test for unsuccessful login: Add test cases to simulate unsuccessful login scenarios, ensuring that the appropriate error messages are displayed when invalid credentials are provided.
    //Test for user signup: Verify that users can successfully sign up and that their information is correctly added to the database.
    //Test for duplicate username during signup: Ensure that users cannot sign up with a username that already exists in the database.

    @Before
    public void setUp() {
        dbManager = new DBManager();
        gui = new GUI();
    }

    /**
     * Tests that a valid name entered passes.
     *
     * @author max
     */
    @Test
    public void validateName1() {
        String validName = "max";
        assertTrue(gui.validateName(validName));
        validName = "josh";
        assertTrue(gui.validateName(validName));
    }

    /**
     * Tests that if a name contains digits, it is not valid.
     *
     * @author max
     */
    @Test
    public void validateName2() {
        String notValid = "123";
        assertFalse(gui.validateName(notValid));
        notValid = "max123josh456";
        assertFalse(gui.validateName(notValid));
    }

    /**
     * Checks to make sure that the user can enter a password that meets all requirements.
     *
     * @author max
     */
    @Test
    public void validatePassword1() {
        String validPassword = "iLoveCake123#";
        assertTrue("Password '" + validPassword + "' should be valid, but something has gone wrong.", gui.validatePassword(validPassword));
        validPassword = "helloWORLD871!#";
        assertTrue("Password '" + validPassword + "' should be valid, but something has gone wrong.", gui.validatePassword(validPassword));
        validPassword = "von-gywFG_HSNU2634578#!#";
        assertTrue("Password '" + validPassword + "' should be valid, but something has gone wrong.", gui.validatePassword(validPassword));
    }

    /**
     * Checks to make sure the user cannot enter a password that's too short.
     *
     * @author max
     */
    @Test
    public void validatePassword2() {
        String tooShort = "123#Ul";
        assertFalse("Password '" + tooShort + "' should be too short, but something has gone wrong.", gui.validatePassword(tooShort));
        String tooLong = "0129857isajhioa7rt927186rgf2i8376f####KJHGSKJDHGIKS";
        assertFalse("Password '" + tooLong + "' should be too long, but something has gone wrong.", gui.validatePassword(tooLong));
    }

    /**
     * Checks to make sure the user cannot enter a password without missing a special character.
     *
     * @author max
     */
    @Test
    public void validatePassword3() {
        String noSpecialChar = "missingSpecialChar123";
        assertFalse("Password '" + noSpecialChar + "' shouldn't have a special character in it, but something has gone wrong.", gui.validatePassword(noSpecialChar));
    }

    /**
     * Checks to make sure the user cannot enter a password without any letters.
     *
     * @author max
     */
    @Test
    public void validatePassword4() {
        String missingUpperAndLower = "12345678#";
        assertFalse("Password '" + missingUpperAndLower + "' shouldn't include any chars, but something has gone wrong.", gui.validatePassword(missingUpperAndLower));
    }

    @Test
    public void validatePassword5() {
        String missingLower = "AKLFGHKLA123#";
        assertFalse("Password '" + missingLower + "' shouldn't include any lower case chars, but something has gone wrong.", gui.validatePassword(missingLower));
    }

    @Test
    public void validatePassword6() {
        String missingUpper = "sdhkk2983kasdb#";
        assertFalse("Password '" + missingUpper + "' shouldn't include any upper case chars, but something has gone wrong.", gui.validatePassword(missingUpper));
    }
}
