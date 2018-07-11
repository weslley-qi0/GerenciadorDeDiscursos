package com.qi0.weslley.gerenciadordediscursos.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.qi0.weslley.gerenciadordediscursos.R;
import com.qi0.weslley.gerenciadordediscursos.adapter.OradorProferimentosAdapter;
import com.qi0.weslley.gerenciadordediscursos.config.ConfiguracaoFirebase;
import com.qi0.weslley.gerenciadordediscursos.model.Discurso;
import com.qi0.weslley.gerenciadordediscursos.model.Orador;
import com.qi0.weslley.gerenciadordediscursos.model.Proferimento;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProferimentosDetalheFragment extends BaseFragment {

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;

    List<Discurso> discursosList = new ArrayList();
    List<Proferimento> proferimentosList = new ArrayList();

    OradorProferimentosAdapter oradorProferimentosAdapter;

    String userUID;
    Orador oradorSelecionado;

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    public ProferimentosDetalheFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = ConfiguracaoFirebase.getAuth();
        userUID = firebaseAuth.getCurrentUser().getUid();
        discursosList = Discurso.pegarDiscursosDoBanco(userUID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_proferimentos_detalhe, container, false);

        oradorSelecionado = (Orador) getArguments().getSerializable("oradorSelecionado");

        if (oradorSelecionado != null) {
            //ToDo peger discursos feitos pelo orador selecionado e add a list
            //discursos.addAll(oradorSelecionado.getProferimentos());
        }

        //firebaseAuth = ConfiguracaoFirebase.getAuth();
        //databaseReference = ConfiguracaoFirebase.getFirebaseDatabase();
        //userUID = firebaseAuth.getCurrentUser().getUid();

        recyclerView = view.findViewById(R.id.recycle_view_proferimentos_orador_detalhe);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        pegarListaDeProferimestosOradorSelecionado();

        oradorProferimentosAdapter = new OradorProferimentosAdapter(proferimentosList, discursosList, getContext());

        recyclerView.setAdapter(oradorProferimentosAdapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void pegarListaDeProferimestosOradorSelecionado() {

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
                            if (proferimento.getIdOradorProferimento().equals(oradorSelecionado.getId())) {
                                proferimentosList.add(proferimento);
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
                        oradorProferimentosAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}
