package com.example.zafiro5.loginregistrorecetas.Actividades;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toolbar;

import com.example.zafiro5.loginregistrorecetas.R;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

public class PDFViewer extends AppCompatActivity {

    // Visor de pdf
    private PDFView receta;
    private android.support.v7.widget.Toolbar toolbar;
    // Variable que almacena la direccion del pdf
    private String uriReceta;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfviewer);

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbarpdf);
        toolbar.setBackgroundColor(getResources().getColor(R.color.primary));
        toolbar.setTitle("Visor PDF");
        toolbar.setTitleTextColor(Color.WHITE);
        receta = (PDFView) findViewById(R.id.visorPdf);

        Intent intent = getIntent();
        uriReceta = intent.getStringExtra("receta");
        cargarPdf();
    }
    // Método para cargar el pdf en la actividad
    private void cargarPdf(){
        /* Los pdf se guardan en la carpeta PDF de assets, por tanto, la uri guardada en la base de datos para los pdf que vienen por defecto empieza por PDF/
         * Así que solo tengo que comprobar la primera letra para saber si los pdf están ubicados en assets o no. */
        if (uriReceta.charAt(0) =='P'){
            this.receta.fromAsset(this.uriReceta) //ASSETS
                    .enableSwipe(true)
                    .enableDoubletap(true)
                    .enableAntialiasing(true)
                    .load();
        }else {
            this.receta.fromFile(new File(this.uriReceta)) //FROM FILE
                    .enableSwipe(true)
                    .enableDoubletap(true)
                    .enableAntialiasing(true)
                    .load();
        }
    }
}
