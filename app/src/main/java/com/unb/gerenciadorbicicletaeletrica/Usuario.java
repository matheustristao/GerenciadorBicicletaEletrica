package com.unb.gerenciadorbicicletaeletrica;

import java.io.Serializable;

/**
 * Created by fabianamitsu on 15/05/15.
 */
public class Usuario implements Serializable{
    private String nome;
    private String sobrenome;
    private String email;
    private String telefone;
    private String senha;
    private String corrente;

    public Usuario() {
    }

    public Usuario(String nome, String sobrenome, String email, String telefone, String corrente) {
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.email = email;
        this.telefone = telefone;
        this.corrente = corrente;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getCorrente() {
        return corrente;
    }

    public void setCorrente(String corrente) {
        this.corrente = corrente;
    }
}
