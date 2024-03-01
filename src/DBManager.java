package src;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBManager {
    //connection to the database
    private static Connection connection;
    private static Statement statement;
    private static ResultSet resultSet;

    /**
     * Checks the login given from the login page against the username and password in the database.
     *
     * @param username The username from the login window.
     * @param password The password from the login window.
     * @return true if login credentials are valid
     * @author max
     */
    public boolean checkUserLogin(String username, String password) {
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
     * Checks the login given from the login page against the username and password in the database.
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

            //execute the query
            resultSet = preparedStatement.executeQuery();

            //return true if there is a user found with that username and password, false if not
            while (resultSet.next()) {
                userInformation.put("pid", resultSet.getString("pid"));
                userInformation.put("name", resultSet.getString("name"));
                userInformation.put("address", resultSet.getString("address"));
                userInformation.put("username", resultSet.getString("username"));
                userInformation.put("password", resultSet.getString("password"));
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
     * @author max
     * @return true if a matching username already exists.
     */
    public boolean checkSignUpUsername(String username) {
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
     * Adds the new user to the database
     *
     * @param username The username from the sign-up window.
     * @param password The password from the sign-up window.
     * @param name The name from the sign-up window.
     * @param address The address from the sign-up window.
     * @author max
     */
    public void addUser(String username, String password, String name, String address) {
        try {
            //connect to my local database
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/comp5590?user=1&password=1");

            //pid
            //name
            //address
            //username
            //password
            //assigned_doctor_id

            //INSERT INTO `comp5590`.`patients` (`pid`, `name`, `address`, `username`, `password`, `assigned_doctor_id`) VALUES ('5', 'testname', 'addy', 'username', 'pass', '1');

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
     * Returns what the next patient id should be when adding a patient.
     * @author max
     * @return An int counter which is the pid that the next user will be.
     */
    private int getNextPID() {
        try {
            //connect to my local database
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/comp5590?user=1&password=1");
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select * from patients");
            int counter = 0;
            while (resultSet.next())
                counter++;

            System.out.println(counter + 1);
            return counter + 1;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }




    /**
     * Returns what the next doctor id should be when making list of doctors.
     * @author Joshwa
     * @return An int counter which is the did that the next user will be.
     */
    private int getNextDID() {
        try {
            //connect to my local database
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/comp5590?user=1&password=1");
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select * from patients");
            int counter = 0;
            while (resultSet.next())
                counter++;

            System.out.println(counter + 1);
            return counter + 1;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }



    /**
     * Gathers all infomation available about doctors.
     * @author Joshwa
     * @return Data which was gathered in form of List<HashMap>.
     */
    public static List<HashMap<String, Object>> getAllDoctors() {
        List<HashMap<String, Object>> doctorsList = new ArrayList<>();
    
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/comp5590?user=1&password=1");
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM doctors");
            ResultSet resultSet = preparedStatement.executeQuery()) {
    

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
            e.printStackTrace(); // Handle the exception appropriately, log or rethrow
        }
    
        return doctorsList;
    }

}