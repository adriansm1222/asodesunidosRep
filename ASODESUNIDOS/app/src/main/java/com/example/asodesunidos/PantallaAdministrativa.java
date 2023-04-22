package com.example.asodesunidos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PantallaAdministrativa extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_administrativa);
    }


    public void agregarCliente(View view){
        Intent intent = new Intent(this, AgregarCliente.class);
        startActivity(intent);
    }

    public void asignarPrestamo(View view){
        Intent intent = new Intent(this, Asignar_prestamos.class);
        startActivity(intent);
    }
}