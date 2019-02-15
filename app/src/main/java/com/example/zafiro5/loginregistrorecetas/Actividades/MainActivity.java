package com.example.zafiro5.loginregistrorecetas.Actividades;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import Fragmentos.RecetasFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    RequestQueue lista;
    String idusuario;
    String baseurl = "http://192.168.1.10:3000";
    private BottomNavigationView btnNavegacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cambiarStatusBar();
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        idusuario = getIntent().getStringExtra("idusuario");
        lista = Volley.newRequestQueue(this);

        comprobarseion(idusuario);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent crearreceta = new Intent(getApplicationContext(), CrearReceta.class);
                crearreceta.putExtra("idusuario", idusuario);
                startActivity(crearreceta);
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
                        Fragment fragmentorecetas = new RecetasFragment();
                        fragmentorecetas.setArguments(argumento);
                        fragment = fragmentorecetas;
                        break;

                    case R.id.navigation_ubicacion:
                        toolbar.setTitle("Ubicacion");
                        //fragment = new Extras_fragmento();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       getMenuInflater().inflate(R.menu.menu_filtro, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.action_search) {
            String[] colors = {"Entrantes", "Pescado", "Carnes", "Verduras", "Ensaladas", "Postres"};

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_perfil) {

        } else if (id == R.id.nav_cerrarsesion) {
            idusuario = "";
            comprobarseion(idusuario);
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

    private void filtro(int seleccion) {
        Fragment fragment = null;
        Bundle args = new Bundle();


        if(seleccion == 0) {
            args.putString("categoria", "Entrantes");
            Fragment fragmento = new RecetasFragment();
            fragmento.setArguments(args);
            fragment = fragmento;
            replaceFragment(fragment);
            Toast.makeText(getApplicationContext(), "¡Has seleccionado Entrantes!", Toast.LENGTH_SHORT).show();
        }

        if(seleccion == 1) {
            args.putString("categoria", "Pescado");
            Fragment fragmento = new RecetasFragment();
            fragmento.setArguments(args);
            fragment = fragmento;
            replaceFragment(fragment);
            Toast.makeText(getApplicationContext(), "¡Has seleccionado Pescado!", Toast.LENGTH_SHORT).show();
        }

        if(seleccion == 2) {
            args.putString("categoria", "Carnes");
            Fragment fragmento = new RecetasFragment();
            fragmento.setArguments(args);
            fragment = fragmento;
            replaceFragment(fragment);
            Toast.makeText(getApplicationContext(), "¡Has seleccionado Carnes!", Toast.LENGTH_SHORT).show();
        }

        if(seleccion == 3) {
            args.putString("categoria", "Verduras");
            Fragment fragmento = new RecetasFragment();
            fragmento.setArguments(args);
            fragment = fragmento;
            replaceFragment(fragment);
            Toast.makeText(getApplicationContext(), "¡Has seleccionado Verduras!", Toast.LENGTH_SHORT).show();
        }

        if(seleccion == 4) {
            args.putString("categoria", "Ensaladas");
            Fragment fragmento = new RecetasFragment();
            fragmento.setArguments(args);
            fragment = fragmento;
            replaceFragment(fragment);
            Toast.makeText(getApplicationContext(), "¡Has seleccionado Ensaladas!", Toast.LENGTH_SHORT).show();
        }

        if(seleccion == 5) {
            args.putString("categoria", "Postres");
            Fragment fragmento = new RecetasFragment();
            fragmento.setArguments(args);
            fragment = fragmento;
            replaceFragment(fragment);
            Toast.makeText(getApplicationContext(), "¡Has seleccionado Postres!", Toast.LENGTH_SHORT).show();
        }



    }

}
