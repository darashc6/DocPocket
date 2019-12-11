package es.losinutiles.docpocket;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collection;

public class AdaptadorListView extends ArrayAdapter<DatosEscaner>implements Filterable {
    private MainActivity main; // Actividad principal
    private Button botonFavorito;
    private DatabaseReference referencia;
    private TextView textoTituloVariable; // TextView con el nombre de la clase
    private TextView textoFechaHistorial; // TextView con la fecha de la captura
    private Context contexto; // Contexto de la aplicación
    private ArrayList<DatosEscaner>lista; // ArrayList de datos escaneados
    private ArrayList<DatosEscaner>fullLista; // ArrayList de datos escaneados
    private String usuario; // Nombre de usuario
    /**
     * Constructor de AdaptadorListView
     * @param context Contexto de la aplicación
     * @param lista ArrayList con los datos escaneados
     */
    public AdaptadorListView(Context context,  ArrayList<DatosEscaner> lista) {
        super(context,0,lista);
        this.contexto=context;
        this.lista=lista;
        fullLista=new ArrayList<>(lista);
        referencia= FirebaseDatabase.getInstance().getReference();
        usuario=FirebaseAuth.getInstance().getCurrentUser().getEmail().split("@")[0];
    }

    /**
     * Devuelve el view de cada elemento del listview
     * @param i Posición del elemento
     * @param view view del elemento
     * @param viewGroup conjunto de view
     * @return view del elemento
     */
    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater=((Activity)contexto).getLayoutInflater();
        view =inflater.inflate(R.layout.elemento_lista_historial,null);
        main=new MainActivity();

        textoTituloVariable=(TextView)view.findViewById(R.id.idTituloVariable);
        textoFechaHistorial=(TextView)view.findViewById(R.id.idTextoHistorial);
        ImageView imagenFoto=(ImageView)view.findViewById((R.id.idImagenFoto));
        ImageView imagenHistorial=(ImageView)view.findViewById((R.id.idDiasConsulta));
        botonFavorito=view.findViewById(R.id.botonFav);

        textoTituloVariable.setText(lista.get(i).getNombreClase());
        textoFechaHistorial.setText(lista.get(i).getDias());
        if (lista.get(i).getLenguaje().equals("Java")) {
            imagenFoto.setBackgroundResource(R.drawable.icono_java);
            imagenFoto.setTag(R.drawable.icono_java);
        } else {
            imagenFoto.setBackgroundResource(R.drawable.icono_csharp);
            imagenFoto.setTag(R.drawable.icono_csharp);
        }
        imagenHistorial.setImageResource(R.drawable.ic_today_black_24dp);
        imagenHistorial.setTag(i);

        if(main.CargarPreferencia(contexto)){
            textoTituloVariable.setTextColor(contexto.getColor(R.color.Blanco));
            textoFechaHistorial.setTextColor(contexto.getColor(R.color.Blanco));
        }else{
            textoTituloVariable.setTextColor(contexto.getColor(R.color.modoOscuro));
            textoFechaHistorial.setTextColor(contexto.getColor(R.color.modoOscuro));
        }

        botonFavorito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!lista.get(i).isFavorito()) {
                    lista.get(i).setFavorito(true);
                    referencia.child("DatosUsuario").child(usuario).child("TabFavoritos").child(lista.get(i).getNombreClase()).setValue(lista.get(i));
                    Toast.makeText(contexto, lista.get(i).getNombreClase()+": Añadido a favoritos", Toast.LENGTH_LONG).show();
                } else {
                    lista.get(i).setFavorito(false);
                    referencia.child("DatosUsuario").child(usuario).child("TabFavoritos").child(lista.get(i).getNombreClase()).removeValue();
                    Toast.makeText(contexto, lista.get(i).getNombreClase()+": Borrado de favoritos", Toast.LENGTH_LONG).show();
                }
            }
        });


        return view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return filertBueno;
    }
    private Filter filertBueno=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results=new FilterResults();
            ArrayList<DatosEscaner>lista2=new ArrayList<DatosEscaner>();
            if(constraint==null||constraint.length()==0){
                lista2.addAll(fullLista);
            }else{
                String filter=constraint.toString().toLowerCase().trim();
                for(DatosEscaner date:lista){
                    if(date.getNombreClase().toLowerCase().contains(filter)) {
                        lista2.add(date);
                    }

                }
                results.values = lista2;
                return results;
            }
            results.values = lista2;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            lista.clear();
            lista.addAll((ArrayList)results.values);
            notifyDataSetChanged();
        }
    };




    /**
     * Función que cuenta el nº de elementos que contiene el listview
     * @return Nº de elementos del listview
     */
    @Override
    public int getCount() {
        return lista.size();
    }

    /**
     * Función que devuelve el id del elemento del listview
     * @param i Posicion del elemento
     * @return 0
     */
    @Override
    public long getItemId(int i) {
        return 0;
    }


}
