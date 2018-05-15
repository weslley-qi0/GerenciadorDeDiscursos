package com.qi0.weslley.gerenciadordediscursos.model;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class Congregacao implements Serializable, Comparable<Congregacao> {

    private String idCongregacao;
    private String nomeCongregacao;
    private String cidadeCongregação;
    private int quantOradores;


    public Congregacao() {
    }

    public Congregacao(String idCongregacao, String nomeCongregacao, String cidadeCongregação, int quantOradores) {
        this.idCongregacao = idCongregacao;
        this.nomeCongregacao = nomeCongregacao;
        this.cidadeCongregação = cidadeCongregação;
        this.quantOradores = quantOradores;
    }

    public String getIdCongregacao() {
        return idCongregacao;
    }

    public void setIdCongregacao(String idCongregacao) {
        this.idCongregacao = idCongregacao;
    }

    public String getNomeCongregacao() {
        return nomeCongregacao;
    }

    public void setNomeCongregacao(String nomeCongregacao) {
        this.nomeCongregacao = nomeCongregacao;
    }

    public String getCidadeCongregação() {
        return cidadeCongregação;
    }

    public void setCidadeCongregação(String cidadeCongregação) {
        this.cidadeCongregação = cidadeCongregação;
    }

    public int getQuantOradores() {
        return quantOradores;
    }

    public void setQuantOradores(int quantOradores) {
        this.quantOradores = quantOradores;
    }

    @Override
    public int compareTo(@NonNull Congregacao o) {
        return this.getNomeCongregacao().compareTo(o.getNomeCongregacao());
    }
}
