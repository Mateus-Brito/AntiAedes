package com.example.antiaedes;

import com.example.antiaedes.entities.Usuario;

import java.io.Serializable;

public class Session implements Serializable {

    private int id;
    private String cpf;
    private String nome;
    private String email;
    private int saldo;

    public Session() {
    }

    //Constructor for functionary
    public Session(int id, String cpf, String nome, String email) {
        this.id = id;
        this.cpf = cpf;
        this.nome = nome;
        this.email = email;
    }

    //Constructor for User
    public Session(int id, String nome, String email, int saldo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.saldo = saldo;
        this.cpf="";
    }

    //
    public Session(Usuario usuario){
        this.id = usuario.getId();
        this.nome = usuario.getNome();
        this.email = usuario.getEmail();
        this.saldo = usuario.getSaldo();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getSaldo() {
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }
}
