package es.losinutiles.docpocket;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdaptadorListView extends BaseAdapter {
    private static LayoutInflater inflater=null;
    Context contexto;
    String [][] datos;
    int [] datosImg;

    public AdaptadorListView(Context contexto, String[][] datos, int[] datosImg) {
        this.contexto = contexto;
        this.datos = datos;
        this.datosImg = datosImg;
        inflater=(LayoutInflater) contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        final View vista=inflater.inflate(R.layout.elemento_lista_historial,null);
        TextView textoTituloVariable=(TextView)vista.findViewById(R.id.idTituloVariable);
        TextView textoFechaHistorial=(TextView)vista.findViewById(R.id.idTextoHistorial);
        ImageView imagenFoto=(ImageView)vista.findViewById((R.id.idImagenFoto));
        ImageView imagenHistorial=(ImageView)vista.findViewById((R.id.idImagenHistorial));
        ImageView imagenFlecha=(ImageView)vista.findViewById((R.id.idImagenFlecha));
        textoTituloVariable.setText(datos[i][0]);
        textoFechaHistorial.setText(datos[i][1]);
        imagenFoto.setImageResource(datosImg[i]);
        imagenHistorial.setImageResource(R.drawable.ic_today_black_24dp);
        imagenFlecha.setImageResource(R.drawable.ic_arrow_back_black_24dp);

        /*
        imagenHistorial.setTag(i);
        imagenFlecha.setTag(i);
        imagenFoto.setTag(i);

        imagenFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent visorImagen=new Intent(contexto,VisorImagen.class);
                visorImagen.putExtra("IMG",datosImg[(Integer) view.getTag()]);
                contexto.startActivity(visorImagen);


            }
        });
        */

        return vista;
    }


    @Override
    public int getCount() {
        return datosImg.length;
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