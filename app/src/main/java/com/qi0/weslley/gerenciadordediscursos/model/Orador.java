package com.qi0.weslley.gerenciadordediscursos.model;

import java.util.List;

public class Orador {

    private String id;
    private String nome;
    private Congregacao congregacao;
    private String congregacaoTest;
    private String telefone;
    private String email;
    private String ultimaVisita;
    private List<Discurso> discursoListOrador;
    private float ratingOrador;

    public Orador() {
    }

    public Orador(String id, String nome, Congregacao congregacao, String telefone, String email, String ultimaVisita, List<Discurso> discursoListOrador, float ratingOrador) {
        this.id = id;
        this.nome = nome;
        this.congregacao = congregacao;
        this.telefone = telefone;
        this.email = email;
        this.ultimaVisita = ultimaVisita;
        this.discursoListOrador = discursoListOrador;
        this.ratingOrador = ratingOrador;
    }

    public Orador(String nome, String congregacaoTest, String ultimaVisita) {
        this.nome = nome;
        this.congregacaoTest = congregacaoTest;
        this.ultimaVisita = ultimaVisita;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Congregacao getCongregacao() {
        return congregacao;
    }

    public void setCongregacao(Congregacao congregacao) {
        this.congregacao = congregacao;
    }

    public String getCongregacaoTest() {
        return congregacaoTest;
    }

    public void setCongregacaoTest(String congregacaoTest) {
        this.congregacaoTest = congregacaoTest;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUltimaVisita() {
        return ultimaVisita;
    }

    public void setUltimaVisita(String ultimaVisita) {
        this.ultimaVisita = ultimaVisita;
    }

    public List<Discurso> getDiscursoListOrador() {
        return discursoListOrador;
    }

    public void setDiscursoListOrador(List<Discurso> discursoListOrador) {
        this.discursoListOrador = discursoListOrador;
    }

    public float getRatingOrador() {
        return ratingOrador;
    }

    public void setRatingOrador(float ratingOrador) {
        this.ratingOrador = ratingOrador;
    }
}
