package com.qi0.weslley.gerenciadordediscursos.fragments;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import com.qi0.weslley.gerenciadordediscursos.activitys.AdicionarEditarActivity;
import com.qi0.weslley.gerenciadordediscursos.config.ConfiguracaoFirebase;
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
import com.qi0.weslley.gerenciadordediscursos.model.Proferimento;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessesFragment extends BaseFragment {

    int mes = 0;
    int ano = 2018;

    EditText edtNomeCongregacao;
    EditText edtCidadeCongregacao;
    EditText edtNumeroDiscurso;
    EditText edtTemaDiscurso;
    String idCongregacao;
    String nomeCongregacao;
    String cidadeCongregacao;
    String numeroDiscurso;
    String temaDiscurso;
    String idProferimento;

    Agenda agendaSelecionada;
    List<Agenda> agendaList = new ArrayList();
    List<Discurso> discursosList = new ArrayList();
    List<Congregacao> congregacaoList;
    List<Orador> oradoresList = new ArrayList();
    List<Proferimento> proferimentoList = new ArrayList<>();
    List<String> idProferimentoList = new ArrayList<>();
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

    public MessesFragment() {
    }

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
                agendaSelecionada = agendaList.get(position);
                idProferimento = agendaSelecionada.getIdProferimento();
                idOradorEscolhido = agendaSelecionada.getIdOrador();
                showPopup(view);
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
        proferimentoList = Proferimento.pegarProferimentosDoBanco(userUID);

        pegarValoresDaAgendaNoBanco(mes, ano);
        agendaAdapter = new AgendaAdapter(agendaList, congregacaoList, oradoresList, discursosList, getContext());
        recyclerView.setAdapter(agendaAdapter);
    }

    @SuppressLint("DefaultLocale")
    public void pegarValoresDaAgendaNoBanco(int mes, int ano) {

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

        idProferimento = null;

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
            pegarValoresAgendaSelecionada();
            listarOradoesPorCongregacao(idCongregacaoEscolhida);
        }

        edtCongregacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogoEscolherCongregacao();
            }
        });

        edtOrador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogoEscolherOrador();
            }
        });

        edtDiscurso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogoEscolherDiscurso();
            }
        });

        dialogo.setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (idCongregacaoEscolhida == null && idOradorEscolhido == null && idDiscursoEscolhido == null) {
                } else {
                    salvarAgenda();
                }
                dialog.dismiss();

            }
        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                idCongregacaoEscolhida = null;
                idOradorEscolhido = null;
                idDiscursoEscolhido = null;
                idProferimento = null;
                dialog.dismiss();
            }
        });


        final AlertDialog alertDialog = dialogo.create();

        alertDialog.show();

        oradoresList = Orador.pegarOradoresDoBanco(userUID);
        //alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
    }

    private void dialogoEscolherCongregacao() {

        AlertDialog.Builder dialogo = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogoView = inflater.inflate(R.layout.dialog_congregacao_list, null);
        dialogo.setView(dialogoView);
        dialogo.setCancelable(false);
        dialogo.setTitle("Escolha uma Congregação");

        RecyclerView recyclerViewDialogo = dialogoView.findViewById(R.id.lista_congregacao_dialogo_agenda);
        LinearLayoutManager layoutManagerDialogo = new LinearLayoutManager(getActivity());
        recyclerViewDialogo.setLayoutManager(layoutManagerDialogo);
        recyclerViewDialogo.setHasFixedSize(true);

        congregacaoAdapter = new CongregacaoAdapter(congregacaoList, oradoresList, getContext());

        recyclerViewDialogo.setAdapter(congregacaoAdapter);

        dialogo.setPositiveButton("Add Nova Congr.", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialogoAddNovaCongregacao();
                //AdicionarEditarActivity.dialogoAddCongregacaoStatic(getContext(), getActivity());
                dialog.dismiss();
            }
        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                oradoresList = Orador.pegarOradoresDoBanco(userUID);
                dialog.dismiss();
            }
        });

        final AlertDialog alertDialog = dialogo.create();

        recyclerViewDialogo.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerViewDialogo, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                congregacaoEscolhida = congregacaoList.get(position);
                idCongregacaoEscolhida = congregacaoEscolhida.getIdCongregacao();
                edtCongregacao.setText(congregacaoEscolhida.getNomeCongregacao());
                alertDialog.dismiss();
                listarOradoesPorCongregacao(idCongregacaoEscolhida);
                dialogoEscolherOrador();
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        }));

        alertDialog.show();
        oradoresList = Orador.pegarOradoresDoBanco(userUID);
    }

    private void dialogoEscolherOrador() {

        AlertDialog.Builder dialogo = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogoView = inflater.inflate(R.layout.dialog_oradores_list, null);
        dialogo.setView(dialogoView);
        dialogo.setCancelable(false);
        dialogo.setTitle("Escolha um Orador");

        RecyclerView recyclerViewDialogo = dialogoView.findViewById(R.id.lista_oradores_dialogo_agenda);
        LinearLayoutManager layoutManagerDialogo = new LinearLayoutManager(getActivity());
        recyclerViewDialogo.setLayoutManager(layoutManagerDialogo);
        recyclerViewDialogo.setHasFixedSize(true);

        if (oradoresList.size() <= 0){
            dialogo.setTitle("Não há Oradores Cadastrados Nessa Congregação!");
        }

        oradorAdaper = new OradorAdaper(oradoresList, congregacaoList, getContext());

        recyclerViewDialogo.setAdapter(oradorAdaper);

        dialogo.setPositiveButton("Add Novo Orador", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getActivity(), AdicionarEditarActivity.class);
                intent.putExtra("qualFragmentAbrir", "AddOradorFragment");
                startActivityForResult(intent, 1);
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
                pegaNomeDaCongregacao(oradorEscolhido.getIdCongregacao());

                if (oradorEscolhido.getProferimentos() != null){
                    //proferimentoList = Orador.pegarProferimentos(userUID, idOradorEscolhido);
                }
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

        AlertDialog.Builder dialogo = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogoView = inflater.inflate(R.layout.dialog_discurso_list, null);
        dialogo.setView(dialogoView);
        dialogo.setCancelable(false);
        dialogo.setTitle("Escolha um Discurso");

        RecyclerView recyclerViewDialogo = dialogoView.findViewById(R.id.lista_discursos_dialogo_agenda);
        LinearLayoutManager layoutManagerDialogo = new LinearLayoutManager(getActivity());
        recyclerViewDialogo.setLayoutManager(layoutManagerDialogo);
        recyclerViewDialogo.setHasFixedSize(true);

        discursoAdapter = new DiscursoAdapter(discursosList, getContext());

        recyclerViewDialogo.setAdapter(discursoAdapter);

        dialogo.setPositiveButton("Add Novo Discurso", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialogoAddNovoDiscurso();
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

    public void dialogoAddNovaCongregacao() {

        final AlertDialog.Builder dialogo = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogoView = inflater.inflate(R.layout.dialog_add_editar_congregacao, null);
        dialogo.setView(dialogoView);
        dialogo.setCancelable(false);
        dialogo.setTitle("Adicionar Congregação");

        edtNomeCongregacao = dialogoView.findViewById(R.id.edt_dialog_add_nome_congregação);
        edtCidadeCongregacao = dialogoView.findViewById(R.id.edt_dialog_add_cidade_congregação);

        dialogo.setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                nomeCongregacao = edtNomeCongregacao.getText().toString().trim();
                cidadeCongregacao = edtCidadeCongregacao.getText().toString().trim();
                salvarCongregacaoNova();
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

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

        edtCidadeCongregacao.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() >= 1) {
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                } else {
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

                }

            }
        });
        edtNomeCongregacao.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() >= 1) {
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                } else {
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

                }

            }
        });
    }

    private void dialogoAddNovoDiscurso() {

        AlertDialog.Builder dialogo = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogoView = inflater.inflate(R.layout.dialog_add_editar_discurso, null);
        dialogo.setView(dialogoView);
        dialogo.setCancelable(false);
        dialogo.setTitle("Adicionar Discurso");

        edtNumeroDiscurso = dialogoView.findViewById(R.id.edt_dialog_add_numero_discurso);
        edtTemaDiscurso = dialogoView.findViewById(R.id.edt_dialog_add_tema_discurso);

        dialogo.setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                numeroDiscurso = edtNumeroDiscurso.getText().toString().trim();
                temaDiscurso = edtTemaDiscurso.getText().toString().trim();
                idDiscursoEscolhido = databaseReference.child("user_data").child(userUID).child("discursos").push().getKey();
                salvarDiscursoNovo();
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

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

        edtNumeroDiscurso.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() >= 1) {
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                } else {
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

                }

            }
        });
        edtTemaDiscurso.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() >= 1) {
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                } else {
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

                }

            }
        });
    }

    public void salvarCongregacaoNova() {

        idCongregacao = databaseReference.child("user_data").child(userUID).child("congregacoes").push().getKey();

        if (!nomeCongregacao.isEmpty()) {
            if (!cidadeCongregacao.isEmpty()) {


                Congregacao congregacaoNova = new Congregacao();
                congregacaoNova.setIdCongregacao(idCongregacao);
                congregacaoNova.setNomeCongregacao(nomeCongregacao);
                congregacaoNova.setCidadeCongregação(cidadeCongregacao);

                databaseReference.child("user_data")
                        .child(userUID)
                        .child("congregacoes")
                        .child(idCongregacao)
                        .setValue(congregacaoNova);

                Toasty.success(getContext(), "Congregação Salva", Toast.LENGTH_SHORT).show();

                edtCongregacao.setText(congregacaoNova.getNomeCongregacao());
                idCongregacaoEscolhida = congregacaoNova.getIdCongregacao();

            } else {
                Toasty.info(getContext(), "Preencha todos os Campos!", Toast.LENGTH_SHORT).show();
                //Toasty.info(AdicionarEditarActivity.this, "Digite o nome da Cidade!", Toast.LENGTH_SHORT).show();
                //dialogoAddNovaCongregacao();
            }
        } else {
            Toasty.info(getContext(), "Preencha todos os Campos!", Toast.LENGTH_SHORT).show();
            //dialogoAddNovaCongregacao();
        }
    }

    private void salvarDiscursoNovo() {

        if (!numeroDiscurso.isEmpty()) {
            if (!temaDiscurso.isEmpty()) {

                String numeroDiscursoFormatado = String.format("%03d", Integer.parseInt(numeroDiscurso));

                Discurso discursoNovo = new Discurso();
                discursoNovo.setIdDiscurso(idDiscursoEscolhido);
                discursoNovo.setNumero(numeroDiscursoFormatado);
                discursoNovo.setTema(temaDiscurso);

                databaseReference.child("user_data")
                        .child(userUID)
                        .child("discursos")
                        .child(idDiscursoEscolhido)
                        .setValue(discursoNovo);

                Toasty.success(getContext(), "Discurso Salvo", Toast.LENGTH_SHORT).show();

                edtDiscurso.setText(discursoNovo.getTema());
                idDiscursoEscolhido = discursoNovo.getIdDiscurso();

            } else {
                Toasty.info(getContext(), "Adicione um tema!", Toast.LENGTH_SHORT).show();
                if (!numeroDiscurso.isEmpty()) {
                    dialogoAddNovoDiscurso();
                    edtNumeroDiscurso.setText(numeroDiscurso);
                    edtTemaDiscurso.setText("");
                }
            }
        } else {
            Toasty.info(getContext(), "Adicione o numero do discurso!", Toast.LENGTH_SHORT).show();
            if (!temaDiscurso.isEmpty()) {
                dialogoAddNovoDiscurso();
                edtTemaDiscurso.setText(temaDiscurso);
                edtNumeroDiscurso.setText("");
            }
        }
    }

    private void salvarAgenda() {

        if (idProferimento == null){
            if (idOradorEscolhido != null){
                idProferimento = databaseReference.child("user_data")
                        .child(userUID)
                        .child("oradores")
                        .child(idOradorEscolhido)
                        .child("proferimentos")
                        .push().getKey();

            }
        }

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
        agenda.setIdProferimento(idProferimento);

        databaseReference.child("user_data")
                .child(userUID)
                .child("agenda")
                .child(anoFormatado)
                .child(mesFormatado)
                .child(dataFormatada)
                .setValue(agenda);


        if (idDiscursoEscolhido != null){
            if (!idDiscursoEscolhido.equals("")){
                databaseReference.child("user_data")
                        .child(userUID)
                        .child("discursos")
                        .child(idDiscursoEscolhido)
                        .child("ultimoProferimento")
                        .setValue(dataFormatada);
            }
        }

        //Todo criar um model Proferimentos e uma list de Proferimentos setar no orador um proferimento
        if (idOradorEscolhido != null){
            if (!idOradorEscolhido.equals("")){
                Proferimento proferimento = new Proferimento();
                proferimento.setIdProferimentos(idProferimento);
                proferimento.setDataProferimento(dataFormatada);
                proferimento.setIdOradorProferimento(idOradorEscolhido);
                proferimento.setIdCongregacaoProferimento(idCongregacaoEscolhida);
                proferimento.setIdDiscursoProferimento(idDiscursoEscolhido);

                databaseReference.child("user_data")
                        .child(userUID)
                        .child("proferimentos")
                        .child(idProferimento)
                        .setValue(proferimento);

                databaseReference.child("user_data")
                        .child(userUID)
                        .child("oradores")
                        .child(idOradorEscolhido)
                        .child("proferimentos")
                        .child(idProferimento)
                        .setValue(idProferimento);
            }
        }


        if (idCongregacaoEscolhida != "" || idOradorEscolhido != "" || idDiscursoEscolhido != "") {
            Toasty.success(getContext(), "Agenda Atualizada", Toast.LENGTH_SHORT).show();
        }

        idCongregacaoEscolhida = null;
        idOradorEscolhido = null;
        idDiscursoEscolhido = null;
        idProferimento = null;
    }

    private void limparAgendaSelecionada() {

        SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
        Date data = null;
        try {
            data = formato.parse(agendaSelecionada.getData());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat dataSDF = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat mes = new SimpleDateFormat("MM");
        SimpleDateFormat ano = new SimpleDateFormat("yyyy");

        String dataFormatada = dataSDF.format(data);
        String mesFormatado = mes.format(data);
        String anoFormatado = ano.format(data);

        Agenda agenda = new Agenda();
        agenda.setData(dataFormatada);
        agenda.setIdCongregacao("");
        agenda.setIdOrador("");
        agenda.setIdDiscurso("");
        agenda.setIdProferimento("");

        databaseReference.child("user_data")
                .child(userUID)
                .child("agenda")
                .child(anoFormatado)
                .child(mesFormatado)
                .child(dataFormatada)
                .setValue(agenda);

        if (idOradorEscolhido != null){
            if (idProferimento != null){
                databaseReference.child("user_data")
                        .child(userUID)
                        .child("oradores")
                        .child(idOradorEscolhido)
                        .child("proferimentos")
                        .child(idProferimento)
                        .removeValue();

                databaseReference.child("user_data")
                        .child(userUID)
                        .child("proferimentos")
                        .child(idProferimento)
                        .removeValue();
            }
        }

        idCongregacaoEscolhida = null;
        idOradorEscolhido = null;
        idDiscursoEscolhido = null;
        idProferimento = null;
    }

    private void pegarValoresAgendaSelecionada() {

        for (Congregacao congregacao : congregacaoList) {
            if (congregacao.getIdCongregacao().equals(agendaSelecionada.getIdCongregacao())) {
                congregacaoEscolhida = congregacao;
                idCongregacaoEscolhida = congregacao.getIdCongregacao();
                String nomeCong = congregacao.getNomeCongregacao();
                edtCongregacao.setText(nomeCong);
            }
        }

        for (Orador orador : oradoresList) {
            if (orador.getId().equals(agendaSelecionada.getIdOrador())) {
                oradorEscolhido = orador;
                idOradorEscolhido = orador.getId();
                String nomeOrador = orador.getNome();
                edtOrador.setText(nomeOrador);
                pegarProferimentosDoBanco();
            }
        }

        for (Discurso discurso : discursosList) {
            if (discurso.getIdDiscurso().equals(agendaSelecionada.getIdDiscurso())) {
                discursoEscolhido = discurso;
                idDiscursoEscolhido = discurso.getIdDiscurso();
                String temaDiscurso = discurso.getTema();
                edtDiscurso.setText(temaDiscurso);
            }
        }

        for (Proferimento proferimento : proferimentoList){
            if (agendaSelecionada.getData().equals(proferimento.getDataProferimento())){
                idProferimento = proferimento.getIdProferimentos();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK && requestCode == 1) {
            Bundle bundle = new Bundle();
            bundle = data.getBundleExtra("oradorSelecionado");
            oradorEscolhido = (Orador) bundle.getSerializable("oradorSelecionado");
            idOradorEscolhido = oradorEscolhido.getId();
            edtOrador.setText(oradorEscolhido.getNome());
        }
    }

    private void listarOradoesPorCongregacao(String idCongregacaoClicada) {

        ArrayList<Orador> oradoresDaCongregacaoClicada = new ArrayList<>();

        if (idCongregacaoClicada != null) {
            for (Orador orador : oradoresList) {
                if (orador.getIdCongregacao().equals(idCongregacaoClicada)) {
                    oradoresDaCongregacaoClicada.add(orador);
                }
            }
            oradoresList.clear();
            oradoresList.addAll(oradoresDaCongregacaoClicada);
        }
    }

    private void pegaNomeDaCongregacao(String idCongregacaoOradorClicado){
        for (Congregacao congregacao : congregacaoList) {
            if (congregacao.getIdCongregacao().equals(idCongregacaoOradorClicado)) {
                congregacaoEscolhida = congregacao;
                idCongregacaoEscolhida = congregacao.getIdCongregacao();
                String nomeCong = congregacao.getNomeCongregacao();
                edtCongregacao.setText(nomeCong);
            }
        }
    }

    private void pegarProferimentosDoBanco(){

        //final List<Proferimento> proferimentoList = new ArrayList<>();

        valueEventListenerAgenda = databaseReference.child("user_data")
                .child(userUID)
                .child("oradores")
                .child(idOradorEscolhido)
                .child("proferimentos")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        proferimentoList.clear();

                        for (DataSnapshot dados : dataSnapshot.getChildren()){
                            String idProferimento = dados.getValue(String.class);
                            idProferimentoList.add(idProferimento);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    @SuppressLint("RestrictedApi")
    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(getContext(), v);
        popup.getMenu().add("Limpar").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                limparAgendaSelecionada();

                return false;
            }
        });
        @SuppressLint("RestrictedApi") MenuPopupHelper menuHelper = new MenuPopupHelper(getContext(), (MenuBuilder) popup.getMenu(), v);
        menuHelper.setForceShowIcon(true);
        menuHelper.setGravity(Gravity.END);
        menuHelper.show();
    }

    //Todo Remover esses metodos se não forem ser ultilizados
    private void pegarNomeCongregacoesSelecionada() {

        //congregacaoList = Congregacao.pegarCongegacoesDoBanco(userUID);

        for (Congregacao congregacao : congregacaoList) {
            if (congregacao.getIdCongregacao().equals(agendaSelecionada.getIdCongregacao())) {
                congregacaoEscolhida = congregacao;
                idCongregacaoEscolhida = congregacao.getIdCongregacao();
                String nomeCong = congregacao.getNomeCongregacao();
                edtCongregacao.setText(nomeCong);
            }
        }
    }
    private void pegarNomeCongregacoesDoBanco() {

        valueEventListenerAgenda = databaseReference
                .child("user_data")
                .child(userUID)
                .child("congregacoes")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot dados : dataSnapshot.getChildren()) {
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
                        for (DataSnapshot dados : dataSnapshot.getChildren()) {
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
                        for (DataSnapshot dados : dataSnapshot.getChildren()) {
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
}
