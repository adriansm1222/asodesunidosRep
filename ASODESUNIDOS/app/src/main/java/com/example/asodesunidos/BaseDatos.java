package com.example.asodesunidos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class BaseDatos extends SQLiteOpenHelper {

    public BaseDatos(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table cliente(cedula text primary key, nombre text, salario real, telefono text, fechaNacimiento text, estadoCivil integer, direccion text, tipo integer)");
        db.execSQL("create table prestamo(id integer primary key autoincrement, cedulaCliente text, monto real, periodo integer, tipo text, mensualidad real, foreign key(cedulaCliente) references Cliente(cedula))");
        db.execSQL("create table ahorro(id integer primary key autoincrement, cedulaCliente text, monto real, tipo text, foreign key(cedulaCliente) references Cliente(cedula))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
