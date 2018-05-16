package com.qi0.weslley.gerenciadordediscursos.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;


import com.qi0.weslley.gerenciadordediscursos.R;
import com.qi0.weslley.gerenciadordediscursos.model.Congregacao;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class CongregacaoAdapter extends RecyclerView.Adapter<CongregacaoAdapter.MyViewHolder>{

    private List<Congregacao> congregacaoList;
    private Context context;

    public CongregacaoAdapter(List<Congregacao> congregacaoList, Context context) {
        this.congregacaoList = congregacaoList;
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
        holder.quantidadeOradores.setText(String.valueOf(congregacao.getQuantOradores()));


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
}
