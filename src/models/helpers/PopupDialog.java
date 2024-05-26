/*
 * Class handling popup dialogs in the system
 */

package models.helpers;

import javax.swing.JOptionPane;

public class PopupDialog {
    // Show pop-up error
    public static void showErrorDialog(Exception e, String className) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, e, "Error in " + className,
                JOptionPane.ERROR_MESSAGE);
    }

    // Show pop-up information
    public static void showInfoDialog(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    // Shows cancel operation dialog
    public static int cancelOperationDialog() {
        return JOptionPane.showConfirmDialog(null, "Are you sure you want to cancel this operation?",
                "Cancel Operation",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
    }

    // Shows cancel operation dialog
    public static int confirmOperationDialog(String message) {
        return JOptionPane.showConfirmDialog(null, message,
                "Confirm Operation",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
    }

    // Show custom pop-up error dialog
    public static void showCustomErrorDialog(String errorDescription) {
        JOptionPane.showMessageDialog(null, errorDescription, "An Error has occured!", JOptionPane.ERROR_MESSAGE);
    }
}
