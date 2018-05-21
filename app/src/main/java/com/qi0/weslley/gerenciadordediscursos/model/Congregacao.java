package com.qi0.weslley.gerenciadordediscursos.model;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.qi0.weslley.gerenciadordediscursos.Config.ConfiguracaoFirebase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Congregacao implements Serializable, Comparable<Congregacao> {

    private String idCongregacao;
    private String nomeCongregacao;
    private String cidadeCongregação;
    private int quantOradores;


    public Congregacao() { }

    public Congregacao(String idCongregacao, String nomeCongregacao, String cidadeCongregação, int quantOradores) {
        this.idCongregacao = idCongregacao;
        this.nomeCongregacao = nomeCongregacao;
        this.cidadeCongregação = cidadeCongregação;
        this.quantOradores = quantOradores;
    }

    public static List<Congregacao> pegarCongegacoesDoBanco(String userUID){
        final List<Congregacao> congregacoesList = new ArrayList<>();

        DatabaseReference databaseReference;
        databaseReference = ConfiguracaoFirebase.getFirebaseDatabase();

        databaseReference.child("user_data").child(userUID).child("congregacoes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                congregacoesList.clear();
                for (DataSnapshot dados : dataSnapshot.getChildren()){
                    Congregacao congregacao = dados.getValue(Congregacao.class);
                    congregacoesList.add(congregacao);
                    Collections.sort(congregacoesList);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return congregacoesList;
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
