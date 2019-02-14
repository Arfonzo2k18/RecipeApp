package Adaptadores;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zafiro5.loginregistrorecetas.R;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import Clases.Receta;
import DBSqlite.TablaRecetas;

public class AdaptadorRecetas extends RecyclerView.Adapter<AdaptadorRecetas.RecetasViewHolder> {

    private ArrayList<Receta> listaRecetas;

    public AdaptadorRecetas(Context context){
        TablaRecetas tablaRecetas = new TablaRecetas(context);
        this.listaRecetas = tablaRecetas.todos_las_recetas();
    }

    public void porAutor(Context context, String autor) {
        TablaRecetas tablaRecetas = new TablaRecetas(context);
        this.listaRecetas = tablaRecetas.recetas_por_autor(autor);
    }

    @NonNull
    @Override
    public AdaptadorRecetas.RecetasViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new RecetasViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_recetas, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorRecetas.RecetasViewHolder recetasViewHolder, int i) {
        final Receta receta = listaRecetas.get(i);
        Bitmap foto;
        ByteArrayInputStream imageStream = new ByteArrayInputStream(receta.getFoto());
        foto = BitmapFactory.decodeStream(imageStream);
        recetasViewHolder.imvReceta.setImageBitmap(foto);
        recetasViewHolder.txvTitulo.setText(receta.getNombre());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return listaRecetas.size();
    }


    class RecetasViewHolder extends RecyclerView.ViewHolder{

        private ImageView imvReceta;
        private TextView txvTitulo;
        private View view;

        public RecetasViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            imvReceta = (ImageView)itemView.findViewById(R.id.imvReceta);
            txvTitulo = (TextView)itemView.findViewById(R.id.txvTitulo);
        }
    }
}
