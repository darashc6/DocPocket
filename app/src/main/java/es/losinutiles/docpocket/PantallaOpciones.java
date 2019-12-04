package es.losinutiles.docpocket;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class PantallaOpciones extends Fragment {
private Button boton;
private MainActivity main;
private Switch modOscuro;
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_pantalla_opciones, container, false);
        boton=view.findViewById(R.id.button2);
        modOscuro=view.findViewById(R.id.switch3);
        main=new MainActivity();
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    this.finalize();
                    Intent intet=new Intent(getContext(),MainActivity.class);
                    startActivity(intet);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        });
        modOscuro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.guardarDatosOscuro(getContext(),modOscuro);
                if(modOscuro.isChecked()){
                    container.setBackgroundResource(R.color.modoOscuro);

                }else{
                    container.setBackgroundResource(R.color.Blanco);
                }
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Toast.makeText(getContext(),"Hola",Toast.LENGTH_LONG).show();
        if(main.CargarPreferencia(getContext())){
            modOscuro.setChecked(true);
        }else{
            modOscuro.setChecked(false);
        }
    }
}
