package com.qi0.weslley.gerenciadordediscursos.model;

public class Orador {

    private String nome;
    private String congregacao;
    private String ultimaVisita;

    public Orador() {
    }

    public Orador(String nome, String congregacao, String ultimaVisita) {
        this.nome = nome;
        this.congregacao = congregacao;
        this.ultimaVisita = ultimaVisita;
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

    public String getUltimaVisita() {
        return ultimaVisita;
    }

    public void setUltimaVisita(String ultimaVisita) {
        this.ultimaVisita = ultimaVisita;
    }
}
