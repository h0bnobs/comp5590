package src.UI;

import src.Database.DBManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;


public class GUI {
    private JFrame frame;
    private JLabel tryAgainLabel;
    private JTextField usernameTextField;
    private JTextField passwordTextField;

    public static void main(String[] args) {
        LoginAndSignup ui = new LoginAndSignup();
        ui.loginInterface();
    }

//---------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * The profile section of the project.
     *
     * @param username The username of the current user.
     * @author max, josh
     */
    public void openProfile(String username, String password) {
        frame = new JFrame("Profile");
        frame.setSize(600, 700);
        frame.setLayout(new GridBagLayout());
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

        //TODO: make it so that newest messages appear at the top of the list.
        //list of messages
        //gets all the user's messages
        List<String> userMessages = database.getUserMessages((String) userInformation.get("pid"));

        JList<String> messageList = new JList<>();
        messageList.setFixedCellHeight(30);
        messageList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (int i = userMessages.size() - 1; i >= 0; i--) {
            if (userMessages.get(i) != null) {
                listModel.addElement("- " + userMessages.get(i));
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
        panelConstraints.gridy = 1;
        panelConstraints.anchor = GridBagConstraints.NORTHWEST;
        panelConstraints.weightx = 1.0;
        panelConstraints.weighty = 1.0;
        frame.add(panel, panelConstraints);

        //logout button
        GridBagConstraints logoutButtonConstraints = new GridBagConstraints();
        logoutButtonConstraints.gridx = 0;
        logoutButtonConstraints.gridy = 7;
        logoutButtonConstraints.anchor = GridBagConstraints.CENTER;
        JButton logoutButton = new JButton("Logout");
        frame.add(logoutButton, logoutButtonConstraints);

        //change doctor button
        GridBagConstraints changeDoctorButtonConstraints = new GridBagConstraints();
        changeDoctorButtonConstraints.gridx = 0;
        changeDoctorButtonConstraints.gridy = 2;
        changeDoctorButtonConstraints.anchor = GridBagConstraints.CENTER;
        JButton changeDoctorButton = new JButton("Change Doctor");
        frame.add(changeDoctorButton, changeDoctorButtonConstraints);

        //appointments button
        GridBagConstraints bookAppointmentButtonConstraints = new GridBagConstraints();
        bookAppointmentButtonConstraints.gridx = 0;
        bookAppointmentButtonConstraints.gridy = 3;
        bookAppointmentButtonConstraints.anchor = GridBagConstraints.CENTER;
        JButton bookAppointmentButton = new JButton("Book Appointment");
        frame.add(bookAppointmentButton, bookAppointmentButtonConstraints);

        //view future bookings button
        GridBagConstraints viewFutureAppointmentsConstraints = new GridBagConstraints();
        viewFutureAppointmentsConstraints.gridx = 0;
        viewFutureAppointmentsConstraints.gridy = 4;
        viewFutureAppointmentsConstraints.anchor = GridBagConstraints.CENTER;
        JButton viewFutureAppointmentsButton = new JButton("View Future Bookings");
        frame.add(viewFutureAppointmentsButton, viewFutureAppointmentsConstraints);

        //view previous bookings button
        GridBagConstraints viewPreviousAppointmentsConstraints = new GridBagConstraints();
        viewPreviousAppointmentsConstraints.gridx = 0;
        viewPreviousAppointmentsConstraints.gridy = 5;
        viewPreviousAppointmentsConstraints.anchor = GridBagConstraints.CENTER;
        JButton viewPreviousAppointmentsButton = new JButton("View Previous Bookings");
        frame.add(viewPreviousAppointmentsButton, viewPreviousAppointmentsConstraints);

        //reschedule a booking button
        GridBagConstraints rescheduleAppointmentConstraints = new GridBagConstraints();
        rescheduleAppointmentConstraints.gridx = 0;
        rescheduleAppointmentConstraints.gridy = 6;
        rescheduleAppointmentConstraints.anchor = GridBagConstraints.CENTER;
        JButton rescheduleAppointmentButton = new JButton("Reschedule a Booking");
        frame.add(rescheduleAppointmentButton, rescheduleAppointmentConstraints);

        //logout button action
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                database.addLog((String) userInformation.get("pid"), "Logged out");
                frame.dispose();
                LoginAndSignup l = new LoginAndSignup();
                l.loginInterface();
            }
        });

        //change doctor button action
        changeDoctorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                database.addLog((String) userInformation.get("pid"), "Started to change doctor");
                frame.dispose();
                ChangeDoctor c = new ChangeDoctor();
                c.changeDoctorInterface(userInformation);
            }
        });

        //book appointment button action
        bookAppointmentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                BookAppointment b = new BookAppointment();
                b.bookAppointmentInterface(userInformation);
            }
        });

        viewFutureAppointmentsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                ViewBookings v = new ViewBookings();
                v.viewFutureAppointmentsInterface(userInformation);
            }
        });

        viewPreviousAppointmentsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                ViewBookings v = new ViewBookings();
                v.viewPreviousAppointmentsInterface(userInformation);
            }
        });

        rescheduleAppointmentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                RescheduleAppointment r = new RescheduleAppointment();
                r.rescheduleAppointmentInterface(userInformation);
                //rescheduleAppointmentInterface(userInformation);
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
                db.addMessage(user, "Welcome " + user.get("name") + ", you are now signed up with dr. " + selectedDoctor, (String) user.get("pid"));

                frame.dispose();
                openProfile(username, password);

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
