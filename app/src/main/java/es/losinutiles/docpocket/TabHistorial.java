package es.losinutiles.docpocket;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ListView;
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
    private Switch botonSwitch;
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

    int[] datosImg={R.drawable.csharp_icon,R.drawable.csharp_icon,R.drawable.csharp_icon,R.drawable.csharp_icon};

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_historial, container, false);
        lista=view.findViewById(R.id.idLista);

        lista.setAdapter(new AdaptadorListView(getContext(),datos,datosImg));
        return view;
    }


    }

