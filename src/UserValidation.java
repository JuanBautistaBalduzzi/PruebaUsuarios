
import java.io.*;
import java.util.Scanner;
import javax.mail.MessagingException;
import org.apache.commons.lang3.RandomStringUtils;
import static org.apache.commons.codec.digest.DigestUtils.sha256Hex;

public class UserValidation {

  public static void verificarContrasenaDebil(String contrasena) throws FileNotFoundException {

    File listado = new File("./Weak_Paswords.txt");
    Scanner contrasenasDebiles = new Scanner(listado);

    while (contrasenasDebiles.hasNextLine()) {
      if (contrasenasDebiles.nextLine().equals(contrasena)) {
        throw new ContrasenaDebilException("Contraseña dentro de las 10000 mas usadas");
      }
    }

  }

  public static void guardarUsuario(String usuario, String contrasena) throws IOException {

    if (usuario.equals(contrasena)) {
      throw new ContrasenaDebilException("No se puede utilizar contraseñas ppor defecto");
    }
    verificarContrasenaDebil(contrasena);

    File registroDeUsuarios = new File("./RegistroUsuarios.txt");
    if (!registroDeUsuarios.exists()) {
      throw new NullPointerException("El Registro de Usuarios no Existe");
    }
    verificarUsuarioDisponible(usuario);
    BufferedWriter registoParaEscribir = new BufferedWriter(
        new FileWriter(registroDeUsuarios.getAbsoluteFile(), true));
    registoParaEscribir.write(usuario + ": " + sha256Hex(contrasena) + "\n");
    registoParaEscribir.close();
    System.out.println(sha256Hex(contrasena));
  }

  public static void verificarUsuarioDisponible(String usuario) throws FileNotFoundException {
    File registroDeUsuarios = new File("./RegistroUsuarios.txt");
    Scanner usuariosRegistrados = new Scanner(registroDeUsuarios);

    while (usuariosRegistrados.hasNextLine()) {
      if (usuariosRegistrados.nextLine().contains(usuario + ":")) {
        throw new UsuarioNoDisponibleExeption("Usuario no disponible");
      }
    }
  }

  public static boolean validarUsuario(String usuario, String contrasena)
      throws FileNotFoundException {
    File registroDeUsuarios = new File("./RegistroUsuarios.txt");
    Scanner usuariosRegistrados = new Scanner(registroDeUsuarios);

    while (usuariosRegistrados.hasNextLine()) {
      if (usuariosRegistrados.nextLine().equals(usuario + ": " + sha256Hex(contrasena))) {
        return true;
      }
    }
    return false;
  }

  public static String enviarToken(String mail) throws MessagingException {
    String token = RandomStringUtils.randomAlphanumeric(6);
    MailDelivered mailDelivered = MailDelivered.instance();
    mailDelivered.enviarTokenValidacionMail(mail, token);
    return token;
  }

  public static void main(String[] args) throws Exception {
    guardarUsuario("admin", "grupo*deRetardos");
    String token = enviarToken("mailprueba@gmail.com");
    boolean a = validarUsuario("admin", "grupo*deRetardos");
    System.out.println(a + token);
  }

}

