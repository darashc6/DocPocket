package es.losinutiles.docpocket;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
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
   //  ArrayList<String[]>array=new ArrayList<String[]>();


    ListView lista;
    Spinner spinnerCategoria;
    ArrayAdapter<Objetos> adapter;
    String []categorias={"Todo","Java","C#"};
    ArrayList<String> nombreClase=new ArrayList<String>();
    ArrayList<String> dias=new ArrayList<String>();
    ArrayList<Integer> idImagen=new ArrayList<Integer>();
    //Todavia no puedo inicializar este array hasta que no tengamos
    //las imagenes guardadas del escaner.

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_historial, container, false);
        lista = view.findViewById(R.id.idLista);
        spinnerCategoria=view.findViewById(R.id.idSpinnerLenguajes);

        spinnerCategoria.setAdapter(new ArrayAdapter<>(view.getContext(),android.R.layout.simple_list_item_1,categorias));

        nombreClase.add("FileWriter");
        nombreClase.add("Integer");
        nombreClase.add("FileLock");
        nombreClase.add("FileChannel");
        nombreClase.add("String");

        dias.add("5 dias");
        dias.add("6 dias");
        dias.add("7 dias");
        dias.add("8 dias");
        dias.add("10 dias");

        idImagen.add(R.drawable.icono_csharp);
        idImagen.add(R.drawable.icono_java);
        idImagen.add(R.drawable.icono_csharp);
        idImagen.add(R.drawable.icono_java);
        idImagen.add(R.drawable.icono_csharp);



        main=new MainActivity();
        this.contaner=container;
        AdaptadorListView adapter=new AdaptadorListView(getContext(),nombreClase,dias, idImagen);
        lista.setAdapter(adapter);

        // initializeViews(view);

        return view;
    }

   /*@Override
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
    }*/

    /* @Override
    public void onStop() {
        super.onStop();
        array.clear();
    }*/

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

