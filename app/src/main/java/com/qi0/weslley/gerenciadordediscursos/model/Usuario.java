package com.qi0.weslley.gerenciadordediscursos.model;

import com.google.firebase.database.Exclude;

public class Usuario {

    private String nome;
    private String congregacao;
    private String email;
    private String senha;

    public Usuario() {
    }

    public Usuario(String nome, String congregacao, String email, String senha) {
        this.nome = nome;
        this.congregacao = congregacao;
        this.email = email;
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCongregacao() {
        return congregacao;
    }

    public void setCongregacao(String congregacao) {
        this.congregacao = congregacao;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
