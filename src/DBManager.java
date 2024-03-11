package src;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DBManager {
    //connection to the database
    private static Connection connection;
    private static Statement statement;
    private static ResultSet resultSet;

    /**
     * Adds a message to the messages table.
     *
     * @param userInformation the patient that the new message is for.
     * @param message         the actual message to be added.
     * @author max
     */
    public void addMessage(HashMap<String, Object> userInformation, String message) {
        try {
            //connect to my local database
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/comp5590?user=1&password=1");

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
    }

    /**
     * Gets all messages that a user has in the messages table.
     * @param pid the patient's pid.
     * @return a list of the patient's messages.
     * @author max
     */
    public List<String> getUserMessages(String pid) {
        List<String> userMessages = new ArrayList<>();

        try {
            //connect to my local database
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/comp5590?user=1&password=1");

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

    /**
     * Adds a log of what user did what functionality.
     *
     * @param pid     the patient ID of the user.
     * @param message the main message of the log.
     * @author max
     */
    public void addLog(String pid, String message) {
        try {
            //connect to my local database
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/comp5590?user=1&password=1");

            //INSERT INTO comp5590.logs (functionality_accessed, date_and_time, pid)
            //String query = "INSERT INTO `comp5590`.`patients` (`pid`, `name`, `address`, `username`, `password`, `assigned_doctor_id`) VALUES (?, ?, ?, ?, ?, ?)";
            //PreparedStatement preparedStatement = connection.prepareStatement(query);
            //preparedStatement.setString(1, String.valueOf(pid));
            //preparedStatement.setString(2, name);

            //prepare a query on that db
            String query = "INSERT INTO `comp5590`.`logs` (`functionality_accessed`, `date_and_time`, `pid`) VALUE (?, ?, ?)";
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

    /**
     * Gets the log message from the first log associated with a user.
     *
     * @param pid the pid associated with the log.
     * @return the log message.
     * @author max
     */
    public String getLogMessage(String pid) {
        try {
            //connect to my local database
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/comp5590?user=1&password=1");

            //prepare a query on that db
            String query = "SELECT logs.functionality_accessed FROM logs INNER JOIN patients ON logs.pid = patients.pid WHERE patients.pid = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, pid);

            resultSet = preparedStatement.executeQuery();
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

    /**
     * deletes log entries associated with a specific patient ID.
     *
     * @param pid The patient ID whose log entries need to be deleted.
     */
    public void deleteLog(String pid) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/comp5590?user=1&password=1");

            String query = "DELETE FROM logs WHERE pid = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, pid);

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


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
            //connect to my local database
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/comp5590?user=1&password=1");

            //prepare a query on that db
            String query = "SELECT * FROM patients WHERE username = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username); // Set username as the first parameter
            preparedStatement.setString(2, password); // Set password as the second parameter

            //execute the query
            resultSet = preparedStatement.executeQuery();

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
            //connect to my local database
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/comp5590?user=1&password=1");

            //prepare a query on that db
            String query = "SELECT * FROM patients WHERE username = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username); // Set username as the first parameter
            preparedStatement.setString(2, password); // Set password as the second parameter

            resultSet = preparedStatement.executeQuery();

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

    /**
     * Checks that a user isn't trying to sign up using a duplicate username
     *
     * @param username The username from the sign-up window.
     * @return true if a matching username already exists.
     * @author max
     */
    public boolean isUsernameDuplicate(String username) {
        try {
            //connect to my local database
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/comp5590?user=1&password=1");

            //prepare a query on that db
            String query = "SELECT * FROM patients WHERE username = ? ";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username); // Set username as the first parameter

            //execute the query
            resultSet = preparedStatement.executeQuery();

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

    /**
     * Adds a doctor to the db.
     *
     * @author max
     */
    public void addDoctor(String firstName, String lastName, String address, String startDate, String specialistArea) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/comp5590?user=1&password=1");

            String query = "INSERT INTO `comp5590`.`doctors` (`did`, `first_name`, `last_name`, `address`, `start_date`, `specialist_area`) VALUES (?, ?, ?, ?, ?, ?)";
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
            //connect to my local database
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/comp5590?user=1&password=1");

            //prepare a query on that db
            //String query = "SELECT * FROM patients WHERE username = ? AND password = ?";
            int pid = getNextPID();
            String query = "INSERT INTO `comp5590`.`patients` (`pid`, `name`, `address`, `username`, `password`, `assigned_doctor_id`) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, String.valueOf(pid));
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, address);
            preparedStatement.setString(4, username);
            preparedStatement.setString(5, password);
            preparedStatement.setString(6, "1");

            preparedStatement.execute();
            //execute the query

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * removes a patient from the database, using their pid or username
     *
     * @param pid      patient's pid
     * @param username patient's username
     * @author max
     */
    public void removePatient(String pid, String username) {
        try {
            //connect to my local database
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/comp5590?user=1&password=1");
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

    /**
     * removes a doctor from the database, using their did.
     *
     * @param did doctors' did
     * @author max
     */
    public void removeDoctor(String did) {
        try {
            //connect to my local database
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/comp5590?user=1&password=1");
            String query = "DELETE FROM doctors WHERE did = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, did);
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns what the next patient id should be when adding a patient.
     *
     * @return An int counter which is the pid that the next user will be.
     * @author max
     */
    public int getNextPID() {
        try {
            //connect to my local database
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/comp5590?user=1&password=1");
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select * from patients");
            int counter = 0;
            while (resultSet.next()) counter++;

            System.out.println(counter + 1);
            return counter + 1;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

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
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/comp5590?user=1&password=1");

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

    /**
     * generates a sign-up message after user signs up.
     *
     * @param userInformation all user information.
     * @return a simple signup message.
     * @author max
     */
    public String generateSignupMessage(HashMap<String, Object> userInformation) {
        String did = (String) userInformation.get("assigned_doctor_id");
        return "Welcome " + userInformation.get("name") + ", you are now signed up with dr. " + getDoctorFullName(did);
    }

    /**
     * Updates the patient's assigned doctor ID when using the sign-up window.
     *
     * @param patientId          the pid of the new user.
     * @param selectedDoctorName the FULL name of their newly selected doctor.
     * @author max
     */
    public void updateAssignedDoctorId(String patientId, String selectedDoctorName) {
        try {
            //connect to my local database
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/comp5590?user=1&password=1");
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


    /**
     * Returns what the next doctor id should be when making list of doctors.
     *
     * @return An int counter which is the did that the next doctor will be
     * @author Joshwa
     */
    public int getNextDID() {
        try {
            //connect to my local database
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/comp5590?user=1&password=1");
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select * from doctors");
            int counter = 0;
            while (resultSet.next()) counter++;

            System.out.println(counter + 1);
            return counter + 1;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * Gathers all information available about doctors.
     *
     * @return Data which was gathered in form of List<HashMap>.
     * @author Joshwa
     */
    public List<HashMap<String, Object>> getAllDoctors() {
        List<HashMap<String, Object>> doctorsList = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/comp5590?user=1&password=1"); PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM doctors"); ResultSet resultSet = preparedStatement.executeQuery()) {


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

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return doctorsList;
    }


    // Possibly temporary but keeping it until I can figure out other solution
    public static List<String> getAllDoctorNames() {
        List<String> doctorNames = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/comp5590?user=1&password=1"); PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM doctors"); ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                // Get doctors first and last names from DB to add to the list
                String fullName = resultSet.getString("first_name") + " " + resultSet.getString("last_name");
                doctorNames.add(fullName);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return doctorNames;
    }


// Can do above method in the getAllDoctors method through doing line 261 stuff & 269 stuff within to create the list there.

}