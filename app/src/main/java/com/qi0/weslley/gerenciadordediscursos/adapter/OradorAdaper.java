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
import com.qi0.weslley.gerenciadordediscursos.model.Orador;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class OradorAdaper extends RecyclerView.Adapter<OradorAdaper.MyViewHolder> {

    private List<Orador> oradores;
    private Context context;

    public OradorAdaper(List<Orador> oradores, Context context) {
        this.oradores = oradores;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_orador, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Orador orador = oradores.get( position );
        holder.nomeOrador.setText( orador.getNome() );
        holder.congregacao.setText( "" );
        holder.ultimaVisita.setText( orador.getUltimaVisita() );

        if( orador.getUrlFotoOrador() != null ){
            Uri uri = Uri.parse( orador.getUrlFotoOrador() );
            Glide.with( context )
                    .load( uri )
                    .into( holder.fotoOrador );

        }else {
            holder.fotoOrador.setImageResource( R.drawable.img_padrao );
        }
    }

    @Override
    public int getItemCount() {
        return oradores.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView nomeOrador, congregacao, ultimaVisita;
        CircleImageView fotoOrador;

        public MyViewHolder(View itemView) {
            super(itemView);
            nomeOrador = itemView.findViewById(R.id.tv_nome_orador);
            congregacao = itemView.findViewById(R.id.tv_orador_nome_congregacao);
            ultimaVisita = itemView.findViewById(R.id.tv_data_ultima_visita);
            fotoOrador = itemView.findViewById(R.id.item_orador_foto);
        }
    }
}
