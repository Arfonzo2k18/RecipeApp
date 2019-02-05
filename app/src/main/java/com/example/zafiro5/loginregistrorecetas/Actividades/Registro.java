package com.example.zafiro5.loginregistrorecetas.Actividades;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.toolbox.Volley;
import com.example.zafiro5.loginregistrorecetas.R;
import android.widget.Toast;

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

public class Registro extends AppCompatActivity implements View.OnClickListener {

    private RequestQueue cola;
    @BindView(R.id.input_email) EditText _emailText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.input_nombre) EditText _nombreText;
    @BindView(R.id.input_apellidos) EditText _aplicacionText;
    @BindView(R.id.input_usuario) EditText _usuarioText;
    @BindView(R.id.input_movil) EditText _movilText;
    @BindView(R.id.input_bio) EditText _bioText;
    @BindView(R.id.input_fecha_nac) EditText _fechanacText;
    @BindView(R.id.btn_crear_cuenta) Button _crear_cuentaButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        ButterKnife.bind(this);

        this._emailText.setHintTextColor(1);
        this._passwordText.setHintTextColor(1);
        this._nombreText.setOnClickListener(this);
        this._aplicacionText.setOnClickListener(this);
        this._bioText.setOnClickListener(this);
        this._movilText.setOnClickListener(this);
        this._crear_cuentaButton.setOnClickListener(this);
        this._fechanacText.setOnClickListener(this);
        this._usuarioText.setOnClickListener(this);


        cola = Volley.newRequestQueue(this);
    }

    @Override
    public void onClick(View v) {

    }
}
