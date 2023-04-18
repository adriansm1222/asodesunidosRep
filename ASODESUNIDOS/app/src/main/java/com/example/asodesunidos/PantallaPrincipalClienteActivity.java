package com.example.asodesunidos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class PantallaPrincipalClienteActivity extends AppCompatActivity {

    String usuario;
    public static final String CEDULA = "PantallaPrincipalClienteActivity.CEDULA";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_principal_cliente);

        Intent intent = getIntent();
        usuario = intent.getStringExtra(MainActivity.CEDULA);
        Toast.makeText(this, usuario, Toast.LENGTH_SHORT).show();

    }

    public void verPrestamos(View view){

    }

    public void gestionarAhorros(View view){
        Intent intent = new Intent(this, GestionaAhorros.class);
        intent.putExtra(CEDULA, usuario);
        startActivity(intent);
    }

    public void calculaCuota(View view){

    }

    public void infoPersonal(View view){

    }

}