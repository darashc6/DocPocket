package es.losinutiles.docpocket;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdaptadorListView extends ArrayAdapter<DatosEscaner> {
    private MainActivity main; // Actividad principal
    private Button botonQuitarFav;
    private DatabaseReference referencia;
    private TextView textoTituloVariable; // TextView con el nombre de la clase
    private TextView textoFechaHistorial; // TextView con la fecha de la captura
    private Context contexto; // Contexto de la aplicación
    private ArrayList<DatosEscaner>lista; // ArrayList de datos escaneados
    private  String usuario;
    private FirebaseAuth aFirebase;
    /**
     * Constructor de AdaptadorListView
     * @param context Contexto de la aplicación
     * @param lista ArrayList con los datos escaneados
     */
    public AdaptadorListView(Context context,  ArrayList<DatosEscaner> lista) {
        super(context,0,lista);
        this.contexto=context;
        this.lista=lista;
        referencia= FirebaseDatabase.getInstance().getReference();


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
        botonQuitarFav=view.findViewById(R.id.botonFav);

        textoTituloVariable.setText(lista.get(i).getNombreClase());
        textoFechaHistorial.setText(lista.get(i).getDias());
        imagenFoto.setImageResource(lista.get(i).getIdImagen());
        imagenHistorial.setImageResource(R.drawable.ic_today_black_24dp);
        imagenHistorial.setTag(i);
        botonQuitarFav.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                return false;
            }
        });
        if (lista.get(i).getIdImagen()==R.drawable.icono_java) {
            imagenFoto.setTag(R.drawable.icono_java);
        } else {
            imagenFoto.setTag(R.drawable.icono_csharp);
        }

        if(main.CargarPreferencia(contexto)){
            textoTituloVariable.setTextColor(contexto.getColor(R.color.Blanco));
            textoFechaHistorial.setTextColor(contexto.getColor(R.color.Blanco));
        }else{
            textoTituloVariable.setTextColor(contexto.getColor(R.color.modoOscuro));
            textoFechaHistorial.setTextColor(contexto.getColor(R.color.modoOscuro));
        }

        /*imagenFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent visorImagen=new Intent(contexto,VisorImagen.class);
                visorImagen.putExtra("IMG",datosImg[(Integer) view.getTag()]);
                contexto.startActivity(visorImagen);


            }
        });*/

        return view;
    }


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

