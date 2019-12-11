package es.losinutiles.docpocket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

public class VisorImagen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /*
         *VisorImagen se encarga de generar el Layout de la imagen seleccionada, como transición
         * hacia la ventana con los botones para realizar la busqueda
         */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visor_imagen);
        ImageView img=(ImageView)findViewById(R.id.visorImagenCompleta);
        Intent intent =getIntent();
        Bundle b =intent.getExtras();
        if(b!=null){
            img.setImageResource(b.getInt("IMG"));
        }
    }
}
