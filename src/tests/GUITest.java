package src.tests;

import org.junit.Before;
import org.junit.Test;
import src.Database.DatabaseInteract;
import src.UI.GUI;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GUITest {
    private DatabaseInteract dbManager;
    static GUI gui;

    @Before
    public void setUp() {
        dbManager = new DatabaseInteract();
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

    /**
     * Tests a valid login using valid username and password
     *
     * @author Joshwa
     */
    @Test
    public void testSuccessfulLogin() {

        dbManager.addPatient("GregoryHouse", "HouseMD@Season20", "Gregory House", "CrippleSt");
        String validUsername = "GregoryHouse";
        String validPassword = "HouseMD@Season20";

        boolean loginResult = dbManager.isUserPresent(validUsername, validPassword);

        assertTrue("True", loginResult);
        dbManager.removePatient((String) dbManager.getUserInfo("GregoryHouse", "HouseMD@Season20").get("pid"), "GregoryHouse");
    }

    /**
     * Empty Password Test
     *
     * @author Joshwa
     */
    @Test
    public void validatePassword7() {
        String emptyPass = "";
        assertFalse("Password '" + emptyPass + "' shouln't be empty, but something has gone wrong.", gui.validatePassword(emptyPass));
    }


}
