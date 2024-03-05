package src.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import src.DBManager;

public class DBManagerTest {
    private DBManager dbManager;

    /**
     * TODO
     * getUserInfo(String username, String password)
     * isUsernameDuplicate(String username)
     * addPatient(String username, String password, String name, String address)
     * removePatient(String pid, String username)
     * getNextPID()
     * getDoctorFullName(String did)
     * generateSignupMessage(HashMap<String, Object> userInformation)
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
     * Tests if a user can be added to the db successfully.
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

    @Test
    public void testGetDoctorFullName() {
        String doctorId = "1";  // Assuming first doctor is the testing
        String expectedFullName = "DOC TOR";  // Full name of testing doctor

        String actualFullName = dbManager.getDoctorFullName(doctorId);

        assertEquals(expectedFullName, actualFullName);
    }


    
}
