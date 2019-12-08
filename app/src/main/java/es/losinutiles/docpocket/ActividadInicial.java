package es.losinutiles.docpocket;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class ActividadInicial extends AppCompatActivity {
    private static int GOOGLE_SIGN=123; // Código de permiso para iniciar sesión con Google
    private FirebaseAuth mFirebase; // Autenticación con Firebase
    private ProgressBar barraProgreso; // Barra de progreso
    private GoogleSignInClient gsic; // Cliente de cuentas de Google

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_inicial);

        barraProgreso=findViewById(R.id.progressBarCircular);
        // Permite elegir con que cuenta de google quiere iniciar sesion
        GoogleSignInOptions gsio=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        gsic=GoogleSignIn.getClient(getBaseContext(), gsio);
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
            GoogleSignInResult resultado= Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(resultado.isSuccess()){
                GoogleSignInAccount cuentaGoogle=resultado.getSignInAccount();
                if (cuentaGoogle.getEmail()!=null) {
                    autenticacionFirebase(cuentaGoogle.getEmail());
                }
            }else{
                Toast.makeText(getApplicationContext(), "Null", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(getApplicationContext(), "Este error saldrá si alguién ha programando a las 1 de la noche y no ha hecho las cosas bien", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Función que verifica la cuenta elegida de Google con Firebase
     * @param emailGoogle cuenta de Google que ha elegido el usuario
     */
    public void autenticacionFirebase(final String emailGoogle) {
        mFirebase.signInWithEmailAndPassword(emailGoogle, emailGoogle).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intentPaginaPrincipal=new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intentPaginaPrincipal);
                    barraProgreso.setVisibility(View.INVISIBLE);
                } else {
                    crearInicioSesion(emailGoogle);
                }
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

    /**
     * Función que crea la cuenta en caso de que no exista a la hora de verificar en Firebase
     * @param emailGoogle
     */
    public void crearInicioSesion(String emailGoogle) {
        mFirebase.createUserWithEmailAndPassword(emailGoogle, emailGoogle).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intentPaginaPrincipal=new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intentPaginaPrincipal);
                } else {
                    Toast.makeText(getApplicationContext(), "Error a la hora de iniciar sesíon", Toast.LENGTH_LONG).show();
                }
                barraProgreso.setVisibility(View.INVISIBLE);
            }
        });
    }

    /**
     * Función que ejecuta justo al abrir la aplicación
     */
    @Override
    protected void onStart() {
        mFirebase=FirebaseAuth.getInstance();
        if (mFirebase.getCurrentUser()!=null) { // Si el usuario ya ha iniciado sesión anteriormente, ira directamente a la página principal
            Intent irDirectamente=new Intent(this, MainActivity.class);
            irDirectamente.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(irDirectamente);
        }
        super.onStart();
    }
}
