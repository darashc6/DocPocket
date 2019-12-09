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
    ArrayList<DatosEscaner> listaDatos=new ArrayList<>();

    /**
     * Aqui introducimos los valores al listView.
     */
   //  ArrayList<String[]>array=new ArrayList<String[]>();

    //Todavia no puedo inicializar este array hasta que no tengamos
    //las imagenes guardadas del escaner.

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_historial, container, false);
        lista = view.findViewById(R.id.idLista);
        spinnerCategoria=view.findViewById(R.id.idSpinnerLenguajes);
        spinnerCategoria.setAdapter(new ArrayAdapter<>(view.getContext(),android.R.layout.simple_list_item_1,categorias));

        Bundle b=getActivity().getIntent().getExtras();
        if (b==null) {
            Toast.makeText(getContext(), "No se que estoy haciendo", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), "No se que estoy haciendo, pero voy bien", Toast.LENGTH_LONG).show();
            String nombreClase=b.getString("nombreClase");
            int idImagen=b.getInt("idImagenLenguaje");
            listaDatos.add(new DatosEscaner(nombreClase, "njfekn", idImagen));
        }

        /*listaDatos.add(new DatosEscaner("FileWriter","5 dias",R.drawable.icono_java));
        listaDatos.add(new DatosEscaner("BufferedReader","6 dias",R.drawable.icono_csharp));
        listaDatos.add(new DatosEscaner("Lock","8 dias",R.drawable.icono_java));
        listaDatos.add(new DatosEscaner("Run","10 dias",R.drawable.icono_java));
        listaDatos.add(new DatosEscaner("InputStreamReader","13 dias",R.drawable.icono_java));
        listaDatos.add(new DatosEscaner("Random","16 dias",R.drawable.icono_csharp));
        listaDatos.add(new DatosEscaner("Timer","19 dias",R.drawable.icono_csharp));*/

        SearchView barraBusqueda=(SearchView)view.findViewById(R.id.idBusqueda);

        main=new MainActivity();
        this.contaner=container;
        final AdaptadorListView adaptador=new AdaptadorListView(getContext(),listaDatos);

        lista.setAdapter(adaptador);

        // initializeViews(view);


        /**
         * Esta es la funcion de la barra de busqueda.
         */
        barraBusqueda.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                for(int i=0;i<listaDatos.size();i++){

                    if(listaDatos.get(i).getNombreClase().contains(query)){

                        Toast.makeText(getContext(), "Soy tonto",Toast.LENGTH_LONG).show();
                        adaptador.getFilter().filter(query);

                        break;
                    }

                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                    adaptador.getFilter().filter(newText);
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

