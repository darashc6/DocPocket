package es.losinutiles.docpocket;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class TabHistorial extends Fragment {
    private MainActivity main;
    private ViewGroup contaner;
    /**
     * Aqui introducimos los valores al listView.
     */
    ArrayList<String[]>array=new ArrayList<String[]>();


    ListView lista;
    String[][] datos = {
            {"PruebaVariable", "5 dias","Java"},
            {"PruebaVariable", "6 dias","C#"},
            {"PruebaVariable", "7 dias","Java"},
            {"PruebaVariable", "9 dias","C#"},
            {"PruebaVariable", "16 dias","Java"},

    };
    //Todavia no puedo inicializar este array hasta que no tengamos
    //las imagenes guardadas del escaner.

    int[] datosImg = {R.drawable.csharp_icon, R.drawable.csharp_icon, R.drawable.csharp_icon, R.drawable.csharp_icon,R.drawable.csharp_icon};

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_historial, container, false);
        lista = view.findViewById(R.id.idLista);
        main=new MainActivity();
        this.contaner=container;
        AdaptadorListView adapter=new AdaptadorListView(getContext(),array,datosImg);
        lista.setAdapter(adapter);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        main=new MainActivity();
        if(!main.CargarPreferencia(getContext())){
            contaner.setBackgroundResource(R.color.Blanco);

        }else{
            contaner.setBackgroundResource(R.color.modoOscuro);
        }
        for(int z=0;z<datos.length;z++){
            if (main.CargarLenguaje(getContext()).equals(datos[z][2])) {
                array.add(datos[z]);
            } else if (main.CargarLenguaje(getContext()).equals(datos[z][2])) {
                array.add(datos[z]);
            } else if(main.CargarLenguaje(getContext()).equals("Todos")) {
                array.add(datos[z]);
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        array.clear();
    }
}

