package com.qi0.weslley.gerenciadordediscursos.model;

public class Discurso {

    private String numero;
    private String tema;
    private String ultimoProferimento;


    public Discurso() {
    }

    public Discurso(String numero, String tema, String ultimoProferimento) {
        this.numero = numero;
        this.tema = tema;
        this.ultimoProferimento = ultimoProferimento;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public String getUltimoProferimento() {
        return ultimoProferimento;
    }

    public void setUltimoProferimento(String ultimoProferimento) {
        this.ultimoProferimento = ultimoProferimento;
    }
}
