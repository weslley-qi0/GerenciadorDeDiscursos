package com.qi0.weslley.gerenciadordediscursos.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qi0.weslley.gerenciadordediscursos.R;
import com.qi0.weslley.gerenciadordediscursos.helper.DateUtil;
import com.qi0.weslley.gerenciadordediscursos.model.Congregacao;
import com.qi0.weslley.gerenciadordediscursos.model.Orador;
import com.qi0.weslley.gerenciadordediscursos.model.Proferimento;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class OradorAdaper extends RecyclerView.Adapter<OradorAdaper.MyViewHolder> {

    private List<Orador> oradores;
    List<Congregacao> congregacoes;
    private List<Proferimento> proferimentosList;
    private Context context;

    public OradorAdaper(List<Orador> oradores, List<Proferimento> proferimentosList, List<Congregacao> congregacoes, Context context) {
        this.oradores = oradores;
        this.congregacoes = congregacoes;
        this.proferimentosList = proferimentosList;
        this.context = context;
    }

    public List<Orador> getOradores() {
        return this.oradores;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_orador, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        Orador orador = oradores.get( position );
        holder.nomeOrador.setText( orador.getNome() );
        holder.congregacao.setText( pegarNomeDaCongregacao(orador.getIdCongregacao()) );
        holder.ultimaVisita.setText(pegarUltimaVisitaPorOrador(orador.getId()));

        if( orador.getUrlFotoOrador() != null ){
            Uri uri = Uri.parse( orador.getUrlFotoOrador() );
            Glide.with( context )
                    .load( uri )
                    .error( R.drawable.img_padrao )
                    .into( holder.fotoOrador );
        }else {
            holder.fotoOrador.setImageResource( R.drawable.img_padrao );
        }
    }

    @Override
    public int getItemCount() {
        return oradores.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nomeOrador, congregacao, ultimaVisita;
        CircleImageView fotoOrador;
        //ImageView menuItem;

        public MyViewHolder(View itemView) {
            super(itemView);
            nomeOrador = itemView.findViewById(R.id.tv_nome_orador);
            congregacao = itemView.findViewById(R.id.tv_orador_nome_congregacao);
            ultimaVisita = itemView.findViewById(R.id.tv_data_ultima_visita);
            fotoOrador = itemView.findViewById(R.id.item_orador_foto);
            //menuItem = itemView.findViewById(R.id.menu_item_recycle_view_orador);
        }

    }

    private String pegarNomeDaCongregacao(String idCongregacao){
        String nomeDaCongregacao = "";

        for (Congregacao congregacao : congregacoes){
            if (congregacao.getIdCongregacao() != null){
                if (congregacao.getIdCongregacao().equals(idCongregacao)){
                    nomeDaCongregacao = congregacao.getNomeCongregacao();
                }
            }

        }
        return nomeDaCongregacao;
    }

    private String pegarUltimaVisitaPorOrador(String idOradorProferimento) {
        List<Proferimento> proferimentosListPorOrador = new ArrayList<>();
        if (proferimentosList.size() > 0){
            for (Proferimento proferimento : proferimentosList){
                if (proferimento.getIdOradorProferimento() != null){
                    if (proferimento.getIdOradorProferimento().equals(idOradorProferimento)){
                        proferimentosListPorOrador.add(proferimento);
                    }
                }
            }
            Collections.sort(proferimentosListPorOrador, new Comparator<Proferimento>() {
                        @Override
                        public int compare(Proferimento o1, Proferimento o2) {
                            return o1.getDataOrdenarProferimento().compareTo(o2.getDataOrdenarProferimento());
                        }
                    }
            );
            Collections.reverse(proferimentosListPorOrador);
            if (proferimentosListPorOrador.size() > 0){
                Proferimento proferimento = proferimentosListPorOrador.get(0);
                return DateUtil.fomatarData(proferimento.getDataProferimento());
            }
        }
        return "";
    }
}
