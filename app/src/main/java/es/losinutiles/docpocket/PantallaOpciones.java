package es.losinutiles.docpocket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PantallaOpciones extends AppCompatActivity {
    private Switch aSwitch;
    private ConstraintLayout layout;
    private RadioButton java;
    private RadioButton cshar;
    private MainActivity main;
    private FirebaseAuth uFirebase;
    private Activity thisRef;
    private GoogleSignInClient gsic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_pantalla_opciones);
        aSwitch=findViewById(R.id.switch1);
        java=findViewById(R.id.radioButton);
        cshar=findViewById(R.id.radioButton2);
        layout=findViewById(R.id.fondo);
        main=new MainActivity();

        uFirebase=FirebaseAuth.getInstance();
        thisRef=this;
        GoogleSignInOptions gsio=new GoogleSignInOptions.Builder()
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        gsic=GoogleSignIn.getClient(getApplicationContext(), gsio);


        aSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(aSwitch.isChecked()){
                    main.guardarDatosOscuro(getBaseContext(),aSwitch);
                    aSwitch.setTextColor(getColor(R.color.Blanco));
                    layout.setBackgroundResource(R.color.modoOscuro);
                    java.setTextColor(getColor(R.color.Blanco));
                    cshar.setTextColor(getColor(R.color.Blanco));
                }else{
                    main.guardarDatosOscuro(getBaseContext(),aSwitch);
                    layout.setBackgroundResource(R.color.Blanco);
                    aSwitch.setTextColor(getColor(R.color.modoOscuro));
                    java.setTextColor(getColor(R.color.modoOscuro));
                    cshar.setTextColor(getColor(R.color.modoOscuro));
                }
            }
        });
    }


    public void guardarAjustes(View view) {
        Intent intent=new Intent(getBaseContext(),MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(main.CargarPreferencia(getBaseContext())){
            aSwitch.setChecked(true);
            aSwitch.setTextColor(getColor(R.color.Blanco));
            layout.setBackgroundResource(R.color.modoOscuro);
            java.setTextColor(getColor(R.color.Blanco));
            cshar.setTextColor(getColor(R.color.Blanco));
        }else{
            aSwitch.setChecked(false);
            aSwitch.setTextColor(getColor(R.color.modoOscuro));
            layout.setBackgroundResource(R.color.Blanco);
            java.setTextColor(getColor(R.color.modoOscuro));
            cshar.setTextColor(getColor(R.color.modoOscuro));
        }
    }

    public void selecLenguaje(View view) {
        cshar.setChecked(false);
        Toast.makeText(getBaseContext(),main.guardarLenguajeDefecto(getBaseContext(),"Java"),Toast.LENGTH_LONG).show();
        Toast.makeText(getBaseContext(),main.CargarLenguaje(getBaseContext()),Toast.LENGTH_LONG).show();
    }

    public void selecLenguaje2(View view) {
        java.setChecked(false);
        Toast.makeText(getBaseContext(),main.guardarLenguajeDefecto(getBaseContext(),"C#"),Toast.LENGTH_LONG).show();
        Toast.makeText(getBaseContext(),main.CargarLenguaje(getBaseContext()),Toast.LENGTH_LONG).show();
    }

    /**
     * Muestra un diálogo de alerta al darle a la opción de cerrar sesion
     * @param view view del botón
     */
    public void cerrarSesion(View view) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setIcon(R.drawable.ic_warning_black_24dp);
        dialogo.setTitle(getResources().getString(R.string.tituloDialogo));
        dialogo.setMessage(getResources().getString(R.string.mensajeDialogo));

        // Configuración del botón negativo ('No')
        dialogo.setNegativeButton(getResources().getString(R.string.botonNegativoDialogo), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        // Configuración del botón positivo ('Si')
        // Si el botón se llega a pulsar el botón 'Si', se va a cerrar la sesión de la cuenta de Google y redirigir a la pantalla inicial
        dialogo.setPositiveButton(getResources().getString(R.string.botonPositivoDoalogo), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                uFirebase.signOut();
                gsic.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.textoSesionCerrada), Toast.LENGTH_LONG).show();
                            Intent paginaInicio=new Intent(getApplicationContext(), ActividadInicial.class);
                            paginaInicio.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(paginaInicio);
                        }
                    }
                });
            }
        });

        dialogo.show();
    }
}
