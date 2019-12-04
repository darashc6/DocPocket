package es.losinutiles.docpocket;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import java.util.prefs.Preferences;

public class TabFavoritos extends Fragment {
    private Button boton2;
    private AdaptadorListView adaptadorListView;
    @Nullable
    @Override
    public  View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_tab_favoritos, container, false);
        boton2 = view.findViewById(R.id.boton2);
        adaptadorListView=new AdaptadorListView();
        boton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Ha hecho clic en el boton del fragmento 1", Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }


}

