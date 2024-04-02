package src.UI;

import src.Database.DBManager;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * The first window, where the user can either log in or sign up.
 */
public class LoginAndSignup {

    private JFrame frame;
    private final GUI gui = new GUI();

    private JLabel tryAgainLabel;
    private JTextField usernameTextField;
    private JTextField passwordTextField;

//---------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * The signup window.
     *
     * @author max
     */
    protected void signupInterface() {
        frame = new JFrame("Sign up");
        frame.setSize(400, 300);
        frame.setLayout(new GridBagLayout());
        DBManager database = new DBManager();

        GridBagConstraints enternameLabelConstraint = new GridBagConstraints();
        enternameLabelConstraint.gridx = 0;
        enternameLabelConstraint.gridy = 0;
        JLabel enterNameLabel = new JLabel("Enter your name: ");
        frame.add(enterNameLabel, enternameLabelConstraint);

        GridBagConstraints enterNameTextFieldConstraint = new GridBagConstraints();
        enterNameTextFieldConstraint.gridx = 1;
        enterNameTextFieldConstraint.gridy = 0;
        JTextField nameTextField = new JTextField(10);
        frame.add(nameTextField, enterNameTextFieldConstraint);

        GridBagConstraints enterAddressLabelConstraint = new GridBagConstraints();
        enterAddressLabelConstraint.gridx = 0;
        enterAddressLabelConstraint.gridy = 1;
        JLabel enterAddressLabel = new JLabel("Enter your address: ");
        frame.add(enterAddressLabel, enterAddressLabelConstraint);

        GridBagConstraints enterAddressTextFieldConstraint = new GridBagConstraints();
        enterAddressTextFieldConstraint.gridx = 1;
        enterAddressTextFieldConstraint.gridy = 1;
        JTextField addressTextField = new JTextField(10);
        frame.add(addressTextField, enterAddressTextFieldConstraint);

        GridBagConstraints enterUsernameLabelConstraint = new GridBagConstraints();
        enterUsernameLabelConstraint.gridx = 0;
        enterUsernameLabelConstraint.gridy = 3;
        JLabel enterUsernameLabel = new JLabel("Enter a username: ");
        frame.add(enterUsernameLabel, enterUsernameLabelConstraint);

        GridBagConstraints enterUsernameTextFieldConstraint = new GridBagConstraints();
        enterUsernameTextFieldConstraint.gridx = 1;
        enterUsernameTextFieldConstraint.gridy = 3;
        usernameTextField = new JTextField(10);
        frame.add(usernameTextField, enterUsernameTextFieldConstraint);

        GridBagConstraints enterPasswordLabelConstraint = new GridBagConstraints();
        enterPasswordLabelConstraint.gridx = 0;
        enterPasswordLabelConstraint.gridy = 4;
        JLabel enterPasswordLabel = new JLabel("Enter a password: ");
        frame.add(enterPasswordLabel, enterPasswordLabelConstraint);

        GridBagConstraints enterPasswordTextFieldConstraint = new GridBagConstraints();
        enterPasswordTextFieldConstraint.gridx = 1;
        enterPasswordTextFieldConstraint.gridy = 4;
        passwordTextField = new JPasswordField(10);
        frame.add(passwordTextField, enterPasswordTextFieldConstraint);

        GridBagConstraints signUpButtonConstraint = new GridBagConstraints();
        signUpButtonConstraint.gridx = 2;
        signUpButtonConstraint.gridy = 7;
        JButton signupButton = new JButton("Next");
        frame.add(signupButton, signUpButtonConstraint);

        GridBagConstraints goBackButtonConstraint = new GridBagConstraints();
        goBackButtonConstraint.gridx = 0;
        goBackButtonConstraint.gridy = 7;
        JButton goBackButton = new JButton("Go back");
        frame.add(goBackButton, goBackButtonConstraint);

        //check for existing user
        //if not found, add it to db and login.
        signupButton.addActionListener(e -> {
            String username = usernameTextField.getText();
            String password = passwordTextField.getText();
            String name = nameTextField.getText();

            //Checks to make sure given name doesn't contain digits.
            if (!gui.validateName(name)) {
                JOptionPane.showMessageDialog(frame, "Your name cannot contain anything of numerical value", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            //Checks to make sure the given password is valid.
            if (!gui.validatePassword(password)) {
                JOptionPane.showMessageDialog(frame, "Password Must Contain: \n• Eight or more Characters long \n• One or more Uppercase characters \n• One or more Lowercase characters \n• One or more numerical characters \n• One or more special Character e.g @, ! or $", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LinkedHashMap<Integer, String> newUser = new LinkedHashMap<>();
            newUser.put(0, usernameTextField.getText());
            newUser.put(1, passwordTextField.getText());
            newUser.put(2, nameTextField.getText());
            newUser.put(3, addressTextField.getText());

            //if the username doesn't exist, create user
            if (!database.isUsernameDuplicate(username)) {
                //create user in the database.
                frame.dispose();
                doctorSelection(username, password, newUser);
            }
        });

        goBackButton.addActionListener(e -> {
            frame.dispose();
            loginInterface();
        });
        frame.setVisible(true);
    }

//---------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * The login part of the project.
     *
     * @author max
     */
    public void loginInterface() {
        frame = new JFrame("Login");

        // TODO: Think about changing the size of the window.
        // TODO: Also think about changing the look of the whole thing because it's ugly. Doesn't give extra marks.
        frame.setSize(400, 300);
        frame.setLayout(new GridBagLayout());

        GridBagConstraints constraint = new GridBagConstraints();
        constraint.gridx = 0;
        constraint.gridy = 0;

        JLabel label = new JLabel("Login: ");
        frame.add(label, constraint);

        constraint.gridx = 1;
        usernameTextField = new JTextField(5);
        frame.add(usernameTextField, constraint);

        constraint.gridx = 2;
        passwordTextField = new JPasswordField(5);
        frame.add(passwordTextField, constraint);

        constraint.gridx = 3;
        JButton loginButton = new JButton("Login");
        frame.add(loginButton, constraint);

        GridBagConstraints signUpLabelConstraint = new GridBagConstraints();
        signUpLabelConstraint.gridx = -1;
        signUpLabelConstraint.gridy = 2;
        signUpLabelConstraint.gridwidth = 3;
        signUpLabelConstraint.anchor = GridBagConstraints.WEST;
        JLabel signUpLabel = new JLabel("Sign up here: ");
        frame.add(signUpLabel, signUpLabelConstraint);

        GridBagConstraints signUpButtonConstraint = new GridBagConstraints();
        signUpButtonConstraint.gridx = -1;
        signUpButtonConstraint.gridy = 3;
        signUpButtonConstraint.gridwidth = 3;
        signUpButtonConstraint.anchor = GridBagConstraints.WEST;
        JButton signupButton = new JButton("Sign up");
        frame.add(signupButton, signUpButtonConstraint);

        tryAgainLabel = new JLabel("Try again");
        GridBagConstraints tryAgainLabelConstraint = new GridBagConstraints();
        tryAgainLabelConstraint.gridx = 3;
        tryAgainLabelConstraint.gridy = 2;

        signupButton.addActionListener(e -> {
            frame.dispose();
            signupInterface();
        });

        //once the login button is pressed its gonna check the username and password entered against the db
        loginButton.addActionListener(event -> {
            String username = usernameTextField.getText();
            String password = passwordTextField.getText();
            DBManager database = new DBManager();
            System.out.println(username);
            System.out.println(password);

            //if the username and password match the login frame is disposed and the profile one is formed.
            if (username.length() <= 0 || password.length() <= 0) {
                JOptionPane.showMessageDialog(frame, "Username and/or Password required", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                //if the username and password match the login frame is disposed and the profile one is formed.
                if (database.isUserPresent(username, password)) {
                    HashMap<String, Object> u = database.getUserInfo(username, password);
                    database.addLog((String) u.get("pid"), "Logged in");
                    frame.dispose();
                    Profile pr = new Profile();
                    pr.openProfile(username, password);
                } else {
                    frame.add(tryAgainLabel, tryAgainLabelConstraint);
                    frame.revalidate();
                    frame.repaint();
                    System.out.println("try again");
                    usernameTextField.setText("");
                    passwordTextField.setText("");
                }
            }
        });

        //go to the sign-up window
        signupButton.addActionListener(e -> {
            frame.dispose();
            signupInterface();
        });

        frame.setVisible(true);
    }

//---------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * The interface for the doctor selection process during the signup process.
     *
     * @param username the patient's username.
     * @param password the patient's password.
     * @param newUser  all data about the patient.
     * @author josh, max
     */
    public void doctorSelection(String username, String password, LinkedHashMap<Integer, String> newUser) {
        frame = new JFrame("Select Doctor");
        frame.setSize(400, 300);
        frame.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        // Add doctors from the database to the list
        List<String> doctorNames = DBManager.getAllDoctorNames();
        String[] doctors = doctorNames.toArray(new String[0]);
        JList<String> doctorList = new JList<>(doctors);
        JScrollPane scrollPane = new JScrollPane(doctorList);

        // List model
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        frame.add(scrollPane, gbc);

        JButton selectButton = new JButton("Choose doctor and login");

        // Select button model
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weighty = 0.0;
        frame.add(selectButton, gbc);

        // Select button Action
        selectButton.addActionListener(e -> {
            String selectedDoctor = doctorList.getSelectedValue();

            //add user to the db using the things they inputted in the signup window
            DBManager db = new DBManager();
            String u = newUser.get(0);
            String p = newUser.get(1);
            String name = newUser.get(2);
            String address = newUser.get(3);
            db.addPatient(u, p, name, address);

            //update patient's assigned doctor id in the db.
            HashMap<String, Object> user = db.getUserInfo(username, password);
            String pid = (String) user.get("pid");
            db.updateAssignedDoctorId(pid, selectedDoctor);

            //add a log and the welcome message.
            db.addLog(pid, "Signed up");
            //"Welcome " + userInformation.get("name") + ", you are now signed up with dr. " + getDoctorFullName(did);
            db.addMessage(user, "Welcome " + user.get("name") + ", you are now signed up with dr. " + selectedDoctor, (String) user.get("pid"));

            frame.dispose();
            Profile pr = new Profile();
            pr.openProfile(username, password);
        });
        frame.setVisible(true);
    }
}
