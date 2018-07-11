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
import java.util.Date;
import java.util.List;

public class OradorProferimentosAdapter extends RecyclerView.Adapter<OradorProferimentosAdapter.MyViewHolder> {

    private List<Proferimento> proferimentoList;
    private List<Discurso> discursoList;
    private Context context;

    public OradorProferimentosAdapter(List<Proferimento> proferimentoList, List<Discurso> discursoList, Context context) {
        this.proferimentoList = proferimentoList;
        this.discursoList = discursoList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_proferimento_orador, parent, false); //Todo Mudar o Layout

        return new OradorProferimentosAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Proferimento proferimento = proferimentoList.get(position);
        Discurso discurso = pegarValoresDiscursoProferimento(proferimento);

        holder.dataProferimento.setText(DateUtil.fomatarData(proferimento.getDataProferimento()));
        holder.numeroDoDiscurso.setText(discurso.getNumero());
        holder.temaDoDiscurso.setText(discurso.getTema());
    }

    @Override
    public int getItemCount() {
        return proferimentoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView dataProferimento, numeroDoDiscurso, temaDoDiscurso;
        public MyViewHolder(View itemView) {
            super(itemView);

            dataProferimento = itemView.findViewById(R.id.tv_item_proferimento_data);
            numeroDoDiscurso = itemView.findViewById(R.id.tv_item_proferimento_numero);
            temaDoDiscurso = itemView.findViewById(R.id.tv_item_proferimento_tema_discurso);
        }
    }

    private Discurso pegarValoresDiscursoProferimento(Proferimento proferimento){
        Discurso discursoRestorno = new Discurso();

        for (Discurso discurso : discursoList){
            if (discurso.getIdDiscurso() != null) {
                String idDiscurso = discurso.getIdDiscurso();
                if (idDiscurso.equals(proferimento.getIdDiscursoProferimento())) {
                    discursoRestorno = discurso;
                }
            }
        }
        return discursoRestorno;
    }
}
