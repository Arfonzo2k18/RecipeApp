package com.example.zafiro5.loginregistrorecetas.Actividades;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.android.volley.toolbox.Volley;
import com.example.zafiro5.loginregistrorecetas.R;

import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Registro extends AppCompatActivity implements View.OnClickListener {

    private RequestQueue cola;
    @BindView(R.id.input_email) EditText _emailText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.input_nombre) EditText _nombreText;
    @BindView(R.id.input_apellidos) EditText _apeText;
    @BindView(R.id.input_usuario) EditText _usuarioText;
    @BindView(R.id.input_movil) EditText _movilText;
    @BindView(R.id.input_bio) EditText _bioText;
    @BindView(R.id.input_fecha_nac) EditText _fechanacText;
    @BindView(R.id.btn_crear_cuenta) Button _crear_cuentaButton;
    ImageView imvRegistro;
    private byte[] foto;
    HashMap<String, String> params = new HashMap<String, String>();
    String baseurl = "http://192.168.1.10:3000/api/register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        ButterKnife.bind(this);

        this.imvRegistro = (ImageView) findViewById(R.id.imvRegistro);

        this.imvRegistro.setOnClickListener(this);
        this._emailText.setHintTextColor(1);
        this._passwordText.setHintTextColor(1);
        this._nombreText.setOnClickListener(this);
        this._apeText.setOnClickListener(this);
        this._bioText.setOnClickListener(this);
        this._movilText.setOnClickListener(this);
        this._crear_cuentaButton.setOnClickListener(this);
        this._fechanacText.setOnClickListener(this);
        this._usuarioText.setOnClickListener(this);

        checkCameraPermission();
        cola = Volley.newRequestQueue(this);
    }

    private boolean validarFormulario() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String nombre = _nombreText.getText().toString();
        String apellidos = _apeText.getText().toString();
        String usuario = _usuarioText.getText().toString();
        String movil = _movilText.getText().toString();
        String biografia = _bioText.getText().toString();
        String fechanac = _fechanacText.getText().toString();


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

        if (nombre.isEmpty()) {
            _nombreText.setError("La clave debe tener entre 4 y 10 carácteres");
            valid = false;
        } else {
            _nombreText.setError(null);
        }

        if (nombre.isEmpty()) {
            _nombreText.setError("El campo nombre no puede estar vacío");
            valid = false;
        } else {
            _nombreText.setError(null);
        }

        if (apellidos.isEmpty()) {
            _apeText.setError("El campo apellidos no puede estar vacío");
            valid = false;
        } else {
            _apeText.setError(null);
        }

        if (usuario.isEmpty()) {
            _usuarioText.setError("El campo usuario no puede estar vacío");
            valid = false;
        } else {
            _usuarioText.setError(null);
        }

        if (movil.length() != 9) {
            _movilText.setError("El campo movil debe tener 9 carácteres");
            valid = false;
        } else {
            _movilText.setError(null);
        }

        if (biografia.isEmpty()) {
            _bioText.setError("El campo biografía no puede estar vacío");
            valid = false;
        } else {
            _bioText.setError(null);
        }

        if (fechanac.isEmpty()) {
            _fechanacText.setError("El campo fecha de nacimiento no puede estar vacío");
            valid = false;
        } else {
            _fechanacText.setError(null);
        }

        return valid;
    }

    private void enviarDatos(){
        params.put("nombre", _nombreText.getText().toString());
        params.put("telefono", _movilText.getText().toString());
        params.put("apellidos", _apeText.getText().toString());
        params.put("username", _usuarioText.getText().toString());
        params.put("biografia", _bioText.getText().toString());
        params.put("fechanac", _fechanacText.getText().toString());
        params.put("email", _emailText.getText().toString());
        params.put("password", _passwordText.getText().toString());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, baseurl,new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                final ProgressDialog progressDialog = new ProgressDialog(Registro.this,
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Creando cuenta...");
                progressDialog.setMax(6);
                progressDialog.show();
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Has creado tu cuenta correctamente. ", Toast.LENGTH_SHORT).show();
                            }
                        }, 1000);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                final ProgressDialog progressDialog = new ProgressDialog(Registro.this,
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Creando cuenta...");
                progressDialog.setMax(6);
                progressDialog.show();
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Ha ocurrido un error inesperado.", Toast.LENGTH_SHORT).show();
                            }
                        }, 1000);
            }
        });
        cola.add(request);
    }

    private boolean checkCameraPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Log.i("Mensaje", "No se tiene permiso para la camara!.");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 225);
            return false;
        } else {
            Log.i("Mensaje", "Tienes permiso para usar la camara.");
            return true;
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_crear_cuenta) {
            if(validarFormulario()) {
                enviarDatos();
                Intent login = new Intent(getApplicationContext(), Login.class);
                startActivity(login);
                overridePendingTransition(R.anim.push_in, R.anim.push_out);
            }
        }

        if(v.getId() == R.id.input_fecha_nac) {
            Calendar c;
            DatePickerDialog dpd;
            c = Calendar.getInstance();
            int dia = c.get(Calendar.DAY_OF_MONTH);
            int mes = c.get(Calendar.MONTH);
            int year = c.get(Calendar.YEAR);
            dpd = new DatePickerDialog(Registro.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int mYear, int mMes, int mDia) {
                    _fechanacText.setText(mYear + "/" + (mMes+1) + "/" + mDia);
                }
            }, dia, mes, year);
            dpd.show();
        }

        if(v.getId() == R.id.imvRegistro) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (data.getExtras() == null) {
                Toast toast = Toast.makeText(getApplicationContext(), "No has hecho ninguna foto.", Toast.LENGTH_LONG);
                toast.show();
            } else {
                // CREAMOS UN MAPA DE BITS CON LOS DATOS QUE HEMOS RECOGIDO DE LA CÁMARA DE FOTOS.
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                // PONEMOS EL MAPA DE BITS EN EL IMAGEVIEW.
                imvRegistro.setImageBitmap(bitmap);
                // CREAMOS UN ARRAY DE BYTES DE SALIDA.
                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                // COMPRIMIMOS EL MAPA DE BITS EN PNG Y LA VARIABLE FOTO TOMA EL VALOR DEL FLUJO DE SALIDA DE ARRAY DE BYTES.
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
                foto = stream.toByteArray();
                params.put("foto", Base64.encodeToString(foto, Base64.DEFAULT));
            }
        }
    }
}
