package Fragmentos;

import android.content.Intent;
import android.os.Bundle;
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

public class RecetasFragment extends Fragment {


    public RecetasFragment() {
    }

    private String categoriaSeleccionada;
    //Propiedades
    private AdaptadorRecetas adaptadorRecetas;
    private RecyclerView recyclerView;
    private View rootView;
    private GridLayoutManager gridLayoutManager;
    ArrayList<Receta> listaRecetas;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Indicamos que este fragmento debe rellenar el menú
        setHasOptionsMenu(true);

        if(getArguments().getString("categoria") != "nada") {
            categoriaSeleccionada = getArguments().getString("categoria");
        } else {
            categoriaSeleccionada = "";
        }

        //Inflamos la Vista rootView para Visualizar el Adaptador personalizado
        rootView = inflater.inflate(R.layout.fragment_recetas, container, false);

        //Vinculamos el RecycleView y el FloatingActionButton con el del XML
        this.recyclerView = (RecyclerView) rootView.findViewById(R.id.rvRecetas);

        if(categoriaSeleccionada == "") {
            cargarRecetas();
        } else {
            cargarRecetasPorCategoria(categoriaSeleccionada);
        }

        //Devolvemos la Vista
        return rootView;
    }

    public void cargarRecetas(){
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        TablaRecetas tablaRecetas = TablaRecetas.getInstance(getContext());
        tablaRecetas.open();
        listaRecetas = tablaRecetas.todos_las_recetas();
        tablaRecetas.closeDatabase();
        adaptadorRecetas = new AdaptadorRecetas(getActivity(), listaRecetas);
        adaptadorRecetas.setOnClicListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PDFViewer.class);
                intent.putExtra("receta",
                        listaRecetas.get(recyclerView.getChildAdapterPosition(view)).getPdf());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adaptadorRecetas);
    }

    public void cargarRecetasPorCategoria(String categoria){
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        TablaRecetas tablaRecetas = TablaRecetas.getInstance(getContext());
        tablaRecetas.open();
        listaRecetas = tablaRecetas.recetas_por_categoria(categoria);
        tablaRecetas.closeDatabase();
        adaptadorRecetas = new AdaptadorRecetas(getActivity(), listaRecetas);
        adaptadorRecetas.setOnClicListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PDFViewer.class);
                intent.putExtra("receta",
                        listaRecetas.get(recyclerView.getChildAdapterPosition(view)).getPdf());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adaptadorRecetas);
    }

}
