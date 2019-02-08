package com.example.zafiro5.loginregistrorecetas.Actividades.Actividades.Actividades;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.zafiro5.loginregistrorecetas.R;

import Fragmentos.Perfil;

public class Principal extends AppCompatActivity {

    String idusuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);

        idusuario = getIntent().getStringExtra("idusuario");

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Fragment fragment = null;
                switch (menuItem.getItemId()) {
                    case R.id.navigation_perfil:
                        fragment = new Perfil();
                        break;
                    case R.id.navigation_recetas:
                       // fragment = new PerfilFragment();
                        break;
                    case R.id.navigation_ubicacion:
                        //fragment = new CocinerosFragment();
                        break;
                }
                replaceFragment(fragment);
                return true;
            }
        });
        setInitialFragment();
    }

    //Método para iniciar los Fragmentos, en este caso cargará NuevoFragment
    private void setInitialFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.contenedor, new Perfil());
        fragmentTransaction.commit();
    }

    //Método que cambiará el Fragmento
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.contenedor, fragment);
        fragmentTransaction.commit();
    }

}
