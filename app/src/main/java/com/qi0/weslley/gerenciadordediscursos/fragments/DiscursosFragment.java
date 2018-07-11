package com.qi0.weslley.gerenciadordediscursos.fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.qi0.weslley.gerenciadordediscursos.activitys.DetalheActivity;
import com.qi0.weslley.gerenciadordediscursos.activitys.MainActivity;
import com.qi0.weslley.gerenciadordediscursos.config.ConfiguracaoFirebase;
import com.qi0.weslley.gerenciadordediscursos.R;
import com.qi0.weslley.gerenciadordediscursos.activitys.AdicionarEditarActivity;
import com.qi0.weslley.gerenciadordediscursos.adapter.DiscursoAdapter;
import com.qi0.weslley.gerenciadordediscursos.helper.RecyclerItemClickListener;
import com.qi0.weslley.gerenciadordediscursos.model.Congregacao;
import com.qi0.weslley.gerenciadordediscursos.model.Discurso;
import com.qi0.weslley.gerenciadordediscursos.model.Orador;
import com.qi0.weslley.gerenciadordediscursos.model.Proferimento;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiscursosFragment extends BaseFragment {

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    DiscursoAdapter adapter;
    Discurso discursoSelecionado;
    ImageView imgDiscursosEmpty;
    TextView msgDiscursosEmpty;

    public ArrayList<Discurso> discursosList = new ArrayList();
    List<Proferimento> proferimentosList = new ArrayList();

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    ValueEventListener valueEventListenerDiscursos;
    String userUID;

    public DiscursosFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseReference = ConfiguracaoFirebase.getFirebaseDatabase();
        firebaseAuth = ConfiguracaoFirebase.getAuth();
        userUID = firebaseAuth.getCurrentUser().getUid();
        pegarDiscursosDoBanco();
        pegarUltimoProferimestosDiscurso();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_discursos, container, false);

        Toolbar toolbarDiscursos = view.findViewById(R.id.toolbar_pricipal);
        ((MainActivity) getActivity()).setSupportActionBar(toolbarDiscursos);
        toolbarDiscursos.setTitle("Discursos");

        //databaseReference = ConfiguracaoFirebase.getFirebaseDatabase();
        //firebaseAuth = ConfiguracaoFirebase.getAuth();
        //userUID = firebaseAuth.getCurrentUser().getUid();

        setHasOptionsMenu(true);

        imgDiscursosEmpty = view.findViewById(R.id.img_discusos_empty);
        msgDiscursosEmpty = view.findViewById(R.id.msg_discurso_empty);
        recyclerView = view.findViewById(R.id.recycle_view_discursos);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        adapter = new DiscursoAdapter(discursosList, proferimentosList, getContext());

        recyclerView.setAdapter(adapter);

        runLayoutAnimation(recyclerView);

        scrollFabHideShow();

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                List<Discurso> discursosListaAtualizada = adapter.getDiscursos();
                discursoSelecionado = (Discurso) discursosListaAtualizada.get(position);
                Intent intentDiscursoDealhes = new Intent(getActivity(), DetalheActivity.class);
                intentDiscursoDealhes.putExtra("qualFragmentAbrir", "DetalheDiscursoFragment");
                intentDiscursoDealhes.putExtra("discursoSelecionado", discursoSelecionado);
                startActivity(intentDiscursoDealhes);
            }

            @Override
            public void onLongItemClick(View view, int position) {
                List<Discurso> discursosListaAtualizada = adapter.getDiscursos();
                discursoSelecionado = (Discurso) discursosListaAtualizada.get(position);
                showPopup(view);
            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        }));

        return view;
    }

    private void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    private void scrollFabHideShow() {
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
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
        databaseReference.removeEventListener(valueEventListenerDiscursos);
    }

    public void pegarDiscursosDoBanco() {

        valueEventListenerDiscursos = databaseReference.child("user_data")
                .child(userUID)
                .child("discursos")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                discursosList.clear();
                for (DataSnapshot dados : dataSnapshot.getChildren()){
                    Discurso discurso = dados.getValue(Discurso.class);
                    discursosList.add(discurso);
                    Collections.sort(discursosList);
                }
                adapter.notifyDataSetChanged();

                if (discursosList.size() == 0){
                    recyclerView.setVisibility(View.GONE);
                    imgDiscursosEmpty.setVisibility(View.VISIBLE);
                    msgDiscursosEmpty.setVisibility(View.VISIBLE);
                }else {
                    recyclerView.setVisibility(View.VISIBLE);
                    imgDiscursosEmpty.setVisibility(View.GONE);
                    msgDiscursosEmpty.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void pegarUltimoProferimestosDiscurso() {

        databaseReference = ConfiguracaoFirebase.getFirebaseDatabase();
        databaseReference.child("user_data")
                .child(userUID)
                .child("proferimentos")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        proferimentosList.clear();
                        for (DataSnapshot dados : dataSnapshot.getChildren()){
                            Proferimento proferimento = dados.getValue(Proferimento.class);
                            proferimentosList.add(proferimento);
                        }
                        Collections.sort(proferimentosList, new Comparator<Proferimento>() {
                                    @Override
                                    public int compare(Proferimento o1, Proferimento o2) {
                                        return o1.getDataOrdenarProferimento().compareTo(o2.getDataOrdenarProferimento());
                                    }
                                }
                        );
                        Collections.reverse(proferimentosList);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    public void pequisarDiscursos(String texto){

        ArrayList<Discurso> discursosListaPesquisa = new ArrayList<>();

        for (Discurso discurso : discursosList) {

            String numeroDiscurso = String.valueOf(discurso.getNumero());
            String temaDiscurso = discurso.getTema().toLowerCase();

            if (numeroDiscurso.contains(texto) || temaDiscurso.contains(texto)){
                discursosListaPesquisa.add(discurso);
            }
        }

        adapter = new DiscursoAdapter(discursosListaPesquisa, proferimentosList, getContext());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void recaregarDiscursos(){
        adapter = new DiscursoAdapter(discursosList, proferimentosList, getContext());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @SuppressLint("RestrictedApi")
    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(getContext(), v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_recycle_view, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_editar:
                        Intent intentEdtarDiscurso = new Intent(getActivity(), AdicionarEditarActivity.class);
                        intentEdtarDiscurso.putExtra("qualFragmentAbrir", "AddDiscursosFragment");
                        intentEdtarDiscurso.putExtra("discursoSelecionado", discursoSelecionado);
                        startActivity(intentEdtarDiscurso);
                        return true;
                    case R.id.item_deletar:
                        databaseReference.child("user_data").child(userUID).child("discursos").child(discursoSelecionado.getIdDiscurso()).removeValue();
                        recaregarDiscursos();
                        Toasty.success(getContext(), "Discurso Deletado", Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        return false;
                }
            }
        });
        @SuppressLint("RestrictedApi") MenuPopupHelper menuHelper = new MenuPopupHelper(getContext(), (MenuBuilder) popup.getMenu(), v);
        menuHelper.setForceShowIcon(true);
        menuHelper.setGravity(Gravity.END);
        menuHelper.show();
    }

}
