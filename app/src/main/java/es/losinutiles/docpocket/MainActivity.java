package es.losinutiles.docpocket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    private AdapterParaFragmentos adapter; // Adapter utilizado para los fragmentos
    private ViewPager viewPager; // Despalzar los fragmentos deslizando hacia la izquierda o derecha
    private Toolbar toolbar; // Lo utilizaremos para mostrar las opciones (Los 3 puntitos) - TODO

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter=new AdapterParaFragmentos(getSupportFragmentManager());
        viewPager=findViewById(R.id.viewPager);
        toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        crearViewPager(viewPager);

        TabLayout tb=findViewById(R.id.Tabs);
        tb.setupWithViewPager(viewPager); // Mete el viewPager creado dentro del TabLayout
        // Meter iconos del tab
        tb.getTabAt(0).setIcon(getDrawable(R.drawable.icono_lista));
        tb.getTabAt(1).setIcon(getDrawable(R.drawable.icono_favorito));
        tb.getTabAt(2).setIcon(getDrawable(R.drawable.icono_soporte));
    }

    /**
     * Función que mete el adapter al viewPager
     * @param vp el viewPager donde le queremos meter el adapter
     */
    public void crearViewPager(ViewPager vp) {
        AdapterParaFragmentos adapter=new AdapterParaFragmentos(getSupportFragmentManager());
        adapter.nuevoFragmento(new TabHistorial());
        adapter.nuevoFragmento(new TabFavoritos());
        adapter.nuevoFragmento(new TabSoporte());

        vp.setAdapter(adapter);
    }
}
