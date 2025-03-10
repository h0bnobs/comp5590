package src.UI;

import src.Database.DatabaseInteract;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The window where a user can reschedule their appointments.
 */
public class RescheduleAppointment {

    private JFrame frame;

    /**
     * The window where the user can reschedule an appointment that they have booked by entering date and time.
     *
     * @param userInformation The current user.
     * @author max
     */
    public void rescheduleAppointmentInterface(HashMap<String, Object> userInformation) {
        frame = new JFrame("Reschedule an appointment");
        frame.setSize(400, 300);
        frame.setLayout(new GridBagLayout());
        DatabaseInteract dbManager = new DatabaseInteract();

        //message
        JLabel message = new JLabel("What date is your appointment?");
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

        //date logic
        LocalDate fullDate = LocalDate.now();
        LocalDate defaultDay = fullDate.plusDays(1);
        int currentDay = defaultDay.getDayOfMonth();
        int currentMonth = fullDate.getMonthValue();
        int currentYear = fullDate.getYear();

        String[] days = new String[31];
        for (int i = 1; i <= 31; i++) {
            days[i - 1] = String.format("%02d", i);
        }

        String[] months = new String[12];
        for (int i = 1; i <= 12; i++) {
            months[i - 1] = String.format("%02d", i);
        }

        String[] years = new String[3];
        for (int i = -1; i <= 1; i++) {
            years[i + 1] = String.valueOf(fullDate.getYear() + i);
        }

        //add the days and months to the drop-downs.
        JComboBox<String> dayComboBox = new JComboBox<>(days);
        dayComboBox.setSelectedIndex(currentDay - 2);
        JComboBox<String> monthComboBox = new JComboBox<>(months);
        monthComboBox.setSelectedIndex(currentMonth - 1);
        JComboBox<String> yearComboBox = new JComboBox<>(years);
        yearComboBox.setSelectedIndex(1);

        panel.add(dayComboBox);
        panel.add(monthComboBox);
        panel.add(yearComboBox);

        //next button
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
            //all necessary information:
            String day = (String) dayComboBox.getSelectedItem();
            String year = (String) yearComboBox.getSelectedItem();
            String month = (String) monthComboBox.getSelectedItem();
            String dateSelected = year + "-" + month + "-" + day;
            String pid = (String) userInformation.get("pid");
            String did = (String) userInformation.get("assigned_doctor_id");

            List<HashMap<String, Object>> appointments = dbManager.getFutureAppointmentsByFullDate(pid, did, dateSelected);

            //if there are no appointments, show an error and return
            //if not, display the appointments, asking for an appointment that will be changed.
            if (appointments.isEmpty()) {
                int option = JOptionPane.showOptionDialog(null, "You have no booked appointments on this date.", "No Booked Appointments", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, new String[]{"OK"}, "OK");
                if (option == JOptionPane.OK_OPTION) {
                    frame.dispose();
                    rescheduleAppointmentInterface(userInformation);
                }
            } else {
                frame.dispose();
                frame = new JFrame("Reschedule an appointment");
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
                scrollPane.setBorder(BorderFactory.createTitledBorder("Appointments that can be rescheduled:"));
                GridBagConstraints scrollPaneConstraints = new GridBagConstraints();
                scrollPaneConstraints.gridx = 0;
                scrollPaneConstraints.gridy = 2;
                scrollPaneConstraints.anchor = GridBagConstraints.NORTH;
                scrollPaneConstraints.weighty = 10.0;
                frame.add(scrollPane, scrollPaneConstraints);

                //go back
                GridBagConstraints goBackButtonConstraint1 = new GridBagConstraints();
                goBackButtonConstraint1.gridy = 6;
                goBackButtonConstraint1.gridx = 0;
                JButton goBackButton1 = new JButton("Go Back");
                frame.add(goBackButton1, goBackButtonConstraint1);

                //reschedule button
                GridBagConstraints rescheduleButtonConstraint = new GridBagConstraints();
                rescheduleButtonConstraint.gridy = 5;
                rescheduleButtonConstraint.gridx = 0;
                JButton rescheduleButton = new JButton("Reschedule this appointment");
                frame.add(rescheduleButton, rescheduleButtonConstraint);

                //reschedule the highlighted appointment
                rescheduleButton.addActionListener(e12 -> {
                    frame.dispose();
                    rescheduleAppointmentWithSelectedAppointment(userInformation, appointmentsList.getSelectedValue());
                });

                //go back
                goBackButton1.addActionListener(e1 -> {
                    frame.dispose();
                    rescheduleAppointmentInterface(userInformation);
                });

                frame.setVisible(true);

            }
        });

        goBackButtons(userInformation, panelConstraints, panel, goBackButton, frame);
    }

    static void goBackButtons(HashMap<String, Object> userInformation, GridBagConstraints panelConstraints, JPanel panel, JButton goBackButton, JFrame frame) {
        goBackButton.addActionListener(e -> {
            frame.dispose();
            Profile pr = new Profile();
            pr.openProfile((String) userInformation.get("username"), (String) userInformation.get("password"));
        });

        frame.add(panel, panelConstraints);
        frame.setVisible(true);
    }

//---------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * After the user has selected an appointment they want to reschedule they then choose when they want to re-book to. Including date and time.
     *
     * @param userInformation     The current user.
     * @param selectedAppointment The appointment to be rescheduled.
     * @author max
     */
    public void rescheduleAppointmentWithSelectedAppointment(HashMap<String, Object> userInformation, String selectedAppointment) {
        frame = new JFrame("Reschedule an appointment");
        frame.setSize(400, 300);
        frame.setLayout(new GridBagLayout());
        DatabaseInteract dbManager = new DatabaseInteract();

        //message
        JLabel message = new JLabel("What date do you want to reschedule to?");
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

        //date logic
        LocalDate fullDate = LocalDate.now();
        LocalDate defaultDay = fullDate.plusDays(1);
        int currentDay = defaultDay.getDayOfMonth();
        int currentMonth = fullDate.getMonthValue();
        int currentYear = fullDate.getYear();

        String[] days = new String[31];
        for (int i = 1; i <= 31; i++) {
            days[i - 1] = String.format("%02d", i);
        }

        String[] months = new String[12];
        for (int i = 1; i <= 12; i++) {
            months[i - 1] = String.format("%02d", i);
        }

        String[] years = new String[3];
        for (int i = -1; i <= 1; i++) {
            years[i + 1] = String.valueOf(fullDate.getYear() + i);
        }

        //add the days and months to the drop-downs.
        JComboBox<String> dayComboBox = new JComboBox<>(days);
        dayComboBox.setSelectedIndex(currentDay - 2);
        JComboBox<String> monthComboBox = new JComboBox<>(months);
        monthComboBox.setSelectedIndex(currentMonth - 1);
        JComboBox<String> yearComboBox = new JComboBox<>(years);
        yearComboBox.setSelectedIndex(1);

        panel.add(dayComboBox);
        panel.add(monthComboBox);
        panel.add(yearComboBox);

        //next button
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

        frame.add(panel, panelConstraints);
        frame.setVisible(true);

        goBackButton.addActionListener(e -> {
            frame.dispose();
            rescheduleAppointmentInterface(userInformation);
        });

        nextButton.addActionListener(e -> {
            frame.dispose();
            frame = new JFrame("Reschedule an appointment");
            frame.setSize(400, 600);
            frame.setLayout(new GridBagLayout());
            DatabaseInteract dbManager1 = new DatabaseInteract();

            //message
            JLabel message1 = new JLabel("Dr. " + dbManager1.getDoctorFullName((String) userInformation.get("assigned_doctor_id")) +
                    " is free at the following times,");

            message1.setFont(message1.getFont().deriveFont(13.0f));
            GridBagConstraints welcomeMessageConstraints1 = new GridBagConstraints();
            welcomeMessageConstraints1.gridx = 0;
            welcomeMessageConstraints1.gridy = 0;
            welcomeMessageConstraints1.anchor = GridBagConstraints.NORTH;
            welcomeMessageConstraints1.weighty = 1.0;
            frame.add(message1, welcomeMessageConstraints1);

            //second message
            JLabel doctorName = new JLabel("please select one:");
            doctorName.setFont(message1.getFont());
            GridBagConstraints doctorNameConstraints = new GridBagConstraints();
            doctorNameConstraints.gridx = 0;
            doctorNameConstraints.gridy = 1;
            doctorNameConstraints.anchor = GridBagConstraints.NORTH;
            doctorNameConstraints.weighty = 10.0;
            frame.add(doctorName, doctorNameConstraints);

            //panel
            GridBagConstraints panelConstraints1 = new GridBagConstraints();
            panelConstraints1.gridx = 0;
            panelConstraints1.gridy = 2;
            JPanel panel1 = new JPanel();

            String date = yearComboBox.getSelectedItem() + "-" + monthComboBox.getSelectedItem() + "-" + dayComboBox.getSelectedItem();
            //System.out.println(date);

            ArrayList<String> notAvailableTimes = dbManager1.doctorsBusyTimes((String) userInformation.get("assigned_doctor_id"), date);
            System.out.println(notAvailableTimes);

            JList<String> messageList = getStringJList(notAvailableTimes);

            JScrollPane scrollPane = new JScrollPane(messageList);
            scrollPane.setPreferredSize(new Dimension(300, 300));
            scrollPane.setBorder(BorderFactory.createTitledBorder("Available times:"));
            GridBagConstraints scrollPaneConstraints = new GridBagConstraints();
            scrollPaneConstraints.gridx = 0;
            scrollPaneConstraints.gridy = 2;
            scrollPaneConstraints.anchor = GridBagConstraints.NORTH;
            scrollPaneConstraints.weighty = 10.0;
            frame.add(scrollPane, scrollPaneConstraints);

            //book appointment button
            GridBagConstraints bookAppointmentButtonConstraint = new GridBagConstraints();
            bookAppointmentButtonConstraint.gridy = 4;
            bookAppointmentButtonConstraint.gridx = 0;
            JButton bookAppointmentButton = new JButton("Book appointment");
            frame.add(bookAppointmentButton, bookAppointmentButtonConstraint);

            //go back button
            GridBagConstraints goBackButtonConstraint1 = new GridBagConstraints();
            goBackButtonConstraint1.gridy = 5;
            goBackButtonConstraint1.gridx = 0;
            JButton goBackButton1 = new JButton("Go back");
            frame.add(goBackButton1, goBackButtonConstraint1);

            goBackButton1.addActionListener(e12 -> {
                frame.dispose();
                rescheduleAppointmentWithSelectedAppointment(userInformation, selectedAppointment);
            });

            bookAppointmentButton.addActionListener(e1 -> {
                String pid = (String) userInformation.get("pid");
                String did = (String) userInformation.get("assigned_doctor_id");
                dbManager1.removeAppointment(did, pid, selectedAppointment);
                dbManager1.addAppointment(did, pid, date + " " + messageList.getSelectedValue());
                dbManager1.addLog(pid, "Rescheduled an appointment");
                dbManager1.addMessage(userInformation, "You rescheduled your appointment with dr: " +
                        dbManager1.getDoctorFullName((String) userInformation.get("assigned_doctor_id")) + " to " +
                        date + " " + messageList.getSelectedValue(), pid);
                frame.dispose();

                Profile pr = new Profile();
                pr.openProfile((String) userInformation.get("username"), (String) userInformation.get("password"));
            });

            frame.add(panel1, panelConstraints1);
            frame.setVisible(true);
        });

        frame.setVisible(true);
    }

    private static JList<String> getStringJList(ArrayList<String> notAvailableTimes) {
        JList<String> messageList = new JList<>();
        messageList.setFixedCellHeight(30);
        messageList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        ArrayList<String> possibleTimes = new ArrayList<>();
        for (int hour = 9; hour <= 17; hour++) {
            String time = String.format("%02d", hour) + ":00:00";
            if (!notAvailableTimes.contains(time)) {
                possibleTimes.add(time);
            }
        }
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (String time : possibleTimes) {
            if (time != null) {
                listModel.addElement(time);
            }
        }
        messageList.setModel(listModel);
        return messageList;
    }
}
