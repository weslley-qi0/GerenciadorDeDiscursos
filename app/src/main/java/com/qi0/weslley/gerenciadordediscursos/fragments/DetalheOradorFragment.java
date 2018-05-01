package com.qi0.weslley.gerenciadordediscursos.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.qi0.weslley.gerenciadordediscursos.R;
import com.qi0.weslley.gerenciadordediscursos.activitys.AdicionarEditarActivity;
import com.qi0.weslley.gerenciadordediscursos.activitys.DetalheActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetalheOradorFragment extends Fragment {

    SmartTabLayout viewPagerTab;
    ViewPager viewPager;

    public DetalheOradorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detalhe_orador, container, false);

        viewPager = view.findViewById(R.id.viewpager_orador_detalhe);
        viewPagerTab = view.findViewById(R.id.viewPagerTab_orador_detalhe);

        return view;
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
                startActivity(intentEditarOrador);
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
                .add("INFO", InfoOradorFragment.class)
                .add("PROFERIMENTOS", ProferimentosDetalheFragment.class)
                .create());

        viewPager.setAdapter(adapter);
    }

    public ViewPager getViewPager() {
        return viewPager;
    }
}
