package com.qi0.weslley.gerenciadordediscursos.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.qi0.weslley.gerenciadordediscursos.R;
import com.qi0.weslley.gerenciadordediscursos.activitys.MainActivity;
import com.qi0.weslley.gerenciadordediscursos.adapter.TabsAdapter;
import com.qi0.weslley.gerenciadordediscursos.config.ConfiguracaoFirebase;
import com.qi0.weslley.gerenciadordediscursos.model.Agenda;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AgendaFragment extends BaseFragment {


    final static String KEY_PREFERENCE = "keyPreference";
    SmartTabLayout viewPagerTab;
    ViewPager viewPager;
    TabsAdapter adapter;
    Toolbar toolbarAgenda;
    Spinner spinner;
    ArrayAdapter<String> spinneradapter;
    List<String> anos;
    ArrayList<Integer> anosList;

    Calendar calendar;
    SharedPreferences sharedPreferences;

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    ValueEventListener valueEventListenerAgenda;
    String userUID;

    int year;
    int anoSpinner;
    private FragmentManager childFragMang;//ToDo

    public AgendaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_agenda, container, false);

        toolbarAgenda = view.findViewById(R.id.toolbar_pricipal);

        ((MainActivity) getActivity()).getSupportActionBar().setCustomView(toolbarAgenda);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Home");

        setHasOptionsMenu(true);

        viewPager = view.findViewById(R.id.viewpager);
        viewPagerTab = view.findViewById(R.id.viewPagerTab);

        databaseReference = ConfiguracaoFirebase.getFirebaseDatabase();
        firebaseAuth = ConfiguracaoFirebase.getAuth();
        userUID = firebaseAuth.getCurrentUser().getUid();
        sharedPreferences = getActivity().getSharedPreferences(KEY_PREFERENCE, Context.MODE_PRIVATE);

        calendar = Calendar.getInstance(); //Cria uma Instancia de Calendar
        year = calendar.get(Calendar.YEAR);
        anos = new ArrayList<>();
        anoSpinner = sharedPreferences.getInt("anoSelecionado", year);
        pegarValoresDaAgendaNoBanco();
        //childFragMang = getChildFragmentManager();//ToDo

        spinner = view.findViewById(R.id.spinner_ano);

        spinneradapter = new ArrayAdapter<String>(getActivity(),
                R.layout.custom_spinner_item, anos){

            // Metodo que retorna a Vie do Spinner
            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;

                if(position == anos.indexOf("Nova Agenda")) {
                    tv.setTypeface(Typeface.DEFAULT_BOLD);
                    tv.setBackgroundColor(getResources().getColor(R.color.white));
                    tv.setTextColor(getResources().getColor(R.color.colorAccent));
                }else {
                    tv.setTypeface(Typeface.DEFAULT);
                    tv.setBackgroundResource(R.drawable.spinner_item_border);
                }

                if (position == anos.indexOf("Agenda " + anoSpinner)){
                    tv.setTextColor(getResources().getColor(R.color.colorAccent));
                    //tv.setBackgroundColor(getResources().getColor(R.color.backgroud_recycle_agenda));
                    //tv.setTypeface(Typeface.DEFAULT_BOLD);
                }else {
                    tv.setBackgroundResource(R.drawable.spinner_item_border);
                }

                return view;
            }
        };

        spinneradapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        spinner.setAdapter(spinneradapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                switch (spinner.getSelectedItem().toString()) {
                    case "Nova Agenda":
                        addNovaAgenda();
                        break;
                }

                if (!spinner.getSelectedItem().toString().equals("Nova Agenda")) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("anoSelecionado", Integer.parseInt(spinner.getSelectedItem().toString().substring(7, 11)));
                    editor.apply();
                }

                //spinner.setSelection(anos.indexOf("Agenda " + anoSpinner));
                /*if (!spinner.getSelectedItem().toString().equals("Nova Agenda")){
                    anoSpinner = Integer.parseInt(spinner.getSelectedItem().toString().substring(7, 11));
                    //anoSpinner = Integer.parseInt(anoClicado.substring(7, 11));
                }*/
                ///anoSpinner = anos.indexOf("Agenda " + anoSpinner);

                //setupViewPager(viewPager);
                //adapter.notifyDataSetChanged();
                atualizarView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

        });
        return view;
    }

    private void atualizarView() {
        if (!isAdded()) return;
        calendar = Calendar.getInstance();
        int currentMes = calendar.get(Calendar.MONTH);
        anoSpinner = sharedPreferences.getInt("anoSelecionado", year);
        //setupViewPager(viewPager);
        adapter = new TabsAdapter(getContext(), getChildFragmentManager());
        viewPager.setAdapter(adapter);
        viewPagerTab.setViewPager(viewPager);
        viewPager.setCurrentItem(currentMes);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        atualizarView();
    }

    private void setupViewPager(ViewPager viewPager) {
        if (!isAdded()) return;
        adapter = new TabsAdapter(getContext(), getChildFragmentManager());
        //adapter = new TabsAdapter(getContext(), childFragMang);
        viewPager.setAdapter(adapter);
    }

    private void addNovaAgenda() {

        if (anosList == null || anosList.size() == 0 || anosList.size() <= 7) {
            initList(year - 2, sharedPreferences.getInt("ultimoAnoList", year + 2));
        }

        for (String ano : anos) {
            if (!ano.equals("Nova Agenda")) {
                int a = Integer.parseInt(ano.substring(7, 11));
                if (anosList.contains(a)) {
                    anosList.remove(anosList.indexOf(a));
                }
            }
        }

        if (anosList.size() == 0){
            initList(year - 2, Integer.parseInt(anos.get(anos.size() - 2).substring(7, 11)) + 5);
            for (String ano : anos) {
                if (!ano.equals("Nova Agenda")) {
                    int a = Integer.parseInt(ano.substring(7, 11));
                    if (anosList.contains(a)) {
                        anosList.remove(anosList.indexOf(a));
                    }
                }
            }
        }

        Integer[] arrayAnos = anosList.toArray(new Integer[anosList.size()]);
        final String[] arrayAnosString = new String[arrayAnos.length];

        for (int i = 0; i < arrayAnos.length; i++) {
            arrayAnosString[i] = String.valueOf(arrayAnos[i]);
        }

        AlertDialog.Builder alertbox = new AlertDialog.Builder(getContext());
        alertbox.setTitle("Escolha uma Ano")
                .setItems(arrayAnosString, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String valor = String.valueOf(anosList.get(which));
                        criarAgenda(anosList.get(which));

                        anos.add("Agenda " + valor);
                        Collections.sort(anos);
                        anoSpinner = Integer.parseInt(valor);
                        //spinneradapter.notifyDataSetChanged();

                        sharedPreferences = getActivity().getSharedPreferences(KEY_PREFERENCE, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("anoSelecionado", anosList.get(which));
                        editor.apply();

                        //int position = anos.indexOf("Agenda " + valor);
                        //spinner.setSelection(position);
                        int novoAno = anosList.get(anosList.size() - 1);
                        anosList.add(novoAno + 1);

                        //sharedPreferences = getActivity().getSharedPreferences(KEY_PREFERENCE, Context.MODE_PRIVATE);
                        //SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("ultimoAnoList", anosList.get(anosList.size() - 1));
                        editor.apply();

                        //setupViewPager(viewPager);
                        //adapter.notifyDataSetChanged();
                        atualizarView();
                    }
                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (anos.contains("Agenda " + anoSpinner)) {
                    int position = anos.indexOf("Agenda " + anoSpinner);
                    spinner.setSelection(position);
                } else if (anos.contains("Agenda " + year)) {
                    int position = anos.indexOf("Agenda " + year);
                    spinner.setSelection(position);
                } else {
                    spinner.setSelection(0);
                }
            }
        });

        alertbox.show();
    }

    private void initList(int start, int last) {
        anosList = new ArrayList<>();
        for (int i = start; i <= last; i++) {
            anosList.add(i);
        }
    }

    public void pegarValoresDaAgendaNoBanco() {

        valueEventListenerAgenda = databaseReference
                .child("user_data")
                .child(userUID)
                .child("agenda")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        anos.clear();
                        for (DataSnapshot dados : dataSnapshot.getChildren()) {
                            String anoDataBase = dados.getKey();
                            anos.add("Agenda " + anoDataBase);
                        }
                        Collections.sort(anos);
                        anos.add("Nova Agenda");
                        spinneradapter.notifyDataSetChanged();

                        if (anos.contains("Agenda " + anoSpinner)) {
                            int position = anos.indexOf("Agenda " + anoSpinner);
                            spinner.setSelection(position);
                        } else if (anos.contains("Agenda " + year)) {
                            int position = anos.indexOf("Agenda " + year);
                            spinner.setSelection(position);
                        } else {
                            spinner.setSelection(0);
                        }

                        //setupViewPager(viewPager);
                        //adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        atualizarView();
    }

    private void criarAgenda(int ano) {
        //Calendar calendar = Calendar.getInstance();
        //int ano = calendar.get(Calendar.YEAR);
        for (int i = 0; i <= 11; i++) {
            criarAgendaNoBanco(i, ano);
        }
    }

    public void criarAgendaNoBanco(int mes, int ano) {
        userUID = firebaseAuth.getCurrentUser().getUid();
        Agenda agenda = new Agenda();

        // cria um calendário na data 01/mes/ano
        Calendar c = new GregorianCalendar(ano, mes, 1);
        // Pega a Data e formata de Acordo com a Região Ex: 00/00/00
        //DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);

        do {
            // o dia da semana ecolhido é domingo?
            if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                SimpleDateFormat dataSDF = new SimpleDateFormat("dd-MM-yyyy");
                SimpleDateFormat mesSDF = new SimpleDateFormat("MM");
                String dataFormatada = dataSDF.format(c.getTime());
                String mesFormatado = mesSDF.format(c.getTime());

                agenda.setData(dataFormatada);
                agenda.setIdCongregacao("");
                agenda.setIdOrador("");
                agenda.setIdDiscurso("");

                databaseReference.child("user_data")
                        .child(userUID)
                        .child("agenda")
                        .child(String.valueOf(ano))
                        .child(mesFormatado)
                        .child(dataFormatada)
                        .setValue(agenda);

            }
            // incrementa um dia no calendário
            c.roll(Calendar.DAY_OF_MONTH, true);

            // enquanto o dia do mês atual for diferente de 1
        } while (c.get(Calendar.DAY_OF_MONTH) != 1);
    }
}
