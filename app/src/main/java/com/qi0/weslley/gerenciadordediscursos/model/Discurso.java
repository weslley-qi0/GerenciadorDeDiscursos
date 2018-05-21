package com.qi0.weslley.gerenciadordediscursos.model;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.qi0.weslley.gerenciadordediscursos.Config.ConfiguracaoFirebase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    public static List<Discurso> pegarDiscursosDoBanco(String userUID){

        final List<Discurso> discursosList = new ArrayList<>();

        DatabaseReference databaseReference;
        databaseReference = ConfiguracaoFirebase.getFirebaseDatabase();

        databaseReference.child("user_data").child(userUID).child("discursos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                discursosList.clear();
                for (DataSnapshot dados : dataSnapshot.getChildren()){
                    Discurso discurso = dados.getValue(Discurso.class);
                    discursosList.add(discurso);
                    Collections.sort(discursosList);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return discursosList;
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
