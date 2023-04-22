package com.example.asodesunidos;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class CalculaCuota extends AppCompatActivity {

    TextView tvCalculaCuota, tvPrestamo, tvPlazo, tvCuota;
    Spinner spPrestamo, spPlazo;
    EditText etInteres, etMonto;
    Button btnCalcular, btnLimpiar;

    double interes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calcula_cuota);

        //Inicializar componentes
        tvCalculaCuota = findViewById(R.id.tvCalculaCuota);
        tvPrestamo = findViewById(R.id.tvPrestamo);
        tvPlazo = findViewById(R.id.tvPlazo);
        tvCuota = findViewById(R.id.tvCuota);
        spPrestamo = findViewById(R.id.spPrestamo);
        spPlazo = findViewById(R.id.spPlazo);
        etInteres = findViewById(R.id.etInteres);
        etMonto = findViewById(R.id.etMonto);
        btnCalcular = findViewById(R.id.btnCalcular);
        btnLimpiar = findViewById(R.id.btnLimpiar);

        //Configuracion spinners
        String []prestamos = {"Seleccione", "Hipotecario", "Educación", "Personal", "Viajes"};
        String []plazos = {"Seleccione", "3", "5", "10"};
        ArrayAdapter<String> adapterPr = new ArrayAdapter<String>(this, R.layout.layout_spinner, prestamos);
        ArrayAdapter<String> adapterPl = new ArrayAdapter<String>(this, R.layout.layout_spinner, plazos);
        spPrestamo.setAdapter(adapterPr);
        spPlazo.setAdapter(adapterPl);

        spPrestamo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tasaInteres(parent.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void tasaInteres(String prestamo){
        etInteres.setError(null);
        switch (prestamo){
            case "Seleccione":{
                etInteres.setText(null);
                interes = 0;
                break;
            }
            case "Hipotecario": {
                etInteres.setText("7.5% de Interés");
                interes = 7.5/100;
                break;
            }
            case "Educación":{
                etInteres.setText("8% de Interés");
                interes = 8.0/100;
                break;
            }
            case "Personal":{
                etInteres.setText("10% de Interés");
                interes = 10.0/100;
                break;
            }
            case "Viajes":{
                etInteres.setText("12% de Interés");
                interes = 12.0/100;
                break;
            }
        }
    }

    public void calcular(View view){
        float monto;
        int plazo;
        if (!camposVacios()){
            monto = Float.parseFloat(etMonto.getText().toString());
            plazo = Integer.parseInt(spPlazo.getSelectedItem().toString())*12;
            DecimalFormat format = new DecimalFormat("#.00");
            tvCuota.setText(String.format("₡%s", format.format(calculaCuota(monto, plazo))));
        }
    }

    public boolean camposVacios(){
        boolean flagVacio = false;
        if(spPlazo.getSelectedItem().equals("Seleccione")){
            Toast.makeText(this, "Debe seleccionar un plazo en años", Toast.LENGTH_LONG).show();
            flagVacio = true;
        }
        if(interes == 0){
            etInteres.setError("");
            etInteres.setText("Debe seleccionar el tipo de préstamo");
            flagVacio = true;
        }
        if(etMonto.getText().toString().isEmpty()){
            etMonto.setError("Debe ingresar el monto del préstamo");
            flagVacio = true;
        }
        return flagVacio;
    }

    public double calculaCuota(float monto, int plazo){
        double interesMensual = interes / 12;
        return Math.ceil((monto * interesMensual) / (1 - Math.pow(1 + interesMensual, -plazo)));
    }

    public void limpiar(View view){
        spPrestamo.setSelection(0);
        spPlazo.setSelection(0);
        tvCuota.setText(R.string.cuota);
        etMonto.setText(null);
    }
}