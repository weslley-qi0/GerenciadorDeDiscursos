package com.qi0.weslley.gerenciadordediscursos.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.Bundler;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.qi0.weslley.gerenciadordediscursos.R;
import com.qi0.weslley.gerenciadordediscursos.activitys.AdicionarEditarActivity;
import com.qi0.weslley.gerenciadordediscursos.model.Orador;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetalheOradorFragment extends BaseFragment {

    SmartTabLayout viewPagerTab;
    ViewPager viewPager;
    CircleImageView fotoOradorDetalhe;
    TextView detalheNomeOrador;
    RatingBar ratingBarDetalheOrador;

    Orador oradorSelecionado;

    public DetalheOradorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detalhe_orador, container, false);

        // Pega o Orador selecionado no RecycleView
        oradorSelecionado = (Orador) getArguments().getSerializable("oradorSelecionado");

        // Mapea os Componentes
        fotoOradorDetalhe = view.findViewById(R.id.img_orador_detalhe_fragment);
        detalheNomeOrador = view.findViewById(R.id.tv_card_nome_detalhe_orador);
        ratingBarDetalheOrador = view.findViewById(R.id.ratindBar_detalhe_orador);
        viewPager = view.findViewById(R.id.viewpager_orador_detalhe);
        viewPagerTab = view.findViewById(R.id.viewPagerTab_orador_detalhe);

        setValoresOradorSelecionado();
        return view;
    }

    private void setValoresOradorSelecionado() {

        detalheNomeOrador.setText(oradorSelecionado.getNome());
        ratingBarDetalheOrador.setRating(oradorSelecionado.getRatingOrador());
        if( oradorSelecionado.getUrlFotoOrador() != null ){
            Uri uri = Uri.parse( oradorSelecionado.getUrlFotoOrador() );
            Glide.with(getContext())
                    .load(uri)
                    .error(R.drawable.img_padrao)
                    .into( fotoOradorDetalhe );
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupViewPager(viewPager);
        viewPagerTab.setViewPager(viewPager);

        final View viewFabDetalhe = getActivity().findViewById(R.id.fab_detalhe);
        viewFabDetalhe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentEditarOrador = new Intent(getActivity(), AdicionarEditarActivity.class);
                intentEditarOrador.putExtra("qualFragmentAbrir", "AddOradorFragment");
                intentEditarOrador.putExtra("oradorSelecionado", oradorSelecionado);
                startActivity(intentEditarOrador);
                getActivity().finish();
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    viewFabDetalhe.setVisibility(View.INVISIBLE);
                }else {
                    viewFabDetalhe.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getChildFragmentManager(), FragmentPagerItems.with(getContext())
                .add("INFO", InfoOradorFragment.class, new Bundler().putSerializable("oradorSelecionado", oradorSelecionado).get())
                .add("PROFERIMENTOS", ProferimentosDetalheFragment.class)
                .create());

        viewPager.setAdapter(adapter);
    }
}
