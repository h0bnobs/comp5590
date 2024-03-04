package src;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
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
        passwordTextField = new JTextField(5);
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
                if (database.checkUserLogin(username, password)) {
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
    private void signupInterface() {
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
                DBManager database = new DBManager();

                //todo: get the doctor id number from the doctor selection

                //if the username doesn't exist, create user
                if (!database.checkSignUpUsername(username)) {
                    //create user in the database.
                    database.addUser(username, password, name, address);
                    frame.dispose();
                    doctorSelection(username, password);

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
     * @author max
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

        JLabel welcomeMessage = new JLabel("Hello, " + userInformation.get("name"));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        frame.add(welcomeMessage, gbc);

        // messagelist
        // TODO: I guess the messages are gonna be stored on the db so we need them from the db
        String[] messages = new String[5];
        messages[0] = database.generateSignupMessage(userInformation);
        JList<String> messageList = new JList<>(messages);
        JScrollPane scrollPane = new JScrollPane(messageList);
        scrollPane.setPreferredSize(new Dimension(580, 200));
        scrollPane.setBorder(BorderFactory.createTitledBorder("Messages"));
        panel.add(scrollPane, BorderLayout.CENTER);

        //panel
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        //gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        panel.setBorder(BorderFactory.createLineBorder(Color.RED));
        frame.add(panel, gbc);

        //logout button
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        JButton logoutButton = new JButton("Logout");
        frame.add(logoutButton, gbc);

        //logout button
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                loginInterface();
            }
        });

        frame.setVisible(true);
    }

//---------------------------------------------------------------------------------------------------------------------------------------------

    private void doctorSelection(String username, String password) {
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

                //update patient's assigned doctor id in the db.
                DBManager db = new DBManager();
                HashMap<String, Object> user = db.getUserInfo(username, password);
                String pid = (String) user.get("pid");

                db.updateAssignedDoctorId(pid, selectedDoctor);
                frame.dispose();
                openProfile(username, password);

            }
        });

        frame.setVisible(true);
    }


}
