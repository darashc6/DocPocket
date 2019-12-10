package es.losinutiles.docpocket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.text.BoringLayout;
import android.transition.Fade;
import android.transition.Transition;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.Button;
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
    private DatabaseReference dFirebase;
    private FirebaseAuth aFirebase;
    private String usuario;
    private DatabaseReference referencia;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        referencia= FirebaseDatabase.getInstance().getReference();
        dFirebase=FirebaseDatabase.getInstance().getReference();
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
<<<<<<< HEAD

    public void aniadirAFavorito(View view) {

        DatosEscaner de=null;
        Button botonAñadirFavorito;
        Button botonQuitarFavorito;
        botonAñadirFavorito=view.getRootView().findViewById(R.id.botonFav);
        botonQuitarFavorito=view.getRootView().findViewById(R.id.botonFav2);
        TextView nombreClase=view.getRootView().findViewById(R.id.idTituloVariable);

        String nombre=nombreClase.getText().toString();

        TextView fecha=view.getRootView().findViewById(R.id.idTextoHistorial);
        int idImagen=0;
        if (nombre.contains(" Java")) {
            idImagen=R.drawable.icono_java;
        } else {
            idImagen=R.drawable.icono_csharp;
        }
        String nombreUsuario=FirebaseAuth.getInstance().getCurrentUser().getEmail().split("@")[0];
        de=new DatosEscaner(nombreClase.getText().toString(), fecha.getText().toString(), idImagen);
        dFirebase.child("DatosUsuario").child(nombreUsuario).child("TabFavoritos").child(nombreClase.getText().toString()).setValue(de);

        Toast.makeText(this,"Se ha añadido a favoritos",Toast.LENGTH_LONG).show();

        Intent refresh=new Intent(this, MainActivity.class);
        startActivity(refresh);
        this.finish();



        /*if (bundle.getString("lenguajeElegido").equals("Java")) {
            de=new DatosEscaner(palabraEscaneada, "Hoy", R.drawable.icono_java);
        } else {
            de=new DatosEscaner(palabraEscaneada, "Hoy", R.drawable.csharp_icon);
        }

        Intent intentPaginaBusqueda=new Intent(getApplicationContext(), MainActivity.class);
        intentPaginaBusqueda.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intentPaginaBusqueda);
        Toast.makeText(getApplicationContext(), "¡Texto escaneado!", Toast.LENGTH_LONG).show()*/
    }

    public void quitarFavorito(View view) {
        Button botonAñadirFavorito;
        Button botonQuitarFavorito;
        botonAñadirFavorito=view.getRootView().findViewById(R.id.botonFav);
        botonQuitarFavorito=view.getRootView().findViewById(R.id.botonFav2);
        botonAñadirFavorito.setVisibility(View.VISIBLE);
        botonQuitarFavorito.setVisibility(View.INVISIBLE);
        TextView nombreClase=view.getRootView().findViewById(R.id.idTituloVariable);
        Log.d("Datos",nombreClase.getText().toString());
        aFirebase=FirebaseAuth.getInstance();
        usuario=aFirebase.getCurrentUser().getEmail().split("@")[0];
        referencia.child("DatosUsuario").child(usuario).child("TabFavoritos").child(nombreClase.getText().toString()).removeValue();

        Toast.makeText(this,"Se ha borrado "+nombreClase.getText().toString(),Toast.LENGTH_LONG).show();

        Intent refresh=new Intent(this, MainActivity.class);
        startActivity(refresh);
        this.finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(CargarPreferencia(getBaseContext())){
            viewPager.setBackgroundResource(R.color.modoOscuro);
        }else{
            viewPager.setBackgroundResource(R.color.Blanco);
        }
    }
=======
>>>>>>> 3f88409e784ff50f749c4d58d902b16668a071e5
}

