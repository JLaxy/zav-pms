package models.modules;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import models.helpers.DateHelper;
import models.helpers.PopupDialog;

public class Emailer {
    private static final String HOST = "smtp.gmail.com";
    private static final String PORT = "587";

    // Email
    private static final String FROM = "zavskitchenandrestobar.pms@gmail.com";
    // Password
    private static final String APP_PASSWORD = "jgzh sznn oxap ypig";

    // Returns Properties that will be used to send email
    private static Properties getProps() {
        // Configuring
        Properties props = System.getProperties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.trust", HOST);
        props.put("mail.smtp.host", HOST);
        props.put("mail.smtp.user", FROM);
        props.put("mail.smtp.password", APP_PASSWORD);
        props.put("mail.smtp.port", PORT);
        props.put("mail.smtp.auth", "true");

        return props;
    }

    // Sends OTP to user
    public static boolean sendOTP(String receiver, String OTP) {
        Session session = Session.getDefaultInstance(getProps());
        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(FROM));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
            message.setSubject("Login OTP Code");
            message.setText("Login attempt during " + DateHelper.getCurrentDateTimeString() + " at "
                    + Security.getSystemName() + ".\n\n\n Your OTP Code is: " + OTP
                    + "\n\n this password will expire in the next 5 minutes.");
            Transport transport = session.getTransport("smtp");
            transport.connect(HOST, FROM, APP_PASSWORD);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            return true;
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, "models.modules.Emailer");
            return false;
        }
    }
}
