package Fragmentos;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.zafiro5.loginregistrorecetas.R;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class Perfil extends Fragment {

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

        lista = Volley.newRequestQueue(getActivity().getApplicationContext());
        //Indicamos que este fragmento debe rellenar el menú
        setHasOptionsMenu(true);
        //cargarPerfil();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_perfil, container, false);
    }

   /* public void cargarPerfil(){
        String direccion = "http://192.168.1.10:3000/api/userprofile/" + getActivity().getIntent().getStringExtra("idusuario");
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, direccion, null, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject response) {
                try {
                    final String nombre = response.getString("nombre");
                    _nombreText.setText(nombre);
                    _apeText.setText(response.getString("apellidos"));
                    _usuarioText.setText(response.getString("username"));
                    _bioText.setText(response.getString("biografia"));
                    _movilText.setText(response.getString("telefono"));
                    _fechanacText.setText(response.getString("fechanac"));

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
    }*/

    private class LongOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                String direccion = "http://192.168.1.10:3000/api/userprofile/" + getActivity().getIntent().getStringExtra("idusuario");
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, direccion, null, new Response.Listener<JSONObject>() {
                    public void onResponse(JSONObject response) {
                        try {
                            final String nombre = response.getString("nombre");
                            final String apellidos = response.getString("apellidos");
                            final String usuario = response.getString("username");
                            final String biografia = response.getString("biografia");
                            final String telefono = response.getString("telefono");
                            final String fechanac = response.getString("fechanac");
                            final String foto = response.getString("imagen");

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
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.interrupted();
            }
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}
