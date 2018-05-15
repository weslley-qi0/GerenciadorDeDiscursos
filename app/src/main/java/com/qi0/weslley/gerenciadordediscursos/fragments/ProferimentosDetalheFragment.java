package com.qi0.weslley.gerenciadordediscursos.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qi0.weslley.gerenciadordediscursos.R;
import com.qi0.weslley.gerenciadordediscursos.adapter.DiscursoAdapter;
import com.qi0.weslley.gerenciadordediscursos.model.Discurso;
import com.qi0.weslley.gerenciadordediscursos.model.Orador;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProferimentosDetalheFragment extends BaseFragment {

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;

    ArrayList<Discurso> discursos = new ArrayList();


    public ProferimentosDetalheFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_proferimentos_detalhe, container, false);

        Orador oradorSelecionado = (Orador) getArguments().getSerializable("oradorSelecionado");

        if (oradorSelecionado != null){
            discursos.addAll(oradorSelecionado.getDiscursoListOrador());
        }

        recyclerView = view.findViewById(R.id.recycle_view_proferimentos_orador_detalhe);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        DiscursoAdapter adapter = new DiscursoAdapter(discursos, getContext());

        recyclerView.setAdapter(adapter);

        return view;
    }
}
