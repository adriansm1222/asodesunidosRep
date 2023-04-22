package com.example.asodesunidos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AgregarCliente extends AppCompatActivity {

    Button regresar, guardar;
    EditText cedula, direccion, telefono, nacimiento, nombre, salario;
    Spinner estadoCivil;

    String [] opciones = new String[]{"Soltero", "Casado", "Divorciado", "Viudo"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_cliente);

        cedula = findViewById(R.id.cedulaPrestamo);
        direccion = findViewById(R.id.direccionField);
        telefono = findViewById(R.id.telefonoField);
        nacimiento = findViewById(R.id.nacimientoField);
        nombre = findViewById(R.id.nombreField);
        salario = findViewById(R.id.salarioField);
        estadoCivil = findViewById(R.id.civilField);
        guardar = findViewById(R.id.guardar);

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertarCliente();
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.layout_spinner, opciones);
        estadoCivil.setAdapter(adapter);

        //Configuracion etFecha
        nacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.nacimientoField:
                        showDatePickerDialog();
                        break;
                }
            }
        });
    }

    private void showDatePickerDialog() {
        DatePickerDialog.OnDateSetListener onNewDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String dayStr = twoDigits(dayOfMonth);
                String monthStr = twoDigits(month+1); // +1 because January is zero
                String selectedDate = dayStr + "/" + monthStr + "/" + year;
                nacimiento.setText(selectedDate);
            }
        };

        String birthDate = nacimiento.getText().toString();

        DatePickerFragment newFragment;
        if (birthDate.isEmpty()) {
            newFragment = DatePickerFragment.newInstance(onNewDateListener);
        } else {
            String[] parts = birthDate.split("/");
            int mes = Integer.parseInt(parts[1])-1;
            newFragment = DatePickerFragment.newInstance(
                    onNewDateListener,
                    Integer.parseInt(parts[2]),
                    mes,
                    Integer.parseInt(parts[0])
            );
        }

        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private String twoDigits(int n) {
        return (n<=9) ? ("0"+n) : String.valueOf(n);
    }

    public void insertarCliente(){
        if(validaciones()){
            try{
                BaseDatos asodesunidos = new BaseDatos(this,"asodesunidos", null, 1);
                SQLiteDatabase bd = asodesunidos.getWritableDatabase();
                String cedulaBD = cedula.getText().toString();
                String nombreBD = nombre.getText().toString();
                String telefonoBD = telefono.getText().toString();
                String direccionBD = direccion.getText().toString();
                int eCivilBD = estadoCivil(estadoCivil.getSelectedItem().toString());
                Float salarioBD = Float.parseFloat(salario.getText().toString());
                String fNacimiento = nacimiento.getText().toString();

                ContentValues cliente = new ContentValues();
                cliente.put("cedula",cedulaBD);
                cliente.put("nombre", nombreBD);
                cliente.put("salario",salarioBD);
                cliente.put("telefono",telefonoBD);
                cliente.put("fechaNacimiento", fNacimiento);
                cliente.put("estadoCivil", eCivilBD);
                cliente.put("direccion", direccionBD);
                cliente.put("tipo", 2);
                bd.insert("cliente",null,cliente);
                bd.close();
                Toast.makeText(this,"CLIENTE INGRESADO EXITOSAMENTE", Toast.LENGTH_SHORT).show();
            }catch(SQLiteConstraintException e){
                Toast.makeText(this,"ERROR AL INGRESAR CLIENTE: Ya existe un cliente con ese numero de cedula", Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(this,"ERROR AL INGRESAR CLIENTE: DEBE LLENAR TODOS LOS CAMPOS", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean validaciones(){
        return !nulos();
    }

    public boolean nulos(){
        return nacimiento.getText().toString().isEmpty() || cedula.getText().toString().isEmpty() || direccion.getText().toString().isEmpty() ||
                telefono.getText().toString().isEmpty() || nombre.getText().toString().isEmpty() || salario.getText().toString().isEmpty() ||
                estadoCivil.getSelectedItem().toString().isEmpty();
    }

    public int estadoCivil(String estado){
        int x = 0;
        switch (estado){
            case "Soltero":{
                x = 1;
                break;
            }
            case "Casado":{
                x = 2;
                break;
            }
            case "Viudo":{
                x = 3;
                break;
            }
            case "Divorciado":{
                x = 4;
                break;
            }

        }

        return x;
    }
}