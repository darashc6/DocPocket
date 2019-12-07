package es.losinutiles.docpocket;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class ActividadInicial extends AppCompatActivity {
    private static int GOOGLE_SIGN=123; // Código de permiso para iniciar sesión con Google
    private FirebaseAuth mFirebase; // Autenticación con Firebase
    private ProgressBar barraProgreso; // Barra de progreso
    private GoogleSignInClient gsic; // Cliente de cuentas de Google

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_inicial);

        mFirebase=FirebaseAuth.getInstance();
        if (mFirebase.getCurrentUser()!=null) { // Si el usuario ya ha iniciado sesión anteriormente, ira directamente a la página principal
            Intent irDirectamente=new Intent(this, MainActivity.class);
            irDirectamente.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(irDirectamente);
        } else { // Sino, que elija la cuenta que quiere usar
            barraProgreso=findViewById(R.id.progressBarCircular);

            // Permite elegir con que cuenta de google quiere iniciar sesion
            GoogleSignInOptions gsio=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
            gsic=GoogleSignIn.getClient(getApplicationContext(), gsio);
        }
    }


    /**
     * Función que inicia sesión utilizando la autencticación de Google
     * @param view view de la función
     */
    public void iniciarSesionGoogle(View view) {
        barraProgreso.setVisibility(View.VISIBLE);
        Intent intentIniciarSesion=gsic.getSignInIntent();
        startActivityForResult(intentIniciarSesion, GOOGLE_SIGN);
    }

    /**
     * Función que obtiene un resultado de la actividad, a través de startActivityForResult()
     * @param requestCode Código de permiso
     * @param resultCode Código de resultado
     * @param data Intent del activity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_SIGN) {
            Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);
            GoogleSignInAccount cuentaGoogle=null;
            try {
                cuentaGoogle = task.getResult(ApiException.class);
                if (cuentaGoogle!=null) {
                    autenticacionFirebaseGoogle(cuentaGoogle);
                }
            } catch (ApiException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error a la hora de verificar la cuenta de Google. Inténtelo de nuevo", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Que haces programando a las 12 de la noche", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Función que verifica la cuenta elegida de Google con Firebase
     * @param cuenta cuenta de Google que ha elegido el usuario
     */
    public void autenticacionFirebaseGoogle(GoogleSignInAccount cuenta) {
        AuthCredential credencial= GoogleAuthProvider.getCredential(cuenta.getIdToken(), null);
        mFirebase.signInWithCredential(credencial).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intentPaginaPrincipal=new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intentPaginaPrincipal);
                } else {
                    Toast.makeText(getApplicationContext(), "Ha habido algún fallo a la hora de iniciar sesión. Inténtelo de nuevo", Toast.LENGTH_LONG).show();
                }
                barraProgreso.setVisibility(View.INVISIBLE);
            }
        });
    }

    /**
     * Función que sobreescribe la funcionalidad al dar el botón de atrás
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
