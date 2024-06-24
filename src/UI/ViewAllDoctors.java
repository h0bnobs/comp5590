package src.UI;

import src.Database.SQLiteExample;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;

/**
 * The window where users can view all the doctors and their info.
 */
public class ViewAllDoctors {
    private JFrame frame;

    /**
     * Window with a table with all the doctors.
     * @param userInformation The current user
     *
     * @author max
     */
    public void viewDoctors(HashMap<String, Object> userInformation) {
        frame = new JFrame("View all doctors");
        frame.setSize(700, 700);
        frame.setLayout(new GridBagLayout());
        SQLiteExample dbManager = new SQLiteExample();
        List<HashMap<String, Object>> doctors = dbManager.getAllDoctors();

        String[] columnNames = {"DID", "First Name", "Last Name", "Address", "Start Date", "Specialist Area"};
        Object[][] data = new Object[doctors.size()][columnNames.length];

        for (int i = 0; i < doctors.size(); i++) {
            HashMap<String, Object> doctor = doctors.get(i);
            data[i][0] = doctor.get("did");
            data[i][1] = doctor.get("first_name");
            data[i][2] = doctor.get("last_name");
            data[i][3] = doctor.get("address");
            data[i][4] = doctor.get("start_date");
            data[i][5] = doctor.get("specialist_area");
        }

        JTable table = new JTable(data, columnNames);
        table.setFillsViewportHeight(true);
        table.getColumnModel().getColumn(0).setPreferredWidth(5);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(650, 400));
        frame.add(scrollPane);

        //go back
        GridBagConstraints goBackButtonConstraint = new GridBagConstraints();
        goBackButtonConstraint.gridy = 5;
        goBackButtonConstraint.gridx = 0;
        JButton goBackButton = new JButton("Go Back");
        frame.add(goBackButton, goBackButtonConstraint);

        goBackButton.addActionListener(e -> {
            frame.dispose();
            Profile p = new Profile();
            p.openProfile((String) userInformation.get("username"), (String) userInformation.get("password"));
        });

        frame.setVisible(true);
    }

}
