package es.losinutiles.docpocket;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Clase de adapter para la navegación de fragmentos
 */
public class AdapterParaFragmentos extends FragmentPagerAdapter {
    private ArrayList<Fragment> listaFragmentos=new ArrayList<>(); // Lista de fragmentos

    /**
     * Constructor de FragmentPageAdapter
     * @param fm
     */

    public AdapterParaFragmentos(@NonNull FragmentManager fm) {

        super(fm);
        
    }

    /**
     * Funcion que retorna un fragmento en específico
     * @param position Entero para retornar ese fragmento
     * @return Fragmento
     */
    @NonNull
    @Override
    public Fragment getItem(int position) {

        return listaFragmentos.get(position);
    }

    /**
     * Funcón que cuenta el nº de fragmentos que hay en total
     * @return El tamaño del ArrayList listaFragmentos
     */
    @Override
    public int getCount() {

        return listaFragmentos.size();
    }

    /**
     * Función que añade un nuevo fragmento a la lista
     * @param f Fragmento a añadir
     */

    public void nuevoFragmento(Fragment f) {

        listaFragmentos.add(f);
    }
}
