package com.qi0.weslley.gerenciadordediscursos.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.qi0.weslley.gerenciadordediscursos.R;
import com.qi0.weslley.gerenciadordediscursos.fragments.DetalheOradorFragment;
import com.qi0.weslley.gerenciadordediscursos.fragments.ProferimentosDetalheFragment;
import com.qi0.weslley.gerenciadordediscursos.model.Orador;

public class DetalheActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe);

        /*Toolbar toolbar_spinner = (Toolbar) findViewById(R.id.toolbar_spinner);
        setSupportActionBar(toolbar_spinner);*/
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setElevation(0);

        String fragmentAbrir = (String) getIntent().getCharSequenceExtra("qualFragmentAbrir");

        Intent intent = getIntent();
        Orador oradorSelecionado = (Orador) intent.getSerializableExtra("orador");

        if (fragmentAbrir.equals("DetalheCongregacaoFragment")){
            replaceFragment(R.id.fragment_container_detalhe, new Fragment());
            //getSupportActionBar().setTitle("Detalhe Congregação");

        }else if (fragmentAbrir.equals("DetalheOradorFragment")){

            Bundle bundle = new Bundle();bundle.putSerializable("oradorSelecionado", oradorSelecionado);
            DetalheOradorFragment detalheOradorFragment = new DetalheOradorFragment();
            detalheOradorFragment.setArguments(bundle);
            replaceFragment(R.id.fragment_container_detalhe, detalheOradorFragment);
            //getSupportActionBar().setTitle("Detalhe Orador");

        }else if (fragmentAbrir.equals("DetalheDiscursoFragment")){
            replaceFragment(R.id.fragment_container_detalhe, new Fragment());
            //getSupportActionBar().setTitle("Detalhe Discurso");
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_detalhe);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}
