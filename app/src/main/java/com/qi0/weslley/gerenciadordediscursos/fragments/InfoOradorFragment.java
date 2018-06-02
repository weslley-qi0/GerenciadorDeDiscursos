package com.qi0.weslley.gerenciadordediscursos.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.qi0.weslley.gerenciadordediscursos.R;
import com.qi0.weslley.gerenciadordediscursos.config.ConfiguracaoFirebase;
import com.qi0.weslley.gerenciadordediscursos.model.Congregacao;
import com.qi0.weslley.gerenciadordediscursos.model.Discurso;
import com.qi0.weslley.gerenciadordediscursos.model.Orador;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class InfoOradorFragment extends BaseFragment {

    public InfoOradorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_info_orador, container, false);

        Orador oradorSelecionado = (Orador) getArguments().getSerializable("oradorSelecionado");
        //Congregacao congregacao = oradorSelecionado.getIdCongregacao(); //Todo Concertar

        TextView tvCardNome = view.findViewById(R.id.tv_card_nome_detalhe_orador);
        TextView tvCardCongregacao = view.findViewById(R.id.tv_card_cangragacao_detalhe_orador);
        TextView tvCardTelefone = view.findViewById(R.id.tv_card_telefone_detalhe_orador);
        TextView tvCardEmail = view.findViewById(R.id.tv_card_email_detalhe_orador);
        TextView tvCardNumerosFeitos = view.findViewById(R.id.tv_card_numero_feitos_detalhe_orador);
        TextView tvCardNumerosCancelados = view.findViewById(R.id.tv_card_numero_cancelados_detalhe_orador);
        TextView tvCardNumerosAlterados = view.findViewById(R.id.tv_card_numero_alterados_detalhe_orador);

        tvCardNome.setText(oradorSelecionado.getNome());
        //tvCardCongregacao.setText(congregacao.getNomeCongregacao()); //Todo Concertar
        tvCardTelefone.setText(oradorSelecionado.getTelefone());
        tvCardEmail.setText(oradorSelecionado.getEmail());
        //tvCardNumerosFeitos.setText(discursosList.size());
        //tvCardNumerosCancelados.setText(oradorSelecionado.getNome());
        //tvCardNumerosAlterados.setText(oradorSelecionado.getNome());

        return view;
    }

}
