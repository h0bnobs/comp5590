package src;

import java.awt.*;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

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

        //messagelist
        //TODO: i guess the messages are gonna be stored on the db so we need them from the db
        String[] messages = {"Message 1", "Message 2", "Message 3"};
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

            }
        });

        frame.setVisible(true);
    }

    /**
     * The login part of the project.
     *
     * @author max
     */
    private void loginInterface() {
        frame = new JFrame("Login");

        //TODO: Think about changing the size of the window.
        //TODO: Also think about changing the look of the whole thing because it's ugly. Doesn't give extra marks.
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
        JButton loginButton = new JButton("Enter");
        frame.add(loginButton, constraint);

        //once the button is pressed its gonna check the username and password entered against the db
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
                    tryAgainLabel = new JLabel("Try again");
                    GridBagConstraints constraint = new GridBagConstraints();
                    constraint.gridx = 1;
                    constraint.gridy = 2;
                    frame.add(tryAgainLabel, constraint);
                    frame.revalidate();
                    frame.repaint();
                    System.out.println("try again");
                    usernameTextField.setText("");
                    passwordTextField.setText("");

                }


            }
        });

        frame.setVisible(true);
    }
}