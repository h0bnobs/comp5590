package src.UI;

import src.Database.DBManager;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;

/**
 * The window where the user can change their doctor.
 */
public class ChangeDoctor {

    private JFrame frame;

    /**
     * Lets the user change their current doctor to a different one.
     *
     * @param userInformation the current user.
     * @author max
     */
    public void changeDoctorInterface(HashMap<String, Object> userInformation) {
        frame = new JFrame("Change your doctor");
        frame.setSize(400, 300);
        frame.setLayout(new GridBagLayout());
        DBManager dbManager = new DBManager();

        String currentDoctorName = dbManager.getDoctorFullName((String) userInformation.get("assigned_doctor_id"));

        //add the doctors from the database to the list
        List<String> doctorNames = DBManager.getAllDoctorNames();

        //remove the patient's current doctor from the list of new doctor's to select
        doctorNames.removeIf(doctor -> doctor.equals(currentDoctorName));

        String[] doctors = doctorNames.toArray(new String[0]);
        JList<String> doctorList = new JList<>(doctors);

        //list model
        JScrollPane scrollPane = new JScrollPane(doctorList);
        scrollPane.setPreferredSize(new Dimension(380, 165));
        scrollPane.setBorder(BorderFactory.createTitledBorder("Select Doctor"));
        GridBagConstraints scrollPaneConstraint = new GridBagConstraints();
        scrollPaneConstraint.anchor = GridBagConstraints.NORTHWEST;
        scrollPaneConstraint.gridwidth = 2;
        frame.add(scrollPane, scrollPaneConstraint);

        //change doctor button
        GridBagConstraints changeDoctorButtonConstraint = new GridBagConstraints();
        changeDoctorButtonConstraint.gridy = 1;
        changeDoctorButtonConstraint.gridx = 1;
        changeDoctorButtonConstraint.gridwidth = 1;
        changeDoctorButtonConstraint.anchor = GridBagConstraints.CENTER;
        changeDoctorButtonConstraint.fill = GridBagConstraints.NONE;
        changeDoctorButtonConstraint.weighty = 0.0;
        JButton changeDoctorButton = new JButton("Change your doctor");
        frame.add(changeDoctorButton, changeDoctorButtonConstraint);

        //go back
        GridBagConstraints goBackButtonConstraint = new GridBagConstraints();
        goBackButtonConstraint.gridy = 1;
        goBackButtonConstraint.gridx = 0;
        goBackButtonConstraint.gridwidth = 1;
        goBackButtonConstraint.anchor = GridBagConstraints.CENTER;
        goBackButtonConstraint.fill = GridBagConstraints.NONE;
        goBackButtonConstraint.weighty = 0.0;
        JButton goBackButton = new JButton("Go Back");
        frame.add(goBackButton, goBackButtonConstraint);

        changeDoctorButton.addActionListener(e -> {
            String pass = JOptionPane.showInputDialog(frame, "Please enter your password to verify:");

            if (pass == null || !pass.equals(userInformation.get("password"))) {
                JOptionPane.showMessageDialog(frame, "Incorrect password, try again.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String selectedDoctor = doctorList.getSelectedValue();

            if (selectedDoctor == null) {
                JOptionPane.showMessageDialog(frame, "Please select a doctor from the list, or go back.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            //update patient's assigned doctor id in the db.
            String pid = (String) userInformation.get("pid");
            dbManager.updateAssignedDoctorId(pid, selectedDoctor);
            dbManager.addLog(pid, "Changed doctor");
            dbManager.addMessage(userInformation, "You changed your doctor to: dr. " + selectedDoctor, (String) userInformation.get("pid"));
            frame.dispose();
            Profile pr = new Profile();
            pr.openProfile((String) userInformation.get("username"), (String) userInformation.get("password"));
        });

        goBackButton.addActionListener(e -> {
            dbManager.addLog((String) userInformation.get("pid"), "Cancelled doctor change");
            frame.dispose();
            Profile pr = new Profile();
            pr.openProfile((String) userInformation.get("username"), (String) userInformation.get("password"));
        });

        frame.setVisible(true);
    }
}
