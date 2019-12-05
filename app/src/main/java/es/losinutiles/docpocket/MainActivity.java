package es.losinutiles.docpocket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
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
import android.widget.Adapter;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.internal.NavigationMenu;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOError;

import io.github.yavski.fabspeeddial.FabSpeedDial;

public class MainActivity extends AppCompatActivity {
    private AdapterParaFragmentos adapter; // Adapter utilizado para los fragmentos
    private ViewPager viewPager; // Despalzar los fragmentos deslizando hacia la izquierda o derecha
    private Toolbar toolbar; // Lo utilizaremos para mostrar las opciones (Los 3 puntitos) - TODO
    private FabSpeedDial opcionesCamara;
    private TabLayout tb;
    private Boolean modoDark;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adapter=new AdapterParaFragmentos(getSupportFragmentManager());
        viewPager=findViewById(R.id.viewPager);
        toolbar=findViewById(R.id.toolbar);
        opcionesCamara=findViewById(R.id.opcionesCamara);
        toolbar.setTitle("DOCKPOCKET V1.0");
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


    public Boolean CargarPreferencia(Context context){
        SharedPreferences preferences=context.getSharedPreferences("modoOscuro", Context.MODE_PRIVATE);
        return preferences.getBoolean("idOscuro",false);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void modoOscuro(MenuItem item) {
    Intent intent=new Intent(getBaseContext(),PantallaOpciones.class);
    startActivity(intent);
    }

}

