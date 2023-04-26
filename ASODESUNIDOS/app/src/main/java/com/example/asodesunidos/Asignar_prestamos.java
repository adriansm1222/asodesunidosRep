package com.example.asodesunidos;

import static java.lang.Math.ceil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Asignar_prestamos extends AppCompatActivity {

    Button buscar, regresar, asignar;

    EditText cedulaCl, credito;

    TextView nombreCl,salarioCl,cuota,total,interes;
    Spinner plazo, tipo;

    String[] plazos = new String[]{"3 años","5 años","10 años"};
    String[] tipos = new String[]{"Hipotecario","Educacion","Personal","Viajes"};

    TableLayout layoutPrestamo;
    TableRow calculo1,calculo2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asignar_prestamos);
        cedulaCl = findViewById(R.id.cedulaPrestamo);
        nombreCl = findViewById(R.id.nombrePrestamo);
        salarioCl = findViewById(R.id.salarioPr);
        credito = findViewById(R.id.creditoPr);
        cuota = findViewById(R.id.cuotaPr);
        total = findViewById(R.id.totalPr);
        interes = findViewById(R.id.interesPr);
        plazo = findViewById(R.id.plazoSp);
        tipo = findViewById(R.id.tipoSp);
        layoutPrestamo = findViewById(R.id.tablaPrestamo);
        calculo1 = findViewById(R.id.calculo1);
        calculo2 = findViewById(R.id.calculo2);
        buscar = findViewById(R.id.buscarCliente);
        asignar = findViewById(R.id.asignar);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.layout_spinner, plazos);
        plazo.setAdapter(adapter);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                R.layout.layout_spinner, tipos);
        tipo.setAdapter(adapter2);
    }

    public void limpiarCliente(){
        cedulaCl.setText("");
        nombreCl.setText("");
        salarioCl.setText("");
    }
    public void limpiarPrestamo(){
        credito.setText("");
        cuota.setText("");
        interes.setText("");
        total.setText("");
    }
    public void buscarCliente(View view){
        BaseDatos asodesunidos = new BaseDatos(this,"asodesunidos",null,1);
        SQLiteDatabase bd = asodesunidos.getWritableDatabase();
        String cedulaBD = cedulaCl.getText().toString();
        if(!cedulaBD.isEmpty()){
            Cursor fila = bd.rawQuery("select nombre, salario from cliente where cedula = " + cedulaBD,null);
            if(fila.moveToFirst()){
                Float r = fila.getFloat(1);
                DecimalFormat decimalFormat = new DecimalFormat("#.#");
                decimalFormat.setRoundingMode(RoundingMode.DOWN);
                nombreCl.setText(fila.getString(0));
                salarioCl.setText(decimalFormat.format(fila.getFloat(1)));
                layoutPrestamo.setVisibility(View.VISIBLE);
            }else{
                Toast.makeText(this,"ERROR AL BUSCAR CLIENTE: NO HAY CLIENTE REGISTRADO CON LA CEDULA INDICADA", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this,"ERROR AL BUSCAR CLIENTE: DEBE DIGITAR LA CEDULA", Toast.LENGTH_SHORT).show();
        }

    }

    public double calculaCuota(float monto, int plazo, float interes){
        double interesMensual = interes / 12;
        return (monto * interesMensual) / (1 - Math.pow(1 + interesMensual, -plazo));
    }

    public void calcular(View view){

        float salarioPorc = Float.parseFloat(salarioCl.getText().toString()) * 0.45F;
        float creditoCalc = Float.parseFloat(credito.getText().toString());

        int plazoCalc = convertidorPlazo(plazo.getSelectedItem().toString());
        int tipoCalc = tipoPrestamo(tipo.getSelectedItem().toString());
        float interesCalc = getInteres(tipoCalc);
        DecimalFormat decimalFormat = new DecimalFormat("#.##");

        int meses = plazoCalc * 12;
        float cuotaCalc = (float) Math.ceil(calculaCuota(creditoCalc,meses,interesCalc));
        float totalCalc = cuotaCalc * meses;

        if(cuotaCalc > salarioPorc){
            Toast.makeText(this,"ERROR AL ASIGNAR PRESTAMO: LA CUOTA NO PUEDE SER MAYOR A UN 45% DEL SALARIO", Toast.LENGTH_SHORT).show();
            limpiarPrestamo();
            return;
        }


        total.setText(decimalFormat.format(totalCalc));
        cuota.setText(Float.toString(cuotaCalc));
        interes.setText(decimalFormat.format(interesCalc*100) + "%");

        calculo1.setVisibility(View.VISIBLE);
        calculo2.setVisibility(View.VISIBLE);
        asignar.setVisibility(View.VISIBLE);
    }

    public void asignarPrestamo(View view){


        if(validaciones()){
            Toast.makeText(this,"ERROR AL ASIGNAR PRESTAMO: DEBE LLENAR TODOS LOS CAMPOS", Toast.LENGTH_SHORT).show();
            return;
        }
        try{
            String cedulaBD = cedulaCl.getText().toString();
            float creditoCalc = Float.parseFloat(credito.getText().toString());
            int plazoCalc = convertidorPlazo(plazo.getSelectedItem().toString());
            String tipoCalc = Integer.toString(tipoPrestamo(tipo.getSelectedItem().toString()));
            float cuotaCalc = Float.parseFloat(cuota.getText().toString());
            float totalCalc = Float.parseFloat(total.getText().toString());
            BaseDatos asodesunidos = new BaseDatos(this, "asodesunidos",null, 1);
            SQLiteDatabase bd = asodesunidos.getWritableDatabase();

            ContentValues prestamo = new ContentValues();
            prestamo.put("cedulaCliente", cedulaBD);
            prestamo.put("monto", creditoCalc);
            prestamo.put("periodo",plazoCalc);
            prestamo.put("tipo",tipoCalc);
            prestamo.put("mensualidad", cuotaCalc);
            prestamo.put("saldo",totalCalc);
            prestamo.put("cuotasPagadas",0);
            bd.insert("prestamo",null,prestamo);
            bd.close();
            Toast.makeText(this,"PRESTAMO INGRESADO EXITOSAMENTE", Toast.LENGTH_SHORT).show();
            limpiarCliente();
            limpiarPrestamo();
            layoutPrestamo.setVisibility(View.INVISIBLE);
            calculo1.setVisibility(View.INVISIBLE);
            calculo2.setVisibility(View.INVISIBLE);
            asignar.setVisibility(View.INVISIBLE);
        }catch(SQLiteConstraintException e){
            Toast.makeText(this,"NO SE HA PODIDO ASIGNAR EL PRESTAMO", Toast.LENGTH_SHORT).show();
        }

    }

    public boolean validaciones(){
        return cedulaCl.getText().toString().isEmpty() || salarioCl.getText().toString().isEmpty() || nombreCl.getText().toString().isEmpty() ||
                credito.getText().toString().isEmpty() || plazo.getSelectedItem().toString().isEmpty()|| tipo.getSelectedItem().toString().isEmpty() ||
                cuota.getText().toString().isEmpty();
    }
    public int convertidorPlazo(String plazo){
        int p = 0;

        switch (plazo){
            case "3 años":{
                p = 3;
                break;
            }
            case "5 años":{
                p = 5;
                break;
            }
            case "10 años":{
                p = 10;
                break;
            }
        }

        return p;
    }

    public float getInteres(int tipo){
        float interes = 0;

        switch (tipo){
            case 1:{
                interes = 7.5F/100;
                break;
            }
            case 2:{
                interes = 8.0F/100;
                break;
            }
            case 3:{
                interes = 10.0F/100;
                break;
            }
            case 4:{
                interes = 12.0F/100;
                break;
            }

        }
        return interes;
    }

    public int tipoPrestamo(String tipo){
        int t = 0;

        switch (tipo){
            case "Hipotecario":{
                t = 1;
                break;
            }
            case "Educacion":{
                t = 2;
                break;
            }
            case "Personal":{
                t = 3;
                break;
            }
            case "Viajes":{
                t = 4;
                break;
            }

        }
        return t;
    }
}