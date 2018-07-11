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
import com.qi0.weslley.gerenciadordediscursos.model.Agenda;
import com.qi0.weslley.gerenciadordediscursos.model.Congregacao;
import com.qi0.weslley.gerenciadordediscursos.model.Orador;
import com.qi0.weslley.gerenciadordediscursos.model.Proferimento;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DiscursoProferimentosAdapter extends RecyclerView.Adapter<DiscursoProferimentosAdapter.MyViewHolder> {

    private List<Proferimento> proferimentoList;
    private List<Congregacao> congregacoesList;
    private List<Orador> oradoresList;
    private Context context;

    public DiscursoProferimentosAdapter(List<Proferimento> proferimentoList, List<Congregacao> congregacaoList, List<Orador> oradoresList, Context context) {
        this.proferimentoList = proferimentoList;
        this.congregacoesList = congregacaoList;
        this.oradoresList = oradoresList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_proferimento_discurso, parent, false); //Todo Mudar o Layout

        return new DiscursoProferimentosAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Proferimento proferimento = proferimentoList.get(position);

        holder.dataProferimento.setText(DateUtil.fomatarData(proferimento.getDataProferimento()));
        holder.nomeOrador.setText(pegarNomeOrador(proferimento.getIdOradorProferimento()));
        holder.nomeCongregação.setText(pegarNomeCongregacao(proferimento.getIdCongregacaoProferimento()));
    }

    @Override
    public int getItemCount() {
        return proferimentoList.size();// ToDo Muda para este
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView dataProferimento, nomeOrador, nomeCongregação;
        public MyViewHolder(View itemView) {
            super(itemView);

            dataProferimento = itemView.findViewById(R.id.tv_item_proferimento_discurso_data);
            nomeOrador = itemView.findViewById(R.id.tv_item_proferimento_discurso_nome_orador);
            nomeCongregação = itemView.findViewById(R.id.tv_item_proferimento_discurso_nome_congregacao);
        }
    }

    private String pegarNomeCongregacao(String idCongregacaoProferimento){
        String nomeCong = "";
        for (Congregacao congregacao : congregacoesList){
            if (congregacao.getIdCongregacao() != null) {
                String idCongregacao = congregacao.getIdCongregacao();
                if (idCongregacao.equals(idCongregacaoProferimento)) {
                    nomeCong = congregacao.getNomeCongregacao();
                    return nomeCong;
                }
            }
        }
        return nomeCong;
    }

    private String pegarNomeOrador(String idOradorProferimento){
        String nomeOrador = "";
        for (Orador orador : oradoresList){
            if (orador.getId() != null) {
                String idOrador = orador.getId();
                if (idOrador.equals(idOradorProferimento)) {
                    nomeOrador = orador.getNome();
                    return nomeOrador;
                }
            }
        }

        return nomeOrador;
    }
}
