package com.example.asodesunidos;

import androidx.appcompat.app.AppCompatActivity;

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

    String usuario;

    public static final String CEDULA = "GestionaAhorros.CEDULA";
    public static final String TIPOAHORRO = "GestionaAhorros.TIPOAHORRO";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestiona_ahorros);

        Intent intent = getIntent();
        usuario = intent.getStringExtra(PantallaPrincipalClienteActivity.CEDULA);
        Toast.makeText(this, usuario, Toast.LENGTH_SHORT).show();

        textViewEscolar = findViewById(R.id.textViewEscolar);
        textViewNavideño = findViewById(R.id.textViewNavideño);
        textViewExtraordinario = findViewById(R.id.textViewExtraordinario);
        textViewMarchamo = findViewById(R.id.textViewMarchamo);
        buttonAddNavideño = findViewById(R.id.buttonAddNavideño);
        buttonAddEscolar = findViewById(R.id.buttonAddEscolar);
        buttonAddMarchamo = findViewById(R.id.buttonAddMarchamo);
        buttonAddExtra = findViewById(R.id.buttonAddExtra);

        consultarPrestamosCliente();
        addListeners();


    }

    @Override
    protected void onResume() {
        super.onResume();
        consultarPrestamosCliente();
    }

    public void addListeners(){
        buttonAddNavideño.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ingresaAhorro("1");
            }
        });
        buttonAddEscolar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ingresaAhorro("2");
            }
        });
        buttonAddMarchamo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ingresaAhorro("3");
            }
        });
        buttonAddExtra.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ingresaAhorro("4");
            }
        });

    }


    public void ingresaAhorro(String tipoAhorro){

        Intent intent = new Intent(this, IngresaAhorro.class);
        intent.putExtra(CEDULA, usuario);
        intent.putExtra(TIPOAHORRO,tipoAhorro);
        startActivity(intent);

    }

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



    }




}