package com.example.asodesunidos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class PantallaPrincipalClienteActivity extends AppCompatActivity {

    String usuario;
    public static final String CEDULA = "PantallaPrincipalClienteActivity.CEDULA";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_principal_cliente);

        Intent intent = getIntent();
        if(intent.getStringExtra(CEDULA) != null){
            usuario = intent.getStringExtra(CEDULA);
        }else {
            usuario = intent.getStringExtra(MainActivity.CEDULA);
        }
    }

    public void verPrestamos(View view){
        Intent intent = new Intent(this, VerPrestamos.class);
        intent.putExtra(CEDULA, usuario);
        startActivity(intent);
        finish();
    }

    public void gestionarAhorros(View view){
        Intent intent = new Intent(this, GestionaAhorros.class);
        intent.putExtra(CEDULA, usuario);
        startActivity(intent);
        finish();
    }

    public void calculaCuota(View view){
        Intent intent = new Intent(this, CalculaCuota.class);
        intent.putExtra(CEDULA, usuario);
        startActivity(intent);
        finish();
    }

    public void infoPersonal(View view){
        Intent intent = new Intent(this, InformacionPersonal.class);
        intent.putExtra(CEDULA, usuario);
        startActivity(intent);
        finish();
    }
    public void logOut(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}