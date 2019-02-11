package com.example.zafiro5.loginregistrorecetas.Actividades;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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


import static com.example.zafiro5.loginregistrorecetas.Actividades.Login.idusuario;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    RequestQueue lista;
    String baseurl = "http://192.168.1.10:3000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lista = Volley.newRequestQueue(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        new cargardatos().execute();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View vista = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class cargardatos extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            String direccion = baseurl + "/api/userprofile/" + idusuario;
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, direccion, null, new Response.Listener<JSONObject>() {
                public void onResponse(JSONObject response) {
                    try {
                        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                        View vista = navigationView.getHeaderView(0);

                        // CARGAMOS EL NOMBRE DE USUARIO EN SU CORRESPONDIENTE CAMPO DEL SIDENAV
                        String usuario = response.getString("username");
                        TextView txvUser = vista.findViewById(R.id.txvUser);
                        txvUser.setText(usuario);

                        // CARGAMOS EL EMAIL EN SU CORRESPONDIENTE CAMPO DEL SIDENAV
                        String email = response.getString("email");
                        TextView txvEmail = vista.findViewById(R.id.txvEmail);
                        txvEmail.setText(email);

                        // CARGAMOS LA IMAGEN DEL USUARIO EN EL SIDENAV
                        ImageView imvFoto = vista.findViewById(R.id.imvFoto);
                        String foto = response.getString("imagen");
                        Picasso.with(getApplicationContext()).load(baseurl + foto).into(imvFoto);

                        // CARGAMOS LOS DATOS RESTANTES EN EL SIDENAV
                        /*String nombre = response.getString("nombre");
                        MenuItem nombreTitulo = vista.findViewById(R.id.nav_camera);
                        nombreTitulo.setTitle(nombre);

                        String apellidos = response.getString("apellidos");
                        MenuItem apellidoTitulo = vista.findViewById(R.id.nav_gallery);
                        apellidoTitulo.setTitle(apellidos);

                        String fechanac = response.getString("fechanac");
                        MenuItem fechanacTitulo = vista.findViewById(R.id.nav_slideshow);
                        fechanacTitulo.setTitle(fechanac);

                        String biografia = response.getString("biografia");
                        String telefono = response.getString("telefono");*/


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
            final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Autenticando...");
            progressDialog.setMax(6);
            progressDialog.show();

            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                        }
                    }, 1000);
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }
}
