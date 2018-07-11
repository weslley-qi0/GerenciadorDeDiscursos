package com.qi0.weslley.gerenciadordediscursos.model;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.qi0.weslley.gerenciadordediscursos.config.ConfiguracaoFirebase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Orador implements Serializable, Comparable<Orador>{

    private String id;
    private String nome;
    private String idCongregacao;
    private String telefone;
    private String email;
    private String ultimaVisita;
    private String urlFotoOrador;
    public Map<String, Object> proferimentos = new HashMap<>();
    private float ratingOrador;

    public Orador() {
    }

    public Orador(String id, String nome, String idCongregacao, String telefone, String email, String ultimaVisita, String urlFotoOrador, Map<String, Object> proferimentos, float ratingOrador) {
        this.id = id;
        this.nome = nome;
        this.idCongregacao = idCongregacao;
        this.telefone = telefone;
        this.email = email;
        this.ultimaVisita = ultimaVisita;
        this.urlFotoOrador = urlFotoOrador;
        this.proferimentos = proferimentos;
        this.ratingOrador = ratingOrador;
    }

    // Pega Todos os oradores e seta no adapter para poder pegar a quantidade
    public static List<Orador> pegarOradoresDoBanco(String userUID){

        final List<Orador> oradoresList = new ArrayList<>();
        DatabaseReference databaseReference;
        databaseReference = ConfiguracaoFirebase.getFirebaseDatabase();

        databaseReference.child("user_data").child(userUID).child("oradores").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                oradoresList.clear();
                for (DataSnapshot dados : dataSnapshot.getChildren()){
                    Orador orador = dados.getValue(Orador.class);
                    oradoresList.add(orador);
                    Collections.sort(oradoresList);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return oradoresList;
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

    public String getIdCongregacao() {
        return idCongregacao;
    }

    public void setIdCongregacao(String idCongregacao) {
        this.idCongregacao = idCongregacao;
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

    public Map<String, Object> getProferimentos() {
        return proferimentos;
    }

    public void setProferimentos(Map<String, Object> proferimentos) {
        this.proferimentos = proferimentos;
    }

    public float getRatingOrador() {
        return ratingOrador;
    }

    public void setRatingOrador(float ratingOrador) {
        this.ratingOrador = ratingOrador;
    }

    @Override
    public int compareTo(@NonNull Orador o) {
        return this.getNome().compareTo(o.getNome());
    }
}
