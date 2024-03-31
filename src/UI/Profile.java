package src.UI;

import src.Database.DBManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;

public class Profile {

    private JFrame frame;
    private final GUI gui = new GUI();


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

}
