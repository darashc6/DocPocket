package es.losinutiles.docpocket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.text.BoringLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.internal.NavigationMenu;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOError;

import io.github.yavski.fabspeeddial.FabSpeedDial;

public class MainActivity extends AppCompatActivity  {
    private AdapterParaFragmentos adapter; // Adapter utilizado para los fragmentos
    private ViewPager viewPager; // Despalzar los fragmentos deslizando hacia la izquierda o derecha
    private Toolbar toolbar; // Lo utilizaremos para mostrar las opciones (Los 3 puntitos)
    private FabSpeedDial opcionesCamara; // Botón con la sopciones de elegir el lenguaje
    private TabLayout tb; // Layout del tab
    private Boolean modoDark; // Switch para cambiar el modo osucro
    private FirebaseAuth uFirebase; // Autenticación de Firebase
    private FirebaseUser usuarioGoogle; // Usuario de Firebase


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        adapter=new AdapterParaFragmentos(getSupportFragmentManager());
        viewPager=findViewById(R.id.viewPager);
        toolbar=findViewById(R.id.toolbar);
        opcionesCamara=findViewById(R.id.opcionesCamara);
        setSupportActionBar(toolbar);
        crearViewPager(viewPager);
        tb=findViewById(R.id.Tabs);
        tb.setupWithViewPager(viewPager); // Mete el viewPager creado dentro del TabLayout
        // Meter iconos del tab
        tb.getTabAt(0).setIcon(getDrawable(R.drawable.icono_lista));
        tb.getTabAt(1).setIcon(getDrawable(R.drawable.icono_favorito));
        tb.getTabAt(2).setIcon(getDrawable(R.drawable.icono_soporte));
        opcionesParaCamara();
    }

    /**
     * Función que mete el adapter al viewPager
     * @param vp el viewPager donde le queremos meter el adapter
     */
    public void crearViewPager(ViewPager vp) {
        adapter.nuevoFragmento(new TabHistorial());
        adapter.nuevoFragmento(new TabFavoritos());
        adapter.nuevoFragmento(new TabSoporte());
        vp.setAdapter(adapter);
    }

    /**
     * Función que mete las opciones del lenguaje en el icono del FAB
     * Pasará por bundle el lenguaje que se ha elegido, para luego meter al listview del lenguaje que pertenezca
     */
    public void opcionesParaCamara() {
        opcionesCamara.setMenuListener(new FabSpeedDial.MenuListener() {
            @Override
            public boolean onPrepareMenu(NavigationMenu navigationMenu) {
                return true;
            }

            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                Bundle b=new Bundle();
                b.putString("lenguajeElegido", menuItem.getTitle().toString());
                Intent intentCamara=new Intent(getApplicationContext(), ActividadCamara.class);
                intentCamara.putExtras(b);
                startActivity(intentCamara);
                return true;
            }

            @Override
            public void onMenuClosed() {
            }
        });
    }

    /**
     * Función que crea las opciones del menu
     * @param menu menu en la que quieres meter las opciones
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Función que muestra la pantalla de ajustes
     */
    public void modoOscuro(MenuItem item) {
        Intent intent=new Intent(getBaseContext(),PantallaOpciones.class);
        startActivity(intent);
    }

    /**
     * Función que guarda la preferencia del switch del modo oscuro
     * @param context Contexto de la aplicación
     * @param oscuro Switch del modo oscuro
     */
    public void guardarDatosOscuro(Context context,Switch oscuro){
        if(oscuro.isChecked()){
            SharedPreferences preferences=context.getSharedPreferences("modoOscuro", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=preferences.edit();
            editor.putBoolean("idOscuro",true);
            editor.commit();
        }else{
            SharedPreferences preferences=context.getSharedPreferences("modoOscuro", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=preferences.edit();
            editor.putBoolean("idOscuro",false);
            editor.commit();
        }
    }

    /**
     * Función que guarda las preferencias del lenguaje por defecto
     * @param context Contexto de la aplicación
     * @param lenguaje El lenguaje que ha elegido el usuario
     * @return El lenguaje que ha elegido el usuario
     */
    public String guardarLenguajeDefecto(Context context, String lenguaje){
        if(lenguaje=="Java"){
            SharedPreferences preferences=context.getSharedPreferences("lenguaje", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=preferences.edit();
            editor.putString("idLenguaje",lenguaje);
            editor.commit();
        }else{
            SharedPreferences preferences=context.getSharedPreferences("lenguaje", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=preferences.edit();
            editor.putString("idLenguaje",lenguaje);
            editor.commit();
        }
        return lenguaje;
    }

    /**
     * Función que carga la preferencia del lenguaje por defecto elegido por el usuario
     * @param context Contexto de la aplicación
     * @return El lenguajpor defecto
     */
    public String CargarLenguaje(Context context){
        SharedPreferences preferences=context.getSharedPreferences("lenguaje", Context.MODE_PRIVATE);
        return preferences.getString("idLenguaje","Todos");

    }

    /**
     * Función que carga la preferencia del switch de modo oscuro
     * @param context Contexto de la aplicación
     * @return Boolean de si el switch ha cambiado o no
     */

    public Boolean CargarPreferencia(Context context){
        SharedPreferences preferences=context.getSharedPreferences("modoOscuro", Context.MODE_PRIVATE);
        return preferences.getBoolean("idOscuro",false);


    }

    /**
     * Función que ejecuta cuando se presiona el botón de 'atrás'
     */
    @Override
    public void onBackPressed() {
        finishAffinity();
        super.onBackPressed();
    }

    /**
     * Función que muestra la actividad de busqueda de la clase
     * Se usa al darle clic al item del listview
     * @param view layout del que viene
     */
    public void mostrarActividadBusqueda(View view) {
        TextView nombreClase=view.findViewById(R.id.idTituloVariable);
        ImageView idImagenLenguage=view.findViewById(R.id.idImagenFoto);
        // Toast.makeText(this, nombreClase.getText().toString(), Toast.LENGTH_SHORT).show();
        Intent actividadBusqueda=new Intent(this, ActividadBusquedaClases.class);
        Bundle b=new Bundle();
        b.putString("claseEscaneado", nombreClase.getText().toString());
        b.putInt("idImagen", Integer.parseInt(idImagenLenguage.getTag().toString()));
        actividadBusqueda.putExtras(b);

        // Para hacer animaciones compartidas
        Pair<View, String> p1=Pair.create((View) nombreClase, getResources().getString(R.string.transicionNombreClase));
        Pair<View, String> p2=Pair.create((View) idImagenLenguage, getResources().getString(R.string.transicionImagen));
        ActivityOptionsCompat opt=ActivityOptionsCompat.makeSceneTransitionAnimation(this, p1, p2);
        startActivity(actividadBusqueda, opt.toBundle());
    }

    /**
     * Función que ejecuta después de iniciar la actividad
     */
    @Override
    protected void onStart() {
        uFirebase=FirebaseAuth.getInstance();
        usuarioGoogle=uFirebase.getCurrentUser();
        if (usuarioGoogle!=null) {
            Toast.makeText(getApplicationContext(), "Iniciado sesión como: "+ usuarioGoogle.getEmail(), Toast.LENGTH_LONG).show();
        }
        super.onStart();
    }
}

