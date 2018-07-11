package com.qi0.weslley.gerenciadordediscursos.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.qi0.weslley.gerenciadordediscursos.R;
import com.qi0.weslley.gerenciadordediscursos.config.ConfiguracaoFirebase;
import com.qi0.weslley.gerenciadordediscursos.fragments.DetalheDiscursoFragment;
import com.qi0.weslley.gerenciadordediscursos.fragments.DetalheOradorFragment;
import com.qi0.weslley.gerenciadordediscursos.fragments.ProferimentosDetalheFragment;
import com.qi0.weslley.gerenciadordediscursos.model.Discurso;
import com.qi0.weslley.gerenciadordediscursos.model.Orador;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DetalheActivity extends BaseActivity {

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    ValueEventListener valueEventListenerAgenda;
    String userUID;

    List<String> anosList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe);

        String fragmentAbrir = (String) getIntent().getCharSequenceExtra("qualFragmentAbrir");

        Intent intent = getIntent();
        Orador oradorSelecionado = (Orador) intent.getSerializableExtra("orador");
        Discurso discursoSelecionado = (Discurso) intent.getSerializableExtra("discursoSelecionado");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_detalhe);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        switch (fragmentAbrir) {
            case "DetalheCongregacaoFragment":
                replaceFragment(R.id.fragment_container_detalhe, new Fragment());
                break;
            case "DetalheOradorFragment":
                Bundle bundleOrador = new Bundle();
                bundleOrador.putSerializable("oradorSelecionado", oradorSelecionado);
                DetalheOradorFragment detalheOradorFragment = new DetalheOradorFragment();
                detalheOradorFragment.setArguments(bundleOrador);
                replaceFragment(R.id.fragment_container_detalhe, detalheOradorFragment);
                break;
            case "DetalheDiscursoFragment":
                Bundle bundleDiscurso = new Bundle();
                bundleDiscurso.putSerializable("discursoSelecionado", discursoSelecionado);
                DetalheDiscursoFragment detalheDiscursoFragment = new DetalheDiscursoFragment();
                detalheDiscursoFragment.setArguments(bundleDiscurso);
                replaceFragment(R.id.fragment_container_detalhe, detalheDiscursoFragment);
                break;
            case "listaDeAnosAgenda":
                pegarValoresDaAgendaNoBanco();
                fab.setVisibility(View.INVISIBLE);
                break;
        }
    }

    public void pegarValoresDaAgendaNoBanco() {

        databaseReference = ConfiguracaoFirebase.getFirebaseDatabase();
        firebaseAuth = ConfiguracaoFirebase.getAuth();
        userUID = firebaseAuth.getCurrentUser().getUid();

        valueEventListenerAgenda = databaseReference
                .child("user_data")
                .child(userUID)
                .child("agenda")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        anosList.clear();
                        for (DataSnapshot dados : dataSnapshot.getChildren()) {
                            String anoDataBase = dados.getKey();
                            anosList.add(anoDataBase);
                        }
                        Collections.sort(anosList);
                        dialogoListaDeAnos();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void dialogoListaDeAnos() {

        final String[] arrayAnosString = new String[anosList.size()];
        for (int i = 0; i < anosList.size(); i++) {
            arrayAnosString[i] = anosList.get(i);
        }
        final AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
        alertbox.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
                finish();
            }
        });
        alertbox.setTitle("Selecione um Ano para Excluir Agenda")
                .setItems(arrayAnosString, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String anoSelecionado = String.valueOf(arrayAnosString[(which)]);
                        dialogConfimarExcluirAgenda(anoSelecionado);
                        //dialog.dismiss();
                    }
                });

        if (anosList == null || anosList.size() <= 0) {
            alertbox.setTitle("Não há Agendas Criadas!");
        }
        AlertDialog dialog = alertbox.create();
        if(!(isFinishing()))  {
            dialog.show();
        }
    }

    private void dialogConfimarExcluirAgenda(final String anoSelecionado) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Deletar Agenda de " + anoSelecionado);
        builder.setCancelable(false);
        builder.setMessage("Deseja realmente excluir o ano de " + anoSelecionado
                + " da Agenda? Isso apagará todos os dados deste Ano e você não poderá mais Recupera-los.");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                excluirAgenda(anoSelecionado);
                dialog.dismiss();
            }
        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void excluirAgenda(String anoSelecionado) {

        databaseReference = ConfiguracaoFirebase.getFirebaseDatabase();
        firebaseAuth = ConfiguracaoFirebase.getAuth();
        userUID = firebaseAuth.getCurrentUser().getUid();

        databaseReference
                .child("user_data")
                .child(userUID)
                .child("agenda")
                .child(anoSelecionado)
                .removeValue();
        finish();
    }

}
