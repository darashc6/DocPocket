package es.losinutiles.docpocket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ActividadBusquedaClases extends AppCompatActivity {
    private TextView claseEncontrada;
    private ImageView imagenLenguajeElegido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_busqueda_clases);
        Bundle b=getIntent().getExtras();
        claseEncontrada=findViewById(R.id.idTituloVariable);
        imagenLenguajeElegido=findViewById(R.id.idImagenFoto);

        claseEncontrada.setText(b.getString("nombreClase"));
        imagenLenguajeElegido.setBackgroundResource(b.getInt("idImagenLenguaje"));
    }

    public void buscarStackOverFlow(View view) {
        Intent webIntent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://stackoverflow.com/search?q="+claseEncontrada.getText()));
        try{
            startActivity(webIntent);
        }catch (ActivityNotFoundException ex){
            Toast.makeText(this,"Ha ocurrido un error, intentalo de nuevo",Toast.LENGTH_LONG).show();
        }
    }

    public void buscarYoutube(View view) {
        Intent appIntent=new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.youtube.com/results?search_query="+claseEncontrada.getText()));
        Intent webIntent=new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.youtube.com/results?search_query="+claseEncontrada.getText()));
        try{
            startActivity(appIntent);
        }catch (ActivityNotFoundException ex){
            startActivity(webIntent);
        }
    }

    public void buscarGoogle(View view) {
        Intent webIntent=new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.google.com/search?q="+claseEncontrada.getText()));
        try{
            startActivity(webIntent);
        }catch (ActivityNotFoundException ex){
            Toast.makeText(this,"Ha ocurrido un error, intentalo de nuevo",Toast.LENGTH_LONG).show();
        }
    }
}
