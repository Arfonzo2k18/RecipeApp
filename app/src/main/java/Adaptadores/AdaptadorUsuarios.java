package Adaptadores;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zafiro5.loginregistrorecetas.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import Clases.Usuario;

public class AdaptadorUsuarios extends RecyclerView.Adapter<AdaptadorUsuarios.UsuariosViewHolder> implements View.OnClickListener {

    String baseurl = "http://192.168.1.10:3000";
    private ArrayList<Usuario> listaUsuarios;
    private Activity activity;
    private View.OnClickListener listener;
    Context context;

    public AdaptadorUsuarios(Activity activity, ArrayList<Usuario> listaUsuarios){
        this.activity = activity;
        this.listaUsuarios = listaUsuarios;
    }

    @NonNull
    @Override
    public AdaptadorUsuarios.UsuariosViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_usuarios, viewGroup, false);
        view.setOnClickListener(this);
        return new AdaptadorUsuarios.UsuariosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorUsuarios.UsuariosViewHolder usuariosViewHolder, int i) {
        final Usuario usuario = listaUsuarios.get(i);
        Picasso.with(context).load(baseurl + usuario.getFoto()).into(usuariosViewHolder.imvUsuario);
        usuariosViewHolder.txvUsuario.setText(usuario.getNombre());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return listaUsuarios.size();
    }


    // Método que se encarga de escuchar ese elemento
    public void setOnClicListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if(listener != null) {
            listener.onClick(view);
        }
    }

    class UsuariosViewHolder extends RecyclerView.ViewHolder{

        private ImageView imvUsuario;
        private TextView txvUsuario;
        private View view;

        public UsuariosViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            imvUsuario = (ImageView)itemView.findViewById(R.id.imvFotoUsuario);
            txvUsuario = (TextView)itemView.findViewById(R.id.txvUsuario);
        }
    }
}
