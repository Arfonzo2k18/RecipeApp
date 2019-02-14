package com.example.zafiro5.loginregistrorecetas.Actividades;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Login extends AppCompatActivity implements View.OnClickListener {


    private RequestQueue cola;
    @BindView(R.id.input_email) EditText _emailText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.btn_login) Button _loginButton;
    @BindView(R.id.link_signup) TextView _signupLink;
    String idusuario;
    String baseurl = "http://192.168.1.10:3000/api/authenticate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        this._emailText.setHintTextColor(1);
        this._passwordText.setHintTextColor(1);
        this._loginButton.setOnClickListener(this);
        this._signupLink.setOnClickListener(this);

        cola = Volley.newRequestQueue(this);

    }

    private boolean validarFormulario() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("Introduce una dirección de correo válida");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("La clave debe tener entre 4 y 10 carácteres");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    private void comprobarDatos(){
        String url = baseurl;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("email", _emailText.getText().toString());
        params.put("password", _passwordText.getText().toString());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url,new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    final ProgressDialog progressDialog = new ProgressDialog(Login.this,
                            R.style.AppTheme_Dark_Dialog);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Autenticando...");
                    progressDialog.setMax(6);
                    progressDialog.show();
                    idusuario = response.getString("idusuario");

                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Has iniciado sesión correctamente.", Toast.LENGTH_SHORT).show();
                                    Intent principal = new Intent(getApplicationContext(), MainActivity.class);
                                    principal.putExtra("idusuario", idusuario);
                                    startActivity(principal);
                                }
                            }, 1000);
                } catch (JSONException e) {
                    final ProgressDialog progressDialog = new ProgressDialog(Login.this,
                            R.style.AppTheme_Dark_Dialog);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Autenticando...");
                    progressDialog.setMax(6);
                    progressDialog.show();
                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Has introducido unas credenciales incorrectas", Toast.LENGTH_SHORT).show();
                                }
                            }, 1000);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                final ProgressDialog progressDialog = new ProgressDialog(Login.this,
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Autenticando...");
                progressDialog.setMax(6);
                progressDialog.show();
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Credenciales incorrectas.", Toast.LENGTH_SHORT).show();
                            }
                        }, 1000);
            }
        });
        cola.add(request);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_login) {
            if(validarFormulario()) {
                comprobarDatos();
            }
        }if(v.getId() == R.id.link_signup) {
            Intent registro = new Intent(getApplicationContext(), Registro.class);
            startActivity(registro);
            overridePendingTransition(R.anim.push_in, R.anim.push_out);
        }
    }
}
