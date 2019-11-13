package es.losinutiles.docpocket;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TabHistorial extends Fragment {
    private Button boton1;
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


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_historial, container, false);

        lista=view.findViewById(R.id.idLista);

        return view;
    }



}
