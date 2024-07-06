package controllers.help;

import controllers.ParentController;
import enums.ScreenPaths;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class FaqsHelpController extends ParentController {

        @FXML
        private void goBack(ActionEvent e) {
                this.borderPaneRootSwitcher.goBack_BP();
        }

        @FXML
        private void q1(ActionEvent e) {
                FaqAnswerController controller = (FaqAnswerController) this
                                .initializeNextScreen_BP(ScreenPaths.Paths.FAQS_ANSWER.getPath(), loggedInUserInfo,
                                                "FAQ");
                controller.initialize(
                                "To reset your password, go to the login page and click on the \"Forgot Password\" button. Follow the instructions and answer the secret question in order to reset your password");
        }

        @FXML
        private void q2(ActionEvent e) {
                FaqAnswerController controller = (FaqAnswerController) this
                                .initializeNextScreen_BP(ScreenPaths.Paths.FAQS_ANSWER.getPath(), loggedInUserInfo,
                                                "FAQ");
                controller.initialize(
                                "Navigate to the 'Inventory' module then choose the ‘Product’. Click on 'Add New Product,' fill in the required fields, and save the information. The product will then appear in your inventory list.");
        }

        @FXML
        private void q3(ActionEvent e) {
                FaqAnswerController controller = (FaqAnswerController) this
                                .initializeNextScreen_BP(ScreenPaths.Paths.FAQS_ANSWER.getPath(), loggedInUserInfo,
                                                "FAQ");

                controller.initialize(
                                "First, check your network connection. If the problem persists, try restarting the application. For continued issues, please contact our support team for further assistance.");
        }

        @FXML
        private void q4(ActionEvent e) {
                FaqAnswerController controller = (FaqAnswerController) this
                                .initializeNextScreen_BP(ScreenPaths.Paths.FAQS_ANSWER.getPath(), loggedInUserInfo,
                                                "FAQ");
                controller.initialize(
                                "Yes, you can customize product categories by going to the 'Inventory’' module, selecting 'View Product' and adding or editing categories as needed.");
        }

        @FXML
        private void q5(ActionEvent e) {
                FaqAnswerController controller = (FaqAnswerController) this
                                .initializeNextScreen_BP(ScreenPaths.Paths.FAQS_ANSWER.getPath(), loggedInUserInfo,
                                                "FAQ");
                controller.initialize(
                                "To generate a sales report, go to the 'Reports' module, select 'Sales Report,' choose the date range, and click 'Generate.' The report can be viewed on-screen or exported as a PDF or Excel file.");
        }

        @FXML
        private void q6(ActionEvent e) {
                FaqAnswerController controller = (FaqAnswerController) this
                                .initializeNextScreen_BP(ScreenPaths.Paths.FAQS_ANSWER.getPath(), loggedInUserInfo,
                                                "FAQ");
                controller.initialize(
                                "Yes, you can back up your data by going to the 'Maintenance' module and selecting 'Data Backup.' Follow the prompts to create a backup file that can be stored securely.");
        }

        @FXML
        private void q7(ActionEvent e) {
                FaqAnswerController controller = (FaqAnswerController) this
                                .initializeNextScreen_BP(ScreenPaths.Paths.FAQS_ANSWER.getPath(), loggedInUserInfo,
                                                "FAQ");
                controller.initialize(
                                "Yes, our system supports multiple users. Each user should have their own account with appropriate access permissions set by the administrator.");
        }

        @FXML
        private void q8(ActionEvent e) {
                FaqAnswerController controller = (FaqAnswerController) this
                                .initializeNextScreen_BP(ScreenPaths.Paths.FAQS_ANSWER.getPath(), loggedInUserInfo,
                                                "FAQ");
                controller.initialize(
                                "To set up user permissions, go to the 'Account Manager' module, choose the user, select 'Edit User', and assign the appropriate roles and permissions.");
        }

        @FXML
        private void q9(ActionEvent e) {
                FaqAnswerController controller = (FaqAnswerController) this
                                .initializeNextScreen_BP(ScreenPaths.Paths.FAQS_ANSWER.getPath(), loggedInUserInfo,
                                                "FAQ");
                controller.initialize(
                                "You can track product stock levels by navigating to the 'Inventory' module, where you can view current stock levels, set reorder points, and receive notifications when stock is low.");
        }

        @FXML
        private void q10(ActionEvent e) {
                FaqAnswerController controller = (FaqAnswerController) this
                                .initializeNextScreen_BP(ScreenPaths.Paths.FAQS_ANSWER.getPath(), loggedInUserInfo,
                                                "FAQ");
                controller.initialize(
                                "There is no way to delete a product in the system. But users can “Void” products. To void a product, go to the 'Inventory' module, select the product you wish to void, click on “Edit Product”, and click on the 'Void' button. Confirm the voiding to remove the product from the system.");
        }

        @FXML
        private void q11(ActionEvent e) {
                FaqAnswerController controller = (FaqAnswerController) this
                                .initializeNextScreen_BP(ScreenPaths.Paths.FAQS_ANSWER.getPath(), loggedInUserInfo,
                                                "FAQ");
                controller.initialize(
                                "To import data, go to the 'Management' module, select 'Restore,' choose the file format, map the data fields accordingly, and upload the file. Follow the prompts to complete the import process.");
        }

        @FXML
        private void q12(ActionEvent e) {
                FaqAnswerController controller = (FaqAnswerController) this
                                .initializeNextScreen_BP(ScreenPaths.Paths.FAQS_ANSWER.getPath(), loggedInUserInfo,
                                                "FAQ");
                controller.initialize(
                                "Yes you can automatically make backup copies of your existing database by logging in as an administrator, going to the maintenance module, and then toggling the auto back-up database button on the Edit Auto Backup Settings page");
        }

        @FXML
        private void q13(ActionEvent e) {
                FaqAnswerController controller = (FaqAnswerController) this
                                .initializeNextScreen_BP(ScreenPaths.Paths.FAQS_ANSWER.getPath(), loggedInUserInfo,
                                                "FAQ");
                controller.initialize(
                                "Ensure the security of your data by using strong passwords, backing up data frequently, and setting up user access controls. For advanced security options, contact our support team.");
        }

        @FXML
        private void q14(ActionEvent e) {
                FaqAnswerController controller = (FaqAnswerController) this
                                .initializeNextScreen_BP(ScreenPaths.Paths.FAQS_ANSWER.getPath(), loggedInUserInfo,
                                                "FAQ");
                controller.initialize(
                                "You can find information on the About page of the program");
        }
}
