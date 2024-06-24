package src.Database;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SQLiteExample {
    private static final Connection connection;

    static {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:./database.db");
            //connection = DriverManager.getConnection("jdbc:mysql://localhost/comp5590?user=1&password=1");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Database URL (this can be an absolute or relative path)

    /**
     * Tests the connection to the db.
     *
     * @return true if there is a connection to the db.
     */
    public static boolean testConn() {
        try {
            String query = "SELECT * FROM patients";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet results = preparedStatement.executeQuery();
            return results.next();
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println(testConn());
    }

//---------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Gets all the user's previous appointments that they booked.
     *
     * @param pid The current user's information
     * @return A list of the appointments.
     */
    public List<HashMap<String, Object>> getAllPreviousAppointments(String pid) {
        List<HashMap<String, Object>> appointments = new ArrayList<>();
        try {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String currentDateTime = now.format(formatter);

            String query = "SELECT * FROM appointments WHERE pid = ? AND date_and_time < ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, pid);
            preparedStatement.setString(2, currentDateTime);

            ResultSet results = preparedStatement.executeQuery();
            while (results.next()) {
                HashMap<String, Object> singleAppointment = new HashMap<>();
                singleAppointment.put("visit_details", results.getString("visit_details"));
                singleAppointment.put("did", results.getString("did"));
                singleAppointment.put("pid", results.getString("pid"));
                singleAppointment.put("prescriptions", results.getString("prescriptions"));
                singleAppointment.put("date_and_time", results.getString("date_and_time"));
                appointments.add(singleAppointment);
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return appointments;
    }

    /**
     * Gets all future appointments no params needed.
     *
     * @param pid the patient
     * @param did the doctor
     * @return a list of appointments
     */
    public List<HashMap<String, Object>> getAllFutureAppointments(String pid, String did) {
        List<HashMap<String, Object>> appointments = new ArrayList<>();
        try {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String currentDateTime = now.format(formatter);

            String query = "SELECT * FROM appointments WHERE pid = ? AND did = ? AND date_and_time > ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, pid);
            preparedStatement.setString(2, did);
            preparedStatement.setString(3, currentDateTime);

            ResultSet results = preparedStatement.executeQuery();
            while (results.next()) {
                HashMap<String, Object> singleAppointment = new HashMap<>();
                singleAppointment.put("visit_details", results.getString("visit_details"));
                singleAppointment.put("did", results.getString("did"));
                singleAppointment.put("pid", results.getString("pid"));
                singleAppointment.put("prescriptions", results.getString("prescriptions"));
                singleAppointment.put("date_and_time", results.getString("date_and_time"));
                appointments.add(singleAppointment);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return appointments;
    }

//---------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Gets all the user's future appointments that they have booked by month.
     *
     * @param pid          The current user's information.
     * @param did          The current user's doctor's id.
     * @param yearAndMonth The month and year to be looked up.
     * @return A list of the appointments.
     */
    public List<HashMap<String, Object>> getFutureAppointmentsByMonth(String pid, String did, String yearAndMonth) {
        List<HashMap<String, Object>> appointments = new ArrayList<>();
        try {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String currentDateTime = now.format(formatter);

            String query = "SELECT * FROM appointments WHERE pid = ? AND did = ? AND strftime('%Y-%m', date_and_time) = ? AND date_and_time > ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, pid);
            preparedStatement.setString(2, did);
            preparedStatement.setString(3, yearAndMonth);
            preparedStatement.setString(4, currentDateTime);

            ResultSet results = preparedStatement.executeQuery();
            while (results.next()) {
                HashMap<String, Object> singleAppointment = new HashMap<>();
                singleAppointment.put("visit_details", results.getString("visit_details"));
                singleAppointment.put("did", results.getString("did"));
                singleAppointment.put("pid", results.getString("pid"));
                singleAppointment.put("prescriptions", results.getString("prescriptions"));
                singleAppointment.put("date_and_time", results.getString("date_and_time"));
                appointments.add(singleAppointment);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return appointments;
    }

//---------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Gets all the user's future appointments that they have booked by the full date.
     *
     * @param pid           the current user's pid.
     * @param did           the current user's doctor id.
     * @param yearMonthDate The full date (year, month then date)
     * @return
     */
    public List<HashMap<String, Object>> getFutureAppointmentsByFullDate(String pid, String did, String yearMonthDate) {
        List<HashMap<String, Object>> appointments = new ArrayList<>();
        try {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String currentDateTime = now.format(formatter);

            String query = "SELECT * FROM appointments WHERE pid = ? AND did = ? AND strftime('%Y-%m-%d', date_and_time) = ? AND date_and_time > ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, pid);
            preparedStatement.setString(2, did);
            preparedStatement.setString(3, yearMonthDate);
            preparedStatement.setString(4, currentDateTime);

            ResultSet results = preparedStatement.executeQuery();
            while (results.next()) {
                HashMap<String, Object> singleAppointment = new HashMap<>();
                singleAppointment.put("visit_details", results.getString("visit_details"));
                singleAppointment.put("did", results.getString("did"));
                singleAppointment.put("pid", results.getString("pid"));
                singleAppointment.put("prescriptions", results.getString("prescriptions"));
                singleAppointment.put("date_and_time", results.getString("date_and_time"));
                appointments.add(singleAppointment);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return appointments;
    }

    /**
     * Adds a new appointment to the database.
     *
     * @param did  The doctor who the appointment was booked with.
     * @param pid  The patient that booked the appointment.
     * @param date The date that the appointment will be booked on.
     * @author max
     */
    public void addAppointment(String did, String pid, String date) {
        try {
            String query = "INSERT INTO appointments (`did`, `pid`, `date_and_time`) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, did);
            preparedStatement.setString(2, pid);
            preparedStatement.setString(3, date);

            preparedStatement.execute();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

//---------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Deletes an appointment from the database.
     *
     * @param did  The doctor who the appointment could be booked with.
     * @param pid  The patient who the appointment could be booked with.
     * @param date The date that the appointment is booked on.
     * @author max
     */
    public void removeAppointment(String did, String pid, String date) {
        try {
            String query = "DELETE FROM appointments WHERE did = ? AND pid = ? AND date_and_time = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, did);
            statement.setString(2, pid);
            statement.setString(3, date);
            statement.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

//---------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Check if an appointment already exists/has been booked.
     *
     * @param did  The doctor who the appointment could be booked with.
     * @param pid  The patient who the appointment could be booked with.
     * @param date The date and time that the appointment could be booked on.
     * @return true if the appointment has been booked, false if not.
     */
    public boolean isAppointmentPresent(String did, String pid, String date) {
        try {
            String query = "SELECT * FROM appointments WHERE did = ? AND pid = ? AND date_and_time = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, did);
            statement.setString(2, pid);
            statement.setString(3, date);

            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();

        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

//---------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Returns all times during a given date, where a doctor is busy.
     *
     * @param did  The doctor to be looked up.
     * @param date The date to be looked up.
     * @return A list of the times a doctor is busy on the given date.
     */
    public ArrayList<String> doctorsBusyTimes(String did, String date) {
        ArrayList<String> freeTimes = new ArrayList<>();
        try {
            String query = "SELECT date_and_time FROM appointments WHERE did = ? AND date(date_and_time) = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, did);
            statement.setString(2, date);
            ResultSet results = statement.executeQuery();

            //list of all appointments
            ArrayList<String> times = new ArrayList<>();
            while (results.next()) {
                String appTime = results.getString("date_and_time");
                times.add(appTime);
            }

            //all available times between 9am and 5pm (assuming the surgery is gonna be open these hours only).
            for (int hour = 9; hour <= 17; hour++) {
                String time = String.format("%02d", hour) + ":00:00";
                if (times.contains(date + " " + time)) {
                    freeTimes.add(time);
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        //System.out.println(freeTimes);
        return freeTimes;
    }

//---------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Adds a message to the messages table.
     *
     * @param userInformation the patient that the new message is for.
     * @param message         the actual message to be added.
     * @author max
     */
    public void addMessage(HashMap<String, Object> userInformation, String message, String pid) {
        if (!userInformation.isEmpty()) {
            try {
                //check if a message already exists in the messages table column 'message_content'
                String query = "SELECT * FROM messages WHERE pid = ? AND message_content = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, (String) userInformation.get("pid"));
                preparedStatement.setString(2, message);

                ResultSet resultSet = preparedStatement.executeQuery();

                if (!resultSet.next()) {
                    String queryAdd = "INSERT INTO messages (pid, message_content, timestamp) VALUES (?, ?, ?)";
                    PreparedStatement preparedStatementAdd = connection.prepareStatement(queryAdd);
                    preparedStatementAdd.setString(1, (String) userInformation.get("pid"));
                    preparedStatementAdd.setString(2, message);

                    LocalDateTime now = LocalDateTime.now();
                    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String dateTime = now.format(format);
                    preparedStatementAdd.setString(3, dateTime);

                    preparedStatementAdd.executeUpdate();
                } else {
                    return;
                }

            } catch (Exception e) {
                e.printStackTrace();

            }
        } else {
            try {
                //check if a message already exists in the messages table column 'message_content'
                String query = "SELECT * FROM messages WHERE pid = ? AND message_content = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, pid);
                preparedStatement.setString(2, message);

                ResultSet resultSet = preparedStatement.executeQuery();

                if (!resultSet.next()) {
                    String queryAdd = "INSERT INTO messages (pid, message_content, timestamp) VALUES (?, ?, ?)";
                    PreparedStatement preparedStatementAdd = connection.prepareStatement(queryAdd);
                    preparedStatementAdd.setString(1, pid);
                    preparedStatementAdd.setString(2, message);

                    LocalDateTime now = LocalDateTime.now();
                    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String dateTime = now.format(format);
                    preparedStatementAdd.setString(3, dateTime);

                    preparedStatementAdd.executeUpdate();
                } else {
                    return;
                }

            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }

//---------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Removes a message from the database.
     *
     * @param messageContents The exact contents of the message to be deleted. Can be empty if the pid is NOT empty.
     * @param pid             The pid of the user. Can be empty if the message contents is NOT empty.
     * @author max
     */
    public void removeMessage(String messageContents, String pid) {
        if (pid.isEmpty() && !messageContents.isEmpty()) {
            try {
                String query = "DELETE FROM messages WHERE message_content = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, messageContents);
                statement.executeUpdate();

            } catch (Exception e) {
                System.out.println(e);
            }
        } else if (!pid.isEmpty() && messageContents.isEmpty()) {
            try {
                String query = "DELETE FROM messages WHERE pid = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, pid);
                statement.executeUpdate();

            } catch (Exception e) {
                System.out.println(e);
            }
        } else {
            try {
                String query = "DELETE FROM messages WHERE message_content = ? AND pid = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, messageContents);
                statement.setString(2, pid);

                System.out.println(statement);
                statement.executeUpdate();

            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }


//---------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Gets all messages that a user has in the messages table.
     *
     * @param pid the patient's pid.
     * @return a list of the patient's messages.
     * @author max
     */
    public List<String> getUserMessages(String pid) {
        List<String> userMessages = new ArrayList<>();
        try {
            String query = "SELECT message_content FROM messages WHERE pid = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, pid);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String message = resultSet.getString("message_content");
                userMessages.add(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userMessages;
    }

//---------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Adds a log of what user did what functionality.
     *
     * @param pid     the patient ID of the user.
     * @param message the main message of the log.
     * @author max
     */
    public void addLog(String pid, String message) {
        try {
            String query = "INSERT INTO logs (functionality_accessed, date_and_time, pid) VALUES (?, ?, ?)";
            //String query = "INSERT INTO logs (functionality_accessed, date_and_time, pid) VALUE (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, message);

            LocalDateTime now = LocalDateTime.now();
            //american only which sucks
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = now.format(formatter);

            preparedStatement.setString(2, formattedDateTime);
            preparedStatement.setString(3, pid);

            preparedStatement.execute();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

//---------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Gets the log message from the first log associated with a user. For testing purposes only.
     *
     * @param pid the pid associated with the log.
     * @return the log message.
     * @author max
     */
    public String getLogMessage(String pid) {
        try {
            //prepare a query on that db
            String query = "SELECT logs.functionality_accessed FROM logs INNER JOIN patients ON logs.pid = patients.pid WHERE patients.pid = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, pid);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("functionality_accessed");
            } else {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

//---------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * deletes log entries associated with a specific patient ID.
     *
     * @param pid The patient ID whose log entries need to be deleted.
     */
    public void deleteLog(String pid) {
        try {
            String query = "DELETE FROM logs WHERE pid = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, pid);

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//---------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Checks the login given from the login page against the usernames and passwords in the database.
     *
     * @param username The username from the login window.
     * @param password The password from the login window.
     * @return true if login credentials are valid
     * @author max
     */
    public boolean isUserPresent(String username, String password) {
        try {
            //prepare a query on that db
            String query = "SELECT * FROM patients WHERE username = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username); // Set username as the first parameter
            preparedStatement.setString(2, password); // Set password as the second parameter

            //execute the query
            ResultSet resultSet = preparedStatement.executeQuery();

            //return true if there is a user found with that username and password, false if not
            if (resultSet.next()) {
                System.out.println("The user " + resultSet.getString("name") + " logged in!");
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

//---------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Gets all current user info.
     *
     * @param username The username from the login window.
     * @param password The password from the login window.
     * @return A HashMap with the user info.
     * @author max
     */
    public HashMap<String, Object> getUserInfo(String username, String password) {
        HashMap<String, Object> userInformation = new HashMap<>();
        try {
            //prepare a query on that db
            String query = "SELECT * FROM patients WHERE username = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username); // Set username as the first parameter
            preparedStatement.setString(2, password); // Set password as the second parameter

            ResultSet resultSet = preparedStatement.executeQuery();

            //return true if there is a user found with that username and password, false if not
            while (resultSet.next()) {
                userInformation.put("pid", resultSet.getString("pid"));
                userInformation.put("name", resultSet.getString("name"));
                userInformation.put("address", resultSet.getString("address"));
                userInformation.put("username", resultSet.getString("username"));
                userInformation.put("password", resultSet.getString("password"));
                userInformation.put("assigned_doctor_id", resultSet.getString("assigned_doctor_id"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return userInformation;
    }

//---------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Checks that a user isn't trying to sign up using a duplicate username
     *
     * @param username The username from the sign-up window.
     * @return true if a matching username already exists.
     * @author max
     */
    public boolean isUsernameDuplicate(String username) {
        try {
            String query = "SELECT * FROM patients WHERE username = ? ";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username); // Set username as the first parameter

            //execute the query
            ResultSet resultSet = preparedStatement.executeQuery();

            //return true if there is a user found with that username and password, false if not
            if (resultSet.next()) {
                System.out.println("The username " + resultSet.getString("username") + " already exists, please try a new one!");
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

//---------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Adds a doctor to the db.
     *
     * @author max
     */
    public void addDoctor(String firstName, String lastName, String address, String startDate, String specialistArea) {
        try {
            String query = "INSERT INTO doctors (`did`, `first_name`, `last_name`, `address`, `start_date`, `specialist_area`) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            int did = getNextDID();

            preparedStatement.setInt(1, did);
            preparedStatement.setString(2, firstName);
            preparedStatement.setString(3, lastName);
            preparedStatement.setString(4, address);
            preparedStatement.setString(5, startDate);
            preparedStatement.setString(6, specialistArea);

            preparedStatement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//---------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Adds the new patient to the database
     *
     * @param username The username from the sign-up window.
     * @param password The password from the sign-up window.
     * @param name     The name from the sign-up window.
     * @param address  The address from the sign-up window.
     * @author max
     */
    public void addPatient(String username, String password, String name, String address) {
        try {
            //String query = "SELECT * FROM patients WHERE username = ? AND password = ?";
            int pid = getNextPID();
            String query = "INSERT INTO patients (`pid`, `name`, `address`, `username`, `password`, `assigned_doctor_id`) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, String.valueOf(pid));
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, address);
            preparedStatement.setString(4, username);
            preparedStatement.setString(5, password);
            preparedStatement.setString(6, "1");

            preparedStatement.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//---------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * removes a patient from the database, using their pid or username
     *
     * @param pid      patient's pid
     * @param username patient's username
     * @author max
     */
    public void removePatient(String pid, String username) {
        try {
            String query;

            if (pid != null && !pid.isEmpty()) {
                query = "DELETE FROM patients WHERE pid = ?";
            } else if (username != null && !username.isEmpty()) {
                query = "DELETE FROM patients WHERE username = ?";
            } else {
                System.out.println("Please provide pid or username to delete a patient.");
                return;
            }

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            if (pid != null && !pid.isEmpty()) {
                preparedStatement.setString(1, pid);
            } else {
                preparedStatement.setString(1, username);
            }
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//---------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * removes a doctor from the database, using their did.
     *
     * @param did doctors' did
     * @author max
     */
    public void removeDoctor(String did) {
        try {
            String query = "DELETE FROM doctors WHERE did = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, did);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//---------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Returns what the next patient id should be when adding a patient.
     *
     * @return An int counter which is the pid that the next user will be.
     * @author max
     */
    public int getNextPID() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from patients");
            int counter = 0;
            while (resultSet.next()) counter++;
            System.out.println(counter + 1);
            return counter + 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

//---------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Returns the doctor's full name.
     *
     * @param did the doctor's id.
     * @return their full name, with a space in between first and second.
     * @author max
     */
    public String getDoctorFullName(String did) {
        String fullName = "";
        try {
            String query = "SELECT CONCAT(first_name, ' ', last_name) AS full_name FROM doctors WHERE did = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, did);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                fullName = resultSet.getString("full_name");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fullName;
    }

//---------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Updates the patient's assigned doctor ID when using the sign-up window.
     *
     * @param patientId          the pid of the new user.
     * @param selectedDoctorName the FULL name of their newly selected doctor.
     * @author max
     */
    public void updateAssignedDoctorId(String patientId, String selectedDoctorName) {
        try {
            int pid = Integer.parseInt(patientId);

            //need first name only
            String firstName = selectedDoctorName.split(" ")[0];

            String getDoctorId = "SELECT did FROM doctors WHERE first_name = ?";
            PreparedStatement preparedStatementDID = connection.prepareStatement(getDoctorId);
            preparedStatementDID.setString(1, firstName);
            ResultSet resultSetDoctorId = preparedStatementDID.executeQuery();
            int did = 0;
            if (resultSetDoctorId.next()) {
                did = resultSetDoctorId.getInt("did");
            } else {
                System.out.println("Doctor not found");
                return;
            }

            //update the patient record where int pid matches the pid in the table, then set their assigned_doctor_id
            //to the did of the doctor whose name matches doctorName.
            String query = "UPDATE patients SET assigned_doctor_id = ? WHERE pid = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, did);
            preparedStatement.setInt(2, pid);

            preparedStatement.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//---------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Returns what the next doctor id should be when making list of doctors.
     *
     * @return An int counter which is the did that the next doctor will be
     * @author Josh
     */
    public int getNextDID() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from doctors");
            int counter = 0;
            while (resultSet.next()) counter++;

            System.out.println(counter + 1);
            return counter + 1;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

//---------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Gathers all information available about doctors.
     *
     * @return Data which was gathered in form of List<HashMap>.
     * @author Josh
     */
    public List<HashMap<String, Object>> getAllDoctors() {
        List<HashMap<String, Object>> doctorsList = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM doctors");
            ResultSet resultSet = preparedStatement.executeQuery();
            // Get infomation row by row.
            while (resultSet.next()) {
                HashMap<String, Object> doctorInformation = new HashMap<>();
                doctorInformation.put("did", resultSet.getString("did"));
                doctorInformation.put("first_name", resultSet.getString("first_name"));
                doctorInformation.put("last_name", resultSet.getString("last_name"));
                doctorInformation.put("address", resultSet.getString("address"));
                doctorInformation.put("start_date", resultSet.getString("start_date"));
                doctorInformation.put("specialist_area", resultSet.getString("specialist_area"));

                doctorsList.add(doctorInformation);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return doctorsList;
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Returns a list of all the doctor's full names.
     *
     * @return list of names.
     * @author Josh
     */
    public static List<String> getAllDoctorNames() {
        List<String> doctorNames = new ArrayList<>();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM doctors");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                // Get doctors first and last names from DB to add to the list
                String fullName = resultSet.getString("first_name") + " " + resultSet.getString("last_name");
                doctorNames.add(fullName);
            }
            return doctorNames;
        } catch (Exception e) {
            System.out.println(e);
        }
        return doctorNames;
    }
}



