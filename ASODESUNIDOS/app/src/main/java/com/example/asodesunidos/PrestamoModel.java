package com.example.asodesunidos;

public class PrestamoModel {
    String id, cedulaCliente, periodo, tipo, mensualidad;
    float saldo, monto;

    public PrestamoModel(String id, String cedulaCliente, float monto, String periodo, String tipo, String mensualidad, float saldo) {
        this.id = id;
        this.cedulaCliente = cedulaCliente;
        this.monto = monto;
        this.periodo = periodo;
        this.tipo = tipo;
        this.mensualidad = mensualidad;
        this.saldo = saldo;
    }

    public String getId() {
        return id;
    }

    public String getCedulaCliente() {
        return cedulaCliente;
    }



    public String getPeriodo() {
        return periodo;
    }

    public String getTipo() {
        return tipo;
    }

    public String getMensualidad() {
        return mensualidad;
    }

    public float getSaldo() {
        return saldo;
    }

    public float getMonto() {
        return monto;
    }
}
