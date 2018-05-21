package com.qi0.weslley.gerenciadordediscursos.fragments;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.qi0.weslley.gerenciadordediscursos.Config.ConfiguracaoFirebase;
import com.qi0.weslley.gerenciadordediscursos.R;
import com.qi0.weslley.gerenciadordediscursos.adapter.AgendaAdapter;
import com.qi0.weslley.gerenciadordediscursos.adapter.CongregacaoAdapter;
import com.qi0.weslley.gerenciadordediscursos.adapter.DiscursoAdapter;
import com.qi0.weslley.gerenciadordediscursos.adapter.OradorAdaper;
import com.qi0.weslley.gerenciadordediscursos.helper.RecyclerItemClickListener;
import com.qi0.weslley.gerenciadordediscursos.model.Agenda;
import com.qi0.weslley.gerenciadordediscursos.model.Congregacao;
import com.qi0.weslley.gerenciadordediscursos.model.Discurso;
import com.qi0.weslley.gerenciadordediscursos.model.Orador;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessesFragment extends BaseFragment {


    int mes = 0;
    int ano = 2018;

    Agenda agendaSelecionada;
    List<Agenda> agendaList = new ArrayList();
    List<Discurso> discursosList = new ArrayList();
    List<Congregacao> congregacaoList = new ArrayList();
    List<Orador> oradoresList = new ArrayList();
    AgendaAdapter agendaAdapter;
    DiscursoAdapter discursoAdapter;
    CongregacaoAdapter congregacaoAdapter;
    OradorAdaper oradorAdaper;

    String idCongregacaoEscolhida;
    String idOradorEscolhido;
    String idDiscursoEscolhido;

    Congregacao congregacaoEscolhida;
    Orador oradorEscolhido;
    Discurso discursoEscolhido;

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    ValueEventListener valueEventListenerAgenda;
    String userUID;

    EditText edtCongregacao;
    EditText edtOrador;
    EditText edtDiscurso;


    public MessesFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messes, container, false);

        databaseReference = ConfiguracaoFirebase.getFirebaseDatabase();
        firebaseAuth = ConfiguracaoFirebase.getAuth();
        userUID = firebaseAuth.getCurrentUser().getUid();

        setHasOptionsMenu(true);

        if (getArguments() != null) {
            this.mes = getArguments().getInt("mes");
        }

        recyclerView = view.findViewById(R.id.recycle_view_meses_fragments);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        atualizarView();

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(
                getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                agendaSelecionada = agendaList.get(position);
                dialogoEdtitarAgenda();
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        }));

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        databaseReference.removeEventListener(valueEventListenerAgenda);
    }

    private void atualizarView() {

        congregacaoList = Congregacao.pegarCongegacoesDoBanco(userUID);
        oradoresList = Orador.pegarOradoresDoBanco(userUID);
        discursosList = Discurso.pegarDiscursosDoBanco(userUID);

        pegarDatasDaAgendaNoBanco(mes, ano);
        agendaAdapter = new AgendaAdapter(agendaList, congregacaoList, oradoresList, discursosList, getContext());
        //agendaAdapter = new AgendaAdapter(agendaList, getContext());
        recyclerView.setAdapter(agendaAdapter);
    }

    @SuppressLint("DefaultLocale")
    public void pegarDatasDaAgendaNoBanco(int mes, int ano) {

        String mesFormatado = String.format("%02d", mes + 1);

        valueEventListenerAgenda = databaseReference
                .child("user_data")
                .child(userUID)
                .child("agenda")
                .child(String.valueOf(ano))
                .child(mesFormatado)
                .addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        agendaList.clear();
                        for (DataSnapshot dados : dataSnapshot.getChildren()) {
                            Agenda agenda = dados.getValue(Agenda.class);
                            agendaList.add(agenda);
                        }
                        agendaAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

    private void dialogoEdtitarAgenda() {
        pegarNomeCongregacoesDoBanco();
        pegarNomeOradorDoBanco();
        pegarTemaDiscursoDoBanco();

        SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
        Date data = null;
        try {
            data = formato.parse(agendaSelecionada.getData());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat dataFormatada = new SimpleDateFormat("dd/MM/yyyy");
        String dataSelecionada = dataFormatada.format(data);

        AlertDialog.Builder dialogo = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogoView = inflater.inflate(R.layout.dialog_add_editar_agenda, null);
        dialogo.setView(dialogoView);
        dialogo.setCancelable(false);
        dialogo.setTitle(dataSelecionada);

        edtCongregacao = dialogoView.findViewById(R.id.edt_dialog_agenda_congregacao);
        edtOrador = dialogoView.findViewById(R.id.edt_dialog_agenda_orador);
        edtDiscurso = dialogoView.findViewById(R.id.edt_dialog_agenda_discurso);

        if (agendaSelecionada != null) {
            //edtCongregacao.setText(nomeCong);
            //edtOrador.setText(agendaSelecionada.getIdCongregacao());
            //edtDiscurso.setText(agendaSelecionada.getIdCongregacao());
        }

        edtCongregacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogoEscolherCongregacao();
                Toast.makeText(getContext(), "CCCCCCC", Toast.LENGTH_SHORT).show();
            }
        });

        edtOrador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogoEscolherOrador();
                Toast.makeText(getContext(), "OOOOOO", Toast.LENGTH_SHORT).show();
            }
        });

        edtDiscurso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogoEscolherDiscurso();
                Toast.makeText(getContext(), "DDDDDDD", Toast.LENGTH_SHORT).show();
            }
        });

        dialogo.setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Salvando Agenda", Toast.LENGTH_SHORT).show();
                salvarAgenda();
                dialog.dismiss();

            }
        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        final AlertDialog alertDialog = dialogo.create();

        alertDialog.show();

        //alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
    }

    private void dialogoEscolherCongregacao() {

        congregacaoList = Congregacao.pegarCongegacoesDoBanco(userUID);
        oradoresList = Orador.pegarOradoresDoBanco(userUID);

        AlertDialog.Builder dialogo = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogoView = inflater.inflate(R.layout.dialog_congregacao_list, null);
        dialogo.setView(dialogoView);
        dialogo.setCancelable(false);
        dialogo.setTitle("Escolha uma Congregação");

        RecyclerView recyclerViewDialogo = dialogoView.findViewById(R.id.lista_congregacao_dialogo_agenda);
        LinearLayoutManager layoutManagerDialogo = new LinearLayoutManager(getActivity());
        recyclerViewDialogo.setLayoutManager(layoutManagerDialogo);
        //recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerViewDialogo.setHasFixedSize(true);

        congregacaoAdapter = new CongregacaoAdapter(congregacaoList, oradoresList, getContext());

        recyclerViewDialogo.setAdapter(congregacaoAdapter);

        dialogo.setPositiveButton("Add Nova Congregação", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Adicionado", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });;


        final AlertDialog alertDialog = dialogo.create();

        recyclerViewDialogo.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerViewDialogo, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                congregacaoEscolhida = congregacaoList.get(position);
                idCongregacaoEscolhida = congregacaoEscolhida.getIdCongregacao();
                edtCongregacao.setText(congregacaoEscolhida.getNomeCongregacao());
                alertDialog.dismiss();
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        }));

        alertDialog.show();
    }

    private void dialogoEscolherOrador() {

        oradoresList = Orador.pegarOradoresDoBanco(userUID);

        AlertDialog.Builder dialogo = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogoView = inflater.inflate(R.layout.dialog_oradores_list, null);
        dialogo.setView(dialogoView);
        dialogo.setCancelable(false);
        dialogo.setTitle("Escolha um Orador");

        RecyclerView recyclerViewDialogo = dialogoView.findViewById(R.id.lista_oradores_dialogo_agenda);
        LinearLayoutManager layoutManagerDialogo = new LinearLayoutManager(getActivity());
        recyclerViewDialogo.setLayoutManager(layoutManagerDialogo);
        //recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerViewDialogo.setHasFixedSize(true);

        oradorAdaper = new OradorAdaper(oradoresList, getContext());

        recyclerViewDialogo.setAdapter(oradorAdaper);

        dialogo.setPositiveButton("Add Novo Orador", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Adicionado", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });


        final AlertDialog alertDialog = dialogo.create();

        recyclerViewDialogo.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerViewDialogo, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                oradorEscolhido = oradoresList.get(position);
                idOradorEscolhido = oradorEscolhido.getId();
                edtOrador.setText(oradorEscolhido.getNome());
                alertDialog.dismiss();
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        }));

        alertDialog.show();
    }

    private void dialogoEscolherDiscurso() {

        discursosList = Discurso.pegarDiscursosDoBanco(userUID);

        AlertDialog.Builder dialogo = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogoView = inflater.inflate(R.layout.dialog_discurso_list, null);
        dialogo.setView(dialogoView);
        dialogo.setCancelable(false);
        dialogo.setTitle("Escolha um Discurso");

        RecyclerView recyclerViewDialogo = dialogoView.findViewById(R.id.lista_discursos_dialogo_agenda);
        LinearLayoutManager layoutManagerDialogo = new LinearLayoutManager(getActivity());
        recyclerViewDialogo.setLayoutManager(layoutManagerDialogo);
        //recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerViewDialogo.setHasFixedSize(true);

        discursoAdapter = new DiscursoAdapter(discursosList, getContext());

        recyclerViewDialogo.setAdapter(discursoAdapter);

        dialogo.setPositiveButton("Add Novo Discurso", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Adicionado", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });;


        final AlertDialog alertDialog = dialogo.create();

        recyclerViewDialogo.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerViewDialogo, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                discursoEscolhido = discursosList.get(position);
                idDiscursoEscolhido = discursoEscolhido.getIdDiscurso();
                edtDiscurso.setText(discursoEscolhido.getTema());
                alertDialog.dismiss();
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        }));

        alertDialog.show();
    }

    private void salvarAgenda() {

        SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
        Date data = null;
        try {
            data = formato.parse(agendaSelecionada.getData());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat dataSDF = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat dia = new SimpleDateFormat("dd");
        SimpleDateFormat mes = new SimpleDateFormat("MM");
        SimpleDateFormat ano = new SimpleDateFormat("yyyy");

        String dataFormatada = dataSDF.format(data);
        String mesFormatado = mes.format(data);
        String anoFormatado = ano.format(data);

        Agenda agenda = new Agenda();
        agenda.setData(dataFormatada);
        agenda.setIdCongregacao(idCongregacaoEscolhida);
        agenda.setIdOrador(idOradorEscolhido);
        agenda.setIdDiscurso(idDiscursoEscolhido);

        databaseReference.child("user_data")
                .child(userUID)
                .child("agenda")
                .child(anoFormatado)
                .child(mesFormatado)
                .child(dataFormatada)
                .setValue(agenda);

    }

    private void pegarNomeCongregacoesDoBanco() {

        valueEventListenerAgenda = databaseReference
                .child("user_data")
                .child(userUID)
                .child("congregacoes")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot dados : dataSnapshot.getChildren()){
                            Congregacao congregacao = dados.getValue(Congregacao.class);
                            if (congregacao.getIdCongregacao().equals(agendaSelecionada.getIdCongregacao())) {
                                congregacaoEscolhida = congregacao;
                                idCongregacaoEscolhida = congregacao.getIdCongregacao();
                                String nomeCong = congregacao.getNomeCongregacao();
                                edtCongregacao.setText(nomeCong);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void pegarNomeOradorDoBanco() {

        valueEventListenerAgenda = databaseReference
                .child("user_data")
                .child(userUID)
                .child("oradores")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot dados : dataSnapshot.getChildren()){
                            Orador orador = dados.getValue(Orador.class);
                            if (orador.getId().equals(agendaSelecionada.getIdOrador())) {
                                oradorEscolhido = orador;
                                idOradorEscolhido = orador.getId();
                                String nomeOrador = orador.getNome();
                                edtOrador.setText(nomeOrador);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void pegarTemaDiscursoDoBanco() {

        valueEventListenerAgenda = databaseReference
                .child("user_data")
                .child(userUID)
                .child("discursos")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot dados : dataSnapshot.getChildren()){
                            Discurso discurso = dados.getValue(Discurso.class);
                            if (discurso.getIdDiscurso().equals(agendaSelecionada.getIdDiscurso())) {
                                discursoEscolhido = discurso;
                                idDiscursoEscolhido = discurso.getIdDiscurso();
                                String temaDiscurso = discurso.getTema();
                                edtDiscurso.setText(temaDiscurso);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem item = menu.findItem(R.id.spinner);
        Spinner spinner = (Spinner) item.getActionView();

        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.spinner_list_item_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String a = String.valueOf(parent.getItemAtPosition(position));
                ano = Integer.parseInt(a);
                datas.clear();

                atualizarView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }*/
}
