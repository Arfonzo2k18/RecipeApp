package com.example.zafiro5.loginregistrorecetas.Actividades;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.zafiro5.loginregistrorecetas.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;

public class Perfil extends AppCompatActivity {

    String baseurl = "http://192.168.1.10:3000";
    private String idusuario;
    private RequestQueue lista;

    @BindView(R.id.edtEmail) EditText edtEmail;
    @BindView(R.id.edtUsuario) EditText edtUsuario;
    @BindView(R.id.edtNombre) EditText edtNombre;
    @BindView(R.id.edtApellidos) EditText edtApellidos;
    @BindView(R.id.edtTelefono) EditText edtTelefono;
    @BindView(R.id.edtBio) EditText edtBio;
    @BindView(R.id.edtFechanac) EditText edtFechanac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        idusuario = getIntent().getStringExtra("idusuario");

        lista = Volley.newRequestQueue(this);
        new ponerdatos().execute();

    }

    private class ponerdatos extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * Metodo que hace una peticion HTTP al servidor y recibe un usuario que posteriormente
         * se cargara en el drawer navigation.
         * */
        @Override
        protected Void doInBackground(Void... params) {
            publishProgress();
            String direccion = baseurl + "/api/userprofile/" + idusuario;
            final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, direccion, null, new Response.Listener<JSONObject>() {
                public void onResponse(JSONObject response) {
                    try {
                        // CARGAMOS DATOS
                        String usuario = response.getString("username");
                        edtUsuario = (EditText)findViewById(R.id.edtUsuario);
                        edtUsuario.setEnabled(false);
                        edtUsuario.setTextColor(getResources().getColor(R.color.iron));
                        edtUsuario.setText(usuario);

                        String email = response.getString("email");
                        edtEmail = (EditText)findViewById(R.id.edtEmail);
                        edtEmail.setEnabled(false);
                        edtEmail.setTextColor(getResources().getColor(R.color.iron));
                        edtEmail.setText(email);

                        String nombre = response.getString("nombre");
                        edtNombre = (EditText)findViewById(R.id.edtNombre);
                        edtNombre.setEnabled(false);
                        edtNombre.setTextColor(getResources().getColor(R.color.iron));
                        edtNombre.setText(nombre);

                        String apellidos = response.getString("apellidos");
                        edtApellidos = (EditText)findViewById(R.id.edtApellidos);
                        edtApellidos.setEnabled(false);
                        edtApellidos.setTextColor(getResources().getColor(R.color.iron));
                        edtApellidos.setText(apellidos);

                        String telefono = response.getString("telefono");
                        edtTelefono = (EditText)findViewById(R.id.edtTelefono);
                        edtTelefono.setEnabled(false);
                        edtTelefono.setTextColor(getResources().getColor(R.color.iron));
                        edtTelefono.setText(telefono);

                        String biografia = response.getString("biografia");
                        edtBio = (EditText)findViewById(R.id.edtBio);
                        edtBio.setEnabled(false);
                        edtBio.setTextColor(getResources().getColor(R.color.iron));
                        edtBio.setText(biografia);

                        String fechanac = response.getString("fechanac");
                        edtFechanac = (EditText)findViewById(R.id.edtFechanac);
                        edtFechanac.setEnabled(false);
                        edtFechanac.setTextColor(getResources().getColor(R.color.iron));
                        edtFechanac.setText(fechanac);

                        // CARGAMOS LA IMAGEN DEL USUARIO
                        ImageView imvFoto = findViewById(R.id.imvFotoPerfil);
                        String foto = response.getString("imagen");
                        Picasso.with(getApplicationContext()).load(baseurl + foto).into(imvFoto);

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
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            final ProgressDialog progressDialog = new ProgressDialog(Perfil.this, R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Cargando datos...");
            progressDialog.setMax(6);
            progressDialog.show();

            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                        }
                    }, 500);
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }

}
