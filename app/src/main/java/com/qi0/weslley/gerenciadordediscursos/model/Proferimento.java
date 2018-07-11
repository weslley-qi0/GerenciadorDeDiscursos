package com.qi0.weslley.gerenciadordediscursos.model;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.qi0.weslley.gerenciadordediscursos.config.ConfiguracaoFirebase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Proferimento implements Serializable, Comparable<Proferimento>{

    private String idProferimentos;
    private String dataProferimento;
    private String dataOrdenarProferimento;
    private String idOradorProferimento;
    private String idDiscursoProferimento;
    private String idCongregacaoProferimento;

    public Proferimento() {
    }

    public Proferimento(String idProferimentos, String dataProferimento, String dataOrdenarProferimento, String idOradorProferimento, String idDiscursoProferimento, String idCongregacaoProferimento) {
        this.idProferimentos = idProferimentos;
        this.dataProferimento = dataProferimento;
        this.dataOrdenarProferimento = dataOrdenarProferimento;
        this.idOradorProferimento = idOradorProferimento;
        this.idDiscursoProferimento = idDiscursoProferimento;
        this.idCongregacaoProferimento = idCongregacaoProferimento;
    }

    public static List<Proferimento> pegarProferimentosDoBanco(String userUID){

        final List<Proferimento> proferimentoList = new ArrayList<>();

        DatabaseReference databaseReference;
        databaseReference = ConfiguracaoFirebase.getFirebaseDatabase();
        databaseReference.child("user_data")
                .child(userUID)
                .child("proferimentos")
                .addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                proferimentoList.clear();

                for (DataSnapshot dados : dataSnapshot.getChildren()){
                    Proferimento proferimento = dados.getValue(Proferimento.class);
                    proferimentoList.add(proferimento);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return proferimentoList;
    }

    public String getIdProferimentos() {
        return idProferimentos;
    }

    public void setIdProferimentos(String idProferimentos) {
        this.idProferimentos = idProferimentos;
    }

    public String getDataProferimento() {
        return dataProferimento;
    }

    public void setDataProferimento(String dataProferimento) {
        this.dataProferimento = dataProferimento;
    }

    public String getDataOrdenarProferimento() {
        return dataOrdenarProferimento;
    }

    public void setDataOrdenarProferimento(String dataOrdenarProferimento) {
        this.dataOrdenarProferimento = dataOrdenarProferimento;
    }

    public String getIdOradorProferimento() {
        return idOradorProferimento;
    }

    public void setIdOradorProferimento(String idOradorProferimento) {
        this.idOradorProferimento = idOradorProferimento;
    }

    public String getIdDiscursoProferimento() {
        return idDiscursoProferimento;
    }

    public void setIdDiscursoProferimento(String idDiscursoProferimento) {
        this.idDiscursoProferimento = idDiscursoProferimento;
    }

    public String getIdCongregacaoProferimento() {
        return idCongregacaoProferimento;
    }

    public void setIdCongregacaoProferimento(String idCongregacaoProferimento) {
        this.idCongregacaoProferimento = idCongregacaoProferimento;
    }

    @Override
    public int compareTo(@NonNull Proferimento o) {
        return this.getDataProferimento().compareTo(o.getDataProferimento());
    }
}
