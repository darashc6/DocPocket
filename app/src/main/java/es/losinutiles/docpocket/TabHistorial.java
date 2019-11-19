package es.losinutiles.docpocket;

import android.graphics.Color;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
<<<<<<< HEAD
import android.widget.Switch;
import android.widget.TextView;
=======
import android.widget.ListView;
>>>>>>> bd425aa25c27399461c0efb6c88cbfdaa61d1771
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

public class TabHistorial extends Fragment {
    private Button boton1;
<<<<<<< HEAD
    private Switch botonSwitch;
=======
    /**
     * Aqui introducimos los valores al listView.
     */
    ListView lista;
    String [][] datos={
            {"PruebaVariable","5 dias"},
            {"PruebaVariable","6 dias"},
            {"PruebaVariable","7 dias"},
            {"PruebaVariable","9 dias"},
            {"PruebaVariable","16 dias"},

    };
    //Todavia no puedo inicializar este array hasta que no tengamos
    //las imagenes guardadas del escaner.

    int[] datosImagenes;

>>>>>>> bd425aa25c27399461c0efb6c88cbfdaa61d1771

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_historial, container, false);
<<<<<<< HEAD
        boton1 = view.findViewById(R.id.boton1);
        botonSwitch=view.findViewById(R.id.botonSwitch);
        boton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Ha hecho clic en el boton del fragmento 1", Toast.LENGTH_SHORT).show();
            }

        });
        return view;
    }

=======

        lista=view.findViewById(R.id.idLista);

        return view;
    }



>>>>>>> bd425aa25c27399461c0efb6c88cbfdaa61d1771
}
