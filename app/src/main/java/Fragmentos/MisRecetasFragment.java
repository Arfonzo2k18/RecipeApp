package Fragmentos;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zafiro5.loginregistrorecetas.Actividades.PDFViewer;
import com.example.zafiro5.loginregistrorecetas.R;

import java.util.ArrayList;

import Adaptadores.AdaptadorRecetas;
import Clases.Receta;
import DBSqlite.TablaRecetas;

public class MisRecetasFragment extends Fragment {

    public MisRecetasFragment() {
    }

    private String usuarioLogged;
    //Propiedades
    private AdaptadorRecetas adaptadorRecetas;
    private RecyclerView recyclerView;
    private View rootView;
    private GridLayoutManager gridLayoutManager;
    private ArrayList<Receta> misRecetas;

    //Objetos para vincularlo con el XML
    private FloatingActionButton btnFlotAdd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Indicamos que este fragmento debe rellenar el menú
        setHasOptionsMenu(true);

        usuarioLogged = getArguments().getString("idusuario");

        //Inflamos la Vista rootView para Visualizar el Adaptador personalizado
        rootView = inflater.inflate(R.layout.fragment_misrecetas, container, false);

        //Vinculamos el RecycleView y el FloatingActionButton con el del XML
        this.recyclerView = (RecyclerView) rootView.findViewById(R.id.rvMisRecetas);

        cargarRecetas(usuarioLogged);

        //Devolvemos la Vista
        return rootView;
    }

    public void cargarRecetas(String autor){
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        TablaRecetas tablaRecetas = TablaRecetas.getInstance(getContext());
        tablaRecetas.open();
        misRecetas = tablaRecetas.recetas_por_autor(autor);
        tablaRecetas.closeDatabase();
        adaptadorRecetas = new AdaptadorRecetas(getActivity(), misRecetas);
        adaptadorRecetas.setOnClicListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PDFViewer.class);
                intent.putExtra("receta", misRecetas.get(recyclerView.getChildAdapterPosition(view)).getPdf());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adaptadorRecetas);
    }

}
