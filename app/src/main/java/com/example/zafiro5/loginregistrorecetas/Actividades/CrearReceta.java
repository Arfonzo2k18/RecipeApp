package com.example.zafiro5.loginregistrorecetas.Actividades;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.zafiro5.loginregistrorecetas.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import Clases.GenerarPDF;
import Clases.Receta;
import DBSqlite.TablaRecetas;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CrearReceta extends AppCompatActivity implements View.OnClickListener {

    private RequestQueue cola;
    @BindView(R.id.input_nombre) EditText _nombreText;
    @BindView(R.id.input_descripcion) EditText _descripcionText;
    @BindView(R.id.btn_crear_receta) Button _crearButton;
    Spinner spinner;
    ImageView imvCrear;
    private byte[] foto;
    private String idusuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_receta);

        ButterKnife.bind(this);

        idusuario = getIntent().getStringExtra("idusuario");

        this.imvCrear = (ImageView) findViewById(R.id.imvCrear);
        this.spinner = (Spinner) findViewById(R.id.spinner);

        // Escucha de pulsaciones en el spinner
        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3){
            }
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        // Elementos en Spinner
        List<String> values = new ArrayList<String>();
        values.add("Entrantes");
        values.add("Pescado");
        values.add("Carnes");
        values.add("Verduras");
        values.add("Ensaladas");
        values.add("Postres");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, values);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        this.imvCrear.setOnClickListener(this);
        this._nombreText.setOnClickListener(this);
        this._descripcionText.setOnClickListener(this);
        this._crearButton.setOnClickListener(this);

        checkCameraPermission();
        checkWriteFilesPermission();
        cola = Volley.newRequestQueue(this);
    }

    private boolean comprobarerrores() {
        boolean valid = true;

        String nombre = _nombreText.getText().toString();
        String descripcion = _descripcionText.getText().toString();
        String categoria = spinner.getSelectedItem().toString();


        if (nombre.isEmpty()) {
            _nombreText.setError("El campo nombre no puede estar vacío.");
            valid = false;
        } else {
            _nombreText.setError(null);
        }

        if (descripcion.isEmpty()) {
            _descripcionText.setError("El campo descripcion no puede estar vacío.");
            valid = false;
        } else {
            _descripcionText.setError(null);
        }

        return valid;
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

    private boolean checkWriteFilesPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Log.i("Mensaje", "No se tiene permiso para crear ficheros!.");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 225);
            return false;
        } else {
            Log.i("Mensaje", "Tienes permiso para crear ficheros.");
            return true;
        }
    }


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.imvCrear) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 0);
        }

        if(view.getId() == R.id.btn_crear_receta) {
            if(comprobarerrores()) {
                Receta nuevareceta = new Receta(_nombreText.getText().toString(), _descripcionText.getText().toString(), foto, crearpdf(_nombreText.getText().toString(), _descripcionText.getText().toString()),idusuario, spinner.getSelectedItem().toString());
                subirreceta(nuevareceta);
            }
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
                imvCrear.setImageBitmap(bitmap);
                // CREAMOS UN ARRAY DE BYTES DE SALIDA.
                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                // COMPRIMIMOS EL MAPA DE BITS EN PNG Y LA VARIABLE FOTO TOMA EL VALOR DEL FLUJO DE SALIDA DE ARRAY DE BYTES.
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
                foto = stream.toByteArray();
            }
        }
    }

    public String crearpdf(String nombre, String descripcion) {
        GenerarPDF pdf;
        pdf = new GenerarPDF(getApplicationContext());
        pdf.openDocument(nombre);
        pdf.addMetaData("Receta", nombre, idusuario);
        pdf.addTtitulo(nombre);
        pdf.addDatosReceta(descripcion);
        pdf.closeDocument();

        return pdf.verPathPDF();
    }

    public void subirreceta(Receta receta) {
        TablaRecetas tablaRecetas = TablaRecetas.getInstance(this);
        tablaRecetas.open();
        tablaRecetas.crear_receta(receta);
        tablaRecetas.closeDatabase();
        Intent principal = new Intent(getApplicationContext(), MainActivity.class);
        principal.putExtra("idusuario", idusuario);
        startActivity(principal);
    }

}
