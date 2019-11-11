package es.losinutiles.docpocket;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EnvioMail extends AsyncTask {
    private Context contexto; // Contexto de la aplicación
    private Session sesion; // Almacena las propiedades del servidor de GMAIL
    private String nombreEscrito; // Nombre que ha escrito el usuario
    private String emailEscrito; // Email que ha escrito el usuario
    private String mensajeEscrito; // Mensaje que ha escrito el usuario
    private final String emailAuxiliar="Info.losinutiles@gmail.com"; // Email auxiliar
    private final String contraseniaAuxiliar="Info.losinutiles.4"; // Contraseña del email auxiliar
    private final String emailLosInutiles="losinutilesbusiness@gmail.com"; // Email oficial de los inútiles

    /**
     * Constructor de la clase EnvioEmail
     * @param contexto Contexto de la aplicación
     * @param nombreEscrito Nombre que ha escrito el usuario
     * @param emailEscrito Email que ha escrito el usuario
     * @param mensajeEscrito Mensaje que ha escrito el usuario
     */
    public EnvioMail(Context contexto, String nombreEscrito, String emailEscrito, String mensajeEscrito) {
        this.contexto = contexto;
        this.nombreEscrito = nombreEscrito;
        this.emailEscrito = emailEscrito;
        this.mensajeEscrito = mensajeEscrito;
    }

    /**
     * Interfaz proporcionada una vez que la clase se extiende de AsyncTask
     * En él inicializamos las propiedades necesarias para conectar al servidor de Gmail
     * @param objects En nuestro caso, esto no nos sirve
     * @return En nuestro caso, nada
     */
    @Override
    protected Object doInBackground(Object[] objects) {
        Properties propiedadesGmail=new Properties(); // Propiedad en la que meteremos los datos necesarios para conectar

        propiedadesGmail.put("mail.smtp.host", "smtp.gmail.com");
        propiedadesGmail.put("mail.smtp.socketFactory.port", "465");
        propiedadesGmail.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        propiedadesGmail.put("mail.smtp.auth", "true");
        propiedadesGmail.put("mail.smtp.port", "465");

        // Esta sesión se utiliza para verificar la cuenta de Gmail
        // Como se necesita una contraseña para verificar la cuenta, y al usuario solo le vamos a pedir su email, se ha creado una cuenta auxiliar de Gmail (Info.losinutiles@gmail.com)
        // Para ver el nombre y el email escrito, lo podemos ponerlo justo despues del mensaje escrito, o en el tema (O donde sea)
        // PosData: Si quito el PasswordAuthentication, peta el programa, por eso lo de crear una nueva cuenta
        sesion=Session.getDefaultInstance(propiedadesGmail, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailAuxiliar, contraseniaAuxiliar);
            }
        });

        try {
            MimeMessage mm=new MimeMessage(sesion);
            mm.setFrom(new InternetAddress(emailAuxiliar)); // Aqui el mensaje se va a mandar desde la cuenta auxiliar
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress(emailLosInutiles)); // Aqui donde se va a recibir el mensaje
            mm.setSubject(nombreEscrito+" - "+emailEscrito); // El tema. En este caso, he escrito el nombre y el email del usuario, para que podamos reconocer quien lo ha escrito
            mm.setText(mensajeEscrito); // El mensaje escrito
            Transport.send(mm); // Transport sirve simplemente para mandar el MimeMessage declarado
        } catch (MessagingException e) {
            Toast.makeText(contexto, "Error", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Función que hace cualquier cosa antes de ejecutar la función doInBackground
     */
    @Override
    protected void onPreExecute() {
        Toast.makeText(contexto, "Enviando...", Toast.LENGTH_SHORT).show();
        super.onPreExecute();
    }

    /**
     * Función que hace cualquier cosa después de ejecutar la función doInBackground
     * @param o Es este caso, no no sirve para nada
     */
    @Override
    protected void onPostExecute(Object o) {
        Toast.makeText(contexto, "¡E-Mail enviado!", Toast.LENGTH_SHORT).show();
        super.onPostExecute(o);
    }
}
