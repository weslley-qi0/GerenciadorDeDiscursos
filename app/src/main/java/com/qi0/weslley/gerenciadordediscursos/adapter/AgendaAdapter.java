package com.qi0.weslley.gerenciadordediscursos.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qi0.weslley.gerenciadordediscursos.R;
import com.qi0.weslley.gerenciadordediscursos.model.Agenda;
import com.qi0.weslley.gerenciadordediscursos.model.Congregacao;
import com.qi0.weslley.gerenciadordediscursos.model.Discurso;
import com.qi0.weslley.gerenciadordediscursos.model.Orador;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AgendaAdapter extends RecyclerView.Adapter<AgendaAdapter.MyViewHolder>{

    private List<Agenda> agendaList;
    private List<Congregacao> congregacaoList;
    private List<Orador> oradorList;
    private List<Discurso> discursoList;

    private Context context;
    private LayoutInflater mInflater;

    public AgendaAdapter(List<Agenda> agendaList, List<Congregacao> congregacaoList, List<Orador> oradoresList, List<Discurso> discursosList, Context context) {
        this.agendaList = agendaList;
        this.congregacaoList = congregacaoList;
        this.oradorList = oradoresList;
        this.discursoList = discursosList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_agenda, parent, false);
        //View view = mInflater.inflate(R.layout.item_agenda_0, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Agenda agenda = agendaList.get(position);

        SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
        Date data = null;

        if (agenda.getData() != null) {
            try {
                data = formato.parse(agenda.getData());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        SimpleDateFormat dia = new SimpleDateFormat("dd");
        SimpleDateFormat mes = new SimpleDateFormat("MMM");
        SimpleDateFormat ano = new SimpleDateFormat("yyyy");

        String diaFormatado = dia.format(data);
        String mesFormatado = mes.format(data);
        String anoFormatado = ano.format(data);

        holder.tvDia.setText(diaFormatado);
        holder.tvMes.setText(mesFormatado.toUpperCase());
        holder.tvAno.setText(anoFormatado);

        holder.agendaCongregacao.setText("Congregação: " + pegarNomeCongregacao(agenda));
        holder.agendaOrador.setText(pegarNomeOrador(agenda));
        holder.agendaDiscurso.setText("Tema: " + pegarTemaDiscurso(agenda));
    }

    @Override
    public int getItemCount() {
        return agendaList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvDia, tvMes, tvAno, agendaOrador, agendaCongregacao, agendaDiscurso;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvDia = itemView.findViewById(R.id.tv_dia_agenda_lis);
            tvMes = itemView.findViewById(R.id.tv_mes_agenda_list);
            tvAno = itemView.findViewById(R.id.tv_ano_agenda_list);
            agendaCongregacao = itemView.findViewById(R.id.tv_agenda_congregacao);
            agendaOrador = itemView.findViewById(R.id.tv_agenda_orador);
            agendaDiscurso = itemView.findViewById(R.id.tv_agenda_tema);

        }
    }

    private String pegarNomeCongregacao(Agenda agenda){
        String nomeCong = "";
        for (Congregacao congregacao : congregacaoList){
            if (congregacao.getIdCongregacao() != null) {
                String idCongregacao = congregacao.getIdCongregacao();
                if (idCongregacao.equals(agenda.getIdCongregacao())) {
                    nomeCong = congregacao.getNomeCongregacao();
                }
            }
        }
        return nomeCong;
    }

    private String pegarNomeOrador(Agenda agenda){

        String nomeOrador = "";
        for (Orador orador : oradorList){
            if (orador.getId() != null) {
                String idOrador = orador.getId();
                if (idOrador.equals(agenda.getIdOrador())) {
                    nomeOrador = orador.getNome();
                }
            }
        }

        return nomeOrador;
    }

    private String pegarTemaDiscurso(Agenda agenda){
        String temaDiscurso = "";
        for (Discurso discurso : discursoList){
            if (discurso.getIdDiscurso() != null) {
                String idDiscurso = discurso.getIdDiscurso();
                if (idDiscurso.equals(agenda.getIdDiscurso())) {
                    temaDiscurso = discurso.getTema();
                }
            }
        }
        return temaDiscurso;
    }
}
