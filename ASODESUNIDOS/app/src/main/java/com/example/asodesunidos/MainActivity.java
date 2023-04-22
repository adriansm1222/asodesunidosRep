package com.example.asodesunidos;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText username;
    EditText password;
    Button iniciarSesionBtn;

    public static final String CEDULA = "MainActivity.CEDULA";

    @SuppressLint("MissingInflatedId")

public class MainActivity extends AppCompatActivity {

    Button btn;
    public static final String CEDULA = "MainActivity.CEDULA";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        iniciarSesionBtn = findViewById(R.id.iniciarSesionBtn);

        consultar();
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

    public void onClickIniciarSesion(View view){ //Consultar BD
        username.setError(null);
        username.setError(null);

        String et_usuario = username.getText().toString();
        String et_contrasenia = password.getText().toString();


        if(et_usuario.isEmpty()){
            username.setError("Este campo no puede estar vac[io"); // si está vacío el método finaliza
            return;
        }
        if(et_contrasenia.isEmpty()){
            password.setError("Este campo no puede estar vac[io");
            return;
        }

        BaseDatos admin = new BaseDatos(this, "asodesunidos", null, 1);
        SQLiteDatabase database = admin.getWritableDatabase();


        Cursor fila = database.rawQuery("select nombre,tipo from cliente where cedula = " + Integer.parseInt(et_usuario), null);
        if(fila.moveToFirst()){
            String responseNombre = fila.getString(0);
            String responseTipo = fila.getString(1);

            if(et_contrasenia.equals("1234")){
                if(responseTipo.equals("0")){
                    //Intent intent = new Intent(this,)
                    //startActivity(intent);
                    Toast.makeText(this, "Admin", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent(this,PantallaPrincipalClienteActivity.class);
                    intent.putExtra(CEDULA, et_usuario);
                    startActivity(intent);

                }
            }
            else{
                Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
            }


        }
        else{
            Toast.makeText(this, "Datos no encontrados", Toast.LENGTH_LONG).show();
        }


        database.close();
    }

}