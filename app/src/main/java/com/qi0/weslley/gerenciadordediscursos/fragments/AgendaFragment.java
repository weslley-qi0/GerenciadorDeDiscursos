package com.qi0.weslley.gerenciadordediscursos.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.qi0.weslley.gerenciadordediscursos.R;
import com.qi0.weslley.gerenciadordediscursos.adapter.TabsAdapter;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class AgendaFragment extends BaseFragment {


    SmartTabLayout viewPagerTab;
    ViewPager viewPager;

    int ano = 2018;

    public AgendaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_agenda, container, false);

        setHasOptionsMenu(true);

        viewPager = view.findViewById(R.id.viewpager);
        viewPagerTab = view.findViewById(R.id.viewPagerTab);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        int currentMes = Calendar.MONTH + 1;

        setupViewPager(viewPager);
        viewPagerTab.setViewPager(viewPager);
        viewPager.setCurrentItem(currentMes + 1);

    }

    private void setupViewPager(ViewPager viewPager) {

        TabsAdapter adapter = new TabsAdapter(getContext(), getChildFragmentManager());
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

       /* MenuItem item = menu.findItem(R.id.spinner);
        Spinner spinner = (Spinner) item.getActionView();

        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.spinner_list_item_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String a = String.valueOf(parent.getItemAtPosition(position));
                ano = Integer.parseInt(a);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
    }

}
