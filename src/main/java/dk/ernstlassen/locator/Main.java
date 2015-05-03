package dk.ernstlassen.locator;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

/**
 * Created by hans on 02/05/15.
 */
public class Main {

    private static String currentIp = "";

    public static void main(String[] args) throws InterruptedException {
        final Map<String, String> env = System.getenv();

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        final String username = (env.get("mailusername") != null)?env.get("mailusername"):"";
        System.out.println("username = " + username);
        final String password = (env.get("mailpassword") != null)?env.get("mailpassword"):"";
        System.out.println("password = " + password);
        final String mailto = (env.get("mailto") != null)?env.get("mailto"):"";
        System.out.println("mailto = " + mailto);
        final String mailfrom = (env.get("mailfrom") != null)?env.get("mailfrom"):"";
        System.out.println("mailfrom = " + mailfrom);

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        while(true){

            String ip = "";
            try {
                URL whatismyip = new URL("http://checkip.amazonaws.com");
                BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
                ip = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(!currentIp.equals(ip)) {
                try {
                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(mailfrom));
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailto));
                    message.setSubject("From Barcelona: Changed ip address");
                    message.setText("New ip address: " + ip);

                    Transport.send(message);
                    System.out.println("Send mail");
                    currentIp = ip;
                } catch (MessagingException e) {
                    System.out.println("Failed to send mail");
                    currentIp = "";
                }
                System.out.println("currentIp = " + currentIp);
            }

            Thread.sleep(360000);
        }
    }
}
