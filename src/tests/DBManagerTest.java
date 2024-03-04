package src.tests;

import static org.junit.Assert.*;

import org.junit.Test;
import src.DBManager;

public class DBManagerTest {
    DBManager dbManager = new DBManager();

    /**
     * Tests if a username and password are in the db
     * Note: THIS WILL FAIL IF THERE IS NOT A USER IN THE DB WITH THE USERNAME "validU" AND THE PASSWORD "validP"
     * @author max
     */
    @Test
    public void testUsernameAndPasswordExists() {
        String someValidUsername = "validU";
        String someValidPassword = "validP";
        assertTrue(dbManager.isUserPresent(someValidUsername, someValidPassword));
    }

    /**
     * Tests if a username and password are not the db
     * Note: THIS WILL FAIL IF THERE IS A USER IN THE DB WITH THE USERNAME "notValidU" AND THE PASSWORD "notValidP"
     * @author max
     */
    @Test
    public void  testUserLoginInvalidCredentials() {
        String notValidU = "notValidU";
        String notValidP = "notValidP";
        assertFalse(dbManager.isUserPresent(notValidU, notValidP));
    }

    /**
     * Tests if a username already exists in the database. Removes the added user after.
     * @author max
     */
    @Test
    public void testisUsernameDuplicate() {
        dbManager.addPatient("temp", "temp", "tempName", "tempAddress");
        String user = "temp";
        assertTrue(dbManager.isUsernameDuplicate(user));
        dbManager.removePatient(String.valueOf(dbManager.getNextPID() - 1), user);
    }

    /**
     * Tests if a username doesn't exist in the database. Removes the added user after.
     * @author max
     * NOTE: WILL FAIL IF THERE IS SOMEONE IN THE DATABASE WITH THE USERNAME TEMP.
     */
    @Test
    public void testIsUsernameNotDuplicate() {
        String user = "notTemp";
        assertFalse(dbManager.isUsernameDuplicate(user));
    }
}
