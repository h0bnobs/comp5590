package src.UI;

import src.Database.DBManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RescheduleAppointment {

    private JFrame frame;
    private final GUI gui = new GUI();

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
        DBManager dbManager = new DBManager();

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

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
                    GridBagConstraints panelConstraints = new GridBagConstraints();
                    panelConstraints.gridx = 0;
                    panelConstraints.gridy = 2;
                    JPanel panel = new JPanel();
                    frame.add(panel, panelConstraints);

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
                    GridBagConstraints goBackButtonConstraint = new GridBagConstraints();
                    goBackButtonConstraint.gridy = 6;
                    goBackButtonConstraint.gridx = 0;
                    JButton goBackButton = new JButton("Go Back");
                    frame.add(goBackButton, goBackButtonConstraint);

                    //reschedule button
                    GridBagConstraints rescheduleButtonConstraint = new GridBagConstraints();
                    rescheduleButtonConstraint.gridy = 5;
                    rescheduleButtonConstraint.gridx = 0;
                    JButton rescheduleButton = new JButton("Reschedule this appointment");
                    frame.add(rescheduleButton, rescheduleButtonConstraint);

                    //reschedule the highlighted appointment
                    rescheduleButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            frame.dispose();
                            rescheduleAppointmentWithSelectedAppointment(userInformation, appointmentsList.getSelectedValue());
                        }
                    });

                    //go back
                    goBackButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            frame.dispose();
                            rescheduleAppointmentInterface(userInformation);
                        }
                    });

                    frame.setVisible(true);

                }
            }
        });

        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                gui.openProfile((String) userInformation.get("username"), (String) userInformation.get("password"));
            }});

        frame.add(panel, panelConstraints);
        frame.setVisible(true);
    }

//---------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * After the user has selected an appointment they want to reschedule they then choose when they want to re-book to. Including date and time.
     *
     * @param userInformation The current user.
     * @param selectedAppointment The appointment to be rescheduled.
     * @author max
     */
    public void rescheduleAppointmentWithSelectedAppointment(HashMap<String, Object> userInformation, String selectedAppointment) {
        frame = new JFrame("Reschedule an appointment");
        frame.setSize(400, 300);
        frame.setLayout(new GridBagLayout());
        DBManager dbManager = new DBManager();

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

        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                rescheduleAppointmentInterface(userInformation);
            }
        });

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                frame = new JFrame("Reschedule an appointment");
                frame.setSize(400, 600);
                frame.setLayout(new GridBagLayout());
                DBManager dbManager = new DBManager();

                //message
                JLabel message = new JLabel("Dr. " + dbManager.getDoctorFullName((String) userInformation.get("assigned_doctor_id")) +
                        " is free at the following times,");

                message.setFont(message.getFont().deriveFont(13.0f));
                GridBagConstraints welcomeMessageConstraints = new GridBagConstraints();
                welcomeMessageConstraints.gridx = 0;
                welcomeMessageConstraints.gridy = 0;
                welcomeMessageConstraints.anchor = GridBagConstraints.NORTH;
                welcomeMessageConstraints.weighty = 1.0;
                frame.add(message, welcomeMessageConstraints);

                //second message
                JLabel doctorName = new JLabel("please select one:");
                doctorName.setFont(message.getFont());
                GridBagConstraints doctorNameConstraints = new GridBagConstraints();
                doctorNameConstraints.gridx = 0;
                doctorNameConstraints.gridy = 1;
                doctorNameConstraints.anchor = GridBagConstraints.NORTH;
                doctorNameConstraints.weighty = 10.0;
                frame.add(doctorName, doctorNameConstraints);

                //panel
                GridBagConstraints panelConstraints = new GridBagConstraints();
                panelConstraints.gridx = 0;
                panelConstraints.gridy = 2;
                JPanel panel = new JPanel();

                String date = yearComboBox.getSelectedItem() + "-" + monthComboBox.getSelectedItem() + "-" + dayComboBox.getSelectedItem();
                //System.out.println(date);

                ArrayList<String> notAvailableTimes = dbManager.doctorsBusyTimes((String) userInformation.get("assigned_doctor_id"), date);
                System.out.println(notAvailableTimes);

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
                GridBagConstraints goBackButtonConstraint = new GridBagConstraints();
                goBackButtonConstraint.gridy = 5;
                goBackButtonConstraint.gridx = 0;
                JButton goBackButton = new JButton("Go back");
                frame.add(goBackButton, goBackButtonConstraint);

                goBackButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        frame.dispose();
                        rescheduleAppointmentWithSelectedAppointment(userInformation, selectedAppointment);
                    }
                });

                bookAppointmentButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String pid = (String) userInformation.get("pid");
                        String did = (String) userInformation.get("assigned_doctor_id");
                        dbManager.removeAppointment(did, pid, selectedAppointment);
                        dbManager.addAppointment(did, pid, date + " " + messageList.getSelectedValue());
                        dbManager.addLog(pid, "Rescheduled an appointment");
                        dbManager.addMessage(userInformation, "You rescheduled your appointment with dr: " +
                                dbManager.getDoctorFullName((String) userInformation.get("assigned_doctor_id")) + " to " +
                                date + " " + messageList.getSelectedValue(), pid);
                        frame.dispose();

                        gui.openProfile((String) userInformation.get("username"), (String) userInformation.get("password"));
                    }
                });

                frame.add(panel, panelConstraints);
                frame.setVisible(true);
            }
        });

        frame.setVisible(true);
    }
}
