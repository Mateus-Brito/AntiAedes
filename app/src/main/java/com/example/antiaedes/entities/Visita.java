package com.example.antiaedes.entities;

import java.io.Serializable;
import java.util.Date;

public class Visita implements Serializable {

    private static final long serialVersionUID = 1L;
    private int id_fun;
    private int id_den;
    private String data;
    private int situation;
    private String observacao;

    public Visita() {
    }

    public Visita(int id_fun, int id_den, String data, int situation, String observacao) {
        this.id_fun = id_fun;
        this.id_den = id_den;
        this.data = data;
        this.situation = situation;
        this.observacao = observacao;
    }

    public int getId_fun() {
        return id_fun;
    }

    public void setId_fun(int id_fun) {
        this.id_fun = id_fun;
    }

    public int getId_den() {
        return id_den;
    }

    public void setId_den(int id_den) {
        this.id_den = id_den;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getSituation() {
        return situation;
    }

    public void setSituation(int situation) {
        this.situation = situation;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}