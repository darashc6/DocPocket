package es.losinutiles.docpocket;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
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

public class TabSoporte extends Fragment {
    private Button botonEnviarMensaje; // Botón para enviar el mensaje
    private EditText nombreEscrito; // Nombre que ha escrito el usuario
    private MainActivity main; // Actividad Principal
    private EditText emailEscrito; // Email que ha escrito es usuario
    private TextView nombre;
    private TextView email;
    private TextView problema;
    private EditText mensajeEscrito; // Mensaje que ha escrito el usuario
    private FirebaseAuth mFirebase;
    private DatabaseReference dFirebase;
    private ArrayList<DatosEscaner> listaDatos=new ArrayList<>();
    private AdaptadorListView adaptador;
    private ListView listaAuxiliar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_soporte, container, false);
        botonEnviarMensaje = view.findViewById(R.id.botonEnviarMensaje);
        nombre=view.findViewById(R.id.textView);
        email=view.findViewById(R.id.textView2);
        problema=view.findViewById(R.id.textView3);
        nombreEscrito=view.findViewById(R.id.textoNombre);
        emailEscrito=view.findViewById(R.id.textoEmail);
        mensajeEscrito=view.findViewById(R.id.textoMensaje);
        mFirebase=FirebaseAuth.getInstance();
        dFirebase= FirebaseDatabase.getInstance().getReference();
        listaAuxiliar=view.findViewById(R.id.listaAuxiliar);

        String nombreUsuario=FirebaseAuth.getInstance().getCurrentUser().getEmail().split("@")[0];
        dFirebase.child("DatosUsuario").child(nombreUsuario).child("TabHistorial").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaDatos.clear();
                if (dataSnapshot.exists()) {
                    long nEscaneado=dataSnapshot.getChildrenCount();
                    for (DataSnapshot ds: dataSnapshot.getChildren()) {
                        DatosEscaner de=ds.getValue(DatosEscaner.class);
                        listaDatos.add(de);
                    }
                    adaptador=new AdaptadorListView(getContext(),listaDatos);
                    listaAuxiliar.setAdapter(adaptador);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // Aquí se hace un onclick listener para enviar el formulario escrito por el usuario
        botonEnviarMensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    EnvioMail em=new EnvioMail(getContext(), nombreEscrito.getText().toString(), emailEscrito.getText().toString(), mensajeEscrito.getText().toString());
                    em.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Error a la hora de mandar E-Mail", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (mFirebase.getCurrentUser()!=null) {
            emailEscrito.setText(mFirebase.getCurrentUser().getEmail());
        }


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        main=new MainActivity();
        if(main.CargarPreferencia(getContext())){
            botonEnviarMensaje.setBackgroundResource(R.color.Rojo);
            nombreEscrito.setTextColor(getContext().getColor(R.color.Blanco));
            emailEscrito.setBackgroundTintList(getContext().getColorStateList(R.color.Blanco));
            mensajeEscrito.setBackgroundTintList(getContext().getColorStateList(R.color.Blanco));
            nombreEscrito.setBackgroundTintList(getContext().getColorStateList(R.color.Blanco));
            emailEscrito.setTextColor(getContext().getColor(R.color.Blanco));
            mensajeEscrito.setTextColor(getContext().getColor(R.color.Blanco));
            nombre.setTextColor(getContext().getColor(R.color.Blanco));
            email.setTextColor(getContext().getColor(R.color.Blanco));
            problema.setTextColor(getContext().getColor(R.color.Blanco));
            mensajeEscrito.setHintTextColor(getContext().getColor(R.color.Blanco));
        }else{
            botonEnviarMensaje.setBackgroundResource(R.color.Blanco);
            nombreEscrito.setTextColor(getContext().getColor(R.color.modoOscuro));
            emailEscrito.setTextColor(getContext().getColor(R.color.modoOscuro));
            mensajeEscrito.setTextColor(getContext().getColor(R.color.modoOscuro));
            nombre.setTextColor(getContext().getColor(R.color.modoOscuro));
            email.setTextColor(getContext().getColor(R.color.modoOscuro));
            problema.setTextColor(getContext().getColor(R.color.modoOscuro));
            emailEscrito.setBackgroundTintList(getContext().getColorStateList(R.color.modoOscuro));
            mensajeEscrito.setBackgroundTintList(getContext().getColorStateList(R.color.modoOscuro));
            nombreEscrito.setBackgroundTintList(getContext().getColorStateList(R.color.modoOscuro));
            mensajeEscrito.setHintTextColor(getContext().getColor(R.color.modoOscuro));
        }
    }
}
