package com.qi0.weslley.gerenciadordediscursos.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qi0.weslley.gerenciadordediscursos.R;

import java.util.List;

public class DatasAdapter extends RecyclerView.Adapter<DatasAdapter.MyViewHolder>{

    private List<String> datas;
    private LayoutInflater mInflater;

    public DatasAdapter(Context context, List<String> datas) {
        this.mInflater = LayoutInflater.from(context);
        this.datas = datas;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_agenda_0, parent, false);
        //View view = mInflater.inflate(R.layout.item_agenda_0, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        String data = datas.get(position);
        holder.tv.setText(data);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv = itemView.findViewById(R.id.tv_data);
        }
    }
}
