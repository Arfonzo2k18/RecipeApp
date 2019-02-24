package com.example.zafiro5.loginregistrorecetas.Actividades;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
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
import android.widget.Toast;

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

import Fragmentos.MisRecetasFragment;
import Fragmentos.NavegacionFragment;
import Fragmentos.RecetasFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    RequestQueue lista;
    String idusuario;
    String baseurl = "http://192.168.1.10:3000";
    String telef;
    private BottomNavigationView btnNavegacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cambiarStatusBar();
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Recipe App");
        setSupportActionBar(toolbar);

        idusuario = getIntent().getStringExtra("idusuario");
        lista = Volley.newRequestQueue(this);

        comprobarseion(idusuario);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        new cargardatos().execute();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View vista = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);

        btnNavegacion = (BottomNavigationView) findViewById(R.id.btnNavegacion);

        //Creamos una escucha para comprobar si se ha pulsado sobre él
        btnNavegacion.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment = null;
                switch (menuItem.getItemId()) {
                    case R.id.navigation_misrecetas:
                        toolbar.setTitle("Mis Recetas");
                        Bundle args = new Bundle();
                        args.putString("idusuario", idusuario);
                        Fragment fragmento = new MisRecetasFragment();
                        fragmento.setArguments(args);
                        fragment = fragmento;
                        break;

                    case R.id.navigation_recetas:
                        toolbar.setTitle("Recipe App");
                        Bundle argumento = new Bundle();
                        argumento.putString("categoria", "nada");
                        argumento.putString("idusuario", idusuario);
                        Fragment fragmentorecetas = new RecetasFragment();
                        fragmentorecetas.setArguments(argumento);
                        fragment = fragmentorecetas;
                        break;

                    case R.id.navigation_ubicacion:
                        toolbar.setTitle("Ubicacion");
                        fragment = new NavegacionFragment();
                        break;
                }
                replaceFragment(fragment);
                return true;
            }
        });

        //Llamamos al método para iniciar el primer fragmento, en este caso el NuevoFragment
        setInitialFragment();
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_perfil) {
            Intent perfil = new Intent(getApplicationContext(), Perfil.class);
            perfil.putExtra("idusuario", idusuario);
            startActivity(perfil);
        } else if (id == R.id.nav_cerrarsesion) {
            idusuario = "";
            comprobarseion(idusuario);
        } else if (id == R.id.nav_sms) {
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(telef, null, "saludos al usuario con id: " + idusuario , null, null);
        } else if (id == R.id.nav_llamada){
            String telefono = "tel:" + telef;
            startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(telefono)));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Clase cargardatos, utilizada para hacer peticiones HTTP de manera asíncrona.
     * */
    private class cargardatos extends AsyncTask<Void, Void, Void> {

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

                        telef = response.getString("telefono");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) { }
            });

            lista.add(request);
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) { }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }

    //Método para iniciar los Fragmentos, en este caso cargará NuevoFragment
    private void setInitialFragment() {
        Bundle argumento = new Bundle();
        argumento.putString("categoria", "nada");
        Fragment fragmentorecetas = new RecetasFragment();
        fragmentorecetas.setArguments(argumento);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.include, fragmentorecetas);
        fragmentTransaction.commit();
    }

    /**
     * Este metodo se encarga de cambiar el fragmento actual por el que introducimos por parametro.
     * @param: Fragment: fragment
     * */
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.include, fragment);
        fragmentTransaction.commit();
    }

    /**
     * Este metodo se encarga de comprobar si la sesion esta activa, en caso
     * de estarlo no estarlo, te manda a la actividad de login.
    * @param: usuario: String
    * */
    private void comprobarseion(String usuario){
        if(usuario == "") {
            Intent salir = new Intent(getApplicationContext(), Login.class);
            Toast.makeText(getApplicationContext(), "¡Vuelve pronto!", Toast.LENGTH_SHORT).show();
            startActivity(salir);
        }
    }

    // Metodo para cambiar el color a la barra de estado.
    private void cambiarStatusBar(){
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.primary_darker));
        }
    }

}
