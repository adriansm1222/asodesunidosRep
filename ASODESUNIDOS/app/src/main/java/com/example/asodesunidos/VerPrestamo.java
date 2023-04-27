package com.example.asodesunidos;

import static com.example.asodesunidos.PrestamoAdapter.tipoPrestamo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class VerPrestamo extends AppCompatActivity {
    TextView tvTipo, tvPlazo, tvInteres, tvMonto, tvSaldo, tvCuota;
    Button btnPagoM, btnVolver;
    Float saldo, saldoAnterior;
    Float cuota;
    Integer cantCuotas;
    Double interes;
    String prestamo;
    String cedula;
    public static final String CEDULA = "VerPrestamo.CEDULA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_prestamo);
        Intent intent = getIntent();
        prestamo = intent.getStringExtra(PrestamoAdapter.PRESTAMO);
        tvTipo = findViewById(R.id.tvTipoE);
        tvPlazo = findViewById(R.id.tvPlazoE);
        tvInteres = findViewById(R.id.tvInteresE);
        tvMonto = findViewById(R.id.tvMontoE);
        tvSaldo = findViewById(R.id.tvSaldoE);
        tvCuota = findViewById(R.id.tvCuotaE);
        btnPagoM = findViewById(R.id.btnPagoM);
        btnVolver = findViewById(R.id.btnVolver);
        consultarPrestamo(prestamo);
        prestamoPago();
    }

    public void consultarPrestamo(String id){
        BaseDatos admin = new BaseDatos(this, "asodesunidos", null, 1);
        SQLiteDatabase baseDatos = admin.getWritableDatabase();
        Cursor fila = baseDatos.rawQuery("select * from prestamo where id = " + id, null);
        if(fila.moveToFirst()){
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            decimalFormat.setRoundingMode(RoundingMode.DOWN);
            cedula = fila.getString(1);
            tvMonto.setText(decimalFormat.format(fila.getFloat(2)));
            tvPlazo.setText(fila.getString(3));
            tvTipo.setText(tipoPrestamo(fila.getString(4)));
            tvInteres.setText(tasaInteres(tvTipo.getText().toString()));
            tvCuota.setText(fila.getString(5));
            tvSaldo.setText(decimalFormat.format(fila.getFloat(6)));
            cuota = Float.parseFloat(fila.getString(5));
            saldo = Float.parseFloat(fila.getString(6));
            cantCuotas = Integer.parseInt(fila.getString(7));
        }
        baseDatos.close();
    }

    public String tasaInteres(String prestamo){
        switch (prestamo){
            case "Hipotecario": {
                interes = 7.5/100;
                return "7.5% de Interés";
            }
            case "Educación":{
                interes = 8.0/100;
                return "8% de Interés";
            }
            case "Personal":{
                interes = 10.0/100;
                return "10% de Interés";
            }
            case "Viajes":{
                interes = 12.0/100;
                return "12% de Interés";
            }
        }
        return "";
    }

    public void actualizarPrestamo(){
        BaseDatos admin = new BaseDatos(this, "asodesunidos", null, 1);
        SQLiteDatabase baseDatos = admin.getWritableDatabase();
        ContentValues registro = new ContentValues();
        registro.put("mensualidad", cuota.toString());
        registro.put("saldo", saldo.toString());
        registro.put("cuotasPagadas", cantCuotas.toString());
        int cantidad = baseDatos.update("prestamo", registro, "id=" + prestamo, null);
        if(cantidad == 1){
            Toast.makeText(this, "Pago realizado con éxito", Toast.LENGTH_SHORT).show();
        }
        baseDatos.close();
    }

    public void pagoMensual(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        builder.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                decimalFormat.setRoundingMode(RoundingMode.DOWN);
                saldo -= cuota;
                cantCuotas++;
                actualizarPrestamo();
                tvSaldo.setText(decimalFormat.format(saldo));
                prestamoPago();
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
    // Set other dialog properties
        builder.setView(inflater.inflate(R.layout.layout_dialog_mensual, null));
    // Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void volver(View view){
        Intent intent = new Intent(this, VerPrestamos.class);
        intent.putExtra(PantallaPrincipalClienteActivity.CEDULA, cedula);
        startActivity(intent);
        finish();
    }
    public void prestamoPago(){
        if(saldo == 0.0){
            btnPagoM.setEnabled(false);
            Toast.makeText(this, "Préstamo pagado!", Toast.LENGTH_SHORT).show();
        }
    }
}