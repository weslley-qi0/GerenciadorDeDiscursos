package com.qi0.weslley.gerenciadordediscursos.fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.qi0.weslley.gerenciadordediscursos.config.ConfiguracaoFirebase;
import com.qi0.weslley.gerenciadordediscursos.R;
import com.qi0.weslley.gerenciadordediscursos.activitys.AdicionarEditarActivity;
import com.qi0.weslley.gerenciadordediscursos.activitys.DetalheActivity;
import com.qi0.weslley.gerenciadordediscursos.adapter.OradorAdaper;
import com.qi0.weslley.gerenciadordediscursos.helper.RecyclerItemClickListener;
import com.qi0.weslley.gerenciadordediscursos.model.Congregacao;
import com.qi0.weslley.gerenciadordediscursos.model.Orador;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 */
public class OradoresFragment extends BaseFragment {

    ImageView imgOradoresEmpty;
    TextView msgOradoresEmpty;

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    OradorAdaper adapter;
    Orador oradorSelecionado;

    ArrayList oradoresList = new ArrayList();
    ArrayList congregacaoList = new ArrayList();

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    StorageReference storageReference;
    ValueEventListener valueEventListenerOradores;
    String userUID;

    public OradoresFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_oradores, container, false);

        Toolbar toolbarOradores = view.findViewById(R.id.toolbar_pricipal);
        toolbarOradores.setTitle("Oradores");

        setHasOptionsMenu(true);

        databaseReference = ConfiguracaoFirebase.getFirebaseDatabase();
        firebaseAuth = ConfiguracaoFirebase.getAuth();

        userUID = firebaseAuth.getCurrentUser().getUid();

        imgOradoresEmpty = view.findViewById(R.id.img_oradores_empty);
        msgOradoresEmpty = view.findViewById(R.id.msg_img_oradores_empty_empty);
        recyclerView = view.findViewById(R.id.recycle_view_oradores);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        //recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        adapter = new OradorAdaper(oradoresList, congregacaoList, getContext());

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
                oradorSelecionado = (Orador) oradoresList.get(position);
                Intent intentOradorDealhes = new Intent(getActivity(), DetalheActivity.class);
                intentOradorDealhes.putExtra("qualFragmentAbrir", "DetalheOradorFragment");
                intentOradorDealhes.putExtra("orador", oradorSelecionado);
                startActivity(intentOradorDealhes);
            }

            @Override
            public void onLongItemClick(View view, int position) {
                oradorSelecionado = (Orador) oradoresList.get(position);
                showPopup(view);
            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        }));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        pegarCongegacoesDoBanco();
        pegarOradores();
    }

    @Override
    public void onStop() {
        super.onStop();
        databaseReference.removeEventListener(valueEventListenerOradores);
    }

    private void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    private void pegarOradores() {

        valueEventListenerOradores = databaseReference.child("user_data")
                             .child(userUID)
                             .child("oradores")
                             .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                oradoresList.clear();
                for (DataSnapshot dados : dataSnapshot.getChildren()){
                    Orador orador = dados.getValue(Orador.class);
                    oradoresList.add(orador);
                    Collections.sort(oradoresList);
                }

                adapter.notifyDataSetChanged();

                if (oradoresList.size() == 0){
                    recyclerView.setVisibility(View.GONE);
                    imgOradoresEmpty.setVisibility(View.VISIBLE);
                    msgOradoresEmpty.setVisibility(View.VISIBLE);
                }else {
                    recyclerView.setVisibility(View.VISIBLE);
                    imgOradoresEmpty.setVisibility(View.GONE);
                    msgOradoresEmpty.setVisibility(View.GONE);
                }
                //getSupportActionBar().setTitle(String.valueOf(oradoresList.size()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void pegarCongegacoesDoBanco(){

        DatabaseReference databaseReference;
        databaseReference = ConfiguracaoFirebase.getFirebaseDatabase();

        databaseReference.child("user_data").child(userUID).child("congregacoes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                congregacaoList.clear();
                for (DataSnapshot dados : dataSnapshot.getChildren()){
                    Congregacao congregacao = dados.getValue(Congregacao.class);
                    congregacaoList.add(congregacao);
                    Collections.sort(congregacaoList);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void deleteImagePerfilOrador() {

        storageReference = ConfiguracaoFirebase.getFirebaseStorage();

        StorageReference imagemRef = storageReference
                .child("imagens")
                .child(userUID)
                .child("orador_perfil")
                .child(oradorSelecionado.getId() + ".jpeg");

        imagemRef.delete().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });
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
                        Intent intentEditarOrador = new Intent(getActivity(), AdicionarEditarActivity.class);
                        intentEditarOrador.putExtra("qualFragmentAbrir", "AddOradorFragment");
                        intentEditarOrador.putExtra("oradorSelecionado", oradorSelecionado);
                        startActivity(intentEditarOrador);
                        return true;
                    case R.id.item_deletar:
                        databaseReference.child("user_data").child(userUID).child("oradores").child(oradorSelecionado.getId()).removeValue();
                        deleteImagePerfilOrador();
                        Toasty.success(getContext(), "Orador Deletado", Toast.LENGTH_SHORT).show();
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
