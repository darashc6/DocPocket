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

public class AdaptadorListView extends BaseAdapter {
    private static LayoutInflater inflater=null;
    private DatabaseReference referencia;
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
        textoTituloVariable.setText(datos[i][0]);
        textoFechaHistorial.setText(datos[i][1]);
        imagenFoto.setImageResource(datosImg[i]);
        imagenHistorial.setImageResource(R.drawable.ic_today_black_24dp);



        /*referencia=FirebaseDatabase.getInstance().getReference();
        referencia.child("DocumentacionJava").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String probar=dataSnapshot.child("FileWriter").getValue().toString();
                    Log.e("Datos: ",""+probar);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
*/


        referencia= FirebaseDatabase.getInstance().getReference();
        referencia.child("DocumentacionJava").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                   //Esto se encarga de sacar todos los datods que se encuentre dentro de DocumentacionJava
                   //Que se encuentra en FireBase
                   //Si se quiere sacar un unico valor, se puede crear una clase para sacar ese unico valor

                    Log.e("Datos: ",""+snapshot.getValue());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
<<<<<<< HEAD

        /*
=======
    */

>>>>>>> 424a7f0a2d36a79c140f2e822cd17c5ecf9226eb
        imagenHistorial.setTag(i);
        imagenFoto.setTag(i);

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
