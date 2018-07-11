package com.qi0.weslley.gerenciadordediscursos.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.qi0.weslley.gerenciadordediscursos.R;
import com.qi0.weslley.gerenciadordediscursos.adapter.DiscursoProferimentosAdapter;
import com.qi0.weslley.gerenciadordediscursos.config.ConfiguracaoFirebase;
import com.qi0.weslley.gerenciadordediscursos.helper.RecyclerItemClickListener;
import com.qi0.weslley.gerenciadordediscursos.model.Congregacao;
import com.qi0.weslley.gerenciadordediscursos.model.Discurso;
import com.qi0.weslley.gerenciadordediscursos.model.Orador;
import com.qi0.weslley.gerenciadordediscursos.model.Proferimento;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetalheDiscursoFragment extends Fragment {

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    EditText edtNumeroDiscurso;
    EditText edtTemaDiscurso;
    ImageView imgProferimentosEmpty;
    TextView msgProferimentosEmpty;

    String userUID;
    String numeroDiscurso;
    String temaDiscurso;
    String idDiscursoEscolhido;

    Discurso discursoSelecionado;
    List<Orador> oradoresList = new ArrayList();
    List<Congregacao> congregacoesList = new ArrayList();
    List<Proferimento> proferimentosList = new ArrayList();
    DiscursoProferimentosAdapter discursoProferimentosAdapter;

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    public DetalheDiscursoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = ConfiguracaoFirebase.getAuth();
        userUID = firebaseAuth.getCurrentUser().getUid();
        oradoresList = Orador.pegarOradoresDoBanco(userUID);
        congregacoesList = Congregacao.pegarCongegacoesDoBanco(userUID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detalhe_discurso, container, false);

        discursoSelecionado = (Discurso) getArguments().getSerializable("discursoSelecionado");

        TextView tvNumeroDetalhe = view.findViewById(R.id.tv_numero_detalhe_proferimento_discurso);
        TextView tvTemaDetalhe = view.findViewById(R.id.tv_tema_detalhe_discurso);
        final View viewFabDetalhe = getActivity().findViewById(R.id.fab_detalhe);

        tvTemaDetalhe.setText(discursoSelecionado.getTema());
        tvNumeroDetalhe.setText(discursoSelecionado.getNumero());

        imgProferimentosEmpty = view.findViewById(R.id.img_proferimentos_empty);
        msgProferimentosEmpty = view.findViewById(R.id.msg_proferimentos_empty);
        recyclerView = view.findViewById(R.id.recycle_view_proferimentos_discurso_detalhe);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        pegarListaDeProferimentosDiscursoSelecionado();

        viewFabDetalhe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogoAddNovoDiscurso();
            }
        });

        discursoProferimentosAdapter = new DiscursoProferimentosAdapter(proferimentosList, congregacoesList, oradoresList, getContext());
        recyclerView.setAdapter(discursoProferimentosAdapter);
        runLayoutAnimation(recyclerView);
        scrollFabHideShow();
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
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

    private void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    private void pegarListaDeProferimentosDiscursoSelecionado() {

        databaseReference = ConfiguracaoFirebase.getFirebaseDatabase();
        databaseReference.child("user_data")
                .child(userUID)
                .child("proferimentos")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        proferimentosList.clear();
                        for (DataSnapshot dados : dataSnapshot.getChildren()) {
                            Proferimento proferimento = dados.getValue(Proferimento.class);
                            if (proferimento.getIdDiscursoProferimento() != null && !proferimento.getIdDiscursoProferimento().equals("")) {
                                if (proferimento.getIdDiscursoProferimento().equals(discursoSelecionado.getIdDiscurso())) {
                                    proferimentosList.add(proferimento);
                                }
                            }

                        }

                        Collections.sort(proferimentosList, new Comparator<Proferimento>() {
                                    @Override
                                    public int compare(Proferimento o1, Proferimento o2) {
                                        return o1.getDataOrdenarProferimento().compareTo(o2.getDataOrdenarProferimento());
                                    }
                                }
                        );
                        Collections.reverse(proferimentosList);
                        discursoProferimentosAdapter.notifyDataSetChanged();

                        if (proferimentosList.size() == 0){
                            recyclerView.setVisibility(View.GONE);
                            imgProferimentosEmpty.setVisibility(View.VISIBLE);
                            msgProferimentosEmpty.setVisibility(View.VISIBLE);
                        }else {
                            recyclerView.setVisibility(View.VISIBLE);
                            imgProferimentosEmpty.setVisibility(View.GONE);
                            msgProferimentosEmpty.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

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

        if (discursoSelecionado != null) {
            edtNumeroDiscurso.setText(discursoSelecionado.getNumero());
            edtTemaDiscurso.setText(discursoSelecionado.getTema());
            idDiscursoEscolhido = discursoSelecionado.getIdDiscurso();

            dialogo.setNeutralButton("EXCLUIR", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    databaseReference.child("user_data").child(userUID).child("discursos").child(discursoSelecionado.getIdDiscurso()).removeValue();
                    Toasty.success(getActivity(), "Discurso Excluido", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                    dialog.dismiss();
                }
            });
        }

        dialogo.setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                numeroDiscurso = edtNumeroDiscurso.getText().toString().trim();
                temaDiscurso = edtTemaDiscurso.getText().toString().trim();
                //idDiscursoEscolhido = databaseReference.child("user_data").child(userUID).child("discursos").push().getKey();
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

        Button bC = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        bC.setTextColor(Color.GRAY);
        Button bN = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
        bN.setTextColor(Color.RED);

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
                    Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    b.setTextColor(getResources().getColor(R.color.green_500));
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
                    Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    b.setTextColor(getResources().getColor(R.color.green_500));
                } else {
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

                }

            }
        });
    }

    private void salvarDiscursoNovo() {

        if (!numeroDiscurso.isEmpty()) {
            if (!temaDiscurso.isEmpty()) {

                if (idDiscursoEscolhido == null || idDiscursoEscolhido.equals("")) {
                    idDiscursoEscolhido = databaseReference.child("user_data").child(userUID).child("discursos").push().getKey();
                }

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

                //edtDiscurso.setText(discursoNovo.getTema());
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

    private void scrollFabHideShow() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                FloatingActionButton fab = getActivity().findViewById(R.id.fab_detalhe);

                if (dy > 0){
                    if (fab.isShown()){
                        fab.hide();
                    }
                }else if (dy < 0){
                    if (!fab.isShown()){
                        fab.show();
                    }
                }
            }
        });
    }

}
