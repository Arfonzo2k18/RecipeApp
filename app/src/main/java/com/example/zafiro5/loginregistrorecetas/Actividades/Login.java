package com.example.zafiro5.loginregistrorecetas.Actividades;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.zafiro5.loginregistrorecetas.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity implements View.OnClickListener {


    private RequestQueue cola;
    private EditText edtEmail, edtPass;
    private Button btnIniciar;
    private JsonObjectRequest Jsonobjectrequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.edtEmail = (EditText)findViewById(R.id.edtEmail);
        this.edtPass = (EditText)findViewById(R.id.edtPass);
        this.btnIniciar = (Button)findViewById(R.id.btnIniciar);

        this.btnIniciar.setOnClickListener(this);

        cola = Volley.newRequestQueue(this);

    }





    private void comprobarDatos(){
        String url = "http://192.168.1.254:3000/api/authenticate?email=" + edtEmail.getText().toString() + "&" + "password=" + edtPass.getText().toString();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String idusuario = response.getString("idusuario");
                    Toast.makeText(getApplicationContext(), "Has iniciado sesión correctamente, idusuario: " + idusuario, Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Has introducido unas credenciales incorrectas", Toast.LENGTH_SHORT).show();
            }
        });
        cola.add(request);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == v.findViewById(R.id.btnIniciar).getId()) {
            comprobarDatos();
        }
    }
}
