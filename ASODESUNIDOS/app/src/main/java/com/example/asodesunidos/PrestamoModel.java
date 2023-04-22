package com.example.asodesunidos;

public class PrestamoModel {
    String id, cedulaCliente, monto, periodo, tipo, mensualidad, saldo;

    public PrestamoModel(String id, String cedulaCliente, String monto, String periodo, String tipo, String mensualidad, String saldo) {
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

    public String getMonto() {
        return monto;
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

    public String getSaldo() {
        return saldo;
    }
}
