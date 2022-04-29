import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class MailDelivered {
  private  static final MailDelivered INSTANCE = new MailDelivered();

  public static MailDelivered instance() {
    return INSTANCE;
  }

  private MailDelivered() {
  }

  protected Authenticator getPasswordAuthentication(String username, String password) {
    return new javax.mail.Authenticator() {
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
      }
    };

  }


  public void enviarTokenValidacionMail(String destinatario, String token)
      throws MessagingException {
    Properties props = System.getProperties();
    props.setProperty("mail.smtp.host", "smtp.gmail.com");
    props.setProperty("mail.smtp.auth", "true");
    props.setProperty("mail.smtp.starttls.enable", "true");
    props.setProperty("mail.smtp.port", "587");

    String remitente = "ddsg16.2022@gmail.com";
    String clave = "********";

    Session session = Session.getInstance(props,
        this.getPasswordAuthentication(remitente, clave));

    MimeMessage message = new MimeMessage(session);
    message.setFrom(new InternetAddress(remitente));
    message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
    message.setSubject("TOKEN VERIFICADOR");
    message.setText("Token: " + token);
    Transport transport = session.getTransport("smtp");
    Transport.send(message);
    transport.close();
  }
}
