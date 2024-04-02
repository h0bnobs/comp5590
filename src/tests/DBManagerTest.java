package src.tests;

import org.junit.Before;
import org.junit.Test;
import src.Database.DBManager;

import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

public class DBManagerTest {
    private DBManager dbManager;

    @Before
    public void setUp() {
        dbManager = new DBManager();
    }

    /**
     * Tests that isUserPresent correctly returns whether a patient is already in the db or not.
     *
     * @author max
     */
    @Test
    public void testIsUserPresent() {
        String pid = String.valueOf(dbManager.getNextPID());
        dbManager.addPatient("someUser1", "somePass1", "Jack Reacher", "Maine");
        assertTrue(dbManager.isUserPresent("someUser1", "somePass1"));
        dbManager.removePatient(pid, "someUser1");
        assertFalse(dbManager.isUserPresent("someUser1", "somePass1"));
    }

    /**
     * Tests that getAllPreviousAppointments correctly returns all previous appointments that the user booked.
     *
     * @author max
     */
    @Test
    public void testGetAllPreviousAppointments() {
        String did = String.valueOf(dbManager.getNextDID());
        dbManager.addDoctor("first", "last", "address", "2023-01-01 10:00:00", "mental health");
        String pid = String.valueOf(dbManager.getNextPID());
        dbManager.addPatient("someUser", "somePass", "Jack Reacher", "Maine");
        dbManager.addAppointment(did, pid, "2020-01-10 10:00:00");
        dbManager.addAppointment(did, pid, "1990-07-19 12:00:00");
        List<HashMap<String, Object>> appointments = dbManager.getAllPreviousAppointments(pid);
        assertTrue(appointments.stream().anyMatch(appointment -> appointment.get("date_and_time").equals("2020-01-10 10:00:00")));
        assertTrue(appointments.stream().anyMatch(appointment -> appointment.get("date_and_time").equals("1990-07-19 12:00:00")));
        assertFalse(appointments.isEmpty());
        assertFalse(appointments.stream().anyMatch(appointment -> appointment.get("date_and_time").equals("9999-07-19 12:00:00")));
        dbManager.removeAppointment(did, pid, "2020-01-10 10:00:00");
        dbManager.removeAppointment(did, pid, "1990-07-19 12:00:00");
        dbManager.removePatient(pid, "someUser");
        dbManager.removeDoctor(did);
    }

    /**
     * Tests that getSpecificFutureAppointments correctly gets the correct appointments when given month and year.
     *
     * @author max
     */
    @Test
    public void testGetSpecificFutureAppointments() {
        String did = String.valueOf(dbManager.getNextDID());
        dbManager.addDoctor("first", "last", "address", "2023-01-01 10:00:00", "mental health");
        String pid = String.valueOf(dbManager.getNextPID());
        dbManager.addPatient("someUser", "somePass", "Jack Reacher", "Maine");
        dbManager.addAppointment(did, pid, "2025-03-03 11:00:00");
        dbManager.addAppointment(did, pid, "2025-03-03 12:00:00");
        List<HashMap<String, Object>> appointments = dbManager.getFutureAppointmentsByMonth(pid, did, "2025-03");
        assertEquals("2025-03-03 11:00:00", appointments.getFirst().get("date_and_time"));
        assertEquals("2025-03-03 12:00:00", appointments.get(1).get("date_and_time"));
        dbManager.removeAppointment(did, pid, "2025-03-03 11:00:00");
        dbManager.removeAppointment(did, pid, "2025-03-03 12:00:00");
        dbManager.removeDoctor(did);
        dbManager.removePatient(pid, "someUser");
    }

    /**
     * Tests that appointments are correctly removed from the database.
     *
     * @author max
     */
    @Test
    public void testRemoveAppointment() {
        String did = String.valueOf(dbManager.getNextDID());
        dbManager.addDoctor("first", "last", "address", "2023-01-01 10:00:00", "mental health");
        String pid = String.valueOf(dbManager.getNextPID());
        dbManager.addPatient("someUser", "somePass", "Jack Reacher", "Maine");
        dbManager.addAppointment(did, pid, "2026-02-02 11:00:00");
        assertTrue(dbManager.isAppointmentPresent(did, pid, "2026-02-02 11:00:00"));
        dbManager.removeAppointment(did, pid, "2026-02-02 11:00:00");
        assertFalse(dbManager.isAppointmentPresent(did, pid, "2026-02-02 11:00:00"));
        dbManager.removePatient(pid, "someUser");
        dbManager.removeDoctor(did);
    }

    /**
     * Tests that addAppointment correctly adds appointments to the database.
     *
     * @author max
     */
    @Test
    public void testAddAppointment() {
        String did = String.valueOf(dbManager.getNextDID());
        dbManager.addDoctor("first", "last", "address", "2023-01-01 10:00:00", "mental health");
        String pid = String.valueOf(dbManager.getNextPID());
        dbManager.addPatient("someUser", "somePass", "Jack Reacher", "Maine");
        dbManager.addAppointment(did, pid, "2025-02-02 11:00:00");
        assertTrue(dbManager.isAppointmentPresent(did, pid, "2025-02-02 11:00:00"));
        dbManager.removeAppointment(did, pid, "2025-02-02 11:00:00");
        dbManager.removePatient(pid, "someUser");
        dbManager.removeDoctor(did);
    }

    /**
     * Tests that doctorsBusyTimes correctly returns the correct times, when a doctor is busy.
     *
     * @author max
     */
    @Test
    public void testDoctorsBusyTimes() {
        String did = String.valueOf(dbManager.getNextDID());
        dbManager.addDoctor("first", "last", "address", "2023-01-01 10:00:00", "mental health");
        String pid = String.valueOf(dbManager.getNextPID());
        dbManager.addPatient("someUser", "somePass", "Jack Reacher", "Maine");
        dbManager.addAppointment(did, pid, "2025-01-01 11:00:00");
        assertTrue(dbManager.doctorsBusyTimes(did, "2025-01-01").contains("11:00:00"));
        assertFalse(dbManager.doctorsBusyTimes(did, "2050-09-23").contains(""));
        dbManager.removeAppointment(did, pid, "2025-01-01 11:00:00");
        dbManager.removePatient(pid, "someUser");
        dbManager.removeDoctor(did);
    }

    /**
     * Tests that getUserMessages correctly returns all the user's messages.
     *
     * @author max
     */
    @Test
    public void testGetUserMessages() {
        String pid = String.valueOf(dbManager.getNextPID());
        dbManager.addPatient("sjkdb", "dsuif", "Jack Reacher", "Maine");
        HashMap<String, Object> userInfo = new HashMap<>();
        dbManager.addMessage(userInfo, "message test!", pid);
        dbManager.addMessage(userInfo, "second message test!", pid);
        List<String> messages = dbManager.getUserMessages(pid);
        assertTrue(messages.contains("message test!"));
        assertTrue(messages.contains("second message test!"));
        assertFalse(messages.contains("not a message!"));
        dbManager.removeMessage("second message test!", pid);
        dbManager.removeMessage("message test!", pid);
        dbManager.removePatient(pid, "sjkdb");
    }

    /**
     * Tests that addMessage correctly adds messages to the database.
     *
     * @author max
     */
    @Test
    public void testAddMessage() {
        String pid = String.valueOf(dbManager.getNextPID());
        dbManager.addPatient("dsuif", "dsuif", "Jack Reacher", "Maine");
        HashMap<String, Object> userInfo = new HashMap<>();
        dbManager.addMessage(userInfo, "message test!", pid);
        List<String> messages = dbManager.getUserMessages(pid);
        assertTrue(messages.contains("message test!"));
        assertFalse(messages.contains("not in messages!"));
        dbManager.removeMessage("message test!", pid);
        dbManager.removePatient(pid, "dsuif");
    }

    /**
     * Tests that getNextDID correctly returns the next doctor id in the database.
     *
     * @author max
     */
    @Test
    public void testGetNextDID() {
        int currentDID = dbManager.getNextDID();
        dbManager.addDoctor("first", "last", "address", "2023-01-01 10:00:00", "mental health");
        dbManager.addDoctor("first", "last", "address123", "2023-01-01 11:00:00", "mental health");
        assertEquals(currentDID + 2, dbManager.getNextDID());
        dbManager.removeDoctor(String.valueOf(dbManager.getNextDID() - 2));
        dbManager.removeDoctor(String.valueOf(dbManager.getNextDID()));
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
     * Tests that the getDoctorFullName method gets the doctor's entire full name
     *
     * @author josh
     */
    @Test
    public void testGetDoctorFullName() {
        dbManager.addDoctor("Josh", "ThrowsCsGames", "Nuke", "2024-01-01 00:12:00", "Knife Kills");
        String actualName = dbManager.getDoctorFullName(String.valueOf(dbManager.getNextDID() - 1));
        assertEquals("Josh ThrowsCsGames", actualName);
        dbManager.removeDoctor(String.valueOf(dbManager.getNextDID() - 1));
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
