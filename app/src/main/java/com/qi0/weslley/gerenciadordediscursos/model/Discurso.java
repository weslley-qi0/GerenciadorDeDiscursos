package com.qi0.weslley.gerenciadordediscursos.model;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class Discurso implements Serializable, Comparable<Discurso>{

    private String idDiscurso;
    private String numero;
    private String tema;
    private String ultimoProferimento;


    public Discurso() {
    }

    public Discurso(String idDiscurso, String numero, String tema, String ultimoProferimento) {
        this.idDiscurso = idDiscurso;
        this.numero = numero;
        this.tema = tema;
        this.ultimoProferimento = ultimoProferimento;
    }

    public String getIdDiscurso() {
        return idDiscurso;
    }

    public void setIdDiscurso(String idDiscurso) {
        this.idDiscurso = idDiscurso;
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

    @Override
    public int compareTo(@NonNull Discurso o) {
        return this.getNumero().compareTo(o.getNumero());
    }
}
