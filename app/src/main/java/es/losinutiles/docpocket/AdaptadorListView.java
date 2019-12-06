package es.losinutiles.docpocket;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOError;
import java.util.ArrayList;

public class AdaptadorListView extends BaseAdapter {
    private static LayoutInflater inflater=null;
    private DatabaseReference referencia;
    private MainActivity main;
    private TextView textoTituloVariable;
    private TextView textoFechaHistorial;
    private TextView lenguaje;
    private TextView datolenguaje;
    Context contexto;
    ArrayList<String[]>datos;
    int [] datosImg;
    public AdaptadorListView(){

    }
    public AdaptadorListView(Context contexto, ArrayList<String[]>datos, int[] datosImg) {
        this.contexto = contexto;
        this.datos = datos;
        this.datosImg = datosImg;
        inflater=(LayoutInflater) contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
         View vista=inflater.inflate(R.layout.elemento_lista_historial,null);
        main=new MainActivity();
        textoTituloVariable=(TextView)vista.findViewById(R.id.idTituloVariable);
        textoFechaHistorial=(TextView)vista.findViewById(R.id.idTextoHistorial);
        lenguaje=vista.findViewById(R.id.textView5);
        datolenguaje=vista.findViewById(R.id.textView6);
        ImageView imagenFoto=(ImageView)vista.findViewById((R.id.idImagenFoto));
        ImageView imagenHistorial=(ImageView)vista.findViewById((R.id.idImagenHistorial));
        textoTituloVariable.setText(datos.get(i)[0]);
        textoFechaHistorial.setText(datos.get(i)[1]);
        datolenguaje.setText(datos.get(i)[2]);
        imagenFoto.setImageResource(datosImg[i]);
        imagenHistorial.setImageResource(R.drawable.ic_today_black_24dp);
        imagenHistorial.setTag(i);
        imagenFoto.setTag(i);

        if(main.CargarPreferencia(contexto)){
            textoTituloVariable.setTextColor(contexto.getColor(R.color.Blanco));
            textoFechaHistorial.setTextColor(contexto.getColor(R.color.Blanco));
            lenguaje.setTextColor(contexto.getColor(R.color.Blanco));
            datolenguaje.setTextColor(contexto.getColor(R.color.Blanco));
        }else{
            textoTituloVariable.setTextColor(contexto.getColor(R.color.modoOscuro));
            textoFechaHistorial.setTextColor(contexto.getColor(R.color.modoOscuro));
            lenguaje.setTextColor(contexto.getColor(R.color.modoOscuro));
            datolenguaje.setTextColor(contexto.getColor(R.color.modoOscuro));
        }

        imagenFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent visorImagen=new Intent(contexto,VisorImagen.class);
                visorImagen.putExtra("IMG",datosImg[(Integer) view.getTag()]);
                contexto.startActivity(visorImagen);


            }
        });

        return vista;
    }


    @Override
    public int getCount() {
        return datos.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


}

