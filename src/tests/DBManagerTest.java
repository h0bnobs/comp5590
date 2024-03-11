package src.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import src.DBManager;

import java.util.HashMap;
import java.util.List;

public class DBManagerTest {
    private DBManager dbManager;

    /**
     * TODO
     * getNextDID()
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
        dbManager.removePatient("", "someUser");
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
        dbManager.addPatient("someUser", "Password@2023", "Jack Reacher", "Maine");
        dbManager.addPatient("someUser2", "Password@2023", "Jack Reacher", "Maine");
        assertEquals(currentPID + 2, dbManager.getNextPID());
        dbManager.removePatient((String) dbManager.getUserInfo("someUser", "somePass").get("pid"), "someUser");
        dbManager.removePatient((String) dbManager.getUserInfo("someUser2", "somePass").get("pid"), "someUser2");
    }

    /**
     * Tests that logs are correctly added to the log table.
     *
     * @author max
     */
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

    /**
     * Tests that doctors are correctly removed from the doctors table.
     *
     * @author max
     */
    @Test
    public void restRemoveDoctor() {
        dbManager.addDoctor("Jack", "Reacher", "USA", "2024-01-01 10:00:00", "dermatology");
        int numOfDoctors = dbManager.getAllDoctors().size();
        //System.out.println(dbManager.getAllDoctors().get(dbManager.getNextDID() - 2));
        assertEquals("Jack Reacher", dbManager.getDoctorFullName(String.valueOf(dbManager.getNextDID() - 1)));
        dbManager.removeDoctor(String.valueOf(dbManager.getNextDID() - 1));
        assertEquals(numOfDoctors - 1, dbManager.getAllDoctors().size());
    }

    /**
     * Tests that the getAllDoctors method returns all doctors correctly.
     *
     * @author max
     */
    @Test
    public void testGetAllDoctors() {
        dbManager.addDoctor("Jack", "Reacher", "USA", "2024-01-01 10:00:00", "dermatology");
        dbManager.addDoctor("Oscar", "Mccormack", "KH", "2023-02-02 09:00:00", "OW2");
        List<HashMap<String, Object>> doctors = dbManager.getAllDoctors();
        assertFalse(doctors.isEmpty());
        boolean containsFirst = doctors.stream().anyMatch(doctor -> doctor.get("did").equals(String.valueOf(dbManager.getNextDID() - 2)));
        boolean containsSecond = doctors.stream().anyMatch(doctor -> doctor.get("did").equals(String.valueOf(dbManager.getNextDID() - 1)));
        assertTrue(containsFirst);
        assertTrue(containsSecond);
        dbManager.removeDoctor(String.valueOf(dbManager.getNextDID() - 2));
        dbManager.removeDoctor(String.valueOf(dbManager.getNextDID()));
    }

    /**
     * Tests that the updateAssignedDoctorID method correctly changes the patient's assigned doctor foreign key correctly.
     *
     * @author max
     */
    @Test
    public void testUpdateAssignedDoctorId() {
        dbManager.addPatient("someUser", "somePass", "Jack Reacher", "Maine");
        dbManager.addDoctor("Jack", "Reacher", "USA", "2024-01-01 10:00:00", "dermatology");
        String pid = String.valueOf(dbManager.getNextPID() - 1);
        String docName = "Jack Reacher";
        dbManager.updateAssignedDoctorId(pid, docName);
        HashMap<String, Object> userInfo = dbManager.getUserInfo("someUser", "somePass");
        assertEquals(String.valueOf(dbManager.getNextDID() - 1), userInfo.get("assigned_doctor_id"));
        dbManager.removePatient(pid, null);
        dbManager.removeDoctor(String.valueOf(dbManager.getNextDID() - 1));
    }

    /**
     * Tests that the getAllDoctorNames method correctly returns the required doctor's full name.
     *
     * @author max
     */
    @Test
    public void testGetAllDoctorNames() {
        dbManager.addDoctor("Bobby", "Shmurda", "Goudhurst", "2024-03-10 09:00:00", "cardiology");
        dbManager.addDoctor("Jack", "Reacher", "USA", "2024-01-01 10:00:00", "dermatology");

        List<String> doctorNames = DBManager.getAllDoctorNames();
        assertTrue(doctorNames.contains("Bobby Shmurda"));
        assertTrue(doctorNames.contains("Jack Reacher"));

        dbManager.removeDoctor(String.valueOf(dbManager.getNextDID() - 1));
        dbManager.removeDoctor(String.valueOf(dbManager.getNextDID() - 1));
    }
}
