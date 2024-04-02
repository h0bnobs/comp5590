package src.UI;

import src.Database.DBManager;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * The window where a user can book an appointment.
 */
public class BookAppointment {

    private JFrame frame;

    /**
     * The window where users can book an appointment with their doctor after they have logged in.
     *
     * @param userInformation The current user's information.
     * @author max
     */
    public void bookAppointmentInterface(HashMap<String, Object> userInformation) {
        frame = new JFrame("Book an appointment");
        frame.setSize(400, 300);
        frame.setLayout(new GridBagLayout());
        DBManager dbManager = new DBManager();

        //message
        JLabel message = new JLabel("Please select a date to book an appointment with");
        message.setFont(message.getFont().deriveFont(13.0f));
        GridBagConstraints welcomeMessageConstraints = new GridBagConstraints();
        welcomeMessageConstraints.gridx = 0;
        welcomeMessageConstraints.gridy = 0;
        welcomeMessageConstraints.anchor = GridBagConstraints.NORTH;
        frame.add(message, welcomeMessageConstraints);

        //second message
        JLabel doctorName = new JLabel("dr. " + dbManager.getDoctorFullName((String) userInformation.get("assigned_doctor_id")));
        doctorName.setFont(message.getFont());
        GridBagConstraints doctorNameConstrain = new GridBagConstraints();
        doctorNameConstrain.gridx = 0;
        doctorNameConstrain.gridy = 1;
        doctorNameConstrain.anchor = GridBagConstraints.NORTH;
        frame.add(doctorName, doctorNameConstrain);

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

        String[] days = new String[32 - currentDay];
        for (int i = currentDay; i <= 31; i++) {
            days[i - currentDay] = String.valueOf(i);
        }

        String[] months = new String[12];
        for (int i = 1; i <= 12; i++) {
            months[i - 1] = String.valueOf(i);
        }

        String[] years = new String[2];
        for (int i = 0; i < 2; i++) {
            years[i] = String.valueOf(fullDate.getYear() + i);
        }

        //add the days and months to the drop-downs.
        JComboBox<String> dayComboBox = new JComboBox<>(days);
        JComboBox<String> monthComboBox = new JComboBox<>(months);
        monthComboBox.setSelectedIndex(currentMonth - 1);
        JComboBox<String> yearComboBox = new JComboBox<>(years);
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

        //today's date message
        int currentDay2 = currentDay - 1;
        JLabel todaysDate = new JLabel("Today is " + currentDay2 + "-" + currentMonth + "-" + fullDate.getYear() + ". We do not accept same-day bookings.");
        doctorName.setFont(message.getFont());
        GridBagConstraints todaysDateConstrain = new GridBagConstraints();
        todaysDateConstrain.gridx = 0;
        todaysDateConstrain.gridy = 7;
        todaysDateConstrain.anchor = GridBagConstraints.NORTH;
        frame.add(todaysDate, todaysDateConstrain);

        //next button action
        nextButton.addActionListener(e -> {
            String day = (String) dayComboBox.getSelectedItem();
            String year = (String) yearComboBox.getSelectedItem();
            String month = "";
            if (Objects.equals(monthComboBox.getSelectedItem(), "10") || Objects.equals(monthComboBox.getSelectedItem(), "11")
                    || Objects.equals(monthComboBox.getSelectedItem(), "12")) {
                month = (String) monthComboBox.getSelectedItem();
            } else {
                month = "0" + monthComboBox.getSelectedItem();
            }

            frame.dispose();
            frame = new JFrame("Book an appointment");
            frame.setSize(400, 600);
            frame.setLayout(new GridBagLayout());
            DBManager dbManager1 = new DBManager();

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
            JLabel doctorName1 = new JLabel("please select one:");
            doctorName1.setFont(message1.getFont());
            GridBagConstraints doctorNameConstraints = new GridBagConstraints();
            doctorNameConstraints.gridx = 0;
            doctorNameConstraints.gridy = 1;
            doctorNameConstraints.anchor = GridBagConstraints.NORTH;
            doctorNameConstraints.weighty = 10.0;
            frame.add(doctorName1, doctorNameConstraints);

            //panel
            GridBagConstraints panelConstraints1 = new GridBagConstraints();
            panelConstraints1.gridx = 0;
            panelConstraints1.gridy = 2;
            JPanel panel1 = new JPanel();

            String date = year + "-" + month + "-" + day;
            ArrayList<String> notAvailableTimes = dbManager1.doctorsBusyTimes((String) userInformation.get("assigned_doctor_id"), date);
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
            GridBagConstraints goBackButtonConstraint1 = new GridBagConstraints();
            goBackButtonConstraint1.gridy = 5;
            goBackButtonConstraint1.gridx = 0;
            JButton goBackButton1 = new JButton("Go Back");
            frame.add(goBackButton1, goBackButtonConstraint1);

            bookAppointmentButton.addActionListener(e1 -> {
                String time = messageList.getSelectedValue();
                String fullDoctorName = dbManager1.getDoctorFullName((String) userInformation.get("assigned_doctor_id"));
                String did = (String) userInformation.get("assigned_doctor_id");
                String pid = (String) userInformation.get("pid");
                int option = JOptionPane.showConfirmDialog(frame, "Please confirm you want to book an appointment with \n Dr. " + fullDoctorName + " on "
                        + date + " at " + time, "Please Confirm", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    String dateTime = date + " " + time;
                    dbManager1.addAppointment(did, pid, dateTime);
                    dbManager1.addLog(pid, "Booked an appointment with their Doctor");
                    dbManager1.addMessage(userInformation, "You have an appointment booked with Dr. " + fullDoctorName + " for: " + dateTime, (String) userInformation.get("pid"));
                    frame.dispose();
                    Profile pr = new Profile();
                    pr.openProfile((String) userInformation.get("username"), (String) userInformation.get("password"));
                }
            });

            goBackButton1.addActionListener(e12 -> {
                frame.dispose();
                bookAppointmentInterface(userInformation);
            });

            frame.add(panel1, panelConstraints1);
            frame.setVisible(true);


        });

        goBackButton.addActionListener(e -> {
            frame.dispose();
            Profile pr = new Profile();
            pr.openProfile((String) userInformation.get("username"), (String) userInformation.get("password"));
        });

        frame.add(panel, panelConstraints);
        frame.setVisible(true);
    }
}
