package com.qi0.weslley.gerenciadordediscursos.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;

import com.qi0.weslley.gerenciadordediscursos.R;
import com.qi0.weslley.gerenciadordediscursos.activitys.AdicionarEditarActivity;
import com.qi0.weslley.gerenciadordediscursos.activitys.DetalheActivity;
import com.qi0.weslley.gerenciadordediscursos.adapter.OradorAdaper;
import com.qi0.weslley.gerenciadordediscursos.helper.RecyclerItemClickListener;
import com.qi0.weslley.gerenciadordediscursos.model.Orador;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class OradoresFragment extends BaseFragment {


    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;

    ArrayList oradores = new ArrayList();

    public OradoresFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_oradores, container, false);

        setHasOptionsMenu(true);

        mokeOradores();

        recyclerView = view.findViewById(R.id.recycle_view_oradores);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        //recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        OradorAdaper adapter = new OradorAdaper(oradores, getContext());

        recyclerView.setAdapter(adapter);

        runLayoutAnimation(recyclerView);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                FloatingActionButton fab = getActivity().findViewById(R.id.fab);

                if (dy > 0){
                    if (fab.isShown()){
                        fab.hide();
                    }
                }else if (dy < 0){
                    if (!fab.isShown()){
                        fab.show();
                    }
                }
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intentOradorDealhes = new Intent(getActivity(), DetalheActivity.class);
                intentOradorDealhes.putExtra("qualFragmentAbrir", "DetalheOradorFragment");
                startActivity(intentOradorDealhes);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        }));

        return view;
    }

    private void mokeOradores() {
        for (int i = 0; i<= 50; i++){
            Orador orador = new Orador();
            orador.setNome("Weslley " + i);
            orador.setCongregacaoTest("Nome da Congregação " + i);
            orador.setUltimaVisita("15/10/18");
            oradores.add(orador);
        }
    }

    private void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }
}
