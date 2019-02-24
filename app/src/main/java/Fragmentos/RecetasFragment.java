package Fragmentos;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.zafiro5.loginregistrorecetas.Actividades.CrearReceta;
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
    private String idusuario;
    ArrayList<Receta> listaRecetas;
    FloatingActionButton fab;

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

        if(getArguments().getString("idusuario") != "nada") {
            idusuario = getArguments().getString("idusuario");
        } else {
            idusuario = "";
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

        fab = (FloatingActionButton) rootView.findViewById(R.id.btnFloat);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent crearReceta = new Intent(getContext(), CrearReceta.class);
                crearReceta.putExtra("idusuario", idusuario);
                startActivity(crearReceta);
            }
        });

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.menu_filtro, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.action_search) {
            String[] colors = {"Entrantes", "Pescado", "Carnes", "Verduras", "Ensaladas", "Postres"};

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Filtrar por:");
            builder.setItems(colors, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int seleccion) {
                    filtro(seleccion);
                }
            });
            builder.show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void filtro(int seleccion) {

        if(seleccion == 0) {
           cargarRecetasPorCategoria("Entrantes");
        }

        if(seleccion == 1) {
            cargarRecetasPorCategoria("Pescado");
        }

        if(seleccion == 2) {
            cargarRecetasPorCategoria("Carnes");
        }

        if(seleccion == 3) {
            cargarRecetasPorCategoria("Verduras");
        }

        if(seleccion == 4) {
            cargarRecetasPorCategoria("Ensaladas");
        }

        if(seleccion == 5) {
            cargarRecetasPorCategoria("Postres");
        }

    }

}
