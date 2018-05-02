package com.qi0.weslley.gerenciadordediscursos.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qi0.weslley.gerenciadordediscursos.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AgendaAdapter extends RecyclerView.Adapter<AgendaAdapter.MyViewHolder>{

    private List<Date> datasList;
    private LayoutInflater mInflater;

    public AgendaAdapter(Context context, List<Date> datas) {
        this.mInflater = LayoutInflater.from(context);
        this.datasList = datas;
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
        //TODO
        Date data = datasList.get(position);
        Calendar cal = Calendar.getInstance();

        cal.setTime(data);

        SimpleDateFormat dia = new SimpleDateFormat("dd");
        SimpleDateFormat mes = new SimpleDateFormat("MMM");
        SimpleDateFormat ano = new SimpleDateFormat("yyyy");

        String diaFormatado = dia.format(data);
        String mesFormatado = mes.format(data);
        String anoFormatado = ano.format(data);

        holder.tvDia.setText(diaFormatado);
        holder.tvMes.setText(mesFormatado.toUpperCase());
        holder.tvAno.setText(anoFormatado);
    }

    @Override
    public int getItemCount() {
        return datasList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvDia, tvMes, tvAno;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvDia = itemView.findViewById(R.id.tv_dia_agenda_lis);
            tvMes = itemView.findViewById(R.id.tv_mes_agenda_list);
            tvAno = itemView.findViewById(R.id.tv_ano_agenda_list);
        }
    }
}
