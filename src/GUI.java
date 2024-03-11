package src;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.swing.*;


public class GUI {
    private JFrame frame;
    private JLabel tryAgainLabel;
    private JTextField usernameTextField;
    private JTextField passwordTextField;

    public static void main(String[] args) {
        GUI ui = new GUI();
        ui.loginInterface();
    }

//---------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * The login part of the project.
     *
     * @author max
     */
    private void loginInterface() {
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

        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                signupInterface();
            }
        });

        //once the login button is pressed its gonna check the username and password entered against the db
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                String username = usernameTextField.getText();
                String password = passwordTextField.getText();
                DBManager database = new DBManager();
                System.out.println(username);
                System.out.println(password);


                //if the username and password match the login frame is disposed and the profile one is formed.
                if (username.length() <= 0 || password.length() <= 0) {
                    JOptionPane.showMessageDialog(frame, "Username and/or Password required", "Error", JOptionPane.ERROR_MESSAGE);
                    return; // Exit the method to prevent further execution
                } else {
                    //if the username and password match the login frame is disposed and the profile one is formed.
                    if (database.isUserPresent(username, password)) {
                        HashMap<String, Object> u = database.getUserInfo(username, password);
                        database.addLog((String) u.get("pid"), "Logged in");
                        frame.dispose();
                        openProfile(username, password);
                    } else {
                        frame.add(tryAgainLabel, tryAgainLabelConstraint);
                        frame.revalidate();
                        frame.repaint();
                        System.out.println("try again");
                        usernameTextField.setText("");
                        passwordTextField.setText("");
                    }
                }
            }
        });

        //go to the sign-up window
        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                signupInterface();
            }
        });

        frame.setVisible(true);
    }

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

//        JPasswordField pass = new JPasswordField(10);
//        pass.setBackground(Color.RED);
//        frame.add(pass);

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

//        GridBagConstraints signUpButtonConstraint = new GridBagConstraints();
//        signUpButtonConstraint.gridx = 0;
//        signUpButtonConstraint.gridy = 4;
//        JButton signupButton = new JButton("Sign up and login");
//        frame.add(signupButton, signUpButtonConstraint);

        //TODO: ADD A LIST OF DOCTORS FROM THE DB TO CHOOSE FROM.

        //check for existing user
        //if not found, add it to db and login.
        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameTextField.getText();
                String password = passwordTextField.getText();
                String name = nameTextField.getText();
                String address = addressTextField.getText();

                //Checks to make sure given name doesn't contain digits.
                if (!validateName(name)) {
                    JOptionPane.showMessageDialog(frame, "Your name cannot contain anything of numerical value", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                //Checks to make sure the given password is valid.
                if (!validatePassword(password)) {
                    JOptionPane.showMessageDialog(frame, "Password Must Contain: \n• Eight or more Characters long \n• One or more Uppercase characters \n• One or more Lowercase characters \n• One or more numerical characters \n• One or more special Character e.g @, ! or $", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                LinkedHashMap<Integer, String> newUser = new LinkedHashMap<>();
                newUser.put(0, usernameTextField.getText());
                newUser.put(1, passwordTextField.getText());
                newUser.put(2, nameTextField.getText());
                newUser.put(3, addressTextField.getText());

                DBManager database = new DBManager();

                //todo: get the doctor id number from the doctor selection

                //if the username doesn't exist, create user
                if (!database.isUsernameDuplicate(username)) {
                    //create user in the database.
                    frame.dispose();
                    doctorSelection(username, password, newUser);

                } else {
                    //TODO: GIVE A MESSAGE SAYING THE USERNAME ALREADY EXISTS.
                    //or just be lazy and say "try again" instead
                }

            }
        });

        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                loginInterface();
            }
        });


        frame.setVisible(true);
    }

//---------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * The profile section of the project.
     *
     * @param username The username of the current user.
     * @author max, josh
     */
    private void openProfile(String username, String password) {
        frame = new JFrame("Profile");
        frame.setSize(600, 700);
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        DBManager database = new DBManager();
        HashMap<String, Object> userInformation = database.getUserInfo(username, password);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        //welcome message
        JLabel welcomeMessage = new JLabel("Hello, " + userInformation.get("name"));
        welcomeMessage.setFont(welcomeMessage.getFont().deriveFont(20.0f));
        GridBagConstraints welcomeMessageConstraints = new GridBagConstraints();
        welcomeMessageConstraints.gridx = 0;
        welcomeMessageConstraints.gridy = 0;
        welcomeMessageConstraints.anchor = GridBagConstraints.CENTER;
        frame.add(welcomeMessage, welcomeMessageConstraints);

        //message list
        // TODO: I guess the messages are gonna be stored on the db so we need them from the db
        List<String> userMessages = database.getUserMessages((String) userInformation.get("pid"));

        JList<String> messageList = new JList<>();
        messageList.setFixedCellHeight(30);
        messageList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (String message : userMessages) {
            if (message != null) {
                listModel.addElement("• " + message);
            }
        }
        messageList.setModel(listModel);


        JScrollPane scrollPane = new JScrollPane(messageList);
        scrollPane.setPreferredSize(new Dimension(580, 150));
        scrollPane.setBorder(BorderFactory.createTitledBorder("Messages"));
        GridBagConstraints scrollPaneConstraints = new GridBagConstraints();
        scrollPaneConstraints.gridx = 0;
        scrollPaneConstraints.gridy = 1;
        scrollPaneConstraints.anchor = GridBagConstraints.NORTHWEST;
        frame.add(scrollPane, scrollPaneConstraints);

        //panel
        panel.setBorder(BorderFactory.createLineBorder(Color.RED));
        GridBagConstraints panelConstraints = new GridBagConstraints();
        panelConstraints.gridx = 0;
        panelConstraints.gridy = 2;
        panelConstraints.anchor = GridBagConstraints.NORTHWEST;
        panelConstraints.weightx = 1.0;
        panelConstraints.weighty = 1.0;
        frame.add(panel, panelConstraints);

        //logout button
        GridBagConstraints logoutButtonConstraints = new GridBagConstraints();
        logoutButtonConstraints.gridx = 0;
        logoutButtonConstraints.gridy = 3;
        logoutButtonConstraints.anchor = GridBagConstraints.CENTER;
        JButton logoutButton = new JButton("Logout");
        frame.add(logoutButton, logoutButtonConstraints);

        //change doctor
        GridBagConstraints changeDoctorButtonConstraints = new GridBagConstraints();
        changeDoctorButtonConstraints.gridx = 0;
        changeDoctorButtonConstraints.gridy = 4;
        changeDoctorButtonConstraints.anchor = GridBagConstraints.CENTER;
        JButton changeDoctorButton = new JButton("Change Doctor");
        frame.add(changeDoctorButton, changeDoctorButtonConstraints);

        //logout button
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                database.addLog((String) userInformation.get("pid"), "Logged out");
                frame.dispose();
                loginInterface();
            }
        });

        changeDoctorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                database.addLog((String) userInformation.get("pid"), "Started to change doctor");
                frame.dispose();
                changeDoctorInterface(userInformation);
            }
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
    private void doctorSelection(String username, String password, LinkedHashMap<Integer, String> newUser) {
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
        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
                db.addMessage(user, "Welcome " + user.get("name") + ", you are now signed up with dr. " + selectedDoctor);

                frame.dispose();
                openProfile(username, password);

            }
        });

        frame.setVisible(true);
    }

//---------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Lets the user change their current doctor to a different one.
     *
     * @param userInformation the current user.
     * @author max
     */
    public void changeDoctorInterface(HashMap<String, Object> userInformation) {
        frame = new JFrame("Change your doctor");
        frame.setSize(400, 300);
        frame.setLayout(new GridBagLayout());
        DBManager dbManager = new DBManager();

        String currentDoctorName = dbManager.getDoctorFullName((String) userInformation.get("assigned_doctor_id"));

        //add the doctors from the database to the list
        List<String> doctorNames = DBManager.getAllDoctorNames();

        //remove the patient's current doctor from the list of new doctor's to select
        doctorNames.removeIf(doctor -> doctor.equals(currentDoctorName));

        String[] doctors = doctorNames.toArray(new String[0]);
        JList<String> doctorList = new JList<>(doctors);

        //list model
        JScrollPane scrollPane = new JScrollPane(doctorList);
        scrollPane.setPreferredSize(new Dimension(380, 165));
        scrollPane.setBorder(BorderFactory.createTitledBorder("Select Doctor"));
        GridBagConstraints scrollPaneConstraint = new GridBagConstraints();
        scrollPaneConstraint.anchor = GridBagConstraints.NORTHWEST;
        scrollPaneConstraint.gridwidth = 2;
        frame.add(scrollPane, scrollPaneConstraint);

        //change doctor button
        GridBagConstraints changeDoctorButtonConstraint = new GridBagConstraints();
        changeDoctorButtonConstraint.gridy = 1;
        changeDoctorButtonConstraint.gridx = 1;
        changeDoctorButtonConstraint.gridwidth = 1;
        changeDoctorButtonConstraint.anchor = GridBagConstraints.CENTER;
        changeDoctorButtonConstraint.fill = GridBagConstraints.NONE;
        changeDoctorButtonConstraint.weighty = 0.0;
        JButton changeDoctorButton = new JButton("Change your doctor");
        frame.add(changeDoctorButton, changeDoctorButtonConstraint);

        //go back
        GridBagConstraints goBackButtonConstraint = new GridBagConstraints();
        goBackButtonConstraint.gridy = 1;
        goBackButtonConstraint.gridx = 0;
        goBackButtonConstraint.gridwidth = 1;
        goBackButtonConstraint.anchor = GridBagConstraints.CENTER;
        goBackButtonConstraint.fill = GridBagConstraints.NONE;
        goBackButtonConstraint.weighty = 0.0;
        JButton goBackButton = new JButton("Go Back");
        frame.add(goBackButton, goBackButtonConstraint);

        //TODO add a go back button + log for when they go back.

        changeDoctorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String pass = JOptionPane.showInputDialog(frame, "Please enter your password to verify:");

                if (pass == null || !pass.equals(userInformation.get("password"))) {
                    JOptionPane.showMessageDialog(frame, "Incorrect password, try again.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String selectedDoctor = doctorList.getSelectedValue();

                if (selectedDoctor == null) {
                    JOptionPane.showMessageDialog(frame, "Please select a doctor from the list, or go back.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                //update patient's assigned doctor id in the db.
                String pid = (String) userInformation.get("pid");
                dbManager.updateAssignedDoctorId(pid, selectedDoctor);
                dbManager.addLog(pid, "Changed doctor");
                dbManager.addMessage(userInformation, "You changed your doctor to: dr. " + selectedDoctor);
                frame.dispose();
                openProfile((String) userInformation.get("username"), (String) userInformation.get("password"));
            }
        });

        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dbManager.addLog((String) userInformation.get("pid"), "Cancelled doctor change");
                frame.dispose();
                openProfile((String) userInformation.get("username"), (String) userInformation.get("password"));
            }
        });

        frame.setVisible(true);
    }

//---------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Makes sure that the name inputted by the user in the signup window doesn't contain digits
     *
     * @param name
     * @return false if the name contains things it isn't meant to.
     * @author josh
     */
    public boolean validateName(String name) {
        return !name.matches(".*\\d.*");
    }

//---------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Makes sure that the password inputted by the user in the signup window meets certain requirements.
     *
     * @param password
     * @return True if the password meets all requirements.
     * @author josh
     */
    public boolean validatePassword(String password) {
        int upperCaseChars = 0;
        int lowerCaseChars = 0;
        int numberChars = 0;
        int specialChars = 0;

        if (password.length() > 8 && password.length() < 45) {
            for (int i = 0; i < password.length(); i++) {
                char passwordChar = password.charAt(i);
                if (Character.isUpperCase(passwordChar)) {
                    upperCaseChars++;
                }
                if (Character.isLowerCase(passwordChar)) {
                    lowerCaseChars++;
                }
                if (Character.isDigit(passwordChar)) {
                    numberChars++;
                }
                if (passwordChar >= 33 && passwordChar <= 46 || passwordChar == 64) {
                    specialChars++;
                }
            }
        }
        return upperCaseChars >= 1 && lowerCaseChars >= 1 && numberChars >= 1 && specialChars >= 1;
    }

}
