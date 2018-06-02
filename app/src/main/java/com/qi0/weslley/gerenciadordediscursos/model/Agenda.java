package com.qi0.weslley.gerenciadordediscursos.model;

public class Agenda {

    private String data;
    private String idCongregacao;
    private String idOrador;
    private String idDiscurso;
    private String idProferimento;

    public Agenda() {
    }

    public Agenda(String data, String idCongregacao, String idOrador, String idDiscurso, String idProferimento) {
        this.data = data;
        this.idCongregacao = idCongregacao;
        this.idOrador = idOrador;
        this.idDiscurso = idDiscurso;
        this.idProferimento = idProferimento;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
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

    public String getIdProferimento() {
        return idProferimento;
    }

    public void setIdProferimento(String idProferimento) {
        this.idProferimento = idProferimento;
    }
}
