package com.example.asodesunidos;

import static android.app.PendingIntent.getActivity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.math.BigDecimal;
import java.util.ArrayList;

public class InformacionPersonal extends AppCompatActivity {

    TextView tvCedula;
    Spinner spEstado;
    EditText etNombre, etTelefono, etSalario, etFecha, etDireccion;
    Button btnVolver, btnGuardar;

    ArrayList<String> infoCliente = new ArrayList<>();
    ArrayList<EditText> editTexts = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_personal);
        String []opciones = {"Soltero/a", "Casado/a", "Divorciado/a", "Viudo/a", "Unión Libre"};
        Intent intent = getIntent();
        String cedula = intent.getStringExtra(PantallaPrincipalClienteActivity.CEDULA);

        //Inicializar componentes
        spEstado = findViewById(R.id.spEstado);
        tvCedula = findViewById(R.id.cedulaTV);
        etNombre = findViewById(R.id.etNombre);
        etTelefono = findViewById(R.id.etTelefono);
        etSalario = findViewById(R.id.etSalario);
        etFecha = findViewById(R.id.etFecha);
        etDireccion = findViewById(R.id.etDireccion);
        editTexts.add(etNombre);
        editTexts.add(etTelefono);
        editTexts.add(etSalario);
        editTexts.add(etFecha);
        editTexts.add(etDireccion);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnVolver = findViewById(R.id.btnVolver);
        //Configurar spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.layout_spinner, opciones);
        spEstado.setAdapter(adapter);

        //Configuracion etFecha
        etFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.etFecha:
                        showDatePickerDialog();
                        break;
                }
            }
        });
        //Configurar campos mediante busqueda en la base de datos
        consultarCliente(cedula);
    }

    public void consultarCliente(String cedula){
        BaseDatos admin = new BaseDatos(this, "asodesunidos", null, 1);
        SQLiteDatabase baseDatos = admin.getWritableDatabase();
        Cursor fila = baseDatos.rawQuery("select * from cliente where cedula = " + cedula, null);
        if(fila.moveToFirst()){
            //Set informacion cliente en campos
            tvCedula.setText(fila.getString(0));
            etNombre.setText(fila.getString(1));
            Double salarioD = Double.valueOf(fila.getString(2));
            String salarioFormato = BigDecimal.valueOf(salarioD).toPlainString();
            etSalario.setText("₡" + salarioFormato);
            etTelefono.setText(fila.getString(3));
            etFecha.setText(fila.getString(4));
            setEstadoSpinner(fila.getString(5));
            etDireccion.setText(fila.getString(6));

            //Guarda informacion del cliente cuando se accesa por primera vez
            for (int i = 0; i < fila.getColumnCount(); i++) {
                if (i == 2) {
                    infoCliente.add(salarioFormato);
                } else {
                    infoCliente.add(fila.getString(i));
                }
            }
        }
    }

    public void setEstadoSpinner(String estado){
        //0-Soltero, 1-Casado, 2-Divorciado, 3-Viudo, 4-UL
        switch (estado){
            case "0": {
                spEstado.setSelection(0);
                break;
            }
            case "1":{
                spEstado.setSelection(1);
                break;
            }
            case "2":{
                spEstado.setSelection(2);
                break;
            }
            case "3":{
                spEstado.setSelection(3);
                break;
            }
            case "4":{
                spEstado.setSelection(4);
                break;
            }
        }
    }


    private String twoDigits(int n) {
        return (n<=9) ? ("0"+n) : String.valueOf(n);
    }

    private void showDatePickerDialog() {
        DatePickerDialog.OnDateSetListener onNewDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String dayStr = twoDigits(dayOfMonth);
                String monthStr = twoDigits(month+1); // +1 because January is zero
                String selectedDate = dayStr + "/" + monthStr + "/" + year;
                etFecha.setText(selectedDate);
            }
        };
        String birthDate = etFecha.getText().toString();
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

    public void guardar(View view) {
        if (!camposVacios()) {
            if (detectarCambios()) {
                //Si se detectan cambios, se procede a actualizar la base de datos
                BaseDatos admin = new BaseDatos(this, "asodesunidos", null, 1);
                SQLiteDatabase baseDatos = admin.getWritableDatabase();
                ContentValues registro = new ContentValues();
                registro.put("cedula", infoCliente.get(0));
                registro.put("nombre", infoCliente.get(1));
                registro.put("salario", infoCliente.get(2));
                registro.put("telefono", infoCliente.get(3));
                registro.put("fechaNacimiento", infoCliente.get(4));
                registro.put("estadoCivil", infoCliente.get(5));
                registro.put("direccion", infoCliente.get(6));
                registro.put("tipo", infoCliente.get(7));
                int cantidad = baseDatos.update("cliente", registro, "cedula=" + infoCliente.get(0), null);
                if(cantidad == 1){
                    Toast.makeText(this, "Informacion guardada exitosamente", Toast.LENGTH_SHORT).show();
                }
                baseDatos.close();
            }else{
                Toast.makeText(this, "No se han modificado los datos", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean camposVacios(){
        //Detecta si se dejaron campos vacios
        boolean flagVacio = false;
        for (int i = 0; i < editTexts.size(); i++) {
            if (editTexts.get(i).getText().toString().isEmpty()){
                flagVacio = true;
                editTexts.get(i).setError("No debe dejar este campo vacio");
            }
        }
        return flagVacio;
    }

    public boolean detectarCambios(){
        //Detecta si se genero cambios en la informacion del formulario para actualizarla
        boolean flagCambios = false;
        String nombre = etNombre.getText().toString();
        if(!infoCliente.get(1).equals(nombre)){
            infoCliente.set(1, nombre);
            flagCambios = true;
        }
        String []salarioPartes = etSalario.getText().toString().split("₡");
        Double salarioD = Double.valueOf(salarioPartes[1]);
        String salarioFormato = BigDecimal.valueOf(salarioD).toPlainString();
        if(!infoCliente.get(2).equals(salarioFormato)){
            infoCliente.set(2, salarioFormato);
            flagCambios = true;
        }
        String telefono = etTelefono.getText().toString();
        if(!infoCliente.get(3).equals(telefono)){
            infoCliente.set(3, telefono);
            flagCambios = true;
        }
        String fecha = etFecha.getText().toString();
        if(!infoCliente.get(4).equals(fecha)){
            infoCliente.set(4, fecha);
            flagCambios = true;
        }
        String estado = estadoCivil();
        if(!infoCliente.get(5).equals(estado)){
            infoCliente.set(5, estado);
            flagCambios = true;
        }
        String direccion = etDireccion.getText().toString();
        if(!infoCliente.get(6).equals(direccion)){
            infoCliente.set(6, direccion);
            flagCambios = true;
        }
        return flagCambios;
    }

    private String estadoCivil(){
        String estado = spEstado.getSelectedItem().toString();
        String estadoCodigo = "";
        //0-Soltero, 1-Casado, 2-Divorciado, 3-Viudo, 4-UL
        switch (estado){
            case "Soltero/a": {
                estadoCodigo = "0";
                break;
            }
            case "Casado/a":{
                estadoCodigo = "1";
                break;
            }
            case "Divorciado/a":{
                estadoCodigo = "2";
                break;
            }
            case "Viudo/a":{
                estadoCodigo = "3";
                break;
            }
            case "Unión Libre":{
                estadoCodigo = "4";
                break;
            }
        }
        return estadoCodigo;
    }
}

