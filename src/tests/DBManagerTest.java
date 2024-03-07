package src.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import src.DBManager;

import java.util.HashMap;

public class DBManagerTest {
    private DBManager dbManager;

    /**
     * TODO
     * updateAssignedDoctorId(String patientId, String selectedDoctorName)
     * getNextDID()
     * getAllDoctors()
     * getAllDoctorNames()
     */

    @Before
    public void setUp() {
        dbManager = new DBManager();
    }

    /**
     * Tests that isUsernameDuplicate correctly finds a username in the db
     *
     * @author max
     */
    @Test
    public void testisUsernameDuplicate() {
        dbManager.addPatient("someUser", "somePass", "Jack Reacher", "Maine");
        assertTrue(dbManager.isUsernameDuplicate("someUser"));
        assertFalse(dbManager.isUsernameDuplicate("notSomeUser"));
    }

    /**
     * Tests that getUserInfo returns correct data.
     *
     * @author max
     */
    @Test
    public void testGetUserInfo1() {
        dbManager.addPatient("someUser", "somePass", "Jack Reacher", "Maine");
        HashMap<String, Object> user = dbManager.getUserInfo("someUser", "somePass");
        assertEquals(user.get("username"), "someUser");
        assertEquals(user.get("password"), "somePass");
        assertEquals(user.get("name"), "Jack Reacher");
        assertEquals(user.get("address"), "Maine");
        dbManager.removePatient((String) user.get("pid"), "someUser");
    }

    /**
     * Tests that getUserInfo returns correct data.
     *
     * @author max
     */
    @Test
    public void testGetUserInfo2() {
        dbManager.addPatient("someUser", "somePass", "Jack Reacher", "Maine");
        HashMap<String, Object> user = dbManager.getUserInfo("someUser", "somePass");
        assertNotEquals(user.get("username"), "not");
        assertNotEquals(user.get("password"), "not");
        assertNotEquals(user.get("name"), "not");
        assertNotEquals(user.get("address"), "not");
        dbManager.removePatient((String) user.get("pid"), "someUser");
    }

    /**
     * Tests if a user can be added to the db successfully.
     *
     * @author max
     */
    @Test
    public void testAddPatient() {
        dbManager.addPatient("someUser", "somePass", "Jack Reacher", "Maine");
        assertTrue(dbManager.isUserPresent("someUser", "somePass"));
        dbManager.removePatient((String) dbManager.getUserInfo("someUser", "somePass").get("pid"), "someUser");
    }

    /**
     * Tests if a patient is successfully removed.
     *
     * @author max
     */
    @Test
    public void testRemovePatient() {
        dbManager.addPatient("someUser", "somePass", "Jack Reacher", "Maine");
        dbManager.removePatient((String) dbManager.getUserInfo("someUser", "somePass").get("pid"), "someUser");
        assertFalse(dbManager.isUserPresent("someUser", "somePass"));
    }

    /**
     * Tests that the next patient ID is generated correctly.
     *
     * @author max
     */
    @Test
    public void testGetNextPID() {
        int currentPID = dbManager.getNextPID();
        dbManager.addPatient("someUser", "somePass", "Jack Reacher", "Maine");
        dbManager.addPatient("someUser2", "somePass", "Jack Reacher", "Maine");
        assertEquals(currentPID + 2, dbManager.getNextPID());
        dbManager.removePatient((String) dbManager.getUserInfo("someUser", "somePass").get("pid"), "someUser");
        dbManager.removePatient((String) dbManager.getUserInfo("someUser2", "somePass").get("pid"), "someUser2");
    }

    @Test
    public void testAddLog() {
        String pid = String.valueOf(dbManager.getNextPID());
        String correctLog = "Signed up";
        dbManager.addPatient("rand", "somePass", "Jack Reacher", "Maine");
        dbManager.addLog(pid, "Signed up");
        assertEquals(correctLog, dbManager.getLogMessage(pid));
        dbManager.deleteLog((String) dbManager.getUserInfo("rand", "somePass").get("pid"));
        assertNull(dbManager.getLogMessage(pid));
        dbManager.removePatient((String) dbManager.getUserInfo("rand", "somePass").get("pid"), "someUser");
    }

    /**
     * Tests uhhhmmmm errrrrr uhhhhh ummmmmm
     *
     * @author josh
     */
    @Test
    public void testGetDoctorFullName() {
//        dbManager.addDoctor();
//        String actualFullName = dbManager.getDoctorFullName(doctorId);
//        assertEquals(expectedFullName, actualFullName);
//        dbManager.removeDoctor(doctorID);

        String doctorId = "1";  // Assuming first doctor is the testing
        String expectedFullName = "DOC TOR";  // Full name of testing doctor

        String actualFullName = dbManager.getDoctorFullName(doctorId);

        assertEquals(expectedFullName, actualFullName);
    }
}
