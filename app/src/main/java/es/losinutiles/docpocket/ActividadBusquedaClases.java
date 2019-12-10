package es.losinutiles.docpocket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ActividadBusquedaClases extends AppCompatActivity {
    private TextView claseEncontrada; // Texto con la clase encontrada
    private ImageView imagenLenguajeElegido; // Imagen con el lenguaje selecionado

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_actividad_busqueda_clases);

        Bundle b=getIntent().getExtras();
        claseEncontrada=findViewById(R.id.idTituloVariable);
        imagenLenguajeElegido=findViewById(R.id.idImagenFoto);
        claseEncontrada.setText(b.getString("claseEscaneado"));
        imagenLenguajeElegido.setBackgroundResource(b.getInt("idImagen"));

        getWindow().getSharedElementEnterTransition().setDuration(1250L);
        slideUpDown();
    }

    /**
     * Función que busca la clase encontrada por StackOverflow
     * @param view No nos hace falta en este caso
     */
    public void buscarStackOverFlow(View view) {
        Intent webIntent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://stackoverflow.com/search?q="+claseEncontrada.getText()));
        try{
            startActivity(webIntent);
        }catch (ActivityNotFoundException ex){
            Toast.makeText(this,"Ha ocurrido un error, intentalo de nuevo",Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Función que busca la clase encontrada por Youtube
     * @param view No nos hace falta en este caso
     */
    public void buscarYoutube(View view) {
        Intent appIntent=new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.youtube.com/results?search_query="+claseEncontrada.getText()));
        Intent webIntent=new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.youtube.com/results?search_query="+claseEncontrada.getText()));
        try{
            startActivity(appIntent);
        }catch (ActivityNotFoundException ex){
            startActivity(webIntent);
        }
    }

    /**
     * Función que buscar la clase encontrada por Google
     * @param view No nos hace falta en este caso
     */
    public void buscarGoogle(View view) {
        Intent webIntent=new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.google.com/search?q="+claseEncontrada.getText()));
        try{
            startActivity(webIntent);
        }catch (ActivityNotFoundException ex){
            Toast.makeText(this,"Ha ocurrido un error, intentalo de nuevo",Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Función de transición de deslizar (desde abajo hacia arriba)
     */
    public void slideUpDown() {
        Slide slide=new Slide(Gravity.BOTTOM);
        slide.setDuration(2000L);
        slide.setInterpolator(AnimationUtils.loadInterpolator(getApplicationContext(), android.R.interpolator.linear_out_slow_in));
        getWindow().setEnterTransition(slide);
    }

    @Override
    public void onBackPressed() {
        Intent i=new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        super.onBackPressed();
    }
}
