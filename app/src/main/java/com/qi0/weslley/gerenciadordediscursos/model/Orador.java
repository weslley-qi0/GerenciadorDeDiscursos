package com.qi0.weslley.gerenciadordediscursos.model;

import java.io.Serializable;
import java.util.List;

public class Orador implements Serializable{

    private String id;
    private String nome;
    private Congregacao congregacao;
    private String telefone;
    private String email;
    private String ultimaVisita;
    private String urlFotoOrador;
    private List<Discurso> discursoListOrador;
    private float ratingOrador;

    public Orador() {
    }

    public Orador(String id, String nome, Congregacao congregacao, String telefone, String email, String ultimaVisita, String urlFotoOrador, List<Discurso> discursoListOrador, float ratingOrador) {
        this.id = id;
        this.nome = nome;
        this.congregacao = congregacao;
        this.telefone = telefone;
        this.email = email;
        this.ultimaVisita = ultimaVisita;
        this.urlFotoOrador = urlFotoOrador;
        this.discursoListOrador = discursoListOrador;
        this.ratingOrador = ratingOrador;
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

    public String getUrlFotoOrador() {
        return urlFotoOrador;
    }

    public void setUrlFotoOrador(String urlFotoOrador) {
        this.urlFotoOrador = urlFotoOrador;
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
