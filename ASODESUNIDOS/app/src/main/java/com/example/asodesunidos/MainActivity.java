package com.example.asodesunidos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btn;
    public static final String CEDULA = "MainActivity.CEDULA";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.btnInfo);
    }

    public void consultar(){
        BaseDatos admin = new BaseDatos(this, "asodesunidos", null, 1);
        SQLiteDatabase baseDatos = admin.getWritableDatabase();

    }

    public void click(View vista){
        abrirInfo();
    }

    public void abrirInfo(){
        Intent intent = new Intent(this, InformacionPersonal.class);
        intent.putExtra(CEDULA, "231304429");
        startActivity(intent);
    }

    public void abrirConsulta(View vista){
        Intent intent = new Intent(this, CalculaCuota.class);
        startActivity(intent);
    }

    public void abrirPrestamos(View vista){
        Intent intent = new Intent(this, VerPrestamos.class);
        startActivity(intent);
    }
}