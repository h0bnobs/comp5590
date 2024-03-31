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

    public static void main(String[] args) {
        LoginAndSignup ui = new LoginAndSignup();
        ui.loginInterface();
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
                Profile pr = new Profile();
                pr.openProfile(username, password);

            }
        });

        frame.setVisible(true);
    }

//---------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Makes sure that the name inputted by the user in the signup window doesn't contain digits
     *
     * @param name the name inputted.
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
     * @param password the user's password.
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
