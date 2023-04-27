package com.example.asodesunidos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class VerPrestamos extends AppCompatActivity {
    RecyclerView recyclerView;
    PrestamoAdapter adapter;
    List<PrestamoModel> prestamosCliente;
    public static final String CEDULA = "VerPrestamos.CEDULA";
    String cedula;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_prestamos);
        Intent intent = getIntent();
        if(intent.getStringExtra(PantallaPrincipalClienteActivity.CEDULA) != null){
            cedula = intent.getStringExtra(PantallaPrincipalClienteActivity.CEDULA);
        }else{
            cedula = intent.getStringExtra(VerPrestamo.CEDULA);
        }
        recyclerView = findViewById(R.id.recycler_view);
        prestamosCliente = new ArrayList<>();
        consultarPrestamos(cedula);
        setRecyclerView();
    }


    private void setRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PrestamoAdapter(this, prestamosCliente);
        recyclerView.setAdapter(adapter);
    }

    private void consultarPrestamos(String cedula){
        BaseDatos admin = new BaseDatos(this, "asodesunidos", null, 1);
        SQLiteDatabase baseDatos = admin.getWritableDatabase();
        Cursor fila = baseDatos.rawQuery("select * from prestamo where cedulaCliente = " + cedula, null);
        while (fila.moveToNext()) {
            prestamosCliente.add(new PrestamoModel(fila.getString(0), fila.getString(1), fila.getFloat(2), fila.getString(3), fila.getString(4), fila.getString(5), fila.getFloat(6)));
        }
        baseDatos.close();
    }

    public void volver(View view){
        Intent intent = new Intent(this, PantallaPrincipalClienteActivity.class);
        intent.putExtra(PantallaPrincipalClienteActivity.CEDULA, cedula);
        startActivity(intent);
        finish();
    }

}