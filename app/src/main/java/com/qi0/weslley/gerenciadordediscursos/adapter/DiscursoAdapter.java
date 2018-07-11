package com.qi0.weslley.gerenciadordediscursos.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qi0.weslley.gerenciadordediscursos.R;
import com.qi0.weslley.gerenciadordediscursos.helper.DateUtil;
import com.qi0.weslley.gerenciadordediscursos.model.Discurso;
import com.qi0.weslley.gerenciadordediscursos.model.Proferimento;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class DiscursoAdapter extends RecyclerView.Adapter<DiscursoAdapter.MyViewHolder> {

    private List<Discurso> discursos;
    private List<Proferimento> proferimentoList;
    private Context context;

    public DiscursoAdapter(List<Discurso> discursos, List<Proferimento> proferimentosList, Context context) {
        this.discursos = discursos;
        this.proferimentoList = proferimentosList;
        this.context = context;
    }

    public List<Discurso> getDiscursos() {
        return this.discursos;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_discurso, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Discurso discurso = discursos.get(position);

        holder.numeroDiscurso.setText(discurso.getNumero());
        holder.temaDiscurso.setText(discurso.getTema());
        holder.ultimoProferimento.setText(pegarUltimoProferimento(discurso.getIdDiscurso()));

    }

    @Override
    public int getItemCount() {
        return discursos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView numeroDiscurso, temaDiscurso, ultimoProferimento;

        public MyViewHolder(View itemView) {
            super(itemView);

            numeroDiscurso = itemView.findViewById(R.id.tv_numero_discurso);
            temaDiscurso = itemView.findViewById(R.id.tv_tema_discurso);
            ultimoProferimento = itemView.findViewById(R.id.tv_data_ultimo_proferimento);
        }
    }

    private String pegarUltimoProferimento(String idDiscursoProferimento) {
        List<Proferimento> proferimentosListPorDiscurso = new ArrayList<>();
        if (proferimentoList.size() > 0){
            for (Proferimento proferimento : proferimentoList){
                if (proferimento.getIdDiscursoProferimento() != null){
                    if (proferimento.getIdDiscursoProferimento().equals(idDiscursoProferimento)){
                        proferimentosListPorDiscurso.add(proferimento);
                    }
                }
            }
            Collections.sort(proferimentosListPorDiscurso, new Comparator<Proferimento>() {
                        @Override
                        public int compare(Proferimento o1, Proferimento o2) {
                            return o1.getDataOrdenarProferimento().compareTo(o2.getDataOrdenarProferimento());
                        }
                    }
            );
            Collections.reverse(proferimentosListPorDiscurso);
            if (proferimentosListPorDiscurso.size() > 0){
                Proferimento proferimento = proferimentosListPorDiscurso.get(0);
                return DateUtil.fomatarData(proferimento.getDataProferimento());
            }
        }
        return "";
    }
}
