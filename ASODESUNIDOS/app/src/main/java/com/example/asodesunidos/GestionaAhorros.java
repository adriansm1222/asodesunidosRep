package com.example.asodesunidos;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class GestionaAhorros extends AppCompatActivity {

    TextView textViewEscolar;
    TextView textViewNavideño;
    TextView textViewExtraordinario;
    TextView textViewMarchamo;

    Button buttonAddExtra;
    Button buttonAddMarchamo;
    Button buttonAddEscolar;
    Button buttonAddNavideño;

    Button buttonVolver;
    String usuario;

    public static final String CEDULA = "GestionaAhorros.CEDULA";
    public static final String TIPOAHORRO = "GestionaAhorros.TIPOAHORRO";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestiona_ahorros);

        Intent intent = getIntent();
        usuario = intent.getStringExtra(PantallaPrincipalClienteActivity.CEDULA);


        textViewEscolar = findViewById(R.id.textViewEscolar);
        textViewNavideño = findViewById(R.id.textViewNavideño);
        textViewExtraordinario = findViewById(R.id.textViewExtraordinario);
        textViewMarchamo = findViewById(R.id.textViewMarchamo);
        buttonAddNavideño = findViewById(R.id.buttonAddNavideño);
        buttonAddEscolar = findViewById(R.id.buttonAddEscolar);
        buttonAddMarchamo = findViewById(R.id.buttonAddMarchamo);
        buttonAddExtra = findViewById(R.id.buttonAddExtra);
        buttonVolver = findViewById(R.id.ButtonVolver);

        consultarPrestamosCliente();
        addListeners();


    }


    @Override
    protected void onResume() {
        super.onResume();
        consultarPrestamosCliente();
    }

    public void addListeners(){
        buttonAddNavideño.setOnClickListener(v -> ingresaAhorro("1"));
        buttonAddEscolar.setOnClickListener(v -> ingresaAhorro("2"));
        buttonAddMarchamo.setOnClickListener(v -> ingresaAhorro("3"));
        buttonAddExtra.setOnClickListener(v -> ingresaAhorro("4"));


    }

    public void volver(View view){
        Intent intent = new Intent(this, PantallaPrincipalClienteActivity.class);
        intent.putExtra(PantallaPrincipalClienteActivity.CEDULA, usuario);
        startActivity(intent);
        finish();
    }


    public void ingresaAhorro(String tipoAhorro){

        Intent intent = new Intent(this, IngresaAhorro.class);
        intent.putExtra(CEDULA, usuario);
        intent.putExtra(TIPOAHORRO,tipoAhorro);
        startActivity(intent);

    }

    @SuppressLint("SetTextI18n")
    public void consultarPrestamosCliente(){

        BaseDatos admin = new BaseDatos(this, "asodesunidos", null, 1);
        SQLiteDatabase database = admin.getWritableDatabase();


        Cursor fila = database.rawQuery("select monto,tipo from ahorro where cedulaCliente = " + Integer.parseInt(usuario), null);
        if (fila.moveToFirst()) {
            do{
                String responseMonto = fila.getString(0);
                String responseTipo = fila.getString(1);


                switch (responseTipo){
                    case "1": // navideño
                        textViewNavideño.setText("₡"+responseMonto);
                        break;

                    case "2":  // escolar
                        textViewEscolar.setText("₡"+responseMonto);
                        break;

                    case "3":  //marchamo
                        textViewMarchamo.setText("₡"+responseMonto);
                        break;

                    case "4":  //extraordinario
                        textViewExtraordinario.setText("₡"+responseMonto);
                        break;

                    default:
                        Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
                        break;
                }

            }while(fila.moveToNext());
        }
        fila.close();
        database.close();
    }




}