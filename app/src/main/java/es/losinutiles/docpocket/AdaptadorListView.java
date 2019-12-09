package es.losinutiles.docpocket;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class AdaptadorListView extends BaseAdapter {
    private static LayoutInflater inflater=null;
    private DatabaseReference referencia;
    private MainActivity main;
    private TextView textoTituloVariable;
    private TextView textoFechaHistorial;
    private Context contexto;
    ArrayList<String> nombreClase;
    ArrayList<String> dias;
    ArrayList<Integer> idImagen;
    int [] datosImg;
    public AdaptadorListView(){

    }
    public AdaptadorListView(Context contexto, ArrayList<String> nombreClase, ArrayList<String> dias, ArrayList<Integer> idImagen) {
        this.contexto = contexto;
        this.nombreClase=nombreClase;
        this.dias=dias;
        this.idImagen=idImagen;
        this.datosImg = datosImg;
        inflater=(LayoutInflater) contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
         View vista=inflater.inflate(R.layout.elemento_lista_historial,null);
        main=new MainActivity();
        textoTituloVariable=(TextView)vista.findViewById(R.id.idTituloVariable);
        textoFechaHistorial=(TextView)vista.findViewById(R.id.idTextoHistorial);
        ImageView imagenFoto=(ImageView)vista.findViewById((R.id.idImagenFoto));
        ImageView imagenHistorial=(ImageView)vista.findViewById((R.id.idDiasConsulta));
        textoTituloVariable.setText(nombreClase.get(i));
        textoFechaHistorial.setText(dias.get(i));
        imagenFoto.setImageResource(idImagen.get(i));
        imagenHistorial.setImageResource(R.drawable.ic_today_black_24dp);
        imagenHistorial.setTag(i);
        imagenFoto.setTag(i);

        if(main.CargarPreferencia(contexto)){
            textoTituloVariable.setTextColor(contexto.getColor(R.color.Blanco));
            textoFechaHistorial.setTextColor(contexto.getColor(R.color.Blanco));
        }else{
            textoTituloVariable.setTextColor(contexto.getColor(R.color.modoOscuro));
            textoFechaHistorial.setTextColor(contexto.getColor(R.color.modoOscuro));
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
        return nombreClase.size();
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

