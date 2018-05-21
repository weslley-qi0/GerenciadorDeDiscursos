package com.qi0.weslley.gerenciadordediscursos.model;

public class Agenda {

    private String data;
    private String idCongregacao;
    private String idOrador;
    private String idDiscurso;

    public Agenda() {
    }



    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Agenda(String data, String idCongregacao, String idOrador, String idDiscurso) {
        this.data = data;
        this.idCongregacao = idCongregacao;
        this.idOrador = idOrador;
        this.idDiscurso = idDiscurso;
    }

    public String getIdCongregacao() {
        return idCongregacao;
    }

    public void setIdCongregacao(String idCongregacao) {
        this.idCongregacao = idCongregacao;
    }

    public String getIdOrador() {
        return idOrador;
    }

    public void setIdOrador(String idOrador) {
        this.idOrador = idOrador;
    }

    public String getIdDiscurso() {
        return idDiscurso;
    }

    public void setIdDiscurso(String idDiscurso) {
        this.idDiscurso = idDiscurso;
    }
}
