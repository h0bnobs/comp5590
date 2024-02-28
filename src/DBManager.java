package src;

import java.sql.*;
import java.util.HashMap;

public class DBManager {
    //connection to the database
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    /**
     * Checks the login given from the login page against the username and password in the database.
     * @param username The username from the login window.
     * @param password The password from the login window.
     * @author max
     * @return true if login credentials are valid
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
     * @param username The username from the login window.
     * @param password The password from the login window.
     * @author max
     * @return A HashMap with the user info.
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

}