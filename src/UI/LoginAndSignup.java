package src.UI;

import src.Database.DatabaseInteract;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.HashMap;

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
    public void signupInterface() {
        frame = new JFrame("Sign up");
        frame.setSize(400, 300);
        frame.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        frame.add(new JLabel("Enter your name: "), gbc);

        gbc.gridx = 1;
        JTextField nameTextField = new JTextField(15);
        frame.add(nameTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        frame.add(new JLabel("Enter your address: "), gbc);

        gbc.gridx = 1;
        JTextField addressTextField = new JTextField(15);
        frame.add(addressTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        frame.add(new JLabel("Enter a username: "), gbc);

        gbc.gridx = 1;
        usernameTextField = new JTextField(15);
        frame.add(usernameTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        frame.add(new JLabel("Enter a password: "), gbc);

        gbc.gridx = 1;
        passwordTextField = new JPasswordField(15);
        frame.add(passwordTextField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton signupButton = new JButton("Sign Up");
        frame.add(signupButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        JButton goBackButton = new JButton("Go Back");
        frame.add(goBackButton, gbc);

        goBackButton.addActionListener(e -> {
            frame.dispose();
            loginInterface();
        });

        signupButton.addActionListener(e -> {
            String username = usernameTextField.getText();
            String password = passwordTextField.getText();
            String name = nameTextField.getText();
            String address = addressTextField.getText();

            if (!gui.validateName(name)) {
                JOptionPane.showMessageDialog(frame, "Name cannot contain numerical values", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!gui.validatePassword(password)) {
                JOptionPane.showMessageDialog(frame, "Password must be at least 8 characters long with upper, lower, numeric, and special characters.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            DatabaseInteract database = new DatabaseInteract();
            if (!database.isUsernameDuplicate(username)) {
                frame.dispose();
                doctorSelection(username, password, name, address);
            }
        });

        frame.setVisible(true);
    }

//---------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * The login window.
     *
     * @author max
     */
    public void loginInterface() {
        frame = new JFrame("Login");
        frame.setSize(400, 200);
        frame.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JButton weatherButton = new JButton("Grab weather");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        frame.add(weatherButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 0;
        frame.add(new JLabel("Username: "), gbc);

        gbc.gridx = 1;
        usernameTextField = new JTextField(15);
        frame.add(usernameTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        frame.add(new JLabel("Password: "), gbc);

        gbc.gridx = 1;
        passwordTextField = new JPasswordField(15);
        frame.add(passwordTextField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton loginButton = new JButton("Login");
        frame.add(loginButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JButton signupButton = new JButton("Sign Up");
        frame.add(signupButton, gbc);

        loginButton.addActionListener(e -> {
            String username = usernameTextField.getText();
            String password = passwordTextField.getText();
            DatabaseInteract database = new DatabaseInteract();

            if (database.isUserPresent(username, password)) {
                frame.dispose();
                Profile profile = new Profile();
                profile.openProfile(username, password);
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        weatherButton.addActionListener(e -> {
            System.out.println(WeatherApp.run());
        });

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
     * @param name     the patient's name.
     * @param address  the patient's address.
     * @author josh, max
     */
    public void doctorSelection(String username, String password, String name, String address) {
        frame = new JFrame("Select Doctor");
        frame.setSize(400, 300);
        frame.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        List<String> doctorNames = DatabaseInteract.getAllDoctorNames();
        String[] doctors = doctorNames.toArray(new String[0]);
        JList<String> doctorList = new JList<>(doctors);
        JScrollPane scrollPane = new JScrollPane(doctorList);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        frame.add(scrollPane, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weighty = 0.0;
        JButton selectButton = new JButton("Select Doctor");
        frame.add(selectButton, gbc);

        selectButton.addActionListener(e -> {
            String selectedDoctor = doctorList.getSelectedValue();
            DatabaseInteract database = new DatabaseInteract();

            database.addPatient(username, password, name, address);
            HashMap<String, Object> user = database.getUserInfo(username, password);
            String pid = (String) user.get("pid");
            database.updateAssignedDoctorId(pid, selectedDoctor);
            database.addLog(pid, "Signed up");
            database.addMessage(user, "Welcome " + user.get("name") + ", you are now signed up with Dr. " + selectedDoctor, (String) user.get("pid"));

            frame.dispose();
            Profile profile = new Profile();
            profile.openProfile(username, password);
        });

        frame.setVisible(true);
    }
}
