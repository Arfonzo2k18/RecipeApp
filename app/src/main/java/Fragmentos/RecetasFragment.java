package Fragmentos;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zafiro5.loginregistrorecetas.R;

import java.util.ArrayList;

import Clases.Receta;

public class RecetasFragment extends Fragment {

    //Propiedades
    private ArrayList<Receta> listaRecetas;
    private RecyclerView recyclerView;
    private View rootView;

    //Objetos para vincularlo con el XML
    private FloatingActionButton btnFlotAdd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Indicamos que este fragmento debe rellenar el menú
        setHasOptionsMenu(true);

        //Inflamos la Vista rootView para Visualizar el Adaptador personalizado
        rootView = inflater.inflate(R.layout.fragment_recetas, container, false);

        //Vinculamos el RecycleView y el FloatingActionButton con el del XML
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rvRecetas);
        btnFlotAdd = (FloatingActionButton) rootView.findViewById(R.id.btnFlotAdd);

        cargarRecetas();

        //Devolvemos la Vista
        return rootView;
    }

    public void cargarRecetas(){
        
    }

}
