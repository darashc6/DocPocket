package es.losinutiles.docpocket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.Toast;

public class PantallaOpciones extends AppCompatActivity {
    private Switch aSwitch;
    private ConstraintLayout layout;
    private RadioButton java;
    private RadioButton cshar;
    private MainActivity main;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_opciones);
        aSwitch=findViewById(R.id.switch1);
        java=findViewById(R.id.radioButton);
        cshar=findViewById(R.id.radioButton2);
        layout=findViewById(R.id.fondo);
        main=new MainActivity();
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

    @Override
    public void onBackPressed() {

    }
}
