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
    private Spinner spinnerCategoria;
    private ArrayAdapter<Objetos> adapter;
    private String []categorias={"Todo","Java","C#"};
    private ArrayList<DatosEscaner> listaDatos=new ArrayList<>();
    private DatabaseReference dFirebase;
    private AdaptadorListView adaptador;
    private boolean datosMostrados=false;
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
        spinnerCategoria=view.findViewById(R.id.idSpinnerLenguajes);
        spinnerCategoria.setAdapter(new ArrayAdapter<>(view.getContext(),android.R.layout.simple_list_item_1,categorias));

        dFirebase=FirebaseDatabase.getInstance().getReference();
        String nombreUsuario=FirebaseAuth.getInstance().getCurrentUser().getEmail().split("@")[0];
        if (!datosMostrados) {
            dFirebase.child("DatosUsuario").child(nombreUsuario).child("TabHistorial").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        long nEscaneado=dataSnapshot.getChildrenCount();
                        if (nEscaneado>0) {
                            for (DataSnapshot ds: dataSnapshot.getChildren()) {
                                DatosEscaner de=ds.getValue(DatosEscaner.class);
                                listaDatos.add(de);
                            }
                            adaptador=new AdaptadorListView(getContext(),listaDatos);
                            lista.setAdapter(adaptador);
                            datosMostrados=true;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


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
                TabHistorial.this.arrayAdapter.getFilter().filter(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                TabHistorial.this.arrayAdapter.getFilter().filter(newText);
                return false;
            }
        });



        return view;
    }

    private void initializeViews(View view){

        spinnerCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if(position >=0 && position<categorias.length){
                    getSelectedCategoryData(position);

                }else{
                    Toast.makeText(view.getContext(),"La categoria seleccionada no existe",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


    private ArrayList<Objetos> getLenguajes(){
        ArrayList<Objetos> data=new ArrayList<>();
        data.clear();
        data.add(new Objetos("Prueba Java",1));
        data.add(new Objetos("Prueba C#",2));
        return data;
    }

    private void getSelectedCategoryData(int categoriaID){
        ArrayList<Objetos> lenguajes=new ArrayList<>();
        if(categoriaID == 0){
            adapter=new ArrayAdapter<Objetos>(getView().getContext(),android.R.layout.simple_list_item_1,getLenguajes());

        }else{
            for(Objetos lenguaje : getLenguajes()){
                if(lenguaje.getCategoriaId()==categoriaID){
                    lenguajes.add(lenguaje);
                }
            }

            adapter= new ArrayAdapter<Objetos>(getView().getContext(),android.R.layout.simple_list_item_1,lenguajes);
        }

        lista.setAdapter(adapter);
    }


    //Esta clase representa a un objeto que hayamos escaneado , por ejemplo. FileWriter, BufferedReader, etc...

    class Objetos{
        private String nombre;
        private int categoriaId;

        public String getNombre(){
            return nombre;
        }

        public int getCategoriaId(){
            return categoriaId;

        }

        public Objetos (String n,int cid){
            this.nombre=n;
            this.categoriaId=cid;
        }



    }




}

