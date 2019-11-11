package es.losinutiles.docpocket;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TabSoporte extends Fragment {
    private Button botonEnviarMensaje; // Botón para enviar el mensaje
    private EditText nombreEscrito; // Nombre que ha escrito el usuario
    private EditText emailEscrito; // Email que ha escrito es usuario
    private EditText mensajeEscrito; // Mensaje que ha escrito el usuario

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_soporte, container, false);
        botonEnviarMensaje = view.findViewById(R.id.botonEnviarMensaje);
        nombreEscrito=view.findViewById(R.id.textoNombre);
        emailEscrito=view.findViewById(R.id.textoEmail);
        mensajeEscrito=view.findViewById(R.id.textoMensaje);

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

        return view;
    }
}
