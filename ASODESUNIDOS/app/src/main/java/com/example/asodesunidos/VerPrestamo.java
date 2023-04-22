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

import java.util.ArrayList;

public class VerPrestamo extends AppCompatActivity {
    TextView tvTipo, tvPlazo, tvInteres, tvMonto, tvSaldo, tvCuota;
    Button btnPagoM, btnPagoE;
    Float saldo, saldoAnterior;
    Float cuota;
    Integer cantCuotas;
    Double interes;
    String prestamo;

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
        btnPagoE = findViewById(R.id.btnPagoE);
        consultarPrestamo(prestamo);
        prestamoPago();
    }

    public void consultarPrestamo(String id){
        BaseDatos admin = new BaseDatos(this, "asodesunidos", null, 1);
        SQLiteDatabase baseDatos = admin.getWritableDatabase();
        Cursor fila = baseDatos.rawQuery("select * from prestamo where id = " + id, null);
        if(fila.moveToFirst()){
            tvMonto.setText(fila.getString(2));
            tvPlazo.setText(fila.getString(3));
            tvTipo.setText(tipoPrestamo(fila.getString(4)));
            tvInteres.setText(tasaInteres(tvTipo.getText().toString()));
            tvCuota.setText(fila.getString(5));
            tvSaldo.setText(fila.getString(6));
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
                saldo -= cuota;
                cantCuotas++;
                actualizarPrestamo();
                tvSaldo.setText(saldo.toString());
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

    public void pagoExtraordinario(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View vistaDialogo = inflater.inflate(R.layout.layout_dialog_extra, null);
        EditText pago = vistaDialogo.findViewById(R.id.pago);
        builder.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if(!pago.getText().toString().isEmpty() && !pago.getText().toString().equals("0")){
                    Float montoPago = Float.parseFloat(pago.getText().toString());
                    saldoAnterior = saldo;
                    saldo -= montoPago;
                    int plazo = Integer.parseInt(tvPlazo.getText().toString());
                    double cuotaF = calculaCuota(saldoAnterior, cuotasRestantes(plazo));
                    cuota = (float) cuotaF;
                    actualizarPrestamo();
                    tvSaldo.setText(saldo.toString());
                    tvCuota.setText(cuota.toString());
                    prestamoPago();
                }
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        // Set other dialog properties
        builder.setView(vistaDialogo);
        // Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public double calculaCuota(float monto, int plazo){
        double interesMensual = interes / 12;
        return (monto * interesMensual) / (1 - Math.pow(1 + interesMensual, -plazo));
    }

    public int cuotasRestantes(int plazo){
        int plazoMeses = plazo*12;
        return plazoMeses - cantCuotas;
    }

    public void prestamoPago(){
        if(saldo == 0.0){
            btnPagoM.setEnabled(false);
            btnPagoE.setEnabled(false);
            Toast.makeText(this, "Préstamo pagado!", Toast.LENGTH_SHORT).show();
        }
    }
}