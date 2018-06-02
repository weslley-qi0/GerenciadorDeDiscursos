package com.qi0.weslley.gerenciadordediscursos.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qi0.weslley.gerenciadordediscursos.R;
import com.qi0.weslley.gerenciadordediscursos.model.Discurso;

import java.util.List;

public class DiscursoAdapter extends RecyclerView.Adapter<DiscursoAdapter.MyViewHolder> {

    private List<Discurso> discursos;
    private Context context;

    public DiscursoAdapter(List<Discurso> discursos, Context context) {
        this.discursos = discursos;
        this.context = context;
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
        holder.ultimoProferimento.setText(discurso.getUltimoProferimento());

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
}
