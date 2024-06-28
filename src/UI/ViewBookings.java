package src.UI;

import src.Database.DatabaseInteract;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Windows where the user can view their previous and future bookings.
 */
public class ViewBookings {

    private JFrame frame;

    /**
     * The window where users enter a month and year and can see all their previous booked appointments.
     *
     * @param userInformation The current user's information.
     * @author max
     */
    public void viewPreviousAppointmentsInterface(HashMap<String, Object> userInformation) {
        frame = new JFrame("View future appointments");
        frame.setSize(400, 450);
        frame.setLayout(new GridBagLayout());
        DatabaseInteract dbManager = new DatabaseInteract();

        //message
        JLabel message = new JLabel("Your previous bookings:");
        message.setFont(message.getFont().deriveFont(13.0f));
        GridBagConstraints welcomeMessageConstraints = new GridBagConstraints();
        welcomeMessageConstraints.gridx = 0;
        welcomeMessageConstraints.gridy = 0;
        welcomeMessageConstraints.anchor = GridBagConstraints.NORTH;
        frame.add(message, welcomeMessageConstraints);

        //panel
        GridBagConstraints panelConstraints = new GridBagConstraints();
        panelConstraints.gridx = 0;
        panelConstraints.gridy = 2;
        JPanel panel = new JPanel();
        frame.add(panel, panelConstraints);

        List<HashMap<String, Object>> appointments = dbManager.getAllPreviousAppointments((String) userInformation.get("pid"));

        //list of appointments
        JList<String> appointmentsList = new JList<>();
        appointmentsList.setFixedCellHeight(30);
        appointmentsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (HashMap<String, Object> appointment : appointments) {
            //String visitDetails = appointment.get("visit_details") != null ? (String) appointment.get("visit_details") : "Doctor left no notes";
            //String prescriptions = appointment.get("prescriptions") != null ? (String) appointment.get("prescriptions") : "Nothing was prescribed";
            String element = (String) appointment.get("date_and_time");
            listModel.addElement(element);
        }
        appointmentsList.setModel(listModel);

        //scroll pane
        JScrollPane scrollPane = new JScrollPane(appointmentsList);
        scrollPane.setPreferredSize(new Dimension(250, 250));
        scrollPane.setBorder(BorderFactory.createTitledBorder("Previous appointments:"));
        GridBagConstraints scrollPaneConstraints = new GridBagConstraints();
        scrollPaneConstraints.gridx = 0;
        scrollPaneConstraints.gridy = 2;
        scrollPaneConstraints.anchor = GridBagConstraints.NORTH;
        scrollPaneConstraints.weighty = 10.0;
        frame.add(scrollPane, scrollPaneConstraints);

        //go back
        GridBagConstraints goBackButtonConstraint = new GridBagConstraints();
        goBackButtonConstraint.gridy = 5;
        goBackButtonConstraint.gridx = 0;
        JButton goBackButton = new JButton("Go back to profile");
        frame.add(goBackButton, goBackButtonConstraint);

        //next button
        GridBagConstraints nextButtonConstraints = new GridBagConstraints();
        nextButtonConstraints.gridx = 0;
        nextButtonConstraints.gridy = 4;
        nextButtonConstraints.anchor = GridBagConstraints.CENTER;
        JButton nextButton = new JButton("Get visit details");
        frame.add(nextButton, nextButtonConstraints);

        nextButton.addActionListener(e -> {
            if (appointmentsList.getSelectedValue() != null) {
                dbManager.addLog((String) userInformation.get("pid"), "Looked at their previous appointment on " + appointmentsList.getSelectedValue());
            }
            //System.out.println(appointmentsList.getSelectedValue());
            for (HashMap<String, Object> appointment : appointments) {
                if (appointment.get("date_and_time") == appointmentsList.getSelectedValue()) {
                    frame.dispose();

                    frame = new JFrame("Visit details");
                    frame.setSize(400, 450);
                    frame.setLayout(new GridBagLayout());

                    String prescriptions = (appointment.get("prescriptions") != null) ? (String) appointment.get("prescriptions") : "Nothing was prescribed.";
                    String visitDetails = (appointment.get("visit_details") != null) ? (String) appointment.get("visit_details") : "Your doctor didn't make any notes.";

                    JTextArea text = new JTextArea();
                    text.setLineWrap(true);
                    text.setLineWrap(true);
                    text.setEditable(false);
                    text.setText("You were prescribed: \n" + prescriptions + "\n\nYour doctor's notes were:\n" + visitDetails);

                    JScrollPane scrollPane1 = new JScrollPane(text);
                    scrollPane1.setPreferredSize(new Dimension(350, 350));
                    GridBagConstraints scrollConstraints = new GridBagConstraints();
                    scrollConstraints.gridy = 0;
                    scrollConstraints.anchor = GridBagConstraints.NORTH;
                    frame.add(scrollPane1, scrollConstraints);

                    //go back
                    GridBagConstraints goBackButtonConstraint1 = new GridBagConstraints();
                    goBackButtonConstraint1.gridy = 2;
                    goBackButtonConstraint1.gridx = 0;
                    JButton goBackButton1 = new JButton("Go Back");
                    frame.add(goBackButton1, goBackButtonConstraint1);

                    //profile button
                    GridBagConstraints goToProfileButtonConstraints = new GridBagConstraints();
                    goToProfileButtonConstraints.gridx = 0;
                    goToProfileButtonConstraints.gridy = 4;
                    goToProfileButtonConstraints.anchor = GridBagConstraints.CENTER;
                    JButton profileButton = new JButton("Go to profile");
                    frame.add(profileButton, goToProfileButtonConstraints);

                    profileButton.addActionListener(e1 -> {
                        frame.dispose();
                        Profile pr = new Profile();
                        pr.openProfile((String) userInformation.get("username"), (String) userInformation.get("password"));
                    });

                    goBackButton1.addActionListener(e12 -> {
                        frame.dispose();
                        viewPreviousAppointmentsInterface(userInformation);
                    });
                    frame.setVisible(true);
                } else if (appointmentsList.getSelectedValue() == null) {
                    JOptionPane.showMessageDialog(frame, "Please select an appointment.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        });

        goBackButton.addActionListener(e -> {
            frame.dispose();
            Profile pr = new Profile();
            pr.openProfile((String) userInformation.get("username"), (String) userInformation.get("password"));
        });

        frame.setVisible(true);
    }


    /**
     * The window where users enter a month and year and can see all their future booked appointments.
     *
     * @param userInformation The current user's information.
     * @author max
     */
    public void viewFutureAppointmentsInterface(HashMap<String, Object> userInformation) {
        frame = new JFrame("View future appointments");
        frame.setSize(400, 300);
        frame.setLayout(new GridBagLayout());
        DatabaseInteract dbManager = new DatabaseInteract();

        //message
        JLabel message = new JLabel("Enter a month and year to search for bookings:");
        message.setFont(message.getFont().deriveFont(13.0f));
        GridBagConstraints welcomeMessageConstraints = new GridBagConstraints();
        welcomeMessageConstraints.gridx = 0;
        welcomeMessageConstraints.gridy = 0;
        welcomeMessageConstraints.anchor = GridBagConstraints.NORTH;
        frame.add(message, welcomeMessageConstraints);

        //panel
        GridBagConstraints panelConstraints = new GridBagConstraints();
        panelConstraints.gridx = 0;
        panelConstraints.gridy = 2;
        JPanel panel = new JPanel();
        frame.add(panel, panelConstraints);

        //date logic
        LocalDate fullDate = LocalDate.now();
        int currentMonth = fullDate.getMonthValue();

        String[] months = new String[12];
        for (int i = 1; i <= 12; i++) {
            months[i - 1] = String.valueOf(i);
        }

        String[] years = new String[2];
        for (int i = 0; i < 2; i++) {
            years[i] = String.valueOf(fullDate.getYear() + i);
        }

        JComboBox<String> monthComboBox = new JComboBox<>(months);
        monthComboBox.setSelectedIndex(currentMonth - 1);
        JComboBox<String> yearComboBox = new JComboBox<>(years);
        panel.add(monthComboBox);
        panel.add(yearComboBox);

        //'next' button
        GridBagConstraints nextButtonConstraints = new GridBagConstraints();
        nextButtonConstraints.gridx = 0;
        nextButtonConstraints.gridy = 4;
        nextButtonConstraints.anchor = GridBagConstraints.CENTER;
        JButton nextButton = new JButton("Next");
        frame.add(nextButton, nextButtonConstraints);

        //go back
        GridBagConstraints goBackButtonConstraint = new GridBagConstraints();
        goBackButtonConstraint.gridy = 5;
        goBackButtonConstraint.gridx = 0;
        JButton goBackButton = new JButton("Go Back");
        frame.add(goBackButton, goBackButtonConstraint);

        nextButton.addActionListener(e -> {
            dbManager.addLog((String) userInformation.get("pid"), "Viewed their future appointments");
            StringBuilder monthAndYear = new StringBuilder();
            String month = "";
            if (Objects.equals(monthComboBox.getSelectedItem(), "10") || Objects.equals(monthComboBox.getSelectedItem(), "11")
                    || Objects.equals(monthComboBox.getSelectedItem(), "12")) {
                month = (String) monthComboBox.getSelectedItem();
            } else {
                month = "0" + monthComboBox.getSelectedItem();
            }
            monthAndYear.append(yearComboBox.getSelectedItem()).append("-").append(month);
            List<HashMap<String, Object>> appointments = dbManager.getFutureAppointmentsByMonth((String) userInformation.get("pid"),
                    (String) userInformation.get("assigned_doctor_id"), monthAndYear.toString());

            if (appointments.isEmpty()) {
                int option = JOptionPane.showOptionDialog(null, "You have no booked appointments this month.", "No Booked Appointments", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, new String[]{"OK"}, "OK");
                if (option == JOptionPane.OK_OPTION) {
                    frame.dispose();
                    viewFutureAppointmentsInterface(userInformation);
                }
            } else {
                frame.dispose();
                frame = new JFrame("View future appointments");
                frame.setSize(400, 450);
                frame.setLayout(new GridBagLayout());

                //panel
                GridBagConstraints panelConstraints1 = new GridBagConstraints();
                panelConstraints1.gridx = 0;
                panelConstraints1.gridy = 2;
                JPanel panel1 = new JPanel();
                frame.add(panel1, panelConstraints1);

                //list of appointments
                JList<String> appointmentsList = new JList<>();
                appointmentsList.setFixedCellHeight(30);
                appointmentsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

                DefaultListModel<String> listModel = new DefaultListModel<>();
                for (HashMap<String, Object> appointment : appointments) {
                    //String visitDetails = appointment.get("visit_details") != null ? (String) appointment.get("visit_details") : "Doctor left no notes";
                    //String prescriptions = appointment.get("prescriptions") != null ? (String) appointment.get("prescriptions") : "Nothing was prescribed";
                    String element = (String) appointment.get("date_and_time");
                    listModel.addElement(element);
                }
                appointmentsList.setModel(listModel);

                //scroll pane
                JScrollPane scrollPane = new JScrollPane(appointmentsList);
                scrollPane.setPreferredSize(new Dimension(250, 250));
                scrollPane.setBorder(BorderFactory.createTitledBorder("Booked appointments:"));
                GridBagConstraints scrollPaneConstraints = new GridBagConstraints();
                scrollPaneConstraints.gridx = 0;
                scrollPaneConstraints.gridy = 2;
                scrollPaneConstraints.anchor = GridBagConstraints.NORTH;
                scrollPaneConstraints.weighty = 10.0;
                frame.add(scrollPane, scrollPaneConstraints);

                //back to profile
                GridBagConstraints goBackToProfileConstraint = new GridBagConstraints();
                goBackToProfileConstraint.gridy = 5;
                goBackToProfileConstraint.gridx = 0;
                JButton goBackToProfileButton = new JButton("Back to profile");
                frame.add(goBackToProfileButton, goBackToProfileConstraint);

                goBackToProfileButton.addActionListener(e1 -> {
                    frame.dispose();
                    Profile pr = new Profile();
                    pr.openProfile((String) userInformation.get("username"), (String) userInformation.get("password"));
                });

                frame.setVisible(true);
            }
        });

        goBackButton.addActionListener(e -> {
            frame.dispose();
            Profile pr = new Profile();
            pr.openProfile((String) userInformation.get("username"), (String) userInformation.get("password"));
        });

        frame.setVisible(true);
    }

}
