package Fragmentos;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.zafiro5.loginregistrorecetas.Actividades.CrearReceta;
import com.example.zafiro5.loginregistrorecetas.Actividades.MainActivity;
import com.example.zafiro5.loginregistrorecetas.Actividades.PDFViewer;
import com.example.zafiro5.loginregistrorecetas.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Adaptadores.AdaptadorRecetas;
import Adaptadores.AdaptadorUsuarios;
import Clases.Receta;
import Clases.Usuario;
import DBSqlite.TablaRecetas;

public class NavegacionFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener {

    RequestQueue peticion;
    String baseurl = "http://192.168.1.10:3000";
    private String usuarioSeleccionado;
    //Propiedades
    private AdaptadorUsuarios adaptadorUsuarios;
    private RecyclerView recyclerView;
    private View rootView;
    private GridLayoutManager gridLayoutManager;
    ArrayList<Usuario> listaUsuarios;
    double longitud, latitud;
    GoogleMap mMap;
    private SupportMapFragment mapFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mapFragment = SupportMapFragment.newInstance();
        mapFragment.getMapAsync(this);

        //Cargamos el mapa en el fragment map
        getChildFragmentManager().beginTransaction().replace(R.id.frameLayout, mapFragment).commit();

        //Indicamos que este fragmento debe rellenar el menú
        setHasOptionsMenu(true);

        listaUsuarios = new ArrayList<Usuario>();

        //Inflamos la Vista rootView para Visualizar el Adaptador personalizado
        rootView = inflater.inflate(R.layout.fragment_navegacion, container, false);

        this.recyclerView = (RecyclerView) rootView.findViewById(R.id.rvUsuarios);

        peticion = Volley.newRequestQueue(getActivity().getApplicationContext());

        new traerusuarios().execute();

        //Devolvemos la Vista
        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
            // Acercamos la camara a la posición del usuario
        LatLng ubi = new LatLng(latitud, longitud);
        mMap.addMarker(new MarkerOptions().position(ubi).title("Chef " /*nombreUsuario*/));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubi, 15)); // Zoom del mapa

        adaptadorUsuarios.setOnClicListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String l = listaUsuarios.get(recyclerView.getChildAdapterPosition(view)).getLatitud();
                String a = listaUsuarios.get(recyclerView.getChildAdapterPosition(view)).getAltitud();
                LatLng lat = new LatLng(Double.parseDouble(l),Double.parseDouble(a));
                mMap.addMarker(new MarkerOptions().position(lat).title(listaUsuarios.get(recyclerView.getChildAdapterPosition(view)).getUsuario()));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lat, 15));//Zoom del mapa
                mMap.animateCamera(CameraUpdateFactory.newLatLng(lat));
            }
        });
    }

    @Override
    public void onClick(View view) {

    }

    private class traerusuarios extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * Metodo que hace una peticion HTTP al servidor y recibe todos los usuarios que posteriormente
         * se cargara en el recycler view.
         * */
        @Override
        protected Void doInBackground(Void... params) {
            String direccion = baseurl + "/api/allUsers";
            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, direccion, null, new Response.Listener<JSONArray>() {
                public void onResponse(JSONArray response) {
                    publishProgress();
                    try {
                        for(int i = 0; i < response.length(); i++) {
                            JSONObject jsonUsuario;
                            jsonUsuario = response.getJSONObject(i);
                            String idusuario = jsonUsuario.getString("_id");
                            String username = jsonUsuario.getString("username");
                            String email = jsonUsuario.getString("email");
                            String nombre = jsonUsuario.getString("nombre");
                            String apellidos = jsonUsuario.getString("apellidos");
                            String fechanac = jsonUsuario.getString("fechanac");
                            String foto = jsonUsuario.getString("imagen");
                            String biografia = jsonUsuario.getString("biografia");
                            String movil = jsonUsuario.getString("telefono");
                            String lat = jsonUsuario.getString("latitud");
                            String alt = jsonUsuario.getString("altitud");
                            Usuario usuario;
                            usuario = new Usuario(idusuario, nombre, apellidos, username, email, biografia, movil, fechanac, foto, lat, alt);
                            listaUsuarios.add(usuario);
                        }

                        recyclerView = (RecyclerView) rootView.findViewById(R.id.rvUsuarios);
                        gridLayoutManager = new GridLayoutManager(getContext(), 1);
                        recyclerView.setLayoutManager(gridLayoutManager);
                        adaptadorUsuarios = new AdaptadorUsuarios(getActivity(), listaUsuarios);
                        adaptadorUsuarios.setOnClicListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String l = listaUsuarios.get(recyclerView.getChildAdapterPosition(view)).getLatitud();
                                String a = listaUsuarios.get(recyclerView.getChildAdapterPosition(view)).getAltitud();
                                LatLng lat = new LatLng(Double.parseDouble(l),Double.parseDouble(a));
                                mMap.addMarker(new MarkerOptions().position(lat).title(listaUsuarios.get(recyclerView.getChildAdapterPosition(view)).getUsuario()));
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lat, 15));//Zoom del mapa
                                mMap.animateCamera(CameraUpdateFactory.newLatLng(lat));
                            }
                        });
                        recyclerView.setAdapter(adaptadorUsuarios);

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

            peticion.add(request);
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity(),
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Cargando usuarios...");
            progressDialog.setMax(6);
            progressDialog.show();

            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                        }
                    },500);
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }
}
