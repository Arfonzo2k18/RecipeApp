package Fragmentos;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.zafiro5.loginregistrorecetas.Actividades.Actividades.Actividades.Login;
import com.example.zafiro5.loginregistrorecetas.Actividades.Actividades.Actividades.Principal;
import com.example.zafiro5.loginregistrorecetas.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import Clases.Usuario;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class Perfil extends Fragment {

    private String idusuario;
    private String nombre;
    private String apellidos;
    private String usuario;
    private String movil;
    private String bio;
    private String fecha_nac;

    @BindView(R.id.input_nombre) EditText _nombreText;
    @BindView(R.id.input_apellidos) EditText _apeText;
    @BindView(R.id.input_usuario) EditText _usuarioText;
    @BindView(R.id.input_movil) EditText _movilText;
    @BindView(R.id.input_bio) EditText _bioText;
    @BindView(R.id.input_fecha_nac) EditText _fechanacText;
    ImageView imvPerfil;
    RequestQueue lista;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        ButterKnife.bind(view);

        //Indicamos que este fragmento debe rellenar el menú
        setHasOptionsMenu(true);
        String idusuario = getActivity().getIntent().getStringExtra("idusuario");
        cargarPerfil(idusuario);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_perfil, container, false);
    }

    public void cargarPerfil(String id){
        String direccion = "http://192.168.1.10:3000/api/userprofile/" + id;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, direccion, null, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject response) {
                try {
                    Usuario perfil = new Usuario(response.getString("nombre"), response.getString("apellidos"), response.getString("username"), response.getString("biografia"), response.getString("telefono"), response.getString("fechanac"), response.getString("imagen").getBytes());
                   /* _nombreText.setText();
                    _apeText.setText();
                    _usuarioText.setText();
                    _bioText.setText();
                    _movilText.setText();
                    _fechanacText.setText();
                    Bitmap bmp;
                    byte[] imagens = response.getString("imagen").getBytes();
                    bmp = BitmapFactory.decodeByteArray(imagens, 0, imagens.length);
                    imvPerfil.setImageBitmap(bmp);*/

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", error.getMessage());
            }
        });
        lista.add(request);
    }

}
