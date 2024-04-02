package src.UI;

/**
 * Contains main method. Sends the user to the login and signup window.
 * Also contains a few methods for testing purposes.
 */
public class GUI {
    public static void main(String[] args) {
        LoginAndSignup ui = new LoginAndSignup();
        ui.loginInterface();
    }

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
