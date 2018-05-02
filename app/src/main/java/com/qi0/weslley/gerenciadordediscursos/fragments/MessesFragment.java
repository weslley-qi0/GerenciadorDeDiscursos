package com.qi0.weslley.gerenciadordediscursos.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.qi0.weslley.gerenciadordediscursos.R;
import com.qi0.weslley.gerenciadordediscursos.adapter.AgendaAdapter;
import com.qi0.weslley.gerenciadordediscursos.adapter.DatasAdapter;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessesFragment extends BaseFragment {


    int mes= 0;
    int ano = 2018;

    ArrayList datas = new ArrayList();
    List<Date> datasList = new ArrayList();
    DatasAdapter adapter;
    AgendaAdapter agendaAdapter;

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;

    public MessesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_messes, container, false);

        setHasOptionsMenu(true);

        if (getArguments() != null) {
            this.mes = getArguments().getInt("mes");
        }

        recyclerView = view.findViewById(R.id.recycle_view_meses_fragments);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        //recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        atualizarView();
        //runLayoutAnimation(recyclerView);

        return  view;
    }

    private void atualizarView() {

        //datas = pegarDatasDoDomingos(mes, ano);
        datasList = pegarListaDataDoDomingos(mes, ano);

        //adapter = new DatasAdapter(getContext(), datas);
        agendaAdapter = new AgendaAdapter(getContext(), datasList);

        recyclerView.setAdapter(agendaAdapter);
    }

    public ArrayList pegarDatasDoDomingos(int mes, int ano) {

        // cria um calendário na data 01/mes/ano
        Calendar c = new GregorianCalendar(ano, mes, 1);
        // Pega a Data e formata de Acordo com a Região Ex: 00/00/00
        //DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
        DateFormat df = DateFormat.getDateInstance(DateFormat.LONG);

        do {
            // o dia da semana ecolhido é domingo?
            if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                datas.add(String.valueOf(df.format(c.getTime())));
            }
            // incrementa um dia no calendário
            c.roll(Calendar.DAY_OF_MONTH, true);

            // enquanto o dia do mês atual for diferente de 1
        } while (c.get(Calendar.DAY_OF_MONTH) != 1);

        return datas;
    }

    public List<Date> pegarListaDataDoDomingos(int mes, int ano) {

        // cria um calendário na data 01/mes/ano
        Calendar c = new GregorianCalendar(ano, mes, 1);
        // Pega a Data e formata de Acordo com a Região Ex: 00/00/00
        //DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
        DateFormat df = DateFormat.getDateInstance(DateFormat.LONG);

        do {
            // o dia da semana ecolhido é domingo?
            if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                //datas.add(String.valueOf(df.format(c.getTime())));
                datasList.add(c.getTime());
            }
            // incrementa um dia no calendário
            c.roll(Calendar.DAY_OF_MONTH, true);

            // enquanto o dia do mês atual for diferente de 1
        } while (c.get(Calendar.DAY_OF_MONTH) != 1);

        return datasList;
    }

    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem item = menu.findItem(R.id.spinner);
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
                datas.clear();

                atualizarView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }*/
}
