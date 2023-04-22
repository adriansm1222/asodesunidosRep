package com.example.asodesunidos;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
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
    Float ahorroActual;
    Float salarioCliente;
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
                String et_monto = editTextNumber.getText().toString();
                if(et_monto.isEmpty()){
                    editTextNumber.setError("Ingrese un monto");
                }
                else{
                    monto = Float.parseFloat(editTextNumber.getText().toString());
                    if(monto>=5000){
                        verificaSalario();
                        if(salarioCliente!=null){
                            if (salarioCliente >= monto) {
                                modificarSalario();
                                insertarAhorro();
                                finish();
                            }
                            else{
                                Toast.makeText(IngresaAhorro.this, "Su salario no cuenta con fondos suficientes", Toast.LENGTH_SHORT).show();
                            }

                        }
                        else{
                            Toast.makeText(IngresaAhorro.this, "Ha ocurrido un error con su usuario", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                    else{
                        editTextNumber.setError("Ingreese una cantidad mayor a ₡5000");
                    }
                }

            }
        });

    }

    public void verificaSalario(){
        BaseDatos admin = new BaseDatos(this, "asodesunidos", null, 1);
        SQLiteDatabase database = admin.getWritableDatabase();


        Cursor fila = database.rawQuery("select salario from cliente where cedula = " + Integer.parseInt(usuario), null);
        if (fila.moveToFirst()) {
            salarioCliente = fila.getFloat(0);
        }
        else{
            Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
        }
        database.close();

    }


    public void modificarSalario(){
        BaseDatos admin = new BaseDatos(this, "asodesunidos", null, 1);
        SQLiteDatabase database = admin.getWritableDatabase();
        verificaAhorroActual();

        ContentValues registro = new ContentValues();
        if(ahorroActual == 0){
            Float nuevoSalario = salarioCliente-monto;
            registro.put("salario",nuevoSalario);

            int cantidad = database.update("cliente",registro,"cedula="+usuario,null);
            database.close();
            if(cantidad==1){
                Toast.makeText(this, "Ahorro ingresado correctamente", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(this, "Ha ocurrido un error al ingreasr el ahorro", Toast.LENGTH_LONG).show();

            }
        }
        else{
            if(ahorroActual>=monto){
                Float diferencia = ahorroActual-monto;
                Float nuevoSalario = salarioCliente+diferencia;
                registro.put("salario",nuevoSalario);
                int cantidad = database.update("cliente",registro,"cedula="+usuario,null);
                database.close();
                if(cantidad!=1){
                    Toast.makeText(this, "Ha ocurrido un error al ingreasr el ahorro", Toast.LENGTH_LONG).show();
                }

            }
            else{
                Float diferencia = monto - ahorroActual;
                Float nuevoSalario = salarioCliente-diferencia;
                registro.put("salario",nuevoSalario);
                int cantidad = database.update("cliente",registro,"cedula="+usuario,null);
                database.close();
                if(cantidad==1){
                    Toast.makeText(this, "Ahorro ingresado correctamente", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(this, "Ha ocurrido un error al ingreasr el ahorro", Toast.LENGTH_LONG).show();

                }
            }




        }




    }

    public void insertarAhorro(){
        BaseDatos admin = new BaseDatos(this, "asodesunidos", null, 1);
        SQLiteDatabase database = admin.getWritableDatabase();

        if(existeAhorro()){
            ContentValues registro = new ContentValues();
            registro.put("monto",monto);
            int cantidad = database.update("ahorro",registro,"cedulaCliente="+usuario+" and tipo="+tipoAhorro,null);
            database.close();
            if(cantidad==1){
                Toast.makeText(this, "Ahorro ingresado correctamente", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(this, "Ha ocurrido un error al ingreasr el ahorro", Toast.LENGTH_LONG).show();

            }
        }
        else{
            ContentValues registro = new ContentValues();
            registro.put("cedulaCliente",usuario);
            registro.put("monto",monto);
            registro.put("tipo",tipoAhorro);
            database.insert("ahorro",null,registro);
            database.close();
        }


    }

    public boolean existeAhorro(){
        BaseDatos admin = new BaseDatos(this, "asodesunidos", null, 1);
        SQLiteDatabase database = admin.getWritableDatabase();


        Cursor fila = database.rawQuery("select monto from ahorro where cedulaCliente = " + Integer.parseInt(usuario) + " and tipo = " + tipoAhorro, null);
        if (fila.moveToFirst()) {
            ahorroActual = fila.getFloat(0);
            database.close();
            return true;
        }
        else{
            ahorroActual = Float.valueOf(0);
            database.close();
            return false;
        }
    }


    public void verificaAhorroActual(){
        BaseDatos admin = new BaseDatos(this, "asodesunidos", null, 1);
        SQLiteDatabase database = admin.getWritableDatabase();


        Cursor fila = database.rawQuery("select monto from ahorro where cedulaCliente = " + Integer.parseInt(usuario) + " and tipo = " + tipoAhorro, null);
        if (fila.moveToFirst()) {
            ahorroActual = fila.getFloat(0);
        }
        else{
            ahorroActual = Float.valueOf(0);
        }
        database.close();
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