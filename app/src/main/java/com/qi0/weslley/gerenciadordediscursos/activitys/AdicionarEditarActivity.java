package com.qi0.weslley.gerenciadordediscursos.activitys;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.qi0.weslley.gerenciadordediscursos.R;
import com.qi0.weslley.gerenciadordediscursos.fragments.AddEditarOradorFragment;

public class AdicionarEditarActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_editar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_pricipal);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String fragmentAbrir = (String) getIntent().getCharSequenceExtra("qualFragmentAbrir");

        if (fragmentAbrir.equals("AddCongregacaoFragment")){
            replaceFragment(R.id.fragment_container_add_editar, new Fragment());
            getSupportActionBar().setTitle("Nova Congregação");
        }else if (fragmentAbrir.equals("AddOradorFragment")){
            replaceFragment(R.id.fragment_container_add_editar, new AddEditarOradorFragment());
            getSupportActionBar().setTitle("Novo Orador");
        }else if (fragmentAbrir.equals("AddDiscursosFragment")){
            replaceFragment(R.id.fragment_container_add_editar, new Fragment());
            getSupportActionBar().setTitle("Novo Discurso");
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add_editar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AdicionarEditarActivity.this, "Salvando...", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

}
