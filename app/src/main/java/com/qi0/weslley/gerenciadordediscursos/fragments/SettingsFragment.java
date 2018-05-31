package com.qi0.weslley.gerenciadordediscursos.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.qi0.weslley.gerenciadordediscursos.config.ConfiguracaoFirebase;
import com.qi0.weslley.gerenciadordediscursos.R;
import com.qi0.weslley.gerenciadordediscursos.activitys.LoginActivity;
import com.qi0.weslley.gerenciadordediscursos.model.Agenda;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends BaseFragment {

    FirebaseAuth firebaseAuth;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        setHasOptionsMenu(true);
        firebaseAuth = FirebaseAuth.getInstance();
        String userEmail = firebaseAuth.getCurrentUser().getEmail();

        Button btDeslogar = view.findViewById(R.id.bt_deslogar);
        btDeslogar.setText("Deslogar de " + userEmail);
        btDeslogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        });

        Button criarAgenda = view.findViewById(R.id.bt_criar_agenda);
        criarAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                criarAgenda();
            }
        });


        return view;
    }

    private void criarAgenda() {
        Calendar calendar = Calendar.getInstance();
        int ano = calendar.get(Calendar.YEAR);
        for (int i = 0; i <= 11; i++) {
            criarAgendaNoBanco(i, ano);
        }
    }

    public void criarAgendaNoBanco(int mes, int ano) {
        FirebaseAuth firebaseAuth;
        DatabaseReference databaseReference;

        firebaseAuth = ConfiguracaoFirebase.getAuth();
        databaseReference = ConfiguracaoFirebase.getFirebaseDatabase();
        String userUID = firebaseAuth.getCurrentUser().getUid();

        Agenda agenda = new Agenda();

        // cria um calendário na data 01/mes/ano
        Calendar c = new GregorianCalendar(ano, mes, 1);
        // Pega a Data e formata de Acordo com a Região Ex: 00/00/00
        //DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);

        do {
            // o dia da semana ecolhido é domingo?
            if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                SimpleDateFormat dataSDF = new SimpleDateFormat("dd-MM-yyyy");
                SimpleDateFormat mesSDF = new SimpleDateFormat("MM");
                String dataFormatada = dataSDF.format(c.getTime());
                String mesFormatado = mesSDF.format(c.getTime());

                agenda.setData(dataFormatada);
                agenda.setIdCongregacao("");
                agenda.setIdOrador("");
                agenda.setIdDiscurso("");

                databaseReference.child("user_data")
                        .child(userUID)
                        .child("agenda")
                        .child(String.valueOf(ano))
                        .child(mesFormatado)
                        .child(dataFormatada)
                        .setValue(agenda);

            }
            // incrementa um dia no calendário
            c.roll(Calendar.DAY_OF_MONTH, true);

            // enquanto o dia do mês atual for diferente de 1
        } while (c.get(Calendar.DAY_OF_MONTH) != 1);
    }

}
