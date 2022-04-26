import org.apache.commons.codec.digest.DigestUtils;
import java.io.*;
import java.util.Scanner;

import static org.apache.commons.codec.digest.DigestUtils.sha256;
import static org.apache.commons.codec.digest.DigestUtils.sha256Hex;

public class UserValidation {

  public static boolean verificarContraseñaDebil(String contrasena) throws FileNotFoundException {

      File listado = new File("./Weak_Paswords.txt");
      Scanner contrasenasDebiles = new Scanner(listado);

      while (contrasenasDebiles.hasNextLine()) {
        if(contrasenasDebiles.nextLine().equals(contrasena))
          throw new ContraseñaDebilException("Contraseña dentro de las 10000 mas usadas");
      }

      return true;
  }
  public static void guardarUsuario(String usuario, String contrasena) throws IOException {

    if(usuario.equals(contrasena))
      throw new ContraseñaDebilException("No se puede utilizar contraseñas ppor defecto");
    verificarContraseñaDebil(contrasena);
    File registroDeUsuarios=new File("./RegistroUsuarios.txt");
    if(!registroDeUsuarios.exists())
      throw new NullPointerException("El Registro de Usuarios no Existe");
    verificarUsuarioDisponible(usuario);
    BufferedWriter registoParaEscribir = new BufferedWriter(new FileWriter("./RegistroUsuarios.txt"));
    registoParaEscribir.write(usuario+": "+ (sha256Hex(contrasena)));
    registoParaEscribir.close();
    System.out.println(sha256Hex(contrasena));
  }
  public static boolean verificarUsuarioDisponible(String usuario) throws FileNotFoundException {
    File registroDeUsuarios=new File("./RegistroUsuarios.txt");
    Scanner usuariosRegistrados = new Scanner(registroDeUsuarios);

    while (usuariosRegistrados.hasNextLine()) {
      if(usuariosRegistrados.nextLine().contains(usuario+":"))
        throw new UsuarioNoDisponibleExeption("Usuario no disponible");
    }
    return true;
  }

  public static boolean validarUsuario(String usuario, String contrasena) throws FileNotFoundException {
    File registroDeUsuarios=new File("./RegistroUsuarios.txt");
    Scanner usuariosRegistrados = new Scanner(registroDeUsuarios);

    while (usuariosRegistrados.hasNextLine()) {
      if(usuariosRegistrados.nextLine().equals(usuario+": "+ sha256Hex(contrasena)))
        return true;
    }
    return false;
  }
  public static void main(String[] args) throws Exception {
    guardarUsuario("admin","forrosdemierda");
    boolean a=validarUsuario("admin","grupo*deRetardos");
    System.out.println(a);
  }

}
class ContraseñaDebilException extends RuntimeException{
  public ContraseñaDebilException(String message){
    super(message);
  }
}
class UsuarioNoDisponibleExeption extends RuntimeException {
  public UsuarioNoDisponibleExeption(String message) {
    super(message);
  }
}
