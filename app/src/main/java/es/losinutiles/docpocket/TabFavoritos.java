package es.losinutiles.docpocket;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TabFavoritos extends Fragment {
    private DatosEscaner de; //DatosEscaner son los datos que introduciremos en el ArrayList de DatosEscaner
    private MainActivity main;
    private ViewGroup contaner;
    private ListView lista;
    private Spinner spinnerCategoria;//Spinner para ver el lenguaje
    private String []categorias={"Todo","Java","C#"};
    private ArrayList<DatosEscaner> listaDatos=new ArrayList<>();
    private DatabaseReference dFirebase;
    private AdaptadorListView adaptador;
    private boolean datosMostrados=false;
    private long tamanioInicial=0;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
         View view = inflater.inflate(R.layout.fragment_tab_favoritos, container, false);
         lista=view.findViewById(R.id.listViewFav);
         //Inicia instancia de firebase
         dFirebase= FirebaseDatabase.getInstance().getReference();
         String emailUsuario= FirebaseAuth.getInstance().getCurrentUser().getEmail().split("@")[0];
        dFirebase.child("DatosUsuario").child(emailUsuario).child("TabFavoritos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               //limpia listaDatos
                listaDatos.clear();
                //Si existe la consulta, añade a listaDatos
                if(dataSnapshot.exists()){
                    long nEscaneado=dataSnapshot.getChildrenCount();
                    for (DataSnapshot ds: dataSnapshot.getChildren()) {
                        de=ds.getValue(DatosEscaner.class);
                        listaDatos.add(de);
                        //Añade a listaDatos DatosEscaner de, el cual pertenecía a la bbd de TabHistorial
                    }
                    tamanioInicial=nEscaneado;
                }
                adaptador=new AdaptadorListView(getContext(),listaDatos);
                lista.setAdapter(adaptador);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        adaptador=new AdaptadorListView(getContext(),listaDatos);
        lista.setAdapter(adaptador);

        return view;
    }


}

