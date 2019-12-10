package es.losinutiles.docpocket;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.SearchEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.zip.Inflater;

public class TabHistorial extends Fragment {
    private MainActivity main;
    private ViewGroup contaner;
    private ListView lista;
    private ArrayList<DatosEscaner> listaDatos=new ArrayList<>();
    private DatabaseReference dFirebase;
    private AdaptadorListView adaptador;
    private SearchView barraBusqueda;
    private ArrayAdapter<DatosEscaner> arrayAdapter;


    /**
     * Aqui introducimos los valores al listView.
     */

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_historial, container, false);
        lista = view.findViewById(R.id.idLista);

        dFirebase=FirebaseDatabase.getInstance().getReference();
        String nombreUsuario=FirebaseAuth.getInstance().getCurrentUser().getEmail().split("@")[0];
        dFirebase.child("DatosUsuario").child(nombreUsuario).child("TabHistorial").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaDatos.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds: dataSnapshot.getChildren()) {
                        DatosEscaner de=ds.getValue(DatosEscaner.class);
                        listaDatos.add(de);
                    }
                    adaptador=new AdaptadorListView(getContext(),listaDatos);
                    lista.setAdapter(adaptador);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        adaptador=new AdaptadorListView(getContext(),listaDatos);
        lista.setAdapter(adaptador);


        /**
         * Esta es la funcion de busqueda.
         */
        barraBusqueda=(SearchView)view.findViewById(R.id.idBusqueda);
        arrayAdapter=new ArrayAdapter<DatosEscaner>(view.getContext(),android.R.layout.simple_list_item_1,listaDatos);
        lista.setAdapter(arrayAdapter);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(view.getContext(),"Tu click"+parent.getItemAtPosition(position),Toast.LENGTH_SHORT).show();

            }
        });
        barraBusqueda.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length()>0) {
                    adaptador.getFilter().filter(newText);
                }
                return false;
            }
        });


        return view;
    }















}

