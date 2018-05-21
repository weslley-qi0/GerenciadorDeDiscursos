package com.qi0.weslley.gerenciadordediscursos.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.qi0.weslley.gerenciadordediscursos.R;
import com.qi0.weslley.gerenciadordediscursos.model.Congregacao;
import com.qi0.weslley.gerenciadordediscursos.model.Orador;

import java.util.ArrayList;
import java.util.List;

public class CongregacaoAdapter extends RecyclerView.Adapter<CongregacaoAdapter.MyViewHolder>{

    private List<Congregacao> congregacaoList;
    private List<Orador> oradorList;
    private Context context;

    public CongregacaoAdapter(List<Congregacao> congregacaoList, List<Orador> oradorList, Context context) {
        this.congregacaoList = congregacaoList;
        this.oradorList = oradorList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_congregacao, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Congregacao congregacao = congregacaoList.get(position);

        holder.nomeCongregacao.setText(congregacao.getNomeCongregacao());
        holder.cidadeCongregacao.setText(congregacao.getCidadeCongregação());
        holder.quantidadeOradores.setText(String.valueOf(pegarQuantOradores(congregacao)));
    }

    @Override
    public int getItemCount() {
        return congregacaoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nomeCongregacao, cidadeCongregacao, quantidadeOradores;

        @SuppressLint("CutPasteId")
        public MyViewHolder(View itemView) {
            super(itemView);

            nomeCongregacao = itemView.findViewById(R.id.tv_congregacao_nome_congregacao);
            cidadeCongregacao = itemView.findViewById(R.id.tv_congregacao_nome_cidade);
            quantidadeOradores = itemView.findViewById(R.id.tv_congregacao_quantide_oradores);
        }
    }

    private int pegarQuantOradores(Congregacao congregacao){

        ArrayList<String> qtdOradores = new ArrayList<>();
        for (Orador orador : oradorList){
            if (orador.getIdCongregacao() != null) {
                String idOradorCongregacao = orador.getIdCongregacao();
                if (idOradorCongregacao.equals(congregacao.getIdCongregacao())) {
                    qtdOradores.add(idOradorCongregacao);
                }
            }
        }
        return qtdOradores.size();
    }
}
