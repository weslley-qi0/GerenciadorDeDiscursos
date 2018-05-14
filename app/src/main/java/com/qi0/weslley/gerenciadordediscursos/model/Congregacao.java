package com.qi0.weslley.gerenciadordediscursos.model;

import java.io.Serializable;

public class Congregacao implements Serializable {

    private String nomeCongregacao;
    private String cidadeCongregação;
    private int quantOradores;


    public Congregacao() {
    }

    public Congregacao(String nomeCongregacao, String cidadeCongregação, int quantOradores) {
        this.nomeCongregacao = nomeCongregacao;
        this.cidadeCongregação = cidadeCongregação;
        this.quantOradores = quantOradores;
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
    public String toString() {
        return nomeCongregacao;
    }
}
