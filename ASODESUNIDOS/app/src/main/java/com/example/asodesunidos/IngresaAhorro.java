package com.example.asodesunidos;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class IngresaAhorro extends AppCompatActivity {

    TextView textViewTipoAhorro;
    EditText editTextNumber;
    String tipoAhorro;
    String usuario;
    String current = "";

    Button button;

    Float monto;


    public static final String CEDULA = "GestionaAhorros.CEDULA";
    public static final String TIPOAHORRO = "GestionaAhorros.CEDULA";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresa_ahorro);

        Intent intent = getIntent();
        usuario = intent.getStringExtra(GestionaAhorros.CEDULA);
        tipoAhorro = intent.getStringExtra(GestionaAhorros.TIPOAHORRO);
        Toast.makeText(this, usuario, Toast.LENGTH_SHORT).show();

        textViewTipoAhorro = findViewById(R.id.textViewTipoAhorro);

        editTextNumber = findViewById(R.id.editTextNumber);

        button = findViewById(R.id.button);

        tituloVentana();
        addListeners();

    }

    public void addListeners(){
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                editTextNumber.setError(null);
                monto = Float.parseFloat(editTextNumber.getText().toString());

                if(monto>=5000 || editTextNumber.getText().equals(" ")){
                    insertarAhorro();
                }
                else{
                    editTextNumber.setError("Ingreese una cantidad mayor a ₡5000");
                }
            }
        });

    }

    public void insertarAhorro(){
        BaseDatos admin = new BaseDatos(this, "asodesunidos", null, 1);
        SQLiteDatabase database = admin.getWritableDatabase();

        ContentValues registro = new ContentValues();
        registro.put("cedulaCliente",usuario);
        registro.put("monto",monto);
        registro.put("tipo",tipoAhorro);
        database.insert("ahorro",null,registro);
        database.close();
        Toast.makeText(this, "Ahorro ingresado correctamente", Toast.LENGTH_LONG).show();
        finish();

    }

    public void tituloVentana(){
        switch (tipoAhorro){

            case "1":
                textViewTipoAhorro.setText("Ahorro Navideño");
                break;

            case "2":
                textViewTipoAhorro.setText("Ahorro Escolar");
                break;

            case "3":
                textViewTipoAhorro.setText("Ahorro Marchamo");
                break;

            case "4":
                textViewTipoAhorro.setText("Ahorro Extraordinario");
                break;

            default:
                Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
                break;
        }
    }

}